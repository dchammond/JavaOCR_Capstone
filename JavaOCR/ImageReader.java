import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ImageReader {
    private BufferedImage image;
    private int width;
    private int height;
    private ArrayList<ArrayList<Pixel>> pixels;
    public ArrayList<ArrayList<Pixel>> readImage(String path) throws IOException{
        try {
            this.image = ImageIO.read(new FileInputStream(new File(path)));
        } catch (Exception e) {
            System.out.println("An error occured accessing a file: " + e);
            System.exit(0);
        }
        this.width = this.image.getWidth();
        assert this.width > 0 : "Image width <= 0 (" + this.width + ")";
        this.height = this.image.getHeight();
        assert this.height > 0 : "Image height <= 0 (" + this.height + ")";
        this.pixels = new ArrayList<ArrayList<Pixel>>(0);
        for (int r = 0; r < this.height; ++r) {
            this.pixels.add(new ArrayList<Pixel>(0));
            for (int c = 0; c < this.width; ++c) {
                int argb = this.image.getRGB(c, r); // Takes x-coord, y-coord)
                Color color = intToColor(argb);
                Pixel px = new Pixel(color);
                assert px != null : "Created a null pixel";
                // getRGB returns an integer of type TYPE_INT_ARGB
                // alpha channel in bits 24-31 NOT USING ALPHA
                // red channel in bits 16-23
                // green channel in bits 8-15
                // blue channel in bits 0-7
                this.pixels.get(r).add(px);
            }
        }
        assert this.pixels.size() > 0 : "Size of pixels <= 0 (" + this.pixels.size() + ")";
        return this.pixels;
    }
    
    public void testPixel(ArrayList<ArrayList<Pixel>> image) {
        int width = image.get(0).size();
        int height = image.size();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = newImage.getRaster();//(WritableRaster) newImage.getData();
        assert raster.getHeight() == height : "raster height does not equal image height\nShould be " + height + " but is " + raster.getHeight();
        assert raster.getWidth() == width : "raster width does not equal image width\nShould be " + width + " but is " + raster.getWidth();
        for (int r = 0; r < height; ++r) {
            for (int c = 0; c < width; ++c) {
                Pixel px = image.get(r).get(c);
                int[] color = colorToIntArray(Pixel.getColor(px));
                raster.setPixel(c, r, color); // x-coord, y-coord
            }
        }
        try {
            ImageIO.write(newImage, "png", new File("/Users/Dillon/Desktop/test.png"));
        } catch (Exception e) {
            System.out.println("Image did not write: " + e);
            System.exit(0);
        }
    }
    
    private Color intToColor(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        /* By using the unary &, we clear out any extra bits from the bit shift
        that are not part of our channel */
        Color color = new Color(red, green, blue);
        return color;
    }
    
    private int[] colorToIntArray(Color color) {
        int[] array = new int[3];
        array[0] = color.getRed();
        array[1] = color.getGreen();
        array[2] = color.getBlue();
        return array;
    }
    
    public void test(String filePath) {
        String file = "/Users/Dillon/Desktop/" + filePath;
        try {
            ArrayList<ArrayList<Pixel>> myPixels = readImage(file);
            testPixel(myPixels);
        } catch (Exception e) {
            System.out.println("why: " + e);
        }
    }
}