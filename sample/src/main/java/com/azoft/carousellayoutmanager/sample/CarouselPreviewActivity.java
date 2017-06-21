package com.azoft.carousellayoutmanager.sample;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener;
import com.azoft.carousellayoutmanager.sample.databinding.ActivityCarouselPreviewBinding;
import com.azoft.carousellayoutmanager.sample.databinding.ItemView2Binding;
import com.github.florent37.expectanim.ExpectAnim;

import java.util.Locale;
import java.util.Random;

import static com.github.florent37.expectanim.core.Expectations.centerVerticalInParent;
import static com.github.florent37.expectanim.core.Expectations.outOfScreen;
import static com.github.florent37.expectanim.core.Expectations.toHaveBackgroundAlpha;
import static com.github.florent37.expectanim.core.Expectations.toRightOf;

public class CarouselPreviewActivity extends AppCompatActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActivityCarouselPreviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_carousel_preview);

		setSupportActionBar(binding.toolbar);

		final TestAdapter adapter = new TestAdapter();

		// create layout manager with needed params: vertical, cycle
		initRecyclerView(binding.listHorizontal, new CustomCarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false), adapter);
		initRecyclerView(binding.listVertical, new CustomCarouselLayoutManager(CarouselLayoutManager.VERTICAL, false), adapter);

		// fab button will add element to the end of the list
		binding.fabScroll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
/*
                final int itemToRemove = adapter.mItemsCount;
                if (10 != itemToRemove) {
                    adapter.mItemsCount++;
                    adapter.notifyItemInserted(itemToRemove);
                }
*/
				binding.listHorizontal.smoothScrollToPosition(adapter.getItemCount() - 2);
				binding.listVertical.smoothScrollToPosition(adapter.getItemCount() - 2);
			}
		});

		// fab button will remove element from the end of the list
		binding.fabChangeData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
/*
                final int itemToRemove = adapter.mItemsCount - 1;
                if (0 <= itemToRemove) {
                    adapter.mItemsCount--;
                    adapter.notifyItemRemoved(itemToRemove);
                }
*/
				binding.listHorizontal.smoothScrollToPosition(1);
				binding.listVertical.smoothScrollToPosition(1);
			}
		});
	}

	private void initRecyclerView(final RecyclerView recyclerView, final CarouselLayoutManager layoutManager, final TestAdapter adapter) {
		// enable zoom effect. this line can be customized
		layoutManager.setPostLayoutListener(new CustomZoomPostLayoutListener());
		layoutManager.setMaxVisibleItems(2);

		recyclerView.setLayoutManager(layoutManager);
		// we expect only fixed sized item for now
		recyclerView.setHasFixedSize(true);
		// sample adapter with random data
		recyclerView.setAdapter(adapter);
		// enable center post scrolling
		recyclerView.addOnScrollListener(new CenterScrollListener());
		// enable center post touching on item and item click listener
		DefaultChildSelectionListener.initCenterItemListener(new DefaultChildSelectionListener.OnCenterItemClickListener() {
			@Override
			public void onCenterItemClicked(@NonNull final RecyclerView recyclerView, @NonNull final CarouselLayoutManager carouselLayoutManager, @NonNull final View v) {
				final int position = recyclerView.getChildLayoutPosition(v);
				final String msg = String.format(Locale.US, "Item %1$d was clicked", position);
				Toast.makeText(CarouselPreviewActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		}, recyclerView, layoutManager);

		layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {

			@Override
			public void onCenterItemChanged(final int adapterPosition) {
				if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
					final int value = adapter.mPosition[adapterPosition];
/*
                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
                    adapter.notifyItemChanged(adapterPosition);
*/
				}
			}
		});
	}

	private static final class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

		@SuppressWarnings("UnsecureRandomNumberGeneration")
		private final Random mRandom = new Random();
		private final int[] mColors;
		private final int[] mPosition;
		private int mItemsCount = 10;

		TestAdapter() {
			mColors = new int[mItemsCount];
			mPosition = new int[mItemsCount];
			for (int i = 0; mItemsCount > i; ++i) {
				//noinspection MagicNumber
				mColors[i] = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
				mPosition[i] = i;
			}
		}

		@Override
		public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
			return new TestViewHolder(ItemView2Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
		}

		@Override
		public void onBindViewHolder(final TestViewHolder holder, final int position) {
			holder.mItemViewBinding.cItem1.setText(String.valueOf(mPosition[position]));
			holder.mItemViewBinding.cItem2.setText(String.valueOf(mPosition[position]));
			holder.mItemViewBinding.locationTypeTV.setText(position == 0 ? "Deliver to" : "Delivery " + (position + 1));
//            holder.mItemViewBinding.rootrel.setBackgroundColor(mColors[position]);
		}

		@Override
		public int getItemCount() {
			return mItemsCount;
		}
	}

	static class TestViewHolder extends RecyclerView.ViewHolder {

		final ItemView2Binding mItemViewBinding;
		final ExpectAnim expectAnim;
		final ExpectAnim otherExpectAnim;

		TestViewHolder(final ItemView2Binding itemViewBinding) {
			super(itemViewBinding.getRoot());

			mItemViewBinding = itemViewBinding;

			expectAnim = new ExpectAnim()

					.expect(mItemViewBinding.constraintLayout)
					.toBe(toHaveBackgroundAlpha(1f))

					.expect(mItemViewBinding.pointTV)
					.toBe(centerVerticalInParent())

					.expect(mItemViewBinding.addressTV)
					.toBe(
							centerVerticalInParent(),
							toRightOf(mItemViewBinding.pointTV).withMarginDp(4)
					)

					.expect(mItemViewBinding.locationTypeTV)
					.toBe(outOfScreen(Gravity.TOP))

					.toAnimation();

			otherExpectAnim = new ExpectAnim()

					.expect(mItemViewBinding.pointTV)
					.toBe(centerVerticalInParent())

					.expect(mItemViewBinding.addressTV)
					.toBe(
							centerVerticalInParent(),
							toRightOf(mItemViewBinding.pointTV).withMarginDp(4)
					)

					.expect(mItemViewBinding.locationTypeTV)
					.toBe(outOfScreen(Gravity.BOTTOM))

					.toAnimation();

			itemViewBinding.getRoot().setTag(this);
		}
	}
}
