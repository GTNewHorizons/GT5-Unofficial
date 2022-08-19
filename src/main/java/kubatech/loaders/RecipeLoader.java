/*
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package kubatech.loaders;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import kubatech.Tags;
import kubatech.api.LoaderReference;
import kubatech.api.enums.ItemList;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeExterminationChamber;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecipeLoader {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[Recipe Loader]");
    protected static final long bitsd = GT_ModHandler.RecipeBits.NOT_REMOVABLE
            | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED
            | GT_ModHandler.RecipeBits.DISMANTLEABLE;

    private static int MTEID = 14201;
    private static final int MTEIDMax = 14300;

    public static void addRecipes() {
        if (LoaderReference.EnderIO) {
            ItemList.ExtremeExterminationChamber.set(new GT_MetaTileEntity_ExtremeExterminationChamber(
                            MTEID++, "multimachine.exterminationchamber", "Extreme Extermination Chamber")
                    .getStackForm(1L));
            GT_ModHandler.addCraftingRecipe(ItemList.ExtremeExterminationChamber.get(1), bitsd, new Object[] {
                "RCR",
                "CHC",
                "VVV",
                'R',
                gregtech.api.enums.ItemList.Robot_Arm_EV,
                'C',
                OrePrefixes.circuit.get(Materials.Data),
                'H',
                gregtech.api.enums.ItemList.Hull_EV,
                'V',
                GT_ModHandler.getModItem("OpenBlocks", "vacuumhopper", 1, new ItemStack(Blocks.hopper))
            });
        }
        if (MTEID > MTEIDMax + 1) throw new RuntimeException("MTE ID's");
    }

    private static boolean lateRecipesInitialized = false;

    public static void addRecipesLate() {
        // Runs on server start
        if (lateRecipesInitialized) return;
        lateRecipesInitialized = true;

        MobRecipeLoader.generateMobRecipeMap();
        MobRecipeLoader.processMobRecipeMap();
    }
}
