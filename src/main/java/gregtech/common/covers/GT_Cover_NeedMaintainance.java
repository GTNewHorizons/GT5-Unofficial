package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;

public class GT_Cover_NeedMaintainance extends GT_CoverBehavior {

    public GT_Cover_NeedMaintainance(ITexture coverTexture) {
        super(coverTexture);
    }

    public static boolean isRotor(ItemStack rotor) {
        return (rotor != null && rotor.getItem() instanceof GT_MetaGenerated_Tool
            && rotor.getItemDamage() >= 170
            && rotor.getItemDamage() <= 176);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        boolean needsRepair = false;
        if (aTileEntity instanceof IGregTechTileEntity tTileEntity) {
            final IMetaTileEntity mTileEntity = tTileEntity.getMetaTileEntity();
            if (mTileEntity instanceof GT_MetaTileEntity_MultiBlockBase multi) {
                final int ideal = multi.getIdealStatus();
                final int real = multi.getRepairStatus();
                final ItemStack tRotor = multi.getRealInventory()[1];
                final int coverVar = aCoverVariable >>> 1;
                if (coverVar < 5) {
                    if (ideal - real > coverVar) needsRepair = true;
                } else if (coverVar == 5 || coverVar == 6) {
                    if (isRotor(tRotor)) {
                        long tMax = GT_MetaGenerated_Tool.getToolMaxDamage(tRotor);
                        long tCur = GT_MetaGenerated_Tool.getToolDamage(tRotor);
                        if (coverVar == 5) {
                            needsRepair = (tCur >= tMax * 8 / 10);
                        } else {
                            long mExpectedDamage = Math.round(
                                Math.min(
                                    multi.mEUt / multi.damageFactorLow,
                                    Math.pow(multi.mEUt, multi.damageFactorHigh)));
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

        aTileEntity.setOutputRedstoneSignal(side, (byte) (needsRepair ? 0 : 15));
        aTileEntity.setOutputRedstoneSignal(side.getOpposite(), (byte) (needsRepair ? 0 : 15));
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 14;
        if (aCoverVariable < 0) {
            aCoverVariable = 13;
        }
        switch (aCoverVariable) {
            case 0 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("056", "Emit if 1 Maintenance Needed"));
            case 1 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("057", "Emit if 1 Maintenance Needed(inverted)"));
            case 2 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("058", "Emit if 2 Maintenance Needed"));
            case 3 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("059", "Emit if 2 Maintenance Needed(inverted)"));
            case 4 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("060", "Emit if 3 Maintenance Needed"));
            case 5 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("061", "Emit if 3 Maintenance Needed(inverted)"));
            case 6 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("062", "Emit if 4 Maintenance Needed"));
            case 7 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("063", "Emit if 4 Maintenance Needed(inverted)"));
            case 8 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("064", "Emit if 5 Maintenance Needed"));
            case 9 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("065", "Emit if 5 Maintenance Needed(inverted)"));
            case 10 -> GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("066", "Emit if rotor needs maintenance low accuracy mod"));
            case 11 -> GT_Utility.sendChatToPlayer(
                aPlayer,
                GT_Utility.trans("067", "Emit if rotor needs maintenance low accuracy mod(inverted)"));
            case 12 -> GT_Utility.sendChatToPlayer(
                aPlayer,
                GT_Utility.trans("068", "Emit if rotor needs maintenance high accuracy mod"));
            case 13 -> GT_Utility.sendChatToPlayer(
                aPlayer,
                GT_Utility.trans("069", "Emit if rotor needs maintenance high accuracy mod(inverted)"));
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection side, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
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

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new NeedMaintainanceUIFactory(buildContext).createWindow();
    }

    private class NeedMaintainanceUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public NeedMaintainanceUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            final String[] tooltipText = { GT_Utility.trans("056", "Emit if 1 Maintenance Needed"),
                GT_Utility.trans("058", "Emit if 2 Maintenance Needed"),
                GT_Utility.trans("060", "Emit if 3 Maintenance Needed"),
                GT_Utility.trans("062", "Emit if 4 Maintenance Needed"),
                GT_Utility.trans("064", "Emit if 5 Maintenance Needed"),
                GT_Utility.trans("066", "Emit if rotor needs maintenance low accuracy mod"),
                GT_Utility.trans("068", "Emit if rotor needs maintenance high accuracy mod"), };

            final String[] buttonText = { GT_Utility.trans("247", "1 Issue"), GT_Utility.trans("248", "2 Issues"),
                GT_Utility.trans("249", "3 Issues"), GT_Utility.trans("250", "4 Issues"),
                GT_Utility.trans("251", "5 Issues"), GT_Utility.trans("252", "Rotor < 20%"),
                GT_Utility.trans("253", "Rotor ≈ 0%"), GT_Utility.trans("INVERTED", "Inverted"),
                GT_Utility.trans("NORMAL", "Normal"), };

            builder
                .widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                        this::getCoverData,
                        this::setCoverData,
                        GT_Cover_NeedMaintainance.this,
                        (index, coverData) -> isEnabled(index, convert(coverData)),
                        (index, coverData) -> new ISerializableObject.LegacyCoverData(
                            getNewCoverVariable(index, convert(coverData))))
                                .addToggleButton(
                                    0,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.addTooltip(tooltipText[0])
                                        .setPos(spaceX * 0, spaceY * 0))
                                .addToggleButton(
                                    1,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.addTooltip(tooltipText[1])
                                        .setPos(spaceX * 0, spaceY * 1))
                                .addToggleButton(
                                    2,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.addTooltip(tooltipText[2])
                                        .setPos(spaceX * 0, spaceY * 2))
                                .addToggleButton(
                                    3,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.addTooltip(tooltipText[3])
                                        .setPos(spaceX * 0, spaceY * 3))
                                .addToggleButton(
                                    4,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.addTooltip(tooltipText[4])
                                        .setPos(spaceX * 4 + 4, spaceY * 0))
                                .addToggleButton(
                                    5,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.addTooltip(tooltipText[5])
                                        .setPos(spaceX * 4 + 4, spaceY * 1))
                                .addToggleButton(
                                    6,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.addTooltip(tooltipText[6])
                                        .setPos(spaceX * 4 + 4, spaceY * 2))
                                .addToggleButton(
                                    7,
                                    CoverDataFollower_ToggleButtonWidget.ofRedstone(),
                                    widget -> widget.setPos(spaceX * 4 + 4, spaceY * 3))
                                .setPos(startX, startY))
                .widget(
                    new TextWidget(buttonText[0]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(buttonText[1]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(buttonText[2]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(buttonText[3]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 3))
                .widget(
                    new TextWidget(buttonText[4]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(buttonText[5]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(buttonText[6]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5 + 4, 4 + startY + spaceY * 2))
                .widget(
                    TextWidget
                        .dynamicString(() -> isEnabled(7, convert(getCoverData())) ? buttonText[7] : buttonText[8])
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
    }
}
