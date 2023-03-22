package com.devinou971.minesweeperandroid

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.slider.Slider
import kotlin.reflect.KFunction1

class ParametersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)

        val root = findViewById<LinearLayout>(R.id.colorsLayout)

        for (i in 0 until Settings.colors.size)
            createColorPicker(root, i, Settings.colors[i])
    }

    private fun createColorPicker(layout: ViewGroup, index: Int, color: Int) =
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

                setBackgroundColor(color)

                val thisButton = this
                setOnClickListener {
                    AlertDialog.Builder(this@ParametersActivity).apply {
                        setTitle("Color picker")

                        fun slider(label: String) =
                            LinearLayout(this@ParametersActivity).apply {
                                orientation = LinearLayout.HORIZONTAL

                                addView(Slider(this@ParametersActivity).apply {
                                    layoutParams =
                                        LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)

                                    valueTo = 255f
                                    stepSize = 1f

                                    var c = 0
                                    var gc: KFunction1<Int, Int>? = null

                                    when (label) {
                                        "Red" -> {
                                            c = Color.RED
                                            gc = Color::red
                                        }
                                        "Green" -> {
                                            c = Color.GREEN
                                            gc = Color::green
                                        }
                                        "Blue" -> {
                                            c = Color.BLUE
                                            gc = Color::blue
                                        }
                                    }

                                    thumbTintList = ColorStateList.valueOf(c)
                                    trackTintList = ColorStateList.valueOf(c)
                                    value = gc!!.invoke(color).toFloat()
                                })

                                addView(TextView(this@ParametersActivity).apply {
                                    layoutParams =
                                        LayoutParams(0, LayoutParams.MATCH_PARENT, .25f)
                                    text = label
                                    gravity = Gravity.CENTER

                                    setTextColor(
                                        when (label) {
                                            "Red" -> Color.RED
                                            "Green" -> Color.GREEN
                                            "Blue" -> Color.BLUE
                                            else -> currentTextColor
                                        }
                                    )
                                })
                            }

                        val redSlider = slider("Red")
                        val greenSlider = slider("Green")
                        val blueSlider = slider("Blue")

                        setView(LinearLayout(this@ParametersActivity).apply {
                            orientation = LinearLayout.VERTICAL
                            setPadding(50, paddingTop, 50, paddingBottom)

                            addView(redSlider)
                            addView(greenSlider)
                            addView(blueSlider)
                        })

                        setPositiveButton("Valider") { _, _ ->
                            val newColor = Color.rgb(
                                (redSlider[0] as Slider).value,
                                (greenSlider[0] as Slider).value,
                                (blueSlider[0] as Slider).value,
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

                //isClickable = false // ToDo
            }

            colorText.apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                hint = "RRGGBB"
                maxLines = 1
                filters = arrayOf(InputFilter.LengthFilter(6))
                inputType = EditorInfo.TYPE_CLASS_TEXT
                println(color)
                setText(
                    (color and 0xff000000.inv().toInt()).toString(16)
                        .padStart(6, '0')
                )

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