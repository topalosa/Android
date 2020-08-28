package fi.jamk.intentsexample

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ResolveInfo
import android.net.Uri
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val SECOND_ACTIVITY_REQUEST = 1
    private var receiver: BroadcastReceiver? = null

    // onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // register receiver
        val filter = IntentFilter()
        filter.addAction("fi.ptm.CUSTOM_BROADCAST")
        receiver = MyBroadcastReceiver()
        registerReceiver(receiver, filter)
    }

    // Unregister the broadcast receiver
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    // use explicit intent to launch another activity (inside application package)
    fun launchActivity(v: View) {
        val intent = Intent(this, SecondActivity::class.java);
        startActivity(intent);
    }

    // use explicit intent with data to launch another activity
    fun launchActivityWithData(v: View) {
        val intent = Intent(this, SecondActivity::class.java);
        intent.putExtra("name", "Kirsi Kernel")
        startActivity(intent);
    }

    // use explicit intent to launch another activity for result (inside application package)
    fun launchActivityForResult(v: View) {
        val intent = Intent(this, SecondActivity::class.java);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST);
    }

    // get result from another activities
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check which request we're responding to
        if (requestCode == SECOND_ACTIVITY_REQUEST) {
            // make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // the Intent's data Uri identifies data send here
                if (data != null) {
                    // safe use for nullable bundle
                    val number = data.extras?.getDouble("number")
                    Toast.makeText(this, "Activity returned random number $number", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // launch a map with implicit intent
    fun launchMap(v: View) {
        // Build the intent
        val location = Uri.parse("geo:0,0?q=Piippukatu 2, Jyväskylä, Finland")
        val mapIntent = Intent(Intent.ACTION_VIEW, location)

        // Verify it resolves
        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(mapIntent, 0)
        val isIntentSafe: Boolean = activities.isNotEmpty()

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "There is no activity to handle map intent!", Toast.LENGTH_LONG).show();
        }
    }

    // open browser with implicit intent - show a chooser
    fun openBrowser(v: View) {
        // Build the intent
        val web = Uri.parse("http://jamk.fi")
        val webIntent = Intent(Intent.ACTION_VIEW, web)

        // Always use string resources for UI text.
        val title = resources.getString(R.string.choose_text)
        // Create intent to show chooser
        val chooser = Intent.createChooser(webIntent, title)

        // Verify the intent will resolve to at least one activity
        if (webIntent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }

    // try to play a sound from web with custom action
    fun playSound(v: View) {
        // Build the intent
        val sound = Uri.parse("http://somedomain.com/music.mp3")
        val intent = Intent("fi.ptm.PLAY_SOUND", sound)

        // Verify it resolves
        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        val isIntentSafe: Boolean = activities.isNotEmpty()

        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "There is no activity to handle this sound intent!", Toast.LENGTH_LONG).show();
        }
    }

    // broadcast an intent
    fun broadcastIntent(v: View) {
        val intent = Intent()
        intent.action = "fi.ptm.CUSTOM_BROADCAST"
        intent.flags = Intent.FLAG_INCLUDE_STOPPED_PACKAGES
        sendBroadcast(intent)
    }

}
