package gtnhlanth.common.tileentity.recipe.beamline;

import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

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
            WerkstoffMaterialPool.HotSuperCoolant.getFluidOrGas(1000)
                .getFluid());

        /*
         * ELECTRON
         */
        BeamlineRecipeAdder2.instance.addSourceChamberRecipe(
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.stick, Materials.Tungsten, 1) },
            null,
            Particle.ELECTRON.ordinal(),
            20,
            1000,
            98,
            0.1f,
            7680);

        BeamlineRecipeAdder2.instance.addSourceChamberRecipe(
            new ItemStack[] { WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.stickLong, 1) },
            null,
            Particle.ELECTRON.ordinal(),
            60,
            5000,
            99,
            0.3f,
            7680);

        /*
         * NEUTRON
         */
        BeamlineRecipeAdder2.instance.addSourceChamberRecipe(
            new ItemStack[] { MaterialsElements.getInstance().CALIFORNIUM.getDust(1) },
            null,
            Particle.NEUTRON.ordinal(),
            10,
            9000,
            95,
            999,
            1920);

        /*
         * ALPHA
         */
        BeamlineRecipeAdder2.instance.addSourceChamberRecipe(
            new ItemStack[] { Materials.Uranium.getDust(1) },
            new ItemStack[] { WerkstoffMaterialPool.Thorium234.get(OrePrefixes.dust, 1) },
            Particle.ALPHA.ordinal(),
            1,
            4270,
            90,
            999,
            480);

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

                        BeamlineRecipeAdder2.instance.addTargetChamberRecipe(
                            wafer.get(1),
                            GTUtility.copyAmountUnsafe((int) Math.pow(2, index + 2), mask.getProducedItem()),
                            new ItemStack(LanthItemList.maskMap.get(mask), 0),
                            1,
                            (int) Math.round(mask.getBaselineAmount() * Math.pow(Math.sqrt(2), index - 1)), // 2x recipe
                                                                                                            // amount
                                                                                                            // increase
                            // per 2 increases in wafer
                            // tier. This greatly
                            // incentivises the use of
                            // higher tier boule wafer
                            // recipes
                            mask.getMinEnergy(),
                            mask.getMaxEnergy(),
                            mask.getMinFocus(),
                            1,
                            1920);

                    }

                }

                continue;

            }

            // Non-wafer recipes

            BeamlineRecipeAdder2.instance.addTargetChamberRecipe(
                GTUtility.copyAmountUnsafe(1, mask.getTCTargetItem()),
                GTUtility.copyAmountUnsafe(4, mask.getProducedItem()),
                new ItemStack(LanthItemList.maskMap.get(mask), 0),
                1,
                mask.getBaselineAmount(),
                mask.getMinEnergy(),
                mask.getMaxEnergy(),
                mask.getMinFocus(),
                1,
                (int) TierEU.RECIPE_LuV);

        }

        /* LuAG */

        // Raw Advanced Crystal Chip

        BeamlineRecipeAdder2.instance.addTargetChamberRecipe(
            WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
            GTUtility.copyAmountUnsafe(64, ItemList.Circuit_Chip_CrystalSoC.get(1)),
            new ItemStack(LanthItemList.maskMap.get(MaskList.CSOC), 0),
            1,
            24,
            5,
            12,
            60,
            1,
            (int) TierEU.RECIPE_LuV);
    }
}
