package gregtech.common.covers.redstone;

import java.util.Arrays;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverLiquidMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;
import io.netty.buffer.ByteBuf;

public class CoverWirelessFluidDetector
    extends CoverAdvancedRedstoneTransmitterBase<CoverWirelessFluidDetector.FluidTransmitterData> {

    public CoverWirelessFluidDetector(ITexture coverTexture) {
        super(FluidTransmitterData.class, coverTexture);
    }

    @Override
    public FluidTransmitterData createDataObject() {
        return new FluidTransmitterData();
    }

    @Override
    public FluidTransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    @Override
    public FluidTransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        FluidTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final byte signal = CoverLiquidMeter
            .computeSignalBasedOnFluid(aTileEntity, aCoverVariable.invert, aCoverVariable.threshold);
        final long hash = hashCoverCoords(aTileEntity, side);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, FluidTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        FluidTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public static class FluidTransmitterData extends CoverAdvancedRedstoneTransmitterBase.TransmitterData {

        /** The special value {@code 0} means threshold check is disabled. */
        private int threshold;

        public FluidTransmitterData(int frequency, UUID uuid, boolean invert, int threshold) {
            super(frequency, uuid, invert);
            this.threshold = threshold;
        }

        public FluidTransmitterData() {
            super();
            this.threshold = 0;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new FluidTransmitterData(frequency, uuid, invert, threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setInteger("threshold", threshold);

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeInt(threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            threshold = tag.getInteger("threshold");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            threshold = aBuf.readInt();

            return this;
        }
    }

    // GUI stuff

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessFluidDetectorUIFactory(buildContext).createWindow();
    }

    private class WirelessFluidDetectorUIFactory extends AdvancedRedstoneTransmitterBaseUIFactory {

        private int maxCapacity;

        public WirelessFluidDetectorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getFrequencyRow() {
            return 0;
        }

        @Override
        protected int getButtonRow() {
            return 1;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            setMaxCapacity();
            super.addUIWidgets(builder);
            builder.widget(
                new TextWidget(GTUtility.trans("222", "Fluid threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5, 4 + startY + spaceY * 2));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<FluidTransmitterData> controller) {
            super.addUIForDataController(controller);
            controller.addFollower(
                new CoverDataFollowerNumericWidget<>(),
                coverData -> (double) coverData.threshold,
                (coverData, state) -> {
                    coverData.threshold = state.intValue();
                    return coverData;
                },
                widget -> widget.setBounds(0, maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE)
                    .setScrollValues(1000, 144, 100000)
                    .setFocusOnGuiOpen(true)
                    .setPos(1, 2 + spaceY * 2)
                    .setSize(spaceX * 5 - 4, 12));
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
}
