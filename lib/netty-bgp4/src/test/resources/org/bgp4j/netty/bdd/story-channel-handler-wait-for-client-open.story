Scenario: Connecting client
Given an unbound server
Then start client

Scenario: Send BGP open to connected client
Given client connect after 5
When waited for 1
Then send BGP open

Scenario: Sleep short and tell client to disconnect
Given client is connected
When waited for 1
Then stop client

Scenario: Wait for client to disconnect and signal done
Given client disconnect after 5
When waited for 1
Then signal done to client
