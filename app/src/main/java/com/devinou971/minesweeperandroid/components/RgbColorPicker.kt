package com.devinou971.minesweeperandroid.components

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.devinou971.minesweeperandroid.R
import com.google.android.material.slider.Slider

class RgbColorPicker(context: Context) : LinearLayout(context) {
    private val redSlider: Slider
    private val greenSlider: Slider
    private val blueSlider: Slider

    private val colorPreview: View

    var red: Int
        get() = redSlider.value.toInt()
        set(value) {
            redSlider.value = value.toFloat()
            colorPreview.setBackgroundColor(color)
        }

    var green: Int
        get() = greenSlider.value.toInt()
        set(value) {
            greenSlider.value = value.toFloat()
            colorPreview.setBackgroundColor(color)
        }

    var blue: Int
        get() = blueSlider.value.toInt()
        set(value) {
            blueSlider.value = value.toFloat()
            colorPreview.setBackgroundColor(color)
        }

    var color: Int
        @ColorInt get() = Color.rgb(redSlider.value, greenSlider.value, blueSlider.value)
        set(@ColorInt value) {
            Color.valueOf(value).apply {
                red = (red() * 255).toInt()
                green = (green() * 255).toInt()
                blue = (blue() * 255).toInt()
            }

            colorPreview.setBackgroundColor(value)
        }

    init {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.rgb_color_picker, this)

        redSlider = findViewById(R.id.red_slider)
        redSlider.addOnChangeListener(SliderAction())
        greenSlider = findViewById(R.id.green_slider)
        greenSlider.addOnChangeListener(SliderAction())
        blueSlider = findViewById(R.id.blue_slider)
        blueSlider.addOnChangeListener(SliderAction())
        colorPreview = findViewById(R.id.color_preview)
    }

    constructor(context: Context, @ColorInt color: Int) : this(context) {
        this.color = color
    }

    private inner class SliderAction : Slider.OnChangeListener {
        override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
            colorPreview.setBackgroundColor(Color.rgb(red, green, blue))
        }
    }
}