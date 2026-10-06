package gregtech.loaders.oreprocessing;

import static goodgenerator.util.NaquadahRecipeOutputs.convert;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertOre;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

public class ProcessingCrushedOre implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingCrushedOre INSTANCE;

    public ProcessingCrushedOre() {
        INSTANCE = this;
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        switch (prefix.getName()) {
            case "crushedCentrifuged" -> {
                // An ore material need not have a dust of its own -- ChargedCertusQuartz removes the dust shape.
                ItemStack dust = MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1);
                if (dust == null) break;

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(convertOre(material, dust))
                    .duration(10 * TICKS)
                    .eut(TierEU.RECIPE_LV / 2)
                    .addTo(hammerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(
                        convertOre(
                            material,
                            MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1),
                            MaterialLibAPI.findStack(
                                GTUtility.selectItemInList(
                                    2,
                                    MaterialUtils.macerateInto(material),
                                    MaterialUtils.oreByProducts(material)),
                                Shapes.dust,
                                1)))
                    .outputChances(10000, 1000)
                    .duration(20 * SECONDS)
                    .eut(2)
                    .addTo(maceratorRecipes);
            }
            case "crushedPurified" -> {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(
                        convertOre(
                            material,
                            GTUtility.copyAmount(
                                1L,
                                MaterialLibAPI
                                    .findStack(MaterialUtils.macerateInto(material), Shapes.crushedCentrifuged, 1),
                                MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dust, 1)),
                            MaterialLibAPI.findStack(
                                GTUtility.selectItemInList(
                                    1,
                                    MaterialUtils.macerateInto(material),
                                    MaterialUtils.oreByProducts(material)),
                                Shapes.dust,
                                1)))
                    .outputChances(10000, 1111)
                    .duration(25 * SECONDS)
                    .eut(48)
                    .addTo(thermalCentrifugeRecipes);

                ItemStack tGem = MaterialLibAPI.findStack(material, Shapes.gem, 1);
                if (tGem == null) {
                    break;
                }

                // Coal's purified ore is not sifted into gems.
                if (material == Materials.Coal) return;

                switch (MaterialUtils.internalName(material)) {
                    case "Tanzanite", "Sapphire", "Olivine", "GreenSapphire", "Opal", "Amethyst", "Emerald", "Ruby", "Amber", "Diamond", "FoolsRuby", "BlueTopaz", "GarnetRed", "Topaz", "Jasper", "GarnetYellow" -> GTValues.RA
                        .stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            convert(
                                material,
                                GTUtility
                                    .copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemExquisite, 1), tGem),
                                GTUtility
                                    .copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemFlawless, 1), tGem),
                                tGem,
                                GTUtility.copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemFlawed, 1), tGem),
                                GTUtility
                                    .copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemChipped, 1), tGem),
                                GTUtility.copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.dust, 1), tGem)))
                        .outputChances(300, 1200, 4500, 1400, 2800, 3500)
                        .duration(40 * SECONDS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(sifterRecipes);
                    default -> GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .itemOutputs(
                            convert(
                                material,
                                GTUtility
                                    .copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemExquisite, 1), tGem),
                                GTUtility
                                    .copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemFlawless, 1), tGem),
                                tGem,
                                GTUtility.copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemFlawed, 1), tGem),
                                GTUtility
                                    .copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.gemChipped, 1), tGem),
                                GTUtility.copyAmount(1L, MaterialLibAPI.findStack(material, Shapes.dust, 1), tGem)))
                        .outputChances(100, 400, 1500, 2000, 4000, 5000)
                        .duration(40 * SECONDS)
                        .eut(TierEU.RECIPE_LV / 2)
                        .addTo(sifterRecipes);
                }

            }
            default -> {}
        }
    }
}
