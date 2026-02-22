package gregtech.mixin.mixins.early.minecraft.pollution;

import static gregtech.common.pollution.PollutionHelper.furnaceAddPollutionOnUpdate;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.FurnacePollution;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(TileEntityFurnace.class)
public abstract class MixinTileEntityFurnacePollution extends TileEntity {

    @Inject(
        method = "updateEntity",
        at = @At(
            value = "FIELD",
            target = "net/minecraft/tileentity/TileEntityFurnace.furnaceBurnTime:I",
            opcode = Opcodes.GETFIELD,
            ordinal = 2))
    private void gt5u$addPollution(CallbackInfo ci) {
        furnaceAddPollutionOnUpdate(this.worldObj, this.xCoord, this.zCoord, FurnacePollution.FURNACE.getPollution());
    }
}
