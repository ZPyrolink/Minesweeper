package com.devinou971.minesweeperandroid.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.Settings
import com.devinou971.minesweeperandroid.components.RgbColorPicker
import com.devinou971.minesweeperandroid.extensions.AlertDialogExt
import com.devinou971.minesweeperandroid.extensions.toColorString

class ColorPickersAdapter(private val array: IntArray) :
    RecyclerView.Adapter<ColorPickersAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idT: TextView
        val button: Button
        val text: EditText

        init {
            itemView.apply {
                idT = findViewById(R.id.cpi_id)
                button = findViewById(R.id.cpi_button)
                text = findViewById(R.id.cpi_text)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.color_picker_item, parent, false)
        )

    override fun getItemCount() = array.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            fun updateColor(colorButton: Button, newColor: Int) {
                colorButton.setBackgroundColor(newColor)
                Settings.colors[position] = newColor
                Settings.save(holder.itemView.context)
            }

            idT.text = "${position + 1}"

            button.apply {
                setBackgroundColor(array[position])
                setOnClickListener {
                    val colorPicker = RgbColorPicker(context, array[position])

                    AlertDialogExt.createAndShow(
                        context, "Color picker", colorPicker,
                        Pair(context.getString(android.R.string.ok)) { _, _ ->
                            val newColor = Color.rgb(
                                colorPicker.red,
                                colorPicker.green,
                                colorPicker.blue
                            )

                            updateColor(this, newColor)
                            holder.text.setText(
                                (newColor and 0xff000000.inv().toInt()).toString(16)
                                    .padStart(6, '0')
                            )
                        },
                        Pair(context.getString(android.R.string.cancel), null)
                    )
                }
            }

            text.apply {
                setText(array[position].toColorString())
                doAfterTextChanged {
                    if (it == null)
                        return@doAfterTextChanged

                    setTextColor(
                        resources.getColor(
                            android.R.color.primary_text_dark,
                            context.theme
                        )
                    )

                    try {
                        updateColor(
                            button, Color.parseColor(
                                "#" + when (it.length) {
                                    6 -> it.toString()
                                    3 -> it[0].toString().repeat(2) +
                                            it[1].toString().repeat(2) +
                                            it[2].toString().repeat(2)
                                    else -> {
                                        setTextColor(Color.RED)
                                        return@doAfterTextChanged
                                    }
                                }
                            )
                        )
                    } catch (e: NumberFormatException) {
                        setTextColor(Color.RED)
                    }
                }
            }
        }
    }
}