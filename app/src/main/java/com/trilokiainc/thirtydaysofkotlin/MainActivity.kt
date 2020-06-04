package com.trilokiainc.thirtydaysofkotlin

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.trilokiainc.thirtydaysofkotlin.adapter.AppListAdapter
import com.trilokiainc.thirtydaysofkotlin.callback.RecycleViewCallback
import com.trilokiainc.thirtydaysofkotlin.model.AppModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecycleViewCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * setting up recyclerview
         */
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        val appListAdapter = AppListAdapter().apply {
            itemClick = { appName ->
                Toast.makeText(this@MainActivity, appName, Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = appListAdapter
        appListAdapter.setOnCallbackListener(this)
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
       override fun onRecycleViewItemClick(appModel: AppModel, position: Int) {
        Toast.makeText(this@MainActivity,
            appModel.appName + " == Position == " + position,
            Toast.LENGTH_SHORT).show()
    }
}
