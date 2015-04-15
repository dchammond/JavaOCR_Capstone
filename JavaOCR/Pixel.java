import java.awt.Color;

public class Pixel {
    private Color pixelColor;
    
    public Pixel(Color color) {
        this.pixelColor = color;
    }
    
    public Color getColor() {
        return this.pixelColor;
    }
    
    public void setColor(Color newColor) {
        this.pixelColor = newColor;
    }
    
    public int getRed() {
        return this.getColor().getRed();
    }
    
    public int getGreen() {
        return this.getColor().getGreen();
    }
    
    public int getBlue() {
        return this.getColor().getBlue();
    }
    
    public void convertToBlackAndWhite(Image image) {
        for (Pixel[] pxArray : image.getImage()) {
            for (Pixel px : pxArray) {
                int average = (px.getRed() + px.getGreen() + px.getBlue()) / 3;
                Color newColor = new Color(average, average, average);
                px.setColor(newColor);
            }
        }
    }
    
    public Image[] seperateByLines(Image image) {
        // Returns an array of individual images that are lines
        
    }
    
    public Image[] extractCharacters(Image[] line) {
        // Accepts an array of line images
        // Returns an array of images of characters
    }
    
    // Wrapper for a Pixel[][]
    private class Image {
        private Pixel[][] image;
        
        public Image(Pixel[][] image) {
            this.image = image;
        }
        
        public Pixel[][] getImage() {
            return this.image;
        }
    }
}