# JavaOCR_Capstone
An Optical Character Recognition System written in Java for my AP Computer Science Capstone

## Process:
 1. Read in an image
 2. Convert to black and white
 3. Separate the image by "lines" of text
 4. Identify single blocks of black pixels (characters)
 5. Match the image of a character to the actual text character and write to a .txt file

### Synthezise:
 1. Has-a relationships between classes
   1. Although my project did not have any inheritance, I showed the OOP idea of a has-a relationship because both ImageHandler and Profile have Pixels
 2. Loops
   1. A lot of my code involves loops that iterate over ArrayLists and nested ArrayLists (up to 3 levels deep!) to do things like manipulate pixels or rows and columns form an image
 3. File I/O
   1. I had to use quite a bit of File I/O in my Profile and ImageHandler as they had to read and write images and txt files
   2. As a result of having File I/O, I also had to implement Exceptions and exception handlers
 4. Testing
   1. Although I do not have any pure test classes, I found the liberal use of assert statements, accompanied by messages explaining what went wrong were very helpful for debugging.

### Instructions:
Simply run the main method of Profile, inputting the params as specified (though location of typed txt can be empty string)