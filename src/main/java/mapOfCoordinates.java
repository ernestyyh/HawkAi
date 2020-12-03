import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class mapOfCoordinates {
    HashMap<String, Coordinates> map = new HashMap<>();

    public mapOfCoordinates() {
        try {
            Scanner read = new Scanner(new File("src/main/resources/postal.txt"));

            while (read.hasNext()) {
                String postal = read.next();
                double lat = read.nextDouble();
                double lon = read.nextDouble();

                map.put(postal, new Coordinates(lat, lon));
            }
        } catch(FileNotFoundException e) {

        }
    }

    public Coordinates getCoordinates(String postal) {

        return map.get(postal);
    }
}
