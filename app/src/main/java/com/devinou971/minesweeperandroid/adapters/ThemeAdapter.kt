package com.devinou971.minesweeperandroid.adapters

import android.graphics.Bitmap
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.devinou971.minesweeperandroid.R
import com.devinou971.minesweeperandroid.extensions.CpxUnit
import com.devinou971.minesweeperandroid.extensions.applyDim

class ThemeAdapter(private var array: Array<Pair<Bitmap, OnClickListener>>,
                   private var currentPosition: Int) :
    RecyclerView.Adapter<ThemeAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ImageView(parent.context).apply {
            layoutParams = LayoutParams(50 applyDim CpxUnit.DIP, 50 applyDim CpxUnit.DIP)
        }
    )

    override fun getItemCount() = array.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView as ImageView).apply {
            setImageBitmap(array[position].first)
            drawable.isFilterBitmap = false
            setOnClickListener(array[position].second)

            if (position == currentPosition)
                setBackgroundColor(context.getColor(R.color.gainsboro))
        }

    }
}