import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// reads meta items from a mod
public class MetaItemReader {

    public static final String[] colors = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray"/*silver*/, "cyan", "purple", "blue", "brown", "green", "red", "black"
    };

    public static void special() {
        /*for (int i = 0; i < colors.length; i++) {
            String dyeColor = colors[i];
            //System.out.println("putMI(" + (62 + i) + ", \"" + "spray.can.dyes." + dyeColor.getName() + "\");");
            //System.out.println("putMI(" + (421 + i) + ", \"" + "dye." + dyeColor.getName() + "\");");

            if (i > 0) // no white
                System.out.println("putMI(" + (820 + i) + ", \"" + "glass_lens." + dyeColor + "\");");
        }*/
    }

    public static void run() {
        Scanner sc = new Scanner(System.in); //System.in is a standard input stream
        System.out.print("Enter path of script folder: ");
        String scriptPath = sc.nextLine();
        System.out.print("Function Name: ");
        String functionName = sc.nextLine();

        Path path = Paths.get(scriptPath);
        try {
            List<Path> paths = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            paths.forEach(path1 -> {
                System.out.println("Reading file " + path1);
                readFile(path1.toFile(), functionName);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFile(File file, String functionName) {
        List<String> lines = new ArrayList<>();
        String line = null;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                line = readLine(line, functionName);
                if (line != null)
                    System.out.println(line);
            }
            fr.close();
            br.close();

            /*for (String s : lines) {
                if(s != null)
                    System.out.println(s);
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readLine(String line, String functionName) {
        int index = line.indexOf("addItem(");
        if (index > 0) {
            int comma = line.indexOf(",", index + 7);
            String id = line.substring(index + 8, comma);
            /*if(id.length() == 1)
                id = "00" + id;
            else if(id.length() == 2)
                id = "0" + id;*/
            int s = line.indexOf("\"") + 1;
            String newId = line.substring(s, line.indexOf("\"", s));
            line = functionName + "(" + id + ", \"" + newId + "\");";
            return line;
        } else
            return null;
    }
}
