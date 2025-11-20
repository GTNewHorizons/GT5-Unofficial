package gtPlusPlus.xmod.gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.modes.BlockMode;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.OverflowUIFactory;
import gtPlusPlus.xmod.gregtech.common.covers.gui.CoverOverflowValveGui;
import io.netty.buffer.ByteBuf;

public class CoverOverflowValve extends Cover {

    private final int minOverflowPoint = 0;
    private final int maxOverflowPoint;
    private int overflowPoint;
    private int voidingRate;
    private boolean canFluidInput;
    private boolean canFluidOutput;

    public CoverOverflowValve(CoverContext context, int maxOverflowPoint) {
        super(context, null);
        this.maxOverflowPoint = maxOverflowPoint;
        this.overflowPoint = maxOverflowPoint;
        this.voidingRate = maxOverflowPoint / 10;
        this.canFluidInput = true;
        this.canFluidOutput = true;
    }

    public int getMinOverflowPoint() {
        return minOverflowPoint;
    }

    public int getMaxOverflowPoint() {
        return maxOverflowPoint;
    }

    public int getOverflowPoint() {
        return overflowPoint;
    }

    public CoverOverflowValve setOverflowPoint(int overflowPoint) {
        this.overflowPoint = overflowPoint;
        return this;
    }

    public int getVoidingRate() {
        return voidingRate;
    }

    public CoverOverflowValve setVoidingRate(int voidingRate) {
        this.voidingRate = voidingRate;
        return this;
    }

    public BlockMode getFluidInputMode() {
        return canFluidInput ? BlockMode.ALLOW : BlockMode.BLOCK;
    }

    public CoverOverflowValve setFluidInputMode(BlockMode fluidInputMode) {
        canFluidInput = fluidInputMode == BlockMode.ALLOW;
        return this;
    }

    /**
     * Use {@link CoverOverflowValve#getFluidInputMode()}
     */
    @Deprecated
    public boolean canFluidInput() {
        return canFluidInput;
    }

    /**
     * Use {@link CoverOverflowValve#setFluidInputMode(BlockMode)}
     */
    @Deprecated
    public CoverOverflowValve setCanFluidInput(boolean canFluidInput) {
        this.canFluidInput = canFluidInput;
        return this;
    }

    public BlockMode getFluidOutputMode() {
        return canFluidOutput ? BlockMode.ALLOW : BlockMode.BLOCK;
    }

    public CoverOverflowValve setFluidOutputMode(BlockMode fluidOutputMode) {
        canFluidOutput = fluidOutputMode == BlockMode.ALLOW;
        return this;
    }

    /**
     * Use {@link CoverOverflowValve#getFluidOutputMode()}
     */
    @Deprecated
    public boolean canFluidOutput() {
        return canFluidOutput;
    }

    /**
     * Use {@link CoverOverflowValve#setFluidOutputMode(BlockMode)}
     */
    @Deprecated
    public CoverOverflowValve setCanFluidOutput(boolean canFluidOutput) {
        this.canFluidOutput = canFluidOutput;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagCompound tag) {
            overflowPoint = tag.getInteger("overflowPoint");
            voidingRate = tag.getInteger("voidingRate");
            canFluidInput = tag.getBoolean("canFluidInput");
            canFluidOutput = tag.getBoolean("canFluidOutput");
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        overflowPoint = byteData.readInt();
        voidingRate = byteData.readInt();
        canFluidInput = byteData.readBoolean();
        canFluidOutput = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("overflowPoint", overflowPoint);
        tag.setInteger("voidingRate", voidingRate);
        tag.setBoolean("canFluidInput", canFluidInput);
        tag.setBoolean("canFluidOutput", canFluidOutput);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(overflowPoint)
            .writeInt(voidingRate)
            .writeBoolean(canFluidInput)
            .writeBoolean(canFluidOutput);
    }

    private FluidStack doOverflowThing(FluidStack fluid) {
        if (fluid != null && fluid.amount > overflowPoint)
            fluid.amount = Math.max(fluid.amount - voidingRate, overflowPoint);
        return fluid;
    }

    private void doOverflowThings(FluidStack[] fluids) {
        for (FluidStack fluid : fluids) doOverflowThing(fluid);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        if (overflowPoint == 0 || voidingRate == 0) return;

        if (coveredTile.get() instanceof IGregTechTileEntity gregTE) {
            IMetaTileEntity tile = gregTE.getMetaTileEntity();
            if (tile instanceof MTEBasicTank fluidTank) {
                fluidTank.setDrainableStack(doOverflowThing(fluidTank.getDrainableStack()));
            } else if (tile instanceof MTEFluidPipe fluidPipe && fluidPipe.isConnectedAtSide(coverSide)) {
                doOverflowThings(fluidPipe.mFluids);
            }
        }
    }

    // Overrides methods

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 5;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return canFluidOutput;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return canFluidInput;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            overflowPoint += (int) (maxOverflowPoint * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        } else {
            overflowPoint -= (int) (maxOverflowPoint * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        }

        if (overflowPoint > maxOverflowPoint) overflowPoint = minOverflowPoint;
        if (overflowPoint <= minOverflowPoint) overflowPoint = maxOverflowPoint;

        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector
                .translateToLocalFormatted("GTPP.chat.text.cover_overflow_valve_overflow_point", overflowPoint));
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int amount = aPlayer.isSneaking() ? 128 : 8;
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            overflowPoint += amount;
        } else {
            overflowPoint -= amount;
        }

        if (overflowPoint > maxOverflowPoint) overflowPoint = minOverflowPoint;
        if (overflowPoint <= minOverflowPoint) overflowPoint = maxOverflowPoint;

        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector
                .translateToLocalFormatted("GTPP.chat.text.cover_overflow_valve_overflow_point", overflowPoint));
        return true;
    }
    // GUI

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected @NotNull CoverBaseGui<CoverOverflowValve> getCoverGui() {
        return new CoverOverflowValveGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new OverflowUIFactory(buildContext).createWindow();
    }

}
