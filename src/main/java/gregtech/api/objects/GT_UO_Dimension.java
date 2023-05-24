package gregtech.api.objects;

import java.util.Random;

import net.minecraftforge.common.config.ConfigCategory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class GT_UO_Dimension {

    private final BiMap<String, GT_UO_Fluid> fFluids;
    private int maxChance;
    public String Dimension = "null";

    public GT_UO_Dimension(ConfigCategory aConfigCategory) { // TODO CONFIGURE
        fFluids = HashBiMap.create();
        if (aConfigCategory.containsKey("Dimension")) {
            aConfigCategory.get("Dimension").comment = "Dimension ID or Class Name";
            Dimension = aConfigCategory.get("Dimension")
                .getString();
        }
        maxChance = 0;
        // GT_FML_LOGGER.info("GT UO "+aConfigCategory.getName()+" Dimension:"+Dimension);
        for (int i = 0; i < aConfigCategory.getChildren()
            .size(); i++) {
            GT_UO_Fluid fluid = new GT_UO_Fluid(
                (ConfigCategory) aConfigCategory.getChildren()
                    .toArray()[i]);
            fFluids.put(fluid.Registry, fluid);
            maxChance += fluid.Chance;
        }
    }

    public GT_UO_Fluid getRandomFluid(Random aRandom) {
        int random = aRandom.nextInt(1000);
        for (BiMap.Entry<String, GT_UO_Fluid> fl : fFluids.entrySet()) {
            int chance = fl.getValue().Chance * 1000 / maxChance;
            if (random <= chance) return fl.getValue();
            // GT_FML_LOGGER.info("GT UO "+fl.getValue().Registry+" Chance:"+chance+" Random:"+random);
            random -= chance;
        }
        return null;
    }

    public String getUOFluidKey(GT_UO_Fluid uoFluid) {
        return fFluids.inverse()
            .get(uoFluid);
    }

    public GT_UO_Fluid getUOFluid(String key) {
        return fFluids.get(key);
    }
}
