package fi.jamk.intentsexample

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

class SecondActivity : AppCompatActivity() {

    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // get data from intent
        val bundle: Bundle? = intent.extras;
        if (bundle != null) {
            val name = bundle!!.getString("name")
            Toast.makeText(this, "Name is $name",Toast.LENGTH_LONG).show()
        }

    }

    // finish this activity
    fun goBack(v: View) {
        finish()
    }

    // finish this activity and send data back to caller
    fun goBackSendData(v: View) {
        val intent = Intent()
        intent.putExtra("number", Math.random())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

