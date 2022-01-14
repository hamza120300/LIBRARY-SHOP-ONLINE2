package com.example.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class ShowBookInfo : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var aboutEditText: EditText
    private lateinit var numOfPagesEditText: EditText
    private lateinit var categoryNameEditText: EditText
    private lateinit var publishHouseEditText: EditText
    private lateinit var publishDateEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var authorNameEditText: EditText
    private lateinit var versionNumberEditText: EditText

    private lateinit var editTextView: TextView
    private lateinit var deleteTextView: TextView
    private lateinit var doneTextView: TextView


    private lateinit var commentEditText: EditText
    private lateinit var commentButton: Button
    private lateinit var commentsRecyclerView: RecyclerView

    private lateinit var book: Book
    private lateinit var user: User

    private lateinit var adapter: CommentAdapter
    private var comments: MutableList<Comment> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_book_info)

        nameEditText = findViewById(R.id.ShowBookInfo_EditText_Name)
        aboutEditText = findViewById(R.id.ShowBookInfo_EditText_About)
        numOfPagesEditText = findViewById(R.id.ShowBookInfo_EditText_NumOfPages)
        categoryNameEditText = findViewById(R.id.ShowBookInfo_EditText_CategoryName)
        publishHouseEditText = findViewById(R.id.ShowBookInfo_EditText_PublishHouse)
        publishDateEditText = findViewById(R.id.ShowBookInfo_EditText_PublishDate)
        priceEditText = findViewById(R.id.ShowBookInfo_EditText_Price)
        authorNameEditText = findViewById(R.id.ShowBookInfo_EditText_AuthorName)
        versionNumberEditText = findViewById(R.id.ShowBookInfo_EditText_VersionNumber)

        editTextView = findViewById(R.id.ShowBookInfo_TextView_Edit)
        deleteTextView = findViewById(R.id.ShowBookInfo_TextView_Delete)
        doneTextView = findViewById(R.id.ShowBookInfo_TextView_Done)

        commentEditText = findViewById(R.id.ShowBookInfo_EditText_Comment)
        commentButton = findViewById(R.id.ShowBookInfo_Button_Comment)
        commentsRecyclerView = findViewById(R.id.ShowBookInfo_RecyclerView_ShowComment)

        book = HomeActivity.getBookFromIntent(intent)
        user = LoginRegisterActivity.getUserDataFromIntent(intent)
        getCommentsForThisBook()
        adapter = CommentAdapter(comments)

        commentsRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        commentsRecyclerView.setHasFixedSize(true)
        commentsRecyclerView.adapter = adapter

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

        nameEditText.setText( book.name)
        aboutEditText.setText(book.about)
        numOfPagesEditText.append(": ${book.numOfPages}")
        categoryNameEditText.append(": ${book.categoryName}")
        publishHouseEditText.append(": ${book.publishHouse}")
        publishDateEditText.append(": ${book.publishDate}")
        priceEditText.append(": ${book.price}")
        versionNumberEditText.append(": ${book.versionNumber}")
        authorNameEditText.append(": ${book.authorName}")

        commentButton.setOnClickListener {
            val comment = commentEditText.text.toString()
            if (comment.isNotEmpty()) {
                val data = mutableMapOf<String, String>()
                data["username"] = user.getUsername()
                data["book_id"] = book.id.toString()
                data["comment"] = comment
                Log.i("aaa", "${user.getUsername()}  ${book.id}  ${comment}")
                ServerRequest.addComment(data, baseContext) {
                    val jsonObject = JSONObject(it)
                    val error = jsonObject.getBoolean("error")
                    if (!error) {
                        val jsonArray = jsonObject.getJSONArray("comment")
                        for (i in 0 until jsonArray.length()) {
                            val commentObj = jsonArray.getJSONObject(i)
                            comments.add(getCommentFromJson(commentObj))
                        }
                        adapter.notifyDataSetChanged()

                        Toast.makeText(baseContext, "Add Comment Successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

        Log.i("aaa","${user.getUsername()}  ${book.username}")
        if(user.getUsername() != book.username){
            deleteTextView.visibility = View.INVISIBLE
            editTextView.visibility = View.INVISIBLE
        }

        deleteTextView.setOnClickListener {
            deleteBook()
        }

        editTextView.setOnClickListener {
            editBook()
            doneTextView.visibility = View.VISIBLE
            deleteTextView.visibility = View.INVISIBLE
            editTextView.visibility = View.INVISIBLE
        }
        doneTextView.setOnClickListener {
            done()
        }
    }


    private fun editBook(){
        nameEditText.isEnabled = true
        aboutEditText.isEnabled = true
        numOfPagesEditText.isEnabled = true
        categoryNameEditText.isEnabled = true
        publishHouseEditText.isEnabled = true
        publishDateEditText.isEnabled = true
        priceEditText.isEnabled = true
        authorNameEditText.isEnabled = true
        versionNumberEditText.isEnabled = true
    }

    private fun done(){
        ServerRequest.updateBook(getData(user),baseContext){
            val jsonObject = JSONObject(it)
            val error = jsonObject.getBoolean("error")
            if(!error) Toast.makeText(baseContext, "Update Successfully", Toast.LENGTH_SHORT).show()
            else Toast.makeText(baseContext, "error", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun deleteBook(){
        val data = mutableMapOf<String,String>()
        data["book_id"] = book.id.toString()
        data["username"] = user.getUsername()
        ServerRequest.deleteBook(data,baseContext){
            val jsonObject = JSONObject(it)
            val error = jsonObject.getBoolean("error")
            if(!error) Toast.makeText(baseContext, "Delete Successfully", Toast.LENGTH_SHORT).show()
            else Toast.makeText(baseContext, "error", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getData(user: User): MutableMap<String, String> {
        val name = nameEditText.text.toString()
        Log.i("aaa","${name}")
        val about = aboutEditText.text.toString()
        val numOfPages = numOfPagesEditText.text.toString()
        val category = categoryNameEditText.text.toString()
        val publishHouse = publishHouseEditText.text.toString()
        val publishDate = publishDateEditText.text.toString()
        val price = priceEditText.text.toString()
        val author = authorNameEditText.text.toString()
        val versionNumber = versionNumberEditText.text.toString()

        val data = mutableMapOf<String, String>()
        data["name"] = name
        data["about"] = about
        data["num_of_page"] = numOfPages
        data["category_name"] = category
        data["publish_date"] = publishHouse
        data["publish_house"] = publishDate
        data["price"] = price
        data["book_id"] = book.id.toString()
        data["author"] = author
        data["version_number"] = versionNumber
        data["username"] = user.getUsername()

        return data
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getCommentsForThisBook() {

        val data = mutableMapOf<String, String>()
        data["book_id"] = book.id.toString()
        ServerRequest.getAllComment(data, baseContext) {
            val jsonObject = JSONObject(it)
            val jsonArray = jsonObject.getJSONArray("comments")
            for (i in 0 until jsonArray.length()) {
                val comment = jsonArray.getJSONObject(i)
                comments.add(getCommentFromJson(comment))
            }
            adapter.notifyDataSetChanged()
            commentsRecyclerView.smoothScrollToPosition(0)
        }
    }

    private fun getCommentFromJson(jsonObject: JSONObject):Comment{
        val id = jsonObject.getInt("id")
        val comment = jsonObject.getString("comment")
        val username = jsonObject.getString("username")
        val date = jsonObject.getString("date")
        return Comment(id, comment, username, date)
    }

}

class CommentAdapter(private val listOfComment: MutableList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.VH>() {
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.CommentLayout_TextView_Username)
        val commentTextView: TextView = itemView.findViewById(R.id.CommentLayout_TextView_Comment)
        val dateTextView: TextView = itemView.findViewById(R.id.CommentLayout_TextView_Date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comment_layout, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val comment = listOfComment[position]
        holder.commentTextView.text = comment.comment
        holder.usernameTextView.text = comment.username
        holder.dateTextView.text = comment.date
    }

    override fun getItemCount(): Int {
        return listOfComment.size
    }
}