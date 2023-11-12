## Distributed Shared Whiteboard

This repository contains the code for a distributed shared whiteboard. The manager can create a canvas; users can request to join the whiteboard. Multiple users are able to draw different shapes and type text anywhere on an interactive canvas. They can also communicate with each other in a chat window at the same time. The manager can get access to file management and user management features.

Class design can be found in the report included in this repository.

The executable jar files used to run the system are included:
* Command to start the server (manager): ```> java –jar CreateWhiteBoard.jar <ip> <port> <username>```
* Command to start the client (user): > ```java –jar JoinWhiteBoard.jar <ip> <port> <username>```
