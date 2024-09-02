package gregtech.common.covers;

import java.util.Arrays;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;
import io.netty.buffer.ByteBuf;

/**
 * TODO: Implement overlay rendering only with
 * {@link CoverBehaviorBase#getSpecialCoverFGTextureImpl(ForgeDirection, int, ISerializableObject, ICoverable)}
 */
public class CoverLiquidMeter extends CoverBehaviorBase<CoverLiquidMeter.LiquidMeterData> {

    public CoverLiquidMeter(ITexture coverTexture) {
        super(LiquidMeterData.class, coverTexture);
    }

    @Override
    public LiquidMeterData createDataObject(int aLegacyData) {
        return new LiquidMeterData(aLegacyData == 0, 0);
    }

    @Override
    public LiquidMeterData createDataObject() {
        return new LiquidMeterData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        return false;
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
    protected LiquidMeterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        LiquidMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte signal = computeSignalBasedOnFluid(aTileEntity, aCoverVariable.inverted, aCoverVariable.threshold);
        aTileEntity.setOutputRedstoneSignal(side, signal);

        return aCoverVariable;
    }

    @Override
    protected LiquidMeterData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID,
        LiquidMeterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aCoverVariable.inverted) {
            aCoverVariable.inverted = false;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("055", "Normal"));
        } else {
            aCoverVariable.inverted = true;
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("054", "Inverted"));
        }
        return aCoverVariable;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        LiquidMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(ForgeDirection side, int aCoverID, LiquidMeterData aCoverVariable,
        ICoverable aTileEntity) {
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
                new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverLiquidMeter.this)
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
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            inverted = aBuf.readBoolean();
            threshold = aBuf.readInt();
            return this;
        }
    }
}
