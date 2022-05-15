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

// reads machines from a mod
public class MetaMachineReader {

    public static final long[] GAV = new long[]{8, 32, 128, 512, 2048, 8192, 32768, 131072, 524288, 2097152, 8388608, 33554432, 134217728, 536870912, Integer.MAX_VALUE};
    public static final long[] V = new long[]{8, 32, 128, 512, 2048, 8192, 32768, 131072, 524288, Integer.MAX_VALUE};
    public static final String[] GAVN = new String[]{"ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "UHV", "UEV", "UIV", "UMV", "UXV", "MAX"};
    public static final String[] VN = new String[]{"ULV", "LV", "MV", "HV", "EV", "IV", "LuV", "ZPM", "UV", "MAX"};

    public static void runSpecial() {
        String functionName = "putMachine";
        /*int[] batteryBufferSlots = new int[]{1, 4, 9, 16};
        for (int i = 0; i < V.length; i++) {
            if (i > 0 && i <= V.length - 2) {
                String id = "transformer." + VN[i].toLowerCase();
                int id2 = 600 + (i - 1);
                System.out.println(functionName + "(" + id2 + ", \"" + id + "\");");
            }
            for (int slot = 0; slot < batteryBufferSlots.length; slot++) {
                String transformerId = "battery_buffer." + VN[i].toLowerCase() + "." + batteryBufferSlots[slot];
                int id2 = 610 + batteryBufferSlots.length * i + slot;
                System.out.println(functionName + "(" + id2 + ", \"" + transformerId + "\");");
            }
            String chargerId = "charger." + VN[i].toLowerCase();

            System.out.println(functionName + "(" + (680 + i) + ", \"" + chargerId + "\");");
        }*/

        /*for (int i = 0; i < V.length; i++) {
            String voltageName = VN[i].toLowerCase();
            List<String> ids = new ArrayList<>();
            ids.add("item_bus.import." + voltageName);
            ids.add("item_bus.export." + voltageName);
            ids.add("fluid_hatch.import." + voltageName);
            ids.add("fluid_hatch.export." + voltageName);
            ids.add("energy_hatch.input." + voltageName);
            ids.add("energy_hatch.output." + voltageName);

            int index = 0;
            for(String id : ids) {
                print(functionName, 700 + 10 * i + index, id);
                index++;
            }
        }*/

        /*for (int i = 1; i < 5; i++) {
            String voltageName = VN[i].toLowerCase();
            print(900 + 10 * (i - 1), "pump." + voltageName);
            print(950 + 10 * (i - 1), "air_collector." + voltageName);
        }*/

        /*for (int i = 2; i < 6; i++) {
            String voltageName = VN[i].toLowerCase();
            print(1010 + (i - 2), "quantum_chest." + voltageName);
            print(1020 + (i - 2), "quantum_tank." + voltageName);
        }

        for (int i = 1; i < 5; i++) {
            String voltageName = VN[i].toLowerCase();
            print(1030 + (i - 1), "block_breaker." + voltageName);
        }*/

        // gregicality
        int id = 2900;
        int[] values = new int[]{1, 4, 9, 16};
        /*for (int tier = 0; tier < 9; ++tier) {
            for (int value : values) {
                final String vn = VN[tier].toLowerCase();
                print(id++,  "convert_gteu." + vn + "." + value);
                print(id++, "convert_forge." + vn + "." + value);
            }
        }*/

        /*id = 2972;
        for (int i = 0; i < V.length - 1; i++) { // minus 1 because we dont want MAX tier
            if (i > 0) {
                String voltageName = VN[i].toLowerCase();
                print(id++, "transformer." + voltageName + ".4");
                print(id++, "transformer." + voltageName + ".8");
                print(id++, "transformer." + voltageName + ".12");
                print(id++, "transformer." + voltageName + ".16");
            }
        }
        for (int i = 0; i < V.length - 1; i++) {
            String voltageName = VN[i].toLowerCase();
            print(id++, "energy_hatch.input." + voltageName + ".4");
            print(id++, "energy_hatch.input." + voltageName + ".16");
            print(id++, "energy_hatch.input." + voltageName + ".64");
            print(id++, "energy_hatch.input." + voltageName + ".128");

            print(id++, "energy_hatch.output." + voltageName + ".16");
            print(id++, "energy_hatch.output." + voltageName + ".32");
            print(id++, "energy_hatch.output." + voltageName + ".64");
            print(id++, "energy_hatch.output." + voltageName + ".128");
        }*/

        /*id = 3220;
        for (int i = 0; i < V.length; i++) {
            print(id++, "fluid_hatch.export_filtered." + VN[i].toLowerCase());
        }*/

        /*id = 4035;
        for (int i = 9; i < GAV.length - 1; i++) {
            String voltageName = GAVN[i].toLowerCase();
            print(id++, "energy_hatch.input." + voltageName + ".4");
            print(id++, "energy_hatch.input." + voltageName + ".16");
            print(id++, "energy_hatch.input." + voltageName + ".64");
            print(id++, "energy_hatch.input." + voltageName + ".128");

            print(id++, "energy_hatch.output." + voltageName + ".16");
            print(id++, "energy_hatch.output." + voltageName + ".32");
            print(id++, "energy_hatch.output." + voltageName + ".64");
            print(id++, "energy_hatch.output." + voltageName + ".128");
        }

        for (int tier = 9; tier < GAV.length - 1; ++tier) {
            for (int value : values) {
                final String vn = GAVN[tier].toLowerCase();
                print(id++, "convert_gteu." + vn + "." + value);
                print(id++, "convert_forge." + vn + "." + value);
            }
        }

        for (int i = 9; i < GAV.length - 1; i++) { // minus 1 because we dont want MAX tier
            final String vn = GAVN[i].toLowerCase();
            print(id++, "transformer." + vn + ".4");
            print(id++, "transformer." + vn + ".8");
            print(id++, "transformer." + vn + ".12");
            print(id++, "transformer." + vn + ".16");
        }*/

        /*id = 4145;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                print(id++, "battery_buffer." + GAVN[i + 9].toLowerCase() + "." + (int) Math.pow(j + 1, 2));
            }
            print(id++, "charger." + GAVN[i + 9].toLowerCase());
        }*/

        /*id = 4179;
        for (int i = 9; i < GAV.length - 1; i++) { // minus 1 because we dont want MAX tier
            print(id++, "transformer." + GAVN[i].toLowerCase());
        }
        id = 4184;
        for (int i = 1; i < GAV.length - 1; i++) { // minus 1 because we dont want MAX tier, plus one because we dont want ULV
            print(id++, "diode." + GAVN[i].toLowerCase());
        }*/

        /*id = 4198;
        for (int i = 1; i < GAV.length - 1; i++) {
            print(id++, "disassembler." + GAVN[i].toLowerCase());
        }*/

        /*id = 4215;
        for (int i = 1; i <= 8; i++)
            print(id + i, "muffler_hatch." + GAVN[i].toLowerCase());*/

        // ceu (the bad one)
        /*List<String> ceu = converter(true);
        List<String> cef = converter(false);

        for(int i = 0; i < ceu.size(); i++){
            print(10650+i*2, ceu.get(i));
            print(10650+i*2+1, cef.get(i));
        }

        putMachine(10649, "fe_emitter");
        putMachine(10648, "fe_receiver");
        putMachine(10647, "gteu_emitter");
        putMachine(10646, "gteu_receiver");*/

        // GTCEU
        /*machines("gas_collector", 530, GAV.length - 2, 1);
        machines("hull", 985, GAV.length, 0);
        machines("item_bus.import", 1150, 10, 0);
        machines("item_bus.export", 1165, 10, 0);
        machines("fluid_hatch.import", 1180, 10, 0);
        machines("fluid_hatch.export", 1195, 10, 0);
        machines("energy_hatch.input", 1210, GAV.length, 0);
        machines("energy_hatch.output", 1225, GAV.length, 0);
        machines("energy_hatch.input_4a", 1240, 5, 5);
        machines("energy_hatch.input_16a", 1245, 5, 5);
        machines("energy_hatch.output_4a", 1250, 5, 5);
        machines("energy_hatch.output_16a", 1255, 5, 5);
        machines("transformer", 1270, GAV.length - 2, 0);
        machines("transformer.adjustable", 1285, GAV.length - 2, 0);
        machines("diode", 1300, GAV.length - 1, 0);*/
        /*int[] batteryBufferSlots = new int[]{4, 8, 16};
        for (int slot = 0; slot < batteryBufferSlots.length; slot++) {
            for (int i = 0; i < GAV.length - 1; i++) {
                String bufferId = "battery_buffer." + GAVN[i].toLowerCase() + "." + batteryBufferSlots[slot];
                print(1315 + GAV.length * slot + i, bufferId);
            }
        }*/
        machines("charger", 1375, GAV.length - 1, 0);
        machines("super_chest", 1560, 5, 1);
        machines("quantum_chest", 1565, 10, 5);
        machines("super_tank", 1575, 5, 1);
        machines("quantum_tank", 1580, 10, 5);
        machines("block_breaker", 1590, 4, 1);
        machines("muffler_hatch", 1657, 8, 1);
    }

    private static void machines(String baseId, int baseIntId, int length, int start) {
        for(int i = 0; i < length; i++) {
            print(baseIntId + i, baseId + "." + GAVN[i + start].toLowerCase());
        }
    }

    public static List<String> converter(boolean convertsToFe) {
        // Putting MAX in between UV and UHV so MAX converters stay in same numerical IDs
        List<String> converters = new ArrayList<>();
        for (int tier = 0; tier <= 8; tier++)
            addConverter(converters, tier, convertsToFe);
        addConverter(converters, 14, convertsToFe);
        for (int tier = 9; tier <= 13; tier++)
            addConverter(converters, tier, convertsToFe);
        return converters;
    }

    private static void addConverter(List<String> converters, int tier, boolean convertsToFe) {
        for (int slots = 1; slots <= 4; slots++)
            converters.add((convertsToFe ? "ceu." : "cef.") + GAVN[tier].toLowerCase() + "." + (slots * slots));
    }

    public static List<String> converter2(boolean convertsToFe) {
        List<String> converters = new ArrayList<>();
        for (int tier = 0; tier < V.length; tier++)
            for (int slots = 1; slots <= 4; slots++) {
                converters.add((convertsToFe ? "ceu." : "cef.") + VN[tier].toLowerCase() + "." + (slots * slots));
            }
        return converters;
    }

    private static void print(int meta, String id) {
        String functionName = "putMachine";
        System.out.println(functionName + "(" + meta + ", \"" + id + "\");");
    }

    public static void run() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter path of script folder: ");
        String scriptPath = sc.nextLine();

        String functionName = "putMachine";

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
        String line = null;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                String s = readLine(line, functionName);
                /*if (s != null)
                    System.out.println(s);*/
            }
            fr.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readLine(String line, String functionName) {
        int offset = 23;
        int index = line.indexOf("registerMetaTileEntity(");
        if (index < 0) {
            index = line.indexOf("registerSimpleMetaTileEntity(");
            offset = 29;
            if (index >= 0) {
                int comma1 = line.indexOf(",", index);
                int comma2 = line.indexOf(",", comma1 + 1);
                if (line.charAt(comma1 + 1) == ' ')
                    comma1++;
                String id = line.substring(comma1 + 1, comma2);
                int s = line.indexOf("\"") + 1;
                if (s <= 0) {
                    System.out.println("INAVLID MACHINE id: " + id);
                    return null;
                }
                String newId = line.substring(s, line.indexOf("\"", s));
                for (int i = 0; i < GAV.length - 2; i++) {
                    int intId = Integer.parseInt(id) + i;
                    System.out.println(functionName + "(" + intId + ", \"" + newId + "." + GAVN[i + 1].toLowerCase() + "\");");
                }
            }
            return null;
        }
        if (index >= 0) {
            int comma = line.indexOf(",", index);
            String id = line.substring(index + offset, comma);
            int s = line.indexOf("\"") + 1;
            if (s <= 0) {
                System.out.println("INAVLID MACHINE id: " + id);
                return null;
            }
            String newId = line.substring(s, line.indexOf("\"", s));
            line = functionName + "(" + id + ", \"" + newId + "\");";
            System.out.println(line);
            return line;
        } else
            return null;
    }
}
