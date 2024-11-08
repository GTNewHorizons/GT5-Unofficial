package gtPlusPlus.core.tileentities.machines;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.util.math.MathUtils;

public class TileEntityPooCollector extends TileEntityBaseFluidCollector {

    public TileEntityPooCollector() {
        super(9, 64000);
    }

    @Override
    public void onPreLogicTick() {}

    @Override
    public <V> boolean addDrop(V aPooMaker) {
        int aChance = MathUtils.randInt(0, 1000);
        ItemStack aPoop = null;
        if (aChance > 0) {
            if (aChance <= 2) {
                aPoop = GTOreDictUnificator.get(OrePrefixes.dust, "dustManureByproducts", 1);
            } else if (aChance <= 10) {
                aPoop = GTOreDictUnificator.get(OrePrefixes.dustSmall, "dustSmallManureByproducts", 1);
            } else if (aChance <= 25) {
                aPoop = GTOreDictUnificator.get(OrePrefixes.dustTiny, "dustTinyManureByproducts", 1);
            }
            // Add to inventory if not full
            if (!this.mInventory.isFull()) this.mInventory.addItemStack(aPoop);
        }
        return false;
    }

    @Override
    public <V> int onPostTick(V aPooMaker) {
        if (this.tank.getFluidAmount() < this.tank.getCapacity()) {
            int aPooAmount = 0;
            // EntityAnimal includes chicken, cow, horse, mooshroom?, ocelot, pig, sheep and wolf
            if (aPooMaker instanceof EntityAnimal) {
                aPooAmount = 10;
            }
            aPooAmount = Math.max((Math.min(this.tank.getCapacity() - this.tank.getFluidAmount(), aPooAmount)), 1);
            // if tank isnt full return a value between 10 and 40L
            return aPooAmount * MathUtils.randInt(1, 4);
        } else {
            return 0;
        }
    }
}
