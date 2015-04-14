import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageReader {
    private File pictureAtPath;
    private BufferedImage image;
    private int width;
    private int height;
    private Pixel[][] pixels;
    public Pixel[][] readImage(String path) throws IOException{
        try {
            this.pictureAtPath = new File(path);
            this.image = ImageIO.read(pictureAtPath);
        } catch (Exception e) {
            System.out.println("An error occured accessing a file: " + e);
            System.exit(0);
        }
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.pixels = new Pixel[height][width];
        for (int r = 0; r < this.height; ++r) {
            for (int c = 0; c < this.width; ++c) {
                this.pixels[r][c] = new Pixel(this.intToColor(this.image.getRGB(r, c)));
                // getRGB returns an integer of type TYPE_INT_ARGB
                // alpha channel in bits 24-31
                // red channel in bits 16-23
                // green channel in bits 8-15
                // blue channel in bits 0-7
            }
        }
        return this.pixels;
    }
    
    private Color intToColor(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        /* By using the unary &, we clear out any extra bits from the bit shift
        that are not part of our channel */
        return new Color((float)red, (float)green, (float)blue, (float)alpha);
    }
}