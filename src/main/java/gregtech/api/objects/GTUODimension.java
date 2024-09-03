package gregtech.api.objects;

import java.util.Random;

import net.minecraftforge.common.config.ConfigCategory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class GTUODimension {

    private final BiMap<String, GTUOFluid> fFluids;
    private int maxChance;
    public String Dimension = "null";

    public GTUODimension(ConfigCategory aConfigCategory) { // TODO CONFIGURE
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
            GTUOFluid fluid = new GTUOFluid(
                (ConfigCategory) aConfigCategory.getChildren()
                    .toArray()[i]);
            fFluids.put(fluid.Registry, fluid);
            maxChance += fluid.Chance;
        }
    }

    public GTUOFluid getRandomFluid(Random aRandom) {
        int random = aRandom.nextInt(1000);
        for (BiMap.Entry<String, GTUOFluid> fl : fFluids.entrySet()) {
            int chance = fl.getValue().Chance * 1000 / maxChance;
            if (random <= chance) return fl.getValue();
            // GT_FML_LOGGER.info("GT UO "+fl.getValue().Registry+" Chance:"+chance+" Random:"+random);
            random -= chance;
        }
        return null;
    }

    public String getUOFluidKey(GTUOFluid uoFluid) {
        return fFluids.inverse()
            .get(uoFluid);
    }

    public GTUOFluid getUOFluid(String key) {
        return fFluids.get(key);
    }
}
