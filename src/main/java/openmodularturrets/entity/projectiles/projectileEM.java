package openmodularturrets.entity.projectiles;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.definitions.complex.dHadronDefinition;
import com.github.technus.tectech.elementalMatter.definitions.primitive.eQuarkDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.damagesources.NormalDamageSource;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turretbase.TurretBase;


/**
 * Created by Bass on 27/07/2017.
 */
public class projectileEM extends TurretProjectile {
    public float gravity=0;
    private TurretBase turretBase;

    private boolean exotic, antiMatter;

    private float massFactor;

    public projectileEM(World par1World) {
        super(par1World);
    }

    public projectileEM(World par1World, TurretBase turretBase) {
        super(par1World, turretBase);
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
                cElementalInstanceStack consumeFromThis=avalableEM.get(TecTech.Rnd.nextInt(avalableEM.size()));
                massFactor =consumeFromThis.definition.getMass()/ dHadronDefinition.hadron_n_.getMass();

                if(consumeFromThis.definition.getType()>1 || consumeFromThis.definition.getType()<-1) exotic = true;
                if(consumeFromThis.definition.getType()<0) antiMatter = true;

                if (consumeFromThis.definition.getCharge() == 0) this.gravity = massFactor/100f;
                else this.gravity = Math.min(0.0025F/Math.abs(consumeFromThis.definition.getCharge()),massFactor/100f);

                avalableEM.removeAmount(false,consumeFromThis.definition.getStackForm(1));
            }
        }
        //todo make the recipe require some overflow hatches

        //todo add more subspace pollution
    }

    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if(this.ticksExisted > 1) {
            if(movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                Block hitBlock = this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
                if(hitBlock != null){
                    if (hitBlock.getMaterial().isSolid() && TecTech.ModConfig.ENABLE_TURRET_EXPLOSIONS && antiMatter) {
                        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "openmodularturrets:laserHit", ConfigHandler.getTurretSoundVolume(), TecTech.Rnd.nextFloat() + 0.5F);
                        GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1.0F,
                                movingobjectposition.blockX,
                                movingobjectposition.blockY,
                                movingobjectposition.blockZ);
                        worldObj.createExplosion(null,
                                movingobjectposition.blockX + 0.5D,
                                movingobjectposition.blockY + 0.5D,
                                movingobjectposition.blockZ + 0.5D, (exotic?10:1) * TecTech.ModConfig.TURRET_EXPLOSION_FACTOR * massFactor * (isAmped?(amp_level*.1f)+1:1) * (ticksExisted/250f), true);
                    } else {
                        return;
                    }
                }
            }

            if(movingobjectposition.entityHit != null && !this.worldObj.isRemote) {
                this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "openmodularturrets:laserHit", ConfigHandler.getTurretSoundVolume(), TecTech.Rnd.nextFloat() + 0.5F);
                if(movingobjectposition.entityHit != null && !this.worldObj.isRemote) {
                    float damage = (exotic?10:1) * TecTech.ModConfig.TURRET_DAMAGE_FACTOR * massFactor * (isAmped?(amp_level*.1f)+1:1);

                    if(movingobjectposition.entityHit instanceof EntityPlayer) {
                        if(this.canDamagePlayer((EntityPlayer)movingobjectposition.entityHit)) {
                            movingobjectposition.entityHit.setFire((exotic?10:1)*2);
                            movingobjectposition.entityHit.attackEntityFrom(new NormalDamageSource("laser"), damage);
                            if(antiMatter) movingobjectposition.entityHit.hurtResistantTime = 0;
                        }
                    } else {
                        movingobjectposition.entityHit.setFire((exotic?10:1)*2);
                        movingobjectposition.entityHit.attackEntityFrom(new NormalDamageSource("laser"), damage);
                        if(antiMatter) movingobjectposition.entityHit.hurtResistantTime = 0;
                    }

                    if (TecTech.ModConfig.ENABLE_TURRET_EXPLOSIONS && antiMatter) {
                        GT_Utility.sendSoundToPlayers(worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1.0F,
                                (int)movingobjectposition.entityHit.posX,
                                (int)movingobjectposition.entityHit.posY,
                                (int)movingobjectposition.entityHit.posZ);
                        worldObj.createExplosion(null,
                                movingobjectposition.entityHit.posX,
                                movingobjectposition.entityHit.posY,
                                movingobjectposition.entityHit.posZ, (exotic?10:1) * TecTech.ModConfig.TURRET_EXPLOSION_FACTOR * massFactor * (isAmped?(amp_level*.1f)+1:1) * (ticksExisted/250f), true);
                    }
                }
            }
            this.setDead();
        }
    }

    public void onEntityUpdate() {
        if(this.ticksExisted >= 75) {
            this.setDead();
        }
    }
}
