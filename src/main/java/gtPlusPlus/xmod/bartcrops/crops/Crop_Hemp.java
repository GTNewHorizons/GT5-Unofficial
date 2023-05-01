package gtPlusPlus.xmod.bartcrops.crops;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.xmod.bartcrops.abstracts.BaseAestheticCrop;
import ic2.api.crops.ICropTile;

public class Crop_Hemp extends BaseAestheticCrop {

    @Override
    public int tier() {
        return 2;
    }

    @Override
    public String name() {
        return "Hemp";
    }

    @Override
    public String discoveredBy() {
        return "Alkalus";
    }

    @Override
    public int growthDuration(ICropTile crop) {
        int ret = 550;

        /*
         * if (crop.isBlockBelow(Blocks.dirt) || crop.isBlockBelow(Blocks.flowing_water)) { ret = 225; }
         */

        if (CORE_Preloader.DEBUG_MODE) {
            ret = 1;
        }

        return ret;
    }

    @Override
    public String[] attributes() {
        return new String[] { "Green", "Soil", "Orange" };
    }

    @Override
    public ItemStack getGain(ICropTile crop) {

        ItemStack ret = this.getDisplayItem();
        if (MathUtils.randInt(0, 10) > 8) {
            ret = ItemUtils.getSimpleStack(ModItems.itemRope, MathUtils.randInt(1, 3));
        }

        return ret;
    }

    @Override
    public ItemStack getDisplayItem() {
        return ItemUtils.getSimpleStack(ModItems.itemRope, 0);
    }
}
