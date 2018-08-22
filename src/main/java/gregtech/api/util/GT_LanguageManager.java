package gregtech.api.util;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.api.GregTech_API;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.Map.Entry;

import static gregtech.api.enums.GT_Values.E;

public class GT_LanguageManager {
    public static final HashMap<String, String> TEMPMAP = new HashMap<String, String>(), BUFFERMAP = new HashMap<String, String>(), LANGMAP = new HashMap<String, String>();
    public static Configuration sEnglishFile;
    public static boolean sUseEnglishFile = false;
    public static boolean i18nPlaceholder = true;

    public static String addStringLocalization(String aKey, String aEnglish) {
        return addStringLocalization(aKey, aEnglish, true);
    }

    public static synchronized String addStringLocalization(String aKey, String aEnglish, boolean aWriteIntoLangFile) {
        if (aKey == null) return E;
        if (aWriteIntoLangFile){ aEnglish = writeToLangFile(aKey, aEnglish);
        if(!LANGMAP.containsKey(aKey)){
        	LANGMAP.put(aKey, aEnglish);
        	}
        }
        TEMPMAP.put(aKey.trim(), aEnglish);
        LanguageRegistry.instance().injectLanguage("en_US", TEMPMAP);
        TEMPMAP.clear();
        if(sUseEnglishFile && !aWriteIntoLangFile && LANGMAP.containsKey(aKey)){
        	aEnglish = LANGMAP.get(aKey);
        }
        return aEnglish;
    }

    private static synchronized String writeToLangFile(String aKey, String aEnglish) {
        if (aKey == null) return E;
        if (sEnglishFile == null) {
            BUFFERMAP.put(aKey.trim(), aEnglish);
        } else {
            if (!BUFFERMAP.isEmpty()) {
                for (Entry<String, String> tEntry : BUFFERMAP.entrySet()) {
                    Property tProperty = sEnglishFile.get("LanguageFile", tEntry.getKey(), tEntry.getValue());
                    if (!tProperty.wasRead() && GregTech_API.sPostloadFinished) sEnglishFile.save();
                }
                BUFFERMAP.clear();
            }
            Property tProperty = sEnglishFile.get("LanguageFile", aKey.trim(), aEnglish);
            if (!tProperty.wasRead() && GregTech_API.sPostloadFinished) sEnglishFile.save();
            if (sEnglishFile.get("EnableLangFile", "UseThisFileAsLanguageFile", false).getBoolean(false)){
                aEnglish = tProperty.getString();
                sUseEnglishFile = true;
            }
        }
        return aEnglish;
    }

    public static String getTranslation(String aKey) {
        if (aKey == null) return E;
        String tTrimmedKey = aKey.trim(), rTranslation = LanguageRegistry.instance().getStringLocalization(tTrimmedKey);
        if (GT_Utility.isStringInvalid(rTranslation)) {
            rTranslation = StatCollector.translateToLocal(tTrimmedKey);
            if (GT_Utility.isStringInvalid(rTranslation) || tTrimmedKey.equals(rTranslation)) {
                if (aKey.endsWith(".name")) {
                    rTranslation = StatCollector.translateToLocal(tTrimmedKey.substring(0, tTrimmedKey.length() - 5));
                    if (GT_Utility.isStringInvalid(rTranslation) || tTrimmedKey.substring(0, tTrimmedKey.length() - 5).equals(rTranslation)) {
                        return aKey;
                    }
                } else {
                    rTranslation = StatCollector.translateToLocal(tTrimmedKey + ".name");
                    if (GT_Utility.isStringInvalid(rTranslation) || (tTrimmedKey + ".name").equals(rTranslation)) {
                        return aKey;
                    }
                }
            }
        }
        return rTranslation;
    }

    public static String getTranslation(String aKey, String aSeperator) {
        if (aKey == null) return E;
        String rTranslation = E;
        StringBuilder rTranslationSB = new StringBuilder(rTranslation);
        for (String tString : aKey.split(aSeperator)) {
            rTranslationSB.append(getTranslation(tString));
        }
        rTranslation = String.valueOf(rTranslationSB);
        return rTranslation;
    }

    public static String getTranslateableItemStackName(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return "null";
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT != null && tNBT.hasKey("display")) {
            String tName = tNBT.getCompoundTag("display").getString("Name");
            if (GT_Utility.isStringValid(tName)) {
                return tName;
            }
        }
        return aStack.getUnlocalizedName() + ".name";
    }
    
    public static void writePlaceholderStrings(){
    	addStringLocalization("Interaction_DESCRIPTION_Index_001", "Puts out into adjacent Slot #");
    	addStringLocalization("Interaction_DESCRIPTION_Index_002", "Grabs in for own Slot #");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_003", "Normal");
    	addStringLocalization("Interaction_DESCRIPTION_Index_004", "Inverted");
    	addStringLocalization("Interaction_DESCRIPTION_Index_005", "No Work at all");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_006", "Export");
    	addStringLocalization("Interaction_DESCRIPTION_Index_007", "Import");
    	addStringLocalization("Interaction_DESCRIPTION_Index_008", "Export (conditional)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_009", "Import (conditional)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_010", "Export (invert cond)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_011", "Import (invert cond)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_012", "Export allow Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_013", "Import allow Output");
    	addStringLocalization("Interaction_DESCRIPTION_Index_014", "Export allow Input (conditional)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_015", "Import allow Output (conditional)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_016", "Export allow Input (invert cond)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_017", "Import allow Output (invert cond)");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_018", "Normal");
    	addStringLocalization("Interaction_DESCRIPTION_Index_019", "Inverted");
    	addStringLocalization("Interaction_DESCRIPTION_Index_020", "Ready to work");
    	addStringLocalization("Interaction_DESCRIPTION_Index_021", "Not ready to work");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_022", "Import");
    	addStringLocalization("Interaction_DESCRIPTION_Index_023", "Import (conditional)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_024", "Import (invert cond)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_025", "Keep Liquids Away");
    	addStringLocalization("Interaction_DESCRIPTION_Index_026", "Keep Liquids Away (conditional)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_027", "Keep Liquids Away (invert cond)");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_028", "Allow");
    	addStringLocalization("Interaction_DESCRIPTION_Index_029", "Allow (conditional)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_030", "Disallow (conditional)");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_031", "Normal Universal Storage");
    	addStringLocalization("Interaction_DESCRIPTION_Index_032", "Inverted Universal Storage");
    	addStringLocalization("Interaction_DESCRIPTION_Index_033", "Normal Electricity Storage");
    	addStringLocalization("Interaction_DESCRIPTION_Index_034", "Inverted Electricity Storage");
    	addStringLocalization("Interaction_DESCRIPTION_Index_035", "Normal Steam Storage");
    	addStringLocalization("Interaction_DESCRIPTION_Index_036", "Inverted Steam Storage");
    	addStringLocalization("Interaction_DESCRIPTION_Index_037", "Normal Average Electric Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_038", "Inverted Average Electric Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_039", "Normal Average Electric Output");
    	addStringLocalization("Interaction_DESCRIPTION_Index_040", "Inverted Average Electric Output");
    	addStringLocalization("Interaction_DESCRIPTION_Index_041", "Normal Electricity Storage(Including Batteries)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_042", "Inverted Electricity Storage(Including Batteries)");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_043", "Allow input, no output");
    	addStringLocalization("Interaction_DESCRIPTION_Index_044", "Deny input, no output");
    	addStringLocalization("Interaction_DESCRIPTION_Index_045", "Allow input, permit any output");
    	addStringLocalization("Interaction_DESCRIPTION_Index_046", "Deny input, permit any output");
    	addStringLocalization("Interaction_DESCRIPTION_Index_047", "Filter Fluid: ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_048", "Pump speed: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_049", "L/tick ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_050", "L/sec");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_051", "Normal");
    	addStringLocalization("Interaction_DESCRIPTION_Index_052", "Inverted");
    	addStringLocalization("Interaction_DESCRIPTION_Index_053", "Slot: ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_054", "Inverted");
    	addStringLocalization("Interaction_DESCRIPTION_Index_055", "Normal");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_056", "Emit if 1 Maintenance Needed");
    	addStringLocalization("Interaction_DESCRIPTION_Index_057", "Emit if 1 Maintenance Needed(inverted)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_058", "Emit if 2 Maintenance Needed");
    	addStringLocalization("Interaction_DESCRIPTION_Index_059", "Emit if 2 Maintenance Needed(inverted)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_060", "Emit if 3 Maintenance Needed");
    	addStringLocalization("Interaction_DESCRIPTION_Index_061", "Emit if 3 Maintenance Needed(inverted)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_062", "Emit if 4 Maintenance Needed");
    	addStringLocalization("Interaction_DESCRIPTION_Index_063", "Emit if 4 Maintenance Needed(inverted)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_064", "Emit if 5 Maintenance Needed");
    	addStringLocalization("Interaction_DESCRIPTION_Index_065", "Emit if 5 Maintenance Needed(inverted)");
    	addStringLocalization("Interaction_DESCRIPTION_Index_066", "Emit if rotor needs maintenance");
    	addStringLocalization("Interaction_DESCRIPTION_Index_067", "Emit if rotor needs maintenance(inverted)");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_068", "Emit if any Player is close");
    	addStringLocalization("Interaction_DESCRIPTION_Index_069", "Emit if other player is close");
    	addStringLocalization("Interaction_DESCRIPTION_Index_070", "Emit if you are close");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_071", "Conducts strongest Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_072", "Conducts from bottom Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_073", "Conducts from top Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_074", "Conducts from north Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_075", "Conducts from south Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_076", "Conducts from west Input");
    	addStringLocalization("Interaction_DESCRIPTION_Index_077", "Conducts from east Input");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_078", "Signal = ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_079", "Conditional Signal = ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_080", "Inverted Conditional Signal = ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_081", "Frequency: ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_082", "Open if work enabled");
    	addStringLocalization("Interaction_DESCRIPTION_Index_083", "Open if work disabled");
    	addStringLocalization("Interaction_DESCRIPTION_Index_084", "Only Output allowed");
    	addStringLocalization("Interaction_DESCRIPTION_Index_085", "Only Input allowed");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_086", "Auto-Input: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_087", "Disabled");
    	addStringLocalization("Interaction_DESCRIPTION_Index_088", "Enabled");
    	addStringLocalization("Interaction_DESCRIPTION_Index_089", "  Auto-Output: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_090", "Machine Processing: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_091", "Redstone Output at Side ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_092", " set to: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_093", "Strong");
    	addStringLocalization("Interaction_DESCRIPTION_Index_094", "Weak");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_095", "Input from Output Side allowed");
    	addStringLocalization("Interaction_DESCRIPTION_Index_096", "Input from Output Side forbidden");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_097", "It's dangerous to go alone! Take this.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_098", "Do not regulate Item Stack Size");
    	addStringLocalization("Interaction_DESCRIPTION_Index_099", "Regulate Item Stack Size to: ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_100", "This is ");//Spartaaaaaaa!!!
    	addStringLocalization("Interaction_DESCRIPTION_Index_101", " Ore.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_102", "There is Lava behind this Rock.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_103", "There is a Liquid behind this Rock.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_104", "There is an Air Pocket behind this Rock.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_105", "Material is changing behind this Rock.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_106", "Found traces of ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_107", "No Ores found.");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_108", "Outputs Liquids, Steam and Items");
    	addStringLocalization("Interaction_DESCRIPTION_Index_109", "Outputs Steam and Items");
    	addStringLocalization("Interaction_DESCRIPTION_Index_110", "Outputs Steam and Liquids");
    	addStringLocalization("Interaction_DESCRIPTION_Index_111", "Outputs Steam");
    	addStringLocalization("Interaction_DESCRIPTION_Index_112", "Outputs Liquids and Items");
    	addStringLocalization("Interaction_DESCRIPTION_Index_113", "Outputs only Items");
    	addStringLocalization("Interaction_DESCRIPTION_Index_114", "Outputs only Liquids");
    	addStringLocalization("Interaction_DESCRIPTION_Index_115", "Outputs nothing");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_116", "Emit Energy to Outputside");
    	addStringLocalization("Interaction_DESCRIPTION_Index_117", "Don't emit Energy");
    	addStringLocalization("Interaction_DESCRIPTION_Index_118", "Emit Redstone if no Slot is free");
    	addStringLocalization("Interaction_DESCRIPTION_Index_119", "Don't emit Redstone");
    	addStringLocalization("Interaction_DESCRIPTION_Index_120", "Invert Redstone");
    	addStringLocalization("Interaction_DESCRIPTION_Index_121", "Don't invert Redstone");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_122", "Emit Redstone if slots contain something");
    	addStringLocalization("Interaction_DESCRIPTION_Index_123", "Don't emit Redstone");
    	addStringLocalization("Interaction_DESCRIPTION_Index_124", "Invert Filter");
    	addStringLocalization("Interaction_DESCRIPTION_Index_125", "Don't invert Filter");
    	addStringLocalization("Interaction_DESCRIPTION_Index_126", "Ignore NBT");
    	addStringLocalization("Interaction_DESCRIPTION_Index_127", "NBT has to match");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_128", "Redstone ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_129", "Energy ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_130", "Fluids ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_131", "Items ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_132", "Pipe is loose.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_133", "Screws are missing.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_134", "Something is stuck.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_135", "Platings are dented.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_136", "Circuitry burned out.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_137", "That doesn't belong there.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_138", "Incomplete Structure.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_139", "Hit with Soft Hammer");
    	addStringLocalization("Interaction_DESCRIPTION_Index_140", "to (re-)start the Machine");
    	addStringLocalization("Interaction_DESCRIPTION_Index_141", "if it doesn't start.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_142", "Running perfectly.");
    	addStringLocalization("Interaction_DESCRIPTION_Index_143", "Missing Mining Pipe");
    	addStringLocalization("Interaction_DESCRIPTION_Index_144", "Missing Turbine Rotor");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_145", "Step Down, In: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_146", "Step Up, In: ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_147", "Amp, Out: ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_148", " V at ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_149", " Amp");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_150", "Chance: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_151", "Does not get consumed in the process");
    	addStringLocalization("Interaction_DESCRIPTION_Index_152", "Total: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_153", "Usage: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_154", "Voltage: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_155", "Amperage: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_156", "Voltage: unspecified");
    	addStringLocalization("Interaction_DESCRIPTION_Index_157", "Amperage: unspecified");
    	addStringLocalization("Interaction_DESCRIPTION_Index_158", "Time: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_159", "Needs Low Gravity");
    	addStringLocalization("Interaction_DESCRIPTION_Index_160", "Needs Cleanroom");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_161", " secs");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_162", "Name: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_163", "  MetaData: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_164", "Hardness: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_165", "  Blast Resistance: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_166", "Is valid Beacon Pyramid Material");
    	addStringLocalization("Interaction_DESCRIPTION_Index_167", "Tank ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_168", "Heat: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_169", "  HEM: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_170", "  Base EU Output: ");    	
    	addStringLocalization("Interaction_DESCRIPTION_Index_171", "Facing: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_172", " / Chance: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_173", "You can remove this with a Wrench");
    	addStringLocalization("Interaction_DESCRIPTION_Index_174", "You can NOT remove this with a Wrench");
    	addStringLocalization("Interaction_DESCRIPTION_Index_175", "Conduction Loss: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_176", "Contained Energy: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_177", "Has Muffler Upgrade");
    	addStringLocalization("Interaction_DESCRIPTION_Index_178", "Progress: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_179", "Max IN: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_180", " EU");
    	addStringLocalization("Interaction_DESCRIPTION_Index_181", "Max OUT: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_182", " EU at ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_183", " Amperes");
    	addStringLocalization("Interaction_DESCRIPTION_Index_184", "Energy: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_185", "EU");
    	addStringLocalization("Interaction_DESCRIPTION_Index_186", "Owned by: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_187", "Type -- Crop-Name: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_188", "  Growth: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_189", "  Gain: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_190", "  Resistance: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_191", "Plant -- Fertilizer: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_192", "  Water: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_193", "  Weed-Ex: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_194", "  Scan-Level: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_195", "Environment -- Nutrients: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_196", "  Humidity: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_197", "  Air-Quality: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_198", "Attributes:");
    	addStringLocalization("Interaction_DESCRIPTION_Index_199", "Discovered by: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_200", " L");
    	addStringLocalization("Interaction_DESCRIPTION_Index_201", "Nothing");
    	addStringLocalization("Interaction_DESCRIPTION_Index_202", "Pollution in Chunk: ");
    	addStringLocalization("Interaction_DESCRIPTION_Index_203", " gibbl");
    	addStringLocalization("Interaction_DESCRIPTION_Index_204", "No Pollution in Chunk! HAYO!");
    	addStringLocalization("Interaction_DESCRIPTION_Index_205", " of ");
//    	addStringLocalization("Interaction_DESCRIPTION_Index_206", "Grab");
//    	addStringLocalization("Interaction_DESCRIPTION_Index_207", "Grab");
//    	addStringLocalization("Interaction_DESCRIPTION_Index_208", "Grab");
//    	addStringLocalization("Interaction_DESCRIPTION_Index_209", "Grab");
//    	addStringLocalization("Interaction_DESCRIPTION_Index_210", "Grab");
        addStringLocalization("Interaction_DESCRIPTION_Index_211", "Items per side: ");
        addStringLocalization("Interaction_DESCRIPTION_Index_212", "Input enabled");
        addStringLocalization("Interaction_DESCRIPTION_Index_213", "Input disabled");
        addStringLocalization("Interaction_DESCRIPTION_Index_214", "Connected");
        addStringLocalization("Interaction_DESCRIPTION_Index_215", "Disconnected");
        addStringLocalization("Interaction_DESCRIPTION_Index_216", "Deprecated Recipe");
        addStringLocalization("Item_DESCRIPTION_Index_000", "Stored Heat: %s");
        addStringLocalization("Item_DESCRIPTION_Index_001", "Durability: %s/%s");
        addStringLocalization("Item_DESCRIPTION_Index_002", "%s lvl %s");
        addStringLocalization("Item_DESCRIPTION_Index_003", "Attack Damage: %s");
        addStringLocalization("Item_DESCRIPTION_Index_004", "Mining Speed: %s");
        addStringLocalization("Item_DESCRIPTION_Index_005", "Turbine Efficiency: %s");
        addStringLocalization("Item_DESCRIPTION_Index_006", "Optimal Steam flow: %sL/sec");
        addStringLocalization("Item_DESCRIPTION_Index_007", "Optimal Gas flow(EU burnvalue per tick): %sEU/t");
        addStringLocalization("Item_DESCRIPTION_Index_008", "Optimal Plasma flow(Plasma energyvalue per tick): %sEU/t");
        addStringLocalization("Item_DESCRIPTION_Index_009", "Contains %s EU   Tier: %s");
        addStringLocalization("Item_DESCRIPTION_Index_010", "Empty. You should recycle it properly.");
        addStringLocalization("Item_DESCRIPTION_Index_011", "%s / %s EU - Voltage: %s");
        addStringLocalization("Item_DESCRIPTION_Index_012", "No Fluids Contained");
        addStringLocalization("Item_DESCRIPTION_Index_013", "%sL / %sL");
        addStringLocalization("Item_DESCRIPTION_Index_014", "Missing Coodinates!");
        addStringLocalization("Item_DESCRIPTION_Index_015", "Device at:");
        addStringLocalization("Item_DESCRIPTION_Index_016", "Amount: %s L");
        addStringLocalization("Item_DESCRIPTION_Index_017", "Temperature: %s K");
        addStringLocalization("Item_DESCRIPTION_Index_018", "State: %s");
    }
    
}
