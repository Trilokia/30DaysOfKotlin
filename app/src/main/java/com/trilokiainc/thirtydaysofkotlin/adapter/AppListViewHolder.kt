package com.trilokiainc.thirtydaysofkotlin.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trilokiainc.thirtydaysofkotlin.model.AppModel
import kotlinx.android.synthetic.main.list_item_app.view.*

class AppListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var itemClick: ((String) -> Unit)? = null

    fun bindView(appModel: AppModel) {
        itemView.textAppName.text = appModel.appName
        itemView.textAppVersion.text = "Version: ${appModel.appVersion}"
        itemView.textAppPkgName.text = "Package: ${appModel.appPackageName}"

        Glide.with(itemView.context).load(appModel.appIcon!!).into(itemView.imageAppIcon)

        itemView.setOnClickListener {
            itemClick?.invoke(appModel.appPackageName)
        }
    }

}