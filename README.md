# MD5-CC
This is a simple program I made to solve the program of duplicate files when I would do regulatory backups.


## How it works
The program will scan a specified directory for any files, when a file is found the hash of the file is stored in a map. Iterating through the map any duplicate files with the corresponding hash is then compressed into a .zip file.


## Step By Step
Lets say you have a folder with a lot of copies, but there might be hundreds or even thousands of files they could be hiding in.

![image](https://user-images.githubusercontent.com/53024171/210036819-30ef8855-a0e0-47c5-b11f-3e0fce0c3243.png)

The folder could even have a sub-folder with copies maybe of a different file aswell.

![image](https://user-images.githubusercontent.com/53024171/210036862-b558b137-c9e4-4406-953d-f157ce2e9da1.png)

Running the program will prompt the user for a directory path to search for copies, we'll input the path with all the copies.

![woah](https://user-images.githubusercontent.com/53024171/210036506-96707227-6a4d-4021-97c2-1deacabed460.png)

When the program is done scanning the directory and its sub-directories, it will again prompt the user for an output path for the .zip file and begin compressing.

![image](https://user-images.githubusercontent.com/53024171/210036706-d37b806c-971f-496f-bc65-cbb969d847ee.png)

Here's the results!

![image](https://user-images.githubusercontent.com/53024171/210036772-0885d9d3-6d57-4ea5-89cd-9e64143dbe46.png)

It even compressed the files in the sub-folder.

![image](https://user-images.githubusercontent.com/53024171/210036882-3a7ad55c-c66b-44b0-ad78-bf8c64ef7510.png)

Here's all the files compressed into a .zip file!

![Untitled-1](https://user-images.githubusercontent.com/53024171/210037408-93bbefb9-f266-4708-b9ec-2289d1afcc24.png)
