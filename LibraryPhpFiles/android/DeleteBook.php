<?php

include_once '../includes/HidenOperations.php';
$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST'){

			if(isset($_POST['username']) and 
				isset($_POST['book_id']))
			{

				// create operation object
					$operation = new Operation();

					if($operation -> deleteBook($_POST['username'],$_POST['book_id'])){
						$response['error'] = false;
					$response['message'] = "Delete successfully";
				}else{
					$response['error'] = true;
					$response['message'] = "error";
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