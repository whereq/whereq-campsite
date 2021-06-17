# Campsite API Demo

### Background
An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it was
decided to open it up for the general public to experience the pristine uncharted territory.

The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, it
was decided to come up with an online web application to manage the reservations. You are responsible for design and development of a REST
API service that will manage the campsite reservations.

To streamline the reservations a few constraints need to be in place -

* The campsite will be free for all.
* The campsite can be reserved for max 3 days.
* The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
* Reservations can be cancelled anytime.
* For sake of simplicity assume the check-in & check-out time is 12:00 AM


### System Requirements
The following guides illustrate how to use some features concretely:

* The users will need to find out when the campsite is available. So the system should expose an API to provide information of the
availability of the campsite for a given date range with the default being 1 month.
* Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsite
along with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful.
* The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allow
modification/cancellation of an existing reservation
* Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlapping
date(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.
* Provide appropriate error messages to the caller to indicate the error cases.
* In general, the system should be able to handle large volume of requests for getting the campsite availability.
There are no restrictions on how reservations are stored as as long as system constraints are not violated.

### Solutions Stack
* JDK 11
* Spring Boot
    * gradlew clean build
* H2 Database
    * http://localhost:8080/h2-console
    * Credential: sa/password
* Swagger
    * http://localhost:8080/swagger-ui/index.html
    
### High Level Design Specification
* H2 Database is used as the persistence layer, it will be initialized when the Spring Boot application started up;
* Campsite availability is managed by a string (bitmap with length as 366 chars), the index is the day of the year, '0' means available, '1' means reserved, the bitmap is a cycle of the year the start index of the availability bitmap is always the current day of the year;
* Campsite availability is associated to the campsite only; 
* Campsite reservation means when the campsite is reserved by whom;
* Campsite reservation request process flow:
    1. Front-end post CampsiteReservationRequest;
    2. Controller receive the CampsiteReservationRequest, build CampsiteEvent accordingly;
    3. Controller invoke AsyncEventHandler to post CampsiteEvent with an global unique sequence number to event bus;
    4. AsyncEventHandler will start an infinite loop to poll from the CAMPSITE_EVENT_QUEUE (a global ConcurrentHashMap) to wait for the result;
    5. CampsiteEventListener listen to the event bus, invoke CampsiteService to process the CampsiteEvent;
    6. CampsiteEventListener get the process result from CampsiteService, put it into the CAMPSITE_EVENT_QUEUE, the key is the global unique sequence number associated with the CampsiteEvent;
    7. AsyncEventHandler get the process result from CAMPSITE_EVENT_QUEUE with the global unique sequence number;
    8. AsyncEventHandler return the result to Controller;
    9. Controller wrap the result as CampsiteReservationResponse and return to front-end;
* Modify an existing campsite reservation is based on the assumptions:
    * The target reservation date slot is guaranteed available, in real case front-end will only show the available date slot;
    * The modification can be only happened under the same user who booked the previous reservation;
* Cancel an existing campsite reservation is based on the assumptions:
    * The campsite reservation can only be cancelled by the user who booked the campsite reservation;
* Exception handling is done in centralized ControllerExceptionHandler;
* The date format across front-end to back-end is **yyyyMMdd**;
    
### The Current Constraints
* The current application can be only tested and deployed as a single instance;
* The campsite availability range is one year;
* Some H2 native queries are used in the code;

### Future evolve
* Consider to use Redis as request queue to support multiple instances;
* Get rid of the native queries with either JPQL or lift the logic to application level;
* Add application profiles for different environments;
* More unit test cases to increase code coverage;
* More test cases for API layers, e.g. Postman, Cucumber;
* Security control;
     



