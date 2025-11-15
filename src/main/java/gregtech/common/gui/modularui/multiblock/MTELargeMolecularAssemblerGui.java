package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELargeMolecularAssembler;

public class MTELargeMolecularAssemblerGui extends MTEMultiBlockBaseGui<MTELargeMolecularAssembler> {

    public MTELargeMolecularAssemblerGui(MTELargeMolecularAssembler multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        BooleanSyncValue showCraftingEffect = new BooleanSyncValue(
            multiblock::isHiddenCraftingFX,
            multiblock::setHiddenCraftingFX);
        syncManager.syncValue("iscrafing", showCraftingEffect);
        return super.createLeftPanelGapRow(parent, syncManager)
            .child(new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
                if (showCraftingEffect.getValue()) {

                    return GTGuiTextures.OVERLAY_BUTTON_LMA_ANIMATION_ON;

                } else {
                    return GTGuiTextures.OVERLAY_BUTTON_LMA_ANIMATION_OFF;
                }
            }))
                .onMousePressed((a) -> {
                    showCraftingEffect.setValue(!showCraftingEffect.getValue());
                    return true;
                })
                .tooltip(t -> t.add(StatCollector.translateToLocalFormatted("GT5U.gui.text.lma_craftingfx"))));
    }
}
