package com.project.aromaloka.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.aromaloka.models.Brand
import com.project.aromaloka.ui.main.ui.search.fragments.BrandFragment
import com.project.aromaloka.ui.main.ui.search.fragments.PerfumeFragment

class SearchPagerAdapter(
    activity: AppCompatActivity,
    private var queryParam: String,
    private var listQueryParam: Array<String>
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PerfumeFragment(queryParam, listQueryParam)
            1 -> BrandFragment(queryParam, listQueryParam)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}