package gregtech.api.enums;

import java.util.ArrayList;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.objects.GT_ArrayList;
import gregtech.api.util.GT_Utility;

public enum Dyes implements IColorModulationContainer {

    /**
     * The valid Colors, see VALUES Array below
     */
    dyeBlack(0, 32, 32, 32, "Black", EnumChatFormatting.BLACK),
    dyeRed(1, 255, 0, 0, "Red", EnumChatFormatting.RED),
    dyeGreen(2, 0, 255, 0, "Green", EnumChatFormatting.DARK_GREEN),
    dyeBrown(3, 96, 64, 0, "Brown", EnumChatFormatting.GOLD),
    dyeBlue(4, 0, 32, 255, "Blue", EnumChatFormatting.DARK_BLUE),
    dyePurple(5, 128, 0, 128, "Purple", EnumChatFormatting.DARK_PURPLE),
    dyeCyan(6, 0, 255, 255, "Cyan", EnumChatFormatting.DARK_AQUA),
    dyeLightGray(7, 192, 192, 192, "Light Gray", EnumChatFormatting.GRAY),
    dyeGray(8, 128, 128, 128, "Gray", EnumChatFormatting.DARK_GRAY),
    dyePink(9, 255, 192, 192, "Pink", EnumChatFormatting.LIGHT_PURPLE),
    dyeLime(10, 128, 255, 128, "Lime", EnumChatFormatting.GREEN),
    dyeYellow(11, 255, 255, 0, "Yellow", EnumChatFormatting.YELLOW),
    dyeLightBlue(12, 96, 128, 255, "Light Blue", EnumChatFormatting.AQUA),
    dyeMagenta(13, 255, 0, 255, "Magenta", EnumChatFormatting.LIGHT_PURPLE),
    dyeOrange(14, 255, 128, 0, "Orange", EnumChatFormatting.GOLD),
    dyeWhite(15, 255, 255, 255, "White", EnumChatFormatting.WHITE),
    /**
     * The NULL Color
     */
    _NULL(-1, 255, 255, 255, "INVALID COLOR"),
    /**
     * Additional Colors only used for direct Color referencing
     */
    CABLE_INSULATION(-1, 64, 64, 64, "Cable Insulation"),
    CONSTRUCTION_FOAM(-1, 64, 64, 64, "Construction Foam"),
    MACHINE_METAL(-1, 210, 220, 255, "Machine Metal");

    public static final Dyes[] VALUES = { dyeBlack, dyeRed, dyeGreen, dyeBrown, dyeBlue, dyePurple, dyeCyan,
        dyeLightGray, dyeGray, dyePink, dyeLime, dyeYellow, dyeLightBlue, dyeMagenta, dyeOrange, dyeWhite };

    public final byte mIndex;
    public final String mName;
    public final short[] mRGBa;
    public final short[] mOriginalRGBa;
    public final EnumChatFormatting formatting;
    private final ArrayList<Fluid> mFluidDyes = new GT_ArrayList<>(false, 1);

    Dyes(int aIndex, int aR, int aG, int aB, String aName) {
        this(aIndex, aR, aR, aB, aName, EnumChatFormatting.GRAY);
    }

    Dyes(int aIndex, int aR, int aG, int aB, String aName, EnumChatFormatting formatting) {
        mIndex = (byte) aIndex;
        mName = aName;
        mRGBa = new short[] { (short) aR, (short) aG, (short) aB, 0 };
        mOriginalRGBa = mRGBa.clone();
        this.formatting = formatting;
    }

    public static Dyes get(int aColor) {
        if (aColor >= 0 && aColor < 16) return VALUES[aColor];
        return _NULL;
    }

    public static short[] getModulation(int aColor, short[] aDefaultModulation) {
        if (aColor >= 0 && aColor < 16) return VALUES[aColor].mRGBa;
        return aDefaultModulation;
    }

    public static Dyes get(String aColor) {
        Object tObject = GT_Utility.getFieldContent(Dyes.class, aColor, false, false);
        if (tObject instanceof Dyes) return (Dyes) tObject;
        return _NULL;
    }

    public static boolean isAnyFluidDye(FluidStack aFluid) {
        return aFluid != null && isAnyFluidDye(aFluid.getFluid());
    }

    public static boolean isAnyFluidDye(Fluid aFluid) {
        if (aFluid != null) for (Dyes tDye : VALUES) if (tDye.isFluidDye(aFluid)) return true;
        return false;
    }

    public boolean isFluidDye(FluidStack aFluid) {
        return aFluid != null && isFluidDye(aFluid.getFluid());
    }

    public boolean isFluidDye(Fluid aFluid) {
        return aFluid != null && mFluidDyes.contains(aFluid);
    }

    public boolean addFluidDye(Fluid aDye) {
        if (aDye == null || mFluidDyes.contains(aDye)) return false;
        mFluidDyes.add(aDye);
        return true;
    }

    public int getSizeOfFluidList() {
        return mFluidDyes.size();
    }

    /**
     * @param aAmount 1 Fluid Material Unit (144) = 1 Dye Item
     */
    public FluidStack getFluidDye(int aIndex, long aAmount) {
        if (aIndex >= mFluidDyes.size() || aIndex < 0) return null;
        return new FluidStack(mFluidDyes.get(aIndex), (int) aAmount);
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    public static Dyes getDyeFromIndex(short index) {
        return index != -1 ? Dyes.get(index) : Dyes.MACHINE_METAL;
    }
}
