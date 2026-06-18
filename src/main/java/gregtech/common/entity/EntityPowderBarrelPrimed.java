package gregtech.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.World;

public class EntityPowderBarrelPrimed extends EntityTNTPrimed {

    public EntityPowderBarrelPrimed(World world) {
        super(world);
        this.fuse = 80;
    }

    public EntityPowderBarrelPrimed(World world, double x, double y, double z, EntityLivingBase igniter) {
        super(world, x, y, z, igniter);
    }
}
