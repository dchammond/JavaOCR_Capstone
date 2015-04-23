import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageReader {
    private File pictureAtPath;
    private BufferedImage image;
    private int width;
    private int height;
    private ArrayList<ArrayList<Pixel>> pixels;
    public ArrayList<ArrayList<Pixel>> readImage(String path) throws IOException{
        int x = 8;
        try {
            this.pictureAtPath = new File(path);
            this.image = ImageIO.read(pictureAtPath);
        } catch (Exception e) {
            System.out.println("An error occured accessing a file: " + e);
            System.exit(0);
        }
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.pixels = new ArrayList<ArrayList<Pixel>>(this.height);
        int r = 0;
        for (ArrayList<Pixel> pxArray : this.pixels) {
            int c = 0;
            for (Pixel px : pxArray) {
                px = new Pixel(intToColor(this.image.getRGB(r, c)));
                // getRGB returns an integer of type TYPE_INT_ARGB
                // alpha channel in bits 24-31 NOT USING ALPHA
                // red channel in bits 16-23
                // green channel in bits 8-15
                // blue channel in bits 0-7
                ++c;
            }
            ++r;
        }
        return this.pixels;
    }
    
    public void test(ArrayList<ArrayList<Pixel>> image) {
        int width = image.get(0).size();
        int height = image.size();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = newImage.getRaster();//(WritableRaster) newImage.getData();
        
        for (int r = 0; r < height; ++r) {
            for (int c = 0; c < width; ++c) {
                Pixel px = image.get(r).get(c);
                int[] color = colorToIntArray(Pixel.getColor(px));
                raster.setPixel(r, c, color);
            }
        }
        try {
            ImageIO.write(newImage, "jpg", new File("~/Desktop/test.jpg"));
        } catch (Exception e) {
            System.out.println("NOO: " + e);
            System.exit(0);
        }
    }
    
    private Color intToColor(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        /* By using the unary &, we clear out any extra bits from the bit shift
        that are not part of our channel */
        return new Color((float)red, (float)green, (float)blue);
    }
    
    private int[] colorToIntArray(Color color) {
        int[] array = new int[3];
        array[0] = color.getRed();
        array[1] = color.getGreen();
        array[2] = color.getBlue();
        return array;
    }
}