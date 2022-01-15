<?php

include_once dirname(__FILE__) . '/Connect.php';

	class Operation{

		private $conn;

		function __construct(){
			$connect = new Connect();
			$this -> conn = $connect-> getConnection();
		}
 
		function registerUser($username,$password){
			$pass = md5($password);
			$coin = 500;
			$query = $this -> conn -> prepare("insert into `user` values (?,?,?)");
			$query -> bind_param("sss",$username,$pass,$coin);
			return $query -> execute();
		}

		function loginUser($username,$password){
			$pass = md5($password);
			$query = $this -> conn -> query("select * from `user` where username = '".$username."' and password = '".$pass."'" );
			return $query->num_rows;
		}

		function getCoins($username){
			$query = $this -> conn -> query("select coins from `user` where username = '".$username."'" );
			while($row = $query->fetch_assoc()){
				return $row['coins'];
			}
		}

		function uploadBook($name,$about,$num_of_page,$category,$publish_house,$publish_date,$price,$version_number,$username,$author){
			$currentDate = date("Y-m-d");
			$query = $this -> conn -> prepare("insert into book(name, about, numOfPage, category_name, publish_date, publish_house, price, version_number, username, upload_date,author ) values (?,?,?,?,?,?,?,?,?,?,?)");
			$query -> bind_param("sssssssssss",$name,$about,$num_of_page,$category,$publish_date,$publish_house,$price,$version_number,$username,$currentDate,$author);
			return $query -> execute();
		}

		function getAllBooks(){
			$query = $this -> conn -> query("select * from book where isRemove = 0" );
			$list = array();
			while($row = $query->fetch_assoc()){
				array_push($list, $row);
			}
			return $list;
		}

		function addComment($username,$book_id,$comment){
			$currentDate = date("Y-m-d");
			$query = $this -> conn -> prepare("insert into `comment`(username,book_id,comment,`date`) values (?,?,?,?)");
			$query -> bind_param("ssss",$username,$book_id,$comment,$currentDate);
			$query -> execute();
			return $this -> conn -> insert_id;
		}

		function getComment($id){
			$query = $this -> conn -> query("select * from comment where id = ".$id );
			$list = array();
			while($row = $query->fetch_assoc()){
				array_push($list, $row);
			}
			return $list;
		}


		function getAllComments($book_id){
			$query = $this -> conn -> query("select * from comment where book_id = ".$book_id );
			$list = array();
			while($row = $query->fetch_assoc()){
				array_push($list, $row);
			}
			return $list;
		}


		function borrowing($username,$book_id,$num_of_days){
			$currentDate = date("Y-m-d");
			$query = $this -> conn -> prepare("insert into `borrowing`(username,book_id,num_of_days,date_of_borrowing) values (?,?,?,?)");
			$query -> bind_param("ssss",$username,$book_id,$num_of_days,$currentDate);
			return $query -> execute();
		}

		function getAllMyUploadBooks($username){
			$query = $this -> conn -> query("select * from book where username = '".$username."' and isRemove = 0" );
			$list = array();
			while($row = $query->fetch_assoc()){
				array_push($list, $row);
			}
			return $list;
		}

		function getAllMyBorrowBooks($username){
			$query = $this -> conn -> query("
				select book.name, book.about, book.numOfPage , book.category_name ,book.publish_date , 
	   			book.publish_house , book.price , book.version_number , book.author , book.id ,book.username
                from book,borrowing
                WHERE borrowing.username = '".$username."' and book.id = borrowing.book_id and isRemove = 0" );
			
			$list = array();
			while($row = $query->fetch_assoc()){
				array_push($list, $row);
			}
			return $list;
		}

		function updateBook($book_id,$name,$about,$num_of_page,$category,$publish_house,$publish_date,$price,$version_number,$username,$author){
			$query = $this -> conn -> prepare(
		"update book
	    set book.name = ? , book.about = ? , book.numOfPage = ? , book.category_name = ? , book.publish_date = ? , 
	    book.publish_house = ? , book.price = ? , book.version_number = ? , book.author = ?
		where book.id = ? and book.username = ? ");
			$query -> bind_param("sssssssssss",$name,$about,$num_of_page,$category,$publish_date,$publish_house,$price,$version_number,$author,$book_id,$username);

			$currentDate = date("Y-m-d");
			$query2 = $this -> conn -> prepare("insert into `action_history` values (?,?,'1','0',?)");
			$query2 -> bind_param("sss",$username,$book_id,$currentDate);
			

			return $query -> execute() and $query2 -> execute() ;
		}

		function deleteBook($username,$book_id){
			$query = $this -> conn -> prepare("update book set isRemove = 1 where id = '".$book_id."'");

			$currentDate = date("Y-m-d");
			$query2 = $this -> conn -> prepare("insert into `action_history` values (?,?,'0','1',?)");
			$query2 -> bind_param("sss",$username,$book_id,$currentDate);
			
			return $query -> execute() and $query2 -> execute() ;
		}
	}