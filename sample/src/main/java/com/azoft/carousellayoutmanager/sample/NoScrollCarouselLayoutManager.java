package com.azoft.carousellayoutmanager.sample;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;

/**
 * Created by rakshakhegde on 16/06/17.
 */

public class NoScrollCarouselLayoutManager extends CarouselLayoutManager {

	public NoScrollCarouselLayoutManager(int orientation) {
		super(orientation);
	}

	public NoScrollCarouselLayoutManager(int orientation, boolean circleLayout) {
		super(orientation, circleLayout);
	}

	@Override
	public boolean canScrollVertically() {
		return false;
	}

	@Override
	public boolean canScrollHorizontally() {
		return false;
	}
}
