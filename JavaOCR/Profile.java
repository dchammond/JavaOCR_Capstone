import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Profile - this class is meant to be the entry point for this OCR program
 * @author Dillon Hammond
 * @version 1.0
 */
public class Profile {
    private static String directoryToStoreData;
    
    /**
     * main - The main method of hte program
     * @param args A String array where
     * <br>
     * args[0] == where to place training data
     * <br>
     * args[1] == the location of a typed txt field containing the information you will write in you handwriting sample
     * <br>
     * args[2] == the location of an image of your writing
     */
    public static void main(String[] args) {
        setTrainingDataDestination(args[0]);
        //submitHandwritingSample(args[1], args[2]);
        examples(args[1], args[2]);
    }
    
    /**
     * setTrainingDataDestination - a method to set whe Profile will send its trainging info to
     * @param pathToFolder The path to the folder where the data will be stored
     */
    public static void setTrainingDataDestination(String pathToFolder) {
        directoryToStoreData = pathToFolder;
    }
    
    /**
     * submitHandwritingSample - A method to submit handwriting to the program
     * @param typedDocument The path to a txt file containing a typed version of your writing
     * @param pathToImageOfWriting The path to the image of your writing
     */
    public static void submitHandwritingSample(String typedDocument, String pathToImageOfWriting) {
        ArrayList<ArrayList<Pixel>> image = new ArrayList<ArrayList<Pixel>>(0);
        try {
            image = ImageHandler.readImage(pathToImageOfWriting);
        } catch (Exception e) {
            System.out.println("An error occured reading hte image: " + e);
            System.exit(0);
        }
        String written;
        try {
            written = readFile(typedDocument);
        } catch (Exception e) {
            written = "";
        }
        written.replaceAll("\\s","");// Get rid of whitespace in string wiht regex
        ArrayList<ArrayList<ArrayList<Pixel>>> characters = Pixel.normalizeCharacters(Pixel.extractCharacters(Pixel.seperateByLines(image)));
        for (int i = 0; i < characters.size(); ++i) {
            ImageHandler.createNewImage(characters.get(i), directoryToStoreData+"char"+written.substring(i, i+1)+".png");
        }
    }
    
    /**
     * examples - An example mehtod to ceate a black and white copy of an iamge and to print the image broken into lines
     * @param typedDocument The path to a txt file containing a typed version of your writing
     * @param pathToImage The path to the image of your writing
     */
    public static void examples(String typedDocument, String pathToImage) {
        ArrayList<ArrayList<Pixel>> image = new ArrayList<ArrayList<Pixel>>(0);
        try {
            image = ImageHandler.readImage(pathToImage);
        } catch (Exception e) {
            System.out.println("An error occured reading the image: " + e);
            System.exit(0);
        }
        Pixel.convertToBlackAndWhite(image);
        ArrayList<ArrayList<Pixel>> blackAndWhite = image;
        ImageHandler.createNewImage(blackAndWhite, directoryToStoreData+"blackandwhite.png");
        ArrayList<ArrayList<ArrayList<Pixel>>> lines = Pixel.seperateByLines(image);
        for (int i = 0; i < lines.size(); ++i) {
            ImageHandler.createNewImage(lines.get(i), directoryToStoreData+"line"+i+".png");
        }
    }
    
    /**
     * readFile - A method to convert a fiel into a String
     * @param fileName The path of the file on disk
     * @throws IOException An Exception is generated if the file cannot be read
     * @return String A string containing the data of the text file
     */
    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("An error ocurred reading a text file: " + e);
            System.exit(0);
        } finally {
            br.close();
        }
        return "";
    }
}
