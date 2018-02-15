package com.think360.sotaknights.model

import android.graphics.Bitmap




/**
 * Created by think360 on 01/08/17.
 */



data class TaskItem(var id: String?, var short_name: String?, var task_client_name: String?, var message_recived_at: String?, var client_phone_number: String?,  var task_number: String?, var date: String?, var time: String?, var location: String?,var solidColor : String,var textColor : String)
data class ImageUrl(val image: String?)
