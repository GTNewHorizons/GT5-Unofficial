package gregtech.mixin.mixins.late.railcraft;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.pollution.Pollution;
import gregtech.common.pollution.PollutionConfig;
import mods.railcraft.common.blocks.RailcraftTileEntity;
import mods.railcraft.common.blocks.machine.TileMultiBlock;
import mods.railcraft.common.blocks.machine.beta.TileEngineSteamHobby;
import mods.railcraft.common.util.steam.SteamBoiler;

// Merged from ModMixins under the MIT License Copyright bartimaeusnek & GTNewHorizons
@Mixin(SteamBoiler.class)
public class MixinRailcraftBoilerPollution {

    @Shadow(remap = false)
    private RailcraftTileEntity tile;

    @Shadow(remap = false)
    protected boolean isBurning;

    @Inject(method = "tick", at = @At(value = "HEAD"), remap = false)
    private void gt5u$tick(int x, CallbackInfo ci) {
        if (!this.isBurning || this.tile == null || this.tile.getWorld() == null) return;
        final World world = this.tile.getWorldObj();
        if ((world.getTotalWorldTime() % 20) == 0) {
            int pollutionAmount;
            if (this.tile instanceof TileMultiBlock) {
                pollutionAmount = (((TileMultiBlock) this.tile).getComponents()
                    .size() - x) * PollutionConfig.fireboxPollutionAmount;
            } else if (this.tile instanceof TileEngineSteamHobby) {
                pollutionAmount = PollutionConfig.hobbyistEnginePollutionAmount;
            } else {
                pollutionAmount = 40;
            }
            Pollution.addPollution(world.getChunkFromBlockCoords(this.tile.getX(), this.tile.getZ()), pollutionAmount);
        }
    }
}
