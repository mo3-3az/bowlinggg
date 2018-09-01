#bowlinggg
Vert.x Based RESTful Server

###Build
mvn package

###Run
Run `Application.main()` or java -jar target/bowlinggg-1.0-SNAPSHOT-fat.jar

###API
Start new game
POST /v1/games -> Response: CREATED(201), Empty body.

Update game
PUT /v1/games/{id}/{pinsKnocked} 
    Successful -> Response: NO_CONTENT(204), Empty body.
    Failure (Invalid id) -> Response: NO_FOUND(404), Error object as json string.
    Failure (Invalid pins) -> Response: BAD_REQUEST(400), Error object as json string.

Get game
GET /v1/games/{id}
    Successful -> Response: NO_CONTENT(200), Game as a json string.
    Failure (Invalid id) -> Response: NO_FOUND(404), Error object as json string.

Delete game
DELETE /v1/games/{id}/{pinsKnocked}
    Successful -> Response: NO_CONTENT(204), Empty body.
    Failure (Invalid id) -> Response: NO_FOUND(404), Error object as json string.