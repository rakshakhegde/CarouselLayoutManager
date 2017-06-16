package com.azoft.carousellayoutmanager.sample;

import android.support.annotation.NonNull;
import android.view.View;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.ItemTransformation;

/**
 * Implementation of {@link CarouselLayoutManager.PostLayoutListener} that makes interesting scaling of items. <br />
 * We are trying to make items scaling quicker for closer items for center and slower for when they are far away.<br />
 * Tis implementation uses atan function for this purpose.
 */
public class CustomZoomPostLayoutListener implements CarouselLayoutManager.PostLayoutListener {

	@Override
	public ItemTransformation transformChild(@NonNull final View child, final float itemPositionToCenterDiff, final int orientation) {
//		final float scale = (float) (2 * (2 * -StrictMath.atan(Math.abs(itemPositionToCenterDiff) + 1.0) / Math.PI + 1));
		final float scale = -Math.abs(itemPositionToCenterDiff / 3f) + 1f;

		// because scaling will make view smaller in its center, then we should move this item to the top or bottom to make it visible
		final float translateY;
		final float translateX;
		if (CarouselLayoutManager.VERTICAL == orientation) {
			translateY = translate(child.getMeasuredHeight(), itemPositionToCenterDiff, scale);
			translateX = 0;
		} else {
			translateX = translate(child.getMeasuredWidth(), itemPositionToCenterDiff, scale);
			translateY = 0;
		}

		return new ItemTransformation(scale, scale, translateX, translateY);
	}

	private float translate(int size, float itemPositionToCenterDiff, float scale) {
		final float translateYGeneral = size * (1 - scale) / 2f;
		return Math.signum(itemPositionToCenterDiff) * translateYGeneral;
	}
}
