package com.github.technus.tectech.thing.metaTileEntity.entity;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.LaserProjectile;
import openmodularturrets.entity.projectiles.TurretProjectile;
import openmodularturrets.tileentity.turretbase.TurretBase;

/**
 * Created by Bass on 27/07/2017.
 */
public class projectileEM extends LaserProjectile {
    public projectileEM(World par1World) {
        super(par1World);
        this.gravity = 0.0F;
    }

    public projectileEM(World par1World, TurretBase turretBase) {
        super(par1World, turretBase);
        this.gravity = 0.0F;
    }
}
