package gregtech.common.covers.redstone;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.math.MathExpression;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.GT_Cover_LiquidMeter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_TextFieldWidget;
import io.netty.buffer.ByteBuf;

public class GT_Cover_WirelessFluidDetector
    extends GT_Cover_AdvancedRedstoneTransmitterBase<GT_Cover_WirelessFluidDetector.FluidTransmitterData> {

    public GT_Cover_WirelessFluidDetector(ITexture coverTexture) {
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
    public FluidTransmitterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID,
        FluidTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte signal = GT_Cover_LiquidMeter
            .computeSignalBasedOnFluid(aTileEntity, aCoverVariable.invert, aCoverVariable.threshold);
        long hash = hashCoverCoords(aTileEntity, aSide);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, FluidTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, FluidTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    public static class FluidTransmitterData extends GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData {

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
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new WirelessFluidDetectorUIFactory(buildContext).createWindow();
    }

    private class WirelessFluidDetectorUIFactory extends AdvancedRedstoneTransmitterBaseUIFactory {

        public WirelessFluidDetectorUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getFrequencyRow() {
            return 1;
        }

        @Override
        protected int getButtonRow() {
            return 2;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            super.addUIWidgets(builder);
            builder.widget(
                new TextWidget(GT_Utility.trans("222", "Fluid threshold")).setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 5, 4 + startY));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<FluidTransmitterData> controller) {
            super.addUIForDataController(controller);
            controller.addFollower(
                new CoverDataFollower_TextFieldWidget<>(),
                coverData -> String.valueOf(coverData.threshold),
                (coverData, state) -> {
                    coverData.threshold = (int) MathExpression.parseMathExpression(state);
                    return coverData;
                },
                widget -> widget.setOnScrollNumbers()
                    .setNumbers(0, Integer.MAX_VALUE)
                    .setPos(1, 2)
                    .setSize(spaceX * 5 - 4, 12));
        }
    }
}
