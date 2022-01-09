package com.github.technus.tectech.compatibility.openmodularturrets.tileentity.turret;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.openmodularturrets.entity.projectiles.projectileEM;
import com.github.technus.tectech.compatibility.openmodularturrets.tileentity.turretbase.TileTurretBaseEM;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.dHadronDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eQuarkDefinition;
import com.github.technus.tectech.thing.item.DebugElementalInstanceContainer_EM;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import openmodularturrets.entity.projectiles.TurretProjectile;
import openmodularturrets.handler.ConfigHandler;
import openmodularturrets.tileentity.turrets.TurretHead;
import openmodularturrets.util.TurretHeadUtil;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.bTransformationInfo.*;

/**
 * Created by Bass on 27/07/2017.
 */
public class TileTurretHeadEM extends TurretHead{
    private cElementalInstanceStackMap hatchContentPointer;

    @Override
    public int getTurretRange() {
        return ConfigHandler.getLaserTurretSettings().getRange()<<1;
    }

    @Override
    public int getTurretPowerUsage() {
        return ConfigHandler.getLaserTurretSettings().getPowerUsage()<<4;
    }

    @Override
    public int getTurretFireRate() {
        return (int)Math.ceil(ConfigHandler.getLaserTurretSettings().getFireRate()/2f);
    }

    @Override
    public double getTurretAccuracy() {
        return (int)Math.ceil(ConfigHandler.getLaserTurretSettings().getAccuracy() / 10.0F);
    }

    @Override
    public void updateEntity() {
        if(!worldObj.isRemote && base instanceof TileTurretBaseEM) {
            hatchContentPointer = ((TileTurretBaseEM) base).getContainerHandler();
        }
        super.updateEntity();
    }

    @Override
    public boolean requiresAmmo() {
        return hatchContentPointer == null || !hatchContentPointer.hasStacks();
    }

    @Override
    public boolean requiresSpecificAmmo() {
        return true;//to enable failure in shooting when there is no EM to use
    }

    @Override
    public Item getAmmo() {
        return DebugElementalInstanceContainer_EM.INSTANCE;//Placeholder item that cannot be achieved, yet still usable for debug
    }

    @Override
    public final TurretProjectile createProjectile(World world, Entity target, ItemStack ammo) {
        while(hatchContentPointer!=null && hatchContentPointer.hasStacks()) {
            cElementalInstanceStack stack = hatchContentPointer.get(TecTech.RANDOM.nextInt(hatchContentPointer.size()));
            if(stack.amount<AVOGADRO_CONSTANT_KIND_OF_SMALLER){
                hatchContentPointer.remove(stack.definition);
                continue;
            }
            hatchContentPointer.removeAmount(false, stack.definition.getStackForm(AVOGADRO_CONSTANT));
            stack=stack.clone();
            stack.amount = AVOGADRO_CONSTANT;
            return new projectileEM(world, TurretHeadUtil.getTurretBase(worldObj, xCoord, yCoord, zCoord), stack);
        }
        return new projectileEM(world, TurretHeadUtil.getTurretBase(worldObj, xCoord, yCoord, zCoord), null);
    }

    @Override
    public String getLaunchSoundEffect() {
        return "laser";
    }
}
