package com.example.library

import android.content.Context

class User(private var username: String, private var password: String) {

    private var coin: Int = 0

    fun getUsername() = username

    fun getPassword() = password

    fun getCoin() = coin

    fun setCoin(coin: Int) {
        this.coin = coin
    }

    fun login(context: Context, whatYouDo: (String) -> Unit) {
        val data = mutableMapOf<String, String>()
        data["username"] = this.username
        data["password"] = this.password
        ServerRequest.login(data, context, whatYouDo)
    }

    fun register(context: Context, whatYouDo: (String) -> Unit) {
        val data = mutableMapOf<String, String>()
        data["username"] = this.username
        data["password"] = this.password
        ServerRequest.register(data, context, whatYouDo)
    }

    fun borrowBook() {

    }

    fun comment() {

    }

    fun uploadBook(book: Book, context: Context, whatYouDo: (String) -> Unit) {
        val data = mutableMapOf<String, String>()
        data["name"] = book.name
        data["about"] = book.about
        data["numOfPage"] = book.numOfPages.toString()
        data["category_name"] = book.categoryName
        data["publish_date"] = book.publishDate
        data["publish_house"] = book.publishHouse
        data["price"] = book.price.toString()
        data["version_number"] = book.versionNumber.toString()
        data["username"] = this.username
        ServerRequest.uploadBook(data, context, whatYouDo)
    }

}