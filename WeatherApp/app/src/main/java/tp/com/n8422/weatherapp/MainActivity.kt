package tp.com.n8422.weatherapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import tp.com.n8422.weatherapp.ui.main.SectionsPagerAdapter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    // example call is : https://api.openweathermap.org/data/2.5/weather?q=Jyväskylä&APPID=YOUR_API_KEY&units=metric&lang=fi
    val API_LINK: String = "https://api.openweathermap.org/data/2.5/weather?q="
    val API_ICON: String = "https://openweathermap.org/img/w/"
    val API_KEY: String = "4aa10528fa469d3d99742cee30665d93"

    // add a few test cities
    private val cities: MutableList<String> = mutableListOf("Jyväskylä", "Helsinki", "Oulu", "New York", "Tokyo")
    // city index, used when data will be loaded
    private var index: Int = 0

    companion object {
        var forecasts: MutableList<Forecast> = mutableListOf()
    }

    private fun setUI() {
        // hide progress bar
        //val prog = ProgressBar
        //prog.visibility = View.INVISIBLE
        // add adapber
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        // add fab
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "How you can add and save a new city?", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadWeatherForecast(cities[index])
        // load forecast





    }
    private fun loadWeatherForecast(city:String) {
        // url for loading
        val url = "$API_LINK$city&APPID=$API_KEY&units=metric&lang=fi"


        // JSON object request with Volley

        val jsonObjectRequest = JsonObjectRequest(

            Request.Method.GET, url, null, Response.Listener<JSONObject> { response ->
                try {
                    // load OK - parse data from the loaded JSON
                    // **add parse codes here... described later**
                    val mainJSONObject = response.getJSONObject("main")
                    val weatherArray = response.getJSONArray("weather")
                    val firstWeatherObject = weatherArray.getJSONObject(0)
                    // city, condition, temperature
                    val city = response.getString("name")
                    val condition = firstWeatherObject.getString("main")
                    val temperature = mainJSONObject.getString("temp")+" °C"
// time
                    val weatherTime: String = response.getString("dt")
                    val weatherLong: Long = weatherTime.toLong()
                    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss")
                    val dt = Instant.ofEpochSecond(weatherLong).atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter).toString()
// icon
                    val weatherIcon = firstWeatherObject.getString("icon")
                    val url = "$API_ICON$weatherIcon.png"

                    // add forecast object to the list
                    forecasts.add(Forecast(city,condition,temperature,dt,url))
                    // use Logcat window to check that loading really works
                    Log.d("WEATHER", "**** weatherCity = " + forecasts[index].city)
                    // load another city if not loaded yet
                    if ((++index) < cities.size) loadWeatherForecast(cities[index])
                    else {
                        Log.d("WEATHER", "*** ALL LOADED!")
                        setUI()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("WEATHER", "***** error: $e")
                    // hide progress bar
                    //ProgressBar.visibility = View.INVISIBLE
                    // show Toast -> should be done better!!!
                    Toast.makeText(this,"Error loading weather forecast!",Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error -> Log.d("PTM", "Error: $error") })
        // start loading data with Volley
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(jsonObjectRequest)
    }
}