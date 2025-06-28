package gregtech.api.enums;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.util.GTUtil;

public enum Dyes implements IColorModulationContainer {

    // spotless:off
    /** The valid Colors, see VALUES Array below */
    dyeBlack(         0, 0x20202000,            "Black", EnumChatFormatting.BLACK),
    dyeRed(           1, 0xff000000,              "Red", EnumChatFormatting.RED),
    dyeGreen(         2, 0x00ff0000,            "Green", EnumChatFormatting.DARK_GREEN),
    dyeBrown(         3, 0x60400000,            "Brown", EnumChatFormatting.GOLD),
    dyeBlue(          4, 0x0020ff00,             "Blue", EnumChatFormatting.DARK_BLUE),
    dyePurple(        5, 0x80008000,           "Purple", EnumChatFormatting.DARK_PURPLE),
    dyeCyan(          6, 0x00ffff00,             "Cyan", EnumChatFormatting.DARK_AQUA),
    dyeLightGray(     7, 0xc0c0c000,       "Light Gray", EnumChatFormatting.GRAY),
    dyeGray(          8, 0x80808000,             "Gray", EnumChatFormatting.DARK_GRAY),
    dyePink(          9, 0xffc0c000,             "Pink", EnumChatFormatting.LIGHT_PURPLE),
    dyeLime(         10, 0x80ff8000,             "Lime", EnumChatFormatting.GREEN),
    dyeYellow(       11, 0xffff0000,           "Yellow", EnumChatFormatting.YELLOW),
    dyeLightBlue(    12, 0x6080ff00,       "Light Blue", EnumChatFormatting.AQUA),
    dyeMagenta(      13, 0xff00ff00,          "Magenta", EnumChatFormatting.LIGHT_PURPLE),
    dyeOrange(       14, 0xff800000,           "Orange", EnumChatFormatting.GOLD),
    dyeWhite(        15, 0xffffff00,            "White", EnumChatFormatting.WHITE),
    // Additional Colors only used for direct Color referencing
    _NULL(           -1, 0xffffff00,    "INVALID COLOR"),
    CABLE_INSULATION(-1, 0x40404000, "Cable Insulation"),
    MACHINE_METAL(   -1, 0xd2dcff00,    "Machine Metal");
    // spotless:on

    public static final Dyes[] VALUES = { dyeBlack, dyeRed, dyeGreen, dyeBrown, dyeBlue, dyePurple, dyeCyan,
        dyeLightGray, dyeGray, dyePink, dyeLime, dyeYellow, dyeLightBlue, dyeMagenta, dyeOrange, dyeWhite };

    public final int rgba;
    public final int mIndex;
    public final String mName;
    public final EnumChatFormatting formatting;
    private final Set<Fluid> fluidDyes = new HashSet<>();

    Dyes(int index, int rgba, @NotNull String name) {
        this(index, rgba, name, EnumChatFormatting.GRAY);
    }

    Dyes(int index, int rgba, @NotNull String name, @NotNull EnumChatFormatting formatting) {
        this.rgba = rgba;
        this.mIndex = index;
        this.mName = name;
        this.formatting = formatting;
    }

    public static Dyes get(int index) {
        if (isDyeIndex(index)) return VALUES[index];
        return _NULL;
    }

    public static Dyes get(@NotNull String color) {
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
            case "Cable Insulation" -> Dyes.CABLE_INSULATION;
            case "Machine Metal"    -> Dyes.MACHINE_METAL;
            default                 -> Dyes._NULL;
        };
        // spotless:on
    }

    public static boolean isDyeIndex(int index) {
        return 0 <= index && index <= 15;
    }

    public static byte rgbaToRed(int rgba) {
        return (byte) ((rgba >> 24) & 0xff);
    }

    public static byte rgbaToGreen(int rgba) {
        return (byte) ((rgba >> 16) & 0xff);
    }

    public static byte rgbaToBlue(int rgba) {
        return (byte) ((rgba >> 8) & 0xff);
    }

    public static byte rgbaToAlpha(int rgba) {
        return (byte) (rgba & 0xff);
    }

    public static short[] rgbaToArray(int rgba) {
        final short r = (short) ((rgba >> 24) & 0xff);
        final short g = (short) ((rgba >> 16) & 0xff);
        final short b = (short) ((rgba >> 8) & 0xff);
        final short a = (short) (rgba & 0xff);
        return new short[] { r, g, b, a };
    }

    public static short[] getModulation(int index, short @NotNull [] defaultModulation) {
        if (isDyeIndex(index)) return Dyes.rgbaToArray(VALUES[index].rgba);
        return defaultModulation;
    }

    public static boolean isAnyFluidDye(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) return false;
        return isAnyFluidDye(fluidStack.getFluid());
    }

    public static boolean isAnyFluidDye(@Nullable Fluid fluid) {
        if (fluid == null) return false;
        final int values = VALUES.length;
        for (Dyes value : VALUES) {
            if (value.isFluidDye(fluid)) return true;
        }
        return false;
    }

    public static @Nullable Dyes getAnyFluidDye(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) return null;
        return getAnyFluidDye(fluidStack.getFluid());
    }

    public static @Nullable Dyes getAnyFluidDye(@Nullable Fluid fluid) {
        if (fluid == null) return null;
        final int values = VALUES.length;
        for (final Dyes dye : VALUES) {
            if (dye.isFluidDye(fluid)) return dye;
        }
        return null;
    }

    public boolean isFluidDye(@Nullable FluidStack fluidStack) {
        if (fluidStack == null) return false;
        return isFluidDye(fluidStack.getFluid());
    }

    public boolean isFluidDye(@Nullable Fluid fluid) {
        if (fluid == null) return false;
        return fluidDyes.contains(fluid);
    }

    public void addFluidDye(@Nullable Fluid fluid) {
        if (fluid == null) return;
        fluidDyes.add(fluid);
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

    public Iterator<Fluid> getFluidDyes() {
        return fluidDyes.iterator();
    }

    @Override
    @Deprecated
    public short[] getRGBA() {
        return Dyes.rgbaToArray(rgba);
    }

    @Deprecated
    public int toInt() {
        return GTUtil.getRGBInt(getRGBA());
    }

    @Deprecated
    public static Dyes getDyeFromIndex(short index) {
        return index != -1 ? Dyes.get(index) : Dyes.MACHINE_METAL;
    }

    /**
     * Transforms a dye index between the GT index for this color and the vanilla index for this color.
     *
     * @param color an integer between 0 and 15
     * @return the transformed color
     */
    @Contract(pure = true)
    public static int transformDyeIndex(final int color) {
        if (color < 0 || color > 15) {
            throw new IllegalArgumentException("Color passed to transformColor must be between 0 and 15");
        }

        return (~(byte) color) & 0xF;
    }

    public String getLocalizedDyeName() {
        return StatCollector.translateToLocal("GT5U.infinite_spray_can.color." + this.mName);
    }
}
