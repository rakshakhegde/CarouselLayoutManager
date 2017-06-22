package com.azoft.carousellayoutmanager.sample

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.ItemTransformation
import com.github.florent37.expectanim.ExpectAnim

class CustomZoomPostLayoutListener : CarouselLayoutManager.PostLayoutListener {

	override fun transformChild(child: View, itemPositionToCenterDiff: Float, orientation: Int): ItemTransformation? {
		Log.i("CustomZoomPostLayout", "transformChild : ${child.height} : ${ViewCompat.isLaidOut(child)}")
		if (!ViewCompat.isLaidOut(child)) {
			return null
		}

		val percent = Math.min(1f, Math.abs(itemPositionToCenterDiff))
		val holder = child.tag as CarouselPreviewActivity.TestViewHolder

		val currentAnim = if (itemPositionToCenterDiff > 0) holder.otherExpectAnim
		else holder.expectAnim

		val oldAnim: ExpectAnim? = child.getTag(R.string.expectanim_view_id) as ExpectAnim?
		if (oldAnim != currentAnim) {
			oldAnim?.setPercent(0F)
		}

		currentAnim.setPercent(percent)

		child.setTag(R.string.expectanim_view_id, currentAnim)

		holder.mItemViewBinding.cItem1.text = String.format("%.2f | %.2f", itemPositionToCenterDiff, percent)
		holder.mItemViewBinding.cItem2.text = String.format("%.2f | %.2f", itemPositionToCenterDiff, percent)

		val grayscale: Int = (255 - percent * 10).toInt()
		holder.mItemViewBinding.cardview.cardBackgroundColor = ColorStateList.valueOf(Color.rgb(grayscale, grayscale, grayscale))

		holder.mItemViewBinding.rootlayout.transform<RecyclerView.LayoutParams> {
			val margin: Int = (100 * Math.abs(itemPositionToCenterDiff) / 2).toInt()
			setMargins(margin, 0, margin, 0)
			marginStart = margin
		}

		return null
	}
}

private inline fun <T : ViewGroup.LayoutParams> View.transform(function: T.() -> Unit) {
	val lp = layoutParams as T
	lp.function()
	layoutParams = lp
}
