package gregtech.common.covers;

import static gregtech.api.enums.GTValues.E;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.FluidFilterUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverFluidfilter extends Cover {

    // Uses the lower 3 bits of the cover variable, so we have 8 options to work with (0-7)
    private final int FILTER_INPUT_DENY_OUTPUT = 0; // 000
    private final int INVERT_INPUT_DENY_OUTPUT = 1; // 001
    private final int FILTER_INPUT_ANY_OUTPUT = 2; // 010
    private final int INVERT_INPUT_ANY_OUTPUT = 3; // 011
    private final int DENY_INPUT_FILTER_OUTPUT = 4; // 100
    private final int DENY_INPUT_INVERT_OUTPUT = 5; // 101
    private final int ANY_INPUT_FILTER_OUTPUT = 6; // 110
    private final int ANY_INPUT_INVERT_OUTPUT = 7; // 111

    private int mFluidID;
    private int mFilterMode;

    public CoverFluidfilter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.mFluidID = -1;
        this.mFilterMode = 0;
    }

    public int getFluidId() {
        return mFluidID;
    }

    public CoverFluidfilter setFluidId(int fluidId) {
        this.mFluidID = fluidId;
        return this;
    }

    public int getFilterMode() {
        return mFilterMode;
    }

    public CoverFluidfilter setFilterMode(int filterMode) {
        this.mFilterMode = filterMode;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagCompound tNBT) {
            mFilterMode = tNBT.getInteger("mFilterMode");
            if (tNBT.hasKey("mFluid", NBT.TAG_STRING)) mFluidID = FluidRegistry.getFluidID(tNBT.getString("mFluid"));
            else mFluidID = -1;
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        mFilterMode = byteData.readByte();
        mFluidID = byteData.readInt();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tNBT = new NBTTagCompound();
        tNBT.setInteger("mFilterMode", mFilterMode);
        if (mFluidID >= 0) tNBT.setString(
            "mFluid",
            FluidRegistry.getFluid(mFluidID)
                .getName());
        return tNBT;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(mFilterMode)
            .writeInt(mFluidID);
    }

    @Override
    public String getDescription() {
        final Fluid fluid = FluidRegistry.getFluid(mFluidID);
        if (fluid == null) return E;

        final FluidStack sFluid = new FluidStack(fluid, 1000);
        return (String.format("Filtering Fluid: %s - %s", sFluid.getLocalizedName(), getFilterMode(mFilterMode)));
    }

    public String getFilterMode(int aFilterMode) {
        return switch (aFilterMode) {
            case FILTER_INPUT_DENY_OUTPUT -> GTUtility.trans("043", "Filter input, Deny output");
            case INVERT_INPUT_DENY_OUTPUT -> GTUtility.trans("044", "Invert input, Deny output");
            case FILTER_INPUT_ANY_OUTPUT -> GTUtility.trans("045", "Filter input, Permit any output");
            case INVERT_INPUT_ANY_OUTPUT -> GTUtility.trans("046", "Invert input, Permit any output");
            case DENY_INPUT_FILTER_OUTPUT -> GTUtility.trans("307", "Deny input, Filter output");
            case DENY_INPUT_INVERT_OUTPUT -> GTUtility.trans("308", "Deny input, Invert output");
            case ANY_INPUT_FILTER_OUTPUT -> GTUtility.trans("309", "Permit any input, Filter output");
            case ANY_INPUT_INVERT_OUTPUT -> GTUtility.trans("310", "Permit any input, Invert output");
            default -> ("UNKNOWN");
        };
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mFilterMode = (mFilterMode + (aPlayer.isSneaking() ? -1 : 1)) % 8;
        if (mFilterMode < 0) {
            mFilterMode = 7;
        }

        GTUtility.sendChatToPlayer(aPlayer, getFilterMode(mFilterMode));
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (coverSide == ForgeDirection.UNKNOWN) return false;
        if (((aX > 0.375D) && (aX < 0.625D)) || ((coverSide.offsetX != 0) && ((aY > 0.375D) && (aY < 0.625D)))
            || (coverSide.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) != 0 && aZ > 0.375D && aZ < 0.625D
            || (coverSide.offsetZ != 0)) {
            final ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack == null) return true;

            final FluidStack tFluid = GTUtility.getFluidForFilledItem(tStack, true);
            if (tFluid != null) {
                final int aFluid = tFluid.getFluidID();
                mFluidID = aFluid;
                final FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid), 1000);
                GTUtility
                    .sendChatToPlayer(aPlayer, GTUtility.trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        if (aFluid == null) return true;

        int aFilterMode = mFilterMode;
        int aFilterFluid = mFluidID;

        if (aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == DENY_INPUT_INVERT_OUTPUT) return false;
        else if (aFilterMode == ANY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == FILTER_INPUT_ANY_OUTPUT;
        else return aFilterMode == INVERT_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        if (aFluid == null) return true;

        int aFilterMode = mFilterMode;
        int aFilterFluid = mFluidID;

        if (aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_DENY_OUTPUT) return false;
        else if (aFilterMode == FILTER_INPUT_ANY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_FILTER_OUTPUT;
        else return aFilterMode == DENY_INPUT_INVERT_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidFilterUIFactory(buildContext).createWindow();
    }

}
