package com.example.library

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject



class LoginRegisterActivity : AppCompatActivity() {


    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginRegisterButton: Button
    private lateinit var haveAccountOrNot: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        usernameEditText = findViewById(R.id.LoginRegister_EditText_Username)
        passwordEditText = findViewById(R.id.LoginRegister_EditText_Password)
        loginRegisterButton = findViewById(R.id.LoginRegister_Button_Login)
        haveAccountOrNot = findViewById(R.id.LoginRegister_TextView_DoNotHaveAccount)
        progressBar = findViewById(R.id.LoginRegister_ProgressBar)
    }

    override fun onStart() {
        super.onStart()

        // button click listener
        loginRegisterButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // create user object
            val user = User(username,password)
            if (loginRegister(loginRegisterButton)) {
                login(user, progressBar)
            } else {
                register(user, progressBar)
            }
        }

        // textView click listener
        haveAccountOrNot.setOnClickListener {
            if (loginRegister(loginRegisterButton)) {
                goToRegister(loginRegisterButton, haveAccountOrNot)
            } else {
                goToLogin(loginRegisterButton, haveAccountOrNot)
            }
        }
    }

    private fun login(user:User, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        user.login(baseContext){
            val jsonObject = JSONObject(it)
            val error = jsonObject.getBoolean("error")

            progressBar.visibility = View.INVISIBLE
            if(!error) {
                Toast.makeText(baseContext, "Login Successfully", Toast.LENGTH_SHORT).show()
                user.setCoin(jsonObject.getInt("coins"))

                var intent = Intent(baseContext,HomeActivity::class.java)
                intent = putUserDataInIntent(user,intent)
                startActivity(intent)
            }
            else{
                Toast.makeText(baseContext, "No user with this username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(user:User, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        user.register(baseContext){
            val jsonObject = JSONObject(it)
            val error = jsonObject.getBoolean("error")

            progressBar.visibility = View.INVISIBLE
            if(!error) {
                Toast.makeText(baseContext, "register Successfully", Toast.LENGTH_SHORT).show()
                user.setCoin(jsonObject.getInt("coins"))

                var intent = Intent(baseContext,HomeActivity::class.java)
                intent = putUserDataInIntent(user,intent)
                startActivity(intent)
            }
            else{
                Toast.makeText(baseContext, "Error occur while register ... try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToRegister(button: Button, textView: TextView) {
        button.text = resources.getText(R.string.register)
        textView.text = resources.getText(R.string.oldUser)
    }

    private fun goToLogin(button: Button, textView: TextView) {
        button.text = resources.getText(R.string.login)
        textView.text = resources.getText(R.string.newUser)
    }

    // if login --------> true
    // else --------> false
    private fun loginRegister(button: Button): Boolean {
        return (button.text == resources.getText(R.string.login))
    }



    companion object UserAndIntent{
        private const val USERNAME: String ="username"
        private const val PASSWORD: String = "password"
        private const val COINS: String = "coins"

        fun putUserDataInIntent(user:User,intent: Intent):Intent{
            intent.putExtra(COINS,user.getCoin())
            intent.putExtra(USERNAME,user.getUsername())
            intent.putExtra(PASSWORD,user.getPassword())
            return intent
        }

        fun getUserDataFromIntent(intent: Intent):User{
            val username = intent.getStringExtra(USERNAME)
            val password = intent.getStringExtra(PASSWORD)
            val coins = intent.getIntExtra(COINS,0)
            val user = User(username!!,password!!)
            user.setCoin(coins)
            return user
        }
    }
}