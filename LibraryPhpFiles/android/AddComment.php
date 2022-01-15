<?php

include_once '../includes/HidenOperations.php';
$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST'){

		if(isset($_POST['username']) and 
			isset($_POST['book_id']) and 	
			 isset($_POST['comment'])){

			// create operation object
				$operation = new Operation();
				$comment_id = $operation -> addComment($_POST['username'],$_POST['book_id'],$_POST['comment']);
				if($comment_id != 0 ){
					$response['comment'] = $operation -> getComment($comment_id);
					$response['error'] = false;
					$response['message'] = "add Comment successfully";
				}else{
					$response['error'] = true;
					$response['message'] = "No user with this username";
				}

		}else{
			$response['error'] = true;
			$response['message'] = "missing data";
		}
	}
	else{
		$response['error'] = true;
		$response['message'] = "Invalid Request ... You must use post method";
	}

echo json_encode($response);