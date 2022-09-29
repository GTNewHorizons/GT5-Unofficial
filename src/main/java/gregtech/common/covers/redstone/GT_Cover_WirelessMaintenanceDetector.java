package gregtech.common.covers.redstone;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.GT_Cover_NeedMaintainance;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.UUID;

public class GT_Cover_WirelessMaintenanceDetector extends GT_Cover_AdvancedRedstoneTransmitterBase<GT_Cover_WirelessMaintenanceDetector.MaintenanceTransmitterData> {

    public GT_Cover_WirelessMaintenanceDetector(ITexture coverTexture) {
        super(MaintenanceTransmitterData.class, coverTexture);
    }

    @Override
    public MaintenanceTransmitterData createDataObject() {
        return new MaintenanceTransmitterData();
    }

    @Override
    public MaintenanceTransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    private static byte computeSignalBasedOnMaintenance(MaintenanceTransmitterData coverVariable, ICoverable tileEntity) {
        boolean signal = false;

        if (tileEntity instanceof IGregTechTileEntity) {
            IMetaTileEntity metaTE = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
            if (metaTE instanceof GT_MetaTileEntity_MultiBlockBase) {
                GT_MetaTileEntity_MultiBlockBase multiTE = (GT_MetaTileEntity_MultiBlockBase) metaTE;
                int ideal = multiTE.getIdealStatus();
                int real = multiTE.getRepairStatus();

                switch (coverVariable.mode) {
                    case NO_ISSUE:
                        signal = ideal == real;
                        break;
                    case ONE_ISSUE:
                    case TWO_ISSUES:
                    case THREE_ISSUES:
                    case FOUR_ISSUES:
                    case FIVE_ISSUES:
                        signal = ideal - real >= coverVariable.mode.ordinal();
                        break;
                    case ROTOR_80:
                    case ROTOR_100:
                        ItemStack rotor = multiTE.getRealInventory()[1];
                        if (GT_Cover_NeedMaintainance.isRotor(rotor)) {
                            long max = GT_MetaGenerated_Tool.getToolMaxDamage(rotor);
                            long current = GT_MetaGenerated_Tool.getToolDamage(rotor);

                            if (coverVariable.mode == MaintenanceMode.ROTOR_80) {
                                signal = current >= max * 8 / 10;
                            } else {
                                long expectedDamage = Math.round(Math.min(
                                        (double) multiTE.mEUt / multiTE.damageFactorLow,
                                        Math.pow(multiTE.mEUt, multiTE.damageFactorHigh)
                                ));
                                signal = current + expectedDamage * 2 >= max;
                            }
                        } else {
                            signal = true;
                        }
                }
            }
        }

        if (coverVariable.invert) {
            signal = !signal;
        }
        
        return (byte) (signal ? 15 : 0);
    }

    @Override
    public MaintenanceTransmitterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID,
                                                        MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte signal = computeSignalBasedOnMaintenance(aCoverVariable, aTileEntity);
        long hash = hashCoverCoords(aTileEntity, aSide);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);
        
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, MaintenanceTransmitterData aCoverVariable,
                                         ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, MaintenanceTransmitterData aCoverVariable,
                                                         ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRateImpl(byte aSide, int aCoverID, MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return 60;
    }

    public enum MaintenanceMode {
        NO_ISSUE,
        ONE_ISSUE,
        TWO_ISSUES,
        THREE_ISSUES,
        FOUR_ISSUES,
        FIVE_ISSUES,
        ROTOR_80,
        ROTOR_100
    }

    public static class MaintenanceTransmitterData extends GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData {
        private MaintenanceMode mode;

        public MaintenanceTransmitterData(int frequency, UUID uuid, boolean invert, MaintenanceMode mode) {
            super(frequency, uuid, invert);
            this.mode = mode;
        }

        public MaintenanceTransmitterData() {
            super();
            this.mode = MaintenanceMode.ONE_ISSUE;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new MaintenanceTransmitterData(frequency, uuid, invert, mode);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setInteger("mode", mode.ordinal());

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeInt(mode.ordinal());
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            mode = MaintenanceMode.values()[tag.getInteger("mode")];
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            mode = MaintenanceMode.values()[aBuf.readInt()];

            return this;
        }
    }

    /**
     * GUI Stuff
     */

    private static final String[] extraTexts = new String[]{
            "No Issues", ">= 1 Issue", ">= 2 Issues", ">= 3 Issues",
            ">= 4 Issues", ">= 5 Issues", "Rotor < 80%", "Rotor < 100%"
    };

    @Override
    public Object getClientGUIImpl(byte aSide, int aCoverID, MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity,
                                   EntityPlayer aPlayer, World aWorld) {
        return new MaintenanceTransmitterGUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    private class MaintenanceTransmitterGUI extends TransmitterGUI<MaintenanceTransmitterData> {

        private static final String guiTexturePath = "gregtech:textures/gui/GuiCoverBig.png";
        private static final int maintenanceButtonIdStart = 2;

        public MaintenanceTransmitterGUI(byte aSide, int aCoverID, MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity) {
            super(aSide, aCoverID, aCoverVariable, aTileEntity);
            this.mGUIbackgroundLocation = new ResourceLocation(guiTexturePath);
            this.gui_height = 143;

            for (int i = 0; i < 8; ++i) {
                new GT_GuiIconCheckButton(this, maintenanceButtonIdStart + i, startX + spaceX * (i % 2 == 0 ? 0 : 6), startY + spaceY * (2 + i / 2), GT_GuiIcon.CHECKMARK, null);
            }
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            for (int i = 0; i < 8; ++i) {
                this.getFontRenderer().drawString(
                        extraTexts[i],
                        startX + spaceX * (i % 2 == 0 ? 1 : 7),
                        4 + startY + spaceY * (2 + i / 2),
                        textColor);
            }
        }

        @Override
        protected void update() {
            super.update();
            updateButtons();
        }

        private void updateButtons() {
            GT_GuiIconCheckButton button;
            for (int i = maintenanceButtonIdStart; i < maintenanceButtonIdStart + 8; ++i) {
                button = (GT_GuiIconCheckButton) this.buttonList.get(i);
                button.enabled = (button.id - maintenanceButtonIdStart) != coverVariable.mode.ordinal();
                button.setChecked(!button.enabled);
            }
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn.id >= maintenanceButtonIdStart && btn.enabled) {
                coverVariable.mode = MaintenanceMode.values()[btn.id - maintenanceButtonIdStart];
            }

            super.buttonClicked(btn);
        }
    }
}
