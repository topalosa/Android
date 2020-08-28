package fi.jamk.activityexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // event listener by coding
        button2.setOnClickListener { view ->
            textView2.text = "Button clicked!"
        }
    }

    // event listener from XML layout
    fun buttonClicked(v: View) {
        textView.text = "Button clicked!"
    }

}
