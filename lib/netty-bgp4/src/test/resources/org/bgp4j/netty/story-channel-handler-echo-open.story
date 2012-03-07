Scenario: Connecting client
Given an unbound server
Then start client

Scenario: Wait for client BGP open and send OPEN back if received client OPEN
Given client connect after 5
When server waited for BGP OPEN after 5 seconds
Then send BGP open

Scenario: Sleep short and tell client to disconnect
Given client is connected
When waited for 1
Then stop client

Scenario: Wait for client to disconnect and signal done
Given client disconnect after 5
When waited for 1
Then signal done to client
