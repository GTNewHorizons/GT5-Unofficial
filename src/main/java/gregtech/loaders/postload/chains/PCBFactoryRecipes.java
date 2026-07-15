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
import static gtPlusPlus.core.material.MaterialsAlloy.QUANTUM;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.CHRONOMATIC_GLASS;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.recipe.metadata.PCBFactoryUpgradeKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.PCBFactoryManager;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;

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
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 16 },
                ItemList.Robot_Arm_ZPM.get(8))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(36 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Naquadah,
                    Materials2FluidShapes.fluidMolten,
                    (int) (18 * INGOTS)))
            .itemOutputs(ItemList.PCBFactory.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("frameGtNaquadahAlloy", 1),
                Materials.get("Artherium-Sn")
                    .getPlates(6))
            .itemOutputs(ItemList.BasicPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("frameGtInfinity", 1),
                MaterialLibAPI.getStack(Materials2Materials.EnrichedHolmium, Materials2Shapes.plate, (int) (6)))
            .itemOutputs(ItemList.ReinforcedPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFrameBox(1),
                QUANTUM.getPlate(6),
                ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(24))
            .itemOutputs(ItemList.RadiationProofPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                HYPOGEN.getFrameBox(1),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.rotor, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Thulium, Materials2Shapes.plate, (int) (6)))
            .itemOutputs(ItemList.InfinityCooledCasing.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SpaceTime,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Copper,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier))))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Copper,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier))))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Copper,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier))))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Gold,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Electrum,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 1))))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Gold,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Electrum,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 1))))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Gold,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Electrum,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 1))))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 1))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Aluminium,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 2))))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 2))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Aluminium,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 2))))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 2))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Aluminium,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 2))))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 2))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Palladium,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Platinum,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 3))))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Palladium,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Platinum,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 3))))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Palladium,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getStack(
                        Materials2Materials.Platinum,
                        Materials2Shapes.foil,
                        (int) ((long) (16 * (Math.sqrt(tier - 3))))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 3))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (5000 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.GrowthMediumSterilized,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (5000 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.GrowthMediumSterilized,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (5000 * (Math.sqrt(tier - 4))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.GrowthMediumSterilized,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.SuperconductorUVBase, (long) (16 * (Math.sqrt(tier - 5)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (7500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.BiohMediumSterilized,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.SuperconductorUVBase, (long) (16 * (Math.sqrt(tier - 5)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (7500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.BiohMediumSterilized,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.SuperconductorUVBase, (long) (16 * (Math.sqrt(tier - 5)))),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (7500 * (Math.sqrt(tier - 5))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.BiohMediumSterilized,
                        Materials2FluidShapes.fluidLiquid,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    new ItemStack(
                        WerkstoffLoader.items.get(OrePrefixes.foil),
                        (int) (16 * (Math.sqrt(tier - 6))),
                        10106),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    CHRONOMATIC_GLASS.getFoil((int) (16 * (Math.sqrt(tier - 6)))))
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (12_500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.MysteriousCrystal,
                        Materials2FluidShapes.fluidMolten,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    new ItemStack(
                        WerkstoffLoader.items.get(OrePrefixes.foil),
                        (int) (16 * (Math.sqrt(tier - 6))),
                        10106),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    CHRONOMATIC_GLASS.getFoil((int) (16 * (Math.sqrt(tier - 6)))))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (12_500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.MysteriousCrystal,
                        Materials2FluidShapes.fluidMolten,
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
                    PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    new ItemStack(
                        WerkstoffLoader.items.get(OrePrefixes.foil),
                        (int) (16 * (Math.sqrt(tier - 6))),
                        10106),
                    GTOreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    CHRONOMATIC_GLASS.getFoil((int) (16 * (Math.sqrt(tier - 6)))))
                .circuit(3)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SulfuricAcid,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.IronIIIChloride,
                        Materials2FluidShapes.fluidLiquid,
                        (int) ((long) (12_500 * (Math.sqrt(tier - 6))))),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.MysteriousCrystal,
                        Materials2FluidShapes.fluidMolten,
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
