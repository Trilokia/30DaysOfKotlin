package com.trilokiainc.thirtydaysofkotlin

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.trilokiainc.thirtydaysofkotlin.adapter.AppListAdapter
import com.trilokiainc.thirtydaysofkotlin.model.AppModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class MainActivity : AppCompatActivity(){

    private val TAG = MainActivity::class.java.simpleName
    private val gplayURL = "https://play.google.com/store/apps/details?id="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * setting up recyclerview
         */
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val appListAdapter = AppListAdapter().apply {
            itemClick = { listItem ->
           Toast.makeText(this@MainActivity, getString(R.string.loading), Toast.LENGTH_SHORT).show()
                scrapPrivacyPolicyLink(listItem);
            }
        }
        recyclerView.adapter = appListAdapter
       // appListAdapter.setOnCallbackListener(this)
        appListAdapter.setAppList(getInstalledAppsList())
    }


    fun isSystemApp(pkgInfo: PackageInfo): Boolean{
        return (pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    }

    /**
     * getting list of user installed apps
     */
    private fun getInstalledAppsList(): List<AppModel> {
        val listOfApp = mutableListOf<AppModel>()
        var appModel: AppModel
        val packs:List<PackageInfo> = packageManager.getInstalledPackages(0)
        for (item in packs.indices){
            val p : PackageInfo = packs[item]
            if((!isSystemApp(p))){
                appModel = AppModel(p.applicationInfo.loadLabel(packageManager).toString(), packageManager.getPackageInfo(p.applicationInfo.packageName,0).versionName, p.applicationInfo.packageName, p.applicationInfo.loadIcon(getPackageManager()))
                listOfApp.add(appModel)
            }
        }
        return listOfApp
    }
      /* override fun onRecycleViewItemClick(appModel: AppModel, position: Int) {
           Toast.makeText(this@MainActivity, appModel.appName,Toast.LENGTH_SHORT).show()
    }*/

    /**
     * function for scraping privacy policy link from Google Play
     *
     */
    private fun scrapPrivacyPolicyLink(packagename: String) {

        var url: String? =null
        Thread(Runnable {
            try {
                val doc: Document = Jsoup.connect(gplayURL+packagename).get()
                val mElementData: Elements = doc.select("div[class=IQ1z0d]")
                val arr = mElementData.toString().split("<div>",ignoreCase = true, limit = 0)
                for (a in arr) {
                    if (a.contains("Privacy Policy")) {
                        Log.d(TAG, "Found: $a")
                        val splitted = a.split("\"").toTypedArray()
                        // Url is at index 1
                        url = splitted[1]
                        break
                    }
                }
                // load url in androidx browser
                runOnUiThread {
                    if(!url.isNullOrEmpty())
                        webTab(url!!)
                    else  Toast.makeText(this@MainActivity, getString(R.string.no_privacy_policy),Toast.LENGTH_SHORT).show() }

            } catch (e: IOException) {
                e.printStackTrace()
               Log.d(TAG,"Error: "+e.message.toString())
                runOnUiThread {
                    Toast.makeText(this@MainActivity, getString(R.string.app_not_found),Toast.LENGTH_SHORT).show()
                }
            }

        }).start()
    }

    fun webTab(url: String){
        try {
            val uri = Uri.parse(url)
            // create an intent builder
            val intentBuilder = CustomTabsIntent.Builder()

            // setting toolbar colors
            intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            intentBuilder.setSecondaryToolbarColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorPrimaryDark
                )
            )
            // build custom tabs intent
            val customTabsIntent = intentBuilder.build()
            // launch the url
            customTabsIntent.launchUrl(this, uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
