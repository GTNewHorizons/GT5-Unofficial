package tectech.loader.recipe;

import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FOG_EXOTIC_TIER;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_MULTISTEP;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_TIER;
import static gregtech.api.util.GTRecipeConstants.FOG_UPGRADE_NAME_SHORT;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.recipe.TecTechRecipeMaps.godforgeExoticMatterRecipes;
import static tectech.recipe.TecTechRecipeMaps.godforgePlasmaRecipes;
import static tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath.getRandomIntInRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.LegacyNameDomain;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade;

public class Godforge implements Runnable {

    public static final ArrayList<Material> plasmaGTMaterialList = new ArrayList<>();
    public static final ArrayList<Integer> plasmaGTWeightList = new ArrayList<>();
    public static final HashMap<ItemStack, Integer> exoticModulePlasmaItemMap = new HashMap<>();
    public static final HashMap<FluidStack, Integer> exoticModulePlasmaFluidMap = new HashMap<>();
    public static final HashMap<ItemStack, Integer> exoticModuleMagmatterItemMap = new HashMap<>();
    public static final List<ItemStack> quarkGluonFluidItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> quarkGluonItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> magmatterTimeFluidItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> magmatterSpaceFluidItemsForNEI = new ArrayList<>();
    public static final List<ItemStack> magmatterItemsForNEI = new ArrayList<>();

    /// The molten fluid of each dust's material, index-aligned with `items`: every caller pairs the result
    /// positionally with a same-index plasma output, so an entry the material has no molten fluid for must be
    /// a null slot rather than a missing one.
    ///
    /// Resolves each dust `ItemStack` back to its unified material via [GTOreDictUnificator#getAssociation]
    /// rather than parsing an OreDictionary name (`"dustIron"` -> `"Iron"`), so this does not depend on the
    /// item having exactly one registered OreDictionary id in a particular order -- true for both legacy and
    /// MaterialLib-cutover dust items, since both go through `addAssociation` on unification.
    private FluidStack[] convertToFluid(ItemStack[] items) {
        FluidStack[] molten = new FluidStack[items.length];

        for (int i = 0; i < items.length; i++) {
            ItemData association = GTOreDictUnificator.getAssociation(items[i]);
            if (association == null) {
                GTLog.err.println("Godforge.convertToFluid: no unification data for " + items[i]);
                continue;
            }
            molten[i] = MaterialUtils.molten(association.mMaterial.mMaterial, 1 * INGOTS);
        }

        return molten;
    }

    @Override
    public void run() {
        // Solid to plasma recipes
        {
            // Fusion tier 1-3
            {
                // Single step
                ItemStack[] solids_t0_1step = { MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Niobium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Americium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Antimony, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Ardite, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Arsenic, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Barium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Beryllium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Caesium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Cadmium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Desh, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Dysprosium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Erbium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Europium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Gallium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Gold, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Holmium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lithium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.MeteoricIron, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Molybdenum, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Neodymium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Oriharukon, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Praseodymium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Promethium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Rubidium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Strontium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tantalum, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tellurium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Terbium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Thulium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tungsten, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Vanadium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Ytterbium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Yttrium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Germanium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Thallium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Rhenium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Iodine, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Hafnium, Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Curium, Shapes.dust, 1) };

                FluidStack[] molten_t0_1step = convertToFluid(solids_t0_1step);

                FluidStack[] solid_plasmas_t0_1step = {
                    MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Calcium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Sulfur, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Zinc, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Niobium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Tin, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Nickel, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Antimony, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Ardite, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Arsenic, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Barium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Beryllium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Caesium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Cadmium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Carbon, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Cerium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Cobalt, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Copper, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Desh, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Dysprosium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Erbium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Europium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Gadolinium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Gallium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Gold, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Holmium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Indium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lanthanum, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lithium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lutetium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Magnesium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Manganese, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.MeteoricIron, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Molybdenum, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Neodymium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Oriharukon, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Palladium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Phosphorus, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Potassium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Praseodymium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Promethium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Rubidium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Samarium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Silicon, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Sodium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Strontium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Tantalum, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Tellurium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Terbium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Thulium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Tungsten, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Uranium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Uranium235, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Vanadium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Ytterbium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Yttrium, FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Chrome, FluidShapes.fluidPlasma, 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Zirconium), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Thorium232), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Germanium), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Thallium), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Ruthenium), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Rhenium), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Rhodium), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Iodine), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Hafnium), 1 * INGOTS),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Curium), 1 * INGOTS) };

                for (int i = 0; i < solids_t0_1step.length; i++) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(solids_t0_1step[i])
                        .fluidOutputs(solid_plasmas_t0_1step[i])
                        .duration(10 * TICKS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);

                    if (molten_t0_1step[i] != null) {
                        GTValues.RA.stdBuilder()
                            .fluidInputs(molten_t0_1step[i])
                            .fluidOutputs(solid_plasmas_t0_1step[i])
                            .duration(10 * TICKS)
                            .eut(TierEU.RECIPE_MAX)
                            .metadata(FOG_PLASMA_MULTISTEP, false)
                            .metadata(FOG_PLASMA_TIER, 0)
                            .addTo(godforgePlasmaRecipes);
                    }
                }
            }

            // Multi-step
            ItemStack[] solids_t0_xstep = { MaterialLibAPI.getStack(Materials.Force, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Bismuth, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.AdvancedNitinol, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.AstralTitanium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Runite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Californium, Shapes.dust, 1) };

            FluidStack[] molten_t0_xstep = convertToFluid(solids_t0_xstep);

            FluidStack[] solid_plasmas_t0_xstep = {
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Force), 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Bismuth, FluidShapes.fluidPlasma, 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AdvancedNitinol), 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AstralTitanium), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Runite), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.CelestialTungsten), 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Platinum, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Plutonium, FluidShapes.fluidPlasma, 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Californium), 1 * INGOTS) };

            for (int i = 0; i < solids_t0_xstep.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t0_xstep[i])
                    .fluidOutputs(solid_plasmas_t0_xstep[i])
                    .duration(2 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, true)
                    .metadata(FOG_PLASMA_TIER, 0)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t0_xstep[i] != null) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t0_xstep[i])
                        .fluidOutputs(solid_plasmas_t0_xstep[i])
                        .duration(2 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);
                }
            }
        }
        // Fusion tier 4-5
        {
            // Single step
            ItemStack[] solids_t1_1step = { MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.dust, 1) };

            FluidStack[] molten_t1_1step = convertToFluid(solids_t1_1step);

            FluidStack[] solid_plasmas_t1_1step = {
                MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Plutonium241, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Redstone, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.CosmicNeutronium, FluidShapes.fluidPlasma, 1 * INGOTS) };

            for (int i = 0; i < solids_t1_1step.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t1_1step[i])
                    .fluidOutputs(solid_plasmas_t1_1step[i])
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, false)
                    .metadata(FOG_PLASMA_TIER, 1)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t1_1step[i] != null) {

                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t1_1step[i])
                        .fluidOutputs(solid_plasmas_t1_1step[i])
                        .duration(5 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 1)
                        .addTo(godforgePlasmaRecipes);
                }
            }

            // Multi-step
            ItemStack[] solids_t1_xstep = { MaterialLibAPI.getStack(Materials.Neptunium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Fermium, Shapes.dust, 1) };

            FluidStack[] molten_t1_xstep = convertToFluid(solids_t1_xstep);

            FluidStack[] solid_plasmas_t1_xstep = {
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 1 * INGOTS) };

            for (int i = 0; i < solids_t1_xstep.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t1_xstep[i])
                    .fluidOutputs(solid_plasmas_t1_xstep[i])
                    .duration(7 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, true)
                    .metadata(FOG_PLASMA_TIER, 1)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t1_xstep[i] != null) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t1_xstep[i])
                        .fluidOutputs(solid_plasmas_t1_xstep[i])
                        .duration(7 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 1)
                        .addTo(godforgePlasmaRecipes);
                }
            }
        }
        // Exotic Plasmas
        {
            // Single step
            ItemStack[] solids_t2_1step = { MaterialLibAPI.getStack(Materials.Rhugnor, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Dragonblood, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Bedrockium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Draconium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Ichorium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.dust, 1) };

            FluidStack[] molten_t2_1step = convertToFluid(solids_t2_1step);

            FluidStack[] solid_plasmas_t2_1step = {
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Rhugnor), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Dragonblood), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.ChromaticGlass), 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Bedrockium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Draconium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Ichorium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidPlasma, 1 * INGOTS) };

            for (int i = 0; i < solids_t2_1step.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t2_1step[i])
                    .fluidOutputs(solid_plasmas_t2_1step[i])
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, false)
                    .metadata(FOG_PLASMA_TIER, 2)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t2_1step[i] != null) {

                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t2_1step[i])
                        .fluidOutputs(solid_plasmas_t2_1step[i])
                        .duration(15 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 2)
                        .addTo(godforgePlasmaRecipes);
                }
            }

            // Multi-step
            ItemStack[] solids_t2_xstep = { MaterialLibAPI.getStack(Materials.Hypogen, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.FleroviumGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.dust, 1) };

            FluidStack[] molten_t2_xstep = convertToFluid(solids_t2_xstep);

            FluidStack[] solid_plasmas_t2_xstep = {
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Hypogen), 144),
                MaterialLibAPI.getFluidStack(Materials.Tritanium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.FleroviumGT5U, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SixPhasedCopper, FluidShapes.fluidPlasma, 1 * INGOTS) };

            for (int i = 0; i < solids_t2_xstep.length; i++) {
                GTValues.RA.stdBuilder()
                    .itemInputs(solids_t2_xstep[i])
                    .fluidOutputs(solid_plasmas_t2_xstep[i])
                    .duration(25 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, true)
                    .metadata(FOG_PLASMA_TIER, 2)
                    .addTo(godforgePlasmaRecipes);

                if (molten_t2_xstep[i] != null) {

                    GTValues.RA.stdBuilder()
                        .fluidInputs(molten_t2_xstep[i])
                        .fluidOutputs(solid_plasmas_t2_xstep[i])
                        .duration(25 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 2)
                        .addTo(godforgePlasmaRecipes);
                }

            }

        }

        // Fluid to plasma recipes
        {
            // Fusion tier 1-3
            {
                // Single step
                FluidStack[] fluids_t0_1step = {
                    MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Tritium, FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 500) };
                FluidStack[] fluid_plasmas_t0_1step = {
                    MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Deuterium, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Tritium, FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidPlasma, 500) };

                for (int i = 0; i < fluids_t0_1step.length; i++) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(fluids_t0_1step[i])
                        .fluidOutputs(fluid_plasmas_t0_1step[i])
                        .duration(1 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, false)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);
                }

                // Multi-step
                FluidStack[] fluids_t0_xstep = { MaterialUtils.anyFluid(Materials.Neon, 500),
                    MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 500),
                    MaterialUtils.anyFluid(Materials.Krypton, 500), MaterialUtils.anyFluid(Materials.Xenon, 500) };
                FluidStack[] fluid_plasmas_t0_xstep = {
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neon), 500),
                    MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidPlasma, 500),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Krypton), 500),
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Xenon), 500) };

                for (int i = 0; i < fluids_t0_xstep.length; i++) {
                    GTValues.RA.stdBuilder()
                        .fluidInputs(fluids_t0_xstep[i])
                        .fluidOutputs(fluid_plasmas_t0_xstep[i])
                        .duration(3 * SECONDS)
                        .eut(TierEU.RECIPE_MAX)
                        .metadata(FOG_PLASMA_MULTISTEP, true)
                        .metadata(FOG_PLASMA_TIER, 0)
                        .addTo(godforgePlasmaRecipes);
                }
            }
            // Fusion tier 4-5
            {
                // Single step
                GTValues.RA.stdBuilder()
                    .fluidInputs(MaterialUtils.anyFluid(Materials.Bromine, 500))
                    .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Bromine), 500))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_MAX)
                    .metadata(FOG_PLASMA_MULTISTEP, false)
                    .metadata(FOG_PLASMA_TIER, 1)
                    .addTo(godforgePlasmaRecipes);

                // Multi-step
                // None yet
            }
            // Exotic
            {
                // None yet
            }
        }

        // Exotic module fake recipes
        {
            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.QuarkGluonPlasma, FluidShapes.fluidLiquid, 1_000))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MAX)
                .metadata(FOG_EXOTIC_TIER, 1)
                .ignoreCollision()
                .fake()
                .addTo(godforgeExoticMatterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, 1),
                    MaterialLibAPI.getFluidStack(Materials.Bismuth, FluidShapes.fluidMolten, 1))
                .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Magmatter, FluidShapes.fluidMolten, 4 * INGOTS))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MAX)
                .metadata(FOG_EXOTIC_TIER, 1)
                .ignoreCollision()
                .fake()
                .addTo(godforgeExoticMatterRecipes);
        }

        // Exotic module plasma material maps
        // GT materials
        plasmaGTMaterialList.addAll(
            Arrays.asList(
                Materials.Aluminium,
                Materials.Americium,
                Materials.Antimony,
                Materials.Ardite,
                Materials.Argon,
                Materials.Arsenic,
                Materials.Barium,
                Materials.Beryllium,
                Materials.Caesium,
                Materials.Calcium,
                Materials.Cadmium,
                Materials.Carbon,
                Materials.Cerium,
                Materials.Chlorine,
                Materials.Cobalt,
                Materials.Copper,
                Materials.Desh,
                Materials.Deuterium,
                Materials.Dysprosium,
                Materials.Erbium,
                Materials.Europium,
                Materials.Fluorine,
                Materials.Gadolinium,
                Materials.Gallium,
                Materials.Gold,
                Materials.Helium,
                Materials.Holmium,
                Materials.Hydrogen,
                Materials.Indium,
                Materials.Iron,
                Materials.Lanthanum,
                Materials.Lithium,
                Materials.Lutetium,
                Materials.Magnesium,
                Materials.Manganese,
                Materials.MeteoricIron,
                Materials.Molybdenum,
                Materials.Neodymium,
                Materials.Nickel,
                Materials.Niobium,
                Materials.Nitrogen,
                Materials.Oriharukon,
                Materials.Palladium,
                Materials.Phosphorus,
                Materials.Potassium,
                Materials.Praseodymium,
                Materials.Promethium,
                Materials.Radon,
                Materials.Rubidium,
                Materials.Samarium,
                Materials.Silicon,
                Materials.Silver,
                Materials.Sodium,
                Materials.Strontium,
                Materials.Sulfur,
                Materials.Tantalum,
                Materials.Tellurium,
                Materials.Terbium,
                Materials.Thulium,
                Materials.Tin,
                Materials.Titanium,
                Materials.Tritium,
                Materials.Tungsten,
                Materials.Uranium235,
                Materials.Uranium,
                Materials.Vanadium,
                Materials.Ytterbium,
                Materials.Yttrium,
                Materials.Zinc));

        plasmaGTWeightList.addAll(
            Arrays.asList(
                6000 /* Aluminium */,
                10000 /* Americium */,
                6000 /* Antimony */,
                6000 /* Ardite */,
                6000 /* Argon */,
                6000 /* Arsenic */,
                6000 /* Barium */,
                6000 /* Beryllium */,
                6000 /* Caesium */,
                10000 /* Calcium */,
                6000 /* Cadmium */,
                6000 /* Carbon */,
                6000 /* Cerium */,
                6000 /* Chlorine */,
                6000 /* Cobalt */,
                6000 /* Copper */,
                6000 /* Desh */,
                6000 /* Deuterium */,
                2000 /* Dysprosium */,
                2000 /* Erbium */,
                6000 /* Europium */,
                6000 /* Fluorine */,
                2000 /* Gadolinium */,
                6000 /* Gallium */,
                6000 /* Gold */,
                10000 /* Helium */,
                6000 /* Holmium */,
                10000 /* Hydrogen */,
                6000 /* Indium */,
                10000 /* Iron */,
                6000 /* Lanthanum */,
                6000 /* Lithium */,
                6000 /* Lutetium */,
                6000 /* Magnesium */,
                6000 /* Manganese */,
                6000 /* Meteoric Iron */,
                6000 /* Molybdenum */,
                6000 /* Neodymium */,
                10000 /* Nickel */,
                10000 /* Niobium */,
                10000 /* Nitrogen */,
                6000 /* Oriharukon */,
                6000 /* Palladium */,
                6000 /* Phosphorus */,
                6000 /* Potassium */,
                6000 /* Praseodymium */,
                2000 /* Promethium */,
                10000 /* Radon */,
                2000 /* Rubidium */,
                6000 /* Samarium */,
                6000 /* Raw Silicon */,
                10000 /* Silver */,
                6000 /* Sodium */,
                2000 /* Strontium */,
                10000 /* Sulfur */,
                6000 /* Tantalum */,
                2000 /* Tellurium */,
                1000 /* Terbium */,
                6000 /* Thulium */,
                10000 /* Tin */,
                10000 /* Titanium */,
                6000 /* Tritium */,
                6000 /* Tungsten */,
                6000 /* Uranium 235 */,
                6000 /* Uranium 238 */,
                6000 /* Vanadium */,
                2000 /* Ytterbium */,
                6000 /* Yttrium */,
                6000 /* Zinc */));

        // GT++ materials
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Germanium, Shapes.dust, 1), 2000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Thallium, Shapes.dust, 1), 2000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Rhenium, Shapes.dust, 1), 2000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Hafnium, Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Curium, Shapes.dust, 1), 10000);

        // Lanthanides Materials
        exoticModulePlasmaItemMap.put(MaterialLibAPI.getStack(Materials.Iodine, Shapes.dust, 1), 6000);

        // Mercury is weird, it has neither dust nor gas, so it needs to be added separately
        exoticModulePlasmaFluidMap
            .put(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1), 6000);

        // Loop for adding all GT plasma materials
        for (int i = 0; i < plasmaGTMaterialList.size(); i++) {
            Material plasmaMaterial = plasmaGTMaterialList.get(i);
            if (plasmaMaterial.hasShape(Shapes.dust)) {
                exoticModulePlasmaItemMap
                    .put(MaterialLibAPI.getStack(plasmaMaterial, Shapes.dust, 1), plasmaGTWeightList.get(i));
            } else {
                exoticModulePlasmaFluidMap.put(MaterialUtils.gas(plasmaMaterial, 1), plasmaGTWeightList.get(i));
            }
        }

        // Magmatter map
        // GT materials
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Draconium, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Ichorium, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Neutronium, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.FleroviumGT5U, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Bedrockium, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Infinity, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Tritanium, Shapes.dust, 1), 100000);

        // GT++ materials
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.CelestialTungsten, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Hypogen, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Rhugnor, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap.put(MaterialLibAPI.getStack(Materials.Dragonblood, Shapes.dust, 1), 100000);

        // For NEI
        for (FluidStack fluid : exoticModulePlasmaFluidMap.keySet()) {
            fluid.amount = getRandomIntInRange(1, 64);
            quarkGluonFluidItemsForNEI.add(GTUtility.getFluidDisplayStack(fluid, true));
        }
        for (ItemStack item : exoticModulePlasmaItemMap.keySet()) {
            item.stackSize = getRandomIntInRange(1, 7);
            quarkGluonItemsForNEI.add(item);
        }
        for (int i = 0; i < 21; i++) {
            magmatterTimeFluidItemsForNEI.add(
                GTUtility.getFluidDisplayStack(
                    MaterialUtils.molten(Materials.temporalFluid, getRandomIntInRange(1, 50)),
                    true));
            magmatterSpaceFluidItemsForNEI.add(
                GTUtility.getFluidDisplayStack(
                    MaterialUtils.molten(Materials.spatialFluid, getRandomIntInRange(51, 100)),
                    true));
        }
        magmatterItemsForNEI.addAll(exoticModuleMagmatterItemMap.keySet());

        // Godforge upgrade materials
        if (EternalSingularity.isModLoaded()) {
            ForgeOfGodsUpgrade.START.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 64),
                ItemList.SuperconductorComposite.get(32),
                MaterialLibAPI.getStack(Materials.MetastableOganesson, Shapes.gearGt, 16),
                getModItem(EternalSingularity.ID, "eternal_singularity", 8L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.FDIM.addExtraCost(
                GregtechItemList.Mega_AlloyBlastSmelter.get(16L),
                ItemList.Casing_Coil_Hypogen.get(64L),
                CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(32L),
                getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                ItemRefer.Field_Restriction_Coil_T3.get(48),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.GPCI.addExtraCost(
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(8),
                GregtechItemList.FusionComputer_UV3.get(8),
                GregtechItemList.Casing_Fusion_Internal2.get(64),
                ItemList.UHTResistantMesh.get(64),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.plateDense, 48),
                MaterialLibAPI.getStack(Materials.Rhugnor, Shapes.gearGt, 32),
                getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.QGPIU.addExtraCost(
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(16),
                ItemRefer.Compact_Fusion_MK5.get(2),
                ItemRefer.Compact_Fusion_Coil_T4.get(64),
                CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(16),
                ItemList.Machine_Multi_TranscendentPlasmaMixer.get(4),
                MaterialLibAPI.getStack(Materials.Rhugnor, Shapes.gearGt, 64),
                MaterialLibAPI.getStack(Materials.Ichorium, Shapes.gearGt, 64),
                getModItem(EternalSingularity.ID, "eternal_singularity", 32L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.CD.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, TEBlockShapes.frameGt, 64),
                MaterialLibAPI.getStack(Materials.Dragonblood, TEBlockShapes.frameGt, 64),
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),
                CustomItemList.EOH_Infinite_Energy_Casing.get(8),
                ItemList.ZPM6.get(2),
                ItemList.Field_Generator_UMV.get(32));

            ForgeOfGodsUpgrade.EE.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.WhiteDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Eternity, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Universium, 2),
                CustomItemList.EOH_Infinite_Energy_Casing.get(64),
                CustomItemList.StabilisationFieldGeneratorTier6.get(48),
                ItemList.ZPM6.get(16),
                ItemList.Transdimensional_Alignment_Matrix.get(8),
                CustomItemList.Machine_Multi_QuarkGluonPlasmaModule.get(32),
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(64),
                ItemList.Field_Generator_UMV.get(64),
                ItemList.Robot_Arm_UMV.get(64));

            ForgeOfGodsUpgrade.END.addExtraCost(
                GTOreDictUnificator
                    .get(OrePrefixes.frameGt, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Eternity, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Magmatter, 64),
                CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                CustomItemList.Machine_Multi_QuarkGluonPlasmaModule.get(64),
                CustomItemList.astralArrayFabricator.get(4),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Magmatter, 4),
                ItemList.ZPM6.get(32),
                ItemList.Field_Generator_UXV.get(64),
                ItemList.Robot_Arm_UXV.get(64));
        }

    }

    public static void runDevEnvironmentRecipes() {
        // put something in here to not crash the game in dev environment when opening the manual insertion window
        ForgeOfGodsUpgrade.START.addExtraCost(
            new ItemStack(Blocks.cobblestone, 4),
            new ItemStack(Blocks.dirt, 12),
            new ItemStack(Blocks.diamond_block, 8),
            new ItemStack(Blocks.gold_block, 32));
        ForgeOfGodsUpgrade.FDIM.addExtraCost(new ItemStack(Blocks.cobblestone, 8));
        ForgeOfGodsUpgrade.GPCI.addExtraCost(new ItemStack(Blocks.cobblestone, 12));
        ForgeOfGodsUpgrade.QGPIU.addExtraCost(new ItemStack(Blocks.cobblestone, 16));
        ForgeOfGodsUpgrade.CD.addExtraCost(new ItemStack(Blocks.cobblestone, 32));
        ForgeOfGodsUpgrade.EE.addExtraCost(new ItemStack(Blocks.cobblestone, 48));
        ForgeOfGodsUpgrade.END.addExtraCost(new ItemStack(Blocks.cobblestone, 64));
    }

    public static void addFakeUpgradeCostRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.START.getExtraCostNoNulls())
            .itemOutputs(
                CustomItemList.Godforge_GravitonFlowModulatorTier1.get(1),
                CustomItemList.Machine_Multi_SmeltingModule.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.START.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.FDIM.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Machine_Multi_MoltenModule.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.FDIM.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.GPCI.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Machine_Multi_PlasmaModule.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.GPCI.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.QGPIU.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Machine_Multi_QuarkGluonPlasmaModule.get(1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.QuarkGluonPlasma, FluidShapes.fluidLiquid, 1_000))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.QGPIU.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.CD.getExtraCostNoNulls())
            .itemOutputs(CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.CD.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.EE.getExtraCostNoNulls())
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Magmatter, FluidShapes.fluidMolten, 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidPlasma, 1_000))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.EE.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.END.getExtraCostNoNulls())
            .itemOutputs(
                CustomItemList.Godforge_GravitonFlowModulatorTier3.get(1),
                MaterialLibAPI.getStack(Materials.GravitonShard, Shapes.gem, 1))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.END.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
    }

    public static void initMoltenModuleRecipes() {
        for (GTRecipe recipe : RecipeMaps.blastFurnaceRecipes.getAllRecipes()) {
            List<ItemStack> itemOutputs = new ArrayList<>(1);
            List<FluidStack> fluidOutputs = new ArrayList<>(2);

            int[] originalChances = recipe.mOutputChances;
            IntList newChances = new IntArrayList();
            for (int i = 0; i < recipe.mOutputs.length; i++) {
                ItemStack stack = recipe.getOutput(i);
                if (stack == null) continue;
                FluidStack potentialFluid = convertToMolten(stack);
                if (potentialFluid != null) {
                    potentialFluid.amount *= stack.stackSize;
                    fluidOutputs.add(potentialFluid);
                } else {
                    itemOutputs.add(stack);
                    if (originalChances != null) {
                        int chance = 10000;
                        if (originalChances.length > i) {
                            chance = originalChances[i];
                        }
                        newChances.add(chance);
                    }
                }
            }

            fluidOutputs.addAll(Arrays.asList(recipe.mFluidOutputs));
            Integer heat = recipe.getMetadata(COIL_HEAT);

            GTRecipeBuilder builder = GTValues.RA.stdBuilder()
                .itemOutputs(itemOutputs.toArray(new ItemStack[0]))
                .fluidOutputs(fluidOutputs.toArray(new FluidStack[0]))
                .duration(recipe.mDuration)
                .eut(recipe.mEUt)
                .specialValue(recipe.mSpecialValue);

            if (recipe.mInputs != null) builder.itemInputs(recipe.mInputs);
            if (recipe.mFluidInputs != null) builder.fluidInputs(recipe.mFluidInputs);
            if (!newChances.isEmpty()) builder.outputChances(newChances.toIntArray());
            if (heat != null) builder.metadata(COIL_HEAT, heat);

            builder.addTo(TecTechRecipeMaps.godforgeMoltenRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.InfusedGold, Shapes.dust, 1),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1L))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.activatednetherite, FluidShapes.fluidMolten, 144),
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidMolten, 2))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .metadata(COIL_HEAT, 50000)
            .addTo(TecTechRecipeMaps.godforgeMoltenRecipes);
    }

    private static FluidStack convertToMolten(ItemStack stack) {
        // if this is null it has to be a gt++ material
        ItemData data = GTOreDictUnificator.getAssociation(stack);
        Material mat = data != null ? data.mMaterial.mMaterial : null;
        if (mat != null && LegacyNameDomain.contains(mat)) {
            if (MaterialUtils.hasMolten(mat)) {
                return MaterialUtils.molten(mat, INGOTS * data.mMaterial.mAmount / GTValues.M);
            } else if (MaterialUtils.fluidOf(mat) != null) {
                return MaterialUtils.fluid(mat, 1_000);
            }
        }
        int[] oreIDs = OreDictionary.getOreIDs(stack);
        if (oreIDs.length == 0) {
            return null;
        }
        String dict = OreDictionary.getOreName(oreIDs[0]);

        // Check various oredicts
        String strippedOreDict = null;
        if (dict.startsWith("ingotHot")) {
            strippedOreDict = dict.substring(8);
        } else if (dict.startsWith("dustRoasted") && !dict.contains("Cobalt")) {
            strippedOreDict = dict.substring(11);
        }
        if (strippedOreDict != null) {
            return FluidRegistry.getFluidStack("molten." + strippedOreDict.toLowerCase(), 1 * INGOTS);
        }
        return null;
    }
}
