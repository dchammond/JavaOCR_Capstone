# JavaOCR_Capstone
An Optical Character Recognition System written in Java for my AP Computer Science Capstone

## Process:
 1. Read in an image
 2. Convert to gray scale (things are white or black)
 3. Separate the image by "lines" of text
 4. Identify single blocks of black pixels (characters)
 5. Match the image of a character to the actual text character and write to a .txt file