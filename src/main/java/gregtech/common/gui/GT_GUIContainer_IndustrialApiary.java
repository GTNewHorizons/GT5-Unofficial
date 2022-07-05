package gregtech.common.gui;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.gui.widgets.GT_GuiSlotTooltip;
import gregtech.api.gui.widgets.GT_GuiTooltip;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_TooltipDataCache;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_IndustrialApiary;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GT_GUIContainer_IndustrialApiary extends GT_GUIContainerMetaTile_Machine {

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

        addToolTip(new GT_GuiSlotTooltip(getContainer().slotPollenToggle, new GT_TooltipDataCache.TooltipData(Arrays.asList("Retrieve pollen", EnumChatFormatting.RED + "WARNING: INCREASES LAG"), null)));

        addToolTip(new GT_GuiSlotTooltip(getContainer().slotItemTransferToggle,
            mTooltipCache.getData("GT5U.machines.item_transfer.tooltip")));
        addToolTip( new GT_GuiSlotTooltip(getContainer().slotBattery,
            mTooltipCache.getData("GT5U.machines.battery_slot.tooltip",
                powerTierName(getContainer().getMachine().mTier),
                powerTierName((byte)(getContainer().getMachine().mTier + 1)))));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float parTicks) {
        mErrorStatesTooltip.enabled = !getContainer().mErrorStates.isEmpty();
        if(mErrorStatesTooltip.enabled){ mErrorStatesTooltip.setToolTipText(new GT_TooltipDataCache.TooltipData(getContainer().mErrorStates, null)); }
        else if(getContainer().mStuttering)
        {
            mErrorStatesTooltip.enabled = true;
            mErrorStatesTooltip.setToolTipText(mTooltipCache.getData("GT5U.machines.stalled_stuttering.tooltip", StatCollector.translateToLocal("GT5U.machines.powersource.power")));
        }
        int accelerated = (1 << getContainer().mSpeed);
        int energyusage = 0;
        if(accelerated == 2)
            energyusage = 32;
        else if(accelerated > 2)
            energyusage = 32 * accelerated << (getContainer().mSpeed - 2);
        mSpeedToggleTooltip.setToolTipText("Acceleration: " + accelerated + "x", "Energy usage: +" + GT_Utility.formatNumbers(energyusage) + " EU/t");

        ArrayList<String> s = new ArrayList<>();
        GT_MetaTileEntity_IndustrialApiary IA = getContainer().getMachine();

        s.add("Energy required: " + GT_Utility.formatNumbers((int)((float)GT_MetaTileEntity_IndustrialApiary.baseEUtUsage * IA.getEnergyModifier()) + energyusage) + " EU/t");
        s.add("Temperature: " + StatCollector.translateToLocal(IA.getTemperature().getName()));
        s.add("Humidity: " + StatCollector.translateToLocal(IA.getHumidity().getName()));
        if(IA.getUsedQueen() != null)
        {
            IBee bee = BeeManager.beeRoot.getMember(IA.getUsedQueen());
            IBeeGenome genome = bee.getGenome();

            s.add("Production Modifier: " + Math.round(100f * IA.getProductionModifier(null, 1f) * genome.getSpeed()) + "%");
            s.add("Flowering Chance: " + Math.round(IA.getFloweringModifier(null, 1f) * genome.getFlowering()) + "%");
            s.add("Lifespan Modifier: " + Math.round(IA.getLifespanModifier(null, null, 1f) * genome.getLifespan()) + "%");
            float tmod = IA.getTerritoryModifier(null, 1f);
            int[] t = Arrays.stream(genome.getTerritory()).map(i->(int)((float)i*tmod)).toArray();
            s.add("Terrority: " + t[0] + " x " + t[1] + " x " + t[2]);
        }


        mInfoTooltip.setToolTipText(new GT_TooltipDataCache.TooltipData(s,null));


        super.drawScreen(mouseX, mouseY, parTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRendererObj.drawString("Ind. Apiary", 8, 4, 4210752);
        this.fontRendererObj.drawString("x", 30, 63, 4210752);
        this.fontRendererObj.drawString((1 << getContainer().mSpeed) + "", 25, 72, 4210752);
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
