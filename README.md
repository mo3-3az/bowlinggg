#bowlinggg
Vert.x Based RESTful Server

###Build
mvn package

###Run
Run `Application.main()` or `java -jar target/bowlinggg-1.0-SNAPSHOT-fat.jar`

###API
Start new game <br/>
POST /v1/games <br/>
    Successful -> Response: CREATED(201), Empty body. <br/>
    Failure (Error) -> Response: INTERNAL_SERVER_ERROR(503), Error object as json string. <br/>
 <br/>
Update game <br/>
PUT /v1/games/{id}/{pinsKnocked} <br/> 
    Successful -> Response: NO_CONTENT(204), Empty body. <br/>
    Failure (Invalid id) -> Response: NO_FOUND(404), Error object as json string. <br/>
    Failure (Invalid pins) -> Response: BAD_REQUEST(400), Error object as json string. <br/>
 <br/>
Get game <br/>
GET /v1/games/{id} <br/>
    Successful -> Response: NO_CONTENT(200), Game as a json string. <br/>
    Failure (Invalid id) -> Response: NO_FOUND(404), Error object as json string. <br/>
 <br/>
Delete game <br/>
DELETE /v1/games/{id}/{pinsKnocked} <br/>
    Successful -> Response: NO_CONTENT(204), Empty body. <br/>
    Failure (Invalid id) -> Response: NO_FOUND(404), Error object as json string. <br/>
    
###Unit Test & Test Coverage
Test using JUnit 4 & Vert.x<br/>
Run `mvn test`<br/>
<br/>
Coverage report by JaCoCo<br/>
`target/site/jacoco/index.html`<br/>