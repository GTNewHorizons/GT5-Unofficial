package gregtech.mixin.mixins.early.forge;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import gregtech.api.items.MetaGeneratedTool;

@Mixin(value = ForgeHooks.class, remap = false)
public class ForgeHooksMixin {

    /**
     * Modifies the block strength.
     *
     * @param original the default block strength (the default return value)
     * @param block    the block to break
     * @param player   the player breaking the block
     * @param world    the world the block is in
     * @param x        the x coordinate of the block
     * @param y        the y coordinate of the block
     * @param z        the z coordinate of the block
     * @return the new block strength
     */
    @ModifyReturnValue(method = "blockStrength", at = @At("RETURN"))
    private static float gt$blockStrengthHack(float original, Block block, EntityPlayer player, World world, int x,
        int y, int z) {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null && stack.getItem() instanceof MetaGeneratedTool tool) {
            return tool.getBlockStrength(stack, block, player, world, x, y, z, original);
        }
        return original;
    }

}
