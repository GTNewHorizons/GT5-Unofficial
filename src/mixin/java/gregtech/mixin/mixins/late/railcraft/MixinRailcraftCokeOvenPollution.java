package gregtech.mixin.mixins.late.railcraft;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionConfig;
import mods.railcraft.common.blocks.machine.MultiBlockPattern;
import mods.railcraft.common.blocks.machine.TileMultiBlock;
import mods.railcraft.common.blocks.machine.TileMultiBlockOven;
import mods.railcraft.common.blocks.machine.alpha.TileBlastFurnace;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(TileMultiBlockOven.class)
public abstract class MixinRailcraftCokeOvenPollution extends TileMultiBlock {

    @Shadow(remap = false)
    protected boolean cooking;

    private MixinRailcraftCokeOvenPollution(List<? extends MultiBlockPattern> patterns) {
        super(patterns);
    }

    @Inject(method = "updateEntity", at = @At("HEAD"))
    private void gt5u$addPollution(CallbackInfo ci) {
        if (this.worldObj.isRemote || !this.cooking || !this.isMaster) return;
        if ((this.worldObj.getTotalWorldTime() % 20) == 0) {
            final int pollution = (((TileMultiBlock) this) instanceof TileBlastFurnace)
                ? PollutionConfig.advancedCokeOvenPollutionAmount
                : PollutionConfig.cokeOvenPollutionAmount;
            Pollution.addPollution(this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord), pollution);
        }
    }
}
