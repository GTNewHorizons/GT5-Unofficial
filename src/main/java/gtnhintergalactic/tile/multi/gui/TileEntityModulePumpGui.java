package gtnhintergalactic.tile.multi.gui;

import static gtnhintergalactic.recipe.SpacePumpingRecipes.RECIPES;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.GuiAxis;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.TabTexture;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.PageButton;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.common.gui.modularui.widget.FluidDisplaySyncHandler;
import gregtech.common.gui.modularui.widget.FluidSlotDisplayOnly;
import gtneioreplugin.plugin.block.ModBlocks;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;

public class TileEntityModulePumpGui extends TileEntityModuleBaseGui {

    private TileEntityModulePump pump;

    public TileEntityModulePumpGui(MTEMultiBlockBase base) {
        super(base);
        this.pump = (TileEntityModulePump) base;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> fluidWidgets = new ListWidget<>();
        for (int i = 0; i < pump.getParallelRecipes(); i++) {
            int finalI1 = i;
            IntSyncValue recipeTierSyncer = new IntSyncValue(
                () -> pump.recipes[finalI1][0],
                val -> pump.recipes[finalI1][0] = val);
            IntSyncValue recipeFluidSyncer = new IntSyncValue(
                () -> pump.recipes[finalI1][1],
                val -> pump.recipes[finalI1][1] = val);
            IntSyncValue recipeParallelSyncer = new IntSyncValue(
                () -> pump.recipeParallelParameters[finalI1].getValue(),
                val -> pump.recipeParallelParameters[finalI1].setValue(val));
            syncManager.syncValue("recipeTier" + i, recipeTierSyncer);
            syncManager.syncValue("recipeFluid" + i, recipeFluidSyncer);
            syncManager.syncValue("recipeParallel" + i, recipeParallelSyncer);
            IPanelHandler fluidSelectorPanel = syncManager.panel(
                "fluid_selector_panel_slot" + i,
                (p_syncManager, syncHandler) -> getFluidSelectorPanel(parent, p_syncManager, syncHandler, finalI1),
                true);

            FluidDisplaySyncHandler fluidDisplaySyncer = new FluidDisplaySyncHandler(() -> {
                if (pump.recipes[finalI1][0] == -1 || pump.recipes[finalI1][1] == -1) return null;
                return RECIPES.get(pump.recipes[finalI1][0])
                    .get(pump.recipes[finalI1][1]);
            });
            syncManager.syncValue("recipeFluidSlot" + i, fluidDisplaySyncer);
            FluidSlotDisplayOnly recipeFluidSlot = new FluidSlotDisplayOnly() {

                @NotNull
                @Override
                public Result onMouseTapped(int mouseButton) {
                    fluidSelectorPanel.openPanel();
                    return Result.SUCCESS;
                }
            }.syncHandler("recipeFluidSlot" + i)
                .tooltipBuilder(t -> {
                    FluidStack fluidStack = fluidDisplaySyncer.getValue();
                    if (fluidStack == null) {
                        t.clearText()
                            .addLine(IKey.lang("tt.spacepump.noFluidTooltip"));
                    } else {
                        t.clearText()
                            .addLine(IKey.str(fluidStack.getLocalizedName()))
                            .add(
                                IKey.str(
                                    EnumChatFormatting.GREEN
                                        + NumberFormat.format(fluidStack.amount, NumberFormat.DEFAULT)
                                        + "L/s "
                                        + EnumChatFormatting.WHITE))
                            .add(IKey.lang("tt.spacepump.parallelTooltip"));
                    }
                });

            fluidWidgets.child(
                new Row().coverChildren()
                    .child(
                        new TextWidget(IKey.lang("tt.spacepump.fluid", i + 1)).marginRight(5)
                            .color(Color.WHITE.main))
                    .child(
                        recipeFluidSlot.size(16, 16)
                            .marginRight(5))
                    .child(IKey.dynamic(() -> {
                        FluidStack fluidStack = fluidDisplaySyncer.getValue();
                        if (fluidStack == null) return "";
                        fluidStack.amount *= recipeParallelSyncer.getValue();
                        return NumberFormat.format(fluidStack.amount, NumberFormat.DEFAULT) + "L/s";
                    })
                        .asWidget()
                        .color(Color.ORANGE.main))
                    .marginBottom(2));
        }
        fluidWidgets.onUpdateListener((unused) -> {
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(fluidWidgets);
            }
        });
        return fluidWidgets.children(super.createTerminalTextWidget(syncManager, parent).getChildren());
    }

    private ModularPanel getFluidSelectorPanel(ModularPanel parent, PanelSyncManager syncManager,
        IPanelHandler thisPanel, int slot) {
        String[] planetTiers = new String[] { "De", "As", "Io", "En", "Pr", "Ha", "BC" }; // for tab icons

        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("fluid_selector_panel" + slot) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        };
        panel.size(planetTiers.length * 24, 90)
            .pos(parentArea.x, parentArea.y + 30);

        AtomicReference<String> search = new AtomicReference<>(EnumChatFormatting.GRAY + "Search...");
        StringSyncValue textFieldSyncer = new StringSyncValue(search::get, search::set);

        PagedWidget.Controller tabController = new PagedWidget.Controller();
        Flow row = new Row().topRel(0, 4, 1)
            .coverChildren();
        PagedWidget<?> pagedWidget = new PagedWidget<>().controller(tabController);
        syncManager.syncValue(
            "pageSwitcher",
            new IntSyncValue(
                () -> firstTierWithFluid(
                    textFieldSyncer.getStringValue()
                        .toLowerCase()),
                val -> { if (val >= 0) pagedWidget.setPage(val); }));
        for (int i = 0; i < planetTiers.length; i++) {

            TabTexture TAB_TOP = TabTexture
                .of(UITexture.fullImage("modularui2", "gui/tab/tabs_top", true), GuiAxis.Y, false, 24, 24, 4);
            Item planetItem = ModBlocks.blocks.get(planetTiers[i])
                .getItemDropped(0, null, 0);
            int finalI = i;
            row.child(
                new PageButton(i, tabController).size(18, 18)
                    .alignY(1)
                    .tab(TAB_TOP, 0)
                    .overlay(new DynamicDrawable(() -> {
                        if (tierContainsFluid(
                            finalI,
                            textFieldSyncer.getStringValue()
                                .toLowerCase())) {
                            return new DrawableStack(
                                new Rectangle().setColor(Color.rgb(0, 255, 0))
                                    .asIcon()
                                    .size(16, 16),
                                new ItemDrawable(new ItemStack(planetItem)).asIcon());
                        } else {
                            return new ItemDrawable(new ItemStack(planetItem)).asIcon();
                        }
                    })));
            Flow fluidColumn = new Column().sizeRel(1)
                .marginTop(10)
                .marginLeft(5);
            Flow tempRow = new Row().widthRel(1)
                .height(24);

            for (int j = 0; j < RECIPES.get(i)
                .size(); j++) {
                int finalI1 = i;
                int finalJ1 = j;
                FluidDisplaySyncHandler fluidDisplaySyncer = new FluidDisplaySyncHandler(
                    () -> RECIPES.get(finalI)
                        .get(finalJ1)
                        .copy());

                FluidStack fluidStack = RECIPES.get(i)
                    .get(j)
                    .copy();
                FluidSlotDisplayOnly fluidDisplay = new FluidSlotDisplayOnly() {

                    @NotNull
                    @Override
                    public Result onMousePressed(int mouseButton) {
                        PanelSyncManager manager = syncManager.getModularSyncManager()
                            .getPanelSyncManager("MTEMultiBlockBase");
                        ((FluidDisplaySyncHandler) manager.getSyncHandler("recipeFluidSlot" + slot + ":0"))
                            .setValue(fluidDisplaySyncer.getValue());
                        ((IntSyncValue) manager.getSyncHandler("recipeTier" + slot + ":0")).setValue(finalI1);
                        ((IntSyncValue) manager.getSyncHandler("recipeFluid" + slot + ":0")).setValue(finalJ1);
                        thisPanel.closePanel();
                        return Result.SUCCESS;
                    }
                }.syncHandler(fluidDisplaySyncer)
                    .tooltipBuilder(
                        t -> t.clearText()
                            .addLine(IKey.str(fluidStack.getLocalizedName()))
                            .addLine(
                                IKey.str(
                                    EnumChatFormatting.GREEN
                                        + NumberFormat.format(fluidStack.amount, NumberFormat.DEFAULT)
                                        + "L/s"
                                        + EnumChatFormatting.WHITE
                                        + " per parallel")));

                int finalJ = j;
                tempRow.child(
                    new SingleChildWidget<>().size(24, 24)
                        .background(new DynamicDrawable(() -> {
                            if (fluidMatchesSearchString(
                                finalI,
                                finalJ,
                                textFieldSyncer.getStringValue()
                                    .toLowerCase()))
                                return new Rectangle().setColor(Color.rgb(0, 255, 0));
                            else return null;
                        }))
                        .child(fluidDisplay.align(Alignment.Center)));
                if ((j + 1) % 6 == 0 || j == RECIPES.get(i)
                    .size() - 1) {
                    fluidColumn.child(tempRow);
                    tempRow = new Row().widthRel(1)
                        .height(24);
                }

            }
            pagedWidget.addPage(fluidColumn);
        }
        panel.child(row)
            .child(
                new Column().sizeRel(1)
                    .child(pagedWidget.sizeRel(1, 0.6f))
                    .child(
                        new TextFieldWidget().size(80, 9)
                            .bottomRel(0, 9 + 4, 0)
                            .left(4)
                            .value(textFieldSyncer)));
        return panel;
    }

    private boolean fluidMatchesSearchString(int i, int j, String stringValue) {
        if (stringValue.isEmpty()) return false;
        FluidStack fluid = RECIPES.get(i)
            .get(j);
        return fluid.getLocalizedName()
            .toLowerCase()
            .contains(stringValue);
    }

    private boolean tierContainsFluid(int index, String target) {
        if (target.isEmpty()) return false;
        return RECIPES.get(index)
            .stream()
            .anyMatch(
                fluidStack -> fluidStack.getLocalizedName()
                    .toLowerCase()
                    .contains(target));
    }

    private int firstTierWithFluid(String target) {
        if (target.isEmpty()) return -1;
        for (int i = 0; i < RECIPES.size(); i++) {
            List<FluidStack> tierRecipes = RECIPES.get(i);
            if (tierRecipes.stream()
                .anyMatch(
                    fluid -> fluid.getLocalizedName()
                        .toLowerCase()
                        .contains(target))) {
                return i;
            }
        }
        return -1;
    }
}
