package gtPlusPlus.xmod.forestry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import binnie.extratrees.genetics.ExtraTreeSpecies;
import cpw.mods.fml.common.Optional;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.arboriculture.EnumWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.arboriculture.genetics.TreeDefinition;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import gtPlusPlus.xmod.forestry.bees.recipe.FR_Gregtech_Recipes;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntityTreeFarm;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HANDLER_FR {

	public static void preInit(){
		if (LoadedMods.Forestry){
			FR_ItemRegistry.Register();
		}		
	}

	public static void Init(){
		if (LoadedMods.Forestry){
		}
	}

	public static void postInit(){
		if (LoadedMods.Forestry){
			FR_Gregtech_Recipes.registerItems();
			new GTPP_Bees();
			mapForestrySaplingToLog();
		}

		if (LoadedMods.ExtraTrees){
			mapExtraTreesSaplingToLog();
		}
	}

	public static boolean createBlockBreakParticles(final World world, final int x, final int y, final int z, final Block block){
		if (LoadedMods.Forestry){
			createBlockBreakParticles_INTERNAL(world, x, y, z, block);
		}
		return false;
	}

	@Optional.Method(modid = "Forestry")
	private static void createBlockBreakParticles_INTERNAL(final World world, final int x, final int y, final int z, final Block block){
		if (LoadedMods.Forestry){
			Class oClass;
			try {
				oClass = ReflectionUtils.getClass("forestry.core.proxy.ProxyCommon");
				Object oProxy = ReflectionUtils.getField(oClass, "common");				
				if (oProxy != null && oClass.isInstance(oProxy)){					
					Method mParticles = ReflectionUtils.getMethod(oClass, "addBlockDestroyEffects", World.class, int.class, int.class, int.class, Block.class, int.class);					
					mParticles.invoke(oProxy, world, x, y, z, block, 0);				
				}
			}
			catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			}
		}		
	}

	@Optional.Method(modid = "Forestry")
	private static void mapForestrySaplingToLog() {
		for (TreeDefinition value : TreeDefinition.values()) {
			ItemStack aSaplingStack = value.getMemberStack(EnumGermlingType.SAPLING);
			EnumWoodType woodType = ReflectionUtils.getField(value, "woodType");
			ItemStack aLog;
			if (woodType != null) {
				aLog = TreeManager.woodItemAccess.getLog(woodType, false);

				GregtechMetaTileEntityTreeFarm.sLogCache.put(value.getUID(), aLog);
				GregtechMetaTileEntityTreeFarm.sLogCache.put(value.getUID() + "fireproof", TreeManager.woodItemAccess.getLog(woodType, true));
			} else {
				aLog = ReflectionUtils.getField(value, "vanillaWood");

				GregtechMetaTileEntityTreeFarm.sLogCache.put(value.getUID(), ReflectionUtils.getField(value, "vanillaWood"));
			}

			GregtechMetaTileEntityTreeFarm.addFakeRecipeToNEI(aSaplingStack, aLog);
		}
	}

	@Optional.Method(modid = "ExtraTrees")
	private static void mapExtraTreesSaplingToLog() {
		for (ExtraTreeSpecies value : ExtraTreeSpecies.values()) {
			ItemStack aSaplingStack = TreeManager.treeRoot.getMemberStack(TreeManager.treeRoot.templateAsIndividual(value.getTemplate()), 0);
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
