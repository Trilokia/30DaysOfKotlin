package com.trilokiainc.thirtydaysofkotlin.model

import android.graphics.drawable.Drawable


data class AppModel(var appName: String,
                    var appVersion: String,
                    var appPackageName: String,
                    var appIcon: Drawable?) {
    constructor(): this("","","",null)}