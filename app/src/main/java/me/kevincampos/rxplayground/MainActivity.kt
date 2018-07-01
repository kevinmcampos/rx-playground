package me.kevincampos.rxplayground

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request


class MainActivity : AppCompatActivity() {

    @SuppressLint("StaticFieldLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        object : AsyncTask<Void, Void, Gist>() {

            override fun doInBackground(vararg p0: Void?): Gist? {
                val client = OkHttpClient()

                val gistUrl = "https://api.github.com/gists/db72a05cc03ef523ee74"

                val request = Request.Builder()
                        .url(gistUrl)
                        .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return Gson().fromJson(response.body().charStream(), Gist::class.java)
                }

                return null
            }

            override fun onPostExecute(result: Gist?) {
                super.onPostExecute(result)

                val gistStats = StringBuilder()
                result?.files?.forEach { gistFileEntry ->
                    gistStats.append(gistFileEntry.key)
                    gistStats.append(" - Length of file ${gistFileEntry.value.content.length}")
                    gistStats.append("\n")
                }

                findViewById<TextView>(R.id.main_message).text = gistStats
            }

        }.execute()


    }
}
