package gregtech.loaders.postload.chains;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.metadata.PCBFactoryUpgrade.BIO;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.CHRONOMATIC_GLASS;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.HYPOGEN;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.recipe.metadata.PCBFactoryUpgradeKey;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_PCBFactoryManager;
import gregtech.api.util.GT_Utility;

@SuppressWarnings("SpellCheckingInspection")
public class GT_PCBFactoryRecipes {

    private static final PCBFactoryTierKey TIER = PCBFactoryTierKey.INSTANCE;
    private static final PCBFactoryUpgradeKey UPGRADE = PCBFactoryUpgradeKey.INSTANCE;

    public static void load() {
        final Fluid solderLuV = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // Load Multi Recipes
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Circuit_Board_Wetware.get(1))
            .metadata(RESEARCH_TIME, 3 * MINUTES)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 32),
                ItemList.Machine_ZPM_CircuitAssembler.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 16 },
                ItemList.Robot_Arm_ZPM.get(8))
            .fluidInputs(new FluidStack(solderLuV, 144 * 36), Materials.Naquadah.getMolten(144 * 18))
            .itemOutputs(ItemList.PCBFactory.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                Materials.get("Artherium-Sn")
                    .getPlates(6))
            .itemOutputs(ItemList.BasicPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                Materials.EnrichedHolmium.getPlates(6))
            .itemOutputs(ItemList.ReinforcedPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CELESTIAL_TUNGSTEN.getFrameBox(1),
                Materials.get("Quantum")
                    .getPlates(6))
            .itemOutputs(ItemList.RadiationProofPhotolithographicFrameworkCasing.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                HYPOGEN.getFrameBox(1),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Infinity, 2),
                Materials.Thulium.getPlates(6))
            .itemOutputs(ItemList.InfinityCooledCasing.get(1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(8 * 144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);

        // Load CircuitBoard Recipes

        // Plastic Circuit Board
        for (int tier = 1; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 1))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Copper, (long) (16 * (Math.sqrt(tier)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier)))),
                    Materials.IronIIIChloride.getFluid((long) (250 * (Math.sqrt(tier)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 1.5))))
                .eut((int) GT_Values.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 1; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 0.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Copper, (long) (16 * (Math.sqrt(tier)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier)))),
                    Materials.IronIIIChloride.getFluid((long) (250 * (Math.sqrt(tier)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 1.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 1; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),
                    GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.AnnealedCopper, (long) (16 * (Math.sqrt(tier)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Copper, (long) (16 * (Math.sqrt(tier)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier)))),
                    Materials.IronIIIChloride.getFluid((long) (250 * (Math.sqrt(tier)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 1.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Advanced Circuit Board
        for (int tier = 2; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 2))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, (long) (16 * (Math.sqrt(tier - 1)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, (long) (16 * (Math.sqrt(tier - 1)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 1)))),
                    Materials.IronIIIChloride.getFluid((long) (500 * (Math.sqrt(tier - 1)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 2.5))))
                .eut((int) GT_Values.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 2; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 1.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, (long) (16 * (Math.sqrt(tier - 1)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, (long) (16 * (Math.sqrt(tier - 1)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 1)))),
                    Materials.IronIIIChloride.getFluid((long) (500 * (Math.sqrt(tier - 1)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 2.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 2; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 1))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),
                    GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, (long) (16 * (Math.sqrt(tier - 1)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, (long) (16 * (Math.sqrt(tier - 1)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 1)))),
                    Materials.IronIIIChloride.getFluid((long) (500 * (Math.sqrt(tier - 1)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 2.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // More Advanced Circuit Board
        for (int tier = 3; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Aluminium, (long) (16 * (Math.sqrt(tier - 2)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 2)))),
                    Materials.IronIIIChloride.getFluid((long) (1000 * (Math.sqrt(tier - 2)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 3.5))))
                .eut((int) GT_Values.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 3; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 2.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Aluminium, (long) (16 * (Math.sqrt(tier - 2)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 2)))),
                    Materials.IronIIIChloride.getFluid((long) (1000 * (Math.sqrt(tier - 2)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 3.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 3; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 2))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),
                    GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Aluminium, (long) (16 * (Math.sqrt(tier - 2)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnergeticAlloy, (long) (16 * (Math.sqrt(tier - 2)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 2)))),
                    Materials.IronIIIChloride.getFluid((long) (1000 * (Math.sqrt(tier - 2)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 3.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Elite Circuit Board
        for (int tier = 4; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Palladium, (long) (16 * (Math.sqrt(tier - 3)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, (long) (16 * (Math.sqrt(tier - 3)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 3)))),
                    Materials.IronIIIChloride.getFluid((long) (2000 * (Math.sqrt(tier - 3)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 4.5))))
                .eut((int) GT_Values.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 4; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Palladium, (long) (16 * (Math.sqrt(tier - 3)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, (long) (16 * (Math.sqrt(tier - 3)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 3)))),
                    Materials.IronIIIChloride.getFluid((long) (2000 * (Math.sqrt(tier - 3)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 4.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 4; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),
                    GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Palladium, (long) (16 * (Math.sqrt(tier - 3)))),
                    GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Platinum, (long) (16 * (Math.sqrt(tier - 3)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 3)))),
                    Materials.IronIIIChloride.getFluid((long) (2000 * (Math.sqrt(tier - 3)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 4.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Wetware Circuit Board
        for (int tier = 5; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 4)))),
                    Materials.IronIIIChloride.getFluid((long) (5000 * (Math.sqrt(tier - 4)))),
                    Materials.GrowthMediumSterilized.getFluid((long) (2000 * (Math.sqrt(tier - 4)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GT_Values.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 5; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 4)))),
                    Materials.IronIIIChloride.getFluid((long) (5000 * (Math.sqrt(tier - 4)))),
                    Materials.GrowthMediumSterilized.getFluid((long) (2000 * (Math.sqrt(tier - 4)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 5; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),
                    GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.EnrichedHolmium, (long) (16 * (Math.sqrt(tier - 4)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.NiobiumTitanium, (long) (16 * (Math.sqrt(tier - 4)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 4)))),
                    Materials.IronIIIChloride.getFluid((long) (5000 * (Math.sqrt(tier - 4)))),
                    Materials.GrowthMediumSterilized.getFluid((long) (2000 * (Math.sqrt(tier - 4)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Bioware Circuit Board
        for (int tier = 6; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator.get(
                        OrePrefixes.foil,
                        Materials.Longasssuperconductornameforuvwire,
                        (long) (16 * (Math.sqrt(tier - 5)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 5)))),
                    Materials.IronIIIChloride.getFluid((long) (7500 * (Math.sqrt(tier - 5)))),
                    Materials.BioMediumSterilized.getFluid((long) (4000 * (Math.sqrt(tier - 5)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GT_Values.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 6; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator.get(
                        OrePrefixes.foil,
                        Materials.Longasssuperconductornameforuvwire,
                        (long) (16 * (Math.sqrt(tier - 5)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 5)))),
                    Materials.IronIIIChloride.getFluid((long) (7500 * (Math.sqrt(tier - 5)))),
                    Materials.BioMediumSterilized.getFluid((long) (4000 * (Math.sqrt(tier - 5)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 6; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),
                    GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    GT_OreDictUnificator.get(
                        OrePrefixes.foil,
                        Materials.Longasssuperconductornameforuvwire,
                        (long) (16 * (Math.sqrt(tier - 5)))),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.Neutronium, (long) (16 * (Math.sqrt(tier - 5)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 5)))),
                    Materials.IronIIIChloride.getFluid((long) (7500 * (Math.sqrt(tier - 5)))),
                    Materials.BioMediumSterilized.getFluid((long) (4000 * (Math.sqrt(tier - 5)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .metadata(UPGRADE, BIO)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }

        // Optical Circuit Board
        for (int tier = 7; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 7))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(1),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    new ItemStack(
                        WerkstoffLoader.items.get(OrePrefixes.foil),
                        (int) (16 * (Math.sqrt(tier - 6))),
                        10106),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    CHRONOMATIC_GLASS.getFoil((int) (16 * (Math.sqrt(tier - 6)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 6)))),
                    Materials.IronIIIChloride.getFluid((long) (12500 * (Math.sqrt(tier - 6)))),
                    Materials.MysteriousCrystal.getMolten((long) (2880 * (Math.sqrt(tier - 6)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))))
                .eut((int) GT_Values.VP[tier] * 3 / 4)
                .metadata(TIER, 1)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 7; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(2),
                    GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    new ItemStack(
                        WerkstoffLoader.items.get(OrePrefixes.foil),
                        (int) (16 * (Math.sqrt(tier - 6))),
                        10106),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    CHRONOMATIC_GLASS.getFoil((int) (16 * (Math.sqrt(tier - 6)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 6)))),
                    Materials.IronIIIChloride.getFluid((long) (12500 * (Math.sqrt(tier - 6)))),
                    Materials.MysteriousCrystal.getMolten((long) (2880 * (Math.sqrt(tier - 6)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 2)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
        for (int tier = 7; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(3),
                    GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                    GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                        .getPlates(1),
                    new ItemStack(
                        WerkstoffLoader.items.get(OrePrefixes.foil),
                        (int) (16 * (Math.sqrt(tier - 6))),
                        10106),
                    GT_OreDictUnificator
                        .get(OrePrefixes.foil, Materials.InfinityCatalyst, (long) (16 * (Math.sqrt(tier - 6)))),
                    CHRONOMATIC_GLASS.getFoil((int) (16 * (Math.sqrt(tier - 6)))))
                .fluidInputs(
                    Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 6)))),
                    Materials.IronIIIChloride.getFluid((long) (12500 * (Math.sqrt(tier - 6)))),
                    Materials.MysteriousCrystal.getMolten((long) (2880 * (Math.sqrt(tier - 6)))))
                .itemOutputs(aBoards.toArray(new ItemStack[0]))
                .duration((int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 6.5))))
                .eut((int) GT_Values.VP[tier + 1] * 3 / 4)
                .metadata(TIER, 3)
                .addTo(RecipeMaps.pcbFactoryRecipes);
        }
    }
}
