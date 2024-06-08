package gregtech.common.covers.redstone;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.GT_Cover_NeedMaintainance;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class GT_Cover_WirelessMaintenanceDetector
    extends GT_Cover_AdvancedRedstoneTransmitterBase<GT_Cover_WirelessMaintenanceDetector.MaintenanceTransmitterData> {

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

    private static byte computeSignalBasedOnMaintenance(MaintenanceTransmitterData coverVariable,
        ICoverable tileEntity) {
        boolean signal = false;

        if (tileEntity instanceof IGregTechTileEntity) {
            IMetaTileEntity metaTE = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
            if (metaTE instanceof GT_MetaTileEntity_MultiBlockBase multiTE) {
                int ideal = multiTE.getIdealStatus();
                int real = multiTE.getRepairStatus();

                switch (coverVariable.mode) {
                    case NO_ISSUE -> signal = ideal == real;
                    case ONE_ISSUE, TWO_ISSUES, THREE_ISSUES, FOUR_ISSUES, FIVE_ISSUES -> signal = ideal - real
                        >= coverVariable.mode.ordinal();
                    case ROTOR_80, ROTOR_100 -> {
                        ItemStack rotor = multiTE.getRealInventory()[1];
                        if (GT_Cover_NeedMaintainance.isRotor(rotor)) {
                            long max = GT_MetaGenerated_Tool.getToolMaxDamage(rotor);
                            long current = GT_MetaGenerated_Tool.getToolDamage(rotor);

                            if (coverVariable.mode == MaintenanceMode.ROTOR_80) {
                                signal = current >= max * 8 / 10;
                            } else {
                                long expectedDamage = Math.round(
                                    Math.min(
                                        (double) multiTE.mEUt / multiTE.damageFactorLow,
                                        Math.pow(multiTE.mEUt, multiTE.damageFactorHigh)));
                                signal = current + expectedDamage * 2 >= max;
                            }
                        } else {
                            signal = true;
                        }
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
    public MaintenanceTransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final byte signal = computeSignalBasedOnMaintenance(aCoverVariable, aTileEntity);
        final long hash = hashCoverCoords(aTileEntity, side);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, MaintenanceTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRateImpl(ForgeDirection side, int aCoverID, MaintenanceTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
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

    // GUI stuff

    private static final String[] extraTexts = new String[] { "No Issues", ">= 1 Issue", ">= 2 Issues", ">= 3 Issues",
        ">= 4 Issues", ">= 5 Issues", "Rotor < 20%", "Rotor ≈ 0%" };

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new WirelessMaintenanceDetectorUIFactory(buildContext).createWindow();
    }

    private class WirelessMaintenanceDetectorUIFactory extends AdvancedRedstoneTransmitterBaseUIFactory {

        public WirelessMaintenanceDetectorUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIHeight() {
            return 143;
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
            super.addUIWidgets(builder);
            for (int i = 0; i < 8; i++) {
                builder.widget(
                    new TextWidget(extraTexts[i]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * (i % 2 == 0 ? 1 : 7), 4 + startY + spaceY * (2 + i / 2)));
            }
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<MaintenanceTransmitterData> controller) {
            super.addUIForDataController(controller);
            for (int i = 0; i < 8; i++) {
                final int index = i;
                controller.addFollower(
                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == MaintenanceMode.values()[index],
                    (coverData, state) -> {
                        coverData.mode = MaintenanceMode.values()[index];
                        return coverData;
                    },
                    widget -> widget.setToggleTexture(GT_UITextures.OVERLAY_BUTTON_CHECKMARK, GT_UITextures.TRANSPARENT)
                        .setPos(spaceX * (index % 2 == 0 ? 0 : 6), spaceY * (2 + index / 2)));
            }
        }
    }
}
