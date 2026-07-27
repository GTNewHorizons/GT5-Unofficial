package gregtech.common.blocks;

import static gregtech.api.enums.OrePrefixes.blockCasing;
import static gregtech.api.enums.OrePrefixes.gearGtSmall;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.OrePrefixes.screw;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTModHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;

public class BlockBoltedCasing extends BlockPrefixMaterial {

    public BlockBoltedCasing(String aName, Int2ObjectFunction<IOreMaterial> materials, int maxMeta) {
        super(aName, OrePrefixes.blockCasing, materials, maxMeta);
    }

    @Override
    public void registerRecipes() {
        for (int i = 0; i < maxMeta; i++) {
            IOreMaterial material = materials.get(i);

            if (material == null) continue;
            if (!material.generatesPrefix(OrePrefixes.blockCasing)) continue;
            if (material.contains(SubTag.NO_RECIPES)) continue;

            GTModHandler.addCraftingRecipe(
                material.getPart(blockCasing, 1),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "PSP", "PGP", "PSP", 'P', material.getPart(plate, 1), 'S', material.getPart(screw, 1),
                    'G', material.getPart(gearGtSmall, 1) });

            GTValues.RA.stdBuilder()
                .itemInputs(material.getPart(plate, 6), material.getPart(screw, 2), material.getPart(gearGtSmall, 1))
                .itemOutputs(material.getPart(blockCasing, 1))
                .duration(10 * SECONDS)
                .eut(BWUtil.calculateRecipeEU(material, 16))
                .addTo(assemblerRecipes);
        }
    }
}
