package com.github.technus.tectech.thing.metaTileEntity.entity;

import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.LaserProjectile;
import openmodularturrets.entity.projectiles.damagesources.NormalDamageSource;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turretbase.TurretBase;
import openmodularturrets.util.PlayerUtil;

import java.util.Random;

/**
 * Created by Bass on 27/07/2017.
 */
public class projectileEM extends LaserProjectile {
    private TurretBase turretBase;

    public projectileEM(World par1World) {
        super(par1World);
        this.gravity = 0.0F;
    }

    public projectileEM(World par1World, TurretBase turretBase, cElementalInstanceStackMap avalableEM) {
        super(par1World, turretBase);
        this.turretBase = turretBase;
        this.gravity = 0.0F;
    }

    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if(this.ticksExisted > 1) {
            if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                Block hitBlock = this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                if(hitBlock != null && !hitBlock.getMaterial().isSolid()) {
                    return;
                }
            }

            if(movingobjectposition.entityHit != null && !this.worldObj.isRemote) {
                Random random = new Random();
                this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "openmodularturrets:laserHit", ConfigHandler.getTurretSoundVolume(), random.nextFloat() + 0.5F);
                if(movingobjectposition.entityHit != null && !this.worldObj.isRemote) {
                    int damage = ConfigHandler.getLaserTurretSettings().getDamage();
                    if(movingobjectposition.entityHit instanceof EntityLivingBase) {
                        EntityLivingBase elb = (EntityLivingBase)movingobjectposition.entityHit;
                        damage = (int)((double)damage + (double)((int)elb.getHealth()) * 0.1D);
                    }

                    if(movingobjectposition.entityHit instanceof EntityPlayer) {
                        if(this.canDamagePlayer((EntityPlayer)movingobjectposition.entityHit)) {
                            movingobjectposition.entityHit.setFire(2);
                            movingobjectposition.entityHit.attackEntityFrom(new NormalDamageSource("laser"), (float)damage);
                            movingobjectposition.entityHit.hurtResistantTime = 0;
                        }
                    } else {
                        movingobjectposition.entityHit.setFire(2);
                        movingobjectposition.entityHit.attackEntityFrom(new NormalDamageSource("laser"), (float)damage);
                        movingobjectposition.entityHit.hurtResistantTime = 0;
                    }
                }
            }

            this.setDead();
        }
    }

    private boolean canDamagePlayer(EntityPlayer entityPlayer) {
        return ConfigHandler.turretDamageTrustedPlayers || this.turretBase.getTrustedPlayer(entityPlayer.getUniqueID()) == null && !PlayerUtil.getPlayerUIDUnstable(this.turretBase.getOwner()).equals(entityPlayer.getUniqueID());
    }
}
