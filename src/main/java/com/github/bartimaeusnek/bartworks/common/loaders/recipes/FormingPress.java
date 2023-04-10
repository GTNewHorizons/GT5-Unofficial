package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.MATERIALS_PER_FUEL;
import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR.HTGRMaterials.sHTGR_Fuel;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_HTGR;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_THTR;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

public class FormingPress implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addFormingPressRecipe(
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials),
                Materials.Graphite.getDust(64),
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 1),
                40,
                30);
        GT_Values.RA.addFormingPressRecipe(
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 1),
                Materials.Silicon.getDust(64),
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 2),
                40,
                30);
        GT_Values.RA.addFormingPressRecipe(
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 2),
                Materials.Graphite.getDust(64),
                new ItemStack(GT_TileEntity_THTR.THTRMaterials.aTHTR_Materials, 1, 3),
                40,
                30);

        int i = 0;
        for (GT_TileEntity_HTGR.HTGRMaterials.Fuel_ fuel : sHTGR_Fuel) {
            GT_Values.RA.addFormingPressRecipe(
                    new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i),
                    Materials.Carbon.getDust(64),
                    new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 1),
                    40,
                    30);
            GT_Values.RA.addFormingPressRecipe(
                    new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 1),
                    Materials.Silicon.getDust(64),
                    new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 2),
                    40,
                    30);
            GT_Values.RA.addFormingPressRecipe(
                    new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 2),
                    Materials.Graphite.getDust(64),
                    new ItemStack(GT_TileEntity_HTGR.HTGRMaterials.aHTGR_Materials, 1, i + 3),
                    40,
                    30);
            i += MATERIALS_PER_FUEL;
        }
    }
}
