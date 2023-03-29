package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.enums.ModIDs.ThaumicTinkerer;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;

public class FuelRecipes implements Runnable {

    @Override
    public void run() {
        if (!GregTech_API.mIC2Classic) {
            GT_Values.RA.addFuel(GT_ModHandler.getIC2Item("biogasCell", 1L), null, 40, 1);
        }

        GT_Values.RA.addFuel(new ItemStack(Items.golden_apple, 1, 1), new ItemStack(Items.apple, 1), 6400, 5);
        GT_Values.RA.addFuel(getModItem(Thaumcraft.modID, "ItemShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "GluttonyShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "FMResource", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "NetherShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 1), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 2), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 3), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 4), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 5), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(TaintedMagic.modID, "WarpedShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(TaintedMagic.modID, "FluxShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(TaintedMagic.modID, "EldritchShard", 1L), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ThaumicTinkerer.modID, "kamiResource", 1L, 6), null, 720, 5);
        GT_Values.RA.addFuel(getModItem(ThaumicTinkerer.modID, "kamiResource", 1L, 7), null, 720, 5);
    }
}
