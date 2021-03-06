package com.trilokiainc.thirtydaysofkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trilokiainc.thirtydaysofkotlin.R
import com.trilokiainc.thirtydaysofkotlin.callback.RecycleViewCallback
import com.trilokiainc.thirtydaysofkotlin.model.AppModel

class AppListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfApps = mutableListOf<AppModel>()

    var itemClick: ((String) -> Unit)? = null


    private var recyclerViewCallback: RecycleViewCallback? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AppListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_app, parent, false)
        ).apply {
            itemClick = { appName ->
                this@AppListAdapter.itemClick?.invoke(appName)
            }
            itemView.setOnClickListener {
                this@AppListAdapter.recyclerViewCallback?.onRecycleViewItemClick(
                    listOfApps[adapterPosition],
                    adapterPosition
                )
            }
        }

    }

    override fun getItemCount(): Int = listOfApps.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val movieViewHolder = viewHolder as AppListViewHolder
        movieViewHolder.bindView(listOfApps[position])
    }

    fun setAppList(listOfApps: List<AppModel>) {
        this.listOfApps = listOfApps.toMutableList()
        notifyDataSetChanged()
    }

    /* fun setOnCallbackListener(recyclerViewCallback: MainActivity) {
         this.recyclerViewCallback = recyclerViewCallback
     }*/

}