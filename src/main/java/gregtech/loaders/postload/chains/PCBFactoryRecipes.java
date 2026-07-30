package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.metadata.PCBFactoryUpgrade.BIO;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.PCB_NANITE_MATERIAL;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.PipeShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.recipe.metadata.PCBFactoryUpgradeKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.PCBFactoryManager;
import gregtech.api.util.recipe.Scanning;

@SuppressWarnings("SpellCheckingInspection")
public class PCBFactoryRecipes {

    private static final PCBFactoryTierKey TIER = PCBFactoryTierKey.INSTANCE;
    private static final PCBFactoryUpgradeKey UPGRADE = PCBFactoryUpgradeKey.INSTANCE;

    public static void load() {
        // Load Multi Recipes
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Circuit_Board_Wetware.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 20 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GTOreDictUnificator.get("frameGtNeutronium", 32),
                ItemList.Machine_ZPM_CircuitAssembler.get(4),
                new Object[] { Circuits.LuV.getIngredient(), 16 },
                ItemList.Robot_Arm_ZPM.get(8))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Indalloy140, 36 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Naquadah, FluidShapes.fluidMolten, (int) (18 * INGOTS)))
            .itemOutputs(ItemList.PCBFactory.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("frameGtNaquadahAlloy", 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ArtheriumSn, 6))
            .itemOutputs(ItemList.BasicPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("frameGtInfinity", 1),
                MaterialLibAPI.getStack(Materials.EnrichedHolmium, Shapes.plate, 6))
            .itemOutputs(ItemList.ReinforcedPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CelestialTungsten, PipeShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.plate, 6),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(24))
            .itemOutputs(ItemList.RadiationProofPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Hypogen, PipeShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.rotor, 2),
                MaterialLibAPI.getStack(Materials.Thulium, Shapes.plate, 6))
            .itemOutputs(ItemList.InfinityCooledCasing.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);

        // Load CircuitBoard Recipes

        // Plastic Circuit Board
        for (int tier = 1; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(GTUtility.powInt(2, tier - 1))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    MaterialLibAPI.getStack(Materials.Copper, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier))))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (250 * (Math.sqrt(tier))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 1.5))))
                .eut((int) GTValues.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 1; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 0.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    MaterialLibAPI.getStack(Materials.Copper, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier))))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (250 * (Math.sqrt(tier))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 1.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(PCB_NANITE_MATERIAL, Materials.Silver)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 1; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(GTUtility.powInt(2, tier))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    MaterialLibAPI.getStack(Materials.Copper, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier))))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (250 * (Math.sqrt(tier))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 1.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Advanced Circuit Board
        for (int tier = 2; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(GTUtility.powInt(2, tier - 2))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI.getStack(Materials.Gold, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI
                        .getStack(Materials.Electrum, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 1))))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 2.5))))
                .eut((int) GTValues.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 2; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 1.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI.getStack(Materials.Gold, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI
                        .getStack(Materials.Electrum, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 1))))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 2.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(PCB_NANITE_MATERIAL, Materials.Silver)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 2; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(GTUtility.powInt(2, tier - 1))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI.getStack(Materials.Gold, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI
                        .getStack(Materials.Electrum, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 1))))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 2.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // More Advanced Circuit Board
        for (int tier = 3; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI
                        .getStack(Materials.Aluminium, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 2))))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 2))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (1000 * (Math.sqrt(tier - 2))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 3.5))))
                .eut((int) GTValues.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 3; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 2.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI
                        .getStack(Materials.Aluminium, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 2))))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 2))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (1000 * (Math.sqrt(tier - 2))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 3.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(PCB_NANITE_MATERIAL, Materials.Silver)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 3; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 2))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI
                        .getStack(Materials.Aluminium, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 2))))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 2))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (1000 * (Math.sqrt(tier - 2))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 3.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Elite Circuit Board
        for (int tier = 4; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI
                        .getStack(Materials.Palladium, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI
                        .getStack(Materials.Platinum, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 3))))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (2000 * (Math.sqrt(tier - 3))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 4.5))))
                .eut((int) GTValues.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 4; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI
                        .getStack(Materials.Palladium, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI
                        .getStack(Materials.Platinum, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 3))))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (2000 * (Math.sqrt(tier - 3))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 4.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(PCB_NANITE_MATERIAL, Materials.Silver)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 4; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI
                        .getStack(Materials.Palladium, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI
                        .getStack(Materials.Platinum, Shapes.foil, (int) ((long) (16 * (Math.sqrt(tier - 3))))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (2000 * (Math.sqrt(tier - 3))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 4.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Wetware Circuit Board
        for (int tier = 5; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (5000 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.GrowthMediumSterilized,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (2000 * (Math.sqrt(tier - 4))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GTValues.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 5; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (5000 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.GrowthMediumSterilized,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (2000 * (Math.sqrt(tier - 4))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(PCB_NANITE_MATERIAL, Materials.Silver)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 5; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (5000 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.GrowthMediumSterilized,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (2000 * (Math.sqrt(tier - 4))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Bioware Circuit Board
        for (int tier = 6; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator.get(
                        OrePrefixes.foil,
                        Materials.Longasssuperconductornameforuvwire,
                        (long) (16 * (Math.sqrt(tier - 5)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (7500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.BiohMediumSterilized,
                        FluidShapes.fluidLiquid,
                        (int) (4000 * (Math.sqrt(tier - 5)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GTValues.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 6; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator.get(
                        OrePrefixes.foil,
                        Materials.Longasssuperconductornameforuvwire,
                        (long) (16 * (Math.sqrt(tier - 5)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (7500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.BiohMediumSterilized,
                        FluidShapes.fluidLiquid,
                        (int) (4000 * (Math.sqrt(tier - 5)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(PCB_NANITE_MATERIAL, Materials.Silver)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 6; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    GTOreDictUnificator.get(
                        OrePrefixes.foil,
                        Materials.Longasssuperconductornameforuvwire,
                        (long) (16 * (Math.sqrt(tier - 5)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (7500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.BiohMediumSterilized,
                        FluidShapes.fluidLiquid,
                        (int) (4000 * (Math.sqrt(tier - 5)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Optical Circuit Board
        for (int tier = 7; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 7))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.foil, (int) (16 * (Math.sqrt(tier - 6)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.foil, (int) (16 * (Math.sqrt(tier - 6)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (12_500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.MysteriousCrystal,
                        FluidShapes.fluidMolten,
                        (int) ((long) (20 * INGOTS * (Math.sqrt(tier - 6))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GTValues.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 7; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.foil, (int) (16 * (Math.sqrt(tier - 6)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.foil, (int) (16 * (Math.sqrt(tier - 6)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (12_500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.MysteriousCrystal,
                        FluidShapes.fluidMolten,
                        (int) ((long) (20 * INGOTS * (Math.sqrt(tier - 6))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(PCB_NANITE_MATERIAL, Materials.Silver)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 7; tier <= PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, PCBFactoryManager.getPlasticMaterialFromTier(tier), 1),
                    MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.foil, (int) (16 * (Math.sqrt(tier - 6)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.foil, (int) (16 * (Math.sqrt(tier - 6)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.SulfuricAcid,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.IronIIIChloride,
                        FluidShapes.fluidLiquid,
                        (int) ((long) (12_500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials.MysteriousCrystal,
                        FluidShapes.fluidMolten,
                        (int) ((long) (20 * INGOTS * (Math.sqrt(tier - 6))))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GTValues.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(PCB_NANITE_MATERIAL, Materials.Gold)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
    }
}
