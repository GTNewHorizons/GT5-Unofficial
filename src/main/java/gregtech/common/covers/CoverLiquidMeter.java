package gregtech.common.covers;

import java.util.Arrays;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;
import io.netty.buffer.ByteBuf;

/**
 * TODO: Implement overlay rendering only with
 * {@link CoverBehaviorBase#getSpecialCoverFGTexture()}
 */
public class CoverLiquidMeter extends CoverBehaviorBase<CoverLiquidMeter.LiquidMeterData> {

    public CoverLiquidMeter(CoverContext context, ITexture coverTexture) {
        super(context, LiquidMeterData.class, coverTexture);
    }

    @Override
    protected LiquidMeterData createDataObject() {
        return null;
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
    public CoverLiquidMeter.LiquidMeterData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null) {
            byte signal = computeSignalBasedOnFluid(coverable, coverData.inverted, coverData.threshold);
            coverable.setOutputRedstoneSignal(coverSide, signal);
        }

        return coverData;
    }

    @Override
    public LiquidMeterData onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (coverData.inverted) {
            coverData.inverted = false;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("055", "Normal"));
        } else {
            coverData.inverted = true;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("054", "Inverted"));
        }
        return coverData;
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
    public int getTickRate() {
        return 1;
    }

    @Override
    public int getDefaultTickRate() {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new LiquidMeterUIFactory(buildContext).createWindow();
    }

    private class LiquidMeterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;
        private int maxCapacity;

        public LiquidMeterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            final String INVERTED = GTUtility.trans("INVERTED", "Inverted");
            final String NORMAL = GTUtility.trans("NORMAL", "Normal");

            setMaxCapacity();

            builder.widget(
                new CoverDataControllerWidget<>(
                    this::getCoverData,
                    this::setCoverData,
                    CoverLiquidMeter.this::createDataObject)
                        .addFollower(
                            CoverDataFollowerToggleButtonWidget.ofRedstone(),
                            coverData -> coverData.inverted,
                            (coverData, state) -> {
                                coverData.inverted = state;
                                return coverData;
                            },
                            widget -> widget.addTooltip(0, NORMAL)
                                .addTooltip(1, INVERTED)
                                .setPos(spaceX * 0, spaceY * 0))
                        .addFollower(
                            new CoverDataFollowerNumericWidget<>(),
                            coverData -> (double) coverData.threshold,
                            (coverData, state) -> {
                                coverData.threshold = state.intValue();
                                return coverData;
                            },
                            widget -> widget.setBounds(0, maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE)
                                .setScrollValues(1000, 144, 100000)
                                .setFocusOnGuiOpen(true)
                                .setPos(spaceX * 0, spaceY * 1 + 2)
                                .setSize(spaceX * 4 + 5, 12))
                        .setPos(startX, startY))
                .widget(
                    new TextWidget()
                        .setStringSupplier(
                            () -> getCoverData() != null ? getCoverData().inverted ? INVERTED : NORMAL : "")
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("222", "Fluid threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 - 10, startY + spaceY * 1 + 4));
        }

        private void setMaxCapacity() {
            final ICoverable tile = getUIBuildContext().getTile();
            if (!tile.isDead() && tile instanceof IFluidHandler) {
                FluidTankInfo[] tanks = ((IFluidHandler) tile).getTankInfo(ForgeDirection.UNKNOWN);
                maxCapacity = Arrays.stream(tanks)
                    .mapToInt(tank -> tank.capacity)
                    .sum();
            } else {
                maxCapacity = -1;
            }
        }
    }

    public static class LiquidMeterData implements ISerializableObject {

        private boolean inverted;
        /**
         * The special value {@code 0} means threshold check is disabled.
         */
        private int threshold;

        public LiquidMeterData() {
            inverted = false;
            threshold = 0;
        }

        public LiquidMeterData(boolean inverted, int threshold) {
            this.inverted = inverted;
            this.threshold = threshold;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new LiquidMeterData(inverted, threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("invert", inverted);
            tag.setInteger("threshold", threshold);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(inverted);
            aBuf.writeInt(threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            inverted = tag.getBoolean("invert");
            threshold = tag.getInteger("threshold");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf) {
            inverted = aBuf.readBoolean();
            threshold = aBuf.readInt();
            return this;
        }
    }
}
