import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

// the converter
public class JsonConverter {

    private static final List<String> output = new ArrayList<>();

    private static void log(String msg) {
        // filter user name
        String[] parts = msg.split("\\\\");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("Users") && i < parts.length - 1) {
                parts[i + 1] = "----";
                i++;
            }
        }
        msg = String.join("\\", parts);
        // print & save for output
        System.out.println(msg);
        output.add(msg);
    }

    public static void run() {
        if (!GTCEMappings.isSog()) {
            throw new IllegalStateException("Can't convert Gregicality IDs yet");
        }
        GTCEMappings.init();
        GTCEuMappings.init();

        Scanner sc = new Scanner(System.in);
        //System.out.print("Enter path of bq json file: ");
        String scriptPath = "C:\\Users\\Joel\\Desktop\\bq\\DefaultQuests.json";//sc.nextLine();

        Path path = Paths.get(scriptPath);
        JsonConverter converter = new JsonConverter();
        converter.readFile(path.toFile());
        /*try {
            List<Path> paths = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            paths.forEach(path1 -> {
                if (path1.toString().endsWith(".zs")) {
                    log("Reading file " + path1);
                    JsonConverter converter = new JsonConverter();
                    converter.forceReRun = forceReRun;
                    converter.readFile(path1.toFile());

                    bracketsConverted.addAndGet(converter.bracketsConverted);
                    machineBracketsConverted.addAndGet(converter.machineBracketsConverted);
                    materialBracketsConverted.addAndGet(converter.materialBracketsConverted);
                    metaItemBracketsConverted.addAndGet(converter.metaItemBracketsConverted);
                    log("");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        log("In total " + converter.itemsConverted + " item ids were converted");

        String[] parts = scriptPath.split("\\\\");
        String outputPath = String.join("\\", Arrays.copyOf(parts, parts.length - 1)) + "\\__convertOutput.txt";
        File outputFile = new File(outputPath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            for (String line : output) {
                fileOutputStream.write(line.getBytes(StandardCharsets.UTF_8));
                fileOutputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        output.clear();
    }

    private static boolean readBool(String msg, Scanner sc) {
        System.out.print(msg);
        String val = sc.nextLine();
        return val.equals("y") || val.equals("Y");
    }

    private int currentLine = 0;
    private int itemsConverted;

    private final List<String> lines = new ArrayList<>();
    private boolean insideItem = false;
    private int lastDamage = -1;

    private String line;

    private void readFile(File file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                try {
                    readLine();
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println("Error at " + currentLine + ". " + line);
                }

                lines.add(line);
                currentLine++;
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);

            for (String s : lines) {
                out.write(s);
                out.write("\n");
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println((currentLine + 1) + " lines converted");
    }

    private void readLine() {
        if (!insideItem) {
            insideItem = line.contains("\"Count:3\"");
            return;
        }
        if (line.contains("Damage:2")) {
            lastDamage = currentLine;
            return;
        }
        int index = line.indexOf("id:8");
        if (index >= 0) {
            index = line.indexOf("\"", index + 5) + 1;
            String item = line.substring(index, line.indexOf("\"", index));
            String[] parts = item.split(":");
            String sMeta = getMeta();
            //System.out.println("Found material item " + parts[0] + ":" + parts[1] + ":" + sMeta);
            int meta = Integer.parseInt(sMeta);
            if (parts[0].equals("gregtech") || parts[0].equals("gtadditions")) {
                if (parts[1].contains("meta_item")) {
                    if (meta < 32000) {
                        convertMaterialItem(parts[0], parts[1], sMeta);
                    } else {
                        convertMetaItem(parts[0], parts[1], sMeta);
                    }
                }
                if (parts[1].equals("machine")) {
                    convertMachine(parts[0], parts[1], sMeta);
                }
            }
            insideItem = false;
            lastDamage = -1;
        }
    }

    private void convertMaterialItem(String mod, String itemId, String meta) {
        int materialId = Integer.parseInt(meta);
        int orePrefix = 0;
        if (meta.length() == 4) {
            materialId = Integer.parseInt(meta.substring(1));
            orePrefix = Integer.parseInt(meta.substring(0, 1));
        } else if (meta.length() == 5) {
            materialId = Integer.parseInt(meta.substring(2));
            orePrefix = Integer.parseInt(meta.substring(0, 2));
        }
        String preFix = null;
        if (itemId.endsWith("_1")) {
            preFix = GTCEMappings.ORE_PREFIX_MAP.get(orePrefix);
        } else if (itemId.endsWith("_2")) {
            preFix = GTCEMappings.ORE_PREFIX_MAP_2.get(orePrefix);
        } else if (itemId.startsWith("ga_")) {
            preFix = GTCEMappings.ORE_PREFIX_MAP_GA.get(orePrefix);
        }

        String material = GTCEMappings.MATERIAL_MAP.get(materialId);
        if (material == null) {
            log("Can't find material in GTCE for " + mod + ":" + itemId + ":" + meta + " in line " + currentLine);
            return;
        }
        Integer newMaterialId = GTCEuMappings.MATERIALS.get(toLowerCamelCase(material));
        if (newMaterialId == null) {
            log("Can't find material in GTCEu for " + mod + ":" + itemId + ":" + meta + "(" + material + ") in line " + currentLine);
            return;
        }

        if (preFix != null) {
            String item = "gregtech:meta_" + toLowerCamelCase(preFix);
            changeMeta(newMaterialId);
            setItem(item);
            itemsConverted++;
        } else {
            log("Can't find ore prefix for " + mod + ":" + itemId + ":" + meta + " in line " + currentLine);
        }
    }

    private void convertMetaItem(String mod, String itemId, String meta) {
        int id = Integer.parseInt(meta);
        String metaItem = null;
        if (itemId.endsWith("_1")) {
            metaItem = GTCEMappings.META_ITEM_MAP_1.get(id);
        } else if (itemId.endsWith("_2")) {
            metaItem = GTCEMappings.META_ITEM_MAP_2.get(id);
        } else if (itemId.startsWith("ga_")) {
            metaItem = GTCEMappings.META_ITEM_MAP_GA.get(id);
        }

        if (metaItem != null) {
            Integer newId = GTCEuMappings.META_ITEMS.get(metaItem);
            if (newId != null) {
                changeMeta(newId);
                setItem("gregtech:meta_item_1");
            } else {
                log("Could not find meta item in GTCEu for " + mod + ":" + itemId + ":" + meta + " in line " + currentLine);
            }
        } else {
            log("Could not find meta item in GTCE for " + mod + ":" + itemId + ":" + meta + " in line " + currentLine);
        }
    }

    private void convertMachine(String mod, String itemId, String meta) {
        int id = Integer.parseInt(meta);
        String metaItem = GTCEMappings.MACHINES.get(id);

        if (metaItem != null) {
            Integer newId = GTCEuMappings.MACHINES.get(metaItem);
            if (newId == null) {
                String[] parts = metaItem.split(":");
                if (parts.length > 1) {
                    newId = GTCEuMappings.MACHINES.get(parts[1]);
                }
            }
            if (newId != null) {
                changeMeta(newId);
                //setItem("gregtech:machine");
            } else {
                log("Could not find machine in GTCEu for " + mod + ":" + itemId + ":" + meta + " in line " + currentLine);
            }
        } else {
            log("Could not find machine in GTCE for " + mod + ":" + itemId + ":" + meta + " in line " + currentLine);
        }
    }

    private String getMeta() {
        if (lastDamage < 0) {
            throw new IllegalStateException("No meta found");
        }
        String line = lines.get(lastDamage);
        int index = line.indexOf("Damage:2");
        index = line.indexOf(": ", index + 8);
        return line.substring(index + 2, line.lastIndexOf(","));
    }

    private void changeMeta(int val) {
        if (lastDamage < 0) {
            throw new IllegalStateException("No meta found");
        }
        String line = lines.get(lastDamage);
        int index = line.indexOf("Damage:2");
        index = line.indexOf(": ", index + 8);
        line = line.substring(0, index + 2) + val + ",";
        lines.set(lastDamage, line);
    }

    private void setItem(String newItem) {
        int index = line.indexOf("id:8");
        if (index >= 0) {
            index = line.indexOf("\"", index + 5);
            line = line.substring(0, index + 1) + newItem + line.substring(line.lastIndexOf("\""));
        } else {
            System.out.println("Error setting item: " + line);
        }
    }

    private static String toLowerCamelCase(String s) {
        if (s.equals("HSSE"))
            return "hsse";
        if (s.equals("HSSS"))
            return "hsss";
        if (s.equals("HSSG"))
            return "hssg";
        StringBuilder builder = new StringBuilder();

        boolean lastWasNumber = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    builder.append("_");
                }
                builder.append(Character.toLowerCase(c));
                lastWasNumber = false;
            } else if (isNumber(c)) {
                if (!lastWasNumber) {
                    if (i > 0) {
                        builder.append("_");
                    }
                    builder.append(c);
                }
                lastWasNumber = true;
            } else {
                builder.append(c);
                lastWasNumber = false;
            }

        }
        return builder.toString();
    }

    private static boolean isNumber(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }
}
