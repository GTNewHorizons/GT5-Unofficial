package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.tileentity.ICoverable;
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
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class GT_Cover_LiquidMeter extends GT_CoverBehaviorBase<GT_Cover_LiquidMeter.LiquidMeterData> {

    public GT_Cover_LiquidMeter() {
        super(LiquidMeterData.class);
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
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected LiquidMeterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if ((aTileEntity instanceof IFluidHandler)) {
            FluidTankInfo[] tTanks = ((IFluidHandler) aTileEntity).getTankInfo(ForgeDirection.UNKNOWN);
            long tMax = 0;
            long tUsed = 0;
            if (tTanks != null) {
                for (FluidTankInfo tTank : tTanks) {
                    if (tTank != null) {
                        tMax += tTank.capacity;
                        FluidStack tLiquid = tTank.fluid;
                        if (tLiquid != null) {
                            tUsed += tLiquid.amount;
                        }
                    }
                }
            }

            long redstoneSignal;
            if (tUsed == 0L) {
                // nothing
                redstoneSignal = 0;
            } else if (tUsed >= tMax) {
                // full
                redstoneSignal = 15;
            } else {
                // 1-14 range
                redstoneSignal = 1 + (14 * tUsed) / tMax;
            }

            if (aCoverVariable.inverted) {
                redstoneSignal = 15 - redstoneSignal;
            }

            if (aCoverVariable.threshold > 0) {
                if (aCoverVariable.inverted && tUsed >= aCoverVariable.threshold) {
                    redstoneSignal = 0;
                } else if (!aCoverVariable.inverted && tUsed < aCoverVariable.threshold) {
                    redstoneSignal = 0;
                }
            }

            aTileEntity.setOutputRedstoneSignal(aSide, (byte) redstoneSignal);
        } else {
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)0);
        }
        return aCoverVariable;
    }

    @Override
    protected LiquidMeterData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aCoverVariable.inverted) {
            aCoverVariable.inverted = false;
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("055", "Normal"));
        } else {
            aCoverVariable.inverted = true;
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("054", "Inverted"));
        }
        return aCoverVariable;
    }

    @Override
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }
    /**
     * GUI Stuff
     */

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public Object getClientGUIImpl(byte aSide, int aCoverID, LiquidMeterData coverData, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    public static class LiquidMeterData implements ISerializableObject {
        private boolean inverted;
        /** The special value {@code 0} means threshold check is disabled. */
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

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private final GT_GuiIconCheckButton invertedButton;
        private final GT_GuiIntegerTextBox thresholdSlot;
        private final LiquidMeterData coverVariable;

        private final int maxCapacity;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
        private final String NORMAL = GT_Utility.trans("NORMAL", "Normal");

        public GUI(byte aSide, int aCoverID, LiquidMeterData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            invertedButton = new GT_GuiIconCheckButton(this, 0, startX + spaceX * 0, startY + spaceY * 0, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF, INVERTED, NORMAL);
            thresholdSlot = new GT_GuiIntegerTextBox(this, 2, startX + spaceX * 0, startY + spaceY * 1 + 2, spaceX * 4 + 5, 12);

            if (tile instanceof IFluidHandler) {
                FluidTankInfo[] tanks = ((IFluidHandler) tile).getTankInfo(ForgeDirection.UNKNOWN);
                maxCapacity =
                        Arrays.stream(tanks)
                                .mapToInt(tank -> tank.capacity)
                                .sum();
            } else {
                maxCapacity = -1;
            }
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(coverVariable.inverted ? INVERTED : NORMAL, startX + spaceX * 1, 4 + startY + spaceY * 0, 0xFF555555);
            this.getFontRenderer().drawString(GT_Utility.trans("222", "Fluid threshold"), startX + spaceX * 5 - 10, startY + spaceY * 1 + 4, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            thresholdSlot.setFocused(true);
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            coverVariable.inverted = !coverVariable.inverted;
            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            if (thresholdSlot.isFocused()) {
                int val = parseTextBox(thresholdSlot);

                int step = 1000;
                if (isShiftKeyDown()) {
                    step *= 100;
                }
                if (isCtrlKeyDown()) {
                    step /= 10;
                }

                val += step * Integer.signum(delta);
                int upperBound = maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE;
                val = GT_Utility.clamp(val, 0, upperBound);
                thresholdSlot.setText(Integer.toString(val));
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
                thresholdSlot.setText(Integer.toString(coverVariable.threshold));
            }
        }

        private void update() {
            invertedButton.setChecked(coverVariable.inverted);
            resetTextBox(thresholdSlot);
        }

        private int parseTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdSlot) {
                String text = box.getText();
                if (text == null) {
                    return 0;
                }

                int val;
                try {
                    val = Integer.parseInt(text.trim());
                } catch (NumberFormatException e) {
                    return 0;
                }

                int upperBound = maxCapacity > 0 ? maxCapacity : Integer.MAX_VALUE;
                return GT_Utility.clamp(val, 0, upperBound);
            }

            throw new UnsupportedOperationException("Unknown text box: " + box);
        }
    }
}
