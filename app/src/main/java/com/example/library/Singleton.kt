package com.example.library

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Singleton private constructor(context: Context) {

    private var queue:RequestQueue = Volley.newRequestQueue(context)

    fun add(stringRequest: StringRequest){
        queue.add(stringRequest)
    }

    companion object{
        private var singleton:Singleton? = null
        fun getInstance(context: Context):Singleton{
            if (singleton == null){
                singleton = Singleton(context)
            }
            return singleton as Singleton
        }
    }

}