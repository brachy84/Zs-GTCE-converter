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

// reads materials from a mod
public class MaterialReader {

    public static void run() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter path of script folder: ");
        String scriptPath = sc.nextLine();
        //System.out.print("Function Name: ");
        String functionName = "put";//sc.nextLine();

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
                String s = readLine(line, functionName);
                if(s != null)
                    System.out.println(s);
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readLine(String line, String functionName) {
        int index = line.indexOf(".Builder(");
        if (index >= 0) {
            int i = line.indexOf("(", index + 6);
            int comma = line.indexOf(",", i);
            String id = line.substring(i + 1, comma);
            int nameStart = line.indexOf("\"", comma + 1);
            String name = line.substring(nameStart + 1, line.indexOf("\"", nameStart + 1));
            //System.out.println(id + " " + name);
            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                return null;
            }
            return functionName + "(" + id + ", \"" + name + "\");";

            /*int comma = line.indexOf(",");
            String id = line.substring(index + 9, comma);
            int s = line.indexOf("\"") + 1;
            String newId = line.substring(s, line.indexOf("\"", s));
            newId = toUpperCamelCase(newId);
            line = functionName + "(" + id + ", \"" + newId + "\");";
            return line;*/
        } else
            return null;
    }

    private static String toUpperCamelCase(String s) {
        StringBuilder builder = new StringBuilder();
        builder.append(Character.toUpperCase(s.charAt(0)));
        boolean lastWasUnderscore = false;
        for(int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if(lastWasUnderscore) {
                builder.append(Character.toUpperCase(c));
                continue;
            }
            if(c == '_') {
                lastWasUnderscore = true;
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }
}
