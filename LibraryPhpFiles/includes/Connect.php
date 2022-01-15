<?php

include_once dirname(__FILE__) . '/Constants.php';

	class Connect{
		
		private $connection;

		function __construct(){
			$this -> connection = new mysqli(host,user,password,database);
			if($this -> connection -> connect_errno){
				echo "error";
			}
		}

		function getConnection(){
			return $this -> connection;
		}
	}