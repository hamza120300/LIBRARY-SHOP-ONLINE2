<?php

include_once '../includes/HidenOperations.php';

$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST'){

	// create operation object
		$operation = new Operation();
		$response["books"] = $operation -> getAllBooks();
		$response['error'] = false;
		$response['message'] = "Download successfully";
	}
	else{
		$response['error'] = true;
		$response['message'] = "Invalid Request ... You must use post method";
	}

echo json_encode($response);