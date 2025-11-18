package kubatech.api.gui;

import static kubatech.api.enums.ItemList.KubaFakeItemEECVoid;

import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import kubatech.Tags;

public class KubaTechUITextures {

    public static final UITexture PICTURE_KUBATECH_LOGO = UITexture.fullImage(Tags.MODID, "gui/logo_13x15_dark");

    public static final UITexture SLOT_EEC_SPAWNER = UITexture.fullImage(Tags.MODID, "gui/slot/gray_spawner");

    public static final UITexture SLOT_EEC_SWORD = UITexture.fullImage(Tags.MODID, "gui/slot/gray_sword");

    public static final UITexture OVERLAY_BUTTON_EEC_RITUAL_MODE_ON = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_ritual_mode_on");

    public static final UITexture OVERLAY_BUTTON_EEC_RITUAL_MODE_OFF = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_ritual_mode_off");

    public static final UITexture OVERLAY_BUTTON_EEC_SPAWN_INFERNALS_ON = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_spawn_infernals_on");

    public static final UITexture OVERLAY_BUTTON_EEC_SPAWN_INFERNALS_OFF = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_spawn_infernals_off");

    public static final ItemDrawable OVERLAY_BUTTON_EEC_VOID_DAMAGED_AND_ENCHANTED_ON = new ItemDrawable(
        KubaFakeItemEECVoid.get(1L));

    public static final UITexture OVERLAY_BUTTON_EEC_VOID_DAMAGED_AND_ENCHANTED_OFF = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_void_damaged_and_enchanted_off");

    public static final UITexture OVERLAY_BUTTON_EEC_WEAPON_CYCLING_ON = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_weapon_cycling_on");

    public static final UITexture OVERLAY_BUTTON_EEC_WEAPON_CYCLING_OFF = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_weapon_cycling_off");

    public static final UITexture OVERLAY_BUTTON_EEC_WEAPON_PRESERVATION_ON = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_weapon_preservation_on");

    public static final UITexture OVERLAY_BUTTON_EEC_WEAPON_PRESERVATION_OFF = UITexture
        .fullImage(Tags.MODID, "gui/overlay_button/machine_mode_eec_weapon_preservation_off");

    public static final UITexture SLOT_FUSION_CRAFTER = UITexture.fullImage(Tags.MODID, "gui/slot/fusion_crafter");

    public static final UITexture APIARY_INVENTORY_BACKGROUND = UITexture
        .fullImage(Tags.MODID, "gui/apiary_inventory_background");
}
