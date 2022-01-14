package com.example.library

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class AccountActivity : AppCompatActivity(),Adapter.Listener {
    private lateinit var myUploadBooksRecyclerView:RecyclerView
    private lateinit var myBorrowBooksRecyclerView:RecyclerView
    private lateinit var adapter: Adapter
    private lateinit var adapter2: Adapter
    private var uploadBooks = mutableListOf<Book>()
    private var borrowBooks = mutableListOf<Book>()
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        myUploadBooksRecyclerView = findViewById(R.id.AccountActivity_RecyclerView_MyUploadBooks)
        myUploadBooksRecyclerView.setHasFixedSize(true)
        myUploadBooksRecyclerView.layoutManager = LinearLayoutManager(baseContext,LinearLayoutManager.HORIZONTAL,false)

        myBorrowBooksRecyclerView = findViewById(R.id.AccountActivity_RecyclerView_MyBorrowBooks)
        myBorrowBooksRecyclerView.setHasFixedSize(true)
        myBorrowBooksRecyclerView.layoutManager = LinearLayoutManager(baseContext,LinearLayoutManager.HORIZONTAL,false)

        user = LoginRegisterActivity.getUserDataFromIntent(intent)

        adapter = Adapter(uploadBooks,user,true)
        adapter2 = Adapter(borrowBooks,user,true)

        myUploadBooksRecyclerView.adapter = adapter
        myBorrowBooksRecyclerView.adapter = adapter2
    }

    override fun onResume() {
        super.onResume()

        Log.i("aaa","aaa")
        getUploadBooks()
        getBorrowBooks()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUploadBooks(){
        uploadBooks.clear()
        val data = mutableMapOf<String,String>()
        data["username"] = user.getUsername()
        ServerRequest.getUploadBooks(data,baseContext){
            val jsonObject = JSONObject(it)
            val jsonArray = jsonObject.getJSONArray("books")
            for (i in 0 until jsonArray.length()) {
                val book = jsonArray.getJSONObject(i)
                uploadBooks.add(HomeActivity.getBookFromJsonObject(book))
            }
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBorrowBooks(){
        uploadBooks.clear()

        val data = mutableMapOf<String,String>()
        data["username"] = user.getUsername()
        ServerRequest.getBorrowBooks(data,baseContext){
            val jsonObject = JSONObject(it)
            val jsonArray = jsonObject.getJSONArray("books")
            for (i in 0 until jsonArray.length()) {
                val book = jsonArray.getJSONObject(i)
                borrowBooks.add(HomeActivity.getBookFromJsonObject(book))
            }
            adapter2.notifyDataSetChanged()
        }
    }

    override fun onClick(book: Book) {
        var intent = Intent(baseContext, ShowBookInfo::class.java)
        intent = HomeActivity.putBookInIntent(book, intent)
        intent = LoginRegisterActivity.putUserDataInIntent(user, intent)
        startActivity(intent)
    }

    override fun onClickBorrow(book: Book) {

    }
}

