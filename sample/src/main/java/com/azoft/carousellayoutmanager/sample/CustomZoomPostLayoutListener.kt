package com.azoft.carousellayoutmanager.sample

import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.View

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
		return null
	}
}
