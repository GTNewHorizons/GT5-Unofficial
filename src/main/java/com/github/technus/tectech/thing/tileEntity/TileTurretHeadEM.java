package com.github.technus.tectech.thing.tileEntity;

import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.thing.item.DebugContainer_EM;
import com.github.technus.tectech.thing.metaTileEntity.entity.projectileEM;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.TurretProjectile;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turrets.TurretHead;
import openmodularturrets.util.TurretHeadUtil;

/**
 * Created by Bass on 27/07/2017.
 */
public class TileTurretHeadEM extends TurretHead{
    private cElementalInstanceStackMap hatchContentPointer;

    public TileTurretHeadEM() {
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

    @Override
    public void updateEntity() {
        if(!worldObj.isRemote && base instanceof TileTurretBaseEM)
            hatchContentPointer =((TileTurretBaseEM) base).getContainerHandler();
        super.updateEntity();
    }

    public boolean requiresAmmo() {
        return hatchContentPointer == null || !hatchContentPointer.hasStacks();
    }

    public boolean requiresSpecificAmmo() {
        return true;//to enable failure in shooting when there is no EM to use
    }

    public Item getAmmo() {
        return DebugContainer_EM.INSTANCE;//Placeholder item that cannot be achieved, yet still usable for debug
    }

    public TurretProjectile createProjectile(World world, Entity target, ItemStack ammo) {
        //if(hatchContentPointer!=null && hatchContentPointer.hasStacks()){
            projectileEM projectile= new projectileEM(world, TurretHeadUtil.getTurretBase(worldObj, xCoord, yCoord, zCoord), hatchContentPointer);
            return projectile;
        //}
        //return new LaserProjectile(world, TurretHeadUtil.getTurretBase(worldObj, xCoord, yCoord, zCoord));
    }

    public String getLaunchSoundEffect() {
        return "laser";
    }
}
