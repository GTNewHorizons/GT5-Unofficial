package gregtech.common.covers.redstone;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public abstract class GT_Cover_AdvancedRedstoneReceiverBase extends GT_Cover_AdvancedWirelessRedstoneBase<GT_Cover_AdvancedRedstoneReceiverBase.ReceiverData> {

    public GT_Cover_AdvancedRedstoneReceiverBase(ITexture coverTexture) {
        super(ReceiverData.class, coverTexture);
    }

    @Override
    public ReceiverData createDataObject() {
        return new ReceiverData();
    }

    @Override
    public ReceiverData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    /**
     * GUI Stuff
     */

    @Override
    public Object getClientGUIImpl(byte aSide, int aCoverID, ReceiverData aCoverVariable, ICoverable aTileEntity,
                                   EntityPlayer aPlayer, World aWorld) {
        return new ReceiverGUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    public enum GateMode {
        AND,
        NAND,
        OR,
        NOR,
        SINGLE_SOURCE
    }

    public static class ReceiverData extends GT_Cover_AdvancedWirelessRedstoneBase.WirelessData {
        private GateMode mode;

        public ReceiverData(int frequency, UUID uuid, GateMode mode) {
            super(frequency, uuid);
            this.mode = mode;
        }

        public ReceiverData() {
            this(0, null, GateMode.AND);
        }

        public GateMode getGateMode() {
            return mode;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ReceiverData(frequency, uuid, mode);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setByte("mode", (byte) mode.ordinal());

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeByte(mode.ordinal());
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            mode = GateMode.values()[tag.getByte("mode")];
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            mode = GateMode.values()[aBuf.readByte()];

            return this;
        }
    }

    private static class ReceiverGUI extends WirelessGUI<ReceiverData> {

        private static final int gateModeButtonIdStart = 1;

        public ReceiverGUI(byte aSide, int aCoverID, ReceiverData aCoverVariable, ICoverable aTileEntity) {
            super(aSide, aCoverID, aCoverVariable, aTileEntity);

            new GT_GuiIconButton(this, gateModeButtonIdStart + 0, startX + spaceX * 0, startY + spaceY * 2, GT_GuiIcon.AND_GATE)
                .setTooltipText(GT_Utility.trans("006", "AND Gate"));
            new GT_GuiIconButton(this, gateModeButtonIdStart + 1, startX + spaceX * 1, startY + spaceY * 2, GT_GuiIcon.NAND_GATE)
                .setTooltipText(GT_Utility.trans("006", "NAND Gate"));
            new GT_GuiIconButton(this, gateModeButtonIdStart + 2, startX + spaceX * 2, startY + spaceY * 2, GT_GuiIcon.OR_GATE)
                .setTooltipText(GT_Utility.trans("006", "OR Gate"));
            new GT_GuiIconButton(this, gateModeButtonIdStart + 3, startX + spaceX * 3, startY + spaceY * 2, GT_GuiIcon.NOR_GATE)
                    .setTooltipText(GT_Utility.trans("006", "NOR Gate"));
            new GT_GuiIconButton(this, gateModeButtonIdStart + 4, startX + spaceX * 4, startY + spaceY * 2, GT_GuiIcon.PROGRESS)
                    .setTooltipText("ANALOG Mode", "Only use this mode with ONE transmitter in total,", "no logic involved");
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(
                GT_Utility.trans("601", "Gate Mode"),
                startX + spaceX * 5,
                4 + startY + spaceY * 2,
                textColor);
        }

        @Override
        protected void update() {
            super.update();
            updateButtons();
        }

        private void updateButtons() {
            GuiButton button;
            for (int i = gateModeButtonIdStart; i < gateModeButtonIdStart + 5; ++i) {
                button = (GuiButton) this.buttonList.get(i);
                button.enabled = (button.id - gateModeButtonIdStart) != coverVariable.mode.ordinal();
            }
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn.id >= gateModeButtonIdStart && btn.enabled) {
                coverVariable.mode = GateMode.values()[btn.id - gateModeButtonIdStart];
            }

            super.buttonClicked(btn);
        }
    }
}
