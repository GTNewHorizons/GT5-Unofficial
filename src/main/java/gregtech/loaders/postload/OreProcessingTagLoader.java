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
 * supports, so automation can filter ores by how they can be processed:
 * <ul>
 * <li>{@code oreSiftable} - the purified ore has a sifter recipe.</li>
 * <li>{@code oreChemicalBathable} - the crushed ore has a chemical bath recipe, with
 * {@code oreChemicalBathableMercury} and {@code oreChemicalBathableSodiumPersulfate} narrowing down the reagent.</li>
 * <li>{@code oreElectromagneticSeparable} - the purified dust has an electromagnetic separator recipe.</li>
 * </ul>
 * Raw ores, crushed ores and purified crushed ores get the same tags with a {@code rawOre}, {@code crushed} resp.
 * {@code crushedPurified} prefix instead of {@code ore}, but only for capabilities whose processing stage is still
 * ahead of (or at) that form's stage. For example, purified crushed ores are past the chemical bath and don't get the
 * {@code ChemicalBathable} tags.
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

        // Chemical bathing happens at the crushed stage, so purified crushed ores are already past it and don't get
        // the tag; sifting and electromagnetic separation happen at resp. after the purified stage.
        tagOres(siftable, "Siftable", true);
        tagOres(bathable, "ChemicalBathable", false);
        tagOres(bathableMercury, "ChemicalBathableMercury", false);
        tagOres(bathableSodiumPersulfate, "ChemicalBathableSodiumPersulfate", false);
        tagOres(separable, "ElectromagneticSeparable", true);
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
     * Registers every ore form of the given materials under {@code <formPrefix><capability>}: all {@code ore*} stone
     * variants collapse into {@code ore<capability>}, while raw ores, crushed ores and purified crushed ores get
     * {@code rawOre<capability>}, {@code crushed<capability>} resp. {@code crushedPurified<capability>}.
     * <p>
     * A form is only tagged while the capability's processing stage is still ahead of (or at) it, so purified crushed
     * ores are skipped unless {@code applicableAtPurified}, and crushedCentrifuged is never tagged, as no special
     * processing route applies at or after that stage.
     */
    private static void tagOres(Set<String> materials, String capability, boolean applicableAtPurified) {
        for (String material : materials) {
            for (OrePrefixes prefix : OrePrefixes.VALUES) {
                if (prefix == OrePrefixes.crushedPurified && !applicableAtPurified) continue;
                String prefixName = prefix.getName();
                String tagBase;
                if (prefixName.startsWith("ore")) {
                    tagBase = "ore";
                } else if (prefix == OrePrefixes.rawOre || prefix == OrePrefixes.crushed
                    || prefix == OrePrefixes.crushedPurified) {
                        tagBase = prefixName;
                    } else {
                        continue;
                    }
                String oreName = prefixName + material;
                if (!OreDictionary.doesOreNameExist(oreName)) continue;
                for (ItemStack stack : GTOreDictUnificator.getOres(oreName)) {
                    GTOreDictUnificator.registerOre(tagBase + capability, stack);
                }
            }
        }
    }
}
