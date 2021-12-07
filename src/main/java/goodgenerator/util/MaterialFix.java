package goodgenerator.util;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class MaterialFix {
    public static void MaterialFluidExtractionFix(Werkstoff material){
        if (material.hasItemType(OrePrefixes.ingot))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.ingot),null,material.getMolten(144),0,32,8);
        if (material.hasItemType(OrePrefixes.plate))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.plate),null,material.getMolten(144),0,32,8);
        if (material.hasItemType(OrePrefixes.gearGtSmall))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.gearGtSmall),null,material.getMolten(144),0,32,8);
        if (material.hasItemType(OrePrefixes.stickLong))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.stickLong),null,material.getMolten(144),0,32,8);
        if (material.hasItemType(OrePrefixes.spring))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.spring),null,material.getMolten(144),0,32,8);
        if (material.hasItemType(OrePrefixes.stick))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.stick),null,material.getMolten(72),0,16,8);
        if (material.hasItemType(OrePrefixes.itemCasing))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.itemCasing),null,material.getMolten(72),0,16,8);
        if (material.hasItemType(OrePrefixes.wireGt01))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.wireGt01),null,material.getMolten(72),0,16,8);
        if (material.hasItemType(OrePrefixes.cableGt01))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.cableGt01), GT_OreDictUnificator.get(OrePrefixes.dustSmall, Materials.Ash,2),material.getMolten(72),10000,16,8);
        if (material.hasItemType(OrePrefixes.foil))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.foil),null,material.getMolten(36),0,8,8);
        if (material.hasItemType(OrePrefixes.springSmall))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.springSmall),null,material.getMolten(36),0,8,8);
        if (material.hasItemType(OrePrefixes.ring))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.ring),null,material.getMolten(36),0,8,8);
        if (material.hasItemType(OrePrefixes.bolt))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.bolt),null,material.getMolten(18),0,4,8);
        if (material.hasItemType(OrePrefixes.wireFine))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.wireFine),null,material.getMolten(18),0,4,8);
        if (material.hasItemType(OrePrefixes.round))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.round),null,material.getMolten(16),0,4,8);
        if (material.hasItemType(OrePrefixes.screw))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.screw),null,material.getMolten(16),0,4,8);
        if (material.hasItemType(OrePrefixes.nugget))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.nugget),null,material.getMolten(16),0,4,8);
        if (material.hasItemType(OrePrefixes.rotor))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.rotor),null,material.getMolten(612),0,136,8);
        if (material.hasItemType(OrePrefixes.gearGt))
            GT_Values.RA.addFluidExtractionRecipe(material.get(OrePrefixes.gearGt),null,material.getMolten(576),0,128,8);
    }
}
