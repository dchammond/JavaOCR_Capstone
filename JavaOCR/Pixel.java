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
        String threshold = "969696"; // Hex version of (150, 150, 150), our threshold for grey
        for (Pixel[] pxArray : image.getImage()) {
            for (Pixel px : pxArray) {
                String currentColor = Integer.toHexString(px.getRed()) + Integer.toHexString(px.getGreen()) + Integer.toHexString(px.getBlue());
                Color newColor;
                newColor = threshold.compareToIgnoreCase(currentColor) <= 0 ? Color.BLACK : Color.WHITE;
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