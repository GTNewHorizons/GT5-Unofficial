package gregtech.mixin.mixins.late.efr;

import static gregtech.common.pollution.PollutionHelper.furnaceAddPollutionOnUpdate;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ganymedes01.etfuturum.tileentities.TileEntityBlastFurnace;
import gregtech.common.pollution.FurnacePollution;

@Mixin(value = TileEntityBlastFurnace.class, remap = false)
public abstract class MixinTileEntityBlastFurnacePollution extends TileEntity {

    @Shadow(remap = false)
    public abstract boolean isBurning();

    @Inject(method = "updateEntity", at = @At("RETURN"), remap = true)
    private void gt5u$addPollution(CallbackInfo ci) {
        if (isBurning()) {
            furnaceAddPollutionOnUpdate(
                this.worldObj,
                this.xCoord,
                this.zCoord,
                FurnacePollution.BLAST_FURNACE.getPollution());
        }
    }
}
