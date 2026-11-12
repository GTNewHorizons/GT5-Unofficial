package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.block.BlockPortal;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.GTMod;
import gregtech.api.util.GTSpawnEventHandler;

@Mixin(BlockPortal.class)
public class PortalSpawnCheckMixin {

    @Redirect(
        method = "updateTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemMonsterPlacer;spawnCreature(Lnet/minecraft/world/World;IDDD)Lnet/minecraft/entity/Entity;"))
    private Entity gt$blockRepelledPortalSpawn(World world, int id, double x, double y, double z) {
        GTSpawnEventHandler handler = GTMod.proxy.spawnEventHandler;
        if (handler != null && handler.isAreaProtected(world, x, y, z)) return null;
        return ItemMonsterPlacer.spawnCreature(world, id, x, y, z);
    }
}
