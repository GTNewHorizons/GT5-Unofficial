package gtnhlanth.common.tileentity.recipe.beamline;

import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.SOURCE_CHAMBER_METADATA;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.TARGET_CHAMBER_METADATA;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.sourceChamberRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.targetChamberRecipes;

import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.item.MaskList;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;

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
            Materials.LiquidNitrogen.getGas(1L)
                .getFluid()
                .getName(),
            Materials.Nitrogen.getGas(1L)
                .getFluid());
        coolantMap.put(
            Materials.LiquidOxygen.getGas(1L)
                .getFluid()
                .getName(),
            Materials.Oxygen.getGas(1L)
                .getFluid());
        coolantMap.put("ic2coolant", FluidRegistry.getFluid("ic2hotcoolant"));
        coolantMap.put(
            Materials.SuperCoolant.getFluid(1L)
                .getFluid()
                .getName(),
            WerkstoffMaterialPool.HotSuperCoolant.getFluidOrGas(1_000)
                .getFluid());

        /*
         * ELECTRON
         */
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Tungsten, 1))
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
            .itemInputs(WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.stickLong, 1))
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
            .itemInputs(MaterialsElements.getInstance().CALIFORNIUM.getDust(1))
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

        /*
         * ALPHA
         */
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Uranium.getDust(1))
            .itemOutputs(WerkstoffMaterialPool.Thorium234.get(OrePrefixes.dust, 1))
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
                                    .particleID(1)
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
                        .particleID(1)
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
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .itemOutputs(GTUtility.copyAmountUnsafe(64, ItemList.Circuit_Chip_CrystalSoC.get(1)))
            .metadata(
                TARGET_CHAMBER_METADATA,
                TargetChamberMetadata.builder(focusItem)
                    .particleID(1)
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
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .itemOutputs(GTUtility.copyAmountUnsafe(64, ItemList.Circuit_Chip_CrystalSoC2.get(1)))
            .metadata(
                TARGET_CHAMBER_METADATA,
                TargetChamberMetadata.builder(focusItem)
                    .particleID(1)
                    .amount(36)
                    .energy(6, 14, 1)
                    .minFocus(70)
                    .build())
            .duration(1)
            .eut(TierEU.RECIPE_LuV)
            .addTo(targetChamberRecipes);
    }
}
