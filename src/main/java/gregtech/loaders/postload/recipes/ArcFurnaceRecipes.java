package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.MOD_ID_RC;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;

public class ArcFurnaceRecipes implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Block_TungstenSteelReinforced.get(1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 2),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Concrete, 1) },
                null,
                160,
                96);

        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Long_Distance_Pipeline_Fluid.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 19L) },
                null,
                180,
                (int) TierEU.RECIPE_MV);

        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Long_Distance_Pipeline_Item.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 7L) },
                null,
                180,
                (int) TierEU.RECIPE_MV);

        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Long_Distance_Pipeline_Fluid_Pipe.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 2L) },
                null,
                10,
                (int) TierEU.RECIPE_MV);

        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Long_Distance_Pipeline_Item_Pipe.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Tin, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 1L) },
                null,
                10,
                (int) TierEU.RECIPE_MV);

        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_Cupronickel.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Cupronickel, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 2) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_Kanthal.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Kanthal, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Cupronickel, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_Nichrome.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Nichrome, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Kanthal, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 4) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_TungstenSteel.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TPV, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Nichrome, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 5) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_HSSG.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSG, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TPV, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 6) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_HSSS.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSS, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSG, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 7) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_Naquadah.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.HSSS, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 8) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_NaquadahAlloy.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahAlloy, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 9) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_Trinium.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Trinium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahAlloy, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 10) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_ElectrumFlux.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ElectrumFlux, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Trinium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 11) },
                null,
                300,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                ItemList.Casing_Coil_AwakenedDraconium.get(1L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.DraconiumAwakened, 8),
                        GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.ElectrumFlux, 1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 12) },
                null,
                300,
                360);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 0),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 2L) },
                null,
                150,
                90);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                150,
                90);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 2),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iron, 6) },
                null,
                150,
                90);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 13),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 2L) },
                null,
                180,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 14),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                180,
                (int) TierEU.RECIPE_MV);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.beta", 1L, 15),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Steel, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Steel, 6L) },
                null,
                180,
                (int) TierEU.RECIPE_MV);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 0),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Aluminium, 2L) },
                null,
                210,
                150);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 3) },
                null,
                210,
                150);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 2),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Aluminium, 6L) },
                null,
                210,
                150);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 3),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 2L) },
                null,
                240,
                180);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 4),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                240,
                180);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 5),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.StainlessSteel, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.StainlessSteel, 6L) },
                null,
                240,
                180);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 6),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 2L) },
                null,
                270,
                210);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 7),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                270,
                210);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 8),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Titanium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Titanium, 6L) },
                null,
                270,
                210);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 9),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 2L) },
                null,
                300,
                240);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 10),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                300,
                240);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 11),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.TungstenSteel, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.TungstenSteel, 6L) },
                null,
                300,
                240);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 12),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Palladium, 2L) },
                null,
                330,
                270);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 13),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Palladium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                330,
                270);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.zeta", 1L, 14),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NiobiumTitanium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Chrome, 6L) },
                null,
                330,
                270);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 0),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 2L) },
                null,
                360,
                300);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Iridium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                360,
                300);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 2),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Enderium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Iridium, 6L) },
                null,
                360,
                300);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 3),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 2L) },
                null,
                360,
                330);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 4),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Osmium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                360,
                330);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 5),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Osmium, 6L) },
                null,
                360,
                330);

        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 6),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 2L) },
                null,
                360,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 7),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 3) },
                null,
                360,
                360);
        GT_Values.RA.addArcFurnaceRecipe(
                getModItem(MOD_ID_RC, "machine.eta", 1L, 8),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Neutronium, 12L),
                        GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Neutronium, 6L) },
                null,
                360,
                360);

        GT_Values.RA.addSimpleArcFurnaceRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                Materials.Oxygen.getGas(2000L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3) },
                null,
                1200,
                (int) TierEU.RECIPE_LV);
    }
}
