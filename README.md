
This work was handed in as a semester project for the **Course 052500 VU – Distributed Systems Engineering** at the University of Vienna. All the code in this repository was written by me.

# Assignment 
October 16, 2017
## 2017W – 052500 VU – Distributed Systems Engineering
Semester Project
A To-Do List Application

The goal of this project is to create a distributed and Web-based  To-Do  List  application
based on the concepts and technologies presented in the lecture. The project will follow the
[Microservice architectural style](https://martinfowler.com/articles/microservices.html). Accordingly, the system shall consist of **four** independent
Microservices (MS)

___

My task was to implement the Microservice that provided the Authentication and Authorization services (MS1) for Microservices implemented by other students of my team.

## Problem Description

* MS1 - User Authentication and Authorization Service.

>> This service implements user registration and user authentication (login) via a username and a password.  In addition, this
service shall also make all authorization decisions (access control) within your system.
More precisely, it decides which user shall be granted access to which To-Do list.  Users
shall only be granted access to their own To-Do Lists

___

As the assignment does not specify what technologies should be used, I decided to solve the problem using Json Web Tokens ([JWT](https://jwt.io/)) while using [Dropwizard](www.dropwizard.io) as an application framework for RESTful web services.

___ 
A "status update" report can be found in **supd.pdf** discussing:

>> * The  core  design  decisions  of  your  Microservice:  How  does  the  service’s  external interface look like? How can your colleagues use your service? Which protocol(s) and technologies are used?
>>
>> * Your thoughts on the service’s deployment: How are you deploying your Microservice? How can it be reached via the network (i.e. IP/hostname and port(s))?
>>
>> * Ideas for testing and presentation: How are you going to test your service when other services you have to depend upon are not available?  How can you demonstrate the functionality of your service in such situations?
>>
>> * The current state of your implementation: What has been achieved so far?  What’s missing?  What works?  What doesn’t?

The final project report handed in by me can be found in **dead.pdf** it contains:

>> * A discussion of how you eventually had to deviate from your original plan laid out in ./ms1/supd.pdf
>>
>> * A description of your service’s interface.
>>
>> * A “quick start tutorial” that describes how one can use your service.