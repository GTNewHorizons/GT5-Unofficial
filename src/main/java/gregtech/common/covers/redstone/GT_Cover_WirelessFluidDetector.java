package gregtech.common.covers.redstone;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.GT_Cover_LiquidMeter;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

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
    public FluidTransmitterData doCoverThingsImpl(
            byte aSide,
            byte aInputRedstone,
            int aCoverID,
            FluidTransmitterData aCoverVariable,
            ICoverable aTileEntity,
            long aTimer) {
        byte signal = GT_Cover_LiquidMeter.computeSignalBasedOnFluid(
                aTileEntity, aCoverVariable.invert, aCoverVariable.threshold);
        long hash = hashCoverCoords(aTileEntity, aSide);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(
            byte aSide, int aCoverID, FluidTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(
            byte aSide, int aCoverID, FluidTransmitterData aCoverVariable, ICoverable aTileEntity) {
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

    /**
     * GUI Stuff
     */
    @Override
    public Object getClientGUIImpl(
            byte aSide,
            int aCoverID,
            FluidTransmitterData aCoverVariable,
            ICoverable aTileEntity,
            EntityPlayer aPlayer,
            World aWorld) {
        return new FluidTransmitterGUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    private class FluidTransmitterGUI extends TransmitterGUI<FluidTransmitterData> {
        private final GT_GuiIntegerTextBox thresholdBox;

        public FluidTransmitterGUI(
                byte aSide, int aCoverID, FluidTransmitterData aCoverVariable, ICoverable aTileEntity) {
            super(aSide, aCoverID, aCoverVariable, aTileEntity, 1, 2);

            thresholdBox = new GT_GuiShortTextBox(this, 1, 1 + startX, 2 + startY, spaceX * 5 - 4, 12);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer()
                    .drawString(GT_Utility.trans("222", "Fluid Threshold"), startX + spaceX * 5, 4 + startY, textColor);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            thresholdBox.setFocused(true);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            super.onMouseWheel(x, y, delta);
            if (thresholdBox.isFocused()) {
                genericMouseWheel(thresholdBox, delta, 0, Integer.MAX_VALUE);
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdBox) {
                coverVariable.threshold = parseTextBox(thresholdBox);
            }

            super.applyTextBox(box);
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            super.resetTextBox(box);
            if (box == thresholdBox) {
                thresholdBox.setText(Integer.toString(coverVariable.threshold));
            }
        }

        @Override
        protected void update() {
            super.update();
            resetTextBox(thresholdBox);
        }
    }
}
