<?php
$ch = curl_init("https://fcm.googleapis.com/fcm/send");

//The device token.
$token = $_POST['token'];
//Title of the Notification.
$title = "A Patient needs yor help";

//Body of the Notification.
$body = "A patient needs blood";

//Creating the notification array.
$notification = array('title' =>$title , 'body' => $body);

//This array contains, the token and the notification. The 'to' attribute stores the token.
$arrayToSend = array('to' => $token, 'notification' => $notification);

//Generating JSON encoded string form the above array.
$json = json_encode($arrayToSend);
//call eka gnin

//Setup headers:
$headers = array();
$headers[] = 'Content-Type: application/json';
$headers[] = 'Authorization: key=AAAAz2-tAvA:APA91bFgkHbayOjZdDNDukvwo94vdc666FpOadKpAwmr9EtJC34TwT5FMjhDygJcBQEiLvEGQ-uIHgdXOfofWgTS3z0Z4TgEMAq6ufwB5EZOTEnhifGmIBPFUqI4D2Nfn5aJfRSWlmNE';

//Setup curl, add headers and post parameters.
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");                                                                     
curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
curl_setopt($ch, CURLOPT_HTTPHEADER,$headers);       

//Send the request
curl_exec($ch);

//Close request
curl_close($ch);

?>