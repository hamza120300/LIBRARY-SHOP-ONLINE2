package com.example.library

class Constant {

    companion object{
        private const val url = "http://192.168.56.1/LibraryPhpFiles/android/"
        const val registerUrl = "${url}RegisterUser.php"
        const val loginUrl = "${url}Login.php"
        const val uploadBookUrl = "${url}UploadBook.php"
        const val downloadBooksUrl = "${url}DownloadBooks.php"
        const val commentUrl = "${url}AddComment.php"
        const val getAllCommentUrl = "${url}GetAllComment.php"
        const val borrowingUrl = "${url}Borrowing.php"
        const val getCoinsUrl = "${url}getCoins.php"
        const val getUploadBooksUrl = "${url}GetMyUploadBook.php"
        const val getBorrowBooksUrl = "${url}GetMyBorrowBook.php"
        const val updateBooksUrl = "${url}UpdateBook.php"
        const val deleteBooksUrl = "${url}DeleteBook.php"
    }
}