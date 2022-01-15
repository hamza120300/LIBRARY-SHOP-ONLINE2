<?php


include_once '../includes/HidenOperations.php';

$response = array();

	if($_SERVER['REQUEST_METHOD'] == 'POST'){

		if(isset($_POST['name']) and 
			isset($_POST['about']) and 
			 isset($_POST['num_of_page']) and 
			  isset($_POST['category_name']) and 
			   isset($_POST['publish_date']) and 
			    isset($_POST['publish_house']) and 
			     isset($_POST['price'] )and 
				  isset($_POST['version_number']) and
				  isset($_POST['author']) and
				   isset($_POST['username'])){

			// create operation object
				$operation = new Operation();
				if($operation -> uploadBook($_POST['name'],$_POST['about'],$_POST['num_of_page'],$_POST['category_name'],$_POST['publish_house'],$_POST['publish_date'],$_POST['price'],$_POST['version_number'],$_POST['username'],$_POST['author'])){
					$response['error'] = false;
					$response['message'] = "Upload successfully";
				}else{
					$response['error'] = true;
					$response['message'] = "Error occured while upload book";
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