package gregtech.common.gui.modularui.singleblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import gregtech.common.gui.modularui.util.MachineModularSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gregtech.common.tileentities.machines.basic.MTEIndustrialApiary;

public class MTEIndustrialApiaryGui extends MTEBasicMachineBaseGui<MTEIndustrialApiary> {

    // TODO error widget

    private final int QUEEN_SLOT_OFFSET = machine.getInputSlot();
    private final int DRONE_SLOT_OFFSET = QUEEN_SLOT_OFFSET + 1;
    private final int UPGRADE_SLOT_OFFSET = DRONE_SLOT_OFFSET + 1;

    public MTEIndustrialApiaryGui(MTEIndustrialApiary machine, BasicUIProperties properties) {
        super(machine, properties);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(SLOT_SIZE / 2)
            .marginLeft(SLOT_SIZE * 3 / 2);

        // input slots
        mainRow.child(
            Flow.column()
                .coverChildren()
                .child(
                    new ItemSlot()
                        .slot(
                            new MachineModularSlot(machine.inventoryHandler, QUEEN_SLOT_OFFSET, baseMetaTileEntity)
                                .singletonSlotGroup())
                        .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_BEE_QUEEN))
                .child(
                    new ItemSlot()
                        .slot(
                            new MachineModularSlot(machine.inventoryHandler, DRONE_SLOT_OFFSET, baseMetaTileEntity)
                                .singletonSlotGroup())
                        .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_BEE_DRONE)));

        // upgrade slots + arrow
        mainRow.child(
            Flow.column()
                .coverChildren()
                .child(createProgressBar(panel, syncManager))
                .child(
                    new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(2)
                        .itemSlotSupplier(
                            () -> new ItemSlot()
                                .addTooltipLine(
                                    GTUtility.translate("GT5U.machines.industrialapiary.upgradeslot.tooltip"))
                                .tooltipShowUpTimer(TOOLTIP_DELAY))
                        .indexOffset(UPGRADE_SLOT_OFFSET)
                        .modularSlotSupplier(MachineModularSlot.supplier(baseMetaTileEntity))
                        .build()));

        // output slots
        mainRow.child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(3)
                .indexOffset(machine.getOutputSlot())
                .modularSlotSupplier(MachineModularSlot.supplier(baseMetaTileEntity))
                .canPut(false)
                .build());

        return getEmptyContent().child(mainRow);
    }

    @Override
    protected ProgressWidget createProgressBar(ModularPanel panel, PanelSyncManager syncManager) {
        return new ProgressWidget()
            .value(new DoubleSyncValue(() -> (double) machine.mProgresstime / machine.mMaxProgresstime))
            .texture(properties.progressBarMUI2, properties.progressBarWidthMUI2)
            .size(properties.progressBarWidthMUI2, properties.progressBarHeightMUI2 / 2)
            .direction(properties.progressBarDirectionMUI2)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @Override
    protected Flow createTopRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        GenericSyncValue<ItemStack, ?> usedQueenSyncer = GenericSyncValue.builder(ItemStack.class)
            .getter(machine::getUsedQueen)
            .adapter(ByteBufAdapters.ITEM_STACK)
            .build();
        FloatSyncValue energyModSyncer = new FloatSyncValue(machine::getEnergyModifier);
        IntSyncValue accelerationSyncer = new IntSyncValue(machine::getAcceleration);
        IntSyncValue additionalEnergySyncer = new IntSyncValue(machine::getAdditionalEnergyUsage);
        EnumSyncValue<EnumTemperature, ?> temperatureSyncer = new EnumSyncValue<>(
            EnumTemperature.class,
            machine::getTemperature);
        EnumSyncValue<EnumHumidity, ?> humiditySyncer = new EnumSyncValue<>(EnumHumidity.class, machine::getHumidity);
        FloatSyncValue territorySyncer = new FloatSyncValue(() -> machine.getTerritoryModifier(null, 0));
        FloatSyncValue productionSyncer = new FloatSyncValue(() -> machine.getProductionModifier(null, 0));
        FloatSyncValue floweringSyncer = new FloatSyncValue(() -> machine.getFloweringModifier(null, 0));
        FloatSyncValue lifespanSyncer = new FloatSyncValue(() -> machine.getLifespanModifier(null, null, 0));

        syncManager.syncValue("usedQueen", usedQueenSyncer);
        syncManager.syncValue("energyMod", energyModSyncer);
        syncManager.syncValue("acceleration", accelerationSyncer);
        syncManager.syncValue("additionalEnergy", additionalEnergySyncer);
        syncManager.syncValue("temperature", temperatureSyncer);
        syncManager.syncValue("humidity", humiditySyncer);
        syncManager.syncValue("territory", territorySyncer);
        syncManager.syncValue("production", productionSyncer);
        syncManager.syncValue("flowering", floweringSyncer);
        syncManager.syncValue("lifespan", lifespanSyncer);

        return super.createTopRightCornerFlow(panel, syncManager).child(
            GTGuiTextures.INFORMATION_SYMBOL.asWidget()
                .size(7, 18)
                .tooltipAutoUpdate(true)
                .tooltipBuilder(tooltip -> {
                    final String energyreq = formatNumber(
                        (int) ((float) MTEIndustrialApiary.baseEUtUsage * energyModSyncer.getFloatValue()
                            * accelerationSyncer.getIntValue()) + additionalEnergySyncer.getIntValue());
                    // The localization in Forestry is written like this.
                    final String Temp = AlleleManager.climateHelper.toDisplay(temperatureSyncer.getValue());
                    final String Hum = AlleleManager.climateHelper.toDisplay(humiditySyncer.getValue());
                    ItemStack usedQueen = usedQueenSyncer.getValue();
                    if (usedQueen != null && beeRoot.isMember(usedQueen, EnumBeeType.QUEEN.ordinal())) {
                        final IBee bee = beeRoot.getMember(usedQueen);
                        if (bee.isAnalyzed()) {
                            final IBeeGenome genome = bee.getGenome();
                            final IBeeModifier mod = beeRoot.getBeekeepingMode(machine.getWorld())
                                .getBeeModifier();
                            final float tmod = territorySyncer.getFloatValue() * mod.getTerritoryModifier(null, 1f);
                            final int[] t = Arrays.stream(genome.getTerritory())
                                .map(i -> (int) ((float) i * tmod))
                                .toArray();
                            addToRichTooltip(
                                () -> machine.mTooltipCache.getUncachedTooltipData(
                                    "GT5U.machines.industrialapiary.infoextended.tooltip",
                                    energyreq,
                                    Temp,
                                    Hum,
                                    genome.getSpeed(),
                                    productionSyncer.getFloatValue() + mod.getProductionModifier(null, 0f),
                                    Math.round(
                                        floweringSyncer.getFloatValue() * genome.getFlowering()
                                            * mod.getFloweringModifier(null, 1f)),
                                    Math.round(
                                        lifespanSyncer.getFloatValue() * genome.getLifespan()
                                            * mod.getLifespanModifier(null, null, 1f)),
                                    t[0],
                                    t[1],
                                    t[2])).accept(tooltip);
                            tooltip.titleMargin(0);
                            return;
                        }
                    }
                    addToRichTooltip(
                        () -> machine.mTooltipCache.getUncachedTooltipData(
                            "GT5U.machines.industrialapiary.info.tooltip",
                            energyreq,
                            Temp,
                            Hum)).accept(tooltip);
                    tooltip.titleMargin(0);
                }));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        IntSyncValue speedSyncer = new IntSyncValue(machine::getSpeed, machine::setSpeed).allowC2S();
        syncManager.syncValue("speed", speedSyncer);
        IntSyncValue maxSpeedSyncer = new IntSyncValue(machine::getMaxSpeed);
        syncManager.syncValue("maxSpeed", maxSpeedSyncer);
        BooleanSyncValue speedLockedSyncer = new BooleanSyncValue(machine::isLockedSpeed, machine::setLockedSpeed)
            .allowC2S();
        syncManager.syncValue("speedLocked", speedLockedSyncer);

        ButtonWidget<?> speedButton = new ButtonWidget<>();
        speedSyncer.changeListener(speedButton::markTooltipDirty);
        speedLockedSyncer.changeListener(speedButton::markTooltipDirty);

        return Flow.row()
            .coverChildren()
            .verticalCenter()
            .crossAxisAlignment(Alignment.CrossAxis.END)
            .leftRel(0)
            .child(
                Flow.column()
                    .coverChildren()
                    .reverseLayout()
                    .child(
                        createAutoOutputButton(
                            syncManager,
                            "fluidAutoOutput",
                            GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID,
                            BaseTileEntity.FLUID_TRANSFER_TOOLTIP))
                    .child(
                        new ToggleButton()
                            .value(new BooleanSyncValue(machine::isAutoQueen, machine::setAutoQueen).allowC2S())
                            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_BEE_QUEEN)
                            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                            .addTooltipStringLines(
                                machine.mTooltipCache.getData("GT5U.machines.industrialapiary.autoqueen.tooltip").text)
                            .tooltipShowUpTimer(TOOLTIP_DELAY))
                    .child(
                        new ButtonWidget<>()
                            .syncHandler(new InteractionSyncHandler().setOnMousePressed(_ -> machine.cancelProcess()))
                            .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                            .addTooltipStringLines(
                                machine.mTooltipCache.getData("GT5U.machines.industrialapiary.cancel.tooltip").text))
                    .tooltipShowUpTimer(TOOLTIP_DELAY))
            .child(
                createAutoOutputButton(
                    syncManager,
                    "itemAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM,
                    BaseTileEntity.ITEM_TRANSFER_TOOLTIP))
            .child(speedButton.onMousePressed(mouseButton -> {
                int maxSpeed = maxSpeedSyncer.getIntValue();
                if (Interactable.hasShiftDown()) {
                    // toggle lock
                    speedLockedSyncer.setBoolValue(!speedLockedSyncer.getBoolValue());

                    // if lock is enabled, set speed to max
                    if (speedLockedSyncer.getBoolValue()) speedSyncer.setIntValue(maxSpeed);
                } else {
                    // if locked, do nothing
                    if (speedLockedSyncer.getBoolValue()) return true;

                    int speed = speedSyncer.getIntValue();
                    if (mouseButton == 0) {
                        speed++;
                        if (speed > maxSpeed) speed = 0;
                    }
                    if (mouseButton == 1) {
                        speed--;
                        if (speed < 0) speed = maxSpeed;
                    }
                    speedSyncer.setIntValue(speed);
                }
                return true;
            })
                .tooltipDynamic(
                    t -> t.addStringLines(
                        machine.mTooltipCache.getUncachedTooltipData(
                            speedLockedSyncer.getBoolValue() ? "GT5U.machines.industrialapiary.speedlocked.tooltip"
                                : "GT5U.machines.industrialapiary.speed.tooltip",
                            machine.getAcceleration(),
                            formatNumber(machine.getAdditionalEnergyUsage())).text))
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .child(
                    Flow.column()
                        .horizontalCenter()
                        .coverChildren()
                        .child(
                            IKey.str("x")
                                .color(Color.GREY.darker(2))
                                .alignment(Alignment.CENTER)
                                .asWidget()
                                .width(18)
                                .scale(0.9f))
                        .child(
                            IKey.dynamic(() -> String.valueOf(machine.getAcceleration()))
                                .color(Color.GREY.darker(2))
                                .alignment(Alignment.CENTER)
                                .asWidget()
                                .width(18)
                                .scale(0.9f))));
    }

    @Override
    protected ItemSlot createChargerSlot() {
        return super.createChargerSlot().bottomRel(0);
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .bottomRel(0)
            .rightRel(0)
            .child(createErrorIcon(panel, syncManager).marginRight(SLOT_SIZE / 2))
            .child(createSpecialSlot())
            .child(makeLogoWidget());
    }

    private boolean isValidForQueenSlot(ItemStack itemStack) {
        return BeeManager.beeRoot.isMember(itemStack, EnumBeeType.QUEEN.ordinal())
            || BeeManager.beeRoot.isMember(itemStack, EnumBeeType.PRINCESS.ordinal());
    }

    private boolean isValidForDroneSlot(ItemStack itemStack) {
        return BeeManager.beeRoot.isMember(itemStack, EnumBeeType.DRONE.ordinal());
    }
}
