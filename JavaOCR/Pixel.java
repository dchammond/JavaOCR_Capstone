import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Pixel - this class represents a Pixel object and contains sattic methods to manipulate Pixels
 * @author Dillon Hammond
 * @version 1.0
 */
public class Pixel {
    /**
     * pixelColor - The private instance variable holding the color of a pixel
     */
    private Color pixelColor;
    
    /**
     * Class constructor defines the Color of a new Pixel object
     * @param color the color to set the Pixel to
     */
    public Pixel(Color color) {
        this.pixelColor = color;
    }
    
    /**
     * getColor - A method to get the Color of a pixel
     * @param px The Pixel to get the color of
     * @return Color the Color object associated with px
     */
    public static Color getColor(Pixel px) {
        return px.pixelColor;
    }
    
    /**
     * setColor - Set a new Color object for a Pixel
     * @param px the Pixel to change
     * @param newColor the new Color that px will have
     */
    public static void setColor(Pixel px, Color newColor) {
        px.pixelColor = newColor;
    }
    
    /**
     * getRed - gets the Red value form a Pixel's color
     * @param px The Pixel to inspect
     * @return int the value of px's red channel
     */
    public static int getRed(Pixel px) {
        return getColor(px).getRed();
    }
    
    /**
     * getGreen - gets the Green value form a Pixel's color
     * @param px The Pixel to inspect
     * @return int the value of px's green channel
     */
    public static int getGreen(Pixel px) {
        return getColor(px).getGreen();
    }
    
    /**
     * getBlue - gets the Blue value form a Pixel's color
     * @param px The Pixel to inspect
     * @return int the value of px's blue channel
     */
    public static int getBlue(Pixel px) {
        return getColor(px).getBlue();
    }
    
    /**
     * getSize - A method to return the total size (length*width) of an image
     * @param ArrayList<ArrayList<Pixel>> The image to get the size of
     * @return int The total size of image
     */
    public static int getSize(ArrayList<ArrayList<Pixel>> image) {
        return getHeight(image) * getWidth(image);
    }
    
    /**
     * getHeight - A method to return the height of an image
     * @param ArrayList<ArrayList<Pixel>> The image to get the height of
     * @return int The height of image
     */
    public static int getHeight(ArrayList<ArrayList<Pixel>> image) {
        return image.size();
    }
    
    /**
     * getWidth - A method to return the width of an image
     * @param ArrayList<ArrayList<Pixel>> The image to get the width of
     * @return int The width of image
     */
    public static int getWidth(ArrayList<ArrayList<Pixel>> image) {
        return image.get(0).size();
    }
    
    /**
     * toString - A method to define how a Pixel object is printed as String output
     * @return String The String indicating the Color of a Pixel
     */
    public String toString() {
        return "Color: " + getColor(this);
    }
    
    /**
     * equals = A method to determine if two Pixels are equal
     * @param pxA - The first Pixel
     * @param pxB - The second Pixel
     * @return boolean True if the Color of pxA matches the Color of pxB, false otherwise
     */
    public static boolean equals(Pixel pxA, Pixel pxB) {
        return getColor(pxA).equals(getColor(pxB));
    }
    
    /**
     * convertToBlackAndWhite - A method to convert an image to Black and White
     * @param image the image to be converted to black and white
     */
    public static void convertToBlackAndWhite(ArrayList<ArrayList<Pixel>> image) {
        String threshold = "969696"; // Hex version of (150, 150, 150), our threshold for something that should be black
        for (ArrayList<Pixel> pxArray : image) {
            for (Pixel px : pxArray) {
                String currentColor = Integer.toHexString(getRed(px)) + Integer.toHexString(getGreen(px)) + Integer.toHexString(getBlue(px));
                Color newColor = threshold.compareToIgnoreCase(currentColor) > 0 ? Color.BLACK : Color.WHITE;
                setColor(px, newColor);
            }
        }
    }
    
    /**
     * seperateByLines - A method to seperate an image into several, smaller images that are a line of text form image
     * @param image - The original, entire image to extract lines of text from
     * @return ArrayList<ArrayList<ArrayList<Pixel>>> An ArrayList of images, each of which is a line of black text
     */
    public static ArrayList<ArrayList<ArrayList<Pixel>>> seperateByLines(ArrayList<ArrayList<Pixel>> image) {
        // Returns an array of individual images that are lines
        // As it scans it throws away lines deemed to be white lines
        // After the first not white line is found, that plus every line until the next white line is a single image
        double minWhiteSpaceFraction = 0.99; // 99% of the pixels in a row have to be white for it to be a white line
        ArrayList<ArrayList<ArrayList<Pixel>>> rowsOfText = new ArrayList<ArrayList<ArrayList<Pixel>>>(0);
        ArrayList<ArrayList<Pixel>> allPixels = image;
        int length = getWidth(image);
        boolean bufferMoreRows = false;
        ArrayList<ArrayList<Pixel>> rowBuffer = new ArrayList<ArrayList<Pixel>>(0);
        for (ArrayList<Pixel> pxArray : allPixels) {
            int blackPixelsFound = 0;
            for (Pixel px : pxArray) {
                if (getColor(px).equals(Color.BLACK)) {
                    ++blackPixelsFound;
                }
            }
            int whitePixelsFound = length - blackPixelsFound;
            if (((double)(double)whitePixelsFound / (double)length) < minWhiteSpaceFraction) {
                bufferMoreRows = true;
                rowBuffer.add(pxArray);
            } else {
                bufferMoreRows = false;
                if (rowBuffer.size() > 0) {
                    ArrayList<ArrayList<Pixel>> tempRowBuffer = new ArrayList<ArrayList<Pixel>>(rowBuffer.size());
                    // the temp is needed to avoid STUPID java object pass by stupid pointer
                        // WHAT IF I WANT PURE PASS_BY_VALUE FOR OBJECTS HUH?
                        // THEN I COULD AVOID THE STUFF RIGHT BELOW ME!
                    for (int i = 0; i < rowBuffer.size(); ++i) {
                        tempRowBuffer.add(new ArrayList<Pixel>(rowBuffer.get(i).size()));
                        for (int j = 0; j < rowBuffer.get(i).size(); ++j) {
                            tempRowBuffer.get(i).add(new Pixel(Pixel.getColor(rowBuffer.get(i).get(j))));
                        }
                    }
                    rowsOfText.add(tempRowBuffer);
                }
                rowBuffer.clear();
                assert rowBuffer.size() == 0 : "rowBuffer size not reset to 0 (" + rowBuffer.size() + ")";
            }
        }
        assert rowsOfText.size() > 0 : "The number of rows made is <= 0 (" + rowsOfText.size() + ")";
        return rowsOfText;
    }
    
    /**
     * extractCharacters - A method to extract individual characters from lines of text
     * @param lines An ArrayList of images that are lines of text
     * @return ArrayList<ArrayList<ArrayList<Pixel>>> An ArrayList of images that are single characters
     */
    public static ArrayList<ArrayList<ArrayList<Pixel>>> extractCharacters(ArrayList<ArrayList<ArrayList<Pixel>>> lines) {
        // Accepts an array of line arrays
        // Returns an array of arrays of characters
        // Similar to how we seperated multiple rows of white pixels as white space
        // Check for multiple columns in a row of white to find spaces between characters
        
        // 1. Get the index 0 ArrayList<ArrayList<Pixel>> from lines (image)
        // 2. Get the index 0 ArrayList<Pixel> from image
        // 3. Store that index 0 Pixel
        // 4. Get the index 1 ArrayList<Pixel> from image
        // 5. Store that index 0 Pixel
        // 6. Repeat until we have gone over every ArrayList<Pixel> from image, that is a column
        // 7. Do white pixel calculation
        // 8. If the column is "black text", bufferMoreColumns = true, add the built up column to the characterBuffer
        // 9. Else, bufferMoreColumns = false, if charBuffer.size() > 0, add it to charImages
        // 10. Get the second ArrayList<ArrayList<Pixel>> form lines (image)
        // 11. Repeat until we hit the end of lines
        // 12. return;
        ArrayList<ArrayList<ArrayList<Pixel>>> charImages = new ArrayList<ArrayList<ArrayList<Pixel>>>(0); // Holds all the collected characters
        ArrayList<ArrayList<Pixel>> charBuffer = new ArrayList<ArrayList<Pixel>>(0); // A series of columns determined to be a character
        ArrayList<Pixel> columnBuffer = new ArrayList<Pixel>(0); // For building a column, that we then determine if it is a space or character
        double minWhiteSpaceFraction = 1.0; // 99.99% of pixels in a column must be white to be considered a space
        boolean bufferMoreColumns = false;
        for (ArrayList<ArrayList<Pixel>> image : lines) {
            int width = getWidth(image);
            int height = getHeight(image);
            System.out.println("width="+width+" height="+height);
            for (int c = 0; c < width; ++c) {
                int blackPixelsFound = 0;
                for (int row = 0; row < height; ++row) {
                    Pixel px = new Pixel(getColor(image.get(row).get(c)));
                    columnBuffer.add(px);
                    if (getColor(px).equals(Color.BLACK)) { ++blackPixelsFound;}
                }
                /*
                for (ArrayList<Pixel> pxArray : image) {
                    Pixel px = new Pixel(getColor(pxArray.get(c)));
                    columnBuffer.add(px);
                    if (getColor(px).equals(Color.BLACK)) { ++blackPixelsFound;}
                }*/
                int whitePixelsFound = height - blackPixelsFound;
                System.out.println("column: " + c);
                System.out.println("white=" + whitePixelsFound + " black=" + blackPixelsFound + " %white=" + (double)( (double)whitePixelsFound / (double)height ));
                if ( (double)( (double)whitePixelsFound / (double)height ) < minWhiteSpaceFraction) {
                    bufferMoreColumns = true;
                    ArrayList<Pixel> tempColumnBuffer = new ArrayList<Pixel>(columnBuffer.size());
                    assert columnBuffer.size() > 0 : "columnBuffer size <= 0 (" + columnBuffer.size() + ")";
                    for (int i = 0; i < columnBuffer.size(); ++i) {
                        tempColumnBuffer.add(new Pixel(getColor(columnBuffer.get(i))));
                    }
                    charBuffer.add(tempColumnBuffer);
                } else {
                    bufferMoreColumns = false;
                    if (charBuffer.size() > 0) {
                        ArrayList<ArrayList<Pixel>> tempCharBuffer = new ArrayList<ArrayList<Pixel>>(charBuffer.size());
                        // the temp is needed to avoid STUPID java object pass by stupid pointer
                            // WHAT IF I WANT PURE PASS_BY_VALUE FOR OBJECTS HUH?
                            // THEN I COULD AVOID THE STUFF RIGHT BELOW ME!
                        for (int i = 0; i < charBuffer.size(); ++i) {
                            tempCharBuffer.add(new ArrayList<Pixel>(charBuffer.get(i).size()));
                            for (int j = 0; j < charBuffer.get(i).size(); ++j) {
                                tempCharBuffer.get(i).add(new Pixel(Pixel.getColor(charBuffer.get(i).get(j))));
                            }
                        }
                        System.out.println("tempcahr size: " + tempCharBuffer.size());
                        charImages.add(tempCharBuffer);
                    }
                    columnBuffer.clear(); // Size is now 0
                    charBuffer.clear(); // Size is now 0
                    assert columnBuffer.size() == 0 : "columnBuffer size is > 0 (" + columnBuffer.size() + ")";
                    assert charBuffer.size() == 0 : "charBuffer size is > 0 (" + charBuffer.size() + ")";
                }
            }
        }
        return charImages;
    }
    
    /**
     * normalizeCharacters - A method to convert ever character to a new image of specific dimensions
     * @param characters An ArrayList of images that are characters
     * @return ArrayList<ArrayList<ArrayList<Pixel>>> An ArrayList of images that are now uniform sized characters
     */
    public static ArrayList<ArrayList<ArrayList<Pixel>>> normalizeCharacters(ArrayList<ArrayList<ArrayList<Pixel>>> characters) {
        // Remember that a character image is stored with its columns represented as rows
          // For example: row 0 is actually column 0, row 1 is actually column 1
        for (ArrayList<ArrayList<Pixel>> images : characters) {
            images = resizeImage(images, 75, 75); // Resize all images to 75 by 75
        }
        return characters;
    }
    
    /**
     * resizeImage - A method that changes the size of images with nearest-neighbor
     * @param image - A single image to convert to a specific size
     * @param newWidth The new width of the image
     * @param newHeight The new height of the image
     * @return ArrayList<ArrayList<Pixel>> A single image that has dimensions of newWidth and newHeight
     */
    public static ArrayList<ArrayList<Pixel>> resizeImage(ArrayList<ArrayList<Pixel>> image, int newWidth, int newHeight) {
        int oldWidth = image.get(0).size();
        int oldHeight = image.size();
        ArrayList<Pixel> columns = new ArrayList<Pixel>(newWidth);
        for (int k = 0; k < newWidth; ++k) {
            columns.add(null);
        }
        assert columns.size() == newWidth : "columns.size() != newWidth (" + columns.size() + ")";
        ArrayList<ArrayList<Pixel>> resizedImage = new ArrayList<ArrayList<Pixel>>(newHeight);
        for (int k = 0; k < newHeight; ++k) {
            resizedImage.add(columns);
        }
        assert resizedImage.size() == newHeight : "resizedImage size != newHeight (" + resizedImage.size() + ")";
        double x_ratio = oldWidth/(double)newWidth;
        double y_ratio = oldHeight/(double)newHeight;
        double px, py;
        int rowNum = 0;
        for (ArrayList<Pixel> row : image) {
            for (int r = 0; r < newHeight; ++r) {
                for (int c = 0; c < newWidth; ++c) {
                    py = Math.min(Math.floor(r / y_ratio), oldHeight);
                    px = Math.min(Math.floor(c / x_ratio), oldWidth);
                    resizedImage.get(rowNum).set((int)(newWidth * r)+c, row.get((int)px + (int)py));
                }
              }
        }
        return resizedImage;
    }
}