package gregtech.crossmod.backhand;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import xonin.backhand.api.core.BackhandUtils;

public class Backhand {

    @Nullable
    public static ItemStack getOffhandItem(EntityPlayer player) {
        return BackhandUtils.getOffhandItem(player);
    }
}
