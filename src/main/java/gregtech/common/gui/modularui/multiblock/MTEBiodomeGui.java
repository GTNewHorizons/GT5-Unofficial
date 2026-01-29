package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.recipe.RecipeMaps.biodomeFakeCalibrationRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.FluidDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTRecipe;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEBiodome;

public class MTEBiodomeGui extends MTEMultiBlockBaseGui<MTEBiodome> {

    public MTEBiodomeGui(MTEBiodome multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createButtonColumn(panel, syncManager).child(createOverviewButton(syncManager, panel));
    }

    protected IWidget createOverviewButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler statsPanel = syncManager.panel(
            "calibrationPanel",
            (p_syncManager, syncHandler) -> openCalibrationPanel(p_syncManager, parent, syncManager),
            true);
        return new ButtonWidget<>().size(18, 18)
            .overlay(GTGuiTextures.FOUNDRY_CALCULATOR)
            .onMousePressed(d -> {
                if (!statsPanel.isPanelOpen()) {
                    statsPanel.openPanel();
                } else {
                    statsPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.machineinfo")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> list = super.createTerminalTextWidget(syncManager, parent);
        return list;
        // list.child(new IKey.dynamic())
    }

    private ModularPanel openCalibrationPanel(PanelSyncManager p_syncManager, ModularPanel parent,
        PanelSyncManager syncManager) {

        ListWidget<IWidget, ?> list = new ListWidget<>().sizeRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        ModularPanel panel = new ModularPanel("calibrationPanel").relative(parent)
            .rightRel(1)
            .topRel(0)
            .size(150, 150)
            .child(list);

        for (GTRecipe recipe : biodomeFakeCalibrationRecipes.getAllRecipes()) {
            list.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                multiblock.setCalibrationRecipe(recipe);
                return true;
            })
                .size(150, 20)
                .tooltipBuilder(t -> {
                    for (ItemStack stack : recipe.mInputs) {
                        t.add(new ItemDrawable(stack));
                        t.addLine(" " + formatNumber(stack.stackSize) + "x " + stack.getDisplayName());
                    }
                    for (FluidStack fluid : recipe.mFluidInputs) {
                        t.add(new FluidDrawable(fluid));
                        t.addLine(" " + formatNumber(fluid.amount) + "L " + fluid.getLocalizedName());
                    }
                })
                .child(
                    Flow.row()
                        .childPadding(4)
                        .marginLeft(4)
                        .child(new ItemDrawable(recipe.mOutputs[0]).asWidget())
                        .child(
                            IKey.str(recipe.mOutputs[0].getDisplayName())
                                .asWidget())));
        }
        return panel;
    }
}
