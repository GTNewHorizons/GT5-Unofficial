package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_NeedMaintainance extends GT_CoverBehavior {

    private boolean isRotor(ItemStack aRotor) {
        return !(aRotor == null || !(aRotor.getItem() instanceof GT_MetaGenerated_Tool) || aRotor.getItemDamage() < 170 || aRotor.getItemDamage() > 176);
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        boolean needsRepair = false;
        if (aTileEntity instanceof IGregTechTileEntity) {
            IGregTechTileEntity tTileEntity = (IGregTechTileEntity) aTileEntity;
            IMetaTileEntity mTileEntity = tTileEntity.getMetaTileEntity();
            if (mTileEntity instanceof GT_MetaTileEntity_MultiBlockBase) {
                GT_MetaTileEntity_MultiBlockBase multi = (GT_MetaTileEntity_MultiBlockBase) mTileEntity;
                int ideal = multi.getIdealStatus();
                int real = multi.getRepairStatus();
                ItemStack tRotor = multi.getRealInventory()[1];
                int coverVar = aCoverVariable >>> 1;
                if (coverVar < 5) {
                    if (ideal - real > coverVar)
                        needsRepair = true;
                } else if (coverVar == 5 || coverVar == 6) {
                    if (isRotor(tRotor)) {
                        long tMax = GT_MetaGenerated_Tool.getToolMaxDamage(tRotor);
                        long tCur = GT_MetaGenerated_Tool.getToolDamage(tRotor);
                        if (coverVar == 5) {
                            needsRepair = (tCur >= tMax * 8 / 10);
                        } else {
                            long mExpectedDamage = Math.round(Math.min(multi.mEUt / multi.damageFactorLow, Math.pow(multi.mEUt, multi.damageFactorHigh)));
                            needsRepair = tCur + mExpectedDamage * 2 >= tMax;
                        }
                    } else {
                        needsRepair = true;
                    }
                }
            }
        }
        if (aCoverVariable % 2 == 0) {
            needsRepair = !needsRepair;
        }

        aTileEntity.setOutputRedstoneSignal(aSide, (byte) (needsRepair ? 0 : 15));
        aTileEntity.setOutputRedstoneSignal(GT_Utility.getOppositeSide(aSide), (byte) (needsRepair ? 0 : 15));
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 14;
        if (aCoverVariable < 0) {
            aCoverVariable = 13;
        }
        switch (aCoverVariable) {
            case 0:
                GT_Utility.sendChatToPlayer(aPlayer, trans("056", "Emit if 1 Maintenance Needed"));
                break;
            case 1:
                GT_Utility.sendChatToPlayer(aPlayer, trans("057", "Emit if 1 Maintenance Needed(inverted)"));
                break;
            case 2:
                GT_Utility.sendChatToPlayer(aPlayer, trans("058", "Emit if 2 Maintenance Needed"));
                break;
            case 3:
                GT_Utility.sendChatToPlayer(aPlayer, trans("059", "Emit if 2 Maintenance Needed(inverted)"));
                break;
            case 4:
                GT_Utility.sendChatToPlayer(aPlayer, trans("060", "Emit if 3 Maintenance Needed"));
                break;
            case 5:
                GT_Utility.sendChatToPlayer(aPlayer, trans("061", "Emit if 3 Maintenance Needed(inverted)"));
                break;
            case 6:
                GT_Utility.sendChatToPlayer(aPlayer, trans("062", "Emit if 4 Maintenance Needed"));
                break;
            case 7:
                GT_Utility.sendChatToPlayer(aPlayer, trans("063", "Emit if 4 Maintenance Needed(inverted)"));
                break;
            case 8:
                GT_Utility.sendChatToPlayer(aPlayer, trans("064", "Emit if 5 Maintenance Needed"));
                break;
            case 9:
                GT_Utility.sendChatToPlayer(aPlayer, trans("065", "Emit if 5 Maintenance Needed(inverted)"));
                break;
            case 10:
                GT_Utility.sendChatToPlayer(aPlayer, trans("066", "Emit if rotor needs maintenance low accuracy mod"));
                break;
            case 11:
                GT_Utility.sendChatToPlayer(aPlayer, trans("067", "Emit if rotor needs maintenance low accuracy mod(inverted)"));
                break;
            case 12:
                GT_Utility.sendChatToPlayer(aPlayer, trans("068", "Emit if rotor needs maintenance high accuracy mod"));
                break;
            case 13:
                GT_Utility.sendChatToPlayer(aPlayer, trans("069", "Emit if rotor needs maintenance high accuracy mod(inverted)"));
                break;
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
        return 60;
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
        private int coverVariable;

        private final String[] tooltiptext = {
                trans("056", "Emit if 1 Maintenance Needed"),
                trans("058", "Emit if 2 Maintenance Needed"),
                trans("060", "Emit if 3 Maintenance Needed"),
                trans("062", "Emit if 4 Maintenance Needed"),
                trans("064", "Emit if 5 Maintenance Needed"),
                trans("066", "Emit if rotor needs maintenance low accuracy mod"),
                trans("068", "Emit if rotor needs maintenance high accuracy mod"),
        };

        private final String[] buttontext = {
                trans("247", "1 Issue"),
                trans("248", "2 Issues"),
                trans("249", "3 Issues"),
                trans("250", "4 Issues"),
                trans("251", "5 Issues"),
                trans("252", "Rotor < 80%"),
                trans("253", "Rotor < 100%"),

                trans("INVERTED","Inverted"),
                trans("NORMAL","Normal"),
        };

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            GuiButton b;
            b = new GT_GuiIconCheckButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.CHECKMARK, null).setTooltipText(tooltiptext[0]);
            b = new GT_GuiIconCheckButton(this, 1, startX + spaceX*0, startY+spaceY*1, GT_GuiIcon.CHECKMARK, null).setTooltipText(tooltiptext[1]);
            b = new GT_GuiIconCheckButton(this, 2, startX + spaceX*0, startY+spaceY*2, GT_GuiIcon.CHECKMARK, null).setTooltipText(tooltiptext[2]);
            b = new GT_GuiIconCheckButton(this, 3, startX + spaceX*0, startY+spaceY*3, GT_GuiIcon.CHECKMARK, null).setTooltipText(tooltiptext[3]);
            b = new GT_GuiIconCheckButton(this, 4, startX + spaceX*4 + 4, startY+spaceY*0, GT_GuiIcon.CHECKMARK, null).setTooltipText(tooltiptext[4]);
            b = new GT_GuiIconCheckButton(this, 5, startX + spaceX*4 + 4, startY+spaceY*1, GT_GuiIcon.CHECKMARK, null).setTooltipText(tooltiptext[5]);
            b = new GT_GuiIconCheckButton(this, 6, startX + spaceX*4 + 4, startY+spaceY*2, GT_GuiIcon.CHECKMARK, null).setTooltipText(tooltiptext[6]);
            b = new GT_GuiIconCheckButton(this, 7, startX + spaceX*4 + 4, startY+spaceY*3, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF);
        }


        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);

            this.fontRendererObj.drawString(buttontext[0],startX + spaceX*1, 4+startY+spaceY*0, 0xFF555555);
            this.fontRendererObj.drawString(buttontext[1],startX + spaceX*1, 4+startY+spaceY*1, 0xFF555555);
            this.fontRendererObj.drawString(buttontext[2],startX + spaceX*1, 4+startY+spaceY*2, 0xFF555555);
            this.fontRendererObj.drawString(buttontext[3],startX + spaceX*1, 4+startY+spaceY*3, 0xFF555555);
            this.fontRendererObj.drawString(buttontext[4],startX + spaceX*5 + 4, 4+startY+spaceY*0, 0xFF555555);
            this.fontRendererObj.drawString(buttontext[5],startX + spaceX*5 + 4, 4+startY+spaceY*1, 0xFF555555);
            this.fontRendererObj.drawString(buttontext[6],startX + spaceX*5 + 4, 4+startY+spaceY*2, 0xFF555555);
            //                                          inverted        normal
            String s2 = ((coverVariable & 0x1) > 0) ? buttontext[7] : buttontext[8];
            this.fontRendererObj.drawString(s2,  startX + spaceX*5 + 4, 4+startY+spaceY*3, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn){
            if (btn.id == 7 || !isEnabled(btn.id)){
                coverVariable = getNewCoverVariable(btn.id, ((GT_GuiIconCheckButton) btn).isChecked());
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons(){
            for (Object o : buttonList)
                ((GT_GuiIconCheckButton) o).setChecked(isEnabled(((GT_GuiIconCheckButton) o).id));
        }

        private int getNewCoverVariable(int id, boolean checked) {
            if (id == 7) {
                if (checked)
                    return coverVariable & ~0x1;
                else
                    return coverVariable | 0x1;
            }
            return (coverVariable & 0x1) | (id << 1) ;
        }

        private boolean isEnabled(int id) {
            if (id == 7)
                return (coverVariable & 0x1) > 0;
            return (coverVariable >>> 1) == id;
        }
    }
}
