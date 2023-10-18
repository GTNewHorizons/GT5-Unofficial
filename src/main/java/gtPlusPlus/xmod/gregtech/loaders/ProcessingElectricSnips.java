package gtPlusPlus.xmod.gregtech.loaders;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_OreRecipeRegistrator;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;

public class ProcessingElectricSnips implements Interface_OreRecipeRegistrator, Runnable {

    public ProcessingElectricSnips() {
        GregtechOrePrefixes.toolElectricSnips.add(this);
    }

    @Override
    public void registerOre(final GregtechOrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName,
            final String aModName, final ItemStack aStack) {
        if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)) {
            if (aMaterial != Materials.Rubber) {
                if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY))
                        && (!aMaterial.contains(SubTag.NO_SMASHING))) {}
            }
        }
    }

    @Override
    public void registerOre(final GregtechOrePrefixes aPrefix, final GT_Materials aMaterial, final String aOreDictName,
            final String aModName, final ItemStack aStack) {
        // TODO Auto-generated method stub

    }

    public void materialsLoops() {
        final Materials[] i = Materials.values();
        final int size = i.length;
        Logger.MATERIALS("Materials to attempt tool gen. with: " + size);
        int used = 0;
        Materials aMaterial = null;
        for (Materials materials : i) {
            aMaterial = materials;
            if ((aMaterial != Materials.Stone) && (aMaterial != Materials.Flint)
                    && (aMaterial != Materials.Rubber)
                    && (aMaterial != Materials._NULL)) {
                if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY))
                        && (!aMaterial.contains(SubTag.NO_SMASHING))
                        && (!aMaterial.contains(SubTag.TRANSPARENT))
                        && (!aMaterial.contains(SubTag.FLAMMABLE))
                        && (!aMaterial.contains(SubTag.MAGICAL))
                        && (!aMaterial.contains(SubTag.NO_SMELTING))) {
                    Logger.MATERIALS("Generating Electric Snips from " + MaterialUtils.getMaterialName(aMaterial));
                    // Input 1

                    final ItemStack plate = GT_OreDictUnificator.get(OrePrefixes.plate, aMaterial, 1L);

                    if ((null != plate)) {
                        addRecipe(aMaterial, 1600000L, 3, ItemList.Battery_RE_HV_Lithium.get(1));
                        addRecipe(aMaterial, 1200000L, 3, ItemList.Battery_RE_HV_Cadmium.get(1));
                        addRecipe(aMaterial, 800000L, 3, ItemList.Battery_RE_HV_Sodium.get(1));
                        used++;
                    } else {
                        Logger.MATERIALS(
                                "Unable to generate Electric Snips from " + MaterialUtils.getMaterialName(aMaterial)
                                        + ", Plate or Long Rod may be invalid. Invalid | Plate? "
                                        + (plate == null)
                                        + " | Rod? "
                                        + " |");
                    }
                    // GT_ModHandler.addCraftingRecipe(,
                    // GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS |
                    // GT_ModHandler.RecipeBits.BUFFERED, new Object[]{"P H", "PIP", " I ",
                    // Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial),
                    // Character.valueOf('P'), OrePrefixes.plate.get(aMaterial),
                    // Character.valueOf('H'), OrePrefixes.toolHeadHammer.get(aMaterial)});
                } else {
                    Logger.MATERIALS(
                            "Unable to generate Electric Snips from " + MaterialUtils.getMaterialName(aMaterial));
                }
            } else {
                Logger.MATERIALS("Unable to generate Electric Snips from " + MaterialUtils.getMaterialName(aMaterial));
            }
        }

        Logger.INFO("Materials used for tool gen: " + used);
    }

    @Override
    public void run() {
        Logger.INFO("Generating Electric Snips for all valid GT Materials.");
        this.materialsLoops();
    }

    public boolean addRecipe(Materials aMaterial, long aBatteryStorage, int aVoltageTier, ItemStack aBattery) {

        ItemStack aOutputStack = MetaGeneratedGregtechTools.INSTANCE.getToolWithStats(
                MetaGeneratedGregtechTools.ELECTRIC_SNIPS,
                1,
                aMaterial,
                Materials.Titanium,
                new long[] { aBatteryStorage, GT_Values.V[aVoltageTier], 3L, -1L });

        ItemStack aInputCutter = GT_MetaGenerated_Tool_01.INSTANCE
                .getToolWithStats(GT_MetaGenerated_Tool_01.WIRECUTTER, 1, aMaterial, aMaterial, null);

        long aDura = MetaGeneratedGregtechTools.getToolMaxDamage(aOutputStack);
        if (aDura <= 32000) {
            Logger.MATERIALS(
                    "Unable to generate Electric Snips from " + MaterialUtils.getMaterialName(aMaterial)
                            + ", Durability: "
                            + aDura);
            return false;
        }
        return RecipeUtils.addShapedRecipe(
                OrePrefixes.wireFine.get(Materials.Electrum),
                aInputCutter,
                OrePrefixes.wireFine.get(Materials.Electrum),
                ELEMENT.STANDALONE.WHITE_METAL.getGear(1),
                CI.getElectricMotor(aVoltageTier, 1),
                ELEMENT.STANDALONE.WHITE_METAL.getGear(1),
                OrePrefixes.plate.get(aMaterial),
                aBattery,
                OrePrefixes.plate.get(aMaterial),
                aOutputStack);
    }
}
