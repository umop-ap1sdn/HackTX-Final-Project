# HackTX-Final-Project
HackTX Submission Project

This project was made for the Annual HackTX Hackathon hosted by UT Austin

# Project Description
This project is designed to demonstrate the ability of Recurrent Neural Networks to perform time-series forecasting, in this case related to Stock Prices

To use this application start by downloading the "HackTX.jar" file and ensuring the Java Version is 16 or higher. If this condition is met, the jar file can be run straight from the desktop.
From here start by creating a reference file to use as training data. Click on "Build Dataset" and enter a stock ticker. By default this is set to "AAPL" but can be changed to whatever result is desired.
Make sure an internet connection is active so the file can be created.
Then go to "Train" to start training the network
Select the created Dataset from the first dropdown menu. If the program has been run before a preset may be selected from the second drop down menu.
Finally Declare an iteration count and learning rate to start training.
The results of this network can then be viewed in the "Test" branch.
Load the desired dataset just as was done for the Train stage, as well as trained neural network. Finally, declare an output length to see the future predictions. Do note that the first 20% of the graph consists of already existing values from the dataset, this exists to show how well/poorly the prediction lines up with existing data.
