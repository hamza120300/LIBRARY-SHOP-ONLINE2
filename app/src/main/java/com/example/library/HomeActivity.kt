package com.example.library

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject


class HomeActivity : AppCompatActivity(), Adapter.Listener {

    private lateinit var uploadBook: FloatingActionButton
    private lateinit var account: FloatingActionButton
    private lateinit var coinsTextView: TextView
    private lateinit var welcomeTextView: TextView
    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var adapter: Adapter
    private var listOfBook = mutableListOf<Book>()
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        uploadBook = findViewById(R.id.Home_FAB_uploadBook)
        account = findViewById(R.id.Home_FAB_Account)
        coinsTextView = findViewById(R.id.Home_TextView_Coins)
        welcomeTextView = findViewById(R.id.Home_TextView_Welcome)
        bookRecyclerView = findViewById(R.id.Home_RecyclerView_Books)
        user = LoginRegisterActivity.getUserDataFromIntent(intent)

        adapter = Adapter(listOfBook,user,false)

        bookRecyclerView.setHasFixedSize(true)
        bookRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        bookRecyclerView.adapter = adapter




        welcomeTextView.append(user.getUsername())

        uploadBook.setOnClickListener {
            var intent = Intent(baseContext, BookInfo::class.java)
            intent = LoginRegisterActivity.putUserDataInIntent(user, intent)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()


        account.setOnClickListener {
            var intent = Intent(baseContext,AccountActivity::class.java)
            intent = LoginRegisterActivity.putUserDataInIntent(user,intent)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        getCoins()
        getBooks()
    }

    @SuppressLint("SetTextI18n")
    private fun getCoins(){
        val data = mutableMapOf<String,String>()
        data["username"] = user.getUsername()
        ServerRequest.getCoins(data,baseContext){
            val jsonObject = JSONObject(it)
            val coins = jsonObject.getInt("coins").toString()
            coinsTextView.text = "Coins: $coins"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getBooks() {
        listOfBook.clear()
        ServerRequest.downloadBooks(baseContext) {
            val jsonObject = JSONObject(it)
            val jsonArray = jsonObject.getJSONArray("books")
            for (i in 0 until jsonArray.length()) {
                val book = jsonArray.getJSONObject(i)
                listOfBook.add(getBookFromJsonObject(book))
            }

            adapter.notifyDataSetChanged()
            bookRecyclerView.smoothScrollToPosition(0)

        }
    }



    override fun onClick(book: Book) {
        var intent = Intent(baseContext, ShowBookInfo::class.java)
        intent = putBookInIntent(book, intent)
        intent = LoginRegisterActivity.putUserDataInIntent(user, intent)
        startActivity(intent)
    }

    override fun onClickBorrow(book: Book) {
        var intent = Intent(baseContext,BorrowingActivity::class.java)
        intent = putBookInIntent(book,intent)
        intent = LoginRegisterActivity.putUserDataInIntent(user,intent)
        startActivity(intent)
    }

    companion object {
        private const val ID = "id"
        private const val NAME = "name"
        private const val ABOUT = "about"
        private const val NUMBER_OF_PAGES = "numberOfPages"
        private const val CATEGORY_NAME = "category"
        private const val PUBLISH_HOUSE = "publishHouse"
        private const val PUBLISH_DATE = "publishDate"
        private const val PRICE = "price"
        private const val VERSION_NUMBER = "versionNumber"
        private const val AUTHOR_NAME = "authorName"
        private const val USERNAME1 = "username1"

        fun putBookInIntent(book: Book, intent: Intent): Intent {
            intent.putExtra(NAME, book.name)
            intent.putExtra(ABOUT, book.about)
            intent.putExtra(NUMBER_OF_PAGES, book.numOfPages)
            intent.putExtra(CATEGORY_NAME, book.categoryName)
            intent.putExtra(PUBLISH_HOUSE, book.publishHouse)
            intent.putExtra(PUBLISH_DATE, book.publishDate)
            intent.putExtra(PRICE, book.price)
            intent.putExtra(VERSION_NUMBER, book.versionNumber)
            intent.putExtra(AUTHOR_NAME, book.authorName)
            intent.putExtra(ID,book.id)
            intent.putExtra(USERNAME1,book.username)
            Log.i("aaa1" ,book.username )
            return intent
        }

        fun getBookFromIntent(intent: Intent): Book {
            val name = intent.getStringExtra(NAME)
            val about = intent.getStringExtra(ABOUT)
            val numOfPages = intent.getIntExtra(NUMBER_OF_PAGES, 0)
            val categoryName = intent.getStringExtra(CATEGORY_NAME)
            val publishHouse = intent.getStringExtra(PUBLISH_HOUSE)
            val publishDate = intent.getStringExtra(PUBLISH_DATE)
            val price = intent.getFloatExtra(PRICE, 0f)
            val versionNumber = intent.getIntExtra(VERSION_NUMBER, 0)
            val authorName = intent.getStringExtra(AUTHOR_NAME)
            val id = intent.getIntExtra(ID,-1)
            val username = intent.getStringExtra(USERNAME1)
            Log.i("aaa12",username!!)
            val book = Book(
                name!!,
                about!!,
                numOfPages,
                categoryName!!,
                publishDate!!,
                publishHouse!!,
                price,
                versionNumber,
                authorName!!,
                username!!
            )
            book.id = id
            return book
        }

        fun getBookFromJsonObject(book: JSONObject): Book {
            val id = book.getInt("id")
            val name = book.getString("name")
            val about = book.getString("about")
            val numOfPage = book.getInt("numOfPage")
            val categoryName = book.getString("category_name")
            val publishDate = book.getString("publish_date")
            val publishHouse = book.getString("publish_house")
            val price = book.getDouble("price").toFloat()
            val versionNumber = book.getInt("version_number")
            val authorName = book.getString("author")
            val username = book.getString("username")
            Log.i("aaa","$publishDate  $publishHouse $username")
            val bookBook = Book(
                name,
                about,
                numOfPage,
                categoryName,
                publishDate.toString(),
                publishHouse,
                price,
                versionNumber,
                authorName,
                username
            )
            bookBook.isUploaded(id)
            return bookBook
        }
    }
}

class Adapter(private val books: MutableList<Book>,private val user: User,private val flag:Boolean) : RecyclerView.Adapter<Adapter.VH>() {

    private lateinit var listener: Listener

    interface Listener {
        fun onClick(book: Book)
        fun onClickBorrow(book: Book)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.context is Listener) listener = recyclerView.context as Listener
        else throw Exception("You don't implements from Listener")
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookNameTextView: TextView = itemView.findViewById(R.id.Book_TextView_Name)
        val bookImageImageView: ImageView = itemView.findViewById(R.id.Book_ImageView_Image)
        val bookAboutTextView: TextView = itemView.findViewById(R.id.Book_TextView_About)
        val bookPriceTextView: TextView = itemView.findViewById(R.id.Book_TextView_Price)
        val viewButton: Button = itemView.findViewById(R.id.Book_Button_View)
        val borrowingButton: Button = itemView.findViewById(R.id.Book_Button_Borrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_layout, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val book = books[position]

        if (flag){
            val param = holder.bookImageImageView.layoutParams
            param.width = 800
            holder.bookImageImageView.layoutParams = param
        }
        holder.bookNameTextView.text = book.name
        holder.bookAboutTextView.text = book.about
        holder.bookPriceTextView.text = book.price.toString()

        holder.borrowingButton.isEnabled = book.username != user.getUsername()

        holder.viewButton.setOnClickListener {
            listener.onClick(book)
        }

        holder.borrowingButton.setOnClickListener {
            listener.onClickBorrow(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }
}