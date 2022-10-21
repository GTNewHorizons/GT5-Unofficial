package gregtech.common.covers;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.CoverDataControllerWidget;
import gregtech.common.gui.modularui.CoverDataFollower_ToggleButtonWidget;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_NeedMaintainance extends GT_CoverBehavior {

    /**
     * @deprecated use {@link #GT_Cover_NeedMaintainance(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_NeedMaintainance() {
        this(null);
    }

    public GT_Cover_NeedMaintainance(ITexture coverTexture) {
        super(coverTexture);
    }

    public static boolean isRotor(ItemStack rotor) {
        return (rotor != null
                && rotor.getItem() instanceof GT_MetaGenerated_Tool
                && rotor.getItemDamage() >= 170
                && rotor.getItemDamage() <= 176);
    }

    @Override
    public boolean isRedstoneSensitive(
            byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(
            byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
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
                    if (ideal - real > coverVar) needsRepair = true;
                } else if (coverVar == 5 || coverVar == 6) {
                    if (isRotor(tRotor)) {
                        long tMax = GT_MetaGenerated_Tool.getToolMaxDamage(tRotor);
                        long tCur = GT_MetaGenerated_Tool.getToolDamage(tRotor);
                        if (coverVar == 5) {
                            needsRepair = (tCur >= tMax * 8 / 10);
                        } else {
                            long mExpectedDamage = Math.round(Math.min(
                                    multi.mEUt / multi.damageFactorLow, Math.pow(multi.mEUt, multi.damageFactorHigh)));
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
    public int onCoverScrewdriverclick(
            byte aSide,
            int aCoverID,
            int aCoverVariable,
            ICoverable aTileEntity,
            EntityPlayer aPlayer,
            float aX,
            float aY,
            float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 14;
        if (aCoverVariable < 0) {
            aCoverVariable = 13;
        }
        switch (aCoverVariable) {
            case 0:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("056", "Emit if 1 Maintenance Needed"));
                break;
            case 1:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("057", "Emit if 1 Maintenance Needed(inverted)"));
                break;
            case 2:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("058", "Emit if 2 Maintenance Needed"));
                break;
            case 3:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("059", "Emit if 2 Maintenance Needed(inverted)"));
                break;
            case 4:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("060", "Emit if 3 Maintenance Needed"));
                break;
            case 5:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("061", "Emit if 3 Maintenance Needed(inverted)"));
                break;
            case 6:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("062", "Emit if 4 Maintenance Needed"));
                break;
            case 7:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("063", "Emit if 4 Maintenance Needed(inverted)"));
                break;
            case 8:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("064", "Emit if 5 Maintenance Needed"));
                break;
            case 9:
                GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("065", "Emit if 5 Maintenance Needed(inverted)"));
                break;
            case 10:
                GT_Utility.sendChatToPlayer(
                        aPlayer, GT_Utility.trans("066", "Emit if rotor needs maintenance low accuracy mod"));
                break;
            case 11:
                GT_Utility.sendChatToPlayer(
                        aPlayer, GT_Utility.trans("067", "Emit if rotor needs maintenance low accuracy mod(inverted)"));
                break;
            case 12:
                GT_Utility.sendChatToPlayer(
                        aPlayer, GT_Utility.trans("068", "Emit if rotor needs maintenance high accuracy mod"));
                break;
            case 13:
                GT_Utility.sendChatToPlayer(
                        aPlayer,
                        GT_Utility.trans("069", "Emit if rotor needs maintenance high accuracy mod(inverted)"));
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
    public boolean manipulatesSidedRedstoneOutput(
            byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 60;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    private static final int startX = 10;
    private static final int startY = 25;
    private static final int spaceX = 18;
    private static final int spaceY = 18;

    @SuppressWarnings("PointlessArithmeticExpression")
    @Override
    protected void addUIWidgets(ModularWindow.Builder builder) {
        final String[] tooltipText = {
            GT_Utility.trans("056", "Emit if 1 Maintenance Needed"),
            GT_Utility.trans("058", "Emit if 2 Maintenance Needed"),
            GT_Utility.trans("060", "Emit if 3 Maintenance Needed"),
            GT_Utility.trans("062", "Emit if 4 Maintenance Needed"),
            GT_Utility.trans("064", "Emit if 5 Maintenance Needed"),
            GT_Utility.trans("066", "Emit if rotor needs maintenance low accuracy mod"),
            GT_Utility.trans("068", "Emit if rotor needs maintenance high accuracy mod"),
        };

        final String[] buttonText = {
            GT_Utility.trans("247", "1 Issue"),
            GT_Utility.trans("248", "2 Issues"),
            GT_Utility.trans("249", "3 Issues"),
            GT_Utility.trans("250", "4 Issues"),
            GT_Utility.trans("251", "5 Issues"),
            GT_Utility.trans("252", "Rotor < 80%"),
            GT_Utility.trans("253", "Rotor < 100%"),
            GT_Utility.trans("INVERTED", "Inverted"),
            GT_Utility.trans("NORMAL", "Normal"),
        };

        builder.widget(new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                                this::getCoverData,
                                this::setCoverData,
                                this,
                                (index, coverData) -> isEnabled(index, convert(coverData)),
                                (index, coverData) -> new ISerializableObject.LegacyCoverData(
                                        getNewCoverVariable(index, convert(coverData))))
                        .addToggleButton(0, CoverDataFollower_ToggleButtonWidget.ofCheck(), widget -> widget.addTooltip(
                                        tooltipText[0])
                                .setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(1, CoverDataFollower_ToggleButtonWidget.ofCheck(), widget -> widget.addTooltip(
                                        tooltipText[1])
                                .setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(2, CoverDataFollower_ToggleButtonWidget.ofCheck(), widget -> widget.addTooltip(
                                        tooltipText[2])
                                .setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(3, CoverDataFollower_ToggleButtonWidget.ofCheck(), widget -> widget.addTooltip(
                                        tooltipText[3])
                                .setPos(spaceX * 0, spaceY * 3))
                        .addToggleButton(4, CoverDataFollower_ToggleButtonWidget.ofCheck(), widget -> widget.addTooltip(
                                        tooltipText[4])
                                .setPos(spaceX * 4 + 4, spaceY * 0))
                        .addToggleButton(5, CoverDataFollower_ToggleButtonWidget.ofCheck(), widget -> widget.addTooltip(
                                        tooltipText[5])
                                .setPos(spaceX * 4 + 4, spaceY * 1))
                        .addToggleButton(6, CoverDataFollower_ToggleButtonWidget.ofCheck(), widget -> widget.addTooltip(
                                        tooltipText[6])
                                .setPos(spaceX * 4 + 4, spaceY * 2))
                        .addToggleButton(
                                7,
                                CoverDataFollower_ToggleButtonWidget.ofRedstone(),
                                widget -> widget.setPos(spaceX * 4 + 4, spaceY * 3))
                        .setPos(startX, startY))
                .widget(new TextWidget(buttonText[0])
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(new TextWidget(buttonText[1])
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(new TextWidget(buttonText[2])
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(new TextWidget(buttonText[3])
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 3))
                .widget(new TextWidget(buttonText[4])
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 0))
                .widget(new TextWidget(buttonText[5])
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 1))
                .widget(new TextWidget(buttonText[6])
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 2))
                .widget(TextWidget.dynamicString(
                                () -> isEnabled(7, convert(getCoverData())) ? buttonText[7] : buttonText[8])
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 3));
    }

    private int getNewCoverVariable(int id, int coverVariable) {
        final boolean checked = (coverVariable & 0x1) > 0;
        if (id == 7) {
            if (checked) return coverVariable & ~0x1;
            else return coverVariable | 0x1;
        }
        return (coverVariable & 0x1) | (id << 1);
    }

    private boolean isEnabled(int id, int coverVariable) {
        if (id == 7) return (coverVariable & 0x1) > 0;
        return (coverVariable >>> 1) == id;
    }

    @Override
    public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity) {
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private int coverVariable;

        private final String[] tooltiptext = {
            GT_Utility.trans("056", "Emit if 1 Maintenance Needed"),
            GT_Utility.trans("058", "Emit if 2 Maintenance Needed"),
            GT_Utility.trans("060", "Emit if 3 Maintenance Needed"),
            GT_Utility.trans("062", "Emit if 4 Maintenance Needed"),
            GT_Utility.trans("064", "Emit if 5 Maintenance Needed"),
            GT_Utility.trans("066", "Emit if rotor needs maintenance low accuracy mod"),
            GT_Utility.trans("068", "Emit if rotor needs maintenance high accuracy mod"),
        };

        private final String[] buttontext = {
            GT_Utility.trans("247", "1 Issue"),
            GT_Utility.trans("248", "2 Issues"),
            GT_Utility.trans("249", "3 Issues"),
            GT_Utility.trans("250", "4 Issues"),
            GT_Utility.trans("251", "5 Issues"),
            GT_Utility.trans("252", "Rotor < 80%"),
            GT_Utility.trans("253", "Rotor < 100%"),
            GT_Utility.trans("INVERTED", "Inverted"),
            GT_Utility.trans("NORMAL", "Normal"),
        };

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private final int textColor = this.getTextColorOrDefault("text", 0xFF555555);

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            GuiButton b;
            b = new GT_GuiIconCheckButton(this, 0, startX + spaceX * 0, startY + spaceY * 0, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(tooltiptext[0]);
            b = new GT_GuiIconCheckButton(this, 1, startX + spaceX * 0, startY + spaceY * 1, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(tooltiptext[1]);
            b = new GT_GuiIconCheckButton(this, 2, startX + spaceX * 0, startY + spaceY * 2, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(tooltiptext[2]);
            b = new GT_GuiIconCheckButton(this, 3, startX + spaceX * 0, startY + spaceY * 3, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(tooltiptext[3]);
            b = new GT_GuiIconCheckButton(
                            this, 4, startX + spaceX * 4 + 4, startY + spaceY * 0, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(tooltiptext[4]);
            b = new GT_GuiIconCheckButton(
                            this, 5, startX + spaceX * 4 + 4, startY + spaceY * 1, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(tooltiptext[5]);
            b = new GT_GuiIconCheckButton(
                            this, 6, startX + spaceX * 4 + 4, startY + spaceY * 2, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(tooltiptext[6]);
            b = new GT_GuiIconCheckButton(
                    this,
                    7,
                    startX + spaceX * 4 + 4,
                    startY + spaceY * 3,
                    GT_GuiIcon.REDSTONE_ON,
                    GT_GuiIcon.REDSTONE_OFF);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);

            this.fontRendererObj.drawString(buttontext[0], startX + spaceX * 1, 4 + startY + spaceY * 0, textColor);
            this.fontRendererObj.drawString(buttontext[1], startX + spaceX * 1, 4 + startY + spaceY * 1, textColor);
            this.fontRendererObj.drawString(buttontext[2], startX + spaceX * 1, 4 + startY + spaceY * 2, textColor);
            this.fontRendererObj.drawString(buttontext[3], startX + spaceX * 1, 4 + startY + spaceY * 3, textColor);
            this.fontRendererObj.drawString(buttontext[4], startX + spaceX * 5 + 4, 4 + startY + spaceY * 0, textColor);
            this.fontRendererObj.drawString(buttontext[5], startX + spaceX * 5 + 4, 4 + startY + spaceY * 1, textColor);
            this.fontRendererObj.drawString(buttontext[6], startX + spaceX * 5 + 4, 4 + startY + spaceY * 2, textColor);
            //                                          inverted        normal
            String s2 = ((coverVariable & 0x1) > 0) ? buttontext[7] : buttontext[8];
            this.fontRendererObj.drawString(s2, startX + spaceX * 5 + 4, 4 + startY + spaceY * 3, textColor);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn.id == 7 || !isEnabled(btn.id)) {
                coverVariable = getNewCoverVariable(btn.id, ((GT_GuiIconCheckButton) btn).isChecked());
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons() {
            for (Object o : buttonList)
                ((GT_GuiIconCheckButton) o).setChecked(isEnabled(((GT_GuiIconCheckButton) o).id));
        }

        private int getNewCoverVariable(int id, boolean checked) {
            if (id == 7) {
                if (checked) return coverVariable & ~0x1;
                else return coverVariable | 0x1;
            }
            return (coverVariable & 0x1) | (id << 1);
        }

        private boolean isEnabled(int id) {
            if (id == 7) return (coverVariable & 0x1) > 0;
            return (coverVariable >>> 1) == id;
        }
    }
}
