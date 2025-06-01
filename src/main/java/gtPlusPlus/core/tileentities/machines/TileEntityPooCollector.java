package gtPlusPlus.core.tileentities.machines;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class TileEntityPooCollector extends TileEntityBaseFluidCollector {

    public TileEntityPooCollector() {
        super(9, 8000);
    }

    @Override
    public void onPreLogicTick() {}

    @Override
    public <V> boolean addDrop(V aPooMaker) {
        int aChance = MathUtils.randInt(0, 50000);
        if (aChance > 0) {
            ItemStack aPoop;
            if (aChance <= 100) {
                aPoop = GregtechItemList.ManureByproductsDust.get(1);
            } else if (aChance <= 500) {
                aPoop = GregtechItemList.SmallManureByproductsDust.get(1);
            } else if (aChance <= 1250) {
                aPoop = GregtechItemList.TinyManureByproductsDust.get(1);
            } else {
                return false;
            }

            if (aPoop == null) return false;

            // Add poop to world
            // Logger.INFO("Adding animal waste for "+aPooMaker.getCommandSenderName());

            // Add to inventory if not full, else espawn in world
            if (!this.mInventory.addItemStack(aPoop)) {
                EntityItem entity = new EntityItem(worldObj, xCoord, yCoord + 1.5, zCoord, aPoop);
                worldObj.spawnEntityInWorld(entity);
            }
        }

        return false;
    }

    private static final ArrayList<Class> aEntityToDrain = new ArrayList<>();

    @Override
    public ArrayList<Class> aThingsToLookFor() {
        if (aEntityToDrain.isEmpty()) {
            aEntityToDrain.add(EntityAnimal.class);
            aEntityToDrain.add(IAnimals.class);
        }
        return aEntityToDrain;
    }

    @Override
    public <V> int onPostTick(V aPooMaker) {
        if (this.tank.getFluidAmount() < this.tank.getCapacity()) {
            int aPooAmount = 0;
            // Vanilla Animals
            if (aPooMaker instanceof EntityChicken) {
                aPooAmount = MathUtils.randInt(1, 40);
            } else if (aPooMaker instanceof EntityHorse) {
                aPooAmount = MathUtils.randInt(20, 40);
            } else if (aPooMaker instanceof EntityCow) {
                aPooAmount = MathUtils.randInt(18, 45);
            } else if (aPooMaker instanceof EntityMooshroom) {
                aPooAmount = 17;
            } else if (aPooMaker instanceof EntitySheep) {
                aPooAmount = MathUtils.randInt(8, 30);
            } else {
                if (aPooMaker instanceof IAnimals) {
                    aPooAmount = MathUtils.randInt(5, 35);
                } else {
                    aPooAmount = MathUtils.randInt(1, 10);
                }
            }
            aPooAmount = Math.max((Math.min(this.tank.getCapacity() - this.tank.getFluidAmount(), aPooAmount) / 10), 1);
            return aPooAmount;
        } else {
            return 0;
        }
    }

    @Override
    public Fluid fluidToProvide() {
        return GTPPFluids.PoopJuice;
    }

    @Override
    public ItemStack itemToSpawnInWorldIfTankIsFull() {
        int a = MathUtils.randInt(0, 100);
        ItemStack aItem = null;
        if (a <= 30) {
            aItem = GregtechItemList.DriedEarthDust.get(1);
        } else if (a <= 40) {
            aItem = GregtechItemList.SmallManureByproductsDust.get(1);
        } else if (a <= 55) {
            aItem = GregtechItemList.TinyManureByproductsDust.get(1);
        }
        return aItem;
    }
}
