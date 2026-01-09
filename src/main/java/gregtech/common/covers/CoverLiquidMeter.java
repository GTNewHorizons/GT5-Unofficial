package gregtech.common.covers;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.CoverLiquidMeterGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.LiquidMeterUIFactory;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;
import io.netty.buffer.ByteBuf;

/**
 * TODO: Implement overlay rendering only with {@link Cover#getOverlayTexture()}
 */
public class CoverLiquidMeter extends Cover implements Invertable {

    private boolean inverted;
    /**
     * The special value {@code 0} means threshold check is disabled.
     */
    private int threshold;

    public CoverLiquidMeter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        inverted = false;
        threshold = 0;
    }

    @Override
    public boolean isInverted() {
        return this.inverted;
    }

    @Override
    public CoverLiquidMeter setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public CoverLiquidMeter setThreshold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        inverted = tag.getBoolean("invert");
        threshold = tag.getInteger("threshold");
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        inverted = byteData.readBoolean();
        threshold = byteData.readInt();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("invert", inverted);
        tag.setInteger("threshold", threshold);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(inverted);
        byteBuf.writeInt(threshold);
    }

    public static byte computeSignalBasedOnFluid(ICoverable tileEntity, boolean inverted, int threshold) {
        if (tileEntity instanceof IFluidHandler) {
            FluidTankInfo[] tanks = ((IFluidHandler) tileEntity).getTankInfo(ForgeDirection.UNKNOWN);
            long max = 0;
            long used = 0;
            if (tanks != null) {
                for (FluidTankInfo tank : tanks) {
                    if (tank != null) {
                        if (tileEntity instanceof BaseMetaTileEntity
                            && ((BaseMetaTileEntity) tileEntity).getMetaTileEntity() instanceof MTEDigitalTankBase) {
                            max += ((MTEDigitalTankBase) ((BaseMetaTileEntity) tileEntity).getMetaTileEntity())
                                .getRealCapacity();
                        } else max += tank.capacity;
                        FluidStack tLiquid = tank.fluid;
                        if (tLiquid != null) {
                            used += tLiquid.amount;
                        }
                    }
                }
            }

            return GTUtility.convertRatioToRedstone(used, max, threshold, inverted);
        } else {
            return 0;
        }
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null) {
            byte signal = computeSignalBasedOnFluid(coverable, inverted, threshold);
            coverable.setOutputRedstoneSignal(coverSide, signal);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (inverted) {
            inverted = false;
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.normal");
        } else {
            inverted = true;
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.inverted");
        }
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
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
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
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    @Override
    public int getDefaultTickRate() {
        return 5;
    }

    private int getMaxCapacity() {
        final ICoverable tile = getTile();
        if (!tile.isDead() && tile instanceof IFluidHandler) {
            FluidTankInfo[] tanks = ((IFluidHandler) tile).getTankInfo(ForgeDirection.UNKNOWN);
            return Arrays.stream(tanks)
                .mapToInt(tank -> tank.capacity)
                .sum();
        } else {
            return -1;
        }
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected @NotNull CoverBaseGui<CoverLiquidMeter> getCoverGui() {
        return new CoverLiquidMeterGui(this, getMaxCapacity());
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new LiquidMeterUIFactory(buildContext, getMaxCapacity()).createWindow();
    }

}
