package com.trilokiainc.thirtydaysofkotlin.callback

import com.trilokiainc.thirtydaysofkotlin.model.AppModel

interface RecycleViewCallback {
    fun onRecycleViewItemClick(appModel :AppModel, position: Int)
}