package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.ChunkCacheAccessor;

@Mixin(ChunkCache.class)
public class ChunkCacheMixin implements ChunkCacheAccessor {

    @Shadow
    private World worldObj;

    @Override
    public World getWorld() {
        return worldObj;
    }
}
