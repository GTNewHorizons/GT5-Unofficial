package gregtech.loaders.oreprocessing;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.util.GTModHandler;
import gregtech.common.items.tools.GTToolItems;
import gregtech.common.items.tools.ToolSolderingIronItem;

public class ProcessingToolOther implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingToolOther() {
        OrePrefixes.toolHeadHammer.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if ((aMaterial == Materials.Stone) || (aMaterial == Materials.Flint)) {
            return;
        }

        if (aMaterial != Materials.Rubber) {
            // Crafting recipes
            {
                ItemStack tPlunger = GTToolItems.PLUNGER.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ITER, 2L));
                if (tPlunger != null) {
                    GTModHandler.addCraftingRecipe(
                        tPlunger,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "xRR", " SR", "S f", 'S', OrePrefixes.stick.get(aMaterial), 'R',
                            OrePrefixes.plate.get(Materials.AnyRubber) });
                }
            }
        }

        if ((!aMaterial.contains(SubTag.WOOD)) && (!aMaterial.contains(SubTag.BOUNCY))
            && (!aMaterial.contains(SubTag.NO_SMASHING))) {
            // Crafting recipes
            {
                ItemStack tWrench = GTToolItems.WRENCH.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                if (tWrench != null) {
                    GTModHandler.addCraftingRecipe(
                        tWrench,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "IhI", "III", " I ", 'I', OrePrefixes.ingot.get(aMaterial) });
                }
                ItemStack tCrowbar = GTToolItems.CROWBAR.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L));
                if (tCrowbar != null) {
                    GTModHandler.addCraftingRecipe(
                        tCrowbar,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "hDS", "DSD", "SDf", 'S', OrePrefixes.stick.get(aMaterial), 'D', Dyes.dyeBlue });
                }
                ItemStack tScrewdriver = GTToolItems.SCREWDRIVER.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                if (tScrewdriver != null) {
                    GTModHandler.addCraftingRecipe(
                        tScrewdriver,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { " fS", " Sh", "W  ", 'S', OrePrefixes.stick.get(aMaterial), 'W',
                            OrePrefixes.stick.get(aMaterial.mHandleMaterial) });
                }
                ItemStack tWireCutter = GTToolItems.WIRE_CUTTER.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                if (tWireCutter != null) {
                    GTModHandler.addCraftingRecipe(
                        tWireCutter,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PfP", "hPd", "STS", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                            OrePrefixes.plate.get(aMaterial), 'T', OrePrefixes.screw.get(aMaterial) });
                }
                ItemStack tScoop = GTToolItems.SCOOP.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.BESTIA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.PANNUS, 2L));
                if (tScoop != null) {
                    GTModHandler.addCraftingRecipe(
                        tScoop,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "SWS", "SSS", "xSh", 'S', OrePrefixes.stick.get(aMaterial), 'W',
                            new ItemStack(Blocks.wool, 1, 32767) });
                }
                ItemStack tBranchCutter = GTToolItems.BRANCH_CUTTER.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L));
                if (tBranchCutter != null) {
                    GTModHandler.addCraftingRecipe(
                        tBranchCutter,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PfP", "PdP", "STS", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                            OrePrefixes.plate.get(aMaterial), 'T', OrePrefixes.screw.get(aMaterial) });
                }
                ItemStack tKnife = GTToolItems.KNIFE.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L));
                if (tKnife != null) {
                    GTModHandler.addCraftingRecipe(
                        tKnife,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "fPh", " S ", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                            OrePrefixes.plate.get(aMaterial) });
                }
                ItemStack tButcheryKnife = GTToolItems.BUTCHERY_KNIFE.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.CORPUS, 4L));
                if (tButcheryKnife != null) {
                    GTModHandler.addCraftingRecipe(
                        tButcheryKnife,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "PPf", "PP ", "Sh ", 'S', OrePrefixes.stick.get(aMaterial), 'P',
                            OrePrefixes.plate.get(aMaterial) });
                }
                // Electric soldering irons.
                GTToolItems.SOLDERING_IRON_LV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.SOLDERING_IRON_MV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                GTToolItems.SOLDERING_IRON_HV.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                    new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_LV,
                    aMaterial,
                    Materials.AnyRubber,
                    Materials.Iron,
                    ItemList.Battery_RE_LV_Lithium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_LV,
                    aMaterial,
                    Materials.AnyRubber,
                    Materials.Iron,
                    ItemList.Battery_RE_LV_Cadmium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_LV,
                    aMaterial,
                    Materials.AnyRubber,
                    Materials.Iron,
                    ItemList.Battery_RE_LV_Sodium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_MV,
                    aMaterial,
                    Materials.AnyRubber,
                    Materials.Steel,
                    ItemList.Battery_RE_MV_Lithium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_MV,
                    aMaterial,
                    Materials.AnyRubber,
                    Materials.Steel,
                    ItemList.Battery_RE_MV_Cadmium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_MV,
                    aMaterial,
                    Materials.AnyRubber,
                    Materials.Steel,
                    ItemList.Battery_RE_MV_Sodium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_HV,
                    aMaterial,
                    Materials.AnySyntheticRubber,
                    Materials.StainlessSteel,
                    ItemList.Battery_RE_HV_Lithium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_HV,
                    aMaterial,
                    Materials.AnySyntheticRubber,
                    Materials.StainlessSteel,
                    ItemList.Battery_RE_HV_Cadmium);
                addSolderingIronRecipe(
                    GTToolItems.SOLDERING_IRON_HV,
                    aMaterial,
                    Materials.AnySyntheticRubber,
                    Materials.StainlessSteel,
                    ItemList.Battery_RE_HV_Sodium);

                ItemStack tTrowel = GTToolItems.TROWEL.registerMaterial(
                    aMaterial,
                    new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 8),
                    new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4),
                    new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2));
                if (tTrowel != null) {
                    GTModHandler.addCraftingRecipe(
                        tTrowel,
                        GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
                        new Object[] { "  d", "SSC", "fPP", 'S', OrePrefixes.stick.get(aMaterial.mHandleMaterial), 'C',
                            OrePrefixes.screw.get(aMaterial), 'P', OrePrefixes.plate.get(aMaterial) });
                }

            }
        }
    }

    /**
     * Adds one electric soldering iron crafting recipe. The head material only supplies the bolt; the casing rubber
     * and the handle metal are fixed per tier.
     */
    private static void addSolderingIronRecipe(ToolSolderingIronItem solderingIronItem, Materials headMaterial,
        Materials rubber, Materials handleMaterial, ItemList battery) {
        ItemStack solderingIron = solderingIronItem.getToolWithMaterial(headMaterial);
        if (solderingIron == null) return;
        GTModHandler.addCraftingRecipe(
            solderingIron,
            GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "LBf", "Sd ", "P  ", 'B', OrePrefixes.bolt.get(headMaterial), 'P',
                OrePrefixes.plate.get(rubber), 'S', OrePrefixes.stick.get(handleMaterial), 'L', battery.get(1L) });
    }
}
