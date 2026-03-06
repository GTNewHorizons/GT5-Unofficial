package kubatech.tileentity.gregtech.multiblock;

import static kubatech.api.enums.ItemList.KubaFakeItemEECVoid;

import net.minecraft.item.Item;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import crazypants.enderio.EnderIO;
import gregtech.api.modularui2.GTGuiTextures;

public class MTEExtremeEntityCrusherGui extends KubaTechGTMultiBlockBaseGui<MTEExtremeEntityCrusher> {

    private static final Item poweredSpawnerItem = Item.getItemFromBlock(EnderIO.blockPoweredSpawner);
    private static final UITexture SLOT_EEC_SPAWNER = getKubaUITexture("gui/slot/gray_spawner");
    private static final UITexture SLOT_EEC_SWORD = getKubaUITexture("gui/slot/gray_sword");

    public MTEExtremeEntityCrusherGui(MTEExtremeEntityCrusher multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .child(
                new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex())
                            .filter(stack -> stack.getItem() == poweredSpawnerItem)
                            .slotGroup("item_inv"))
                    .background(GTGuiTextures.SLOT_ITEM_DARK, SLOT_EEC_SPAWNER))
            .child(
                new ItemSlot().slot(new ModularSlot(multiblock.weaponCache, 0))
                    .marginTop(4)
                    .background(GTGuiTextures.SLOT_ITEM_DARK, SLOT_EEC_SWORD))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));

    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(mIsProducingInfernalDrops_name, mIsProducingInfernalDrops);
        syncManager.syncValue(voidAllDamagedAndEnchantedItems_name, voidAllDamagedAndEnchantedItems);
        syncManager.syncValue(mPreserveWeapon_name, mPreserveWeapon);
        syncManager.syncValue(mCycleWeapons_name, mCycleWeapons);
        syncManager.syncValue(isInRitualMode_name, isInRitualMode);
    }

    public final BooleanSyncValue mIsProducingInfernalDrops = new BooleanSyncValue(
        multiblock::isProducingInfernalDrops,
        multiblock::setIsProducingInfernalDrops);
    public final String mIsProducingInfernalDrops_name = "mIsProducingInfernalDrops";

    public final BooleanSyncValue voidAllDamagedAndEnchantedItems = new BooleanSyncValue(
        multiblock::isVoidAllDamagedAndEnchantedItems,
        multiblock::setVoidAllDamagedAndEnchantedItems);
    public final String voidAllDamagedAndEnchantedItems_name = "voidAllDamagedAndEnchantedItems";

    public final BooleanSyncValue mPreserveWeapon = new BooleanSyncValue(
        multiblock::isPreserveWeapon,
        multiblock::setPreserveWeapon);
    public final String mPreserveWeapon_name = "mPreserveWeapon";

    public final BooleanSyncValue mCycleWeapons = new BooleanSyncValue(
        multiblock::isCycleWeapons,
        multiblock::setCycleWeapons);
    public final String mCycleWeapons_name = "mCycleWeapons";

    public final BooleanSyncValue isInRitualMode = new BooleanSyncValue(
        multiblock::isInRitualMode,
        multiblock::setIsInRitualMode);
    public final String isInRitualMode_name = "isInRitualMode";

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createLeftPanelGapRow(parent, syncManager)
            // EEC Buttons
            .child(new PreserveWeaponButton(syncManager))
            .child(new CycleWeaponsButton(syncManager))
            .child(new VoidDamagedAndEnchantedButton(syncManager))
            .child(new allowInfernalDropButton(syncManager))
            .child(new RitualModeButton(syncManager));
    }

    private class PreserveWeaponButton extends KubaCycleButtonWidget {

        protected PreserveWeaponButton(PanelSyncManager syncManager) {
            super(() -> _yesNo(mPreserveWeapon, OVERLAY_BUTTON_ON, OVERLAY_BUTTON_OFF));
            this.size(18, 18)
                .syncHandler(mPreserveWeapon_name)
                .length(2)
                .tooltipBuilder(this::createTooltip);
        }

        private static final UITexture OVERLAY_BUTTON_ON = getKubaUITextureAlpha(
            "gui/overlay_button/machine_mode_eec_weapon_preservation_on");
        private static final UITexture OVERLAY_BUTTON_OFF = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_weapon_preservation_off");

        private void createTooltip(RichTooltip t) {
            addTranslatedLockableTooltips(t, "kubatech.gui.text.eec.preserve_weapon" + yesNo(mPreserveWeapon));
        }
    }

    private class CycleWeaponsButton extends KubaCycleButtonWidget {

        protected CycleWeaponsButton(PanelSyncManager syncManager) {
            super(() -> _yesNo(mCycleWeapons, OVERLAY_BUTTON_ON, OVERLAY_BUTTON_OFF));
            this.size(18, 18)
                .syncHandler(mCycleWeapons_name)
                .length(2)
                .tooltipBuilder(this::createTooltip);
        }

        private static final UITexture OVERLAY_BUTTON_ON = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_weapon_cycling_on");
        private static final UITexture OVERLAY_BUTTON_OFF = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_weapon_cycling_off");

        private void createTooltip(RichTooltip t) {
            addTranslatedLockableTooltips(
                t,
                "kubatech.gui.text.eec.cycle_weapons" + yesNo(mCycleWeapons),
                "kubatech.gui.text.eec.cycle_weapons.info");
        }
    }

    private class VoidDamagedAndEnchantedButton extends KubaCycleButtonWidget {

        protected VoidDamagedAndEnchantedButton(PanelSyncManager syncManager) {
            super(() -> _yesNo(voidAllDamagedAndEnchantedItems, OVERLAY_BUTTON_ON, OVERLAY_BUTTON_OFF));
            this.size(18, 18)
                .syncHandler(voidAllDamagedAndEnchantedItems_name)
                .length(2)
                .tooltipBuilder(this::createTooltip);
        }

        // Todo. Check if this is intended
        private static final IDrawable OVERLAY_BUTTON_ON = new ItemDrawable(KubaFakeItemEECVoid.get(1)).asIcon();
        private static final UITexture OVERLAY_BUTTON_OFF = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_void_damaged_and_enchanted_off");

        private void createTooltip(RichTooltip t) {
            addTranslatedLockableTooltips(
                t,
                "kubatech.gui.text.eec.void_all_damaged" + yesNo(voidAllDamagedAndEnchantedItems),
                "kubatech.gui.text.eec.void_all_damaged.warning");
        }
    }

    private class allowInfernalDropButton extends KubaCycleButtonWidget {

        protected allowInfernalDropButton(PanelSyncManager syncManager) {
            super(() -> _yesNo(mIsProducingInfernalDrops, OVERLAY_BUTTON_ON, OVERLAY_BUTTON_OFF));
            this.size(18, 18)
                .syncHandler(mIsProducingInfernalDrops_name)
                .length(2)
                .tooltipBuilder(this::createTooltip);
        }

        private static final UITexture OVERLAY_BUTTON_ON = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_spawn_infernals_on");
        private static final UITexture OVERLAY_BUTTON_OFF = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_spawn_infernals_off");

        private void createTooltip(RichTooltip t) {
            addTranslatedLockableTooltips(
                t,
                "kubatech.gui.text.eec.infernal_drop" + yesNo(mIsProducingInfernalDrops),
                "kubatech.gui.text.eec.infernal_drop_always");
        }
    }

    private class RitualModeButton extends KubaCycleButtonWidget {

        protected RitualModeButton(PanelSyncManager syncManager) {
            super(() -> _yesNo(isInRitualMode, OVERLAY_BUTTON_ON, OVERLAY_BUTTON_OFF));
            this.size(18, 18)
                .syncHandler(isInRitualMode_name)
                .length(2)
                .tooltipBuilder(this::createTooltip);
        }

        private static final UITexture OVERLAY_BUTTON_ON = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_ritual_mode_on");
        private static final UITexture OVERLAY_BUTTON_OFF = getKubaUITexture(
            "gui/overlay_button/machine_mode_eec_ritual_mode_off");

        private void createTooltip(RichTooltip t) {
            addTranslatedLockableTooltips(t, "kubatech.gui.text.eec.ritual_mode" + yesNo(isInRitualMode));
        }
    }

    private <T> T _yesNo(BooleanSyncValue Value, T yes, T no) {
        return Value.getValue() ? yes : no;
    }

    private String yesNo(BooleanSyncValue v) {
        return _yesNo(v, "_on", "_off");
    }
}
