package gregtech.mixin.mixins.late.thaumcraft;

import net.minecraft.tileentity.TileEntity;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionConfig;
import thaumcraft.common.tiles.TileAlchemyFurnace;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(TileAlchemyFurnace.class)
public abstract class MixinThaumcraftAlchemyFurnacePollution extends TileEntity {

    @Inject(
        method = "updateEntity",
        at = @At(
            value = "FIELD",
            target = "thaumcraft/common/tiles/TileAlchemyFurnace.furnaceBurnTime:I",
            opcode = Opcodes.PUTFIELD,
            ordinal = 0,
            remap = false))
    private void gt5u$addPollution(CallbackInfo ci) {
        if (!this.worldObj.isRemote && (this.worldObj.getTotalWorldTime() % 20) == 0) {
            Pollution.addPollution(
                this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord),
                PollutionConfig.furnacePollutionAmount);
        }
    }
}
