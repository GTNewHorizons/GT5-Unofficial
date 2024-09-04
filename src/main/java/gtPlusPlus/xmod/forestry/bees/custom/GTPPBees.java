package gtPlusPlus.xmod.forestry.bees.custom;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.ingots.BaseItemIngotOld;
import gtPlusPlus.core.item.base.misc.BaseItemMisc;
import gtPlusPlus.core.item.base.misc.BaseItemMisc.MiscTypes;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class GTPPBees {

    // Custom Comb Drop Base Items
    public static Item dropForceGem;
    public static Item dropBiomassBlob;
    public static Item dropEthanolBlob;
    public static Item dropNikoliteDust;
    public static Item dropFluorineBlob;

    // Base Comb Item
    public static ItemCustomComb combs;

    // Combs obtained via reflection
    public static ItemStack Comb_Slag;
    public static ItemStack Comb_Stone;

    // Materials obtained via reflection
    public static Materials PTFE;
    public static Materials PBS;

    // public static GTPP_Branch_Definition definition;

    public GTPPBees() {
        if (Forestry.isModLoaded()) {

            // Set Materials and Comb stacks from GT via Reflection
            setMaterials();
            setCustomItems();

            try {
                combs = new ItemCustomComb();
                combs.initCombsRecipes();
                GTPPBeeDefinition.initBees();
            } catch (Throwable t) {
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
        addExtractorRecipe(ItemUtils.getSimpleStack(dropBiomassBlob), FluidUtils.getFluidStack("biomass", 30));
        addExtractorRecipe(ItemUtils.getSimpleStack(dropEthanolBlob), FluidUtils.getFluidStack("ethanol", 6));
        addExtractorRecipe(ItemUtils.getSimpleStack(dropFluorineBlob), FluidUtils.getFluidStack("fluorine", 4));
    }

    private void addExtractorRecipe(ItemStack input, FluidStack output) {
        GTValues.RA.stdBuilder()
            .itemInputs(input)
            .fluidOutputs(output)
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(8)
            .addTo(fluidExtractionRecipes);
    }

    private static boolean tryGetBeesBoolean() {
        try {
            Class<?> mProxy = Class.forName("gregtech.GTMod.gregtechproxy");
            Field mNerf = FieldUtils.getDeclaredField(mProxy, "mGTBees", true);
            boolean returnValue = (boolean) mNerf.get(GTMod.gregtechproxy);
            return returnValue;
        } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
            return false;
        }
    }

    private void setMaterials() {
        try {

            Class<?> gtBees = Class.forName("gregtech.loaders.misc.GTBees");
            Class<?> gtCombItemClass = Class.forName("gregtech.common.items.ItemComb");
            Class gtCombEnumClass = Class.forName("gregtech.common.items.CombType");
            Field gtCombs = FieldUtils.getDeclaredField(gtBees, "combs", true);
            gtCombs.setAccessible(true);
            ReflectionUtils.makeFieldAccessible(gtCombs);
            Enum gtCombTypeSlag = Enum.valueOf(gtCombEnumClass, "SLAG");
            Enum gtCombTypeStone = Enum.valueOf(gtCombEnumClass, "STONE");
            Object oCombObject = gtCombs.get(null);

            Logger.DEBUG_BEES("Field getModifiers: " + gtCombs.getModifiers());
            Logger.DEBUG_BEES("Field toGenericString: " + gtCombs.toGenericString());
            Logger.DEBUG_BEES("Field getClass: " + gtCombs.getClass());
            Logger.DEBUG_BEES("Field isEnumConstant: " + gtCombs.isEnumConstant());
            Logger.DEBUG_BEES("Field isSynthetic: " + gtCombs.isSynthetic());
            Logger.DEBUG_BEES("Field get(gtBees) != null: " + (gtCombs.get(gtBees) != null));
            Logger.DEBUG_BEES("Field isAccessible: " + gtCombs.isAccessible());

            Logger.BEES("gtBees: " + (gtBees != null));
            Logger.BEES("gtCombItemClass: " + (gtCombItemClass != null));
            Logger.BEES("gtCombEnumClass: " + (gtCombEnumClass != null));
            Logger.BEES("gtCombs: " + (gtCombs != null));
            Logger.BEES("gtCombTypeSlag: " + (gtCombTypeSlag != null));
            Logger.BEES("gtCombTypeStone: " + (gtCombTypeStone != null));
            Logger.BEES("oCombObject: " + (oCombObject != null));

            // if (gtCombItemClass.isInstance(oCombObject)){
            Method getStackForType;
            getStackForType = gtCombItemClass.getDeclaredMethod("getStackForType", gtCombEnumClass);

            if (getStackForType != null) {
                Logger.BEES("Found Method: getStackForType");
            }
            if (Comb_Slag == null) {
                Comb_Slag = (ItemStack) getStackForType.invoke(gtBees, gtCombTypeSlag);
            }
            if (Comb_Stone == null) {
                Comb_Stone = (ItemStack) getStackForType.invoke(gtBees, gtCombTypeStone);
            }

        } catch (NullPointerException | ClassNotFoundException | IllegalArgumentException | IllegalAccessException
            | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            Logger.BEES("Bad Reflection. setMaterials()");
        }

        PTFE = trySetValue("Polytetrafluoroethylene");
        PBS = trySetValue("StyreneButadieneRubber");
    }

    private Materials trySetValue(String material) {
        Materials mTemp = Materials.get(material);
        if (mTemp != Materials._NULL) {
            return mTemp;
        }
        return Materials._NULL;
    }
}
