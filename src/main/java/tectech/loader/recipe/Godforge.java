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

import gregtech.api.enums.materials2.Materials;
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
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.loaders.materials.LegacyNameDomain;
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

    /// Resolves each dust `ItemStack` back to its unified material via [GTOreDictUnificator#getAssociation]
    /// rather than parsing an OreDictionary name (`"dustIron"` -> `"Iron"`), so this does not depend on the
    /// item having exactly one registered OreDictionary id in a particular order -- true for both legacy and
    /// MaterialLib-cutover dust items, since both go through `addAssociation` on unification.
    private FluidStack[] convertToFluid(ItemStack[] items) {
        List<FluidStack> molten = new ArrayList<>();

        for (ItemStack itemStack : items) {
            ItemData association = GTOreDictUnificator.getAssociation(itemStack);
            Material material = association == null || association.mMaterial == null
                || association.mMaterial.mMaterial == null ? null : association.mMaterial.mMaterial;
            if (material == null || !LegacyNameDomain.contains(material)) {
                GTLog.err.println("Godforge.convertToFluid: no unification data for " + itemStack + ", skipping");
                continue;
            }
            molten.add(MaterialUtils.molten(material, 1 * INGOTS));
        }

        return molten.toArray(new FluidStack[0]);
    }

    @Override
    public void run() {
        // Solid to plasma recipes
        {
            // Fusion tier 1-3
            {
                // Single step
                ItemStack[] solids_t0_1step = {
                    MaterialLibAPI.getStack(Materials.Aluminium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Iron, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Calcium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Sulfur, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Zinc, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Niobium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tin, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Titanium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Nickel, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Silver, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Americium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Antimony, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Ardite, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Arsenic, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Barium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Beryllium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Caesium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Cadmium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Cerium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Cobalt, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Copper, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Desh, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Dysprosium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Erbium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Europium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Gadolinium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Gallium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Gold, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Holmium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Indium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lanthanum, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lithium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Lutetium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Magnesium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Manganese, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.MeteoricIron, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Molybdenum, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Neodymium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Oriharukon, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Palladium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Phosphorus, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Potassium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Praseodymium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Promethium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Rubidium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Samarium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Silicon, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Sodium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Strontium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tantalum, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tellurium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Terbium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Thulium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Tungsten, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Uranium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Uranium235, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Vanadium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Ytterbium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Yttrium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Chrome, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Zirconium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Thorium232, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Germanium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Thallium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Ruthenium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Rhenium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Rhodium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Iodine, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Hafnium, Materials2Shapes.dust, 1),
                    MaterialLibAPI.getStack(Materials.Curium, Materials2Shapes.dust, 1) };

                FluidStack[] molten_t0_1step = convertToFluid(solids_t0_1step);

                FluidStack[] solid_plasmas_t0_1step = {
                    MaterialLibAPI
                        .getFluidStack(Materials.Aluminium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Iron, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Calcium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Sulfur, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Zinc, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Niobium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Tin, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Titanium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Nickel, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Silver, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Americium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Antimony, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Ardite, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Arsenic, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Barium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Beryllium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Caesium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Cadmium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Carbon, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Cerium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Cobalt, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Copper, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Desh, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Dysprosium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Erbium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Europium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Gadolinium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Gallium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Gold, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Holmium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Indium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Lanthanum, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Lithium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Lutetium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Magnesium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Manganese, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.MeteoricIron, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Molybdenum, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Neodymium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Oriharukon, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Palladium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Phosphorus, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Potassium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Praseodymium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Promethium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Rubidium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Samarium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Silicon, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Sodium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Strontium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Tantalum, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Tellurium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Terbium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Thulium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Tungsten, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Uranium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Uranium235, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Vanadium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Ytterbium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Yttrium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                    MaterialLibAPI
                        .getFluidStack(Materials.Chrome, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
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
            ItemStack[] solids_t0_xstep = {
                MaterialLibAPI.getStack(Materials.Force, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Bismuth, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.AdvancedNitinol, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Boron, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.AstralTitanium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Runite, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iridium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadah, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Osmium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Platinum, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Plutonium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Californium, Materials2Shapes.dust, 1) };

            FluidStack[] molten_t0_xstep = convertToFluid(solids_t0_xstep);

            FluidStack[] solid_plasmas_t0_xstep = {
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Force), 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Bismuth, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AdvancedNitinol), 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.AstralTitanium), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Runite), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.CelestialTungsten), 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Iridium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Naquadah, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Osmium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Platinum, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Plutonium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
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
            ItemStack[] solids_t1_1step = { MaterialLibAPI.getStack(Materials.Lead, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Plutonium241, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Thorium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Naquadria, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Redstone, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Materials2Shapes.dust, 1) };

            FluidStack[] molten_t1_1step = convertToFluid(solids_t1_1step);

            FluidStack[] solid_plasmas_t1_1step = {
                MaterialLibAPI.getFluidStack(Materials.Lead, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Plutonium241, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Thorium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Naquadria, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Redstone, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials.CosmicNeutronium,
                    Materials2FluidShapes.fluidPlasma,
                    1 * INGOTS) };

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
            ItemStack[] solids_t1_xstep = {
                MaterialLibAPI.getStack(Materials.Neptunium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Fermium, Materials2Shapes.dust, 1) };

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
            ItemStack[] solids_t2_1step = {
                MaterialLibAPI.getStack(Materials.Rhugnor, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Dragonblood, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Bedrockium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Draconium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.DraconiumAwakened, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Ichorium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Infinity, Materials2Shapes.dust, 1) };

            FluidStack[] molten_t2_1step = convertToFluid(solids_t2_1step);

            FluidStack[] solid_plasmas_t2_1step = {
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Rhugnor), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Dragonblood), 1 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.ChromaticGlass), 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Bedrockium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Draconium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials.DraconiumAwakened,
                    Materials2FluidShapes.fluidPlasma,
                    1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Ichorium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Infinity, Materials2FluidShapes.fluidPlasma, 1 * INGOTS) };

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
            ItemStack[] solids_t2_xstep = {
                MaterialLibAPI.getStack(Materials.Hypogen, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Tritanium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.FleroviumGT5U, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Neutronium, Materials2Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Materials2Shapes.dust, 1) };

            FluidStack[] molten_t2_xstep = convertToFluid(solids_t2_xstep);

            FluidStack[] solid_plasmas_t2_xstep = {
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Hypogen), 144),
                MaterialLibAPI
                    .getFluidStack(Materials.Tritanium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.FleroviumGT5U, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Neutronium, Materials2FluidShapes.fluidPlasma, 1 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials.SixPhasedCopper,
                    Materials2FluidShapes.fluidPlasma,
                    1 * INGOTS) };

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
                    MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Deuterium, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Radon, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Tritium, Materials2FluidShapes.fluidGas, 500),
                    MaterialLibAPI.getFluidStack(Materials.Mercury, Materials2FluidShapes.fluidLiquid, 500) };
                FluidStack[] fluid_plasmas_t0_1step = {
                    MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Deuterium, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Radon, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Tritium, Materials2FluidShapes.fluidPlasma, 500),
                    MaterialLibAPI.getFluidStack(Materials.Mercury, Materials2FluidShapes.fluidPlasma, 500) };

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
                FluidStack[] fluids_t0_xstep = { MaterialUtils.legacyGtppFluid(Materials.Neon, 500),
                    MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 500),
                    MaterialUtils.legacyGtppFluid(Materials.Krypton, 500),
                    MaterialUtils.legacyGtppFluid(Materials.Xenon, 500) };
                FluidStack[] fluid_plasmas_t0_xstep = {
                    new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neon), 500),
                    MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidPlasma, 500),
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
                    .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Bromine, 500))
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
                .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Materials2Shapes.dust, 1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Iron, Materials2FluidShapes.fluidMolten, 1))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.QuarkGluonPlasma, Materials2FluidShapes.fluidLiquid, 1_000))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MAX)
                .metadata(FOG_EXOTIC_TIER, 1)
                .ignoreCollision()
                .fake()
                .addTo(godforgeExoticMatterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Materials2Shapes.dust, 1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Iron, Materials2FluidShapes.fluidMolten, 1),
                    MaterialLibAPI.getFluidStack(Materials.Bismuth, Materials2FluidShapes.fluidMolten, 1))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Magmatter, Materials2FluidShapes.fluidMolten, 4 * INGOTS))
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
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Zirconium, Materials2Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Thorium232, Materials2Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Germanium, Materials2Shapes.dust, 1), 2000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Thallium, Materials2Shapes.dust, 1), 2000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Ruthenium, Materials2Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Rhenium, Materials2Shapes.dust, 1), 2000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Rhodium, Materials2Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Hafnium, Materials2Shapes.dust, 1), 6000);
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Curium, Materials2Shapes.dust, 1), 10000);

        // Lanthanides Materials
        exoticModulePlasmaItemMap
            .put(MaterialLibAPI.getStack(Materials.Iodine, Materials2Shapes.dust, 1), 6000);

        // Mercury is weird, it has neither dust nor gas, so it needs to be added separately
        exoticModulePlasmaFluidMap
            .put(MaterialLibAPI.getFluidStack(Materials.Mercury, Materials2FluidShapes.fluidLiquid, 1), 6000);

        // Loop for adding all GT plasma materials
        for (int i = 0; i < plasmaGTMaterialList.size(); i++) {
            Material plasmaMaterial = plasmaGTMaterialList.get(i);
            if (plasmaMaterial.hasShape(Materials2Shapes.dust)) {
                exoticModulePlasmaItemMap
                    .put(MaterialLibAPI.getStack(plasmaMaterial, Materials2Shapes.dust, 1), plasmaGTWeightList.get(i));
            } else {
                exoticModulePlasmaFluidMap.put(MaterialUtils.gas(plasmaMaterial, 1), plasmaGTWeightList.get(i));
            }
        }

        // Magmatter map
        // GT materials
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.CosmicNeutronium, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Draconium, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.DraconiumAwakened, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Ichorium, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Neutronium, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.FleroviumGT5U, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Bedrockium, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Infinity, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Tritanium, Materials2Shapes.dust, 1), 100000);

        // GT++ materials
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.CelestialTungsten, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Hypogen, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Rhugnor, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.ChromaticGlass, Materials2Shapes.dust, 1), 100000);
        exoticModuleMagmatterItemMap
            .put(MaterialLibAPI.getStack(Materials.Dragonblood, Materials2Shapes.dust, 1), 100000);

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
                MaterialLibAPI.getStack(Materials.MetastableOganesson, Materials2Shapes.gearGt, 16),
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
                MaterialLibAPI.getStack(Materials.Quantum, Materials2Shapes.plateDense, 48),
                MaterialLibAPI.getStack(Materials.Rhugnor, Materials2Shapes.gearGt, 32),
                getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.QGPIU.addExtraCost(
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(16),
                ItemRefer.Compact_Fusion_MK5.get(2),
                ItemRefer.Compact_Fusion_Coil_T4.get(64),
                CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(16),
                ItemList.Machine_Multi_TranscendentPlasmaMixer.get(4),
                MaterialLibAPI.getStack(Materials.Rhugnor, Materials2Shapes.gearGt, 64),
                MaterialLibAPI.getStack(Materials.Ichorium, Materials2Shapes.gearGt, 64),
                getModItem(EternalSingularity.ID, "eternal_singularity", 32L),
                ItemList.Robot_Arm_UIV.get(64L),
                ItemList.Field_Generator_UEV.get(64L));

            ForgeOfGodsUpgrade.CD.addExtraCost(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Materials2PipeShapes.frameGt, 64),
                MaterialLibAPI.getStack(Materials.Dragonblood, Materials2PipeShapes.frameGt, 64),
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
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.QuarkGluonPlasma, Materials2FluidShapes.fluidLiquid, 1_000))
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
                MaterialLibAPI
                    .getFluidStack(Materials.Magmatter, Materials2FluidShapes.fluidMolten, 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Neutronium, Materials2FluidShapes.fluidPlasma, 1_000))
            .duration(1)
            .eut(1)
            .metadata(FOG_UPGRADE_NAME_SHORT, translateToLocal(ForgeOfGodsUpgrade.EE.getShortNameKey()))
            .fake()
            .addTo(TecTechRecipeMaps.godforgeFakeUpgradeCostRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ForgeOfGodsUpgrade.END.getExtraCostNoNulls())
            .itemOutputs(
                CustomItemList.Godforge_GravitonFlowModulatorTier3.get(1),
                MaterialLibAPI.getStack(Materials.GravitonShard, Materials2Shapes.gem, 1))
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
                MaterialLibAPI.getStack(Materials.InfusedGold, Materials2Shapes.dust, 1),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1L))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, 2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.activatednetherite, Materials2FluidShapes.fluidMolten, 144),
                MaterialLibAPI.getFluidStack(Materials.Boron, Materials2FluidShapes.fluidMolten, 2))
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
