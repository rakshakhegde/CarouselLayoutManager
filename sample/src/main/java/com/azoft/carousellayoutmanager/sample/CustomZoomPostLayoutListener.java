package com.azoft.carousellayoutmanager.sample;

import android.support.annotation.NonNull;
import android.view.View;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.ItemTransformation;
import com.azoft.carousellayoutmanager.sample.CarouselPreviewActivity.TestViewHolder;

public class CustomZoomPostLayoutListener implements CarouselLayoutManager.PostLayoutListener {

	@Override
	public ItemTransformation transformChild(@NonNull final View child, final float itemPositionToCenterDiff, final int orientation) {
		if (child.getHeight() == 0) {
			return null;
		}
		final TestViewHolder holder = (TestViewHolder) child.getTag();
		float percent = Math.min(1f, Math.abs(itemPositionToCenterDiff));
		if (itemPositionToCenterDiff < 0) {
			holder.expectAnim.setPercent(percent);
		} else if (itemPositionToCenterDiff > 0) {
			holder.otherExpectAnim.setPercent(percent);
		} else {
			holder.expectAnim.setPercent(0);
			holder.otherExpectAnim.setPercent(0);
		}
		holder.mItemViewBinding.cItem1.setText(String.format("%.2f", itemPositionToCenterDiff));
		holder.mItemViewBinding.cItem2.setText(String.format("%.2f", itemPositionToCenterDiff));

//		ViewGroup.LayoutParams lp = holder.mItemViewBinding.rootrel.getLayoutParams();
//		Resources res = child.getResources();
//		int maxHeight = res.getDimensionPixelSize(R.dimen.max_card_height);
//		int minHeight = res.getDimensionPixelSize(R.dimen.min_card_height);
//		lp.height = (int) ((maxHeight - minHeight) * percent + minHeight);
//		holder.mItemViewBinding.rootrel.setLayoutParams(lp);
//		holder.mItemViewBinding.rootrel.setMinimumHeight(lp.height);
//		holder.mItemViewBinding.rootrel.requestLayout();
//		holder.itemView.requestLayout();
		return null;
	}
}
