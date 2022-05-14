public class Main {

    public static void main(String[] args) {
        // set mappings to either gregicality OR sog. sog is default
         GTCEMappings.setGregicality();
        // ZsConvertData.setShadowsOfGreg();

        // you can add you own mappings here with ZsConvertData.put...(id, mapping)
        // the default mappings are initialised in ZsConverter.run()

        //technologicalJourney();
        //omnifactory();

        ZsConverter.run(false); // if forceReRun is true, files will be reconverted even if they already were converted
        //JsonConverter.run();
    }

    // here you can find some default mappings that are not active by default

    public static void technologicalJourney() {
        /*GTCEMappings.put(516, "LvSuperconductor");
        GTCEMappings.put(517, "LvSuperconductorBase");
        GTCEMappings.put(518, "Draconium");
        GTCEMappings.put(519, "AwakenDraconium");
        GTCEMappings.put(520, "Chaos");
        GTCEMappings.put(521, "Chaosalloy");
        GTCEMappings.put(524, "StarMetalAlloy");
        GTCEMappings.put(525, "Aquamarine");*/

        /*ZsConvertData.putMbt(1000, "coke_oven_2");
        ZsConvertData.putMbt(1002, "primitive_alloy");
        ZsConvertData.putMbt(1003, "primitive_assembler");
        ZsConvertData.putMbt(1004, "armor_infuser");
        ZsConvertData.putMbt(1005, "chaos_replicator");
        ZsConvertData.putMbt(1006, "dragon_egg_replicator");
        ZsConvertData.putDevtechMachine(1300, "chest_iv");
        ZsConvertData.putDevtechMachine(1301, "tank_iv");
        ZsConvertData.putDevtechMachine(1302, "chest_v");
        ZsConvertData.putDevtechMachine(1303, "tank_v");
        ZsConvertData.putDevtechMachine(1304, "chest_vi");
        ZsConvertData.putDevtechMachine(1305, "tank_vi");
        ZsConvertData.putDevtechMachine(1306, "chest_vii");
        ZsConvertData.putDevtechMachine(1307, "tank_vii");*/
    }

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

    public static void shattered() {
        /*GTCEMappings.put(500, "jade");
        GTCEMappings.put(501, "manasteel");
        GTCEMappings.put(502, "terrasteel");
        GTCEMappings.put(503, "dark_steel");
        GTCEMappings.put(504, "blood_crystal");
        GTCEMappings.put(505, "autismium");
        GTCEMappings.put(506, "elven_elementium");
        GTCEMappings.put(508, "infinity");
        GTCEMappings.put(509, "titanium_iridium");
        GTCEMappings.put(511, "leadstone");
        GTCEMappings.put(512, "coralium");
        GTCEMappings.put(513, "liquified_coralium");
        GTCEMappings.put(514, "jade");
        GTCEMappings.put(515, "jade");
        GTCEMappings.put(516, "jade");
        GTCEMappings.put(517, "jade");
        GTCEMappings.put(519, "jade");
        GTCEMappings.put(520, "jade");
        GTCEMappings.put(521, "jade");
        GTCEMappings.put(522, "jade");
        GTCEMappings.put(523, "jade");
        GTCEMappings.put(524, "jade");
        GTCEMappings.put(525, "jade");
        GTCEMappings.put(526, "jade");
        GTCEMappings.put(527, "jade");
        GTCEMappings.put(528, "jade");
        GTCEMappings.put(529, "jade");
        GTCEMappings.put(530, "jade");
        GTCEMappings.put(531, "jade");
        GTCEMappings.put(532, "jade");
        GTCEMappings.put(533, "jade");
        GTCEMappings.put(534, "jade");
        GTCEMappings.put(535, "jade");
        GTCEMappings.put(536, "jade");
        GTCEMappings.put(537, "jade");
        GTCEMappings.put(538, "jade");
        GTCEMappings.put(539, "jade");
        GTCEMappings.put(540, "jade");
        GTCEMappings.put(541, "jade");
        GTCEMappings.put(542, "jade");
        GTCEMappings.put(543, "jade");
        GTCEMappings.put(544, "jade");
        GTCEMappings.put(545, "jade");
        GTCEMappings.put(546, "jade");
        GTCEMappings.put(547, "jade");
        GTCEMappings.put(548, "jade");
        GTCEMappings.put(549, "jade");
        GTCEMappings.put(550, "jade");
        GTCEMappings.put(551, "jade");
        GTCEMappings.put(552, "jade");
        GTCEMappings.put(553, "jade");
        GTCEMappings.put(554, "jade");
        GTCEMappings.put(555, "jade");
        GTCEMappings.put(556, "jade");
        GTCEMappings.put(557, "jade");
        GTCEMappings.put(558, "jade");
        GTCEMappings.put(559, "jade");*/
    }
}
