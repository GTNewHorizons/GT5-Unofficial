package gregtech.common.covers;

import static gregtech.api.enums.GTValues.E;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
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
import gregtech.common.covers.modes.BlockMode;
import gregtech.common.covers.modes.FilterDirectionMode;
import gregtech.common.covers.modes.FilterType;
import gregtech.common.gui.modularui.cover.CoverFluidfilterGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
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

    public FilterDirectionMode getFilterDirection() {
        return (getFilterMode() >> 2 & 0x1) == 0 ? FilterDirectionMode.INPUT : FilterDirectionMode.OUTPUT;
    }

    public void setFilterDirection(FilterDirectionMode ioMode) {
        FilterDirectionMode oldMode = getFilterDirection();
        if (ioMode == oldMode) return;

        int filterMode = getFilterMode();
        if (ioMode == FilterDirectionMode.INPUT) {
            filterMode &= 0x3;
        } else {
            filterMode |= 0x4;
        }
        setFilterMode(filterMode);
    }

    public FilterType getFilterType() {
        return (getFilterMode() & 0x1) == 0 ? FilterType.WHITELIST : FilterType.BLACKLIST;
    }

    public void setFilterType(FilterType filterType) {
        FilterType oldFilterType = getFilterType();
        if (filterType == oldFilterType) return;

        int filterMode = getFilterMode();
        if (filterType == FilterType.WHITELIST) {
            filterMode &= 0x6;
        } else {
            filterMode |= 0x1;
        }
        setFilterMode(filterMode);
    }

    public BlockMode getBlockMode() {
        return (getFilterMode() >> 1 & 0x1) == 0 ? BlockMode.BLOCK : BlockMode.ALLOW;
    }

    public void setBlockMode(BlockMode blockMode) {
        BlockMode oldBlockMode = getBlockMode();
        if (blockMode == oldBlockMode) return;

        int filterMode = getFilterMode();
        if (blockMode == BlockMode.BLOCK) {
            filterMode &= 0x5;
        } else {
            filterMode |= 0x2;
        }
        setFilterMode(filterMode);
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
        return (StatCollector.translateToLocalFormatted(
            "gt.interact.desc.filtering_fuild",
            sFluid.getLocalizedName(),
            getFilterMode(mFilterMode)));
    }

    public String getFilterMode(int aFilterMode) {
        return switch (aFilterMode) {
            case FILTER_INPUT_DENY_OUTPUT -> translateToLocal("gt.interact.desc.filter_i_deny_o");
            case INVERT_INPUT_DENY_OUTPUT -> translateToLocal("gt.interact.desc.invert_i_deny_o");
            case FILTER_INPUT_ANY_OUTPUT -> translateToLocal("gt.interact.desc.filter_i_any_o");
            case INVERT_INPUT_ANY_OUTPUT -> translateToLocal("gt.interact.desc.invert_i_any_o");
            case DENY_INPUT_FILTER_OUTPUT -> translateToLocal("gt.interact.desc.deny_i_filter_o");
            case DENY_INPUT_INVERT_OUTPUT -> translateToLocal("gt.interact.desc.deny_i_invert_o");
            case ANY_INPUT_FILTER_OUTPUT -> translateToLocal("gt.interact.desc.any_i_filter_o");
            case ANY_INPUT_INVERT_OUTPUT -> translateToLocal("gt.interact.desc.any_i_invert_o");
            default -> translateToLocal("gt.interact.desc.unknown");
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
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverFluidfilterGui(this);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidFilterUIFactory(buildContext).createWindow();
    }

}
