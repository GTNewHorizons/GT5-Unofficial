package gregtech.common.gui.modularui.multiblock.godforge;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;

public class MTEMoltenModuleGui extends MTEBaseModuleGui<MTEMoltenModule> {

    public MTEMoltenModuleGui(MTEMoltenModule multiblock) {
        super(multiblock);
    }

    @Override
    public Modules<MTEMoltenModule> getModuleType() {
        return Modules.MOLTEN;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        SyncValues.MOLTEN_MODE.registerFor(getModuleType(), getMainPanel(), hypervisor);
    }

    @Override
    protected boolean usesExtraButton() {
        return true;
    }

    @Override
    protected IWidget createExtraButton() {
        BooleanSyncValue alloyModeSyncer = SyncValues.MOLTEN_MODE
            .lookupFrom(getModuleType(), getMainPanel(), hypervisor);
        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (multiblock.isAlloyModeOn()) {
                    return GTGuiTextures.TT_OVERLAY_BUTTON_FURNACE_MODE;
                }
                return GTGuiTextures.TT_OVERLAY_BUTTON_FURNACE_MODE_OFF;
            }))
            .onMousePressed(d -> {
                alloyModeSyncer.setBoolValue(!alloyModeSyncer.getValue());
                return true;
            })
            .tooltipDynamic(t -> {
                if (alloyModeSyncer.getBoolValue()) {
                    t.addLine(translateToLocal("fog.button.alloymode.tooltip.02"));
                } else {
                    t.addLine(translateToLocal("fog.button.alloymode.tooltip.01"));
                }
            })
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }
}
