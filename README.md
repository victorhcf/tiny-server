# Tiny Shortener
Tiny Shortener URL is an application to generate short URLs. 

# Task Description
Create a simple URL shortener service (UI is optional but will be considered a plus). Plan the implementation
to be suitable for a very high volume of traffic and large amounts of data.

The service should be able to:
  * Shorten a given URL and return shortened link;
  * Expand a shortened URL into the original one and redirect to the original location.

Shortened URLs should expire after a configured retention period.

Implementation expectations
* Satisfactory implementation should consider aspects of the high volume and/or high load
* Security precautions should be taken for all shortened URLs (it is up to you to make a decision on what those security precautions are)
* Think about how you can optimize the storage
* Consider how to scale your solution horizontally (it's not necessary to implement it, but prepare to explain this when you present your solution)
* [Optional] Think about monetization aspects of the service

## Solution
This application is a REST API that handles the server side of the application. Responsible for the generation of short URLs and for redirecting users to the right place when access to the short URLs.
The application is designed for high scalability and high availability.
Implementing an algorithm to generate the codes for each URL without collision in a scalable way can be tricky.
In order to do that we cannot simply encode the long URL in the same format because this way if different users upload the same URL it would have the same output code which is not acceptable. Each URL must receive a unique shortcode.
So to do that we used implementing a distributed counter that is used to generate the URL.
So each time the application is requested to generate a URL it`ll get the next number from the counter and encode this number in base62.
This log will give us 8 characters short code and it will be possible to handle 3.5 trillion unique identifiers.
In other to implement this counter in a distributed system without the risk of getting duplicated numbers we are making use of REDIS. REDIS has support for what they call atomic increment. REDIS makes use of Lua on the server side to make sure the clients will always get a different number even if they are very frequent.


## Libraries 
* Java Spring Boot
* H2 Database Engine
* Lombok
* Redis

## Installation

Need to deploy a war package into Tomcat or any other application server.
Needs also runs the tiny-client to be able to see the interface.

## TODO List
* Implement automated tests
* Implement authentication
* Develop a pricing model
* Make a second instance of Redis, 1 for the counter and another for caching
* Deploy multi instances f the backend with a load balancer in the front.
* Change database to MongoDB

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
