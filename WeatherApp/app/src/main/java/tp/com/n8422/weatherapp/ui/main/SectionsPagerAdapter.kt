package tp.com.n8422.weatherapp.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import tp.com.n8422.weatherapp.MainActivity
import tp.com.n8422.weatherapp.R



/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // A new placeholder fragment
        return PlaceholderFragment.newInstance(position)
    }

    override fun getCount(): Int {
        // Size of the forecast in MainActivity
        return MainActivity.forecasts.size
    }
}