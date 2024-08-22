package gregtech.api.objects;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraftforge.fluids.FluidRegistry;

import static gregtech.api.enums.Materials.Helium_3;
import static gregtech.api.enums.Materials.SaltWater;

public class GT_UO_DimensionList {

    private Configuration fConfig;
    private String fCategory;
    private final BiMap<String, GT_UO_Dimension> fDimensionList;

    public int[] blackList = new int[0];

    public GT_UO_DimensionList() {
        fDimensionList = HashBiMap.create();
    }

    public void save(){
        fConfig.save();
    }

    public GT_UO_Dimension GetDimension(int aDimension) {
        if (CheckBlackList(aDimension)) return null;
        if (fDimensionList.containsKey(Integer.toString(aDimension)))
            return fDimensionList.get(Integer.toString(aDimension));
        for (BiMap.Entry<String, GT_UO_Dimension> dl : fDimensionList.entrySet())
            if (DimensionManager.getProvider(aDimension)
                .getClass()
                .getName()
                .contains(dl.getValue().Dimension)) return dl.getValue();
        return fDimensionList.get("Default");
    }

    private boolean CheckBlackList(int aDimensionId) {
        try {
            return java.util.Arrays.binarySearch(blackList, aDimensionId) >= 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void SetConfigValues(String aDimensionName, String aDimension, String aName, String aRegistry,
        int aMinAmount, int aMaxAmount, int aChance, int aDecreasePerOperationAmount) {
        String Category = fCategory + "." + aDimensionName;
        fConfig.get(Category, "Dimension", aDimension)
            .getString();
        Category += "." + aName;
        fConfig.get(Category, "Registry", aRegistry)
            .getString();
        fConfig.get(Category, "MinAmount", aMinAmount)
            .getInt(aMinAmount);
        fConfig.get(Category, "MaxAmount", aMaxAmount)
            .getInt(aMaxAmount);
        fConfig.get(Category, "Chance", aChance)
            .getInt(aChance);
        fConfig.get(Category, "DecreasePerOperationAmount", aDecreasePerOperationAmount)
            .getInt(aDecreasePerOperationAmount);
        // IT IS IN BUCKETS!!!
    }

    public void SetDafultValues() {
        SetConfigValues("Overworld", "0", "gas_natural_gas", "gas_natural_gas", 0, 700, 20, 7);
        SetConfigValues("Overworld", "0", "liquid_light_oil", "liquid_light_oil", 0, 650, 20, 6);
        SetConfigValues("Overworld", "0", "liquid_medium_oil", "liquid_medium_oil", 0, 600, 20, 5);
        SetConfigValues("Overworld", "0", "liquid_heavy_oil", "liquid_heavy_oil", 0, 550, 20, 4);
        SetConfigValues("Overworld", "0", "oil", "oil", 0, 600, 20, 5);
        SetConfigValues("Moon", "Moon", "helium-3", "helium-3", 24, 128, 100, 1);

        SetConfigValues("Ross128b", "Ross128b", "veryheavyoil", "liquid_extra_heavy_oil", 0, 625, 40, 5);
        SetConfigValues("Ross128b", "Ross128b", "lava", "lava", 0, 820, 5, 5);
        SetConfigValues("Ross128b", "Ross128b", "gas_natural_gas", "gas_natural_gas", 0, 625, 65, 5);

        SetConfigValues("Ross128ba", "Ross128ba", "saltwater", "saltwater", 0, 1250, 40, 5);
        SetConfigValues("Ross128ba", "Ross128ba", "helium-3", "helium-3", 0, 1250, 60, 5);
    }

    public void getConfig(Configuration aConfig, String aCategory) {
        fCategory = aCategory;
        fConfig = aConfig;
        if (!fConfig.hasCategory(fCategory)) SetDafultValues();

        blackList = new int[] { -1, 1 };
        blackList = aConfig.get(fCategory, "DimBlackList", blackList, "Dimension IDs Black List")
            .getIntList();
        java.util.Arrays.sort(blackList);

        for (int i = 0; i < fConfig.getCategory(fCategory)
            .getChildren()
            .size(); i++) {
            GT_UO_Dimension Dimension = new GT_UO_Dimension(
                (ConfigCategory) fConfig.getCategory(fCategory)
                    .getChildren()
                    .toArray()[i]);
            fDimensionList.put(Dimension.Dimension, Dimension);
        }
        save();
    }
}
