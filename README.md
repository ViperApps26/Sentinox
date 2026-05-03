
# SENTINOX

## **ViperApps**
This repository will host the development of the chosen app

What is the app about?
Our intention is to create an app in which, throughout the search of a chosen medicine by the user, it returns a history of people's opinions that have been shared on the social media platform BlueSky. To provide further information, the diverse adverse effects of the chosen medication are also displayed for the user to see. 

What motivated us to choose this topic?
We thought about how nowadays, medication and pills are everywhere. Around a 60-80% of the people worldwide take some sort of medication each yea;, it does not necessarily have to be for a specific illness, but also as a reinforcment, such as vitamins. This has made an impact on us, because we take these substances and we follow our doctor's instructions, but how well informed are we about what we intake? and how do the drugs we are prescribed affect another's person's body and health. 
We found this a real-life issue, and we found it interesting to explore and develop a small solution for it with our application

![Medicine Image](https://cdn.pixabay.com/photo/2023/10/01/14/40/medicine-8287535_1280.jpg)


### Sprint 1

**FIRST WEEK**
Chosen APIs:
- PubChem
- Bluesky
- Sentiment Analysis

We have primarily focused on identifying challenging yet practical ideas to design an application that provides real value to users. The proposed solution is a medication guide that combines reliable information with user generated opinions, allowing individuals to compare experiences and discover new insights about the wide range of medications available today.

Once this was decided, we sketched an initial version of the class diagram, which includes the majority of the classes to be developed in IntelliJ.
In addition, the initial project structure, including the first packages and files, was also established.

Our preliminary vision of the class diagram is the following:

![IMG_1049](https://github.com/user-attachments/assets/fb2d0fe4-600c-44b4-a298-e0220d3015f7)



**SECOND AND THIRD WEEK**

During the second week we've focused on building our SQL databases which we intend to use subsequently.
Another thing we spent some time working on, was creating a diverse group of tests to validate the results obtained and analyse further the functionality of the APIs selected.

We also focused on developing the different tests to validate the code we built, following the TDD approach.


### Sprint 2

**FIRST WEEK**

In the initial week, the processes for extracting data from Bluesky and PubChem were automated. These processes were also reorganized into distinct modules. Furthermore, integration with ActiveMQ was established, enabling both modules to broadcast data as JSON events. This development laid the groundwork for an event-driven architecture and facilitated a continuous flow of data throughout the system.

**SECOND WEEK**

During the second week, the event publishing system was completely implemented and standardized across both the Bluesky and PubChem modules. Messages were consistently structured and transmitted to specific topics within ActiveMQ. Furthermore, sentiment analysis was incorporated into the Bluesky pipeline, enhancing the events with additional context. This process ensured a uniform and scalable method for generating events throughout the system.

**THIRD WEEK**

This week marked the completion of the event consumption layer with the implementation of the Event Store Builder. This component effectively subscribes to ActiveMQ topics, subsequently storing all incoming events within a structured file system. A comprehensive end-to-end test of the entire data pipeline was successfully conducted, validating the reliable communication channels between all modules. Furthermore, the week concluded with the integration of enhanced error handling mechanisms and targeted minor optimizations, solidifying the event-driven architecture.


### Sprint 3

**FIRST WEEK**

In the first week of Sprint 3, we built the Business Unit module to consume real-time events from ActiveMQ and store them in an in-memory datamart. We structured it following MVC, ensuring clean separation of responsibilities and preparing the system for data analysis.
