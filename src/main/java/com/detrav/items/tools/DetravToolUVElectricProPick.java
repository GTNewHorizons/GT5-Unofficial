package com.detrav.items.tools;

import com.detrav.enums.Textures01;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravToolUVElectricProPick extends DetravToolZPMElectricProPick {
    public int getBaseQuality() {
        return 2;
    }

    public float getMaxDurabilityMultiplier() {
        return 4.0F;
    }

    public IIconContainer getIcon(boolean aIsToolHead, ItemStack aStack) {
        return Textures01.mTextures[2];
    }
}
