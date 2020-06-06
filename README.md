# Message Service

Message Service is a Rest API for delivering, fetching and deleting messages to a particular email/username/phoneNumber.

## Installation

Use Maven to install and make the service available.

```bash
./mvnw install && ./mvnw spring-boot:run -pl message-service-api
```

Note: It might be that your maven is configured with the companies artifactory which will lead to failure of the project. Resolution is to use a generic settings.xml file placed in .m2 directory.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository>${user.home}/.m2/repository</localRepository>
  <interactiveMode>true</interactiveMode>
  <offline>false</offline>

</settings>
```


## Examples

After the service is up, curl can be used for accessing the service. Below are some examples of different operations performed using curl



####Send Message to the service
   * To Multiple emails/users/phoneNumbers
```bash
curl --header "Content-Type: application/json" --request POST --data '{"messageInfo":{"messageBody":"Hello.."},"toEmails":["akhilesh1","akhilesh2"]}' http://localhost:8080/message
```
   * To single email/user/phoneNumber
    
```bash
curl --header "Content-Type: application/json" --request POST --data '{"messageInfo":{"messageBody":"Hello.."},"toEmails":["akhilesh1"]}' http://localhost:8080/message
```

####Retrieve 
   * All UnFetched messages for a email address:
```bash
curl --header "Content-Type: application/json" --request GET http://localhost:8080/messages/akhilesh1\?unFetched=true
```
   * All messages based on Indexes(Indexes needs to be separated by hyphen) with Specified Indexes: 
```bash
curl --header "Content-Type: application/json" --request GET http://localhost:8080/messages/akhilesh1\?indexes=0-1
```
   * All messages based on  Default Indexes(0-9): 
```bash
curl --header "Content-Type: application/json" --request GET http://localhost:8080/messages/akhilesh2
```
* Deleting Messages with indexes for a particular email/username/phoneNr
```bash
curl --header "Content-Type: application/json" --request DELETE http://localhost:8080/messages/akhilesh1/0-1
```

#Health Check

To check the health of the system, actuator is used which comes with Spring Boot

```bash
http://localhost:8080/actuator/health
```

#Assumptions

* User will be uniquely identified by his/her email address.
* 