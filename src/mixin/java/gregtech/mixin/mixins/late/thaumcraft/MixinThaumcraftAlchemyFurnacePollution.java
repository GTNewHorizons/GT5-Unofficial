package gregtech.mixin.mixins.late.thaumcraft;

import static gregtech.common.pollution.PollutionHelper.furnaceAddPollutionOnUpdate;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.FurnacePollution;
import thaumcraft.common.tiles.TileAlchemyFurnace;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(value = TileAlchemyFurnace.class, remap = false)
public abstract class MixinThaumcraftAlchemyFurnacePollution extends TileEntity {

    @Shadow(remap = false)
    public abstract boolean isBurning();

    @Inject(method = "updateEntity", at = @At("RETURN"), remap = true)
    private void gt5u$addPollution(CallbackInfo ci) {
        if (isBurning()) {
            furnaceAddPollutionOnUpdate(
                this.worldObj,
                this.xCoord,
                this.zCoord,
                FurnacePollution.ALCHEMICAL_FURNACE.getPollution());
        }
    }
}
