package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.recipe.RecipeMaps.biodomeFakeCalibrationRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.FluidDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
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

        StringSyncValue dimensionSync = syncManager.findSyncHandler("dimension", StringSyncValue.class);
        IntSyncValue calibrationSync = syncManager.findSyncHandler("calibrationPercent", IntSyncValue.class);
        IntSyncValue stateSync = syncManager.findSyncHandler("state", IntSyncValue.class);
        IntSyncValue warmupTicksSync = syncManager.findSyncHandler("warmupTicks", IntSyncValue.class);

        list.child(IKey.dynamic(() -> {
            String dim = dimensionSync.getValue();
            int calPct = calibrationSync.getValue();
            MTEBiodome.BiodomeState st = MTEBiodome.BiodomeState.fromOrdinal(stateSync.getValue());
            if (dim == null || dim.isEmpty()) {
                return EnumChatFormatting.GRAY + "No dimension selected";
            }
            EnumChatFormatting color = switch (st) {
                case ACTIVE -> EnumChatFormatting.GREEN;
                case CALIBRATING, WARMUP, SHUTTING_DOWN -> EnumChatFormatting.YELLOW;
                default -> calPct <= 0 ? EnumChatFormatting.RED : EnumChatFormatting.GRAY;
            };
            return color + "Dimension: " + EnumChatFormatting.WHITE + dim;
        })
            .asWidget());

        list.child(IKey.dynamic(() -> {
            int calPct = calibrationSync.getValue();
            String dim = dimensionSync.getValue();
            if (calPct <= 0 || dim == null || dim.isEmpty()) return "";
            String calDisplay = String.format("%.1f%%", calPct / 100.0);
            EnumChatFormatting color = calPct >= MTEBiodome.MAX_CALIBRATION ? EnumChatFormatting.GREEN
                : EnumChatFormatting.YELLOW;
            return EnumChatFormatting.GRAY + "Calibration: " + color + calDisplay;
        })
            .asWidget()
            .setEnabledIf(
                w -> calibrationSync.getValue() > 0 && dimensionSync.getValue() != null
                    && !dimensionSync.getValue()
                        .isEmpty()));

        list.child(IKey.dynamic(() -> {
            MTEBiodome.BiodomeState st = MTEBiodome.BiodomeState.fromOrdinal(stateSync.getValue());
            if (st != MTEBiodome.BiodomeState.WARMUP) return "";
            int wt = warmupTicksSync.getValue();
            if (wt <= 0) return "";
            int warmupPct = wt * 100 / MTEBiodome.WARMUP_DURATION;
            return EnumChatFormatting.GRAY + "Warmup: " + EnumChatFormatting.YELLOW + warmupPct + "%";
        })
            .asWidget()
            .setEnabledIf(w -> {
                MTEBiodome.BiodomeState st = MTEBiodome.BiodomeState.fromOrdinal(stateSync.getValue());
                return st == MTEBiodome.BiodomeState.WARMUP && warmupTicksSync.getValue() > 0;
            }));

        GenericListSyncHandler<ItemStack> itemInputSyncer = (GenericListSyncHandler<ItemStack>) syncManager
            .findSyncHandler("itemInputs");
        GenericListSyncHandler<FluidStack> fluidInputSyncer = (GenericListSyncHandler<FluidStack>) syncManager
            .findSyncHandler("fluidInputs");

        DynamicSyncHandler recipeHandler = new DynamicSyncHandler().widgetProvider((syncManager1, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return Flow.column()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .coverChildren()
                .child(createItemRecipeInfo(packet, syncManager))
                .child(createFluidRecipeInfo(packet, syncManager));
        });

        Runnable serverNotifier = () -> {
            if (!syncManager.isClient()) {
                notifyRecipeHandler(recipeHandler, itemInputSyncer, fluidInputSyncer);
            }
        };
        itemInputSyncer.setChangeListener(serverNotifier);
        fluidInputSyncer.setChangeListener(serverNotifier);

        return list.child(
            new DynamicSyncedWidget<>().widthRel(0.85f)
                .coverChildrenHeight()
                .syncHandler(recipeHandler));
    }

    private void notifyRecipeHandler(DynamicSyncHandler recipeHandler,
        GenericListSyncHandler<ItemStack> itemOutputSyncer, GenericListSyncHandler<FluidStack> fluidOutputSyncer) {
        recipeHandler.notifyUpdate(packet -> {
            List<ItemStack> items = itemOutputSyncer.getValue();
            packet.writeInt(items.size());
            for (ItemStack item : items) {
                NetworkUtils.writeItemStack(packet, item);
            }

            List<FluidStack> fluids = fluidOutputSyncer.getValue();
            packet.writeInt(fluids.size());
            for (FluidStack fluid : fluids) {
                NetworkUtils.writeFluidStack(packet, fluid);
            }
        });
    }

    private IWidget createItemRecipeInfo(PacketBuffer packet, PanelSyncManager syncManager) {
        int size = packet.readInt();
        Flow column = Flow.column()
            .coverChildren();

        List<ItemStack> itemList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ItemStack item = NetworkUtils.readItemStack(packet);
            // Some multiblocks set outputs to null
            if (item == null) {
                continue;
            }
            itemList.add(item);
        }
        // create row for each entry
        for (ItemStack stack : itemList) {
            column.child(
                Flow.row()
                    .widthRel(1)
                    .height(DISPLAY_ROW_HEIGHT)
                    .child(
                        new ItemDisplayWidget().item(stack)
                            .background(IDrawable.NONE)
                            .displayAmount(false)
                            .size(DISPLAY_ROW_HEIGHT - 1)
                            .marginRight(2))
                    .child(
                        IKey.str(
                            EnumChatFormatting.AQUA + stack.getDisplayName()
                                + EnumChatFormatting.RESET
                                + " x "
                                + EnumChatFormatting.WHITE
                                + stack.stackSize)
                            .asWidget()));
        }

        return column;
    }

    private IWidget createFluidRecipeInfo(PacketBuffer packet, PanelSyncManager syncManager) {
        int size = packet.readInt();
        Flow column = Flow.column()
            .coverChildren();

        // create merged map of fluidstack to total amount in recipe
        final List<FluidStack> fluidStacks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            FluidStack fluidStack = NetworkUtils.readFluidStack(packet);
            // Some multiblocks set outputs to null
            if (fluidStack == null) {
                continue;
            }
            fluidStacks.add(fluidStack);

        }
        // create row for each entry
        for (FluidStack fluidStack : fluidStacks) {
            column.child(
                Flow.row()
                    .widthRel(1)
                    .height(DISPLAY_ROW_HEIGHT)
                    .child(
                        new FluidDisplayWidget().background(IDrawable.EMPTY)
                            .displayAmount(false)
                            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                            .value(fluidStack)
                            .size(DISPLAY_ROW_HEIGHT - 1)
                            .marginRight(2))
                    .child(
                        IKey.str(
                            EnumChatFormatting.AQUA + fluidStack.getLocalizedName()
                                + EnumChatFormatting.RESET
                                + " x "
                                + EnumChatFormatting.WHITE
                                + fluidStack.amount)
                            .asWidget()));
        }
        return column;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        syncManager.syncValue("calibrationPercent", new IntSyncValue(multiblock::getCalibrationPercent));
        syncManager.syncValue(
            "state",
            new IntSyncValue(
                () -> multiblock.getState()
                    .ordinal()));
        syncManager.syncValue("warmupTicks", new IntSyncValue(multiblock::getWarmupTicks));
        syncManager.syncValue("dimension", new StringSyncValue(multiblock::getDimension));

        syncManager.syncValue(
            "itemInputs",
            new GenericListSyncHandler<>(
                () -> multiblock.getInputItems() != null ? Arrays.asList(multiblock.getInputItems())
                    : Collections.emptyList(),
                x -> {},
                NetworkUtils::readItemStack,
                NetworkUtils::writeItemStack,
                (a, b) -> a.isItemEqual(b) && a.stackSize == b.stackSize,
                null));

        syncManager.syncValue(
            "fluidInputs",
            new GenericListSyncHandler<>(
                () -> multiblock.getInputFluids() != null ? Arrays.stream(multiblock.getInputFluids())
                    .map(fluidStack -> {
                        if (fluidStack == null) return null;
                        return new FluidStack(fluidStack, fluidStack.amount) {

                            @Override
                            public boolean isFluidEqual(FluidStack other) {
                                return super.isFluidEqual(other) && amount == other.amount;
                            }
                        };
                    })
                    .collect(Collectors.toList()) : Collections.emptyList(),
                x -> {},
                NetworkUtils::readFluidStack,
                NetworkUtils::writeFluidStack,
                (a, b) -> a.isFluidEqual(b) && a.amount == b.amount,
                null));

        super.registerSyncValues(syncManager);
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
            list.child(
                new ButtonWidget<>()
                    .syncHandler(
                        new InteractionSyncHandler()
                            .setOnMousePressed(mouseData -> { multiblock.setCalibrationRecipe(recipe); }))
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
