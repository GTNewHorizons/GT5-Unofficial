package gregtech.common.items;

import static gregtech.api.enums.GTValues.L;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gregtech.client.GTTooltipHandler.Tier.HV;
import static gregtech.client.GTTooltipHandler.Tier.IV;
import static gregtech.client.GTTooltipHandler.Tier.LuV;
import static gregtech.client.GTTooltipHandler.Tier.ZPM;
import static gregtech.client.GTTooltipHandler.registerTieredTooltip;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorComputerCore0;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorComputerCore1;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorComputerCore2;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorComputerCore3;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorFrame0;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorFrame1;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorFrame2;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorFrame3;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorHologram;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorLens0;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorLens1;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorLens2;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorLens3;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorPowerCore0;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorPowerCore1;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorPowerCore2;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorPowerCore3;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorTeleporterCore0;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorTeleporterCore1;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorTeleporterCore2;
import static gregtech.common.items.IDMetaItem04.MatterManipulatorTeleporterCore3;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.ModItems;

public class MetaGeneratedItem04 extends MetaGeneratedItem {

    public static MetaGeneratedItem04 INSTANCE;

    public MetaGeneratedItem04() {
        super("metaitem.04", (short) 0, Short.MAX_VALUE);
        INSTANCE = this;

        init();
    }

    private void init() {
        addItems();
        registerAllTieredTooltips();
    }

    private void addItems() {
        addManipulatorParts();
    }

    public void addRecipes() {
        addManipulatorRecipes();
    }

    private void addManipulatorParts() {
        ItemList.MatterManipulatorHologram.set(addItem(MatterManipulatorHologram.ID, "Matter Manipulator Plan", ""));
        ItemList.MatterManipulatorPowerCore0
            .set(addItem(MatterManipulatorPowerCore0.ID, "Prototype Matter Manipulator Power Core", ""));
        ItemList.MatterManipulatorComputerCore0
            .set(addItem(MatterManipulatorComputerCore0.ID, "Prototype Matter Manipulator Computer Core", ""));
        ItemList.MatterManipulatorTeleporterCore0
            .set(addItem(MatterManipulatorTeleporterCore0.ID, "Prototype Matter Manipulator Teleporter Core", ""));
        ItemList.MatterManipulatorFrame0
            .set(addItem(MatterManipulatorFrame0.ID, "Prototype Matter Manipulator Frame", ""));
        ItemList.MatterManipulatorLens0
            .set(addItem(MatterManipulatorLens0.ID, "Prototype Matter Manipulator Lens Assembly", ""));
        ItemList.MatterManipulatorPowerCore1
            .set(addItem(MatterManipulatorPowerCore1.ID, "Matter Manipulator Power Core MKI", ""));
        ItemList.MatterManipulatorComputerCore1
            .set(addItem(MatterManipulatorComputerCore1.ID, "Matter Manipulator Computer Core MKI", ""));
        ItemList.MatterManipulatorTeleporterCore1
            .set(addItem(MatterManipulatorTeleporterCore1.ID, "Matter Manipulator Teleporter Core MKI", ""));
        ItemList.MatterManipulatorFrame1.set(addItem(MatterManipulatorFrame1.ID, "Matter Manipulator Frame MKI", ""));
        ItemList.MatterManipulatorLens1
            .set(addItem(MatterManipulatorLens1.ID, "Matter Manipulator Lens Assembly MKI", ""));
        ItemList.MatterManipulatorPowerCore2
            .set(addItem(MatterManipulatorPowerCore2.ID, "Matter Manipulator Power Core MKII", ""));
        ItemList.MatterManipulatorComputerCore2
            .set(addItem(MatterManipulatorComputerCore2.ID, "Matter Manipulator Computer Core MKII", ""));
        ItemList.MatterManipulatorTeleporterCore2
            .set(addItem(MatterManipulatorTeleporterCore2.ID, "Matter Manipulator Teleporter Core MKII", ""));
        ItemList.MatterManipulatorFrame2.set(addItem(MatterManipulatorFrame2.ID, "Matter Manipulator Frame MKII", ""));
        ItemList.MatterManipulatorLens2
            .set(addItem(MatterManipulatorLens2.ID, "Matter Manipulator Lens Assembly MKII", ""));
        ItemList.MatterManipulatorPowerCore3
            .set(addItem(MatterManipulatorPowerCore3.ID, "Matter Manipulator Power Core MKIII", ""));
        ItemList.MatterManipulatorComputerCore3
            .set(addItem(MatterManipulatorComputerCore3.ID, "Matter Manipulator Computer Core MKIII", ""));
        ItemList.MatterManipulatorTeleporterCore3
            .set(addItem(MatterManipulatorTeleporterCore3.ID, "Matter Manipulator Teleporter Core MKIII", ""));
        ItemList.MatterManipulatorFrame3.set(addItem(MatterManipulatorFrame3.ID, "Matter Manipulator Frame MKIII", ""));
        ItemList.MatterManipulatorLens3
            .set(addItem(MatterManipulatorLens3.ID, "Matter Manipulator Lens Assembly MKIII", ""));
    }

    private void registerAllTieredTooltips() {
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorFrame0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorLens0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorFrame1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorLens1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorFrame2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorLens2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorFrame3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorLens3.get(1), ZPM);
    }

    private void addManipulatorRecipes() {
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // Power core MK0
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.StainlessSteel, 8),
                getModItem(IndustrialCraft2.ID, "itemBatCrystal", 1), // energy crystal
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 12),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(L * 4))
            .itemOutputs(ItemList.MatterManipulatorPowerCore0.get(1))
            .eut((int) TierEU.RECIPE_HV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Computer core MK0
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.StainlessSteel, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 2),
                getModItem(Mods.EnderIO.ID, "blockEndermanSkull", 1, 2), // tormented enderman skull
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 12),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(L * 8))
            .itemOutputs(ItemList.MatterManipulatorComputerCore0.get(1))
            .eut((int) TierEU.RECIPE_HV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.circuitAssemblerRecipes);

        // Teleporter core MK0
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                ItemList.Emitter_HV.get(2),
                getModItem(Mods.Thaumcraft.ID, "ItemResource", 1, 15), // primal charm
                ItemList.QuantumEye.get(2),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Thaumium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Thaumium, 16))
            .fluidInputs(FluidRegistry.getFluidStack("ender", 2000))
            .itemOutputs(ItemList.MatterManipulatorTeleporterCore0.get(1))
            .eut((int) TierEU.RECIPE_HV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Frame MK0
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 16))
            .itemOutputs(ItemList.MatterManipulatorFrame0.get(1))
            .eut((int) TierEU.RECIPE_HV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.benderRecipes);

        // Lens MK0
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.StainlessSteel, 4),
                getModItem(Mods.Thaumcraft.ID, "FocusTrade", 1), // equal trade focus
                ItemList.Field_Generator_MV.get(1),
                ItemList.Electric_Piston_HV.get(2),
                ItemList.Electric_Motor_HV.get(2))
            .fluidInputs(Materials.SolderingAlloy.getMolten(L * 4))
            .itemOutputs(ItemList.MatterManipulatorLens0.get(1))
            .eut((int) TierEU.RECIPE_HV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Manipulator MK0
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                ItemList.MatterManipulatorLens0.get(1),
                ItemList.MatterManipulatorTeleporterCore0.get(1),
                ItemList.MatterManipulatorComputerCore0.get(1),
                ItemList.MatterManipulatorPowerCore0.get(1),
                ItemList.MatterManipulatorFrame0.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(L * 8))
            .itemOutputs(ItemList.MatterManipulator0.get(1))
            .eut((int) TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Power core MK1
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.TungstenSteel, 8),
                ItemList.Energy_LapotronicOrb.get(1),
                ItemList.Circuit_Chip_PIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorIV, 6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 2),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 16))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 8)))
            .itemOutputs(ItemList.MatterManipulatorPowerCore1.get(1))
            .eut((int) TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Computer core MK1
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 12),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.TungstenSteel, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 16))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 16)))
            .itemOutputs(ItemList.MatterManipulatorComputerCore1.get(1))
            .eut((int) TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.circuitAssemblerRecipes);

        // Teleporter core MK1
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Enderium, 1),
                getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0), // teleporter
                ItemList.Emitter_IV.get(2),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Enderium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Enderium, 16))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 16)))
            .itemOutputs(ItemList.MatterManipulatorTeleporterCore1.get(1))
            .eut((int) TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Frame MK1
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 32))
            .itemOutputs(ItemList.MatterManipulatorFrame1.get(1))
            .eut((int) TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.benderRecipes);

        // Lens MK1
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.NetherStar, 2),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.StainlessSteel, 4),
                ItemList.Field_Generator_IV.get(1),
                ItemList.Electric_Piston_IV.get(2),
                ItemList.Electric_Motor_IV.get(2))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 4)))
            .itemOutputs(ItemList.MatterManipulatorLens1.get(1))
            .eut((int) TierEU.RECIPE_IV)
            .duration(20 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Manipulator MK1
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                ItemList.MatterManipulatorLens1.get(1),
                ItemList.MatterManipulatorTeleporterCore1.get(1),
                ItemList.MatterManipulatorComputerCore1.get(1),
                ItemList.MatterManipulatorPowerCore1.get(1),
                ItemList.MatterManipulatorFrame1.get(1))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 16)))
            .itemOutputs(ItemList.MatterManipulator1.get(1))
            .eut((int) TierEU.RECIPE_IV)
            .duration(30 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Power core MK2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Circuit_Chip_HPIC.get(2))
            .metadata(RESEARCH_TIME, 40 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.HSSS, 8),
                ItemList.Energy_LapotronicOrb2.get(1),
                ItemList.Circuit_Chip_HPIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorLuV, 6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.HSSS, 16))
            .fluidInputs(
                new FluidStack(solderIndalloy, (int) (L * 8)),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000))
            .itemOutputs(ItemList.MatterManipulatorPowerCore2.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Computer core MK2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1))
            .metadata(RESEARCH_TIME, 40 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.HSSS, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 18),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.HSSS, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.HSSS, 16))
            .fluidInputs(
                new FluidStack(solderIndalloy, (int) (L * 16)),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000))
            .itemOutputs(ItemList.MatterManipulatorComputerCore2.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Teleporter core MK2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Quantium, 1))
            .metadata(RESEARCH_TIME, 40 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Quantium, 1),
                ItemList.Emitter_LuV.get(2),
                ItemList.Field_Generator_LuV.get(1),
                ItemList.QuantumStar.get(4),
                getModItem(Mods.GraviSuite.ID, "itemSimpleItem", 4, 3), // gravitation engine
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Duranium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Duranium, 16))
            .fluidInputs(
                new FluidStack(solderIndalloy, (int) (L * 16)),
                Materials.Quantium.getMolten(L * 16),
                Materials.Duranium.getMolten(L * 8),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000))
            .itemOutputs(ItemList.MatterManipulatorTeleporterCore2.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Frame MK2
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.HSSS, 48))
            .itemOutputs(ItemList.MatterManipulatorFrame2.get(1))
            .eut((int) TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(RecipeMaps.benderRecipes);

        // Lens MK2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GGMaterial.orundum.get(OrePrefixes.lens, 1))
            .metadata(RESEARCH_TIME, 40 * MINUTES)
            .itemInputs(
                GGMaterial.orundum.get(OrePrefixes.lens, 2),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.HSSS, 4),
                ItemList.Field_Generator_LuV.get(1),
                ItemList.Electric_Motor_LuV.get(2),
                ItemList.Electric_Piston_LuV.get(2))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 4)))
            .itemOutputs(ItemList.MatterManipulatorLens2.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Manipulator MK2
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                ItemList.MatterManipulatorLens2.get(1),
                ItemList.MatterManipulatorTeleporterCore2.get(1),
                ItemList.MatterManipulatorComputerCore2.get(1),
                ItemList.MatterManipulatorPowerCore2.get(1),
                ItemList.MatterManipulatorFrame2.get(1))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 16)))
            .itemOutputs(ItemList.MatterManipulator2.get(1))
            .eut((int) TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);

        // Power core MK3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Circuit_Chip_UHPIC.get(2))
            .metadata(RESEARCH_TIME, 60 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.NaquadahAlloy, 8),
                ItemList.Energy_Module.get(1),
                ItemList.Circuit_Chip_UHPIC.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 12),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 16))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 16)), Materials.SuperCoolant.getFluid(32000))
            .itemOutputs(ItemList.MatterManipulatorPowerCore3.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Computer core MK3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1))
            .metadata(RESEARCH_TIME, 60 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 30),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.NaquadahAlloy, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 16))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 32)), Materials.SuperCoolant.getFluid(64000))
            .itemOutputs(ItemList.MatterManipulatorComputerCore3.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Teleporter core MK3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MysteriousCrystal, 1))
            .metadata(RESEARCH_TIME, 60 * MINUTES)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MysteriousCrystal, 1),
                ItemList.Emitter_ZPM.get(2),
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Gravistar.get(4),
                new ItemStack(ModItems.itemStandarParticleBase, 16, 0), // gravitons
                getModItem(Mods.GraviSuite.ID, "itemSimpleItem", 16, 3),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Tritanium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Tritanium, 16))
            .fluidInputs(
                new FluidStack(solderIndalloy, (int) (L * 16)),
                Materials.MysteriousCrystal.getMolten(L * 16),
                Materials.Tritanium.getMolten(L * 16),
                Materials.SuperCoolant.getFluid(32000))
            .itemOutputs(ItemList.MatterManipulatorTeleporterCore3.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Frame MK3
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.NaquadahAlloy, 64))
            .itemOutputs(ItemList.MatterManipulatorFrame3.get(1))
            .eut((int) TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(RecipeMaps.benderRecipes);

        // Lens MK3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 1))
            .metadata(RESEARCH_TIME, 60 * MINUTES)
            .itemInputs(
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 2),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 4),
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Electric_Piston_ZPM.get(2),
                ItemList.Electric_Motor_ZPM.get(2))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 16)))
            .itemOutputs(ItemList.MatterManipulatorLens3.get(1))
            .duration(1 * MINUTES)
            .eut((int) TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Manipulator MK3
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                ItemList.MatterManipulatorLens3.get(1),
                ItemList.MatterManipulatorTeleporterCore3.get(1),
                ItemList.MatterManipulatorComputerCore3.get(1),
                ItemList.MatterManipulatorPowerCore3.get(1),
                ItemList.MatterManipulatorFrame3.get(1))
            .fluidInputs(new FluidStack(solderIndalloy, (int) (L * 16)))
            .itemOutputs(ItemList.MatterManipulator3.get(1))
            .eut((int) TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(RecipeMaps.assemblerRecipes);
    }
}
