package gregtech.common.gui.modularui.multiblock.godforge;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import tectech.loader.ConfigHandler;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;

public class MTEPlasmaModuleGui extends MTEBaseModuleGui<MTEPlasmaModule> {

    public MTEPlasmaModuleGui(MTEPlasmaModule multiblock) {
        super(multiblock);
    }

    @Override
    public Modules<MTEPlasmaModule> getModuleType() {
        return Modules.PLASMA;
    }

    @Override
    protected boolean usesExtraButton() {
        return true;
    }

    @Override
    protected IWidget createExtraButton() {
        IPanelHandler debugPanel = Panels.PLASMA_DEBUG.getFrom(getModuleType(), getMainPanel(), hypervisor);
        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(GTGuiTextures.TT_OVERLAY_BUTTON_LOAF_MODE)
            .onMousePressed(d -> {
                if (!debugPanel.isPanelOpen()) {
                    debugPanel.openPanel();
                } else {
                    debugPanel.closePanel();
                }
                return true;
            })
            .tooltip(t -> t.addLine(translateToLocal("tt.gui.tooltip.plasma_module.debug_window")))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
            .setEnabledIf($ -> ConfigHandler.debug.DEBUG_MODE);
    }
}
