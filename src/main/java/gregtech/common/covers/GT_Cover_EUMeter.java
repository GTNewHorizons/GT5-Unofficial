package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicBatteryBuffer;
import gregtech.api.net.GT_Packet_TileEntityCoverNew;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class GT_Cover_EUMeter extends GT_CoverBehaviorBase<GT_Cover_EUMeter.EUMeterData> {

    public GT_Cover_EUMeter() {
        super(EUMeterData.class);
    }

    @Override
    public EUMeterData createDataObject(int aLegacyData) {
        return new EUMeterData(aLegacyData, 0);
    }

    @Override
    public EUMeterData createDataObject() {
        return new EUMeterData();
    }

    @Override
    protected EUMeterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final long stored = aCoverVariable.type.getTileEntityStoredEnergy(aTileEntity);
        final long capacity = aCoverVariable.type.getTileEntityEnergyCapacity(aTileEntity);

        byte redstoneSignal;

        if (stored == 0L) {
            // nothing
            redstoneSignal = 0;
        } else if (stored >= capacity) {
            // full
            redstoneSignal = 15;
        } else {
            // 1-14 range
            redstoneSignal = (byte) (1 + (14 * stored) / capacity);
        }

        if (aCoverVariable.inverted) {
            redstoneSignal = (byte) (15 - redstoneSignal);
        }

        if (aCoverVariable.threshold > 0) {
            if (aCoverVariable.inverted && stored >= aCoverVariable.threshold) {
                redstoneSignal = 0;
            } else if (!aCoverVariable.inverted && stored < aCoverVariable.threshold) {
                redstoneSignal = 0;
            }
        }

        aTileEntity.setOutputRedstoneSignal(aSide, redstoneSignal);
        return aCoverVariable;
    }

    @Override
    protected EUMeterData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int num = (aCoverVariable.getNum() + (aPlayer.isSneaking() ? -1 : 1) + EnergyType.values().length * 2) % (EnergyType.values().length * 2);
        switch (num) {
            case 0:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("031", "Normal Universal Storage"));
                break;
            case 1:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("032", "Inverted Universal Storage"));
                break;
            case 2:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("033", "Normal Electricity Storage"));
                break;
            case 3:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("034", "Inverted Electricity Storage"));
                break;
            case 4:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("035", "Normal Steam Storage"));
                break;
            case 5:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("036", "Inverted Steam Storage"));
                break;
            case 6:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("037", "Normal Average Electric Input"));
                break;
            case 7:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("038", "Inverted Average Electric Input"));
                break;
            case 8:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("039", "Normal Average Electric Output"));
                break;
            case 9:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("040", "Inverted Average Electric Output"));
                break;
            case 10:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("041", "Normal Electricity Storage(Including Batteries)"));
                break;
            case 11:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("042", "Inverted Electricity Storage(Including Batteries)"));
                break;
        }
        aCoverVariable.setNum(num);
        return aCoverVariable;
    }

    //region Static Result Methods
    @Override
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity) {
        return 20;
    }
    //endregion

    //region GUI Stuff
    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public Object getClientGUIImpl(byte aSide, int aCoverID, EUMeterData coverData, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }
    //endregion

    public static class EUMeterData implements ISerializableObject {
        private EnergyType type;
        private boolean inverted;
        /**
         * The special value {@code 0} means threshold check is disabled.
         */
        private long threshold;


        public EUMeterData() {
            type = EnergyType.UNIVERSAL_STORAGE;
            inverted = false;
            threshold = 0;
        }

        public EUMeterData(EnergyType type, boolean inverted, long threshold) {
            this.type = type;
            this.inverted = inverted;
            this.threshold = threshold;
        }

        public EUMeterData(int num, long threshold) {
            this();
            this.setNum(num);
            this.threshold = threshold;
        }

        public int getNum() {
            return type.ordinal() * 2 + (inverted ? 1 : 0);
        }

        public void setNum(int num) {
            type = EnergyType.getEnergyType(num / 2);
            inverted = num % 2 == 1;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new EUMeterData(type, inverted, threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("typeOrdinal", type.ordinal());
            tag.setBoolean("inverted", inverted);
            tag.setLong("threshold", threshold);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(type.ordinal());
            aBuf.writeBoolean(inverted);
            aBuf.writeLong(threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            int typeOrdinal = tag.getInteger("typeOrdinal");
            type = EnergyType.getEnergyType(typeOrdinal);
            inverted = tag.getBoolean("inverted");
            threshold = tag.getLong("threshold");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
            int typeOrdinal = aBuf.readInt();
            type = EnergyType.getEnergyType(typeOrdinal);
            inverted = aBuf.readBoolean();
            threshold = aBuf.readLong();
            return this;
        }
    }

    private enum EnergyType {
        UNIVERSAL_STORAGE(
            GT_Utility.trans("301", "Universal"),
            GT_Utility.trans("256", "Universal Storage"),
            ICoverable::getUniversalEnergyStored,
            ICoverable::getUniversalEnergyCapacity
        ),
        ELECTRICITY_STORAGE(
            GT_Utility.trans("302", "Int. EU"),
            GT_Utility.trans("257", "Electricity Storage"),
            ICoverable::getStoredEU,
            ICoverable::getEUCapacity
        ),
        STEAM_STORAGE(
            GT_Utility.trans("303", "Steam"),
            GT_Utility.trans("258", "Steam Storage"),
            ICoverable::getStoredSteam,
            ICoverable::getSteamCapacity
        ),
        AVERAGE_ELECTRIC_INPUT(
            GT_Utility.trans("304", "Avg. Input"),
            GT_Utility.trans("259", "Average Electric Input"),
            ICoverable::getAverageElectricInput,
            (te) -> te.getInputVoltage() * te.getInputAmperage()
        ),
        AVERAGE_ELECTRIC_OUTPUT(
            GT_Utility.trans("305", "Avg. Output"),
            GT_Utility.trans("260", "Average Electric Output"),
            ICoverable::getAverageElectricOutput,
            (te) -> te.getOutputVoltage() * te.getOutputAmperage()
        ),
        ELECTRICITY_STORAGE_INCLUDING_BATTERIES(
            GT_Utility.trans("306", "EU stored"),
            GT_Utility.trans("261", "Electricity Storage(Including Batteries)"),
            (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof GT_MetaTileEntity_BasicBatteryBuffer) {
                        GT_MetaTileEntity_BasicBatteryBuffer buffer = (GT_MetaTileEntity_BasicBatteryBuffer) mte;
                        return buffer.getStoredEnergy()[0];
                    }
                }
                return te.getStoredEU();
            },
            (te) -> {
                if (te instanceof IGregTechTileEntity) {
                    IMetaTileEntity mte = ((IGregTechTileEntity) te).getMetaTileEntity();
                    if (mte instanceof GT_MetaTileEntity_BasicBatteryBuffer) {
                        GT_MetaTileEntity_BasicBatteryBuffer buffer = (GT_MetaTileEntity_BasicBatteryBuffer) mte;
                        return buffer.getStoredEnergy()[1];
                    }
                }
                return te.getEUCapacity();
            }
        );

        private final String title;
        private final String tooltip;
        private final Function<ICoverable, Long> getTileEntityStoredEnergyFunc;
        private final Function<ICoverable, Long> getTileEntityEnergyCapacityFunc;

        EnergyType(String title, String tooltip, Function<ICoverable, Long> getTileEntityStoredEnergyFunc, Function<ICoverable, Long> getTileEntityEnergyCapacityFunc) {
            this.title = title;
            this.tooltip = tooltip;
            this.getTileEntityStoredEnergyFunc = getTileEntityStoredEnergyFunc;
            this.getTileEntityEnergyCapacityFunc = getTileEntityEnergyCapacityFunc;
        }

        public String getTitle() {
            return title;
        }

        public String getTooltip() {
            return tooltip;
        }

        public long getTileEntityStoredEnergy(ICoverable aTileEntity) {
            return getTileEntityStoredEnergyFunc.apply(aTileEntity);
        }

        public long getTileEntityEnergyCapacity(ICoverable aTileEntity) {
            return getTileEntityEnergyCapacityFunc.apply(aTileEntity);
        }

        public EnergyType getNext() {
            return values()[(ordinal() + 1) % values().length];
        }

        public static EnergyType getEnergyType(int ordinal) {
            if (ordinal < 0 || values().length <= ordinal) {
                ordinal = 0;
            }
            return values()[ordinal];
        }
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private final GT_GuiIconButton typeButton;
        private final GT_GuiIconCheckButton invertedButton;
        private final GT_GuiIntegerTextBox thresholdSlot;
        private final EUMeterData coverVariable;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
        private final String NORMAL = GT_Utility.trans("NORMAL", "Normal");

        public GUI(byte aSide, int aCoverID, EUMeterData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            typeButton = new GT_GuiIconButton(this, 0, startX, startY, GT_GuiIcon.CYCLIC);
            invertedButton = new GT_GuiIconCheckButton(this, 2, startX, startY + spaceY, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF, INVERTED, NORMAL);
            thresholdSlot = new GT_GuiIntegerTextBox(this, 4, startX, startY + spaceY * 2 + 2, spaceX * 8, 12);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(coverVariable.type.getTitle(), startX + spaceX, 4 + startY, 0xFF555555);
            this.getFontRenderer().drawString(coverVariable.inverted ? INVERTED : NORMAL, startX + spaceX, 4 + startY + spaceY, 0xFF555555);
            this.getFontRenderer().drawString(GT_Utility.trans("222.1", "Energy threshold"), startX, startY + spaceY * 3 + 4, 0xFF555555);
        }


        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            thresholdSlot.setFocused(true);
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn == typeButton) {
                coverVariable.type = coverVariable.type.getNext();
            }
            if (btn == invertedButton) {
                coverVariable.inverted = !coverVariable.inverted;
            }
            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            if (thresholdSlot.isFocused()) {
                long val = parseTextBox(thresholdSlot);

                long step = 1000;
                if (isShiftKeyDown()) {
                    step *= 100;
                }
                if (isCtrlKeyDown()) {
                    step /= 10;
                }

                try {
                    val = Math.addExact(val, delta * step);
                } catch (ArithmeticException e) {
                    val = Long.MAX_VALUE;
                }
                val = Math.max(0, val);
                thresholdSlot.setText(Long.toString(val));
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdSlot) {
                coverVariable.threshold = parseTextBox(thresholdSlot);
            }

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdSlot) {
                thresholdSlot.setText(Long.toString(coverVariable.threshold));
            }
        }

        private void update() {
            invertedButton.setChecked(coverVariable.inverted);
            typeButton.setTooltipText(coverVariable.type.getTooltip());
            resetTextBox(thresholdSlot);
        }


        private long parseTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdSlot) {
                String text = box.getText();
                if (text == null) {
                    return 0;
                }
                long val;
                try {
                    val = Long.parseLong(text.trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
                return Math.max(0, val);
            }
            throw new UnsupportedOperationException("Unknown text box: " + box);
        }
    }
}
