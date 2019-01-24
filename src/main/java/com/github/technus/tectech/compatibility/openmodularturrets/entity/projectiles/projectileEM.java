package com.github.technus.tectech.compatibility.openmodularturrets.entity.projectiles;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.hadron.dHadronDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eQuarkDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Utility;
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


/**
 * Created by Bass on 27/07/2017.
 */
public class projectileEM extends LaserProjectile {
    public float gravity=0;
    private TurretBase turretBase;

    private boolean exotic, antiMatter,isAmped;

    private int ampLevel;

    private float massFactor;

    public projectileEM(World par1World) {
        super(par1World);
    }

    public projectileEM(World par1World, TurretBase turretBase) {
        super(par1World, turretBase);
        int amp=TurretHeadUtil.getAmpLevel(turretBase);
        if (amp > 0) {
            this.isAmped = true;
            this.ampLevel = amp;
        }
    }

    public projectileEM(World par1World, TurretBase turretBase, cElementalInstanceStackMap avalableEM) {
        super(par1World, turretBase);
        this.turretBase = turretBase;
        boolean onlyQuarks=true;
        if(avalableEM!=null && avalableEM.hasStacks()) {
            for (cElementalInstanceStack stack : avalableEM.values()) {
                if (!(stack.definition instanceof eQuarkDefinition)) {
                    onlyQuarks = false;
                }
            }
            if (onlyQuarks) {
                avalableEM.clear();
            } else {
                cElementalInstanceStack consumeFromThis=avalableEM.get(TecTech.RANDOM.nextInt(avalableEM.size()));
                massFactor =consumeFromThis.definition.getMass()/ dHadronDefinition.hadron_n_.getMass();

                if(consumeFromThis.definition.getType()>1 || consumeFromThis.definition.getType()<-1) {
                    exotic = true;
                }
                if(consumeFromThis.definition.getType()<0) {
                    antiMatter = true;
                }

                if (consumeFromThis.definition.getCharge() == 0) {
                    gravity = massFactor / 100f;
                } else {
                    gravity = Math.min(0.0025F / Math.abs(consumeFromThis.definition.getCharge()), massFactor / 100f);
                }

                avalableEM.removeAmount(false,consumeFromThis.definition.getStackForm(1));
            }
        }
        //todo make the recipe require some overflow hatches

        //todo add more subspace pollution
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if(ticksExisted > 1) {
            if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                Block hitBlock = worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                if(hitBlock != null){
                    if (hitBlock.getMaterial().isSolid() && TecTech.configTecTech.ENABLE_TURRET_EXPLOSIONS && antiMatter) {
                        worldObj.playSoundEffect(posX, posY, posZ, "openmodularturrets:laserHit", ConfigHandler.getTurretSoundVolume(), TecTech.RANDOM.nextFloat() + 0.5F);
                        GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1.0F,
                                movingobjectposition.blockX,
                                movingobjectposition.blockY,
                                movingobjectposition.blockZ);
                        worldObj.createExplosion(null,
                                movingobjectposition.blockX + 0.5D,
                                movingobjectposition.blockY + 0.5D,
                                movingobjectposition.blockZ + 0.5D, (exotic?10:1) * TecTech.configTecTech.TURRET_EXPLOSION_FACTOR * massFactor * (isAmped? ampLevel*.1f +1:1) * (ticksExisted/250f), true);
                    } else {
                        return;
                    }
                }
            }

            if(movingobjectposition.entityHit != null && !worldObj.isRemote) {
                worldObj.playSoundEffect(posX, posY, posZ, "openmodularturrets:laserHit", ConfigHandler.getTurretSoundVolume(), TecTech.RANDOM.nextFloat() + 0.5F);
                if(movingobjectposition.entityHit != null && !worldObj.isRemote) {
                    float damage = (exotic?10:1) * TecTech.configTecTech.TURRET_DAMAGE_FACTOR * massFactor * (isAmped? ampLevel*.1f +1:1);

                    if(movingobjectposition.entityHit instanceof EntityPlayer) {
                        if(canDamagePlayer((EntityPlayer)movingobjectposition.entityHit)) {
                            movingobjectposition.entityHit.setFire((exotic?10:1)*2);
                            movingobjectposition.entityHit.attackEntityFrom(new NormalDamageSource("laser"), damage);
                            if(antiMatter) {
                                movingobjectposition.entityHit.hurtResistantTime = 0;
                            }
                        }
                    } else {
                        movingobjectposition.entityHit.setFire((exotic?10:1)*2);
                        movingobjectposition.entityHit.attackEntityFrom(new NormalDamageSource("laser"), damage);
                        if(antiMatter) {
                            movingobjectposition.entityHit.hurtResistantTime = 0;
                        }
                    }

                    if (TecTech.configTecTech.ENABLE_TURRET_EXPLOSIONS && antiMatter) {
                        GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1.0F,
                                (int)movingobjectposition.entityHit.posX,
                                (int)movingobjectposition.entityHit.posY,
                                (int)movingobjectposition.entityHit.posZ);
                        worldObj.createExplosion(null,
                                movingobjectposition.entityHit.posX,
                                movingobjectposition.entityHit.posY,
                                movingobjectposition.entityHit.posZ, (exotic?10:1) * TecTech.configTecTech.TURRET_EXPLOSION_FACTOR * massFactor * (isAmped? ampLevel*.1f +1:1) * (ticksExisted/250f), true);
                    }
                }
            }
            setDead();
        }
    }

    public boolean canDamagePlayer(EntityPlayer entityPlayer) {
        return ConfigHandler.turretDamageTrustedPlayers || this.turretBase.getTrustedPlayer(entityPlayer.getUniqueID()) == null && !PlayerUtil.getPlayerUIDUnstable(this.turretBase.getOwner()).equals(entityPlayer.getUniqueID());
    }

    @Override
    public void onEntityUpdate() {
        if(ticksExisted >= 75) {
            setDead();
        }
    }

    @Override
    protected float getGravityVelocity() {
        return gravity;
    }
}
