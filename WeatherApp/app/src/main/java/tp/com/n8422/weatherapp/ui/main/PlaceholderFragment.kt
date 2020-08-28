package tp.com.n8422.weatherapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.ViewModelProviders.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_main.*
import tp.com.n8422.weatherapp.Forecast
import tp.com.n8422.weatherapp.MainActivity
import tp.com.n8422.weatherapp.R

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    companion object {
        private const val ARG_FORECAST_POSITION = "forecast_position"

        @JvmStatic
        fun newInstance(position: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_FORECAST_POSITION, position)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pageViewModel = of(this).get(PageViewModel::class.java).apply {
            val position: Int = arguments!!.getInt(ARG_FORECAST_POSITION)
            setForecast(MainActivity.forecasts[position])
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        pageViewModel.forecast.observe(viewLifecycleOwner, Observer<Forecast> {
            cityTextView.text = it.city
            conditionTextView.text = it.condition
            temperatureTextView.text = it.temperature
            timeTextView.text = it.time
            Glide.with(this).load(it.icon).into(iconImageView)
        })

        return root
    }
}