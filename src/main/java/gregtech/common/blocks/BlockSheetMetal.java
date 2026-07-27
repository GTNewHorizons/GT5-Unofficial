package gregtech.common.blocks;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;

public class BlockSheetMetal extends BlockPrefixMaterial {

    public BlockSheetMetal(String aName, Int2ObjectFunction<IOreMaterial> materials, int maxMeta) {
        super(aName, OrePrefixes.sheetmetal, materials, maxMeta);
    }

    @Override
    public void registerRecipes() {
        for (int i = 0; i < maxMeta; i++) {
            IOreMaterial material = materials.get(i);

            if (material == null) continue;
            if (!material.generatesPrefix(OrePrefixes.sheetmetal)) continue;
            if (material.contains(SubTag.NO_RECIPES)) continue;

            GTValues.RA.stdBuilder()
                .itemInputs(material.getPart(OrePrefixes.plate, 2), GTUtility.getIntegratedCircuit(11))
                .itemOutputs(material.getPart(OrePrefixes.sheetmetal, 1))
                .eut(TierEU.RECIPE_LV)
                .duration(10)
                .addTo(RecipeMaps.benderRecipes);
        }
    }
}
