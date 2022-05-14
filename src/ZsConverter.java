import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// the converter
public class ZsConverter {

    private static final List<String> output = new ArrayList<>();

    private static void log(String msg) {
        // filter user name
        String[] parts = msg.split("\\\\");
        for(int i = 0; i < parts.length; i++) {
            if(parts[i].equals("Users") && i < parts.length - 1) {
                parts[i+1] = "----";
                i++;
            }
        }
        msg = String.join("\\", parts);
        // print & save for output
        System.out.println(msg);
        output.add(msg);
    }

    public static void run(boolean forceReRun) {
        GTCEMappings.init();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter path of script folder: ");
        String scriptPath = sc.nextLine();

        AtomicInteger bracketsConverted = new AtomicInteger();
        AtomicInteger machineBracketsConverted = new AtomicInteger();
        AtomicInteger materialBracketsConverted = new AtomicInteger();
        AtomicInteger metaItemBracketsConverted = new AtomicInteger();

        Path path = Paths.get(scriptPath);
        try {
            List<Path> paths = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            paths.forEach(path1 -> {
                if (path1.toString().endsWith(".zs")) {
                    log("Reading file " + path1);
                    ZsConverter converter = new ZsConverter();
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
        }

        log(" * In total " + bracketsConverted + " brackets were converted");
        log(" * Material Brackets: " + materialBracketsConverted);
        log(" * MetaItem Brackets: " + metaItemBracketsConverted);
        log(" * Machine Brackets: " + machineBracketsConverted);

        File outputFile = new File(scriptPath + "\\__convertOutput.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            for(String line : output) {
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

    private boolean forceReRun;
    private int currentLine = 0;
    private int bracketsConverted;
    private int machineBracketsConverted;
    private int materialBracketsConverted;
    private int metaItemBracketsConverted;
    private String line;

    private void readFile(File file) {
        List<String> lines = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                readLine();
                lines.add(line);
                currentLine++;
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);
            bracketsConverted = machineBracketsConverted + materialBracketsConverted + metaItemBracketsConverted;

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
        int brIndex = line.indexOf("<");

        while (brIndex >= 0) {
            int closingBrIndex = line.indexOf(">", brIndex);
            if (closingBrIndex < 0) {
                log("Could not find closing >");
                return;
            }
            String part1 = line.substring(0, brIndex + 1);
            String bracketContent = line.substring(brIndex + 1, closingBrIndex);
            String part2 = line.substring(closingBrIndex);

            bracketContent = handleBracket(bracketContent);
            closingBrIndex = brIndex + bracketContent.length() + 1;

            line = part1 + bracketContent + part2;

            brIndex = line.indexOf("<", closingBrIndex + 1);
        }
    }

    private void addError(String msg) {

    }

    private int countChar(char c, String string) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c)
                count++;
        }
        return count;
    }

    private String handleBracket(String bracketContent) {
        bracketContent = convertToProperBracket(bracketContent);
        return bracketContent;
    }

    private String convertToProperBracket(String bracketContent) {
        String[] parts = bracketContent.split(":");
        String oldBracket = bracketContent;
        if (parts.length == 3 && (parts[0].equals("gregtech") || parts[0].equals("gtadditions"))) {
            if (parts[1].contains("meta_item")) {
                String meta = parts[2];
                String materialMeta;
                int orePrefixMeta = -1;

                if (meta.length() < 4) {
                    materialMeta = meta;
                    orePrefixMeta = 0;
                } else {
                    materialMeta = meta.substring(meta.length() - 3);
                    if(meta.length() == 4) {
                        orePrefixMeta = Integer.parseInt(meta.substring(0, 1));
                    } else if(meta.length() == 5) {
                        orePrefixMeta = Integer.parseInt(meta.substring(0, 2));
                    }
                }

                bracketContent = "metaitem:";

                if (orePrefixMeta == 32) {
                    String metaItem = findMetaItem(Integer.parseInt(meta), parts[1]);
                    if (metaItem == null) {
                        log("Could not find meta item with meta " + meta + " in line " + (currentLine + 1));
                        addError("  //FIXME: MetaItem id: " + materialMeta);
                        return oldBracket;
                    }
                    bracketContent += metaItem;
                    metaItemBracketsConverted++;
                } else {
                    String materialItem = findMaterialItem(Integer.parseInt(materialMeta), orePrefixMeta, parts[1]);
                    if (materialItem == null) {
                        log("Could not find material with meta " + orePrefixMeta + materialMeta + " in line " + (currentLine + 1) + ". Item id is invalid!");
                        addError("  //FIXME: <" + bracketContent + ">");
                        return oldBracket;
                    }
                    if (materialItem.equals("nM")) {
                        log("Could not find material for id " + materialMeta + " in line " + (currentLine + 1));
                        addError("  //FIXME: Material id: " + materialMeta);
                        return oldBracket;
                    }
                    if (materialItem.equals("nO")) {
                        log("Could not find OrePrefix with meta " + orePrefixMeta + " in line " + (currentLine + 1));
                        addError("  //FIXME: OrePrefix id: " + orePrefixMeta);
                        return oldBracket;
                    }
                    bracketContent += materialItem;
                    materialBracketsConverted++;
                }
            } else if (parts[1].equals("machine")) {
                String meta = parts[2];
                String machine = GTCEMappings.MACHINES.get(Integer.parseInt(meta));
                if (machine == null) {
                    log("Could not find machine with meta " + meta + " in line " + (currentLine + 1));
                    line += "  //FIXME: Machine id: " + meta;
                    return oldBracket;
                }
                bracketContent = "meta_tile_entity:" + machine;
                machineBracketsConverted++;
            }
        }
        return bracketContent;
    }

    private String findMetaItem(int id, String rawItem) {
        Map<Integer, String> metaItems = null;
        if (rawItem.startsWith("ga_")) {
            if (rawItem.endsWith("2"))
                metaItems = GTCEMappings.META_ITEM_MAP_GA_2;
            else
                metaItems = GTCEMappings.META_ITEM_MAP_GA;
        } else if (rawItem.endsWith("_1")) {
            metaItems = GTCEMappings.META_ITEM_MAP_1;
        } else if (rawItem.endsWith("_2")) {
            metaItems = GTCEMappings.META_ITEM_MAP_2;
        }
        if (metaItems != null) {
            return metaItems.get(id);
        }
        return null;
    }

    private String findMaterialItem(int materialMeta, int orePrefixMeta, String rawItem) {
        String material = GTCEMappings.MATERIAL_MAP.get(materialMeta);
        if (material == null) {
            return "nM";
        }

        Map<Integer, String> orePrefixes = null;

        if (rawItem.startsWith("ga_")) {
            orePrefixes = GTCEMappings.ORE_PREFIX_MAP_GA;
        } else if (rawItem.endsWith("_1")) {
            orePrefixes = GTCEMappings.ORE_PREFIX_MAP;
        } else if (rawItem.endsWith("_2")) {
            orePrefixes = GTCEMappings.ORE_PREFIX_MAP_2;
        }

        if (orePrefixes != null) {
            String orePrefix = orePrefixes.get(orePrefixMeta);
            if (orePrefix == null) {
                return "nO";
            }
            return orePrefix + material;
        }
        return null;
    }

    private String convertToCeu(String bracketContent) {
        String[] parts = bracketContent.split(":");
        String oldBracket = bracketContent;
        if (parts.length == 3 && (parts[0].equals("gregtech"))) {

        }
        return bracketContent;
    }
}
