package com.devinou971.minesweeperandroid

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class ParametersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)

        val root = findViewById<LinearLayout>(R.id.mainLayout)

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

            val colorButton = Button(this@ParametersActivity).apply {
                layoutParams =
                    LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                        topMargin = 10
                        bottomMargin = 10
                    }
                isClickable = false
                setBackgroundColor(color)
            }

            addView(colorButton)

            addView(EditText(this@ParametersActivity).apply {
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


                    val newColor = Color.parseColor(
                        "#" + when (it.length) {
                            6 -> it.toString()
                            3 -> it[0].toString().repeat(2) +
                                    it[1].toString().repeat(2) +
                                    it[2].toString().repeat(2)
                            else -> return@doAfterTextChanged
                        }
                    )

                    colorButton.setBackgroundColor(newColor)
                    Settings.colors[index] = newColor
                    Settings.save(context)
                }
            })
        })
}