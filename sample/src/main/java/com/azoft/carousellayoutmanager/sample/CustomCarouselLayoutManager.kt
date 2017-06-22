package com.azoft.carousellayoutmanager.sample

import com.azoft.carousellayoutmanager.CarouselLayoutManager

/**
 * Created by rakshakhegde on 22/06/17.
 */
class CustomCarouselLayoutManager(orientation: Int, circleLayout: Boolean = false) :
		CarouselLayoutManager(orientation, circleLayout) {

	override fun convertItemPositionDiffToSmoothPositionDiff(itemPositionDiff: Float): Double {
		return 1.3 * super.convertItemPositionDiffToSmoothPositionDiff(itemPositionDiff)
	}
}
