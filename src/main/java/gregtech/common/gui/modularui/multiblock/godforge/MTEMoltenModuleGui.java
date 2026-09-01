package gregtech.common.gui.modularui.multiblock.godforge;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
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

        SyncValues.ALLOY_CAPABLE.registerFor(getModuleType(), getMainPanel(), hypervisor);
        SyncValues.ALLOY_MODE.registerFor(getModuleType(), getMainPanel(), hypervisor);
    }

    @Override
    protected boolean usesExtraButton() {
        return true;
    }

    @Override
    protected IWidget createExtraButton() {
        BooleanSyncValue alloyCapable = SyncValues.ALLOY_CAPABLE
            .lookupFrom(getModuleType(), getMainPanel(), hypervisor);
        BooleanSyncValue alloyMode = SyncValues.ALLOY_MODE.lookupFrom(getModuleType(), getMainPanel(), hypervisor);

        return new ButtonWidget<>().size(16)
            .background(GTGuiTextures.TT_BUTTON_CELESTIAL_32x32)
            .overlay(new DynamicDrawable(() -> {
                if (alloyMode.getBoolValue()) {
                    return new ItemDrawable(GregtechItemList.Mega_AlloyBlastSmelter.get(1));
                }
                return new ItemDrawable(ItemList.Machine_Multi_BlastFurnace.get(1));
            }))
            .onMousePressed(d -> {
                if (alloyCapable.getBoolValue()) {
                    alloyMode.setBoolValue(!alloyMode.getBoolValue());
                }
                return true;
            })
            .tooltipDynamic(t -> {
                if (!alloyMode.getBoolValue()) {
                    t.addLine(translateToLocal("fog.button.alloymode.tooltip.01"));
                }
                if (alloyCapable.getBoolValue() && alloyMode.getBoolValue()) {
                    t.addLine(translateToLocal("fog.button.alloymode.tooltip.02"));
                }
                if (!alloyCapable.getBoolValue()) {
                    t.addLine(EnumChatFormatting.GRAY + translateToLocal("fog.button.alloymode.tooltip.03"));
                }
            })
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }
}
