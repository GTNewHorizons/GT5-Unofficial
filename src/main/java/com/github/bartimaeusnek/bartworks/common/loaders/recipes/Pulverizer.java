package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class Pulverizer implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 1),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.Titanium.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(4));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 2),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.TungstenSteel.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(5));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 3),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9),
                        WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(6));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 4),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.Iridium.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(7));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 5),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.Osmium.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(8));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 13),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.Neutronium.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(9));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 14),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.CosmicNeutronium.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(10));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[0], 1, 15),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.Infinity.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(11));
        GT_Values.RA.addPulveriserRecipe(
                new ItemStack(ItemRegistry.bw_glasses[1], 1, 0),
                new ItemStack[] { Materials.BorosilicateGlass.getDust(9), Materials.TranscendentMetal.getDust(8) },
                null,
                800,
                BW_Util.getMachineVoltageFromTier(12));

        for (int i = 6; i < 11; i++) {
            GT_Values.RA.addPulveriserRecipe(
                    new ItemStack(ItemRegistry.bw_glasses[0], 1, i),
                    new ItemStack[] { Materials.BorosilicateGlass.getDust(9) },
                    null,
                    400,
                    BW_Util.getMachineVoltageFromTier(1));
        }
    }
}
