package gregtech.api.enums;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.common.config.Client;

public enum Dyes implements IColorModulationContainer {

    // spotless:off
    // The valid colors, see `VALUES` array below.
    dyeBlack(         0,   0x20202000,            "Black", EnumChatFormatting.BLACK),
    dyeRed(           1,   0xff000000,              "Red", EnumChatFormatting.RED),
    dyeGreen(         2,   0x00ff0000,            "Green", EnumChatFormatting.DARK_GREEN),
    dyeBrown(         3,   0x60400000,            "Brown", EnumChatFormatting.GOLD),
    dyeBlue(          4,   0x0020ff00,             "Blue", EnumChatFormatting.DARK_BLUE),
    dyePurple(        5,   0x80008000,           "Purple", EnumChatFormatting.DARK_PURPLE),
    dyeCyan(          6,   0x00ffff00,             "Cyan", EnumChatFormatting.DARK_AQUA),
    dyeLightGray(     7,   0xc0c0c000,       "Light Gray", EnumChatFormatting.GRAY),
    dyeGray(          8,   0x80808000,             "Gray", EnumChatFormatting.DARK_GRAY),
    dyePink(          9,   0xffc0c000,             "Pink", EnumChatFormatting.LIGHT_PURPLE),
    dyeLime(         10,   0x80ff8000,             "Lime", EnumChatFormatting.GREEN),
    dyeYellow(       11,   0xffff0000,           "Yellow", EnumChatFormatting.YELLOW),
    dyeLightBlue(    12,   0x6080ff00,       "Light Blue", EnumChatFormatting.AQUA),
    dyeMagenta(      13,   0xff00ff00,          "Magenta", EnumChatFormatting.LIGHT_PURPLE),
    dyeOrange(       14,   0xff800000,           "Orange", EnumChatFormatting.GOLD),
    dyeWhite(        15,   0xffffff00,            "White", EnumChatFormatting.WHITE),
    // Additional Colors only used for direct Color referencing
    _NULL(           -1,   0xffffff00,    "INVALID COLOR"),
    CABLE_INSULATION(-2, cableInsulation(), "Cable Insulation"),
    MACHINE_METAL(   -3,    machineMetal(),    "Machine Metal");
    // spotless:on

    /** Constructs the configured cable insulation color. */
    private static int cableInsulation() {
        final Client.ColorModulation.CableInsulation insulation = Client.colorModulation.cableInsulation;
        return ((insulation.red & 0xFF) << 24) | ((insulation.green & 0xFF) << 16) | ((insulation.blue & 0xFF) << 8);
    }

    /** Constructs the configured machine metal color. */
    private static int machineMetal() {
        final Client.ColorModulation.MachineMetal metal = Client.colorModulation.machineMetal;
        return ((metal.red & 0xFF) << 24) | ((metal.green & 0xFF) << 16) | ((metal.blue & 0xFF) << 8);
    }

    /** RGBA color value (0xrrggbbaa). */
    public final int rgba;
    @Deprecated
    private final short[] rgbaArray;
    /** Dye index in range 0–15, or -1 for special cases. */
    public final int mIndex;
    /** Localized dye name. */
    public final String mName;
    /** Text formatting color. */
    public final EnumChatFormatting formatting;
    /** Set of all fluids that can be converted into this specific dye. */
    private final HashSet<Fluid> fluidDyesSet = new HashSet<>();

    /** Global mapping of fluids to the dye they produce when processed. */
    private static final HashMap<Fluid, Dyes> fluidDyesMap = new HashMap<>();
    /** Valid dye colors, indexed 0-15. */
    public static final Dyes[] VALUES = { dyeBlack, dyeRed, dyeGreen, dyeBrown, dyeBlue, dyePurple, dyeCyan,
        dyeLightGray, dyeGray, dyePink, dyeLime, dyeYellow, dyeLightBlue, dyeMagenta, dyeOrange, dyeWhite };

    Dyes(int index, int rgba, @NotNull String name) {
        this(index, rgba, name, EnumChatFormatting.GRAY);
    }

    Dyes(int index, int rgba, @NotNull String name, @NotNull EnumChatFormatting formatting) {
        this.rgba = rgba;
        this.rgbaArray = new short[] { rgbaToRed(rgba), rgbaToGreen(rgba), rgbaToBlue(rgba), rgbaToAlpha(rgba) };
        this.mIndex = index;
        this.mName = name;
        this.formatting = formatting;
    }

    public static @NotNull Dyes get(int index) {
        return getOrDefault(index, Dyes._NULL);
    }

    public static @NotNull Dyes getOrDefault(int index, @NotNull Dyes defaultDye) {
        if (isDyeIndex(index)) return VALUES[index];
        return defaultDye;
    }

    public static @NotNull Dyes get(@NotNull String color) {
        return getOrDefault(color, Dyes._NULL);
    }

    public static @NotNull Dyes getOrDefault(@NotNull String color, @NotNull Dyes defaultDye) {
        // spotless:off
        return switch (color) {
            case "Black"            -> Dyes.dyeBlack;
            case "Red"              -> Dyes.dyeRed;
            case "Green"            -> Dyes.dyeGreen;
            case "Brown"            -> Dyes.dyeBrown;
            case "Blue"             -> Dyes.dyeBlue;
            case "Purple"           -> Dyes.dyePurple;
            case "Cyan"             -> Dyes.dyeCyan;
            case "Light Gray"       -> Dyes.dyeLightGray;
            case "Gray"             -> Dyes.dyeGray;
            case "Pink"             -> Dyes.dyePink;
            case "Lime"             -> Dyes.dyeLime;
            case "Yellow"           -> Dyes.dyeYellow;
            case "Light Blue"       -> Dyes.dyeLightBlue;
            case "Magenta"          -> Dyes.dyeMagenta;
            case "Orange"           -> Dyes.dyeOrange;
            case "White"            -> Dyes.dyeWhite;
            case "INVALID COLOR"    -> Dyes._NULL;
            case "Cable Insulation" -> Dyes.CABLE_INSULATION;
            case "Machine Metal"    -> Dyes.MACHINE_METAL;
            default                 -> defaultDye;
        };
        // spotless:on
    }

    public static boolean isDyeIndex(int index) {
        return 0 <= index && index <= 15;
    }

    /**
     * Transforms a dye index between the GT index for this color and the vanilla index for this color.
     *
     * @param index an integer between 0 and 15
     * @return the transformed index
     */
    public static int transformDyeIndex(int index) {
        if (isDyeIndex(index)) return 15 - index;
        throw new IllegalArgumentException("Index passed to `transformDyeIndex` must be between 0 and 15");
    }

    public static byte rgbaToRed(int rgba) {
        return (byte) ((rgba >>> 24) & 0xff);
    }

    public static byte rgbaToGreen(int rgba) {
        return (byte) ((rgba >>> 16) & 0xff);
    }

    public static byte rgbaToBlue(int rgba) {
        return (byte) ((rgba >>> 8) & 0xff);
    }

    public static byte rgbaToAlpha(int rgba) {
        return (byte) (rgba & 0xff);
    }

    public static short @NotNull [] getModulation(int index) {
        return getModulation(index, Dyes._NULL.getRGBA());
    }

    @Deprecated
    public static short @NotNull [] getModulation(int index, short @NotNull [] defaultModulation) {
        if (isDyeIndex(index)) return VALUES[index].rgbaArray;
        return defaultModulation;
    }

    public static @Nullable Dyes getFluidDye(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) return null;
        return getFluidDye(fluidStack.getFluid());
    }

    public static @Nullable Dyes getFluidDye(@Nullable Fluid fluid) {
        if (fluid == null) return null;
        return fluidDyesMap.get(fluid);
    }

    public static boolean isFluidDye(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) return false;
        return isFluidDye(fluidStack.getFluid());
    }

    public static boolean isFluidDye(@Nullable Fluid fluid) {
        if (fluid == null) return false;
        return fluidDyesMap.containsKey(fluid);
    }

    public void addFluidDye(@Nullable Fluid fluid) {
        if (fluid == null) return;
        fluidDyesSet.add(fluid);
        fluidDyesMap.put(fluid, this);
    }

    public byte getRed() {
        return rgbaToRed(rgba);
    }

    public byte getBlue() {
        return rgbaToBlue(rgba);
    }

    public byte getGreen() {
        return rgbaToGreen(rgba);
    }

    public byte getAlpha() {
        return rgbaToAlpha(rgba);
    }

    public @NotNull HashSet<@NotNull Fluid> getFluidDyes() {
        return fluidDyesSet;
    }

    @Override
    @Deprecated
    public short @NotNull [] getRGBA() {
        return rgbaArray;
    }

    @Deprecated
    public int toInt() {
        return rgba >>> 8;
    }

    public @NotNull String getLocalizedDyeName() {
        return StatCollector.translateToLocal("GT5U.infinite_spray_can.color." + this.mName);
    }
}
