import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ImageHandler - this class is not meant to be instantiated
 * This class contains several static methods to read, create, and test the equality of images
 * @author Dillon Hammond
 * @version 1.0
 */
public class ImageHandler {
    /**
     * readImage - A mehtod that takes in a file path and turns the picture at that location into
     * a doubly, nested ArrayList of Pixel objects
     * @param path The absolute (not relative) path to the image file
     * @throws IOException Exception is thrown if there is an issue reading the image file
     * @return ArrayList<ArrayList<Pixel>> The image converted into Pixel objects
     */
    public static ArrayList<ArrayList<Pixel>> readImage(String path) throws IOException{
        BufferedImage image = null;;
        int width = 0;
        int height = 0;
        ArrayList<ArrayList<Pixel>> pixels = new ArrayList<ArrayList<Pixel>>(0);
        try {
            image = ImageIO.read(new FileInputStream(new File(path)));
        } catch (Exception e) {
            System.out.println("An error occured accessing the file at " + path + " : " + e);
            System.exit(0);
        }
        width = image.getWidth();
        assert width > 0 : "Image width <= 0 (" + width + ")";
        height = image.getHeight();
        assert height > 0 : "Image height <= 0 (" + height + ")";
        pixels = new ArrayList<ArrayList<Pixel>>(0);
        for (int r = 0; r < height; ++r) {
            pixels.add(new ArrayList<Pixel>(0));
            for (int c = 0; c < width; ++c) {
                int argb = image.getRGB(c, r); // Takes x-coord, y-coord)
                Color color = intToColor(argb);
                Pixel px = new Pixel(color);
                assert px != null : "Created a null pixel from Color: " + color + " from argb " + argb;
                // getRGB returns an integer of type TYPE_INT_ARGB
                // alpha channel in bits 24-31 NOT USING ALPHA
                // red channel in bits 16-23
                // green channel in bits 8-15
                // blue channel in bits 0-7
                pixels.get(r).add(px);
            }
        }
        assert pixels.size() > 0 : "Size of pixels <= 0 (" + pixels.size() + ")";
        image.flush();
        return pixels;
    }
    
    /**
     * createNewImage - This method writes a new image of a specified format to the disk
     * @param image The double nested ArrayList of Pixel objects representing an image
     * @param newFilePath The path where the new file will be created
     */
    public static void createNewImage(ArrayList<ArrayList<Pixel>> image, String newFilePath) {
        int width = image.get(0).size();
        int height = image.size();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = newImage.getRaster();
        assert raster.getHeight() == height : "raster height does not equal image height\nShould be " + height + " but is " + raster.getHeight();
        assert raster.getWidth() == width : "raster width does not equal image width\nShould be " + width + " but is " + raster.getWidth();
        for (int r = 0; r < height; ++r) {
            for (int c = 0; c < width; ++c) {
                Pixel px = image.get(r).get(c);
                int[] color = colorToIntArray(Pixel.getColor(px));
                raster.setPixel(c, r, color); // x-coord, y-coord
            }
        }
        String fileFormat = newFilePath.substring(newFilePath.indexOf('.')+1);
        assert fileFormat.indexOf('.') < 0 : "Invalid format: " + fileFormat; // There should be no . in the format
        try {
            ImageIO.write(newImage, fileFormat, new File(newFilePath));
        } catch (Exception e) {
            System.out.println("An error occurred while writing the image: " + e);
            System.exit(0);
        }
    }

    /**
     * equals - A method to test the equality of two images based off percent pixel match and a tolerance for error
     * @param imageA The first image to compare
     * @param imageB The second image to compare
     * @param tolerance A percent represented as a decimalo indicating the maximum allowed difference for two images to equal each other
     * @pre-condition imageA and imageB have the same dimensions
     * @return boolean Whether or not the iamges were equal within tolerance
     */
    public static boolean equals(ArrayList<ArrayList<Pixel>> imageA, ArrayList<ArrayList<Pixel>> imageB, double tolerance) {
        int matchingPixels = 0;
        int totalPixels = imageA.size() * imageA.get(0).size();
        for (int r = 0; r < imageA.size(); ++r) {
            for (int c = 0; c < imageA.get(r).size(); ++c) {
                if (Pixel.equals(imageA.get(r).get(c), imageB.get(r).get(c))) {
                    ++matchingPixels;
                }
            }
        }
        double percentMatch = (double)matchingPixels / (double)totalPixels;
        if (1.0 - percentMatch <= tolerance) {
            return true;
        }
        return false;
    }
    
    /**
     * intToColor - A small private method to convert an int to a Color object
     * @param argb The integer containing an alpha, red, green, and blue channels
     * @return Color a new Color object made of the red, green, blue channels (alpha is ignored and is set to Color's defualt value)
     */
    private static Color intToColor(int argb) {
        // We ignore the alpha channel
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        /* By using the unary &, we clear out any extra bits from the bit shift
        that are not part of our channel */
        Color color = new Color(red, green, blue);
        return color;
    }
    
    /**
     * colorToIntArray - turn a Color into an array of 3 ints that represent red, green, blue channels
     * @param color The Color object to be converted into ints
     * @return int[] An int array of size 3 that contains red, gree, blue channels
     */
    private static int[] colorToIntArray(Color color) {
        int[] array = new int[3];
        array[0] = color.getRed();
        array[1] = color.getGreen();
        array[2] = color.getBlue();
        return array;
    }
}