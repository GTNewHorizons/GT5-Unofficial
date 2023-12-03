package gtPlusPlus.core.util.minecraft;

import static gregtech.api.recipe.RecipeMaps.fluidCannerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.FluidGT6;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.base.cell.BaseItemPlasmaCell;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;

public class FluidUtils {

    private static HashMap<String, Fluid> sFluidCache = new HashMap<>();

    public static FluidStack getWater(final int amount) {
        return FluidUtils.getFluidStack("water", amount);
    }

    public static FluidStack getDistilledWater(final int amount) {
        return FluidUtils.getFluidStack("ic2distilledwater", amount);
    }

    public static FluidStack getHotWater(final int amount) {
        return FluidUtils.getFluidStack("ic2hotwater", amount);
    }

    public static FluidStack getLava(final int amount) {
        return FluidUtils.getFluidStack("lava", amount);
    }

    public static FluidStack getPahoehoeLava(final int amount) {
        return FluidUtils.getFluidStack("ic2pahoehoelava", amount);
    }

    public static FluidStack getSteam(final int amount) {
        return FluidUtils.getFluidStack("steam", amount);
    }

    public static FluidStack getSuperHeatedSteam(final int amount) {
        return FluidUtils.getFluidStack("ic2superheatedsteam", amount);
    }

    public static FluidStack getHydrofluoricAcid(int amount) {
        return FluidUtils.getFluidStack("hydrofluoricacid", amount);
    }

    public static Fluid sGregtechHydrofluoricAcid = null;

    public static FluidStack getHydrofluoricAcidGT(int amount) {
        if (sGregtechHydrofluoricAcid == null) {
            FluidStack aGTHF = FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 1);
            sGregtechHydrofluoricAcid = aGTHF != null ? aGTHF.getFluid() : getHydrofluoricAcid(1).getFluid();
        }
        return FluidUtils.getFluidStack(sGregtechHydrofluoricAcid, amount);
    }

    public static boolean doesHydrofluoricAcidGtExist() {
        if (sGregtechHydrofluoricAcid == null) {
            getHydrofluoricAcidGT(1);
        }
        return sGregtechHydrofluoricAcid != null && sGregtechHydrofluoricAcid != getHydrofluoricAcid(1).getFluid();
    }

    private static FluidStack createFluidStack(Fluid aFluid, int aAmount) {
        if (aFluid != null) {
            return new FluidStack(aFluid, aAmount);
        }
        return null;
    }

    public static FluidStack getFluidStack(final String aFluidName, final int aAmount) {
        Fluid aFluid = sFluidCache.get(aFluidName);
        if (aFluid != null) {
            return createFluidStack(aFluid, aAmount);
        } else {
            Fluid aLookupFluid = FluidRegistry.getFluid(aFluidName);
            if (aLookupFluid != null) {
                sFluidCache.put(aFluidName, aLookupFluid);
                return createFluidStack(aLookupFluid, aAmount);
            }
        }
        return null;
    }

    public static FluidStack getFluidStack(final FluidStack aFluidStack, final int aAmount) {
        if (aFluidStack == null) {
            return null;
        }
        return new FluidStack(aFluidStack, aAmount);
    }

    public static FluidStack getFluidStack(final Fluid aFluid, final int aAmount) {
        if (aFluid == null) {
            return null;
        }
        return new FluidStack(aFluid, aAmount);
    }

    public static Fluid addGtFluid(final String aName, final String aLocalized, final GT_Materials aMaterial,
            final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer,
            final int aFluidAmount) {
        return addGtFluid(
                aName,
                aLocalized,
                aMaterial,
                aState,
                aTemperatureK,
                aFullContainer,
                aEmptyContainer,
                aFluidAmount,
                true);
    }

    public static Fluid addGtFluid(final String aName, final String aLocalized, final GT_Materials aMaterial,
            final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer,
            final int aFluidAmount, final boolean aGenerateCell) {
        Fluid g = addGTFluid(
                aName,
                "fluid.autogenerated",
                aLocalized,
                aMaterial != null ? aMaterial.mRGBa : new short[] { 255, 255, 255, 0 },
                aState,
                aTemperatureK,
                aFullContainer,
                aEmptyContainer,
                aFluidAmount,
                aGenerateCell);
        if (aMaterial != null) {
            switch (aState) {
                case 1 -> {
                    aMaterial.mFluid = (g);
                }
                case 2 -> {
                    aMaterial.mGas = (g);
                }
                case 3 -> {
                    aMaterial.mPlasma = (g);
                }
            }
        }
        return g;
    }

    public static Fluid addGTFluid(final String aName, final String aLocalized, final short[] aRGBa, final int aState,
            final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer,
            final int aFluidAmount, final boolean aGenerateCell) {
        return addGTFluid(
                "molten." + aName,
                "molten.autogenerated",
                aLocalized,
                aRGBa,
                aState,
                aTemperatureK,
                aFullContainer,
                aEmptyContainer,
                aFluidAmount,
                aGenerateCell);
    }

    public static Fluid addGTFluidNonMolten(final String aName, final String aLocalized, final short[] aRGBa,
            final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer,
            final int aFluidAmount, final boolean aGenerateCell) {
        return addGTFluid(
                "fluid." + aName,
                "fluid.autogenerated",
                aLocalized,
                aRGBa,
                aState,
                aTemperatureK,
                aFullContainer,
                aEmptyContainer,
                aFluidAmount,
                aGenerateCell);
    }

    public static Fluid addGTFluidNoPrefix(final String aName, final String aLocalized, final short[] aRGBa,
            final int aState, final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer,
            final int aFluidAmount, final boolean aGenerateCell) {
        return addGTFluid(
                aName,
                "fluid.autogenerated",
                aLocalized,
                aRGBa,
                aState,
                aTemperatureK,
                aFullContainer,
                aEmptyContainer,
                aFluidAmount,
                aGenerateCell);
    }

    // Gas
    public static Fluid addGtGas(final String aName, final String aLocalized, final short[] aRGBa, final int aState,
            final long aTemperatureK, final ItemStack aFullContainer, final ItemStack aEmptyContainer,
            final int aFluidAmount, final boolean aGenerateCell) {
        return addGTFluid(
                aName,
                "fluid.autogenerated",
                aLocalized,
                aRGBa,
                aState,
                aTemperatureK,
                aFullContainer,
                aEmptyContainer,
                aFluidAmount,
                aGenerateCell);
    }

    public static Fluid addGTPlasma(final Material aMaterial) {
        if (aMaterial.getLocalizedName().toLowerCase().contains("clay") || (aMaterial.getComposites().size() > 1)
                || aMaterial.getLocalizedName().toLowerCase().contains("wrought")) {
            return null;
        }
        Logger.INFO("Generating a " + aMaterial.getLocalizedName() + " Plasma Cell");
        if (aMaterial.vComponentCount != 1) {
            Logger.INFO("Compound made from: ");
            for (final MaterialStack x : aMaterial.getComposites()) {
                Logger.INFO(x.getStackMaterial().getLocalizedName());
            }
            Logger.INFO("Material is a composite, not generating plasma.");
            return null;
        }

        ItemStack temp = null;
        // Generate a Cell if we need to
        if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellPlasma" + aMaterial.getUnlocalizedName(), 1)
                == null) {
            new BaseItemPlasmaCell(aMaterial);
            temp = aMaterial.getPlasmaCell(1);
        } else {
            temp = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellPlasma" + aMaterial.getUnlocalizedName(), 1);
        }
        if (temp != null) {
            return addGTFluid(
                    "plasma." + Utils.sanitizeString(aMaterial.getLocalizedName().toLowerCase()),
                    "plasma.autogenerated",
                    aMaterial.getLocalizedName() + " Plasma",
                    aMaterial.getRGBA(),
                    3,
                    10000,
                    temp,
                    ItemUtils.getEmptyCell(),
                    1000,
                    false);
        }
        return null;
    }

    public static Fluid addGTFluid(String aName, final String aTexture, final String aLocalized, final short[] aRGBa,
            final int aState, final long aTemperatureK, ItemStack aFullContainer, final ItemStack aEmptyContainer,
            final int aFluidAmount, final boolean aGenerateFilledCell) {

        String aNameOriginal = aName;
        Logger.INFO("Generating Fluid for " + aName);

        aName = Utils.sanitizeString(aName.toLowerCase());

        String aLocalName = (aLocalized == null) ? aName : aLocalized;

        Fluid rFluid;
        Fluid gFluid = FluidRegistry.getFluid(aName);
        FluidStack aCheck = FluidUtils.getWildcardFluidStack(aName.toLowerCase(), 1000);
        boolean register = false;
        if (aCheck != null) {
            rFluid = aCheck.getFluid();
        } else if (gFluid != null) {
            rFluid = gFluid;
        } else {
            rFluid = new FluidGT6(aName, aTexture, (aRGBa != null) ? aRGBa : Dyes._NULL.getRGBA());
            register = true;
        }

        if (register) {
            GT_LanguageManager.addStringLocalization(rFluid.getUnlocalizedName(), aLocalName);
            if (FluidRegistry.registerFluid(rFluid)) {
                switch (aState) {
                    case 0 -> {
                        rFluid.setGaseous(false);
                        rFluid.setViscosity(10000);
                    }
                    case 1, 4 -> {
                        rFluid.setGaseous(false);
                        rFluid.setViscosity(1000);
                    }
                    case 2 -> {
                        rFluid.setGaseous(true);
                        rFluid.setDensity(-100);
                        rFluid.setViscosity(200);
                    }
                    case 3 -> {
                        rFluid.setGaseous(true);
                        rFluid.setDensity(-10000);
                        rFluid.setViscosity(10);
                        rFluid.setLuminosity(15);
                    }
                }
            }
        }

        String aNameNonMolten = aLocalName.contains("Molten") ? aLocalName.replace("Molten", "") : aLocalName;

        if (aFullContainer == null) {
            ItemStack oreStack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aLocalName, 1);
            aFullContainer = oreStack;
            if (aFullContainer == null) {
                oreStack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aNameOriginal, 1);
                aFullContainer = oreStack;
                if (aFullContainer == null) {
                    oreStack = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aNameNonMolten, 1);
                    aFullContainer = oreStack;
                    if (aFullContainer != null) {
                        Logger.INFO("Found cell for " + aNameNonMolten);
                    }
                } else {
                    Logger.INFO("Found cell for " + aNameOriginal);
                }
            } else {
                Logger.INFO("Found cell for " + aLocalName);
            }
        }

        Item tempCell = null;
        // Generate a Cell if we need to
        if (aGenerateFilledCell && aFullContainer == null) {
            String aMatName = aNameOriginal;
            if (aMatName.contains("molten.")) {
                aMatName = aMatName.replace("molten.", "");
                aMatName = aMatName.substring(0, 1).toUpperCase() + aMatName.substring(1);
            }
            if (aMatName.contains("fluid.")) {
                aMatName = aMatName.replace("fluid.", "");
                aMatName = aMatName.substring(0, 1).toUpperCase() + aMatName.substring(1);
            }
            Logger.INFO("Generating cell for " + aMatName + ", " + aLocalName);
            tempCell = new BaseItemComponent(aMatName, aLocalName, aRGBa);
            aFullContainer = ItemUtils.getSimpleStack(tempCell);
        }

        if ((rFluid.getTemperature() == new Fluid("test").getTemperature()) || (rFluid.getTemperature() <= 0)) {
            rFluid.setTemperature((int) (aTemperatureK));
        }
        if ((aFullContainer != null) && (aEmptyContainer != null)
                && !FluidContainerRegistry.registerFluidContainer(
                        new FluidStack(rFluid, aFluidAmount),
                        aFullContainer,
                        aEmptyContainer)) {
            GT_Values.RA.stdBuilder().itemInputs(ItemList.Cell_Empty.get(1L)).itemOutputs(aFullContainer)
                    .fluidInputs(new FluidStack(rFluid, aFluidAmount)).duration(4).eut(1).addTo(fluidCannerRecipes);
        }
        return rFluid;
    }

    public static boolean valid(final Object aStack) {
        return (aStack instanceof ItemStack) && (((ItemStack) aStack).getItem() != null)
                && (((ItemStack) aStack).stackSize >= 0);
    }

    public static boolean invalid(final Object aStack) {
        return !(aStack instanceof ItemStack) || (((ItemStack) aStack).getItem() == null)
                || (((ItemStack) aStack).stackSize < 0);
    }

    public static boolean equal(final ItemStack aStack1, final ItemStack aStack2) {
        return equal(aStack1, aStack2, false);
    }

    public static boolean equal(final ItemStack aStack1, final ItemStack aStack2, final boolean aIgnoreNBT) {
        return (aStack1 != null) && (aStack2 != null) && equal_(aStack1, aStack2, aIgnoreNBT);
    }

    public static boolean equal_(final ItemStack aStack1, final ItemStack aStack2, final boolean aIgnoreNBT) {
        return (aStack1.getItem() == aStack2.getItem())
                && (aIgnoreNBT || ((aStack1.getTagCompound() == null == (aStack2.getTagCompound() == null))
                        && ((aStack1.getTagCompound() == null)
                                || aStack1.getTagCompound().equals(aStack2.getTagCompound()))))
                && ((meta(aStack1) == meta(aStack2)) || (meta(aStack1) == 32767) || (meta(aStack2) == 32767));
    }

    public static ItemStack copy(final Object... aStacks) {
        for (final Object tStack : aStacks) {
            if (valid(tStack)) {
                return ((ItemStack) tStack).copy();
            }
        }
        return null;
    }

    public static ItemStack copyMeta(final long aMetaData, final Object... aStacks) {
        final ItemStack rStack = copy(aStacks);
        if (invalid(rStack)) {
            return null;
        }
        return meta(rStack, aMetaData);
    }

    public static short meta(final ItemStack aStack) {
        return (short) Items.feather.getDamage(aStack);
    }

    public static ItemStack meta(final ItemStack aStack, final long aMeta) {
        Items.feather.setDamage(aStack, (short) aMeta);
        return aStack;
    }

    public static ItemStack amount(final long aAmount, final Object... aStacks) {
        final ItemStack rStack = copy(aStacks);
        if (invalid(rStack)) {
            return null;
        }
        rStack.stackSize = (int) aAmount;
        return rStack;
    }

    public static ItemStack container(final ItemStack aStack, final boolean aCheckIFluidContainerItems) {
        if (invalid(aStack)) {
            return null;
        }
        if (aStack.getItem().hasContainerItem(aStack)) {
            return aStack.getItem().getContainerItem(aStack);
        }
        if (equal(aStack, ItemUtils.getEmptyCell(), true)) {
            return null;
        }
        if (aCheckIFluidContainerItems && (aStack.getItem() instanceof IFluidContainerItem)
                && (((IFluidContainerItem) aStack.getItem()).getCapacity(aStack) > 0)) {
            final ItemStack tStack = amount(1L, aStack);
            ((IFluidContainerItem) aStack.getItem()).drain(tStack, Integer.MAX_VALUE, true);
            if (!equal(aStack, tStack)) {
                return tStack;
            }
            return null;
        }
        if (equal(aStack, ItemList.IC2_ForgeHammer.get(1)) || equal(aStack, ItemList.IC2_WireCutter.get(1))) {
            return copyMeta(meta(aStack) + 1, aStack);
        }
        return null;
    }

    public static ItemStack container(final ItemStack aStack, final boolean aCheckIFluidContainerItems,
            final int aStacksize) {
        return amount(aStacksize, container(aStack, aCheckIFluidContainerItems));
    }

    public static Fluid generateFluidNonMolten(final String unlocalizedName, final String localizedName,
            final int MeltingPoint, final short[] RGBA, final ItemStack dustStack, final ItemStack dustStack2) {
        return generateFluidNonMolten(
                unlocalizedName,
                localizedName,
                MeltingPoint,
                RGBA,
                dustStack,
                dustStack2,
                144,
                true);
    }

    public static Fluid generateFluidNonMolten(final String unlocalizedName, final String localizedName,
            final int MeltingPoint, final short[] RGBA, final ItemStack dustStack, final ItemStack dustStack2,
            final boolean aGenerateCell) {
        return generateFluidNonMolten(
                unlocalizedName,
                localizedName,
                MeltingPoint,
                RGBA,
                dustStack,
                dustStack2,
                144,
                aGenerateCell);
    }

    public static final Fluid generateFluidNonMolten(final String unlocalizedName, final String localizedName,
            final int MeltingPoint, final short[] RGBA, ItemStack dustStack, final ItemStack dustStack2,
            final int amountPerItem, final boolean aGenerateCell) {
        if (dustStack == null) {
            dustStack = ItemUtils
                    .getItemStackOfAmountFromOreDictNoBroken("dust" + Utils.sanitizeString(localizedName), 1);
        }
        FluidStack aFStack = (FluidUtils.getFluidStack(unlocalizedName.toLowerCase(), 1));
        if (aFStack == null) {
            Logger.WARNING("Generating our own fluid.");

            final Fluid gtFluid = FluidUtils.addGTFluidNonMolten(
                    unlocalizedName,
                    localizedName,
                    RGBA,
                    4,
                    MeltingPoint,
                    null,
                    ItemUtils.getEmptyCell(),
                    1000,
                    aGenerateCell);

            if (dustStack != null) {
                GT_Values.RA.stdBuilder().itemInputs(dustStack)
                        .fluidOutputs(FluidUtils.getFluidStack(gtFluid, amountPerItem)).duration(1 * SECONDS).eut(16)
                        .addTo(fluidExtractionRecipes);
            }
            if (dustStack2 != null) {
                GT_Values.RA.stdBuilder().itemInputs(dustStack2)
                        .fluidOutputs(FluidUtils.getFluidStack(gtFluid, amountPerItem)).duration(1 * SECONDS).eut(16)
                        .addTo(fluidExtractionRecipes);
            }

            return gtFluid;
        } else {
            Logger.INFO("FLUID GENERATION FAILED FOR " + localizedName + ", ALREADY EXISTS");
            return aFStack.getFluid();
        }
    }

    public static Fluid generateFluidNoPrefix(final String unlocalizedName, final String localizedName,
            final int MeltingPoint, final short[] RGBA) {
        return generateFluidNoPrefix(unlocalizedName, localizedName, MeltingPoint, RGBA, true);
    }

    public static Fluid generateFluidNoPrefix(final String unlocalizedName, final String localizedName,
            final int MeltingPoint, final short[] RGBA, final boolean aGenerateCell) {
        Fluid gtFluid;
        if (FluidUtils.getFluidStack(unlocalizedName.toLowerCase(), 1) == null) {
            Logger.WARNING("Generating our own fluid.");
            gtFluid = FluidUtils.addGTFluidNoPrefix(
                    unlocalizedName,
                    localizedName,
                    RGBA,
                    4,
                    MeltingPoint,
                    null,
                    ItemUtils.getEmptyCell(),
                    1000,
                    aGenerateCell);
        } else {
            gtFluid = FluidUtils.getFluidStack(unlocalizedName.toLowerCase(), 1).getFluid();
        }
        return gtFluid;
    }

    public static Fluid generateGas(final String unlocalizedName, final String localizedName, final int MeltingPoint,
            final short[] RGBA, final boolean aGenerateCell) {
        Fluid gtFluid;
        if (FluidUtils.getFluidStack(unlocalizedName.toLowerCase(), 1) == null) {
            Logger.WARNING("Generating our own gas.");
            gtFluid = FluidUtils.addGtGas(
                    unlocalizedName,
                    localizedName,
                    RGBA,
                    3,
                    MeltingPoint,
                    null,
                    ItemUtils.getEmptyCell(),
                    1000,
                    aGenerateCell);
        } else {
            gtFluid = FluidUtils.getFluidStack(unlocalizedName.toLowerCase(), 1).getFluid();
        }
        return gtFluid;
    }

    public static FluidStack getMobEssence(final int amount) {
        return EnchantingUtils.getMobEssence(amount);
    }

    public static boolean doesFluidExist(String aFluidName) {
        FluidStack aFStack1 = (FluidUtils.getFluidStack("molten" + "." + aFluidName.toLowerCase(), 1));
        FluidStack aFStack2 = (FluidUtils.getFluidStack("fluid" + "." + aFluidName.toLowerCase(), 1));
        FluidStack aFStack3 = (FluidUtils.getFluidStack(aFluidName.toLowerCase(), 1));
        FluidStack aFStack4 = (FluidUtils.getFluidStack(aFluidName, 1));
        FluidStack aFStack5 = (FluidUtils.getFluidStack("liquid_" + aFluidName.toLowerCase(), 1));
        FluidStack aFStack6 = (FluidUtils.getFluidStack("liquid" + "." + aFluidName.toLowerCase(), 1));
        return aFStack1 != null || aFStack2 != null
                || aFStack3 != null
                || aFStack4 != null
                || aFStack5 != null
                || aFStack6 != null;
    }

    public static FluidStack getWildcardFluidStack(String aFluidName, int amount) {
        FluidStack aFStack1 = (FluidUtils.getFluidStack(aFluidName, amount));
        FluidStack aFStack2 = (FluidUtils.getFluidStack(aFluidName.toLowerCase(), amount));
        FluidStack aFStack3 = (FluidUtils.getFluidStack("molten" + "." + aFluidName.toLowerCase(), amount));
        FluidStack aFStack4 = (FluidUtils.getFluidStack("fluid" + "." + aFluidName.toLowerCase(), amount));
        FluidStack aFStack5 = (FluidUtils.getFluidStack("liquid_" + aFluidName.toLowerCase(), amount));
        FluidStack aFStack6 = (FluidUtils.getFluidStack("liquid" + "." + aFluidName.toLowerCase(), amount));
        if (aFStack1 != null) {
            return aFStack1;
        }
        if (aFStack2 != null) {
            return aFStack2;
        }
        if (aFStack3 != null) {
            return aFStack3;
        }
        if (aFStack4 != null) {
            return aFStack4;
        }
        if (aFStack5 != null) {
            return aFStack5;
        }
        if (aFStack6 != null) {
            return aFStack6;
        }
        return null;
    }

    public static FluidStack getWildcardFluidStack(Materials aMaterial, int amount) {
        FluidStack aFStack1 = aMaterial.getFluid(amount);
        FluidStack aFStack2 = aMaterial.getGas(amount);
        FluidStack aFStack3 = aMaterial.getMolten(amount);
        FluidStack aFStack4 = aMaterial.getSolid(amount);
        if (aFStack1 != null) {
            return aFStack1;
        } else if (aFStack2 != null) {
            return aFStack2;
        } else if (aFStack3 != null) {
            return aFStack3;
        } else if (aFStack4 != null) {
            return aFStack4;
        } else {
            return null;
        }
    }

    public static FluidStack getAir(int aAmount) {
        return FluidUtils.getFluidStack("air", aAmount);
    }
}
