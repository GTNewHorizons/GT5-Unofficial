package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_Cover_LiquidMeter extends GT_CoverBehavior {
    private static final int INVERT_BIT = 0x00000001;
    private static final int THRESHOLD_MASK = 0x000FFFF0;
    /** {@code THRESHOLD_MASK >>> THRESHOLD_BIT_SHIFT} should exactly remove all {@code 0} bits on the right side of {@code THRESHOLD_MASK}. */
    private static final int THRESHOLD_MASK_BIT_SHIFT = 4;

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
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

            boolean inverted = (aCoverVariable & INVERT_BIT) == INVERT_BIT;
            if (inverted) {
                redstoneSignal = 15 - redstoneSignal;
            }

            long threshold = (aCoverVariable & THRESHOLD_MASK) >>> THRESHOLD_MASK_BIT_SHIFT;
            threshold *= 1_000L;
            if (threshold > 0L) {
                if (inverted && tUsed >= threshold) {
                    redstoneSignal = 0;
                } else if (!inverted && tUsed < threshold) {
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
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if ((aCoverVariable & INVERT_BIT) == INVERT_BIT) {
            aCoverVariable = aCoverVariable & ~INVERT_BIT;
            GT_Utility.sendChatToPlayer(aPlayer, trans("055","Normal"));
        } else {
            aCoverVariable = aCoverVariable | INVERT_BIT;
            GT_Utility.sendChatToPlayer(aPlayer, trans("054","Inverted"));
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
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
    public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity)  {
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private final GT_GuiIconCheckButton invertedButton;
        private final GT_GuiIntegerTextBox thresholdSlot;
        private int coverVariable;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private final String INVERTED = trans("INVERTED","Inverted");
        private final String NORMAL = trans("NORMAL","Normal");

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;
            invertedButton = new GT_GuiIconCheckButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF, INVERTED, NORMAL);
            thresholdSlot = new GT_GuiIntegerTextBox(this, 2, startX + spaceX * 0, startY + spaceY * 1 + 2, spaceX * 2 + 5, 12);
            thresholdSlot.setMaxStringLength(5);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(isInverted() ? INVERTED : NORMAL, startX + spaceX*3, 4+startY+spaceY*0, 0xFF555555);
            this.getFontRenderer().drawString(trans("222", "Fluid threshold (KL)"), startX + spaceX * 3, startY + spaceY * 1 + 4, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            thresholdSlot.setFocused(true);
        }

        @Override
        public void buttonClicked(GuiButton btn){
            if (isInverted())
                coverVariable = (coverVariable & ~INVERT_BIT);
            else
                coverVariable = (coverVariable | INVERT_BIT);

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            if (thresholdSlot.isFocused()) {
                int val = parseTextBox(thresholdSlot);

                int step = 1;
                if (isShiftKeyDown()) {
                    step *= 10;
                }
                if (isCtrlKeyDown()) {
                    step *= 100;
                }

                val += step * Integer.signum(delta);
                val = GT_Utility.clamp(val, 0, THRESHOLD_MASK >>> THRESHOLD_MASK_BIT_SHIFT);
                thresholdSlot.setText(Integer.toString(val));
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdSlot) {
                int val = parseTextBox(thresholdSlot);
                coverVariable = (coverVariable & ~THRESHOLD_MASK) | ((val << THRESHOLD_MASK_BIT_SHIFT) & THRESHOLD_MASK);
            }

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdSlot) {
                thresholdSlot.setText(Integer.toString(getThreshold()));
            }
        }

        private void update(){
            invertedButton.setChecked(isInverted());
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

                return GT_Utility.clamp(val, 0, THRESHOLD_MASK >>> THRESHOLD_MASK_BIT_SHIFT);
            }

            throw new UnsupportedOperationException("Unknown text box: " + box);
        }

    private boolean isInverted() {
        return ((coverVariable & INVERT_BIT) != 0);
        }

        private int getThreshold() {
            return (coverVariable & THRESHOLD_MASK) >>> THRESHOLD_MASK_BIT_SHIFT;
        }
    }
}
