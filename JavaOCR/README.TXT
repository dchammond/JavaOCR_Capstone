Planning:
    Things I will need:
        Utility to read image data (might have to stick to jpg?) - ImageReader.java
            The image data will be turned into a Pixel[][]
        A way to store the information of the picture - Pixel.java
            A method to convert to grayscale
            A method to seperate lines and then characters in the image
            A method to normalize the extracted characters into uniform sized sets of Pixels (e.g. all 75 x 75)
        A utility that focuses on image comparison - ImageComparison.java
            Method that compares two sets of Pixels for total accuracy
            Customizable in tolerance to decide if two images are the same
        Profile generator to write information about a certain person's handwriting - ProfileGenerator.java
            Makes and knows images that are made up of the average of all training images
            Stores these in a directory, maybe zip them?