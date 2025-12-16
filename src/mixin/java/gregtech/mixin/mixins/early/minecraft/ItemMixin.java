package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

import gregtech.api.covers.CoverRegistry;

@Mixin(Item.class)
public class ItemMixin {

    @ModifyReturnValue(method = "doesSneakBypassUse", at = @At("RETURN"), remap = false)
    private boolean gt5u$checkCoverShift(boolean original, @Local(argsOnly = true) EntityPlayer player) {
        // player.getHeldItem() has already been null checked
        if (CoverRegistry.isCover(player.getHeldItem())) {
            return true;
        }
        return original;
    }

}
