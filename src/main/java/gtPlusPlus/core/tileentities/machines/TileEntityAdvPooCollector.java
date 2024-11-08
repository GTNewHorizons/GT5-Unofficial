package gtPlusPlus.core.tileentities.machines;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.util.math.MathUtils;

public class TileEntityAdvPooCollector extends TileEntityBaseFluidCollector {

    public TileEntityAdvPooCollector() {
        super(36, 256000);
    }

    @Override
    public void onPreLogicTick() {}

    @Override
    public <V> boolean addDrop(V aPooMaker) {
        // Generate a random integer from 0 to 250
        int aChance = MathUtils.randInt(0, 250);
        ItemStack aPoop = null;
        if (aChance > 0) {
            // if said integer is bigger than 0, has a 0.8% 4% and 10% chances of giving Manure byproducts
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
                // advanced poo collector is 4x better than basic one
                aPooAmount = 40;
            }
            aPooAmount = Math.max(Math.min(this.tank.getCapacity() - this.tank.getFluidAmount(), aPooAmount), 1);
            // if tank isnt full return a value between 40 and 160L
            return aPooAmount * MathUtils.randInt(1, 4);
        } else return 0;
    }
}
