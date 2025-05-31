package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.api.enums.ToolModes;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.common.handlers.WrenchPrecisionModeClientState;

@Mixin(value = PlayerControllerMP.class)
public class WrenchPrecisionMixin {

    // Access to Minecraft instance so we can get the client player
    @Shadow
    private Minecraft mc;

    /**
     * Inject into the block-breaking handler on the client side.
     * Prevents repeated breaking when using wrench in "Precision Mode"
     */
    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    private void limitBreak(int x, int y, int z, int side, CallbackInfo ci) {
        if (WrenchPrecisionModeClientState.canBreak()) {
            WrenchPrecisionModeClientState.markBroken();
            return;
        }

        // Check that the player is holding a wrench in precision mode
        EntityPlayerSP player = mc.thePlayer;
        if (player == null) return;

        ItemStack heldItem = player.getHeldItem();
        if (heldItem == null) return;

        final int mode = MetaGeneratedTool.getToolMode(heldItem);
        if (mode != ToolModes.WRENCH_PRECISION.get()) return;

        // Cancel the block break attempt
        ci.cancel();
    }
}
