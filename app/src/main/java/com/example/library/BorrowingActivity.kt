package com.example.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class BorrowingActivity : AppCompatActivity() {
    private lateinit var bookNameTextView: TextView
    private lateinit var bookAboutTextView: TextView
    private lateinit var bookPriceTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var numOfDaysNumberPicker: NumberPicker
    private lateinit var borrowingButton: Button
    private lateinit var book: Book
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrowing)
        bookNameTextView = findViewById(R.id.BorrowingActivity_TextView_Name)
        bookAboutTextView = findViewById(R.id.BorrowingActivity_TextView_About)
        bookPriceTextView = findViewById(R.id.BorrowingActivity_TextView_Price)
        totalPriceTextView = findViewById(R.id.BorrowingActivity_TextView_TotalPrice)
        numOfDaysNumberPicker = findViewById(R.id.BorrowingActivity_NumberPicker_NumOfBorrowing)
        borrowingButton = findViewById(R.id.BorrowingActivity_Button_Borrowing)
        book = HomeActivity.getBookFromIntent(intent)
        user = LoginRegisterActivity.getUserDataFromIntent(intent)

    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        bookNameTextView.text = book.name
        bookAboutTextView.text = book.about
        bookPriceTextView.append(" ${book.price}")
        totalPriceTextView.text = "Total price: ${book.price}"
        numOfDaysNumberPicker.minValue = 1
        numOfDaysNumberPicker.maxValue = 365
        numOfDaysNumberPicker.setOnValueChangedListener { numberPicker, i, i2 ->
            totalPriceTextView.text = "Total price: ${i2 * book.price}"
        }

        borrowingButton.setOnClickListener {
            val numOfDays = numOfDaysNumberPicker.value
            val data = mutableMapOf<String,String>()
            data["username"] = user.getUsername()
            data["book_id"] = book.id.toString()
            data["num_of_days"] = numOfDays.toString()
            ServerRequest.borrowing(data,baseContext){
                val jsonObject = JSONObject(it)
                val error = jsonObject.getBoolean("error")
                if(!error){
                    Toast.makeText(baseContext, "Borrowing Successfully", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(baseContext, "Error -> you may borrowing this book or your coins don't enough", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}