package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.widget.RotatedDrawable;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.util.ModuleManager;

public class ModuleRingButtons {

    public static ParentWidget<?> createModuleRing(SyncHypervisor hypervisor, int ringIndex) {
        ParentWidget<?> parent = new ParentWidget<>().size(31);

        parent.child(
            GTGuiTextures.GODFORGE_MODULE_RING.asWidget()
                .size(27)
                .marginTop(2)
                .marginLeft(2));

        Table<Modules<?>, Integer, IPanelHandler> panelHandlers = buildPanelHandlers(hypervisor);

        parent.child(createModuleButton(hypervisor, 0, ringIndex, panelHandlers).align(Alignment.TopCenter));
        parent.child(createModuleButton(hypervisor, 1, ringIndex, panelHandlers).align(Alignment.CenterRight));
        parent.child(createModuleButton(hypervisor, 2, ringIndex, panelHandlers).align(Alignment.BottomCenter));
        parent.child(createModuleButton(hypervisor, 3, ringIndex, panelHandlers).align(Alignment.CenterLeft));

        return parent;
    }

    private static Table<Modules<?>, Integer, IPanelHandler> buildPanelHandlers(SyncHypervisor hypervisor) {
        Table<Modules<?>, Integer, IPanelHandler> panelHandlers = HashBasedTable.create();
        ModuleManager moduleManager = hypervisor.getData()
            .getModuleManager();

        for (int i = 0; i < 16; i++) {
            final int ii = i;
            panelHandlers.put(
                Modules.SMELTING,
                i,
                Panels.MAIN_SMELTING.getModuleSubpanel(() -> moduleManager.getModuleAt(ii), i, hypervisor));
            panelHandlers.put(
                Modules.MOLTEN,
                i,
                Panels.MAIN_MOLTEN.getModuleSubpanel(() -> moduleManager.getModuleAt(ii), i, hypervisor));
            panelHandlers.put(
                Modules.PLASMA,
                i,
                Panels.MAIN_PLASMA.getModuleSubpanel(() -> moduleManager.getModuleAt(ii), i, hypervisor));
            panelHandlers.put(
                Modules.EXOTIC,
                i,
                Panels.MAIN_EXOTIC.getModuleSubpanel(() -> moduleManager.getModuleAt(ii), i, hypervisor));
        }

        return panelHandlers;
    }

    private static Widget<?> createModuleButton(SyncHypervisor hypervisor, int ringLocation, int ringIndex,
        Table<Modules<?>, Integer, IPanelHandler> panelHandlers) {
        ModuleManager moduleManager = hypervisor.getData()
            .getModuleManager();
        final int moduleIndex = ringIndex * 4 + ringLocation;

        return new ButtonWidget<>().width(ringLocation % 2 == 0 ? 5 : 14)
            .height(ringLocation % 2 == 0 ? 14 : 5)
            .background(IDrawable.EMPTY)
            .overlay(new DynamicDrawable(() -> {
                MTEBaseModule module = moduleManager.getModuleAt(moduleIndex);
                if (module == null) {
                    return IDrawable.EMPTY;
                }

                IGregTechTileEntity igtte = module.getBaseMetaTileEntity();
                if (igtte == null) {
                    return IDrawable.EMPTY;
                }

                UITexture texture = GTGuiTextures.GODFORGE_MODULE_OFF;
                if (igtte.isActive()) {
                    texture = GTGuiTextures.GODFORGE_MODULE_ON;
                }

                int rotation;
                if (ringLocation == 0) {
                    rotation = 1;
                } else if (ringLocation == 1) {
                    rotation = 0;
                } else if (ringLocation == 2) {
                    rotation = 3;
                } else {
                    rotation = 2;
                }

                return new RotatedDrawable(texture).rotationRadian(rotation * (float) (Math.PI / 2));
            }))
            .onMousePressed(d -> {
                MTEBaseModule multiblock = moduleManager.getModuleAt(moduleIndex);
                if (multiblock == null) {
                    return true;
                }

                Modules<?> module = Modules.getModule(multiblock);
                IPanelHandler panelHandler = panelHandlers.get(module, moduleIndex);
                if (!panelHandler.isPanelOpen()) {
                    panelHandler.openPanel();
                } else {
                    panelHandler.closePanel();
                }

                return true;
            })
            .tooltipDynamic(t -> {
                MTEBaseModule module = moduleManager.getModuleAt(moduleIndex);
                if (module != null) {
                    t.addLine(module.getLocalName());
                    t.addLine(
                        EnumChatFormatting.ITALIC + ""
                            + EnumChatFormatting.GRAY
                            + translateToLocal("gt.blockmachines.multimachine.FOG.clicktoopenmoduleui"));
                }
            })
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .clickSound(ForgeOfGodsGuiUtil.getButtonSound());
    }
}
