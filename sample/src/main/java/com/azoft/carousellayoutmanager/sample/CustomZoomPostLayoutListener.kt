package com.azoft.carousellayoutmanager.sample

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.ItemTransformation
import com.github.florent37.expectanim.ExpectAnim

class CustomZoomPostLayoutListener : CarouselLayoutManager.PostLayoutListener {

	override fun transformChild(child: View, itemPositionToCenterDiff: Float, orientation: Int): ItemTransformation? {
//		Log.i("CustomZoomPostLayout", "transformChild : ${child.height} : ${ViewCompat.isLaidOut(child)} : $itemPositionToCenterDiff")
		if (!ViewCompat.isLaidOut(child)) {
			return null
		}

		val absItemPositionToCenterDiff = Math.abs(itemPositionToCenterDiff)
		val percent = Math.min(1f, absItemPositionToCenterDiff)
		val holder = child.tag as CarouselPreviewActivity.TestViewHolder

		val currentAnim = if (itemPositionToCenterDiff > 0) holder.otherExpectAnim
		else holder.expectAnim

		val oldAnim: ExpectAnim? = child.getTag(R.string.expectanim_view_id) as ExpectAnim?
		if (oldAnim != currentAnim) {
			oldAnim?.setPercent(0F)
		}

		currentAnim.setPercent(percent)

		child.setTag(R.string.expectanim_view_id, currentAnim)

		holder.mItemViewBinding.cItem1.text = String.format("%.2f | %.2f", percent, itemPositionToCenterDiff)
		holder.mItemViewBinding.cItem2.text = String.format("%.2f | %.2f", percent, itemPositionToCenterDiff)

		val grayscale: Int = (255 - percent * 15).toInt()
		holder.mItemViewBinding.card.cardBackgroundColor = ColorStateList.valueOf(Color.rgb(grayscale, grayscale, grayscale))

//		holder.mItemViewBinding.rootlayout.transform<ViewGroup.LayoutParams> {
//			width = (600 + 100 * (1 - Math.abs(itemPositionToCenterDiff) / 2)).toInt()
////			height = (100 + 50 * (1 - Math.abs(itemPositionToCenterDiff) / 2)).toInt()
//			Log.i("CustomZoomPostLayout", "$itemPositionToCenterDiff : ${child.height} : ${child.top} : $width : $height")
//		}

//		child.visibility = if (absItemPositionToCenterDiff > 3F) View.GONE else View.VISIBLE

		return null
	}
}

private inline fun <T : ViewGroup.LayoutParams> View.transform(function: T.() -> Unit) {
	val lp = layoutParams as T
	lp.function()
	layoutParams = lp
}
