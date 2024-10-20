package gregtech.mixin.mixins.late.railcraft;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionConfig;
import mods.railcraft.common.carts.EntityTunnelBore;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(EntityTunnelBore.class)
public abstract class MixinRailcraftTunnelBorePollution extends EntityMinecart {

    @Shadow(remap = false)
    private boolean active;

    private MixinRailcraftTunnelBorePollution(World world) {
        super(world);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void gt5u$addPollution(CallbackInfo ci) {
        if (!worldObj.isRemote || !active) return;
        Pollution.addPollution(
            worldObj.getChunkFromBlockCoords((int) posX, (int) posZ),
            PollutionConfig.tunnelBorePollutionAmount);
    }
}
