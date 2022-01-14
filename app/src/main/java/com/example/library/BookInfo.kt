package com.example.library

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class BookInfo : AppCompatActivity() {
    private lateinit var upload: Button
    private lateinit var nameEditText: EditText
    private lateinit var aboutEditText: EditText
    private lateinit var numOfPagesEditText: EditText
    private lateinit var categoryNameEditText: EditText
    private lateinit var publishHouseEditText: EditText
    private lateinit var publishDateEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var versionNumberEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_info)


        nameEditText = findViewById(R.id.BookInfo_EditText_Name)
        aboutEditText = findViewById(R.id.BookInfo_EditText_About)
        numOfPagesEditText = findViewById(R.id.BookInfo_EditText_NumOfPages)
        categoryNameEditText = findViewById(R.id.BookInfo_EditText_CategoryName)
        publishHouseEditText = findViewById(R.id.BookInfo_EditText_PublishHouse)
        publishDateEditText = findViewById(R.id.BookInfo_EditText_PublishDate)
        priceEditText = findViewById(R.id.BookInfo_EditText_Price)
        authorEditText = findViewById(R.id.BookInfo_EditText_AuthorName)
        versionNumberEditText = findViewById(R.id.BookInfo_EditText_VersionNumber)
        upload = findViewById(R.id.BookInfo_Button_Upload)

        val user = LoginRegisterActivity.getUserDataFromIntent(intent)


        upload.setOnClickListener {
            val data = getData(user)
            ServerRequest.uploadBook(data, baseContext) {
                val jsonObject = JSONObject(it)
                val error = jsonObject.getBoolean("error")
                if(!error){
                    Toast.makeText(baseContext, "Upload Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(baseContext, "Error while upload book ... try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getData(user: User): MutableMap<String, String> {
        val name = nameEditText.text.toString()
        val about = aboutEditText.text.toString()
        val numOfPages = numOfPagesEditText.text.toString()
        val category = categoryNameEditText.text.toString()
        val publishHouse = publishHouseEditText.text.toString()
        val publishDate = publishDateEditText.text.toString()
        val price = priceEditText.text.toString()
        val author = authorEditText.text.toString()
        val versionNumber = versionNumberEditText.text.toString()



        val data = mutableMapOf<String, String>()
        data["name"] = name
        data["about"] = about
        data["num_of_page"] = numOfPages
        data["category_name"] = category
        data["publish_date"] = publishDate
        data["publish_house"] = publishHouse
        data["price"] = price
        data["author"] = author
        data["version_number"] = versionNumber
        data["username"] = user.getUsername()

        return data
    }
}