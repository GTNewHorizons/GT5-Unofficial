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
import static goodgenerator.util.ItemRefer.Compassline_Casing_UMV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UXV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_ZPM;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.technus.tectech.recipe.TT_recipeAdder;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.MyMaterial;
import goodgenerator.util.StackUtils;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.recipe.common.CI;

public class ComponentAssemblyLineMiscRecipes {

    public static final String[] circuitTierMaterials = { "Primitive", "Basic", "Good", "Advanced", "Data", "Elite",
        "Master", "Ultimate", "Superconductor", "Infinite", "Bio", "Optical", "Exotic", "Cosmic" };

    static final HashMap<String, Integer> NameToTier = new HashMap<>();

    static void run() {
        for (int i = 0; i < circuitTierMaterials.length; i++) NameToTier.put(circuitTierMaterials[i], i);
        // Cry about it
        NameToTier.put("Nano", 11);

        generateCasingRecipes();
        generateWrapRecipes();

        // The controller itself
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_EV.get(1))
            .metadata(RESEARCH_TIME, 1 * HOURS)
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
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 16),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 32),
                ItemList.FluidSolidifierZPM.get(16L),
                getALCircuit(8, 16),
                getALCircuit(7, 20),
                getALCircuit(6, 24))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("molten.indalloy140"), 144 * 12),
                Materials.Naquadria.getMolten(144 * 16),
                Materials.Lubricant.getFluid(5000))
            .itemOutputs(Component_Assembly_Line.get(1))
            .eut(TierEU.RECIPE_UHV / 2)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    /** Recipes for the Component Assembly Line Casings */
    private static void generateCasingRecipes() {
        int t = 1;
        // lv 1
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Steel, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tin, 6),
                getCircuit(t, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * (t + 1)))
            .itemOutputs(Compassline_Casing_LV.get(1))
            .duration(16 * SECONDS)
            .eut(GT_Values.VP[t])
            .addTo(assemblerRecipes);
        // mv 2
        t++;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Aluminium, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Aluminium, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.AnyCopper, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * (t + 1)))
            .itemOutputs(Compassline_Casing_MV.get(1))
            .duration(16 * SECONDS)
            .eut(GT_Values.VP[t])
            .addTo(assemblerRecipes);
        // hv 3
        t++;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StainlessSteel, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.StainlessSteel, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Gold, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * (t + 1)))
            .itemOutputs(Compassline_Casing_HV.get(1))
            .duration(16 * SECONDS)
            .eut(GT_Values.VP[t])
            .addTo(assemblerRecipes);
        // ev 4
        t++;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * (t + 1)))
            .itemOutputs(Compassline_Casing_EV.get(1))
            .duration(16 * SECONDS)
            .eut(GT_Values.VP[t])
            .addTo(assemblerRecipes);
        // iv 5
        t++;
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.TungstenSteel, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * (t + 1)))
            .itemOutputs(Compassline_Casing_IV.get(1))
            .duration(16 * SECONDS)
            .eut(GT_Values.VP[t])
            .addTo(assemblerRecipes);

        Fluid sold = FluidRegistry.getFluid("molten.indalloy140");
        // Assline Recipes!
        // luv 6
        t++;
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_IV.get(1))
            .metadata(RESEARCH_TIME, (2250 << t) * TICKS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plateDense, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.gearGt, 4),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.gearGtSmall, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                new FluidStack(sold, 144 * t * 4),
                CI.getTieredFluid(t, 144 * t * 2),
                StackUtils.getTieredFluid(t, 144 * t),
                Materials.Lubricant.getFluid(1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_LuV.get(1))
            .eut(TierEU.RECIPE_IV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // zpm 7
        t++;
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_LuV.get(1))
            .metadata(RESEARCH_TIME, (2250 << t) * TICKS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Naquadah, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                new FluidStack(sold, 144 * t * 4),
                CI.getTieredFluid(t, 144 * t * 2),
                StackUtils.getTieredFluid(t, 144 * t),
                Materials.Lubricant.getFluid(1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_ZPM.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // uv 8
        t++;

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_ZPM.get(1))
            .metadata(RESEARCH_TIME, (2250 << t) * TICKS)
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmium, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Osmium, 4),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                new FluidStack(sold, 144 * t * 4),
                CI.getTieredFluid(t, 144 * t * 2),
                StackUtils.getTieredFluid(t, 144 * t),
                Materials.Lubricant.getFluid(1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_UV.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // uhv 9
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UV,
            1,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.CosmicNeutronium, 4),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UHV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UV);
        sold = FluidRegistry.getFluid("molten.mutatedlivingsolder");
        // uev 10
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UHV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UHV,
            1,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UEV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UHV);
        // uiv 11
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UEV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UEV,
            1,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.TranscendentMetal, 4),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UIV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UEV);
        // umv 12
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UIV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UIV,
            1,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, 4),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, 16),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t - 1, 144 * t * 2),
                StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UMV.get(1),
            50 * 20,
            (int) TierEU.RECIPE_UIV);
        // uxv 13
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UMV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UMV,
            1,
            new Object[] {
                GT_OreDictUnificator
                    .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1),
                GT_OreDictUnificator
                    .get(OrePrefixes.plateDense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 3),
                MyMaterial.shirabon.get(OrePrefixes.plateDense, 3), ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GT_OreDictUnificator
                    .get(OrePrefixes.gearGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                MyMaterial.shirabon.get(OrePrefixes.gearGt, 2),
                GT_OreDictUnificator
                    .get(OrePrefixes.gearGtSmall, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8),
                MyMaterial.shirabon.get(OrePrefixes.gearGtSmall, 8),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 4), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { new FluidStack(sold, 144 * t * 4),
                MaterialsUEVplus.BlackDwarfMatter.getMolten(144 * t * 2), MaterialsUEVplus.Eternity.getMolten(144 * t),
                Materials.Lubricant.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UXV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UMV);
    }

    private static void generateWrapRecipes() {
        for (int i = 0; i <= 11; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(getCircuit(i, 16), GT_Utility.getIntegratedCircuit(16))
                .fluidInputs(Materials.SolderingAlloy.getMolten(72L))
                .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, i))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GameRegistry.findItemStack("dreamcraft", "item.PikoCircuit", 16),
                GT_Utility.getIntegratedCircuit(16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72L))
            .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, 12))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GameRegistry.findItemStack("dreamcraft", "item.QuantumCircuit", 16),
                GT_Utility.getIntegratedCircuit(16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72L))
            .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, 13))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
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
            if (tier < 0 || tier > GT_Values.VN.length) throw new IllegalArgumentException("Tier is out of range!");
            return ItemList.valueOf(this.name() + "_" + GT_Values.VN[tier]);
        }
    }

    private static ItemStack getCircuit(int tier, long amount) {
        return GT_OreDictUnificator.get(OrePrefixes.circuit, getCircuitMaterial(tier), amount);
    }

    private static Object[] getALCircuit(int tier, int amount) {
        return new Object[] { OrePrefixes.circuit.get(getCircuitMaterial(tier)), amount };
    }

    @Nullable
    public static Materials getCircuitMaterial(int tier) {
        return Materials.getRealMaterial(circuitTierMaterials[tier]);
    }
}
