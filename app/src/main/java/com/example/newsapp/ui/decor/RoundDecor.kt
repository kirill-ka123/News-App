package com.example.newsapp.ui.decor

//import android.graphics.Canvas
//import android.view.View
//import androidx.recyclerview.widget.RecyclerView
//
//class RoundDecor(
//    private val cornerRadius: Float,
//    private val roundPolitic: RoundPolitic = RoundPolitic.Every(RoundMode.ALL)
//) : Decorator.ViewHolderDecor {
//
//    override fun draw(
//        canvas: Canvas,
//        view: View,
//        recyclerView: RecyclerView,
//        state: RecyclerView.State
//    ) {
//
//        val viewHolder = recyclerView.getChildViewHolder(view)
//        val nextViewHolder =
//            recyclerView.findViewHolderForAdapterPosition(viewHolder.adapterPosition + 1)
//        val previousChildViewHolder =
//            recyclerView.findViewHolderForAdapterPosition(viewHolder.adapterPosition - 1)
//
//        if (cornerRadius.compareTo(0f) != 0) {
//            val roundMode = getRoundMode(previousChildViewHolder, viewHolder, nextViewHolder)
//            val outlineProvider = view.outlineProvider
//            if (outlineProvider is RoundOutlineProvider) {
//                outlineProvider.roundMode = roundMode
//                view.invalidateOutline()
//            } else {
//                view.outlineProvider = RoundOutlineProvider(cornerRadius, roundMode)
//                view.clipToOutline = true
//            }
//        }
//    }
//}