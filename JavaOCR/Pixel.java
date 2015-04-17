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
    
    public static int getSize(ArrayList<Pixel[]> image) {
        return getHeight(image) * getWidth(image);
    }
    
    public static int getHeight(ArrayList<Pixel[]> image) {
        return image.size();
    }
    
    public static int getWidth(ArrayList<Pixel[]> image) {
        return image.get(0).length;
    }
    
    public static void convertToBlackAndWhite(ArrayList<Pixel[]> image) {
        String threshold = "969696"; // Hex version of (150, 150, 150), our threshold for something that should be black
        for (Pixel[] pxArray : image) {
            for (Pixel px : pxArray) {
                String currentColor = Integer.toHexString(getRed(px)) + Integer.toHexString(getGreen(px)) + Integer.toHexString(getBlue(px));
                Color newColor = threshold.compareToIgnoreCase(currentColor) <= 0 ? Color.BLACK : Color.WHITE;
                setColor(px, newColor);
            }
        }
    }
    
    public static ArrayList<ArrayList<Pixel[]>> seperateByLines(ArrayList<Pixel[]> image) {
        // Returns an array of individual images that are lines
        // As it scans it throws away lines deemed to be white lines
        // After the first not white line is found, that plus every line until the next white line is a single image
        double minWhiteSpaceFraction = 0.2; // 20% of the pixels in a row have to be white for it to be a white line
        ArrayList<ArrayList<Pixel[]>> rowsOfText = new ArrayList<ArrayList<Pixel[]>>(0);
        ArrayList<Pixel[]> allPixels = image;
        int length = getWidth(image);
        boolean bufferMoreRows = false;
        ArrayList<Pixel[]> rowBuffer = new ArrayList<Pixel[]>(0);;
        for (Pixel[] pxArray : allPixels) {
            int blackPixelsFound = 0;
            for (Pixel px : pxArray) {
                if (getColor(px).equals(Color.BLACK)) {
                    ++blackPixelsFound;
                }
            }
            int whitePixelsFound = length - blackPixelsFound;
            if (whitePixelsFound / length < minWhiteSpaceFraction) {
                bufferMoreRows = true;
                rowBuffer.add(pxArray);
            } else {
                bufferMoreRows = false;
                if (rowBuffer.size() > 0) {
                    rowsOfText.add(rowBuffer);
                }
                rowBuffer.clear(); // rowBuffer.size() == 0
            }
        }
        return rowsOfText;
    }
    
    public static ArrayList<ArrayList<Pixel[]>> extractCharacters(ArrayList<ArrayList<Pixel[]>> lines) {
        // Accepts an array of line arrays
        // Returns an array of arrays of characters
    }
}