import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class Pixel {
    private Color pixelColor;
    
    public Pixel(Color color) {
        this.pixelColor = color;
    }
    
    public static Color getColor(Pixel px) {
        return px.pixelColor;
    }
    
    public static void setColor(Pixel px, Color newColor) {
        px.pixelColor = newColor;
    }
    
    public static int getRed(Pixel px) {
        return getColor(px).getRed();
    }
    
    public static int getGreen(Pixel px) {
        return getColor(px).getGreen();
    }
    
    public static int getBlue(Pixel px) {
        return getColor(px).getBlue();
    }
    
    public static int getSize(ArrayList<ArrayList<Pixel>> image) {
        return getHeight(image) * getWidth(image);
    }
    
    public static int getHeight(ArrayList<ArrayList<Pixel>> image) {
        return image.size();
    }
    
    public static int getWidth(ArrayList<ArrayList<Pixel>> image) {
        return image.get(0).size();
    }
    
    public static void convertToBlackAndWhite(ArrayList<ArrayList<Pixel>> image) {
        String threshold = "969696"; // Hex version of (150, 150, 150), our threshold for something that should be black
        for (ArrayList<Pixel> pxArray : image) {
            for (Pixel px : pxArray) {
                String currentColor = Integer.toHexString(getRed(px)) + Integer.toHexString(getGreen(px)) + Integer.toHexString(getBlue(px));
                Color newColor = threshold.compareToIgnoreCase(currentColor) <= 0 ? Color.BLACK : Color.WHITE;
                setColor(px, newColor);
            }
        }
    }
    
    public static ArrayList<ArrayList<ArrayList<Pixel>>> seperateByLines(ArrayList<ArrayList<Pixel>> image) {
        // Returns an array of individual images that are lines
        // As it scans it throws away lines deemed to be white lines
        // After the first not white line is found, that plus every line until the next white line is a single image
        double minWhiteSpaceFraction = 0.2; // 20% of the pixels in a row have to be white for it to be a white line
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
                    rowsOfText.add(rowBuffer);
                }
                rowBuffer.clear(); // rowBuffer.size() == 0
                assert(rowBuffer.size() == 0);
            }
        }
        return rowsOfText;
    }
    
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
        double minWhiteSpaceFraction = 0.9; // 90% of pixels in a column must be white to be considered a space
        int height = charImages.get(0).size();
        boolean bufferMoreColumns = false;
        for (ArrayList<ArrayList<Pixel>> image : lines) {
            for (int c = 0; c < height; ++c) {
                int blackPixelsFound = 0;
                for (ArrayList<Pixel> pxArray : image) {
                    Pixel px = pxArray.get(c);
                    columnBuffer.add(px);
                    if (getColor(px).equals(Color.BLACK)) { ++blackPixelsFound;}
                }
                int whitePixelsFound = height - blackPixelsFound;
                if ( (double)( (double)whitePixelsFound / (double)height ) < minWhiteSpaceFraction) {
                    bufferMoreColumns = true;
                    charBuffer.add(columnBuffer);
                } else {
                    bufferMoreColumns = false;
                    if (charBuffer.size() > 0) {
                        charImages.add(charBuffer);
                    }
                    columnBuffer.clear(); // Size is now 0
                    charBuffer.clear(); // Size is now 0
                    assert(columnBuffer.size() == 0);
                    assert(charBuffer.size() == 0);
                }
            }
        }
        return charImages;
    }
    
    public static ArrayList<ArrayList<ArrayList<Pixel>>> normalizeCharacters(ArrayList<ArrayList<ArrayList<Pixel>>> characters) {
        // Remember that a character image is stored with its columns represented as rows
          // For example: row 0 is actually column 0, row 1 is actually column 1
        for (ArrayList<ArrayList<Pixel>> images : characters) {
            images = resizeImage(images, 75, 75); // Resize all images to 75 by 75
        }
        return characters;
    }
    
    private static ArrayList<ArrayList<Pixel>> resizeImage(ArrayList<ArrayList<Pixel>> image, int newWidth, int newHeight) {
        int oldWidth = image.get(0).size();
        int oldHeight = image.size();
        ArrayList<Pixel> columns = new ArrayList<Pixel>(newWidth);
        ArrayList<ArrayList<Pixel>> resizedImage = new ArrayList<ArrayList<Pixel>>(newHeight);
        for (ArrayList<Pixel> col : resizedImage) {
            col = columns;
        }
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