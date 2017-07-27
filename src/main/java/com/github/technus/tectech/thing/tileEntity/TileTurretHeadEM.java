package com.github.technus.tectech.thing.tileEntity;

import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.definitions.eLeptonDefinition;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumGlassItem;
import com.github.technus.tectech.thing.metaTileEntity.entity.projectileEM;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.LaserProjectile;
import openmodularturrets.entity.projectiles.TurretProjectile;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turrets.TurretHead;
import openmodularturrets.util.TurretHeadUtil;

import java.lang.reflect.Field;

/**
 * Created by Bass on 27/07/2017.
 */
public class TileTurretHeadEM extends TurretHead{
    private cElementalInstanceStackMap consumedEM;

    public TileTurretHeadEM() {
        try {
            GT_Utility.getField(this, "turretTier").setInt(this, 6);
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public int getTurretRange() {
        return ConfigHandler.getLaserTurretSettings().getRange();
    }

    public int getTurretPowerUsage() {
        return ConfigHandler.getLaserTurretSettings().getPowerUsage();
    }

    public int getTurretFireRate() {
        return ConfigHandler.getLaserTurretSettings().getFireRate();
    }

    public double getTurretAccuracy() {
        return ConfigHandler.getLaserTurretSettings().getAccuracy() / 10.0D;
    }

    public boolean requiresAmmo() {
        return true;
    }

    public boolean requiresSpecificAmmo() {
        return true;
    }

    public Item getAmmo() {
        //consume em
        consumedEM=new cElementalInstanceStackMap().putReplace();
        return QuantumGlassItem.INSTANCE;
    }

    public TurretProjectile createProjectile(World world, Entity target, ItemStack ammo) {
        if(consumedEM!=null && consumedEM.hasStacks()){
            projectileEM projectile= new projectileEM(world, TurretHeadUtil.getTurretBase(worldObj, xCoord, yCoord, zCoord));
            consumedEM=null;
            return projectile;
        }
        return new LaserProjectile(world, TurretHeadUtil.getTurretBase(worldObj, xCoord, yCoord, zCoord));
    }

    public String getLaunchSoundEffect() {
        return "laser";
    }
}
