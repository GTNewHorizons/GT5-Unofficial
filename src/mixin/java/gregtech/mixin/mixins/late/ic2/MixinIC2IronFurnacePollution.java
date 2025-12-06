package gregtech.mixin.mixins.late.ic2;

import static gregtech.common.pollution.PollutionHelper.furnaceAddPollutionOnUpdate;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.FurnacePollution;
import ic2.core.block.machine.tileentity.TileEntityIronFurnace;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(value = TileEntityIronFurnace.class, remap = false)
public abstract class MixinIC2IronFurnacePollution extends TileEntity {

    @Shadow
    public abstract boolean isBurning();

    @Inject(method = "updateEntityServer", at = @At("RETURN"))
    private void gt5u$updateEntityServer(CallbackInfo ci) {
        if (isBurning()) {
            furnaceAddPollutionOnUpdate(
                this.worldObj,
                this.xCoord,
                this.zCoord,
                FurnacePollution.IRON_FURNACE.getPollution());
        }
    }
}
