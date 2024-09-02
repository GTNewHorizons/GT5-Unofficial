package gtnhlanth.common.tileentity.recipe.beamline;

import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
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
            Materials.Water.getFluid(1L)
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
                        mask.getBaselineAmount() * (int) Math.pow(Math.sqrt(3), index - 1), // 3x recipe amount increase
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

            /*
             * if (!Arrays.asList(MaskList.CPU.getForbiddenWafers()).contains(wafer)) {
             * BeamlineRecipeAdder.instance.addTargetChamberRecipe( wafer.get(1), GT_Utility.copyAmountUnsafe((int)
             * Math.pow(2, index + 2), ItemList.Circuit_Wafer_CPU.get(1)), //Varies new
             * ItemStack(LanthItemList.maskMap.get(MaskList.CPU), 0), // Varies 0, 10 * (int) Math.pow(2, index - 1), //
             * Varies 1, //Varies 10000000, //Varies 50, //Varies 1, 1920 ); } /* PPIC
             */

            /*
             * if (!Arrays.asList(MaskList.PPIC.getForbiddenWafers()).contains(wafer)) {
             * GTLog.out.print("Adding recipe for PPIC with " + wafer.get(1).getUnlocalizedName() + " amount: " + 40 *
             * (int) Math.pow(2, index - 1)); BeamlineRecipeAdder.instance.addTargetChamberRecipe( wafer.get(1),
             * ItemList.Circuit_Wafer_PPIC.get((int) Math.pow(2, index + 2)), //Varies new
             * ItemStack(LanthItemList.maskMap.get(MaskList.PPIC), 0), // Varies 0, 40 * (int) Math.pow(2, index - 1),
             * // Varies 1, //Varies 10000000, //Varies 50, //Varies 1, 1920 ); }
             */

        }
        /*
         * BeamlineRecipeAdder2.instance.addTargetChamberRecipe( new ItemStack(Items.coal, 1), new
         * ItemStack(Items.diamond, 1), null, 1, 20, 100, 1000, 60, 1, 1920);
         * BeamlineRecipeAdder2.instance.addTargetChamberRecipe( new ItemStack(Items.coal, 1), new
         * ItemStack(Items.cooked_chicken, 1), null, 1, 20, 1, 10, 60, 1, 1920);
         */

        BeamlineRecipeAdder2.instance.addTargetChamberRecipe(
            new ItemStack(Items.chicken, 1),
            new ItemStack(Items.cooked_chicken),
            null,
            Particle.PHOTON.ordinal(),
            400,
            5,
            20,
            80,
            1,
            7864320);

        BeamlineRecipeAdder2.instance.addTargetChamberRecipe(
            new ItemStack(Items.chicken, 1),
            new ItemStack(Items.egg),
            null,
            Particle.PHOTON.ordinal(),
            400,
            21,
            600,
            80,
            1,
            7864320);

    }
}
