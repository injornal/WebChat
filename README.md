# WebChat 
## Description

A socket-based chatting A2P desktop application. 
Allows for creating users, chats, and sending messages within those chats.
All of the messages are saved on the server, so every time a user logs in, their messages and chats get downloaded from the server.
Allows for multiple connections with the same username.

Both the server and the client applications are completely asynchronous and thread-safe. 
The client side is based on the publish-subscribe pattern thus guaranteeing proper callback flow.
Uses a JSON-driven API.

## User guide
Upon downloading, compile and run src/server/Server.java from inside the src folder:
`javac -cp .:json-20140107.jar server/Server.java; java -cp .:json-20140107 server.Server`
This will run the server app on localhost:8080. Using VSCode, just run Server.

To run the client, change line 20 in src/app/App.java to the IP address of the server.
Anyone in the same network can now compile and run src/app/App.java (`javac -cp .:json-20140107.jar app/App.java; java -cp .:json-20140107 app.App`) which would open the login window.
After creating an account, you can create a new chat by pressing `Create Chat` and `Enter/Return`. 
Others can join by pressing `Create Chat` and entering the chat ID which can be seen on top of the opened chat. 

The application also supports shortcuts. For example, you can use numbers to swap between open chats.
