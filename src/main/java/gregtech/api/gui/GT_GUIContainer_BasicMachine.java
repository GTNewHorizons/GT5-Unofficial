package gregtech.api.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import java.awt.Rectangle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiSlotTooltip;
import gregtech.api.gui.widgets.GT_GuiSmartTooltip;
import gregtech.api.gui.widgets.GT_GuiSmartTooltip.TooltipVisibilityProvider;
import gregtech.api.gui.widgets.GT_GuiTabLine.GT_GuiTabIconSet;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_Steel;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.common.power.Power;
import gregtech.nei.NEI_TransferRectHost;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my Basic Machines
 */
public class GT_GUIContainer_BasicMachine extends GT_GUIContainerMetaTile_Machine implements NEI_TransferRectHost {

    private static final int NEEDS_STEAM_VENTING = 64;
    private static final GT_GuiTabIconSet TAB_ICONSET_BRONZE = new GT_GuiTabIconSet(
            GT_GuiIcon.TAB_NORMAL_BRONZE,
            GT_GuiIcon.TAB_HIGHLIGHT_BRONZE,
            GT_GuiIcon.TAB_DISABLED_BRONZE);
    private static final GT_GuiTabIconSet TAB_ICONSET_STEEL = new GT_GuiTabIconSet(
            GT_GuiIcon.TAB_NORMAL_STEEL,
            GT_GuiIcon.TAB_HIGHLIGHT_STEEL,
            GT_GuiIcon.TAB_DISABLED_STEEL);
    private final int textColor = this.getTextColorOrDefault("title", 0x404040);
    public final String mName, mNEI;
    public final byte mProgressBarDirection, mProgressBarAmount;

    // Tooltip localization keys
    private static final String BATTERY_SLOT_TOOLTIP = "GT5U.machines.battery_slot.tooltip",
            BATTERY_SLOT_TOOLTIP_ALT = "GT5U.machines.battery_slot.tooltip.alternative",
            UNUSED_SLOT_TOOLTIP = "GT5U.machines.unused_slot.tooltip",
            SPECIAL_SLOT_TOOLTIP = "GT5U.machines.special_slot.tooltip",
            FLUID_INPUT_TOOLTIP = "GT5U.machines.fluid_input_slot.tooltip",
            FLUID_OUTPUT_TOOLTIP = "GT5U.machines.fluid_output_slot.tooltip",
            STALLED_STUTTERING_TOOLTIP = "GT5U.machines.stalled_stuttering.tooltip",
            STALLED_VENT_TOOLTIP = "GT5U.machines.stalled_vent.tooltip",
            FLUID_TRANSFER_TOOLTIP = "GT5U.machines.fluid_transfer.tooltip",
            ITEM_TRANSFER_TOOLTIP = "GT5U.machines.item_transfer.tooltip",
            POWER_SOURCE_KEY = "GT5U.machines.powersource.";

    public GT_GUIContainer_BasicMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName,
            String aTextureFile, String aNEI) {
        this(aInventoryPlayer, aTileEntity, aName, aTextureFile, aNEI, (byte) 0, (byte) 1);
    }

    public GT_GUIContainer_BasicMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName,
            String aTextureFile, String aNEI, byte aProgressBarDirection, byte aProgressBarAmount) {
        super(
                new GT_Container_BasicMachine(aInventoryPlayer, aTileEntity),
                RES_PATH_GUI + "basicmachines/" + aTextureFile);
        mProgressBarDirection = aProgressBarDirection;
        mProgressBarAmount = (byte) Math.max(1, aProgressBarAmount);
        mName = aName;
        mNEI = aNEI;
    }

    /**
     * Load data for and create appropriate tooltips for this machine
     */
    @Override
    protected void setupTooltips() {
        super.setupTooltips();
        GT_MetaTileEntity_BasicMachine machine = getMachine();
        GT_Recipe_Map recipes = machine.getRecipeList();
        GT_Container_BasicMachine container = getContainer();
        Rectangle tProblemArea = new Rectangle(this.guiLeft + 79, this.guiTop + 44, 18, 18);
        String batterySlotTooltipKey;
        Object[] batterySlotTooltipArgs;
        if (machine.isSteampowered()) {
            batterySlotTooltipKey = UNUSED_SLOT_TOOLTIP;
            batterySlotTooltipArgs = new String[0];
            addToolTip(new GT_GuiSmartTooltip(tProblemArea, new TooltipVisibilityProvider() {

                public boolean shouldShowTooltip() {
                    return hasErrorCode(NEEDS_STEAM_VENTING);
                }
            }, mTooltipCache.getData(STALLED_VENT_TOOLTIP)));
        } else {
            String pTier1 = powerTierName(machine.mTier);
            if (machine.mTier == GT_Values.VN.length - 1) {
                batterySlotTooltipKey = BATTERY_SLOT_TOOLTIP_ALT;
                batterySlotTooltipArgs = new String[] { pTier1 };
            } else {
                batterySlotTooltipKey = BATTERY_SLOT_TOOLTIP;
                batterySlotTooltipArgs = new String[] { pTier1, powerTierName((byte) (machine.mTier + 1)) };
            }
            addToolTip(
                    new GT_GuiSlotTooltip(
                            container.slotFluidTransferToggle,
                            mTooltipCache.getData(FLUID_TRANSFER_TOOLTIP)));
            addToolTip(
                    new GT_GuiSlotTooltip(
                            container.slotItemTransferToggle,
                            mTooltipCache.getData(ITEM_TRANSFER_TOOLTIP)));
        }
        if (recipes != null && recipes.hasFluidInputs()) {
            addToolTip(
                    new GT_GuiSlotTooltip(
                            container.slotFluidInput,
                            mTooltipCache.getData(FLUID_INPUT_TOOLTIP, machine.getCapacity())));
        }
        if (recipes != null && recipes.hasFluidOutputs()) {
            addToolTip(
                    new GT_GuiSlotTooltip(
                            container.slotFluidOutput,
                            mTooltipCache.getData(FLUID_OUTPUT_TOOLTIP, machine.getCapacity())));
        }
        addToolTip(
                new GT_GuiSlotTooltip(
                        getContainer().slotBattery,
                        mTooltipCache.getData(batterySlotTooltipKey, batterySlotTooltipArgs)));
        addToolTip(
                new GT_GuiSlotTooltip(
                        container.slotSpecial,
                        mTooltipCache.getData(
                                recipes != null && recipes.usesSpecialSlot() ? SPECIAL_SLOT_TOOLTIP
                                        : UNUSED_SLOT_TOOLTIP)));
        addToolTip(new GT_GuiSmartTooltip(tProblemArea, new TooltipVisibilityProvider() {

            public boolean shouldShowTooltip() {
                return container.mStuttering && !hasErrorCode(NEEDS_STEAM_VENTING);
            }
        },
                mTooltipCache.getData(
                        STALLED_STUTTERING_TOOLTIP,
                        StatCollector.translateToLocal(
                                POWER_SOURCE_KEY + (machine.isSteampowered() ? "steam" : "power")))));
    }

    /**
     * Apply proper coloration to a machine's power tier short name
     * 
     * @param machineTier
     * @return colored power tier short name
     */
    private String powerTierName(byte machineTier) {
        return GT_Values.TIER_COLORS[machineTier] + GT_Values.VN[machineTier];
    }

    private GT_MetaTileEntity_BasicMachine getMachine() {
        return (GT_MetaTileEntity_BasicMachine) mContainer.mTileEntity.getMetaTileEntity();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRendererObj.drawString(mName, 8, 4, textColor);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (mContainer != null) {
            if (!getMachine().isSteampowered()) {
                if (getContainer().mFluidTransfer) drawTexturedModalRect(x + 7, y + 62, 176, 18, 18, 18);
                if (getContainer().mItemTransfer) drawTexturedModalRect(x + 25, y + 62, 176, 36, 18, 18);
            }
            if (getContainer().mStuttering) drawTexturedModalRect(x + 79, y + 44, 176, 54, 18, 18);

            if (mContainer.mMaxProgressTime > 0) {
                int tSize = mProgressBarDirection < 2 ? 20 : 18;
                int tProgress = Math.max(
                        1,
                        Math.min(
                                tSize * mProgressBarAmount,
                                (mContainer.mProgressTime > 0 ? 1 : 0) + mContainer.mProgressTime * tSize
                                        * mProgressBarAmount
                                        / mContainer.mMaxProgressTime))
                        % (tSize + 1);

                switch (mProgressBarDirection) { // yes, my OCD was mad at me before I did the Tabs.
                    case 0:
                        drawTexturedModalRect(x + 78, y + 24, 176, 0, tProgress, 18);
                        break;
                    case 1:
                        drawTexturedModalRect(x + 78 + 20 - tProgress, y + 24, 176 + 20 - tProgress, 0, tProgress, 18);
                        break;
                    case 2:
                        drawTexturedModalRect(x + 78, y + 24, 176, 0, 20, tProgress);
                        break;
                    case 3:
                        drawTexturedModalRect(x + 78, y + 24 + 18 - tProgress, 176, 18 - tProgress, 20, tProgress);
                        break;
                    case 4:
                        tProgress = 20 - tProgress;
                        drawTexturedModalRect(x + 78, y + 24, 176, 0, tProgress, 18);
                        break;
                    case 5:
                        tProgress = 20 - tProgress;
                        drawTexturedModalRect(x + 78 + 20 - tProgress, y + 24, 176 + 20 - tProgress, 0, tProgress, 18);
                        break;
                    case 6:
                        tProgress = 18 - tProgress;
                        drawTexturedModalRect(x + 78, y + 24, 176, 0, 20, tProgress);
                        break;
                    case 7:
                        tProgress = 18 - tProgress;
                        drawTexturedModalRect(x + 78, y + 24 + 18 - tProgress, 176, 18 - tProgress, 20, tProgress);
                        break;
                }
            }
        }
    }

    @Override
    protected GT_GuiTabIconSet getTabBackground() {
        if (getMachine().isSteampowered()) {
            return getMachine() instanceof GT_MetaTileEntity_BasicMachine_Steel ? TAB_ICONSET_STEEL
                    : TAB_ICONSET_BRONZE;
        }
        return super.getTabBackground();
    }

    /**
     * Whether the machine currently has this error code
     */
    private boolean hasErrorCode(int errorCode) {
        return (getContainer().mDisplayErrorCode & errorCode) != 0;
    }

    private GT_Container_BasicMachine getContainer() {
        return (GT_Container_BasicMachine) mContainer;
    }

    @Override
    public String getNeiTransferRectString() {
        return mNEI;
    }

    @Override
    public String getNeiTransferRectTooltip() {
        Power powerInfo = getMachine().getPower();
        if (getMachine().isSteampowered()) {
            return powerInfo.getTierString() + " Steam recipes";
        } else {
            return "Recipes available in " + powerInfo.getTierString();
        }
    }

    @Override
    public Object[] getNeiTransferRectArgs() {
        return new Object[] { getMachine().getPower() };
    }

    @Override
    public Rectangle getNeiTransferRect() {
        return new Rectangle(65, 13, 36, 18);
    }
}
