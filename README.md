
# Viper Apps

## **SENTINOX**
This repository will host the development of the chosen app

### What is the app about?
Our intention is to create an application in which, through the search of a medicine chosen by the user, a history of people’s opinions shared on the social media platform Bluesky is returned. To provide further information, the different adverse effects of the selected medication are also displayed for the user to see.

What motivated us to choose this topic?
We thought about how nowadays medication and pills are present everywhere in everyday life. Around 60–80% of people worldwide take some type of medication every year; this does not necessarily have to be for a specific illness, but also as reinforcement products such as vitamins.

This made an impact on us because we consume these substances and follow our doctors’ instructions, but we often do not know enough about what we are actually taking or how these drugs may affect different people and their health. We considered this a real-life issue and found it interesting to explore and develop a small solution for it through our application.


![Medicine Image](https://cdn.pixabay.com/photo/2023/10/01/14/40/medicine-8287535_1280.jpg)



### APIs and Tools used

The APIs we decided to use are PubChem and Bluesky. PubChem provides scientifically supported information about different drugs, including known adverse effects and medical data. Bluesky allows us to collect public comments posted by users about their personal experiences with different medicines, such as how they felt or what effects they noticed.

Finally, we used a sentiment analysis tool to process the qualitative information obtained from Bluesky posts. This allows us to classify users opinions as positive, negative, or neutral, making the social media data easier to analyze.


**DataMart Structure**

ViperApps datamart is an in-memory structure created to arrange and offer quick access to all the drug-related data gathered from Bluesky and PubChem.
The MedicineDataMart class, which internally contains a Map<String, MedicineStats>, is the main part of the datamart. The name of the medication is represented by the map's key, and all the statistics and information pertaining to that medication are stored in the accompanying value.

The MedicineStats class represents each medication and includes:
- a PubChem list of adverse reactions.
- Bluesky provided a compilation of user comments.
- There are three types of sentiment counters: neutral, negative, and positive.

The Comment class was developed to represent user comments, encompassing:
- the author comment's
- text comment
- categorization of emotions
- date of publication.

The BusinessUnit module updates the datamart on a regular basis. The BusinessUnitEventHandler processes both historical events loaded from the event store and real-time events ingested from ActiveMQ, registering the data into the datamart.


### Modules

- **Bluesky module**

The Bluesky module is responsible for collecting public opinions about different medicines from the Bluesky social media platform. It searches for posts related to a predefined list of medicines and extracts relevant information such as the post text, author, creation date, and the medicine being mentioned.

After retrieving the posts, the module applies sentiment analysis to classify each opinion as positive, negative, or neutral. Once the event is created, it is published to ActiveMQ through the BlueskyPosts topic, so that the rest of the system can consume it in real time.

_To execute this module, it is necessary to provide the Bluesky token, an user, a password, the ActiveMQ broker URL -> failover:(tcp://localhost:61616), the topic where the events will be published -> BlueskyPosts and the route of the file with the list of medicines -> MedicinesList.txt._


- **PubChem module**

The PubChem module is in charge of retrieving scientific information about medicines from the PubChem API. For each medicine in the list, it searches for the corresponding compound and extracts information related to adverse effects.

Each adverse effect is transformed into an event containing the medicine name, its PubChem CID, and the reaction or side effect found. These events are then sent to ActiveMQ through the PubChemReactions topic. This module provides the medical and scientific context needed to complement the social media information obtained from Bluesky.

_To execute this module, it needs the ActiveMQ broker URL -> failover:(tcp://localhost:61616), the topic where PubChem events will be published -> PubChemReactions and the file -> MedicinesList.txt._

- **EventStoreBuilder module**

The EventStoreBuilder module is responsible for consuming the events published in ActiveMQ and storing them locally. It subscribes to the topics used by the feeders, mainly BlueskyPosts and PubChemReactions, and receives the events as they are produced.

Each event is stored in a structured file system using JSON Lines format. The files are organized by topic, source system, and date, which makes it possible to recover historical information later. This module acts as the bridge between the real-time event flow and the historical event storage.

_To execute this module, the Brocker URL is needed -> failover:(tcp://localhost:61616) and the client ID -> EventStoreBuilder._

Additionally, the ActiveMQ connection URLs included a failover configuration. This guarantees continuous event consumption and publication by enabling the modules to automatically reconnect to the broker in the event of brief connection failures.

- **BusinessUnit module**

The BusinessUnit module is the part of the system that gives value to the final user. It consumes real-time events from ActiveMQ and updates a local datamart with aggregated information about each medicine.

For every medicine, the datamart stores sentiment information from Bluesky posts and adverse reactions from PubChem. This allows the system to compare public perception with known medical effects. At this stage, the datamart is implemented in memory, which keeps the design simple and makes real-time updates fast.

_To execute this module, it needs the broker URL -> failover:(tcp://localhost:61616) and the client ID -> BusinessUnit._


### System and application arquitecture

Because it integrates both real-time and historical data processing, our application uses a Lambda architecture. The Bluesky and PubChem modules create live events from ActiveMQ, which are consumed by the BusinessUnit module. However, it may also recreate the datamart using past events that are kept in the event store via.events files. The system's general architecture is depicted in the diagram below.

<img width="629" height="329" alt="Screenshot 2026-05-13 at 22 19 27" src="https://github.com/user-attachments/assets/c5457a4a-7acd-42b8-a9fc-8bfe47d63896" />


We shall now discuss the application architecture after outlining the fundamental system architecture. This graphic focuses on the project's internal structure, demonstrating how the various levels interact with one another and how the program was organized using the MVC approach.


<img width="382" height="575" alt="Screenshot 2026-05-13 at 22 39 02" src="https://github.com/user-attachments/assets/b23a3ab6-0a15-416f-aeed-b144ba325e26" />


Besides from this, we also created a use case diagram to display all functions the user can carry out with our application:


<img width="708" height="417" alt="PHOTO-2026-05-16-17-31-23" src="https://github.com/user-attachments/assets/a276b6be-a32f-4ac8-836a-c96634721202" />



### Principles and patterns

- *Principle of Single Responsibility (SRP)*

By making sure that every class in the project has a single, well-defined responsibility, we implemented the Single Responsibility Principle. Certain classes, for instance, are just in charge of retrieving data from APIs; others are in charge of publishing events to ActiveMQ; yet others are in charge of processing or storing the data.

The system is easier to debug and expand in subsequent iterations because to this separation, which also enhances readability and maintainability.

- *The Open-Closed Principle (OCP)*
  
The system was designed with the Open-Closed Principle in mind, allowing the addition of new features without needlessly changing the current code.

For example, rather than changing the current structure, new classes can be created to add new event processors, subjects, or data sources into the system. This increases the architecture's scalability and lowers the possibility of introducing mistakes into components that are already functional.

- *Demeter's Law*
  
To lessen coupling between classes and modules, the Law of Demeter was adhered to. Instead of relying on the internal workings of other components, each class simply interacts with the objects that are directly relevant to it.

Because modifications made within one module have less of an effect on the application as a whole, the architecture becomes cleaner and easier to maintain.

- *GitFlow*
  
To better manage the project versions and arrange the development process, we employed the GitFlow workflow.

Features were developed, modifications were tested, and stable versions were integrated into the main branch using separate branches. This made it possible for us to create various system components in a safer manner, monitor the project's progress, and prevent conflicts.


### Brief explanation of how the sprints were carried out

#### Sprint 1

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


#### Sprint 2

**FIRST WEEK**

In the initial week, the processes for extracting data from Bluesky and PubChem were automated. These processes were also reorganized into distinct modules. Furthermore, integration with ActiveMQ was established, enabling both modules to broadcast data as JSON events. This development laid the groundwork for an event-driven architecture and facilitated a continuous flow of data throughout the system.

**SECOND WEEK**

During the second week, the event publishing system was completely implemented and standardized across both the Bluesky and PubChem modules. Messages were consistently structured and transmitted to specific topics within ActiveMQ. Furthermore, sentiment analysis was incorporated into the Bluesky pipeline, enhancing the events with additional context. This process ensured a uniform and scalable method for generating events throughout the system.

**THIRD WEEK**

This week marked the completion of the event consumption layer with the implementation of the Event Store Builder. This component effectively subscribes to ActiveMQ topics, subsequently storing all incoming events within a structured file system. A comprehensive end-to-end test of the entire data pipeline was successfully conducted, validating the reliable communication channels between all modules. Furthermore, the week concluded with the integration of enhanced error handling mechanisms and targeted minor optimizations, solidifying the event-driven architecture.


#### Sprint 3

**FIRST WEEK**

In the first week of Sprint 3, we built the Business Unit module to consume real-time events from ActiveMQ and store them in an in-memory datamart. We structured it following MVC, ensuring clean separation of responsibilities and preparing the system for data analysis.

**SECOND WEEK**

During the second week of the third sprint, access to stored historical events was integrated into the event store and combined with real-time events consumed from ActiveMQ.
Furthermore, a JavaFX graphical interface was developed with the objective of assisting users when consulting information about medicines, visualising Bluesky comments, side effects obtained from PubChem, and accessing the joint analysis of both data sources. Finally, we validated the queries related to both real-time and historical data.
