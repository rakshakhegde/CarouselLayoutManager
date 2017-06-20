package com.azoft.carousellayoutmanager.sample;

import android.support.annotation.NonNull;
import android.view.View;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.ItemTransformation;
import com.azoft.carousellayoutmanager.sample.CarouselPreviewActivity.TestViewHolder;

/**
 * Implementation of {@link CarouselLayoutManager.PostLayoutListener} that makes interesting scaling of items. <br />
 * We are trying to make items scaling quicker for closer items for center and slower for when they are far away.<br />
 * Tis implementation uses atan function for this purpose.
 */
public class CustomZoomPostLayoutListener implements CarouselLayoutManager.PostLayoutListener {

	@Override
	public ItemTransformation transformChild(@NonNull final View child, final float itemPositionToCenterDiff, final int orientation) {
		final TestViewHolder holder = (TestViewHolder) child.getTag();
		float percent = Math.min(1f, Math.abs(itemPositionToCenterDiff));
		if (itemPositionToCenterDiff > 0) {
			holder.expectAnim.setPercent(percent);
		} else {
			holder.otherExpectAnim.setPercent(percent);
		}
		return null;
	}
}
