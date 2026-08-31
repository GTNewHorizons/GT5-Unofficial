package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.common.tileentities.machines.multi.MTEQuadcellPlasmaCollider.RESIDUE_CONVERSION_DIVISOR;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.sync.Predicates;
import gregtech.common.tileentities.machines.multi.MTEQuadcellPlasmaCollider;
import gregtech.common.tileentities.machines.multi.MTEQuadcellPlasmaCollider.PlasmaType;

public class MTEQuadcellPlasmaColliderGui extends MTEMultiBlockBaseGui<MTEQuadcellPlasmaCollider> {

    public MTEQuadcellPlasmaColliderGui(MTEQuadcellPlasmaCollider multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue(
            "ForceDR",
            new IntSyncValue(() -> multiblock.FORCE_CURRENT_DR, multiblock::setForceDR).allowC2S());

        syncManager.syncValue(
            "RuniteDR",
            new IntSyncValue(() -> multiblock.RUNITE_CURRENT_DR, multiblock::setRuniteDR).allowC2S());
        syncManager.syncValue(
            "CelestialTungstenDR",
            new IntSyncValue(() -> multiblock.CELESTIAL_TUNGSTEN_CURRENT_DR, multiblock::setCelestialTungstenDR)
                .allowC2S());
        syncManager.syncValue(
            "OrikalkumDR",
            new IntSyncValue(() -> multiblock.ORIKALKUM_CURRENT_DR, multiblock::setOrikalkumDR).allowC2S());

        syncManager.syncValue(
            "RuniteBoost",
            new FloatSyncValue(() -> multiblock.RUNITE_CURRENT_BOOST, val -> multiblock.RUNITE_CURRENT_BOOST = val));
        syncManager.syncValue(
            "CelestialTungstenBoost",
            new FloatSyncValue(
                () -> multiblock.CELESTIAL_TUNGSTEN_CURRENT_BOOST,
                val -> multiblock.CELESTIAL_TUNGSTEN_CURRENT_BOOST = val));
        syncManager.syncValue(
            "OrikalkumBoost",
            new FloatSyncValue(
                () -> multiblock.ORIKALKUM_CURRENT_BOOST,
                val -> multiblock.ORIKALKUM_CURRENT_BOOST = val));

        syncManager.syncValue("EnergyProduced", new LongSyncValue(() -> multiblock.lEUt, val -> multiblock.lEUt = val));
        syncManager.syncValue(
            "ResidueOutput",
            new IntSyncValue(() -> multiblock.drainedSinceLastOutput / Math.max(1, multiblock.residueCycles)));
    }

    @Override
    protected int getTerminalRowHeight() {
        return super.getTerminalRowHeight() + 20;
    }

    @Override
    protected int getTextBoxToInventoryGap() {
        return 2;
    }

    @Override
    protected int getBasePanelHeight() {
        return 203;
    }

    @Override
    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .size(getTerminalRowWidth(), getTerminalRowHeight())
            .marginBottom(2)
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .paddingTop(4)
                    .paddingBottom(4)
                    .paddingLeft(4)
                    .paddingRight(0)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel).collapseDisabledChild()
                            .setEnabledIf(_ -> !multiblock.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8))
                    .child(
                        createConfigurationTerminalTextWidget(syncManager).collapseDisabledChild()
                            .setEnabledIf(_ -> multiblock.terminalSwitch)
                            .size(getTerminalWidgetWidth() - 10, getTerminalWidgetHeight() - 8))
                    .childIf(
                        multiblock.supportsTerminalRightCornerColumn(),
                        () -> createTerminalRightCornerColumn(panel, syncManager)));
    }

    @Override
    protected Flow createButtonColumn(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createButtonColumn(parent, syncManager).child(createConfigurationButton());
    }

    @Override
    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createTerminalRightCornerColumn(panel, syncManager).setEnabledIf($ -> !multiblock.terminalSwitch);
    }

    @Override
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .size(0);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue force = PlasmaType.FORCE.getSyncValue(syncManager);
        IntSyncValue runite = PlasmaType.RUNITE.getSyncValue(syncManager);
        IntSyncValue celestial = PlasmaType.CELESTIAL.getSyncValue(syncManager);
        IntSyncValue orikalkum = PlasmaType.ORIKALKUM.getSyncValue(syncManager);

        FloatSyncValue runiteBoost = PlasmaType.RUNITE.getBoostSyncValue(syncManager);
        FloatSyncValue celestialBoost = PlasmaType.CELESTIAL.getBoostSyncValue(syncManager);
        FloatSyncValue orikalkumBoost = PlasmaType.ORIKALKUM.getBoostSyncValue(syncManager);

        SyncHandler<?> progress = syncManager.getSyncHandlerFromMapKey("maxProgressTime:0");
        LongSyncValue eut = syncManager.findSyncHandler("EnergyProduced", LongSyncValue.class);
        IntSyncValue residue = syncManager.findSyncHandler("ResidueOutput", IntSyncValue.class);

        return super.createTerminalTextWidget(syncManager, parent).child(createRecipeInfoTextWidget(syncManager))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.qpc.generating",
                        EnumChatFormatting.AQUA + formatNumber(eut.getLongValue()) + EnumChatFormatting.WHITE))
                    .color(Color.WHITE.main)
                    .alignment(Alignment.CenterLeft)
                    .asWidget()
                    .marginBottom(2)
                    .fullWidth()
                    .setEnabledIf(_ -> Predicates.isPositive(progress)))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocalFormatted(
                        "GT5U.gui.text.qpc.residue",
                        EnumChatFormatting.DARK_AQUA
                            + formatFluid(Math.floorDiv(residue.getIntValue(), RESIDUE_CONVERSION_DIVISOR))
                            + EnumChatFormatting.WHITE))
                    .color(Color.WHITE.main)
                    .alignment(Alignment.CenterLeft)
                    .asWidget()
                    .marginBottom(2)
                    .fullWidth()
                    .setEnabledIf(_ -> Predicates.arePositive(progress, residue)))
            .child(
                IKey.lang("GT5U.gui.text.qpc.expected_rate")
                    .color(Color.WHITE.main)
                    .alignment(Alignment.CenterLeft)
                    .asWidget()
                    .fullWidth()
                    .marginBottom(2)
                    .setEnabledIf(_ -> Predicates.isPositive(progress)))
            .child(IKey.dynamic(() -> {
                float multiplier = 1.0f / (1.0f - ((runiteBoost.getFloatValue() + celestialBoost.getFloatValue())
                    * orikalkumBoost.getFloatValue()));
                int value = Math.round(force.getIntValue() / multiplier);
                return StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.qpc.plasma_display",
                    PlasmaType.FORCE.getLocalName(),
                    EnumChatFormatting.GREEN + formatFluid(value) + EnumChatFormatting.WHITE);

            })
                .color(PlasmaType.FORCE.getRGB())
                .asWidget()
                .marginBottom(2)
                .marginLeft(2)
                .setEnabledIf(_ -> Predicates.isPositive(progress)))
            .child(IKey.dynamic(() -> {
                float multiplier = 1.0f / (1.0f - ((runiteBoost.getFloatValue() + celestialBoost.getFloatValue())
                    * orikalkumBoost.getFloatValue()));
                int value = Math.round(runite.getIntValue() / multiplier);
                return StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.qpc.plasma_display",
                    PlasmaType.RUNITE.getLocalName(),
                    EnumChatFormatting.GREEN + formatFluid(value) + EnumChatFormatting.WHITE);
            })
                .color(PlasmaType.RUNITE.getRGB())
                .asWidget()
                .marginBottom(2)
                .marginLeft(2)
                .setEnabledIf(_ -> Predicates.isPositive(progress)))
            .child(IKey.dynamic(() -> {
                float multiplier = 1.0f / (1.0f - ((runiteBoost.getFloatValue() + celestialBoost.getFloatValue())
                    * orikalkumBoost.getFloatValue()));
                int value = Math.round(celestial.getIntValue() / multiplier);
                return StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.qpc.plasma_display",
                    PlasmaType.CELESTIAL.getLocalName(),
                    EnumChatFormatting.GREEN + formatFluid(value) + EnumChatFormatting.WHITE);
            })
                .color(PlasmaType.CELESTIAL.getRGB())
                .asWidget()
                .marginBottom(2)
                .marginLeft(2)
                .setEnabledIf(_ -> Predicates.isPositive(progress)))
            .child(IKey.dynamic(() -> {
                float multiplier = 1.0f / (1.0f - ((runiteBoost.getFloatValue() + celestialBoost.getFloatValue())
                    * orikalkumBoost.getFloatValue()));
                int value = Math.round(orikalkum.getIntValue() / multiplier);
                return StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.qpc.plasma_display",
                    PlasmaType.ORIKALKUM.getLocalName(),
                    EnumChatFormatting.GREEN + formatFluid(value) + EnumChatFormatting.WHITE);

            })
                .color(PlasmaType.ORIKALKUM.getRGB())
                .asWidget()
                .marginBottom(2)
                .marginLeft(3)
                .setEnabledIf(_ -> Predicates.isPositive(progress)));
    }

    protected Flow createConfigurationTerminalTextWidget(PanelSyncManager syncManager) {
        return Flow.column()
            .horizontalCenter()
            .coverChildren()
            .child(createConfigurationRow(PlasmaType.FORCE, syncManager))
            .child(createConfigurationRow(PlasmaType.RUNITE, syncManager))
            .child(createConfigurationRow(PlasmaType.CELESTIAL, syncManager))
            .child(createConfigurationRow(PlasmaType.ORIKALKUM, syncManager));
    }

    protected Flow createConfigurationRow(PlasmaType plasma, PanelSyncManager syncManager) {
        IntSyncValue syncValue = plasma.getSyncValue(syncManager);

        return Flow.column()
            .size(171, 16 + 1 + 9)
            .child(
                IKey.str(plasma.getLocalName())
                    .color(plasma.getRGB())
                    .shadow(true)
                    .asWidget()
                    .marginBottom(1))
            .child(
                Flow.row()
                    .height(16)
                    .widthRel(1)
                    .child(
                        new FluidDisplayWidget().background(IDrawable.EMPTY)
                            .hoverBackground(IDrawable.EMPTY)
                            .value(plasma.getFluid())
                            .displayAmount(false)
                            .size(16)
                            .marginRight(2))
                    .child(
                        new ParentWidget<>().size(103, 10)
                            .child(
                                GTGuiTextures.PLASMA_COLLIDER_SLIDER_BG.asWidget()
                                    .size(103, 10))
                            .child(
                                new SliderWidget().size(101, 8)
                                    .margin(1, 1)
                                    .verticalCenter()
                                    .bounds(0, plasma.getMaxDR())
                                    .sliderSize(2, 8)
                                    .sliderTexture(new Rectangle().color(Color.WHITE.main))
                                    .background(new Rectangle().horizontalGradient(Color.GREY.main, plasma.getRGB()))
                                    .value(
                                        new DoubleValue.Dynamic(syncValue::getDoubleValue, syncValue::setDoubleValue))))
                    .child(
                        new TextFieldWidget().size(48, 16)
                            .formatAsInteger(true)
                            .numbersInt(0, plasma.getMaxDR())
                            .value(new IntValue.Dynamic(syncValue::getIntValue, syncValue::setIntValue))
                            .setTextAlignment(Alignment.CENTER)
                            .setTextColor(plasma.getRGB())
                            .marginLeft(2)));
    }

    protected IWidget createConfigurationButton() {
        return new ButtonWidget<>().size(18)
            .overlay(GuiTextures.GEAR)
            .onMousePressed(d -> {
                multiblock.terminalSwitch = !multiblock.terminalSwitch;
                return true;
            })
            .tooltipBuilder(t -> t.addLine(StatCollector.translateToLocal("GT5U.gui.button.qpc.configure")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }
}
