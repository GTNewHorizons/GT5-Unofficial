package gtPlusPlus.core.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.GT_Version;
import gregtech.api.objects.XSTR;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractTerminal;

public class GTPPCore {

    // Math Related
    public static final float PI = (float) Math.PI;
    public static volatile Random RANDOM = new XSTR();

    public static boolean DEVENV = false;

    // Mod Variables
    public static final String name = "GT++";
    public static final String VERSION = GT_Version.VERSION;

    // Tooltips;
    public static final Supplier<String> GT_Tooltip = () -> StatCollector.translateToLocal("GTPP.core.GT_Tooltip");
    public static final Supplier<String> GT_Tooltip_Builder = () -> StatCollector
        .translateToLocal("GTPP.core.GT_Tooltip_Builder");
    public static final Supplier<String> GT_Tooltip_Radioactive = () -> StatCollector
        .translateToLocal("GTPP.core.GT_Tooltip_Radioactive");

    /**
     * Lists/Maps
     */

    // Burnables List
    public static List<Pair<Integer, ItemStack>> burnables = new ArrayList<>();

    // TesseractMaps
    public static final Map<UUID, Map<Integer, MTETesseractGenerator>> sTesseractGeneratorOwnershipMap = new HashMap<>();
    public static final Map<UUID, Map<Integer, MTETesseractTerminal>> sTesseractTerminalOwnershipMap = new HashMap<>();

    // BookMap
    public static final Map<String, ItemStack> sBookList = new ConcurrentHashMap<>();

    public static final GT_Materials[] sMU_GeneratedMaterials = new GT_Materials[1000];

    public static void crash() {
        crash("Generic Crash");
    }

    public static void crash(String aReason) {
        try {
            Logger.INFO("==========================================================");
            Logger.INFO("[GT++ CRASH]");
            Logger.INFO("==========================================================");
            Logger.INFO("Oooops...");
            Logger.INFO("This should only happen in a development environment or when something really bad happens.");
            Logger.INFO("Reason: " + aReason);
            Logger.INFO("==========================================================");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        FMLCommonHandler.instance()
            .exitJava(0, true);
    }
}
