package com.example.library

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest


private const val tag = "ServerRequest"

class ServerRequest {

    companion object {

        fun register(
            data: MutableMap<String, String>,
            context: Context,
            whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.registerUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun login(data: MutableMap<String, String>, context: Context, whatYouDo: (String) -> Unit) {
            val stringRequest = getStringRequest(Constant.loginUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun uploadBook(
            data: MutableMap<String, String>,
            context: Context,
            whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.uploadBookUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun downloadBooks(
            context: Context,
            whatYouDo: (String) -> Unit
        ) {
            val stringRequest =
                getStringRequest(Constant.downloadBooksUrl, mutableMapOf(), whatYouDo)
            add(context, stringRequest)
        }

        fun addComment(
            data: MutableMap<String, String>,
            context: Context,
            whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.commentUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun getAllComment(
            data: MutableMap<String, String>,
            context: Context,
            whatYouDo: (String) -> Unit
            ) {
                val stringRequest = getStringRequest(Constant.getAllCommentUrl, data, whatYouDo)
                add(context, stringRequest)
            }

        fun borrowing(data: MutableMap<String, String>,
                      context: Context,
                      whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.borrowingUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun getCoins(data: MutableMap<String, String>,
                     context: Context,
                     whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.getCoinsUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun getUploadBooks(data: MutableMap<String, String>,
                     context: Context,
                     whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.getUploadBooksUrl, data, whatYouDo)
            add(context, stringRequest)
        }
        fun getBorrowBooks(data: MutableMap<String, String>,
                     context: Context,
                     whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.getBorrowBooksUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun updateBook(data: MutableMap<String, String>,
                           context: Context,
                           whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.updateBooksUrl, data, whatYouDo)
            add(context, stringRequest)
        }

        fun deleteBook(data: MutableMap<String, String>,
                       context: Context,
                       whatYouDo: (String) -> Unit
        ) {
            val stringRequest = getStringRequest(Constant.deleteBooksUrl, data, whatYouDo)
            add(context, stringRequest)
        }


        private fun add(context: Context, stringRequest: StringRequest) {
            val singleton = Singleton.getInstance(context)
            singleton.add(stringRequest)
        }

        private fun getStringRequest(
            url: String,
            data: MutableMap<String, String>,
            whatYouDo: (String) -> Unit
        ): StringRequest {
            val stringRequest = object : StringRequest(Request.Method.POST, url, {
                whatYouDo(it)
            }, {
                Log.i(tag, "${it.message}")
            }) {
                override fun getParams(): MutableMap<String, String> {
                    return data
                }
            }
            return stringRequest
        }
    }
}