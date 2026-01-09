package gtPlusPlus.xmod.forestry.bees.custom;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotOld;
import gtPlusPlus.core.item.base.misc.BaseItemMisc;
import gtPlusPlus.core.item.base.misc.BaseItemMisc.MiscTypes;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class GTPPBees {

    // Custom Comb Drop Base Items
    public static Item dropForceGem;
    public static Item dropBiomassBlob;
    public static Item dropEthanolBlob;
    public static Item dropNikoliteDust;
    public static Item dropFluorineBlob;

    // Base Comb Item
    public static ItemCustomComb combs;

    public static ItemStack Comb_Slag;
    public static ItemStack Comb_Stone;

    public static Materials PTFE;
    public static Materials PBS;

    // public static GTPP_Branch_Definition definition;

    public GTPPBees() {
        if (Forestry.isModLoaded()) {

            setMaterials();
            setCustomItems();

            try {
                combs = new ItemCustomComb();
                combs.initCombsRecipes();
                GTPPBeeDefinition.initBees();
            } catch (Exception t) {
                Logger.BEES("Failed to load bees, probably due to an ancient forestry version");
                t.printStackTrace();
            }
        }
    }

    private void setCustomItems() {
        dropForceGem = new BaseItemMisc("Force", new short[] { 250, 250, 20 }, 64, MiscTypes.GEM, null);
        dropBiomassBlob = new BaseItemMisc("Biomass", new short[] { 33, 225, 24 }, 64, MiscTypes.DROP, null);
        dropEthanolBlob = new BaseItemMisc("Ethanol", new short[] { 255, 128, 0 }, 64, MiscTypes.DROP, null);

        // Nikolite may not exist, so lets make it.
        dropNikoliteDust = ItemUtils
            .generateSpecialUseDusts("Nikolite", "Nikolite", Utils.rgbtoHexValue(60, 180, 200))[2];

        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("ingotNikolite", 1) == null) {
            new BaseItemIngotOld("itemIngotNikolite", "Nikolite", Utils.rgbtoHexValue(60, 180, 200), 0);
        }

        dropFluorineBlob = new BaseItemMisc("Fluorine", new short[] { 30, 230, 230 }, 64, MiscTypes.DROP, null);
        addRecipes();
    }

    private void addRecipes() {
        addExtractorRecipe(new ItemStack(dropBiomassBlob), Materials.Biomass.getFluid(30));
        addExtractorRecipe(new ItemStack(dropEthanolBlob), Materials.Ethanol.getFluid(6));
        addExtractorRecipe(new ItemStack(dropFluorineBlob), Materials.Fluorine.getGas(4));
    }

    private void addExtractorRecipe(ItemStack input, FluidStack output) {
        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .fluidOutputs(output)
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(8)
            .addTo(fluidExtractionRecipes);
    }

    private void setMaterials() {
        if (Comb_Slag == null) {
            Comb_Slag = GTBees.combs.getStackForType(CombType.SLAG);
        }
        if (Comb_Stone == null) {
            Comb_Stone = GTBees.combs.getStackForType(CombType.STONE);
        }
        if (PTFE == null) {
            PTFE = trySetValue("Polytetrafluoroethylene");
        }
        if (PBS == null) {
            PBS = trySetValue("StyreneButadieneRubber");
        }
    }

    private Materials trySetValue(String material) {
        Materials mTemp = Materials.get(material);
        if (mTemp != Materials._NULL) {
            return mTemp;
        }
        return Materials._NULL;
    }
}
