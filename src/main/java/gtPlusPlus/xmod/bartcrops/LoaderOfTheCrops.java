package gtPlusPlus.xmod.bartcrops;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.bartcrops.crops.Crop_Hemp;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;

/**
 * Mostly borrowed from the Crops++ Crop Loader.
 * 
 * @author Alkalus
 */
public class LoaderOfTheCrops {

    private static List<Boolean> mHasCropObj = new ArrayList<>();
    private CropCard mCropObj;
    private ItemStack mBaseSeed;
    private static List<LoaderOfTheCrops> mCropList = cropLoader();

    public LoaderOfTheCrops(CropCard cropObj) {
        this.mCropObj = cropObj;
    }

    public LoaderOfTheCrops(CropCard cropObj, ItemStack baseseed) {
        this.mCropObj = cropObj;
        this.mBaseSeed = baseseed;
    }

    public static CropCard cropUnpackerCC(LoaderOfTheCrops inp) {
        return inp.mCropObj;
    }

    private static ItemStack cropUnpackerCG(LoaderOfTheCrops inp) {
        return inp.mBaseSeed;
    }

    private static LoaderOfTheCrops cropHelper(CropCard cropObj) {
        return new LoaderOfTheCrops(cropObj, ItemUtils.getItemStackOfAmountFromOreDict("crop" + cropObj.name(), 0));
    }

    public static List<LoaderOfTheCrops> cropLoader() {
        List<LoaderOfTheCrops> p = new ArrayList<>();

        p.add(new LoaderOfTheCrops(new Crop_Hemp(), new ItemStack(Item.getItemById(111), 3)));

        return p;
    }

    private static List<CropCard> cropObjs() {
        List<CropCard> p = new ArrayList<>();

        for (LoaderOfTheCrops loaderOfTheCrops : mCropList) {
            p.add(cropUnpackerCC((LoaderOfTheCrops) loaderOfTheCrops));
        }

        return p;
    }

    private static List<ItemStack> setBaseSeed() {
        List<ItemStack> p = new ArrayList<>();

        for (LoaderOfTheCrops loaderOfTheCrops : mCropList) {
            p.add(cropUnpackerCG((LoaderOfTheCrops) loaderOfTheCrops));
        }

        return p;
    }

    private static List<String> setnames() {
        List<String> s = new ArrayList<>();

        for (int i = 0; i < mCropList.size(); ++i) {
            s.add(((CropCard) cropObjs().get(i)).name());
        }

        return s;
    }

    public static void load(FMLPreInitializationEvent preinit) {
        for (int i = 0; i < mCropList.size(); ++i) {
            mHasCropObj.add(true);
        }
    }

    public static void register() {
        for (int i = 0; i < mCropList.size(); ++i) {
            if ((Boolean) mHasCropObj.get(i) && cropObjs().get(i) != null) {
                Crops.instance.registerCrop((CropCard) cropObjs().get(i));
            }
        }
    }

    public static void registerBaseSeed() {
        List<ItemStack> baseseed = new ArrayList<>(setBaseSeed());

        for (int i = 0; i < mCropList.size(); ++i) {
            if (baseseed.get(i) != null && cropObjs().get(i) != null) {
                Crops.instance.registerBaseSeed((ItemStack) baseseed.get(i), (CropCard) cropObjs().get(i), 1, 1, 1, 1);
            }
        }
    }
}
