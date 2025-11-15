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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.GT_Version;
import gregtech.api.objects.XSTR;
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

    // Alkalus
    public static final String AUTHOR = EnumChatFormatting.DARK_GREEN + "Alkalus";

    // Tooltips;
    public static final Supplier<String> GT_Tooltip = () -> StatCollector
        .translateToLocalFormatted("GTPP.core.GT_Tooltip", AUTHOR);
    public static final Supplier<String> GT_Tooltip_Builder = () -> StatCollector
        .translateToLocalFormatted("GTPP.core.GT_Tooltip_Builder", AUTHOR);
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
}
