package gregtech.common.gui;

import forestry.api.apiculture.*;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.widgets.GT_GuiSlotTooltip;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_TooltipDataCache;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GT_GUIContainer_IndustrialApiary extends GT_GUIContainerMetaTile_Machine {


    private static final String
        BATTERY_SLOT_TOOLTIP = "GT5U.machines.battery_slot.tooltip",
        UNUSED_SLOT_TOOLTIP = "GT5U.machines.unused_slot.tooltip",
        STALLED_STUTTERING_TOOLTIP = "GT5U.machines.stalled_stuttering.tooltip",
        ITEM_TRANSFER_TOOLTIP = "GT5U.machines.item_transfer.tooltip",
        POWER_SOURCE_POWER = "GT5U.machines.powersource.power",
        RETRIEVE_POLLEN_TOOLTIP = "GT5U.machines.industrialapiary.pollen.tooltip",
        CANCEL_PROCESS_TOOLTIP = "GT5U.machines.industrialapiary.cancel.tooltip",
        SPEED_TOOLTIP = "GT5U.machines.industrialapiary.speed.tooltip",
        SPEED_LOCKED_TOOLTIP = "GT5U.machines.industrialapiary.speedlocked.tooltip",
        INFO_TOOLTIP = "GT5U.machines.industrialapiary.info.tooltip",
        INFO_WITH_BEE_TOOLTIP = "GT5U.machines.industrialapiary.infoextended.tooltip"
            ;


    GT_GuiTooltip mErrorStatesTooltip;
    GT_GuiTooltip mSpeedToggleTooltip;
    GT_GuiTooltip mInfoTooltip;

    public GT_GUIContainer_IndustrialApiary(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(new GT_Container_IndustrialApiary(aInventoryPlayer, aTileEntity), "gregtech:textures/gui/basicmachines/IndustrialApiary.png");
    }

    @Override
    protected void setupTooltips() {
        Rectangle tBeeProblemArea = new Rectangle(this.guiLeft + 100, this.guiTop + 63, 18, 18);
        addToolTip(mErrorStatesTooltip = new GT_GuiTooltip(tBeeProblemArea, "") {
            @Override
            protected void onTick() {
                boolean e = this.enabled;
                super.onTick();
                this.enabled = e;
            }
        });
        addToolTip(mSpeedToggleTooltip = new GT_GuiSlotTooltip(getContainer().slotSpeedToggle, new GT_TooltipDataCache.TooltipData(null, null)));
        mErrorStatesTooltip.enabled = false;
        addToolTip(mInfoTooltip = new GT_GuiTooltip(new Rectangle(this.guiLeft + 163, guiTop + 5, 6, 17)));

        addToolTip(new GT_GuiSlotTooltip(getContainer().slotPollenToggle, mTooltipCache.getData(RETRIEVE_POLLEN_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(getContainer().slotCancelProcess, mTooltipCache.getData(CANCEL_PROCESS_TOOLTIP)));


        addToolTip(new GT_GuiSlotTooltip(getContainer().slotItemTransferToggle, mTooltipCache.getData(ITEM_TRANSFER_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(getContainer().slotBattery, mTooltipCache.getData(BATTERY_SLOT_TOOLTIP,
                powerTierName(getContainer().getMachine().mTier),
                powerTierName((byte)(getContainer().getMachine().mTier + 1)))));
        addToolTip(new GT_GuiSlotTooltip(getContainer().slotSpecial, mTooltipCache.getData(UNUSED_SLOT_TOOLTIP)));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float parTicks) {
        mErrorStatesTooltip.enabled = !getContainer().mErrorStates.isEmpty();
        if(mErrorStatesTooltip.enabled){ mErrorStatesTooltip.setToolTipText(new GT_TooltipDataCache.TooltipData(getContainer().mErrorStates, getContainer().mErrorStates)); }
        else if(getContainer().mStuttering)
        {
            mErrorStatesTooltip.enabled = true;
            mErrorStatesTooltip.setToolTipText(mTooltipCache.getData(STALLED_STUTTERING_TOOLTIP, StatCollector.translateToLocal(POWER_SOURCE_POWER)));
        }
        int accelerated = (1 << getContainer().mSpeed);
        int energyusage = 0;
        if(accelerated == 2)
            energyusage = 32;
        else if(accelerated > 2)
            energyusage = 32 * accelerated << (getContainer().mSpeed - 2);
        mSpeedToggleTooltip.setToolTipText(mTooltipCache.getUncachedTooltipData(getContainer().mLockedSpeed ? SPEED_LOCKED_TOOLTIP : SPEED_TOOLTIP, accelerated, GT_Utility.formatNumbers(energyusage)));
        ArrayList<String> s = new ArrayList<>();
        GT_MetaTileEntity_IndustrialApiary IA = getContainer().getMachine();

        String energyreq = GT_Utility.formatNumbers((int)((float)GT_MetaTileEntity_IndustrialApiary.baseEUtUsage * IA.getEnergyModifier() * accelerated) + energyusage);
        String Temp = StatCollector.translateToLocal(IA.getTemperature().getName());
        String Hum = StatCollector.translateToLocal(IA.getHumidity().getName());
        boolean moreinformationgiven = false;
        if(IA.getUsedQueen() != null && BeeManager.beeRoot.isMember(IA.getUsedQueen(), EnumBeeType.QUEEN.ordinal())) {
            IBee bee = BeeManager.beeRoot.getMember(IA.getUsedQueen());
            if (bee.isAnalyzed()) {
                moreinformationgiven = true;
                IBeeGenome genome = bee.getGenome();
                IBeeModifier mod = BeeManager.beeRoot.getBeekeepingMode(IA.getWorld()).getBeeModifier();
                float tmod = IA.getTerritoryModifier(null, 1f) * mod.getTerritoryModifier(null, 1f);
                int[] t = Arrays.stream(genome.getTerritory()).map(i -> (int) ((float) i * tmod)).toArray();
                mInfoTooltip.setToolTipText(mTooltipCache.getUncachedTooltipData(INFO_WITH_BEE_TOOLTIP, energyreq, Temp, Hum,
                    Math.round(100f * IA.getProductionModifier(null, 1f) * genome.getSpeed() * mod.getProductionModifier(null, 1f)),
                    Math.round(IA.getFloweringModifier(null, 1f) * genome.getFlowering() * mod.getFloweringModifier(null, 1f)),
                    Math.round(IA.getLifespanModifier(null, null, 1f) * genome.getLifespan() * mod.getLifespanModifier(null, null, 1f)),
                    t[0], t[1], t[2]));
            }
        }
        if(!moreinformationgiven)
            mInfoTooltip.setToolTipText(mTooltipCache.getUncachedTooltipData(INFO_TOOLTIP, energyreq, Temp, Hum));


        super.drawScreen(mouseX, mouseY, parTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRendererObj.drawString("Ind. Apiary", 8, 4, 4210752);
        this.fontRendererObj.drawString("x", 30, 63, 4210752);
        this.fontRendererObj.drawString((1 << getContainer().mSpeed) + "", 26, 72, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        if (this.mContainer != null) {
            if (getContainer().mItemTransfer)
                drawTexturedModalRect(x + 7, y + 62, 176, 18, 18, 18);
            if(getContainer().retrievePollen)
                drawTexturedModalRect(x + 7, y + 44, 194, 18, 18, 18);
            if(getContainer().mMaxProgressTime > 0) {
                double p = (double) getContainer().mProgressTime / getContainer().mMaxProgressTime;
                drawTexturedModalRect(x+70, y+3, 176, 0, (int)(p*20), 18);
            }
            if(mErrorStatesTooltip.enabled)
                drawTexturedModalRect(x+100, y+63, 176, 36, getContainer().mStuttering ? 18 : 9, 18);
        }
    }

    private GT_Container_IndustrialApiary getContainer(){
        return ((GT_Container_IndustrialApiary) this.mContainer);
    }

    // taken from GT_GUIContainer_BasicMachine
    private String powerTierName(byte machineTier) {
        return GT_Values.TIER_COLORS[machineTier] + GT_Values.VN[machineTier];
    }

}
