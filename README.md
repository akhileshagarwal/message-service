# Foobar

Message Service is a Rest API for delivering, fetching and deleting messages to a particular email/username/phoneNumber.

## Installation

Use Maven to install and make the service available.

```bash
./mvnw install && ./mvnw spring-boot:run -pl message-service-api
```

## Examples

After the service is up, curl can be used for accessing the service. Below are some examples of different operations performed using curl

```python

//Send Messages
1. To Multiple emails/users/phoneNumbers
curl --header "Content-Type: application/json" --request POST --data '{"messageInfo":{"messageBody":"Hello.."},"toEmails":["akhilesh1","akhilesh2"]}' http://localhost:8080/message

2. To single email/user/phoneNumber
  curl --header "Content-Type: application/json" --request POST --data '{"messageInfo":{"messageBody":"Hello.."},"toEmails":["akhilesh1"]}' http://localhost:8080/message

//Retrieve Messages from the service
1. All UnFetched messages for a email address:
   	curl --header "Content-Type: application/json" --request GET http://localhost:8080/messages/akhilesh1\?unFetched=true
   
2. All messages based on Indexes(Indexes needs to be separated by hyphen): 
    2.1 With Specified Indexes: curl --header "Content-Type: application/json" --request GET http://localhost:8080/messages/akhilesh1\?indexes=0-1
    2.2 With Default Indexes(0-9): curl --header "Content-Type: application/json" --request GET http://localhost:8080/messages/akhilesh2

3. Deleting Messages with indexes for a particular email/username/phoneNr
    curl --header "Content-Type: application/json" --request DELETE http://localhost:8080/messages/akhilesh1/0-1
```

#Health Check

To check the health of the system, actuator is used which comes with Spring Boot

```python
http://localhost:8080/actuator/health
```