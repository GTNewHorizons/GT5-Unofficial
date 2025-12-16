package gtPlusPlus.xmod.gregtech.common.modularui2;

import static gregtech.api.enums.Mods.GTPlusPlus;

import com.cleanroommc.modularui.drawable.UITexture;

public final class GTPPGuiTextures {

    public static void init() {}

    public static final UITexture PICTURE_CANISTER_DARK = UITexture.builder()
        .location(GTPlusPlus.ID, "gui/overlay_slot/canister_dark")
        .fullImage()
        .canApplyTheme()
        .name(GTPPTextureIds.PICTURE_CANISTER_DARK)
        .build();
}
