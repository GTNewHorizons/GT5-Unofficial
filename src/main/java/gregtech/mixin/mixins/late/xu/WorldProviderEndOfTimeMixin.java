package gregtech.mixin.mixins.late.xu;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;

import com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime;

@Mixin(WorldProviderEndOfTime.class)
@SuppressWarnings("unused")
public abstract class WorldProviderEndOfTimeMixin extends WorldProvider {

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return false;
    }
}
