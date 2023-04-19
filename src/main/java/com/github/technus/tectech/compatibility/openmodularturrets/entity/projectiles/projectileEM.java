package com.github.technus.tectech.compatibility.openmodularturrets.entity.projectiles;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import openmodularturrets.entity.projectiles.LaserProjectile;
import openmodularturrets.entity.projectiles.damagesources.NormalDamageSource;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turretbase.TurretBase;
import openmodularturrets.util.PlayerUtil;
import openmodularturrets.util.TurretHeadUtil;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMHadronDefinition;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GT_Utility;

/**
 * Created by Bass on 27/07/2017.
 */
public class projectileEM extends LaserProjectile {

    public float gravity = 0;
    private TurretBase turretBase;

    private boolean strange, antiMatter, isAmped;

    private int ampLevel;

    private float massFactor;
    private double mass, charge;

    public projectileEM(World par1World) {
        super(par1World);
    }

    public projectileEM(World par1World, TurretBase turretBase) {
        super(par1World, turretBase);
        int amp = TurretHeadUtil.getAmpLevel(turretBase);
        if (amp > 0) {
            this.isAmped = true;
            this.ampLevel = amp;
        }
    }

    public projectileEM(World par1World, TurretBase turretBase, EMInstanceStack projectileContent) {
        super(par1World, turretBase);
        this.turretBase = turretBase;
        if (projectileContent != null) {
            mass = projectileContent.getMass();
            charge = projectileContent.getCharge();
            massFactor = (float) (projectileContent.getDefinition().getMass() / EMHadronDefinition.hadron_n_.getMass());

            if (projectileContent.getDefinition().getGeneration() > 1
                    || projectileContent.getDefinition().getGeneration() < -1) {
                strange = true;
            }
            if (projectileContent.getDefinition().getGeneration() < 0) {
                antiMatter = true;
            }

            if (projectileContent.getDefinition().getCharge() == 0) {
                gravity = massFactor / 100f;
            } else {
                gravity = Math
                        .min(0.0025F / Math.abs(projectileContent.getDefinition().getCharge()), massFactor / 100f);
            }
        }
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if (ticksExisted > 1) {
            if (!worldObj.isRemote) {
                worldObj.playSoundEffect(
                        posX,
                        posY,
                        posZ,
                        "openmodularturrets:laserHit",
                        ConfigHandler.getTurretSoundVolume(),
                        TecTech.RANDOM.nextFloat() + 0.5F);
                switch (movingobjectposition.typeOfHit) {
                    case BLOCK:
                        Block hitBlock = worldObj.getBlock(
                                movingobjectposition.blockX,
                                movingobjectposition.blockY,
                                movingobjectposition.blockZ);
                        if (hitBlock != null) {
                            if (TecTech.configTecTech.ENABLE_TURRET_EXPLOSIONS && antiMatter
                                    && hitBlock.getMaterial().isSolid()) {
                                GT_Utility.sendSoundToPlayers(
                                        worldObj,
                                        SoundResource.IC2_MACHINES_MACHINE_OVERLOAD,
                                        1.0F,
                                        -1.0F,
                                        movingobjectposition.blockX,
                                        movingobjectposition.blockY,
                                        movingobjectposition.blockZ);
                                worldObj.createExplosion(
                                        null,
                                        movingobjectposition.blockX + 0.5D,
                                        movingobjectposition.blockY + 0.5D,
                                        movingobjectposition.blockZ + 0.5D,
                                        TecTech.configTecTech.TURRET_EXPLOSION_FACTOR * (strange ? 10 : 1)
                                                * massFactor
                                                * (isAmped ? ampLevel * .1f + 1 : 1)
                                                * (ticksExisted / 250f),
                                        true);
                            } else {
                                return;
                            }
                        }
                        break;
                    case ENTITY:
                        float damage = (strange ? 10 : 1) * TecTech.configTecTech.TURRET_DAMAGE_FACTOR
                                * massFactor
                                * (isAmped ? ampLevel * .1f + 1 : 1);

                        if (movingobjectposition.entityHit instanceof EntityPlayer) {
                            EntityPlayer player = (EntityPlayer) movingobjectposition.entityHit;
                            if (canDamagePlayer(player)) {
                                movingobjectposition.entityHit.setFire((strange ? 10 : 1) * 2);
                                movingobjectposition.entityHit
                                        .attackEntityFrom(new NormalDamageSource("laser"), damage);
                                if (antiMatter) {
                                    movingobjectposition.entityHit.hurtResistantTime = 0;
                                }
                                if (strange) {
                                    TecTech.anomalyHandler.addCancer(player, mass);
                                }
                                if (charge != 0) {
                                    TecTech.anomalyHandler.addCharge(player, charge);
                                }
                            }
                        } else {
                            movingobjectposition.entityHit.setFire((strange ? 10 : 1) * 2);
                            movingobjectposition.entityHit.attackEntityFrom(new NormalDamageSource("laser"), damage);
                            if (antiMatter) {
                                movingobjectposition.entityHit.hurtResistantTime = 0;
                            }
                        }

                        if (TecTech.configTecTech.ENABLE_TURRET_EXPLOSIONS && antiMatter) {
                            GT_Utility.sendSoundToPlayers(
                                    worldObj,
                                    SoundResource.IC2_MACHINES_MACHINE_OVERLOAD,
                                    1.0F,
                                    -1.0F,
                                    (int) movingobjectposition.entityHit.posX,
                                    (int) movingobjectposition.entityHit.posY,
                                    (int) movingobjectposition.entityHit.posZ);
                            worldObj.createExplosion(
                                    null,
                                    movingobjectposition.entityHit.posX,
                                    movingobjectposition.entityHit.posY,
                                    movingobjectposition.entityHit.posZ,
                                    (strange ? 10 : 1) * TecTech.configTecTech.TURRET_EXPLOSION_FACTOR
                                            * massFactor
                                            * (isAmped ? ampLevel * .1f + 1 : 1)
                                            * (ticksExisted / 250f),
                                    true);
                        }
                        break;
                    default:
                        break;
                }
            }
            setDead();
        }
    }

    public boolean canDamagePlayer(EntityPlayer entityPlayer) {
        return ConfigHandler.turretDamageTrustedPlayers || this.turretBase.getTrustedPlayer(entityPlayer.getUniqueID())
                == null
                && !PlayerUtil.getPlayerUIDUnstable(this.turretBase.getOwner()).equals(entityPlayer.getUniqueID());
    }

    @Override
    public void onEntityUpdate() {
        if (ticksExisted >= 75) {
            setDead();
        }
    }

    @Override
    protected float getGravityVelocity() {
        return gravity;
    }
}
