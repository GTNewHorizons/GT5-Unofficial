package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

public class GT_UITextures {

    private static final String MODID = "gregtech";

    public static final UITexture SINGLEBLOCK_BACKGROUND_DEFAULT =
            AdaptableUITexture.of(MODID, "gui/background/singleblock_background_default", 176, 166, 4);

    public static final UITexture SINGLEBLOCK_BACKGROUND_BRONZE =
            AdaptableUITexture.of(MODID, "gui/background/singleblock_background_bronze", 176, 166, 4);

    public static final UITexture SINGLEBLOCK_BACKGROUND_STEEL =
            AdaptableUITexture.of(MODID, "gui/background/singleblock_background_steel", 176, 166, 4);

    public static final AdaptableUITexture ITEM_SLOT_BRONZE =
            AdaptableUITexture.of(MODID, "gui/slot/item_bronze", 18, 18, 1);

    public static final AdaptableUITexture ITEM_SLOT_STEEL =
            AdaptableUITexture.of(MODID, "gui/slot/item_steel", 18, 18, 1);
}
