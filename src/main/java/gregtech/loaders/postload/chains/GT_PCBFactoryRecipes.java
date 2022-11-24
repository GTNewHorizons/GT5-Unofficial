package gregtech.loaders.postload.chains;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_PCBFactoryRecipes {

    public static void load() {
        final int mBioUpgradeBitMap = 0b1000;
        final int mTier3BitMap = 0b100;
        final int mTier2BitMap = 0b10;
        final int mTier1BitMap = 0b1;

        GT_Values.RA.addPCBFactoryRecipe(
                new ItemStack[] {
                    Materials.Plastic.getPlates(1), GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Copper, 16)
                },
                new FluidStack[] {Materials.SulfuricAcid.getFluid(1000), Materials.IronIIIChloride.getFluid(1000)},
                new ItemStack[] {ItemList.Circuit_Board_Plastic_Advanced.get(8)},
                30 * 20,
                30,
                mTier1BitMap);
    }
}
