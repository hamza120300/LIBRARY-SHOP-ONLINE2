<?php

include_once '../includes/HidenOperations.php';

$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST'){

		if(isset($_POST['username']) and 
			isset($_POST['password'])){

			// create operation object
				$operation = new Operation();
				if($operation -> registerUser($_POST['username'],$_POST['password'])){
					$response['error'] = false;
					$response['message'] = "Register successfully";
					$response['coins'] = $operation -> getCoins($_POST['username']);
				}else{
					$response['error'] = true;
					$response['message'] = "May be error occur";
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