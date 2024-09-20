package gtPlusPlus.core.tileentities.machines;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class TileEntityAdvPooCollector extends TileEntityBaseFluidCollector {

    public TileEntityAdvPooCollector() {
        super(18, 128000);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public void onPreLogicTick() {}

    @Override
    public <V> boolean addDrop(V aPooMaker) {
        int aChance = MathUtils.randInt(0, 50000);
        if (aChance > 0) {
            ItemStack aPoop;
            if (aChance <= 200) {
                aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustManureByproducts", 1);
            } else if (aChance <= 1000) {
                aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);
            } else if (aChance <= 2000) {
                aPoop = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyManureByproducts", 1);
            } else {
                return false;
            }

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
            aEntityToDrain.add(EntityVillager.class);
            aEntityToDrain.add(EntityPlayer.class);
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
                if (aPooMaker instanceof EntityAnimal || aPooMaker instanceof IAnimals) {
                    aPooAmount = MathUtils.randInt(5, 35);
                } else if (aPooMaker instanceof EntityVillager) {
                    aPooAmount = MathUtils.randInt(25, 30);
                } else if (aPooMaker instanceof EntityPlayer) {
                    aPooAmount = MathUtils.randInt(1, 3);
                } else {
                    aPooAmount = MathUtils.randInt(1, 10);
                }
            }
            aPooAmount = Math.max(Math.min(this.tank.getCapacity() - this.tank.getFluidAmount(), aPooAmount), 1);
            return Math.max(
                1,
                (aPooAmount
                    * MathUtils
                        .getRandomFromArray(new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 4 })
                    / 10));
        } else {
            return 0;
        }
    }

    @Override
    public Fluid fluidToProvide() {
        return AgriculturalChem.PoopJuice;
    }

    @Override
    public ItemStack itemToSpawnInWorldIfTankIsFull() {
        int a = MathUtils.randInt(0, 75);
        ItemStack aItem = null;
        if (a <= 30) {
            aItem = ItemUtils.getSimpleStack(AgriculturalChem.dustDirt);
        } else if (a <= 40) {
            aItem = ItemUtils.getItemStackOfAmountFromOreDict("dustManureByproducts", 1);
        } else if (a <= 55) {
            aItem = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);
        }
        return aItem;
    }

    @Override
    public int getBaseTickRate() {
        return MathUtils.randInt(50, 200);
    }
}
