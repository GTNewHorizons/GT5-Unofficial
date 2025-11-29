package gregtech.common.gui.modularui.multiblock.godforge;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;

public class MTEExoticModuleGui extends MTEBaseModuleGui<MTEExoticModule> {

    public MTEExoticModuleGui(MTEExoticModule multiblock) {
        super(multiblock);
    }

    @Override
    public Modules<MTEExoticModule> getModuleType() {
        return Modules.EXOTIC;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        SyncValues.MAGMATTER_CAPABLE.registerFor(getModuleType(), getMainPanel(), hypervisor);
        SyncValues.MAGMATTER_MODE.registerFor(getModuleType(), getMainPanel(), hypervisor);
    }

    @Override
    protected boolean usesExtraButton() {
        return true;
    }

    @Override
    protected IWidget createExtraButton() {
        BooleanSyncValue magmatterCapable = SyncValues.MAGMATTER_CAPABLE
            .lookupFrom(getModuleType(), getMainPanel(), hypervisor);
        BooleanSyncValue magmatterMode = SyncValues.MAGMATTER_MODE
            .lookupFrom(getModuleType(), getMainPanel(), hypervisor);

        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (magmatterMode.getBoolValue()) {
                    return new ItemDrawable(GTOreDictUnificator.get(OrePrefixes.dust, Materials.MagMatter, 1));
                }
                return new ItemDrawable(CustomItemList.Godforge_FakeItemQGP.get(1));
            }))
            .onMousePressed(d -> {
                if (magmatterCapable.getBoolValue()) {
                    magmatterMode.setBoolValue(!magmatterMode.getBoolValue());
                }
                return true;
            })
            .tooltipDynamic(t -> {
                if (!magmatterMode.getBoolValue()) {
                    t.addLine(translateToLocal("fog.button.magmattermode.tooltip.01"));
                }
                if (magmatterCapable.getBoolValue() && magmatterMode.getBoolValue()) {
                    t.addLine(translateToLocal("fog.button.magmattermode.tooltip.02"));
                }
                if (!magmatterCapable.getBoolValue()) {
                    t.addLine(EnumChatFormatting.GRAY + translateToLocal("fog.button.magmattermode.tooltip.03"));
                }
            })
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }

    @Override
    protected boolean usesTerminalLeftButton() {
        return true;
    }

    @Override
    protected IWidget createTerminalLeftButton() {
        IPanelHandler expectedInputsPanel = Panels.EXOTIC_INPUTS_LIST.getFrom(getMainPanel(), hypervisor);
        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.PICTURE_INFO)
            .onMousePressed(d -> {
                if (!expectedInputsPanel.isPanelOpen()) {
                    expectedInputsPanel.openPanel();
                } else {
                    expectedInputsPanel.closePanel();
                }
                return true;
            })
            .tooltip(t -> t.addLine(translateToLocal("fog.button.exoticinputs.tooltip")))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }
}
