<?php

include_once '../includes/HidenOperations.php';

$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST'){

		if(isset($_POST['username']) and 
			isset($_POST['book_id']) and
			isset($_POST['num_of_days']) ){

			// create operation object
				$operation = new Operation();
				if($operation -> borrowing($_POST['username'],$_POST['book_id'],$_POST['num_of_days'])){
					$response['error'] = false;
					$response['message'] = "borrowing successfully";
				}else{
					$response['error'] = true;
					$response['message'] = "error ";
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