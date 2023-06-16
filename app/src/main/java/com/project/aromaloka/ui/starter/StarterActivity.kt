package com.project.aromaloka.ui.starter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.project.aromaloka.R
import com.project.aromaloka.adapter.StarterPagerAdapter
import com.project.aromaloka.databinding.ActivityStarterBinding
import com.project.aromaloka.ui.auth.LoginActivity
import com.project.aromaloka.ui.main.BottomMainActivity
import com.project.aromaloka.utils.SessionPreferences
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

@RequiresApi(Build.VERSION_CODES.M)
class StarterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStarterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.consStarter.visibility = View.INVISIBLE

        val pref = SessionPreferences.getInstance(dataStore)

        lifecycleScope.launch {
            val session = pref.getSession().first()

            if (session != null && session.isLogin) {
                startActivity(Intent(this@StarterActivity, BottomMainActivity::class.java))
                finish()
            }else{
                binding.consStarter.visibility = View.VISIBLE
            }
        }


        setViewPager()
        setListeners()

    }

    private fun setViewPager() {
        binding.apply {
            val adapter = StarterPagerAdapter(this@StarterActivity)
            viewPager.adapter = adapter

            layoutTab.apply {
                setSliderColor(
                    getColor(R.color.secondaryColorPink),
                    getColor(R.color.primaryColorPink)
                )
                setSliderWidth(resources.getDimension(com.intuit.sdp.R.dimen._60sdp))
                setSliderHeight(resources.getDimension(com.intuit.sdp.R.dimen._4sdp))
                setSlideMode(IndicatorSlideMode.WORM)
                setIndicatorStyle(IndicatorStyle.DASH)
                setupWithViewPager(viewPager)
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnNext.setOnClickListener {
                val index = viewPager.currentItem
                if (index == 2) {
                    startActivity(Intent(this@StarterActivity, LoginActivity::class.java))
                }
                viewPager.setCurrentItem(index + 1, true)
            }

            btnSkip.setOnClickListener {
                startActivity(Intent(this@StarterActivity, LoginActivity::class.java))
            }
        }
    }
}