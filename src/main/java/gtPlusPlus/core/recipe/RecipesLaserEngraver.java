package gtPlusPlus.core.recipe;

import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import toxiceverglades.dimension.DimensionEverglades;

public class RecipesLaserEngraver implements IOreRecipeRegistrator {

    public RecipesLaserEngraver() {
        OrePrefixes.crafting.add(this);
    }

    @Override
    public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName,
        final String aModName, final ItemStack aStack) {
        if (aOreDictName.equals(OreDictNames.craftingLensWhite.toString())) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Lithium, 2L),
                    GTUtility.copyAmount(0L, aStack))
                .itemOutputs(MaterialsElements.getInstance().LITHIUM7.getPlateDouble(1))
                .duration(4 * MINUTES)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 3L),
                    GTUtility.copyAmount(0L, aStack))
                .itemOutputs(MaterialsElements.getInstance().LITHIUM7.getDust(1))
                .duration(2 * MINUTES)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);

        } else if (aOreDictName.equals(OreDictNames.craftingLensLime.toString())) {
            // Coil Wires
            ItemStack coilWire1 = GregtechItemList.DehydratorCoilWireEV.get(1);
            ItemStack coilWire2 = GregtechItemList.DehydratorCoilWireIV.get(1);
            ItemStack coilWire3 = GregtechItemList.DehydratorCoilWireLuV.get(1);
            ItemStack coilWire4 = GregtechItemList.DehydratorCoilWireZPM.get(1);

            // Wires to Laser
            ItemStack wireT1a = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Aluminium, 1);
            ItemStack wireT1b = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Nichrome, 1);
            ItemStack wireT2a = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Osmium, 1);
            ItemStack wireT2b = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Platinum, 1);
            ItemStack wireT3a = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 1);
            ItemStack wireT3b = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.YttriumBariumCuprate, 1);
            ItemStack wireT3c = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 1);
            ItemStack wireT4a = GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 1);

            // T1
            GTValues.RA.stdBuilder()
                .itemInputs(wireT1a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire1)
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT1b, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire1)
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(laserEngraverRecipes);
            // T2
            GTValues.RA.stdBuilder()
                .itemInputs(wireT2a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire2)
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT2b, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire2)
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(laserEngraverRecipes);
            // T3
            GTValues.RA.stdBuilder()
                .itemInputs(wireT3a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire3)
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT3b, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire3)
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
            GTValues.RA.stdBuilder()
                .itemInputs(wireT3c, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire3)
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
            // T4
            GTValues.RA.stdBuilder()
                .itemInputs(wireT4a, GTUtility.copyAmount(0L, aStack))
                .itemOutputs(coilWire4)
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(laserEngraverRecipes);

        } else if (aOreDictName.equals(OreDictNames.craftingLensOrange.toString())) {
            GTValues.RA.stdBuilder()
                .itemInputs(GregtechItemList.AlkalusDisk.get(1), GTUtility.copyAmount(0L, aStack))
                .itemOutputs(new ItemStack(DimensionEverglades.portalItem))
                .duration(3 * HOURS)
                .eut(TierEU.RECIPE_IV)
                .addTo(laserEngraverRecipes);
        }
    }
}
