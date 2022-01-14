package com.example.library

class Book( val name:String, val about:String, val numOfPages:Int, val categoryName:String, val publishDate:String, val publishHouse:String,
 val price:Float, val versionNumber:Int,val authorName:String,val username:String) {

    private val uploadDate:String? = null
    var id:Int = -1

    fun isUploaded(id:Int){
        this.id = id
    }


}