package goodgenerator.loader;

import static goodgenerator.util.ItemRefer.*;
import static goodgenerator.util.Log.LOGGER;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.Level;

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
            "Master", "Ultimate", "Superconductor", "Infinite", "Bio", "Optical", "Piko", "Quantum" };

    static final HashMap<String, Integer> NameToTier = new HashMap<>();

    static void run() {
        for (int i = 0; i < circuitTierMaterials.length; i++) NameToTier.put(circuitTierMaterials[i], i);
        // Cry about it
        NameToTier.put("Nano", 11);

        generateCasingRecipes();
        generateWrapRecipes();
        // Try and find the ZPM Fluid solidifier
        ItemStack solidifier;
        try {
            Class<?> c = Class.forName("com.dreammaster.gthandler.CustomItemList");
            Object maybeSolidifier = c.getMethod("valueOf", String.class).invoke(null, "FluidSolidifierZPM");
            solidifier = (ItemStack) (c.getMethod("get", long.class, Object[].class)
                    .invoke(maybeSolidifier, 16L, null));
            if (GT_Utility.isStackValid(solidifier)) LOGGER.log(Level.INFO, "ZPM Fluid Solidifier found.");
            else throw new NullPointerException();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "ZPM Fluid Solidifier not found, falling back to IV.", e);
            solidifier = ItemList.Machine_IV_FluidSolidifier.get(16);
        }

        // The controller itself
        GT_Values.RA.addAssemblylineRecipe(
                Compassline_Casing_EV.get(1),
                3600 * 20,
                new Object[] { ItemList.Machine_Multi_Assemblyline.get(16L), ItemList.Casing_Assembler.get(16L),
                        ItemList.Casing_Gearbox_TungstenSteel.get(32L), ComponentType.Robot_Arm.getComponent(8).get(16),
                        ComponentType.Conveyor_Module.getComponent(8).get(32),
                        ComponentType.Electric_Motor.getComponent(7).get(32),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 32), solidifier,
                        getALCircuit(8, 16), getALCircuit(7, 20), getALCircuit(6, 24) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("molten.indalloy140"), 144 * 12),
                        Materials.Naquadria.getMolten(144 * 16), Materials.Lubricant.getFluid(5000) },
                Component_Assembly_Line.get(1),
                30 * 20,
                getV(8) * 2);
    }

    /** Recipes for the Component Assembly Line Casings */
    private static void generateCasingRecipes() {
        int t = 1;
        // lv 1
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 4),
                        ComponentType.Robot_Arm.getComponent(t).get(4),
                        ComponentType.Electric_Piston.getComponent(t).get(8),
                        ComponentType.Electric_Motor.getComponent(t).get(10),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Steel, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tin, 6), getCircuit(t, 16), },
                Materials.SolderingAlloy.getMolten(144 * (t + 3)),
                Compassline_Casing_LV.get(1),
                16 * 20,
                getV(t));
        // mv 2
        t++;
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Aluminium, 4),
                        ComponentType.Robot_Arm.getComponent(t).get(4),
                        ComponentType.Electric_Piston.getComponent(t).get(8),
                        ComponentType.Electric_Motor.getComponent(t).get(10),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Aluminium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.AnyCopper, 6), getCircuit(t, 8),
                        getCircuit(t - 1, 16) },
                Materials.SolderingAlloy.getMolten(144 * (t + 1)),
                Compassline_Casing_MV.get(1),
                16 * 20,
                getV(t));
        // hv 3
        t++;
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StainlessSteel, 4),
                        ComponentType.Robot_Arm.getComponent(t).get(4),
                        ComponentType.Electric_Piston.getComponent(t).get(8),
                        ComponentType.Electric_Motor.getComponent(t).get(10),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.StainlessSteel, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Gold, 6), getCircuit(t, 8),
                        getCircuit(t - 1, 16) },
                Materials.SolderingAlloy.getMolten(144 * (t + 1)),
                Compassline_Casing_HV.get(1),
                16 * 20,
                getV(t));
        // ev 4
        t++;
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 4),
                        ComponentType.Robot_Arm.getComponent(t).get(4),
                        ComponentType.Electric_Piston.getComponent(t).get(8),
                        ComponentType.Electric_Motor.getComponent(t).get(10),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 6), getCircuit(t, 8),
                        getCircuit(t - 1, 16) },
                Materials.SolderingAlloy.getMolten(144 * (t + 1)),
                Compassline_Casing_EV.get(1),
                16 * 20,
                getV(t));
        // iv 5
        t++;
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 4),
                        ComponentType.Robot_Arm.getComponent(t).get(4),
                        ComponentType.Electric_Piston.getComponent(t).get(8),
                        ComponentType.Electric_Motor.getComponent(t).get(10),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.TungstenSteel, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 6), getCircuit(t, 8),
                        getCircuit(t - 1, 16) },
                Materials.SolderingAlloy.getMolten(144 * (t + 1)),
                Compassline_Casing_IV.get(1),
                16 * 20,
                getV(t));

        Fluid sold = FluidRegistry.getFluid("molten.indalloy140");
        // Assline Recipes!
        // luv 6
        t++;
        GT_Values.RA.addAssemblylineRecipe(
                Compassline_Casing_IV.get(1),
                2250 << t,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1),
                        WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plateDense, 6),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.gearGt, 4),
                        WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.gearGtSmall, 16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8),
                        getALCircuit(t, 8), getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                        StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_LuV.get(1),
                30 * 20,
                6000);
        // zpm 7
        t++;
        GT_Values.RA.addAssemblylineRecipe(
                Compassline_Casing_LuV.get(1),
                2250 << t,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 6),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Iridium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Naquadah, 8), getALCircuit(t, 8),
                        getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                        StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_ZPM.get(1),
                30 * 20,
                24000);
        // uv 8
        t++;
        GT_Values.RA.addAssemblylineRecipe(
                Compassline_Casing_ZPM.get(1),
                2250 << t,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmium, 6),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Osmium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8), getALCircuit(t, 8),
                        getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                        StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_UV.get(1),
                30 * 20,
                100000);
        // uhv 9
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                Compassline_Casing_UV.get(1),
                375 << (t - 2),
                1 << (t - 3),
                500000,
                1,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 6),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.CosmicNeutronium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8), getALCircuit(t, 8),
                        getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                        StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_UHV.get(1),
                50 * 20,
                500000);
        sold = FluidRegistry.getFluid("molten.mutatedlivingsolder");
        // uev 10
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                Compassline_Casing_UHV.get(1),
                375 << (t - 2),
                1 << (t - 3),
                2000000,
                1,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 6),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8), getALCircuit(t, 8),
                        getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                        StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_UEV.get(1),
                50 * 20,
                2000000);
        // uiv 11
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                Compassline_Casing_UEV.get(1),
                375 << (t - 2),
                1 << (t - 3),
                8000000,
                1,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 6),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.TranscendentMetal, 4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8), getALCircuit(t, 8),
                        getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t, 144 * t * 2),
                        StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_UIV.get(1),
                50 * 20,
                8000000);
        // umv 12
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                Compassline_Casing_UIV.get(1),
                375 << (t - 2),
                1 << (t - 3),
                32000000,
                1,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 6),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, 4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, 16),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 8), getALCircuit(t, 8),
                        getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4), CI.getTieredFluid(t - 1, 144 * t * 2),
                        StackUtils.getTieredFluid(t, 144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_UMV.get(1),
                50 * 20,
                32000000);
        // uxv 13
        t++;
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                Compassline_Casing_UMV.get(1),
                375 << (t - 2),
                1 << (t - 3),
                128_000_000,
                1,
                new Object[] { GT_OreDictUnificator
                        .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1),
                        GT_OreDictUnificator.get(
                                OrePrefixes.plateDense,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                3),
                        MyMaterial.shirabon.get(OrePrefixes.plateDense, 3),
                        ComponentType.Robot_Arm.getComponent(t).get(8),
                        ComponentType.Electric_Piston.getComponent(t).get(10),
                        ComponentType.Electric_Motor.getComponent(t).get(16),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGt,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                2),
                        MyMaterial.shirabon.get(OrePrefixes.gearGt, 2),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGtSmall,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8),
                        MyMaterial.shirabon.get(OrePrefixes.gearGtSmall, 8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 4),
                        getALCircuit(t, 8), getALCircuit(t - 1, 16) },
                new FluidStack[] { new FluidStack(sold, 144 * t * 4),
                        MaterialsUEVplus.BlackDwarfMatter.getMolten(144 * t * 2),
                        MaterialsUEVplus.Eternity.getMolten(144 * t), Materials.Lubricant.getFluid(1000 * (t - 2)) },
                Compassline_Casing_UXV.get(1),
                50 * 20,
                (int) TierEU.RECIPE_UMV);
    }

    private static int getV(int tier) {
        return (int) (GT_Values.V[tier] - (GT_Values.V[tier] >> 4));
    }

    private static void generateWrapRecipes() {
        for (int i = 0; i <= 11; i++) {
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { getCircuit(i, 16), GT_Utility.getIntegratedCircuit(16) },
                    Materials.SolderingAlloy.getMolten(72L),
                    new ItemStack(Loaders.circuitWrap, 1, i),
                    30 * 20,
                    30);
        }
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GameRegistry.findItemStack("dreamcraft", "item.PikoCircuit", 16),
                        GT_Utility.getIntegratedCircuit(16) },
                Materials.SolderingAlloy.getMolten(72L),
                new ItemStack(Loaders.circuitWrap, 1, 12),
                30 * 20,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GameRegistry.findItemStack("dreamcraft", "item.QuantumCircuit", 16),
                        GT_Utility.getIntegratedCircuit(16) },
                Materials.SolderingAlloy.getMolten(72L),
                new ItemStack(Loaders.circuitWrap, 1, 13),
                30 * 20,
                30);
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
