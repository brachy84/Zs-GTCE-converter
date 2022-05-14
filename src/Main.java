public class Main {

    public static void main(String[] args) {
        // set mappings to either gregicality OR sog. sog is default
         GTCEMappings.setGregicality();
        // ZsConvertData.setShadowsOfGreg();

        // you can add you own mappings here with ZsConvertData.put...(id, mapping)
        // the default mappings are initialised in ZsConverter.run()

        //technologicalJourney();
        //omnifactory();

        ZsConverter.run(); // if forceReRun is true, files will be reconverted even if they already were converted
        //JsonConverter.run();
    }

    // here you can find some default mappings that are not active by default

    public static void omnifactory() {
        GTCEMappings.put(700, "ConductiveIron");
        GTCEMappings.put(701, "EnergeticAlloy");
        GTCEMappings.put(702, "VibrantAlloy");
        GTCEMappings.put(703, "PulsatingIron");
        GTCEMappings.put(704, "DarkSteel");
        GTCEMappings.put(705, "ElectricalSteel");
        GTCEMappings.put(706, "Lumium");
        GTCEMappings.put(707, "Signalum");
        GTCEMappings.put(708, "Enderium");
        GTCEMappings.put(709, "Omnium");
        GTCEMappings.put(710, "Draconium");
        GTCEMappings.put(712, "EndSteel");
        GTCEMappings.put(713, "Ardite");
        GTCEMappings.put(714, "Manyullyn");
        GTCEMappings.put(976, "Microversium");
    }
}
