package gregtech.loaders.postload.chains;

import static gregtech.api.enums.ModIDs.BartWorks;
import static gregtech.api.enums.ModIDs.GTPlusPlus;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_PCBFactoryManager;
import gregtech.api.util.GT_Utility;

@SuppressWarnings("SpellCheckingInspection")
public class GT_PCBFactoryRecipes {

    public static void load() {
        final int mBioUpgradeBitMap = 0b1000;
        final int mTier3BitMap = 0b100;
        final int mTier2BitMap = 0b10;
        final int mTier1BitMap = 0b1;

        final Fluid solderLuV = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        // Load Multi Recipes
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Circuit_Board_Wetware.get(1),
                3600,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 32),
                        ItemList.Machine_ZPM_CircuitAssembler.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 16 }, ItemList.Robot_Arm_ZPM.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 144 * 36), Materials.Naquadah.getMolten(144 * 18) },
                ItemList.PCBFactory.get(1),
                6000 * 20,
                (int) GT_Values.VP[8]);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                        Materials.get("Artherium-Sn")
                                 .getPlates(6) },
                null,
                ItemList.BasicPhotolithographicFrameworkCasing.get(1),
                30 * 20,
                (int) GT_Values.VP[7]);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                        Materials.EnrichedHolmium.getPlates(6) },
                null,
                ItemList.ReinforcedPhotolithographicFrameworkCasing.get(1),
                30 * 20,
                (int) GT_Values.VP[9]);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_ModHandler.getModItem(GTPlusPlus.modID, "blockFrameGtCelestialTungsten", 1),
                        Materials.get("Quantum")
                                 .getPlates(6) },
                null,
                ItemList.RadiationProofPhotolithographicFrameworkCasing.get(1),
                30 * 20,
                (int) GT_Values.VP[11]);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_ModHandler.getModItem(GTPlusPlus.modID, "blockFrameGtHypogen", 1),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Infinity, 2),
                        Materials.Thulium.getPlates(6) },
                Materials.SpaceTime.getMolten(144 * 8),
                ItemList.InfinityCooledCasing.get(1),
                10 * 20,
                (int) GT_Values.VP[12]);

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
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.AnnealedCopper,
                                    (long) (16 * (Math.sqrt(tier)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Copper,
                                    (long) (16 * (Math.sqrt(tier)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier)))),
                            Materials.IronIIIChloride.getFluid((long) (250 * (Math.sqrt(tier)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 1.5))),
                    (int) GT_Values.VP[tier] * 3 / 4,
                    mTier1BitMap | mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 1; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 0.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                            GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.AnnealedCopper,
                                    (long) (16 * (Math.sqrt(tier)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Copper,
                                    (long) (16 * (Math.sqrt(tier)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier)))),
                            Materials.IronIIIChloride.getFluid((long) (250 * (Math.sqrt(tier)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 1.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 1; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(64));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Plastic_Advanced.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                            GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.AnnealedCopper,
                                    (long) (16 * (Math.sqrt(tier)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Copper,
                                    (long) (16 * (Math.sqrt(tier)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier)))),
                            Materials.IronIIIChloride.getFluid((long) (250 * (Math.sqrt(tier)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 1.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier3BitMap);
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
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Gold,
                                    (long) (16 * (Math.sqrt(tier - 1)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Electrum,
                                    (long) (16 * (Math.sqrt(tier - 1)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 1)))),
                            Materials.IronIIIChloride.getFluid((long) (500 * (Math.sqrt(tier - 1)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 2.5))),
                    (int) GT_Values.VP[tier] * 3 / 4,
                    mTier1BitMap | mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 2; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 1.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                            GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Gold,
                                    (long) (16 * (Math.sqrt(tier - 1)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Electrum,
                                    (long) (16 * (Math.sqrt(tier - 1)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 1)))),
                            Materials.IronIIIChloride.getFluid((long) (500 * (Math.sqrt(tier - 1)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 2.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 2; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 1))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Epoxy_Advanced.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                            GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Gold,
                                    (long) (16 * (Math.sqrt(tier - 1)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Electrum,
                                    (long) (16 * (Math.sqrt(tier - 1)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 1)))),
                            Materials.IronIIIChloride.getFluid((long) (500 * (Math.sqrt(tier - 1)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 2.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier3BitMap);
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
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Aluminium,
                                    (long) (16 * (Math.sqrt(tier - 2)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.EnergeticAlloy,
                                    (long) (16 * (Math.sqrt(tier - 2)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 2)))),
                            Materials.IronIIIChloride.getFluid((long) (1000 * (Math.sqrt(tier - 2)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 3.5))),
                    (int) GT_Values.VP[tier] * 3 / 4,
                    mTier1BitMap | mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 3; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 2.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                            GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Aluminium,
                                    (long) (16 * (Math.sqrt(tier - 2)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.EnergeticAlloy,
                                    (long) (16 * (Math.sqrt(tier - 2)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 2)))),
                            Materials.IronIIIChloride.getFluid((long) (1000 * (Math.sqrt(tier - 2)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 3.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 3; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 2))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Fiberglass_Advanced.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                            GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Aluminium,
                                    (long) (16 * (Math.sqrt(tier - 2)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.EnergeticAlloy,
                                    (long) (16 * (Math.sqrt(tier - 2)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 2)))),
                            Materials.IronIIIChloride.getFluid((long) (1000 * (Math.sqrt(tier - 2)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 3.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier3BitMap);
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
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Palladium,
                                    (long) (16 * (Math.sqrt(tier - 3)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Platinum,
                                    (long) (16 * (Math.sqrt(tier - 3)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 3)))),
                            Materials.IronIIIChloride.getFluid((long) (2000 * (Math.sqrt(tier - 3)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 4.5))),
                    (int) GT_Values.VP[tier] * 3 / 4,
                    mTier1BitMap | mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 4; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                            GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Palladium,
                                    (long) (16 * (Math.sqrt(tier - 3)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Platinum,
                                    (long) (16 * (Math.sqrt(tier - 3)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 3)))),
                            Materials.IronIIIChloride.getFluid((long) (2000 * (Math.sqrt(tier - 3)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 4.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier2BitMap | mTier3BitMap);
        }
        for (int tier = 4; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 3))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Multifiberglass_Elite.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                            GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Palladium,
                                    (long) (16 * (Math.sqrt(tier - 3)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Platinum,
                                    (long) (16 * (Math.sqrt(tier - 3)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 3)))),
                            Materials.IronIIIChloride.getFluid((long) (2000 * (Math.sqrt(tier - 3)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 4.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier3BitMap);
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
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.EnrichedHolmium,
                                    (long) (16 * (Math.sqrt(tier - 4)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.NiobiumTitanium,
                                    (long) (16 * (Math.sqrt(tier - 4)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 4)))),
                            Materials.IronIIIChloride.getFluid((long) (5000 * (Math.sqrt(tier - 4)))),
                            Materials.GrowthMediumSterilized.getFluid((long) (2000 * (Math.sqrt(tier - 4)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))),
                    (int) GT_Values.VP[tier] * 3 / 4,
                    mTier1BitMap | mTier2BitMap | mTier3BitMap | mBioUpgradeBitMap);
        }
        for (int tier = 5; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                            GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.EnrichedHolmium,
                                    (long) (16 * (Math.sqrt(tier - 4)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.NiobiumTitanium,
                                    (long) (16 * (Math.sqrt(tier - 4)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 4)))),
                            Materials.IronIIIChloride.getFluid((long) (5000 * (Math.sqrt(tier - 4)))),
                            Materials.GrowthMediumSterilized.getFluid((long) (2000 * (Math.sqrt(tier - 4)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 5.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier2BitMap | mTier3BitMap | mBioUpgradeBitMap);
        }
        for (int tier = 5; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 4))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Wetware_Extreme.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                            GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.EnrichedHolmium,
                                    (long) (16 * (Math.sqrt(tier - 4)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.NiobiumTitanium,
                                    (long) (16 * (Math.sqrt(tier - 4)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 4)))),
                            Materials.IronIIIChloride.getFluid((long) (5000 * (Math.sqrt(tier - 4)))),
                            Materials.GrowthMediumSterilized.getFluid((long) (2000 * (Math.sqrt(tier - 4)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 5.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier3BitMap | mBioUpgradeBitMap);
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
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Longasssuperconductornameforuvwire,
                                    (long) (16 * (Math.sqrt(tier - 5)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Neutronium,
                                    (long) (16 * (Math.sqrt(tier - 5)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 5)))),
                            Materials.IronIIIChloride.getFluid((long) (7500 * (Math.sqrt(tier - 5)))),
                            Materials.BioMediumSterilized.getFluid((long) (4000 * (Math.sqrt(tier - 5)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))),
                    (int) GT_Values.VP[tier] * 3 / 4,
                    mTier1BitMap | mTier2BitMap | mTier3BitMap | mBioUpgradeBitMap);
        }
        for (int tier = 6; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5.5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                            GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Longasssuperconductornameforuvwire,
                                    (long) (16 * (Math.sqrt(tier - 5)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Neutronium,
                                    (long) (16 * (Math.sqrt(tier - 5)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 5)))),
                            Materials.IronIIIChloride.getFluid((long) (7500 * (Math.sqrt(tier - 5)))),
                            Materials.BioMediumSterilized.getFluid((long) (4000 * (Math.sqrt(tier - 5)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 6.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier2BitMap | mTier3BitMap | mBioUpgradeBitMap);
        }
        for (int tier = 6; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
            int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 5))));
            List<ItemStack> aBoards = new ArrayList<>();
            for (int i = amountOfBoards; i > 64; i -= 64) {
                aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(i));
                amountOfBoards -= 64;
            }
            aBoards.add(ItemList.Circuit_Board_Bio_Ultra.get(amountOfBoards));
            GT_Values.RA.addPCBFactoryRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                            GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                            GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                .getPlates(1),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Longasssuperconductornameforuvwire,
                                    (long) (16 * (Math.sqrt(tier - 5)))),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.foil,
                                    Materials.Neutronium,
                                    (long) (16 * (Math.sqrt(tier - 5)))) },
                    new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 5)))),
                            Materials.IronIIIChloride.getFluid((long) (7500 * (Math.sqrt(tier - 5)))),
                            Materials.BioMediumSterilized.getFluid((long) (4000 * (Math.sqrt(tier - 5)))) },
                    aBoards.toArray(new ItemStack[0]),
                    (int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 6.5))),
                    (int) GT_Values.VP[tier + 1] * 3 / 4,
                    mTier3BitMap | mBioUpgradeBitMap);
        }

        if (GTPlusPlus.isModLoaded()) {
            // Optical Circuit Board
            for (int tier = 7; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
                int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 7))));
                List<ItemStack> aBoards = new ArrayList<>();
                for (int i = amountOfBoards; i > 64; i -= 64) {
                    aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                    amountOfBoards -= 64;
                }
                aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
                GT_Values.RA.addPCBFactoryRecipe(
                        new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                                GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                    .getPlates(1),
                                GT_ModHandler.getModItem(
                                    BartWorks.modID,
                                        "gt.bwMetaGeneratedfoil",
                                        (long) (16 * (Math.sqrt(tier - 6))),
                                        10106),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.foil,
                                        Materials.InfinityCatalyst,
                                        (long) (16 * (Math.sqrt(tier - 6)))),
                                GT_ModHandler.getModItem(
                                        "miscutils",
                                        "itemFoilChromaticGlass",
                                        (long) (16 * (Math.sqrt(tier - 6)))) },
                        new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 6)))),
                                Materials.IronIIIChloride.getFluid((long) (12500 * (Math.sqrt(tier - 6)))),
                                Materials.MysteriousCrystal.getMolten((long) (2880 * (Math.sqrt(tier - 6)))) },
                        aBoards.toArray(new ItemStack[0]),
                        (int) Math.ceil(600 / Math.sqrt(Math.pow(1.5, tier - 5.5))),
                        (int) GT_Values.VP[tier] * 3 / 4,
                        mTier1BitMap | mTier2BitMap | mTier3BitMap);
            }
            for (int tier = 7; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
                int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6.5))));
                List<ItemStack> aBoards = new ArrayList<>();
                for (int i = amountOfBoards; i > 64; i -= 64) {
                    aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                    amountOfBoards -= 64;
                }
                aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
                GT_Values.RA.addPCBFactoryRecipe(
                        new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                                GT_Utility.getNaniteAsCatalyst(Materials.Silver),
                                GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                    .getPlates(1),
                                GT_ModHandler.getModItem(
                                    BartWorks.modID,
                                        "gt.bwMetaGeneratedfoil",
                                        (long) (16 * (Math.sqrt(tier - 6))),
                                        10106),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.foil,
                                        Materials.InfinityCatalyst,
                                        (long) (16 * (Math.sqrt(tier - 6)))),
                                GT_ModHandler.getModItem(
                                        "miscutils",
                                        "itemFoilChromaticGlass",
                                        (long) (16 * (Math.sqrt(tier - 6)))) },
                        new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 6)))),
                                Materials.IronIIIChloride.getFluid((long) (12500 * (Math.sqrt(tier - 6)))),
                                Materials.MysteriousCrystal.getMolten((long) (2880 * (Math.sqrt(tier - 6)))) },
                        aBoards.toArray(new ItemStack[0]),
                        (int) Math.ceil(500 / Math.sqrt(Math.pow(1.5, tier - 6.5))),
                        (int) GT_Values.VP[tier + 1] * 3 / 4,
                        mTier2BitMap | mTier3BitMap);
            }
            for (int tier = 7; tier <= GT_PCBFactoryManager.mTiersOfPlastics; tier++) {
                int amountOfBoards = (int) Math.ceil(8 * (Math.sqrt(Math.pow(2, tier - 6))));
                List<ItemStack> aBoards = new ArrayList<>();
                for (int i = amountOfBoards; i > 64; i -= 64) {
                    aBoards.add(ItemList.Circuit_Board_Optical.get(i));
                    amountOfBoards -= 64;
                }
                aBoards.add(ItemList.Circuit_Board_Optical.get(amountOfBoards));
                GT_Values.RA.addPCBFactoryRecipe(
                        new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                                GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                                GT_PCBFactoryManager.getPlasticMaterialFromTier(tier)
                                                    .getPlates(1),
                                GT_ModHandler.getModItem(
                                    BartWorks.modID,
                                        "gt.bwMetaGeneratedfoil",
                                        (long) (16 * (Math.sqrt(tier - 6))),
                                        10106),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.foil,
                                        Materials.InfinityCatalyst,
                                        (long) (16 * (Math.sqrt(tier - 6)))),
                                GT_ModHandler.getModItem(
                                        "miscutils",
                                        "itemFoilChromaticGlass",
                                        (long) (16 * (Math.sqrt(tier - 6)))) },
                        new FluidStack[] { Materials.SulfuricAcid.getFluid((long) (500 * (Math.sqrt(tier - 6)))),
                                Materials.IronIIIChloride.getFluid((long) (12500 * (Math.sqrt(tier - 6)))),
                                Materials.MysteriousCrystal.getMolten((long) (2880 * (Math.sqrt(tier - 6)))) },
                        aBoards.toArray(new ItemStack[0]),
                        (int) Math.ceil(400 / Math.sqrt(Math.pow(1.5, tier - 6.5))),
                        (int) GT_Values.VP[tier + 1] * 3 / 4,
                        mTier3BitMap);
            }
        }
    }
}
