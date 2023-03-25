package com.devinou971.minesweeperandroid

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devinou971.minesweeperandroid.adapters.ColorPickersAdapter
import com.devinou971.minesweeperandroid.components.RgbColorPicker
import com.devinou971.minesweeperandroid.extensions.startThread
import com.devinou971.minesweeperandroid.extensions.toColorString
import com.devinou971.minesweeperandroid.storageclasses.AppDatabase

class ParametersActivity : AppCompatActivity() {
    private lateinit var colorsList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)

        colorsList = findViewById<RecyclerView>(R.id.colors_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ColorPickersAdapter(Settings.colors)
        }

        findViewById<Button>(R.id.clear_settings).setOnClickListener {
            Settings.reset()
            colorsList.adapter = ColorPickersAdapter(Settings.colors)
        }
        findViewById<Button>(R.id.clear_data).setOnClickListener {
            startThread {
                AppDatabase.getAppDataBase(this).clearAllTables()

                runOnUiThread {
                    Toast.makeText(this, "Data removed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Use the ColorPickerAdapter")
    private fun createColorPicker(layout: ViewGroup, index: Int) =
        layout.addView(LinearLayout(this).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL

            addView(TextView(this@ParametersActivity).apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                text = "${index + 1}"
                textSize = 25f
            })

            addView(Space(this@ParametersActivity).apply {
                layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
            })

            fun updateColor(colorButton: Button, newColor: Int) {
                colorButton.setBackgroundColor(newColor)
                Settings.colors[index] = newColor
                Settings.save(context)
            }

            val colorButton = Button(this@ParametersActivity)
            val colorText = EditText(this@ParametersActivity)

            colorButton.apply {
                layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                        topMargin = 10
                        bottomMargin = 10
                    }

                setBackgroundColor(Settings.colors[index])

                val thisButton = this
                setOnClickListener {
                    AlertDialog.Builder(this@ParametersActivity).apply {
                        setTitle("Color picker")

                        val colorPicker =
                            RgbColorPicker(this@ParametersActivity, Settings.colors[index])

                        setView(colorPicker)

                        setPositiveButton("Valider") { _, _ ->
                            val newColor = Color.rgb(
                                colorPicker.red,
                                colorPicker.green,
                                colorPicker.blue
                            )

                            updateColor(thisButton, newColor)
                            colorText.setText(
                                (newColor and 0xff000000.inv().toInt()).toString(16)
                                    .padStart(6, '0')
                            )
                        }

                        setNegativeButton("Annuler") { _, _ -> }
                    }.create().show()
                }
            }

            colorText.apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                hint = "RRGGBB"
                maxLines = 1
                filters = arrayOf(InputFilter.LengthFilter(6))
                inputType = EditorInfo.TYPE_CLASS_TEXT

                setText(Settings.colors[index].toColorString())

                doAfterTextChanged {
                    if (it == null)
                        return@doAfterTextChanged

                    updateColor(
                        colorButton, Color.parseColor(
                            "#" + when (it.length) {
                                6 -> it.toString()
                                3 -> it[0].toString().repeat(2) +
                                        it[1].toString().repeat(2) +
                                        it[2].toString().repeat(2)
                                else -> return@doAfterTextChanged
                            }
                        )
                    )
                }
            }

            addView(colorButton)
            addView(colorText)
        })
}