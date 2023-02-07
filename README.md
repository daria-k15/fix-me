# Simulated FIX Crypto Market

- Building Project
> mvn clean install

## Router
The router perform no buiness
logic, it just dispatch messages to the destination component(s). The router
accept incomming connections from multiple brokers and markets. It's a
market connetivity provider, because it allows brokers to send messages (in FIX format)
to markets, without depending on specific implementation of the market.
Router listens on 2 port: 
- Port 5000 for messages from Broker components. When a Broker establishes the
  connection the Router asigns it a unique 6 digit ID and communicates the ID to
  the Broker
- Port 5001 for messages from Market components. When a Market establishes the
  connection the Router asigns it a unique 6 digit ID and communicates the ID to
  the Market.
> java -jar router/target/router-1.0-SNAPSHOT.jar
## Broker
The Broker sends two types of messages:
- Buy - An order where the broker wants to buy an instrument
- Sell - An order where the broker want to sell an instrument

and receives from the market messages of the following types:
- Executed - when the order was accepted by the market and the action succeeded
- Rejected - when the order could not be met
> java -jar broker/target/broker-1.0-SNAPSHOT.jar
## Market
A market has a list of instruments that can be traded. When orders are received from
brokers the market tries to execute it. If the execution is successfull, it updates the
internal instrument list and sends the broker an Executed message. If the order can’t be
met, the market sends a Rejected message.
>java -jar market/target/market-1.0-SNAPSHOT.jar