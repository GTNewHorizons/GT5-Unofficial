package gtPlusPlus.xmod.forestry;

import static gregtech.api.enums.Mods.ExtraTrees;
import static gregtech.api.enums.Mods.Forestry;

import net.minecraft.item.ItemStack;

import binnie.extratrees.genetics.ExtraTreeSpecies;
import cpw.mods.fml.common.Optional;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.EnumWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.arboriculture.genetics.TreeDefinition;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm;

public class HANDLER_FR {

    public static void preInit() {
        if (Forestry.isModLoaded()) {
            FR_ItemRegistry.Register();
        }
    }

    public static void postInit() {
        if (Forestry.isModLoaded()) {
            FR_Gregtech_Recipes.registerItems();
            new GTPP_Bees();
            mapForestrySaplingToLog();
        }

        if (ExtraTrees.isModLoaded()) {
            mapExtraTreesSaplingToLog();
        }
    }

    @Optional.Method(modid = Mods.Names.FORESTRY)
    private static void mapForestrySaplingToLog() {
        for (TreeDefinition value : TreeDefinition.values()) {
            ItemStack aSaplingStack = value.getMemberStack(EnumGermlingType.SAPLING);
            EnumWoodType woodType = ReflectionUtils.getField(value, "woodType");
            ItemStack aLog;
            if (woodType != null) {
                aLog = TreeManager.woodItemAccess.getLog(woodType, false);

                GregtechMetaTileEntityTreeFarm.sLogCache.put(value.getUID(), aLog);
                GregtechMetaTileEntityTreeFarm.sLogCache
                        .put(value.getUID() + "fireproof", TreeManager.woodItemAccess.getLog(woodType, true));
            } else {
                aLog = ReflectionUtils.getField(value, "vanillaWood");

                GregtechMetaTileEntityTreeFarm.sLogCache
                        .put(value.getUID(), ReflectionUtils.getField(value, "vanillaWood"));
            }

            GregtechMetaTileEntityTreeFarm.addFakeRecipeToNEI(aSaplingStack, aLog);
        }
    }

    @Optional.Method(modid = Mods.Names.EXTRA_TREES)
    private static void mapExtraTreesSaplingToLog() {
        for (ExtraTreeSpecies value : ExtraTreeSpecies.values()) {
            ItemStack aSaplingStack = TreeManager.treeRoot
                    .getMemberStack(TreeManager.treeRoot.templateAsIndividual(value.getTemplate()), 0);
            ItemStack aLog = null;
            if (value.getLog() != null) {
                aLog = value.getLog().getItemStack();

                GregtechMetaTileEntityTreeFarm.sLogCache.put(value.getUID(), aLog);
                GregtechMetaTileEntityTreeFarm.sLogCache.put(value.getUID() + "fireproof", aLog);
            }

            GregtechMetaTileEntityTreeFarm.addFakeRecipeToNEI(aSaplingStack, aLog);
        }
    }
}
