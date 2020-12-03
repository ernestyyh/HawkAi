import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class mapOfHawker {

    HashMap<String, Hawker> map = new HashMap<>();

    public mapOfHawker() {
        map.put("640964", new Hawker("S11 Eating House",
             "964 Jurong West Street 91", "640964",
             1.342105, 103.692821));

        map.put("640907", new Hawker("You & Lai Coffee Foodlink",
             "907 Jurong West Street 91", "640907",
             1.341361, 103.685819));
        
        map.put("640964", new Hawker("Best Coffee Pte Ltd",
             "959 Jurong West Street 91", "640959",
             1.341568, 103.690501));
        
        map.put("640851", new Hawker("851 Coffeeshop",
             "851 Jurong West Street 81", "640851",
             1.346722, 103.692671));

        map.put("640851", new Hawker("Hong Joo Eating House Pte Ltd",
             "851 Jurong West Street 81", "640851",
             1.346792, 103.692657));

        map.put("640815", new Hawker("Eating House 815",
             "815 Jurong West Street 81", "640815",
             1.345334, 103.696149));

        map.put("648202", new Hawker("Jurong West Hawker Centre & Market",
             "50 Jurong West Street 61", "648202",
             1.341182, 103.697250));

        try {
            Scanner read = new Scanner(new File("src/main/resources/titlecase.txt"));

            while (read.hasNext()) {
                String name = read.nextLine();
                String address = read.nextLine();
                String postal = read.nextLine();
                double lat = read.nextDouble();
                double lon = read.nextDouble();
                read.nextLine();

                map.put(postal, new Hawker(name, address, postal, lat, lon));
            }
        } catch(FileNotFoundException e) {

        }

    }

    public HashMap<String, Hawker> getMap() {
        return map;
    }

    public Hawker getHawker(String postal) {
        Hawker result = null;

        for (Hawker current : map.values()) {
            if (current.getPostal().equals(postal)) {
                result = current;
                break;
            }
        }

        return result;
    }
}