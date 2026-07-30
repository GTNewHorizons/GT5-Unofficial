package gtnhlanth.common.tileentity.recipe.beamline;

import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.SOURCE_CHAMBER_METADATA;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.TARGET_CHAMBER_METADATA;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.sourceChamberRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.targetChamberRecipes;
import static gtnhlanth.common.beamline.Particle.OMEGA;
import static gtnhlanth.common.beamline.Particle.PHOTON;

import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTUtility;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.item.MaskList;
import gtnhlanth.common.register.LanthItemList;

public class BeamlineRecipeLoader {

    public static final HashMap<String, Fluid> coolantMap = new HashMap<>();

    private static final ItemList[] VIABLE_WAFERS = new ItemList[] { ItemList.Circuit_Silicon_Wafer,
        ItemList.Circuit_Silicon_Wafer2, ItemList.Circuit_Silicon_Wafer3, ItemList.Circuit_Silicon_Wafer4,
        ItemList.Circuit_Silicon_Wafer5, ItemList.Circuit_Silicon_Wafer6, ItemList.Circuit_Silicon_Wafer7 };

    public static void load() {

        /*
         * Coolant list
         */

        coolantMap.put(
            MaterialLibAPI.getFluidStack(Materials.LiquidNitrogen, FluidShapes.fluidGas, 1)
                .getFluid()
                .getName(),
            MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 1)
                .getFluid());
        coolantMap.put(
            MaterialLibAPI.getFluidStack(Materials.LiquidOxygen, FluidShapes.fluidGas, 1)
                .getFluid()
                .getName(),
            MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1)
                .getFluid());
        coolantMap.put("ic2coolant", FluidRegistry.getFluid("ic2hotcoolant"));
        coolantMap.put(
            MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 1)
                .getFluid()
                .getName(),
            MaterialLibAPI.getFluidStack(Materials.HotSuperCoolant, FluidShapes.fluidLiquid, 1_000)
                .getFluid());

        /*
         * ELECTRON
         */
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tungsten, Shapes.stick, 1))
            .metadata(
                SOURCE_CHAMBER_METADATA,
                SourceChamberMetadata.builder()
                    .particleID(Particle.ELECTRON.ordinal())
                    .rate(20)
                    .energy(1000, 0.1f)
                    .focus(98)
                    .build())
            .duration(20)
            .eut(TierEU.RECIPE_IV)
            .addTo(sourceChamberRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LanthanumHexaboride, Shapes.stickLong, 1))
            .metadata(
                SOURCE_CHAMBER_METADATA,
                SourceChamberMetadata.builder()
                    .particleID(Particle.ELECTRON.ordinal())
                    .rate(60)
                    .energy(5000, 0.3f)
                    .focus(99)
                    .build())
            .duration(20)
            .eut(TierEU.RECIPE_IV)
            .addTo(sourceChamberRecipes);

        /*
         * NEUTRON
         */
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Californium, Shapes.dust, 1))
            .metadata(
                SOURCE_CHAMBER_METADATA,
                SourceChamberMetadata.builder()
                    .particleID(Particle.NEUTRON.ordinal())
                    .rate(10)
                    .energy(9000, 999)
                    .focus(95)
                    .build())
            .duration(20)
            .eut(TierEU.RECIPE_EV)
            .addTo(sourceChamberRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Beryllium, Shapes.stickLong, 1))
            .metadata(
                SOURCE_CHAMBER_METADATA,
                SourceChamberMetadata.builder()
                    .particleID(Particle.NEUTRON.ordinal())
                    .rate(10)
                    .energy(100_000, 999)
                    .focus(95)
                    .build())
            .duration(20)
            .eut(TierEU.RECIPE_LuV)
            .addTo(sourceChamberRecipes);

        /*
         * ALPHA
         */
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Thorium234, Shapes.dust, 1))
            .metadata(
                SOURCE_CHAMBER_METADATA,
                SourceChamberMetadata.builder()
                    .particleID(Particle.ALPHA.ordinal())
                    .rate(1)
                    .energy(4270, 999)
                    .focus(90)
                    .build())
            .duration(20)
            .eut(TierEU.RECIPE_HV)
            .addTo(sourceChamberRecipes);

        /*
         * PROTON
         */
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1000))
            .metadata(
                SOURCE_CHAMBER_METADATA,
                SourceChamberMetadata.builder()
                    .particleID(Particle.PROTON.ordinal())
                    .rate(40)
                    .energy(1_000_000, 0.3f)
                    .focus(99)
                    .build())
            .duration(20)
            .eut(TierEU.RECIPE_UV)
            .addTo(sourceChamberRecipes);

        /*
         * OMEGA
         */
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Unstable, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .outputChances(500)
            .metadata(
                SOURCE_CHAMBER_METADATA,
                SourceChamberMetadata.builder()
                    .particleID(OMEGA.ordinal())
                    .rate(60)
                    .energy(1_700_000, 0.3f)
                    .focus(99)
                    .build())
            .duration(20)
            .eut(TierEU.RECIPE_UHV)
            .addTo(sourceChamberRecipes);
        /*
         * TARGET CHAMBER
         */

        for (MaskList mask : MaskList.values()) {

            if (mask.getProducedItem() == null) // Blank or error
                continue;

            if (mask.getTCTargetItem() == null) { // Wafer TC recipe

                int index = 0;
                for (ItemList wafer : VIABLE_WAFERS) {

                    index++;

                    if (!Arrays.asList(mask.getForbiddenWafers())
                        .contains(wafer)) {

                        ItemStack focusItem = new ItemStack(LanthItemList.maskMap.get(mask), 0);

                        GTValues.RA.stdBuilder()
                            .itemInputs(focusItem, wafer.get(1))
                            .itemOutputs(
                                GTUtility
                                    .copyAmountUnsafe((int) GTUtility.powInt(2, index + 2), mask.getProducedItem()))
                            .metadata(
                                TARGET_CHAMBER_METADATA,
                                TargetChamberMetadata.builder(focusItem)
                                    .particleID(PHOTON.getId())
                                    // 2x recipe amount increase per 2 increases in wafer tier.
                                    // This greatly incentivises the use of higher tier boule wafer recipes
                                    .amount(
                                        (int) Math.round(
                                            mask.getBaselineAmount() * Math.sqrt(GTUtility.powInt(2, index - 1))))
                                    .energy(mask.getMinEnergy(), mask.getMaxEnergy(), 1)
                                    .minFocus(mask.getMinFocus())
                                    .build())
                            .duration(1)
                            .eut(TierEU.RECIPE_EV)
                            .addTo(targetChamberRecipes);
                    }
                }

                continue;

            }

            // Non-wafer recipes

            ItemStack focusItem = new ItemStack(LanthItemList.maskMap.get(mask), 0);
            GTValues.RA.stdBuilder()
                .itemInputs(focusItem, GTUtility.copyAmountUnsafe(1, mask.getTCTargetItem()))
                .itemOutputs(GTUtility.copyAmountUnsafe(4, mask.getProducedItem()))
                .metadata(
                    TARGET_CHAMBER_METADATA,
                    TargetChamberMetadata.builder(focusItem)
                        .particleID(PHOTON.getId())
                        .amount(mask.getBaselineAmount())
                        .energy(mask.getMinEnergy(), mask.getMaxEnergy(), 1)
                        .minFocus(mask.getMinFocus())
                        .build())
                .duration(1)
                .eut(TierEU.RECIPE_LuV)
                .addTo(targetChamberRecipes);
        }

        /* LuAG */

        // Raw Advanced Crystal Chip

        ItemStack focusItem = new ItemStack(LanthItemList.maskMap.get(MaskList.CSOC), 0);
        GTValues.RA.stdBuilder()
            .itemInputs(
                focusItem,
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gemExquisite, 1))
            .itemOutputs(GTUtility.copyAmountUnsafe(64, ItemList.Circuit_Chip_CrystalSoC.get(1)))
            .metadata(
                TARGET_CHAMBER_METADATA,
                TargetChamberMetadata.builder(focusItem)
                    .particleID(PHOTON.getId())
                    .amount(24)
                    .energy(5, 12, 1)
                    .minFocus(60)
                    .build())
            .duration(1)
            .eut(TierEU.RECIPE_LuV)
            .addTo(targetChamberRecipes);

        focusItem = new ItemStack(LanthItemList.maskMap.get(MaskList.ACC), 0);
        GTValues.RA.stdBuilder()
            .itemInputs(
                focusItem,
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gemExquisite, 1))
            .itemOutputs(GTUtility.copyAmountUnsafe(64, ItemList.Circuit_Chip_CrystalSoC2.get(1)))
            .metadata(
                TARGET_CHAMBER_METADATA,
                TargetChamberMetadata.builder(focusItem)
                    .particleID(PHOTON.getId())
                    .amount(36)
                    .energy(6, 14, 1)
                    .minFocus(70)
                    .build())
            .duration(1)
            .eut(TierEU.RECIPE_LuV)
            .addTo(targetChamberRecipes);

        // Lapotron chip

        focusItem = new ItemStack(LanthItemList.maskMap.get(MaskList.ACC), 0);
        GTValues.RA.stdBuilder()
            .itemInputs(
                focusItem,
                MaterialLibAPI.getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gemExquisite, 1))
            .itemOutputs(GTUtility.copyAmountUnsafe(64, ItemList.Circuit_Chip_CrystalSoC2.get(1)))
            .metadata(
                TARGET_CHAMBER_METADATA,
                TargetChamberMetadata.builder(focusItem)
                    .particleID(PHOTON.getId())
                    .amount(36)
                    .energy(6, 14, 1)
                    .minFocus(70)
                    .build())
            .duration(1)
            .eut(TierEU.RECIPE_LuV)
            .addTo(targetChamberRecipes);

    }
}
