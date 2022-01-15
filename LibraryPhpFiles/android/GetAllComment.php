<?php

include_once '../includes/HidenOperations.php';
$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST'){

		if(isset($_POST['book_id'])){

			// create operation object
				$operation = new Operation();
				$response['comments'] = $operation -> getAllComments($_POST['book_id']);
				$response['error'] = false;
				$response['message'] = "add Comment successfully";
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