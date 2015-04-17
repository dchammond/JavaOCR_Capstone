import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageReader {
    private File pictureAtPath;
    private BufferedImage image;
    private int width;
    private int height;
    private ArrayList<Pixel[]> pixels;
    public ArrayList<Pixel[]> readImage(String path) throws IOException{
        try {
            this.pictureAtPath = new File(path);
            this.image = ImageIO.read(pictureAtPath);
        } catch (Exception e) {
            System.out.println("An error occured accessing a file: " + e);
            System.exit(0);
        }
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.pixels = new ArrayList<Pixel[]>(this.height);
        for (int r = 0; r < this.height; ++r) {
            for (int c = 0; c < this.width; ++c) {
                this.pixels.get(r)[c] = (new Pixel(this.intToColor(this.image.getRGB(r, c))));
                // getRGB returns an integer of type TYPE_INT_ARGB
                // alpha channel in bits 24-31 NOT USING ALPHA
                // red channel in bits 16-23
                // green channel in bits 8-15
                // blue channel in bits 0-7
            }
        }
        return this.pixels;
    }
    
    private Color intToColor(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        /* By using the unary &, we clear out any extra bits from the bit shift
        that are not part of our channel */
        return new Color((float)red, (float)green, (float)blue);
    }
}