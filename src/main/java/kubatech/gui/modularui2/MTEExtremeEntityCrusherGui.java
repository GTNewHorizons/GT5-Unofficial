package kubatech.gui.modularui2;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import kubatech.api.implementations.KubaTechGTMultiBlockBaseGUI;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeEntityCrusher;

public class MTEExtremeEntityCrusherGui extends KubaTechGTMultiBlockBaseGUI<MTEExtremeEntityCrusher> {

    private static final IDrawable OVERLAY_EEC_VOID_DAMAGED_ON = new ItemDrawable(
        kubatech.api.enums.ItemList.KubaFakeItemEECVoid.get(1L));

    private BooleanSyncValue preserveWeaponSyncer;
    private BooleanSyncValue cycleWeaponsSyncer;
    private BooleanSyncValue voidDamagedSyncer;
    private BooleanSyncValue infernalDropsSyncer;
    private BooleanSyncValue ritualModeSyncer;
    private BooleanSyncValue ritualValidSyncer;
    private BooleanSyncValue preventingGUIWeaponUseSyncer;
    private IntSyncValue maxProgressSyncer;
    private StringSyncValue biodomeDimSyncer;

    public MTEExtremeEntityCrusherGui(MTEExtremeEntityCrusher multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        preserveWeaponSyncer = new BooleanSyncValue(multiblock::isPreserveWeapon, multiblock::setPreserveWeapon)
            .allowC2S();
        syncManager.syncValue("eecPreserveWeapon", preserveWeaponSyncer);

        cycleWeaponsSyncer = new BooleanSyncValue(multiblock::isCycleWeapons, multiblock::setCycleWeapons).allowC2S();
        syncManager.syncValue("eecCycleWeapons", cycleWeaponsSyncer);

        voidDamagedSyncer = new BooleanSyncValue(multiblock::isVoidAllDamagedAndEnchantedItems, val -> {
            if (multiblock.mMaxProgresstime > 0) return;
            multiblock.setVoidAllDamagedAndEnchantedItems(val);
        }).allowC2S();
        syncManager.syncValue("eecVoidDamaged", voidDamagedSyncer);

        infernalDropsSyncer = new BooleanSyncValue(multiblock::isProducingInfernalDrops, val -> {
            if (multiblock.mMaxProgresstime > 0) return;
            multiblock.setProducingInfernalDrops(val);
        }).allowC2S();
        syncManager.syncValue("eecInfernalDrops", infernalDropsSyncer);

        ritualModeSyncer = new BooleanSyncValue(multiblock::isInRitualMode, val -> {
            if (multiblock.mMaxProgresstime > 0) return;
            multiblock.setInRitualMode(val);
            multiblock.checkRitualConnection();
        }).allowC2S();
        syncManager.syncValue("eecRitualMode", ritualModeSyncer);

        ritualValidSyncer = new BooleanSyncValue(multiblock::isRitualValid, multiblock::setRitualValid);
        syncManager.syncValue("eecRitualValid", ritualValidSyncer);

        preventingGUIWeaponUseSyncer = new BooleanSyncValue(
            multiblock::isPreventingGUIWeaponUse,
            multiblock::setPreventingGUIWeaponUse);
        syncManager.syncValue("eecPreventGUIWeapon", preventingGUIWeaponUseSyncer);

        maxProgressSyncer = new IntSyncValue(
            () -> multiblock.mMaxProgresstime,
            val -> multiblock.mMaxProgresstime = val);
        syncManager.syncValue("eecMaxProgress", maxProgressSyncer);

        biodomeDimSyncer = new StringSyncValue(multiblock::getBiodomeDimensionName);
        syncManager.syncValue("biodomeDimension", biodomeDimSyncer);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(
                    () -> StatCollector
                        .translateToLocalFormatted("GT5U.gui.text.biodome_active", biodomeDimSyncer.getValue()))
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .fullWidth()
                    .setEnabledIf(
                        w -> !biodomeDimSyncer.getValue()
                            .isEmpty()))
            .child(
                IKey.dynamic(
                    () -> ritualValidSyncer.getBoolValue()
                        ? StatCollector.translateToLocal("kubatech.gui.text.eec.ritual_mode_connected")
                        : EnumChatFormatting.DARK_RED
                            + StatCollector.translateToLocal("kubatech.gui.text.eec.ritual_mode_error"))
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .fullWidth()
                    .setEnabledIf(w -> ritualModeSyncer.getBoolValue()))
            .child(
                IKey.str(StatCollector.translateToLocal("kubatech.gui.text.eec.preserve_weapon_warning"))
                    .color(0xFFFFFF00)
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .fullWidth()
                    .setEnabledIf(
                        w -> preventingGUIWeaponUseSyncer.getBoolValue() && !ritualModeSyncer.getBoolValue()
                            && maxProgressSyncer.getIntValue() > 0));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager).child(createPreserveWeaponButton())
            .child(createCycleWeaponsButton())
            .child(createVoidDamagedButton())
            .child(createInfernalDropsButton())
            .child(createRitualModeButton());
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(com.cleanroommc.modularui.utils.Alignment.MainAxis.END)
            .reverseLayout(true)
            .childIf(
                multiblock.doesBindPlayerInventory(),
                () -> new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex())
                            .singletonSlotGroup())
                    .background(GTGuiTextures.SLOT_ITEM_DARK, GTGuiTextures.SLOT_EEC_SPAWNER)
                    .marginTop(4))
            .child(
                new ItemSlot().slot(
                    new ModularSlot(multiblock.inventoryHandler, 0).filter(MTEExtremeEntityCrusher::isUsableWeapon)
                        .singletonSlotGroup()
                        .changeListener((newItem, onlyAmountChanged, client, init) -> {
                            if (!client) {
                                multiblock.updateWeaponCache();
                            }
                        }))
                    .background(GTGuiTextures.SLOT_ITEM_DARK, GTGuiTextures.SLOT_EEC_SWORD)
                    .marginTop(4))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));
    }

    private ToggleButton createPreserveWeaponButton() {
        return new ToggleButton().value(preserveWeaponSyncer)
            .overlay(
                new DynamicDrawable(
                    () -> preserveWeaponSyncer.getBoolValue() ? GTGuiTextures.OVERLAY_EEC_WEAPON_PRESERVATION_ON
                        : GTGuiTextures.OVERLAY_EEC_WEAPON_PRESERVATION_OFF))
            .tooltipBuilder(t -> {
                t.setAutoUpdate(true);
                t.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocal(
                            "kubatech.gui.text.eec.preserve_weapon"
                                + (preserveWeaponSyncer.getBoolValue() ? "_on" : "_off"))));
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ToggleButton createCycleWeaponsButton() {
        return new ToggleButton().value(cycleWeaponsSyncer)
            .overlay(
                new DynamicDrawable(
                    () -> cycleWeaponsSyncer.getBoolValue() ? GTGuiTextures.OVERLAY_EEC_WEAPON_CYCLING_ON
                        : GTGuiTextures.OVERLAY_EEC_WEAPON_CYCLING_OFF))
            .tooltipBuilder(t -> {
                t.setAutoUpdate(true);
                t.addLine(
                    IKey.dynamic(
                        () -> StatCollector.translateToLocal(
                            "kubatech.gui.text.eec.cycle_weapons"
                                + (cycleWeaponsSyncer.getBoolValue() ? "_on" : "_off"))));
                t.addLine(
                    EnumChatFormatting.GRAY
                        + StatCollector.translateToLocal("kubatech.gui.text.eec.cycle_weapons.info"));
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ButtonWidget<?> createVoidDamagedButton() {
        return createMachineStatusButton(
            voidDamagedSyncer,
            OVERLAY_EEC_VOID_DAMAGED_ON,
            GTGuiTextures.OVERLAY_EEC_VOID_DAMAGED_OFF,
            "kubatech.gui.text.eec.void_all_damaged",
            StatCollector.translateToLocal("kubatech.gui.text.eec.void_all_damaged.warning"));
    }

    private ButtonWidget<?> createInfernalDropsButton() {
        return createMachineStatusButton(
            infernalDropsSyncer,
            GTGuiTextures.OVERLAY_EEC_SPAWN_INFERNALS_ON,
            GTGuiTextures.OVERLAY_EEC_SPAWN_INFERNALS_OFF,
            "kubatech.gui.text.eec.infernal_drop",
            StatCollector.translateToLocal("kubatech.gui.text.eec.infernal_drop_always"));
    }

    private ButtonWidget<?> createRitualModeButton() {
        return createMachineStatusButton(
            ritualModeSyncer,
            GTGuiTextures.OVERLAY_EEC_RITUAL_MODE_ON,
            GTGuiTextures.OVERLAY_EEC_RITUAL_MODE_OFF,
            "kubatech.gui.text.eec.ritual_mode",
            null);
    }

    private ButtonWidget<?> createMachineStatusButton(BooleanSyncValue syncer, IDrawable onOverlay,
        IDrawable offOverlay, String tooltipPrefix, String extraTooltip) {
        return new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
            IDrawable icon = syncer.getBoolValue() ? onOverlay : offOverlay;
            if (maxProgressSyncer.getIntValue() > 0)
                return new DrawableStack(icon, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
            return icon;
        }))
            .background(new DynamicDrawable(() -> {
                if (syncer.getBoolValue()) return GTGuiTextures.BUTTON_STANDARD_PRESSED;
                return GTGuiTextures.BUTTON_STANDARD;
            }))
            .onMousePressed(mouseButton -> {
                if (maxProgressSyncer.getIntValue() > 0) return true;
                syncer.setBoolValue(!syncer.getBoolValue(), true, true);
                return true;
            })
            .tooltipBuilder(t -> {
                t.setAutoUpdate(true);
                t.addLine(
                    IKey.dynamic(
                        () -> StatCollector
                            .translateToLocal(tooltipPrefix + (syncer.getBoolValue() ? "_on" : "_off"))));
                if (maxProgressSyncer.getIntValue() > 0) {
                    t.addLine(
                        EnumChatFormatting.DARK_RED
                            + StatCollector.translateToLocal("GT5U.gui.button.forbidden_while_running"));
                }
                if (extraTooltip != null) {
                    t.addLine(EnumChatFormatting.GRAY + extraTooltip);
                }
            })
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    @Override
    protected boolean shouldDisplayRecipeLock() {
        return false;
    }
}
