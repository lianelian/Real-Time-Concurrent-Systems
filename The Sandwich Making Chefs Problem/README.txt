=====================================================
Project Title - The Sandwich Making Chefs Problem
Description - This project tackles a process coordination problem called "The Sandwich Making Chefs Problem", This problem was implemented from the cigarette smokers problem by S. Patil. The problem is solved by evaluating facilities for the synchronizing of concurrent threads and processes.
Name: Lian Elian
=====================================================
Files in this project:
MakeSandwich.java: Creates the agent thread and the three chef threads
Table.java: A shared table between the agent and chef to keep track of the current sandwich being made

To run this program in Eclipse:
-> Open Eclipse
-> load the project '101201545_Assignment1' and navigate to src\MakeSandwich.java from the project folder
-> Navigate to the Run tab in the toolbox and click Run '101201545_Assignment1'

When this program runs:
-> The main function MakeSandwich() in the MakeSandwich.java is invoked
-> This creates one Agent thread of class type Agent(), which works as the producer.
 and 3 Chef threads of class type Chef(), which are the consumers. It then starts all threads.
-> The Agent thread picks two random ingredients and places them in the shared table. Then chooses the chef with the remaining ingredient.
-> The Chef thread with the remaining ingredient will run and take the two ingredients from the table
-> The Chosen chef will use the ingredients to make and eat the sandwich, then leave the table.
-> This goes on until 20 sandwiches are made.
