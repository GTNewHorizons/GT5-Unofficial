package gregtech.api.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;

public enum Dyes implements IColorModulationContainer {

    // spotless:off
    // The valid colors, see `VALUES` array below.
    dyeBlack(         0,   0x202020,            "Black",           EnumChatFormatting.BLACK, () -> GTGuiThemes.STANDARD_BLACK),
    dyeRed(           1,   0xff0000,              "Red",             EnumChatFormatting.RED, () -> GTGuiThemes.STANDARD_RED),
    dyeGreen(         2,   0x00ff00,            "Green",      EnumChatFormatting.DARK_GREEN, () -> GTGuiThemes.STANDARD_GREEN),
    dyeBrown(         3,   0x604000,            "Brown",            EnumChatFormatting.GOLD, () -> GTGuiThemes.STANDARD_BROWN),
    dyeBlue(          4,   0x0020ff,             "Blue",       EnumChatFormatting.DARK_BLUE, () -> GTGuiThemes.STANDARD_BLUE),
    dyePurple(        5,   0x800080,           "Purple",     EnumChatFormatting.DARK_PURPLE, () -> GTGuiThemes.STANDARD_PURPLE),
    dyeCyan(          6,   0x00ffff,             "Cyan",       EnumChatFormatting.DARK_AQUA, () -> GTGuiThemes.STANDARD_CYAN),
    dyeLightGray(     7,   0xc0c0c0,       "Light Gray",            EnumChatFormatting.GRAY, () -> GTGuiThemes.STANDARD_LIGHT_GRAY),
    dyeGray(          8,   0x808080,             "Gray",       EnumChatFormatting.DARK_GRAY, () -> GTGuiThemes.STANDARD_GRAY),
    dyePink(          9,   0xffc0c0,             "Pink",    EnumChatFormatting.LIGHT_PURPLE, () -> GTGuiThemes.STANDARD_PINK),
    dyeLime(         10,   0x80ff80,             "Lime",           EnumChatFormatting.GREEN, () -> GTGuiThemes.STANDARD_LIME),
    dyeYellow(       11,   0xffff00,           "Yellow",          EnumChatFormatting.YELLOW, () -> GTGuiThemes.STANDARD_YELLOW),
    dyeLightBlue(    12,   0x6080ff,       "Light Blue",            EnumChatFormatting.AQUA, () -> GTGuiThemes.STANDARD_LIGHT_BLUE),
    dyeMagenta(      13,   0xff00ff,          "Magenta",    EnumChatFormatting.LIGHT_PURPLE, () -> GTGuiThemes.STANDARD_MAGENTA),
    dyeOrange(       14,   0xff8000,           "Orange",            EnumChatFormatting.GOLD, () -> GTGuiThemes.STANDARD_ORANGE),
    dyeWhite(        15,   0xffffff,            "White",           EnumChatFormatting.WHITE, () -> GTGuiThemes.STANDARD_WHITE),
    // Additional Colors only used for direct Color referencing
    _NULL(           -1,   0xffffff,    "INVALID COLOR"),
    CABLE_INSULATION(-2, cableInsulation(), "Cable Insulation"),
    MACHINE_METAL(   -3,    machineMetal(),    "Machine Metal"),
    GUI_METAL(   -4,    guiMetal(),    "Gui Metal");
    // spotless:on

    /** Constructs the configured cable insulation color. */
    private static int cableInsulation() {
        final Client.ColorModulation.CableInsulation insulation = Client.colorModulation.cableInsulation;
        final int r = GTUtility.clamp(insulation.red, 0, 255);
        final int g = GTUtility.clamp(insulation.green, 0, 255);
        final int b = GTUtility.clamp(insulation.blue, 0, 255);
        return (r << 16) | (g << 8) | b;
    }

    /** Constructs the configured machine metal color. */
    private static int machineMetal() {
        final Client.ColorModulation.MachineMetal metal = Client.colorModulation.machineMetal;
        final int r = GTUtility.clamp(metal.red, 0, 255);
        final int g = GTUtility.clamp(metal.green, 0, 255);
        final int b = GTUtility.clamp(metal.blue, 0, 255);
        return (r << 16) | (g << 8) | b;
    }

    /** Constructs the configured gui metal color. */
    private static int guiMetal() {
        final Client.ColorModulation.GuiMetal metal = Client.colorModulation.guiMetal;
        final int r = GTUtility.clamp(metal.red, 0, 255);
        final int g = GTUtility.clamp(metal.green, 0, 255);
        final int b = GTUtility.clamp(metal.blue, 0, 255);
        return (r << 16) | (g << 8) | b;
    }

    /** RGB color value (0xrrggbb). */
    public final int rgb;
    @Deprecated
    public final short[] mRGBa;
    /** Dye index in range 0–15, or -1 for special cases. */
    public final int mIndex;
    /** Localized dye name. */
    public final String mName;
    /** Text formatting color. */
    public final EnumChatFormatting formatting;
    /** Set of all fluids that can be converted into this specific dye. */
    private final HashSet<Fluid> fluidDyesSet = new HashSet<>();
    public final Supplier<GTGuiTheme> mui2Theme;

    /** Global mapping of fluids to the dye they produce when processed. */
    private static final Map<Fluid, Dyes> fluidDyesMap = new HashMap<>();
    private static final Map<String, Dyes> dyesFromName = new HashMap<>();
    /** Valid dye colors, indexed 0-15. */
    public static final Dyes[] VALUES = { dyeBlack, dyeRed, dyeGreen, dyeBrown, dyeBlue, dyePurple, dyeCyan,
        dyeLightGray, dyeGray, dyePink, dyeLime, dyeYellow, dyeLightBlue, dyeMagenta, dyeOrange, dyeWhite };

    static {
        for (Dyes dye : Dyes.values()) {
            dyesFromName.put(dye.name(), dye);
            dyesFromName.put(dye.mName, dye);
        }
    }

    Dyes(int index, int rgb, @NotNull String name) {
        this(index, rgb, name, EnumChatFormatting.GRAY, () -> GTGuiThemes.STANDARD);
    }

    Dyes(int index, int rgb, @NotNull String name, @NotNull EnumChatFormatting formatting,
        Supplier<GTGuiTheme> mui2Theme) {
        this.rgb = rgb;
        final short r = (short) ((rgb >>> 16) & 0xff);
        final short g = (short) ((rgb >>> 8) & 0xff);
        final short b = (short) (rgb & 0xff);
        final short a = 0;
        this.mRGBa = new short[] { r, g, b, a };
        this.mIndex = index;
        this.mName = name;
        this.formatting = formatting;
        this.mui2Theme = mui2Theme;
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
        return dyesFromName.getOrDefault(color, defaultDye);
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

    @Deprecated
    public static short @NotNull [] getModulation(int index) {
        return getModulation(index, Dyes._NULL.getRGBA());
    }

    @Deprecated
    public static short @NotNull [] getModulation(int index, short @NotNull [] defaultModulation) {
        if (isDyeIndex(index)) return VALUES[index].mRGBa;
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

    public @NotNull HashSet<@NotNull Fluid> getFluidDyes() {
        return fluidDyesSet;
    }

    @Override
    @Deprecated
    public short @NotNull [] getRGBA() {
        return mRGBa;
    }

    public int toInt() {
        return rgb;
    }

    public @NotNull String getLocalizedDyeName() {
        return StatCollector.translateToLocal("GT5U.infinite_spray_can.color." + this.mName);
    }
}
