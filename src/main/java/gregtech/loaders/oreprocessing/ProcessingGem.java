package gregtech.loaders.oreprocessing;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class ProcessingGem implements gregtech.api.interfaces.IOreRecipeRegistrator {//TODO COMPARE WITH GEM??? generators

    public ProcessingGem() {
        OrePrefixes.gem.add(this);
        OrePrefixes.gemChipped.add(this);
        OrePrefixes.gemExquisite.add(this);
        OrePrefixes.gemFlawed.add(this);
        OrePrefixes.gemFlawless.add(this);
    }

    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
        boolean aNoWorking = aMaterial.contains(SubTag.NO_WORKING);
        boolean aNoSmelting = aMaterial.contains(SubTag.NO_SMELTING);
        boolean aSpecialRecipeReq = (aMaterial.contains(SubTag.MORTAR_GRINDABLE)) && (GregTech_API.sRecipeFile.get(ConfigCategories.Tools.mortar, aMaterial.mName, true));

        switch (aPrefix) {
            case gem:
                if (!OrePrefixes.block.isIgnored(aMaterial)) {
                    GT_ModHandler.addCompressionRecipe(GT_Utility.copyAmount(9L, aStack), GT_OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L));
                }
                if (!aNoSmelting) {
                    GT_ModHandler.addSmeltingRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L));
                }

                if (!aNoWorking) {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"h", "X", 'X', OrePrefixes.gemFlawless.get(aMaterial)});
                        if (aMaterial.contains(SubTag.SMELTING_TO_GEM))
                            GT_ModHandler.addCraftingRecipe(GT_Utility.copyAmount(1L, aStack), GT_Proxy.tBits, new Object[]{"XXX", "XXX", "XXX", 'X', OrePrefixes.nugget.get(aMaterial)});
                        if (aSpecialRecipeReq)
                            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"X", "m", 'X', OrePrefixes.gem.get(aMaterial)});
                    }
                }
                if ("Coal".equals(aMaterial.mName) || "Charcoal".equals(aMaterial.mName)) {
                    if (GregTech_API.sRecipeFile.get(ConfigCategories.Recipes.disabledrecipes, "torchesFromCoal", false)) {
                        GT_ModHandler.removeRecipeDelayed(GT_Utility.copyAmount(1L, aStack), null, null, new ItemStack(Items.stick, 1, 0));
                    }
                }
                break;
            case gemChipped:
                if (!aNoWorking) {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"h", "X", 'X', OrePrefixes.gemFlawed.get(aMaterial)});
                        if (aSpecialRecipeReq)
                            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L), GT_Proxy.tBits, new Object[]{"X", "m", 'X', OrePrefixes.gemChipped.get(aMaterial)});
                    }
                }
                break;
            case gemExquisite:
                if (!aNoWorking) {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial))
                        if (aSpecialRecipeReq)
                            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 4L), GT_Proxy.tBits, new Object[]{"X", "m", 'X', OrePrefixes.gemExquisite.get(aMaterial)});
                }
                break;
            case gemFlawed:
                if (!aNoWorking) {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"h", "X", 'X', OrePrefixes.gem.get(aMaterial)});
                        if (aSpecialRecipeReq)
                            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"X", "m", 'X', OrePrefixes.gemFlawed.get(aMaterial)});
                    }
                }
                break;
            case gemFlawless:
                if (!aNoWorking) {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"h", "X", 'X', OrePrefixes.gemExquisite.get(aMaterial)});
                        if (aSpecialRecipeReq)
                            GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial, 2L), GT_Proxy.tBits, new Object[]{"X", "m", 'X', OrePrefixes.gemFlawless.get(aMaterial)});
                    }
                }
                break;
            default:
                break;
        }
        //DO NOT DELETE. This is a Hack that prevents rebuilding of the nameToId table!
        OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", ""));
    }

    @Override
    public void registerOreAsync(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {

        long aMaterialMass = aMaterial.getMass();
        boolean aNoSmashing = aMaterial.contains(SubTag.NO_SMASHING);
        boolean aNoWorking = aMaterial.contains(SubTag.NO_WORKING);
        boolean aFuelPower = aMaterial.mFuelPower > 0;
        Collection<ItemStack> col = Collections.unmodifiableCollection(OreDictionary.getOres("craftingLens" + aMaterial.mColor.mName.replace(" ", "")));
        switch (aPrefix) {
            case gem:
                if (aFuelPower) {
                    GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aStack), null, aMaterial.mFuelPower * 2, aMaterial.mFuelType);
                }
                if (!OrePrefixes.block.isIgnored(aMaterial)) {
                    GT_ModHandler.addCompressionRecipe(GT_Utility.copyAmount(9L, aStack), GT_OreDictUnificator.get(OrePrefixes.block, aMaterial, 1L));
                }
                if (aNoSmashing) {
                    GT_Values.RA.addForgeHammerRecipe(aStack, GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 2L), 64, 16);
                } else {
                    GT_Values.RA.addForgeHammerRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L), (int) Math.max(aMaterialMass, 1L), 16);
                    GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L), (int) Math.max(aMaterialMass * 2L, 1L), 24);
                    GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(2L, aStack), GT_OreDictUnificator.get(OrePrefixes.plateDouble, aMaterial, 1L), (int) Math.max(aMaterialMass * 2L, 1L), 96);
                    GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(3L, aStack), GT_OreDictUnificator.get(OrePrefixes.plateTriple, aMaterial, 1L), (int) Math.max(aMaterialMass * 3L, 1L), 96);
                    GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(4L, aStack), GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, aMaterial, 1L), (int) Math.max(aMaterialMass * 4L, 1L), 96);
                    GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(5L, aStack), GT_OreDictUnificator.get(OrePrefixes.plateQuintuple, aMaterial, 1L), (int) Math.max(aMaterialMass * 5L, 1L), 96);
                    GT_Values.RA.addBenderRecipe(GT_Utility.copyAmount(9L, aStack), GT_OreDictUnificator.get(OrePrefixes.plateDense, aMaterial, 1L), (int) Math.max(aMaterialMass * 9L, 1L), 96);
                }

                if (aNoWorking) {
                    GT_Values.RA.addLatheRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 2L), (int) Math.max(aMaterialMass, 1L), 16);
                } else {
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_Values.RA.addImplosionRecipe(GT_Utility.copyAmount(3L, aStack), 8, GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));
                    }
                }

                if ("CertusQuartz".equals(aMaterial.mName)) {
                    GT_Values.RA.addElectrolyzerRecipe(aStack, 0, GT_ModHandler.getModItem("appliedenergistics2", "item.ItemMultiMaterial", 1L, 1), null, null, null, null, null, 2000, 30);
                }
                for (ItemStack is : col) {//Engraver Recipe adder
                    is.stackSize = 0;
                    GT_Values.RA.addLaserEngraverRecipe(GT_Utility.copyAmount(3L, aStack), is, GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 1L), 1200, 480);
                }

                break;
            case gemChipped:
                if (aFuelPower)
                    GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aStack), null, aMaterial.mFuelPower / 2, aMaterial.mFuelType);
                if (!aNoWorking) {
                    GT_Values.RA.addLatheRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L), GT_OreDictUnificator.get(OrePrefixes.dustTiny, aMaterial, 1L), (int) Math.max(aMaterialMass, 1L), 8);
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_Values.RA.addImplosionRecipe(GT_Utility.copyAmount(3L, aStack), 8, GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));
                    }
                }
                for (ItemStack is : col) {//Engraver Recipe adder
                    is.stackSize = 0;
                    GT_Values.RA.addLaserEngraverRecipe(GT_Utility.copyAmount(3L, aStack), is, GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, 1L), 600, 30);
                }
                break;
            case gemExquisite:
                if (aFuelPower)
                    GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aStack), null, aMaterial.mFuelPower * 8, aMaterial.mFuelType);
                GT_Values.RA.addForgeHammerRecipe(aStack, GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, 2L), 64, 16);
                break;
            case gemFlawed:
                if (aFuelPower)
                    GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aStack), null, aMaterial.mFuelPower, aMaterial.mFuelType);
                if (!aNoWorking) {
                    GT_Values.RA.addLatheRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 2L), GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial, 1L), (int) Math.max(aMaterialMass, 1L), 12);
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_Values.RA.addImplosionRecipe(GT_Utility.copyAmount(3L, aStack), 8, GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));
                    }
                }
                GT_Values.RA.addForgeHammerRecipe(aStack, GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, 2L), 64, 16);
                for (ItemStack is : col) {//Engraver Recipe adder
                    is.stackSize = 0;
                    GT_Values.RA.addLaserEngraverRecipe(GT_Utility.copyAmount(3L, aStack), is, GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L), 600, 120);
                }
                break;
            case gemFlawless:
                if (aFuelPower)
                    GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aStack), null, aMaterial.mFuelPower * 4, aMaterial.mFuelType);
                if (!aNoWorking) {
                    GT_Values.RA.addLatheRecipe(GT_Utility.copyAmount(1L, aStack), GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L), GT_OreDictUnificator.getDust(aMaterial, aPrefix.mMaterialAmount - OrePrefixes.stickLong.mMaterialAmount), (int) Math.max(aMaterialMass * 5L, 1L), 16);
                    if (aMaterial.mUnificatable && (aMaterial.mMaterialInto == aMaterial)) {
                        GT_Values.RA.addImplosionRecipe(GT_Utility.copyAmount(3L, aStack), 8, GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1), GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 2));
                    }
                }
                GT_Values.RA.addForgeHammerRecipe(aStack, GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 2L), 64, 16);
                for (ItemStack is : col) {//Engraver Recipe adder
                    is.stackSize = 0;
                    GT_Values.RA.addLaserEngraverRecipe(GT_Utility.copyAmount(3L, aStack), is, GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, 1L), 2400, 2000);
                }
                break;
            default:
                break;
        }
    }
}
