package goodgenerator.loader;

import static goodgenerator.util.ItemRefer.Compassline_Casing_EV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_HV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_IV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_LV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_LuV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_MV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UEV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UHV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UIV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_ZPM;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.StackUtils;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.util.recipe.Scanning;
import tectech.recipe.TTRecipeAdder;

public class ComponentAssemblyLineMiscRecipes {

    public static final String[] circuitTierMaterials = { "Primitive", "Basic", "Good", "Advanced", "Data", "Elite",
        "Master", "Ultimate", "Superconductor", "Infinite", "Bio", "Optical", "Exotic", "Cosmic", "Transcendent" };

    static final HashMap<String, Integer> NameToTier = new HashMap<>();

    static void run() {
        for (int i = 0; i < circuitTierMaterials.length; i++) NameToTier.put(circuitTierMaterials[i], i);
        // Cry about it
        NameToTier.put("Nano", 11);

        generateCasingRecipes();
        generateWrapRecipes();

        // The controller itself
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_EV.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.Machine_Multi_Assemblyline.get(16L),
                ItemList.Casing_Assembler.get(16L),
                ItemList.Casing_Gearbox_TungstenSteel.get(32L),
                ComponentType.Robot_Arm.getComponent(8)
                    .get(16),
                ComponentType.Conveyor_Module.getComponent(8)
                    .get(32),
                ComponentType.Electric_Motor.getComponent(7)
                    .get(32),
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, TEBlockShapes.pipeMedium, 16),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.plateSuperdense, 4),
                ItemList.FluidSolidifierZPM.get(16L),
                getALCircuit(8, 16),
                getALCircuit(7, 20),
                getALCircuit(6, 24))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Indalloy140, FluidShapes.fluidMolten, 12 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 5_000))
            .itemOutputs(Component_Assembly_Line.get(1))
            .eut(TierEU.RECIPE_UHV / 2)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    /** Recipes for the Component Assembly Line Casings */
    private static void generateCasingRecipes() {
        int t = 1;
        // lv 1
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Steel, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Tin, TEBlockShapes.wireGt04, 6),
                getCircuit(t, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_LV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // mv 2
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Aluminium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Copper, TEBlockShapes.wireGt04, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_MV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // hv 3
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.StainlessSteel, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Gold, TEBlockShapes.wireGt04, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_HV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // ev 4
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Titanium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Aluminium, TEBlockShapes.wireGt04, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_EV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // iv 5
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TungstenSteel, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Tungsten, TEBlockShapes.wireGt04, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_IV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // Assline Recipes!
        // luv 6
        t++;
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_IV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Europium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.plateDense, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.gearGtSmall, 16),
                MaterialLibAPI.getStack(Materials.VanadiumGallium, TEBlockShapes.cableGt04, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Indalloy140, FluidShapes.fluidMolten, t * 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Zeron100, FluidShapes.fluidMolten, t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_LuV.get(1))
            .eut(TierEU.RECIPE_IV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // zpm 7
        t++;
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_LuV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iridium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.gearGtSmall, 16),
                MaterialLibAPI.getStack(Materials.Naquadah, TEBlockShapes.cableGt04, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Indalloy140, FluidShapes.fluidMolten, t * 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Pikyonium64B, FluidShapes.fluidMolten, t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_ZPM.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // uv 8
        t++;

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_ZPM.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Osmium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.gearGtSmall, 16),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, TEBlockShapes.cableGt04, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Indalloy140, FluidShapes.fluidMolten, t * 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.AdvancedNitinol, FluidShapes.fluidMolten, t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_UV.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // uhv 9
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UV,
            1,
            new Object[] { MaterialLibAPI.getStack(Materials.CosmicNeutronium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.gearGtSmall, 16),
                MaterialLibAPI.getStack(Materials.Bedrockium, TEBlockShapes.cableGt04, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Indalloy140, FluidShapes.fluidMolten, t * 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.AbyssalAlloy, FluidShapes.fluidMolten, t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 1000 * (t - 2)) },
            Compassline_Casing_UHV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UV);
        // uev 10
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UHV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UHV,
            1,
            new Object[] { MaterialLibAPI.getStack(Materials.Infinity, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.gearGtSmall, 16),
                MaterialLibAPI.getStack(Materials.Draconium, TEBlockShapes.cableGt04, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.MutatedLivingSolder, FluidShapes.fluidLiquid, t * 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Quantum, FluidShapes.fluidMolten, t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 1000 * (t - 2)) },
            Compassline_Casing_UEV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UHV);
        // uiv 11
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UEV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UEV,
            1,
            new Object[] { MaterialLibAPI.getStack(Materials.protohalkonite, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.protohalkonite, Shapes.gearGtSmall, 16),
                MaterialLibAPI.getStack(Materials.NetherStar, TEBlockShapes.cableGt04, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.MutatedLivingSolder, FluidShapes.fluidLiquid, t * 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Hypogen, FluidShapes.fluidMolten, t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 1000 * (t - 2)) },
            Compassline_Casing_UIV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UEV);
    }

    private static void generateWrapRecipes() {
        for (int i = 0; i <= 14; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(getCircuit(i, 16))
                .circuit(16)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, 1 * HALF_INGOTS))
                .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, i))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }
    }

    @SuppressWarnings("unused")
    private enum ComponentType {

        Electric_Motor,
        Electric_Piston,
        Robot_Arm,
        Electric_Pump,
        Field_Generator,
        Conveyor_Module,
        Emitter,
        Sensor;

        public ItemList getComponent(int tier) {
            if (tier < 0 || tier > GTValues.VN.length) throw new IllegalArgumentException("Tier is out of range!");
            return ItemList.valueOf(this.name() + "_" + GTValues.VN[tier]);
        }
    }

    private static ItemStack getCircuit(int tier, long amount) {
        return Circuits.values()[tier].get((int) amount);
    }

    private static Object[] getALCircuit(int tier, int amount) {
        return new Object[] { Circuits.values()[tier].getIngredient(), amount };
    }
}
