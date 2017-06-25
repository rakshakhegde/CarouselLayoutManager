package com.azoft.carousellayoutmanager.sample

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.azoft.carousellayoutmanager.CarouselLayoutManager
import com.azoft.carousellayoutmanager.CenterScrollListener
import com.azoft.carousellayoutmanager.DefaultChildSelectionListener
import com.azoft.carousellayoutmanager.sample.databinding.ActivityCarouselPreviewBinding
import com.azoft.carousellayoutmanager.sample.databinding.ItemViewBinding
import com.facebook.stetho.Stetho
import com.github.florent37.expectanim.ExpectAnim
import com.github.florent37.expectanim.core.Expectations.*

class CarouselPreviewActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Stetho.initializeWithDefaults(applicationContext)

		val binding: ActivityCarouselPreviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_carousel_preview)

		val adapter = TestAdapter()

		initRecyclerView(binding.listVertical, CustomCarouselLayoutManager(CarouselLayoutManager.VERTICAL), adapter)

		// fab button will add element to the end of the list
		binding.fabScroll.setOnClickListener {
			/*
                final int itemToRemove = adapter.mItemsCount;
                if (10 != itemToRemove) {
                    adapter.mItemsCount++;
                    adapter.notifyItemInserted(itemToRemove);
                }
*/
			binding.listVertical.smoothScrollToPosition(adapter.itemCount - 2)
		}

		// fab button will remove element from the end of the list
		binding.fabChangeData.setOnClickListener {
			/*
                final int itemToRemove = adapter.mItemsCount - 1;
                if (0 <= itemToRemove) {
                    adapter.mItemsCount--;
                    adapter.notifyItemRemoved(itemToRemove);
                }
*/
			binding.listVertical.smoothScrollToPosition(1)
		}
	}

	private fun initRecyclerView(recyclerView: RecyclerView, layoutManager: CarouselLayoutManager, adapter: TestAdapter) {
		// enable zoom effect. this line can be customized
		layoutManager.setPostLayoutListener(CustomZoomPostLayoutListener())
		layoutManager.maxVisibleItems = 2

		recyclerView.layoutManager = layoutManager
		// we expect only fixed sized item for now
		recyclerView.setHasFixedSize(true)
		// sample adapter with random data
		recyclerView.adapter = adapter
		// enable center post scrolling
		recyclerView.addOnScrollListener(CenterScrollListener())
		// enable center post touching on item and item click listener
		DefaultChildSelectionListener.initCenterItemListener(DefaultChildSelectionListener.
				OnCenterItemClickListener { recyclerView, carouselLayoutManager, v ->
					val position = recyclerView.getChildLayoutPosition(v)
					val msg = "Item $position was clicked"
					Toast.makeText(this@CarouselPreviewActivity, msg, Toast.LENGTH_SHORT).show()
				}, recyclerView, layoutManager)

		layoutManager.addOnItemSelectionListener { adapterPosition ->
			if (CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
				/*
                    adapter.mPosition[adapterPosition] = (value % 10) + (value / 10 + 1) * 10;
                    adapter.notifyItemChanged(adapterPosition);
*/
			}
		}
	}

	private class TestAdapter : RecyclerView.Adapter<TestViewHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
			return TestViewHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
		}

		override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
			holder.mItemViewBinding.cItem1.text = position.toString()
			holder.mItemViewBinding.cItem2.text = position.toString()

			holder.mItemViewBinding.locationTypeTV.text = if (position == 0) "Deliver to"
			else "Delivery " + (position + 1)

			holder.mItemViewBinding.pointTV.text = "D${position + 1}"
		}

		override fun getItemCount(): Int {
			return 8
		}
	}

	internal class TestViewHolder(val mItemViewBinding: ItemViewBinding) : RecyclerView.ViewHolder(mItemViewBinding.root) {
		val expectAnim: ExpectAnim = ExpectAnim()

				.expect(mItemViewBinding.pointTV)
				.toBe(
						centerVerticalInParent(),
						leftOfParent().withMarginDp(40F)
				)

				.expect(mItemViewBinding.addressTV)
				.toBe(
						centerVerticalInParent(),
						toRightOf(mItemViewBinding.pointTV).withMarginDp(8F)
				)

				.expect(mItemViewBinding.locationTypeTV)
				.toBe(aboveOf(mItemViewBinding.card), alpha(0F))

				.expect(mItemViewBinding.searchImage)
				.toBe(toLeftOf(mItemViewBinding.card), alpha(0F))

				.expect(mItemViewBinding.card)
				.toBe(width(300).toDp(), height(50).toDp())

				.toAnimation()

		val otherExpectAnim: ExpectAnim = ExpectAnim()

				.expect(mItemViewBinding.pointTV)
				.toBe(
						centerVerticalInParent(),
						leftOfParent().withMarginDp(40F)
				)

				.expect(mItemViewBinding.addressTV)
				.toBe(
						centerVerticalInParent(),
						toRightOf(mItemViewBinding.pointTV).withMarginDp(8F)
				)

				.expect(mItemViewBinding.locationTypeTV)
				.toBe(belowOf(mItemViewBinding.card), alpha(0F))

				.expect(mItemViewBinding.searchImage)
				.toBe(toLeftOf(mItemViewBinding.card), alpha(0F))

				.expect(mItemViewBinding.card)
				.toBe(width(300).toDp(), height(50).toDp())

				.toAnimation()

		init {
			mItemViewBinding.root.tag = this
		}
	}
}
