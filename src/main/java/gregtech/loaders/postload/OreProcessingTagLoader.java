package gregtech.loaders.postload;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;

/**
 * Registers extra OreDict tags on ores (and raw ores) describing which special processing routes their ore chain
 * supports, so packs and automation can filter ores by how they can be processed:
 * <ul>
 * <li>{@code oreSiftable} - the purified ore has a sifter recipe.</li>
 * <li>{@code oreChemicalBathable} - the crushed ore has a chemical bath recipe, with
 * {@code oreChemicalBathableMercury} and {@code oreChemicalBathableSodiumPersulfate} narrowing down the reagent.</li>
 * <li>{@code oreElectromagneticSeparable} - the purified dust has an electromagnetic separator recipe.</li>
 * </ul>
 * Raw ores get the same tags with a {@code rawOre} prefix instead of {@code ore}.
 * <p>
 * The tags are derived from the registered recipes rather than from material flags, so they stay correct for every
 * material system adding ore processing recipes (GT, BartWorks Werkstoff, GT++, ...) and for manual recipe overrides.
 * This must run on load complete, after every mod has registered its recipes.
 */
public class OreProcessingTagLoader implements Runnable {

    @Override
    public void run() {
        Set<String> siftable = new HashSet<>();
        for (GTRecipe recipe : RecipeMaps.sifterRecipes.getAllRecipes()) {
            collectMaterials(recipe.mInputs, OrePrefixes.crushedPurified, siftable);
        }

        Set<String> bathable = new HashSet<>();
        Set<String> bathableMercury = new HashSet<>();
        Set<String> bathableSodiumPersulfate = new HashSet<>();
        Fluid mercury = Materials.Mercury.getFluid(1L)
            .getFluid();
        Fluid sodiumPersulfate = Materials.SodiumPersulfate.getFluid(1L)
            .getFluid();

        for (GTRecipe recipe : RecipeMaps.chemicalBathRecipes.getAllRecipes()) {
            Set<String> materials = new HashSet<>();
            collectMaterials(recipe.mInputs, OrePrefixes.crushed, materials);
            if (materials.isEmpty()) continue;
            bathable.addAll(materials);
            for (FluidStack fluid : recipe.mFluidInputs) {
                if (fluid == null) continue;
                if (fluid.getFluid() == mercury) {
                    bathableMercury.addAll(materials);
                } else if (fluid.getFluid() == sodiumPersulfate) {
                    bathableSodiumPersulfate.addAll(materials);
                }
            }
        }

        Set<String> separable = new HashSet<>();
        for (GTRecipe recipe : RecipeMaps.electroMagneticSeparatorRecipes.getAllRecipes()) {
            collectMaterials(recipe.mInputs, OrePrefixes.dustPure, separable);
        }

        tagOres(siftable, "Siftable");
        tagOres(bathable, "ChemicalBathable");
        tagOres(bathableMercury, "ChemicalBathableMercury");
        tagOres(bathableSodiumPersulfate, "ChemicalBathableSodiumPersulfate");
        tagOres(separable, "ElectromagneticSeparable");
    }

    /**
     * Collects the material name part of every OreDict name of the given prefix found on the given recipe inputs.
     */
    private static void collectMaterials(ItemStack[] inputs, OrePrefixes prefix, Set<String> out) {
        if (inputs == null) return;
        String prefixName = prefix.getName();
        for (ItemStack stack : inputs) {
            if (stack == null || stack.getItem() == null) continue;
            for (int oreId : OreDictionary.getOreIDs(stack)) {
                String oreName = OreDictionary.getOreName(oreId);
                if (!oreName.startsWith(prefixName)) continue;
                // getOrePrefix does longest-prefix matching, so this rejects e.g. crushedPurified when looking for
                // crushed
                if (OrePrefixes.getOrePrefix(oreName) != prefix) continue;
                String material = oreName.substring(prefixName.length());
                if (!material.isEmpty()) out.add(material);
            }
        }
    }

    /**
     * Registers every ore and raw ore of the given materials under {@code ore<capability>} resp.
     * {@code rawOre<capability>}.
     */
    private static void tagOres(Set<String> materials, String capability) {
        String oreTag = "ore" + capability;
        String rawOreTag = "rawOre" + capability;
        for (String material : materials) {
            for (OrePrefixes prefix : OrePrefixes.VALUES) {
                boolean isRawOre = prefix == OrePrefixes.rawOre;
                if (!isRawOre && !prefix.getName()
                    .startsWith("ore")) continue;
                String oreName = prefix.getName() + material;
                if (!OreDictionary.doesOreNameExist(oreName)) continue;
                for (ItemStack stack : GTOreDictUnificator.getOres(oreName)) {
                    GTOreDictUnificator.registerOre(isRawOre ? rawOreTag : oreTag, stack);
                }
            }
        }
    }
}
