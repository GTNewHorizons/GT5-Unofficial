package gregtech.mixin.mixins.late.galacticraftcore;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionConfig;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(EntityTieredRocket.class)
public abstract class MixinGalacticraftRocketPollution extends EntityAutoRocket implements IRocketType {

    private MixinGalacticraftRocketPollution(World world) {
        super(world);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void gt5u$addRocketPollution(CallbackInfo ci) {
        if (this.worldObj.isRemote || !(launchPhase == EnumLaunchPhase.LAUNCHED.ordinal()
            || launchPhase == EnumLaunchPhase.IGNITED.ordinal())) {
            return;
        }

        int pollutionAmount = 0;
        if (launchPhase == EnumLaunchPhase.LAUNCHED.ordinal()) {
            pollutionAmount = PollutionConfig.rocketPollutionAmount * this.getRocketTier();
        } else if (launchPhase == EnumLaunchPhase.IGNITED.ordinal()) {
            pollutionAmount = PollutionConfig.rocketPollutionAmount * this.getRocketTier() / 100;
        }
        Pollution.addPollution(worldObj.getChunkFromBlockCoords((int) posX, (int) posZ), pollutionAmount);
    }
}
