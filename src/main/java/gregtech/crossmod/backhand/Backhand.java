package gregtech.crossmod.backhand;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.Mods;
import xonin.backhand.api.core.BackhandUtils;

public class Backhand {

    @Nullable
    public static ItemStack getOffhandItem(EntityPlayer player) {
        if (Mods.Backhand.isModLoaded()) {
            return BackhandUtils.getOffhandItem(player);
        }

        return null;
    }
}
