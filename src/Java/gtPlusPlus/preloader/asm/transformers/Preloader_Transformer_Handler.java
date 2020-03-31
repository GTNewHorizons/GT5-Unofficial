package gtPlusPlus.preloader.asm.transformers;

import static gtPlusPlus.preloader.asm.ClassesToTransform.COFH_ORE_DICTIONARY_ARBITER;
import static gtPlusPlus.preloader.asm.ClassesToTransform.FORGE_CHUNK_MANAGER;
import static gtPlusPlus.preloader.asm.ClassesToTransform.FORGE_ORE_DICTIONARY;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GALACTICRAFT_ENTITY_AUTO_ROCKET;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GALACTICRAFT_FLUID_UTILS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GALACTICRAFT_TILE_ENTITY_FUEL_LOADER;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GTPP_MTE_HATCH_SUPER_INPUT_BUS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GTPP_MTE_HATCH_SUPER_OUTPUT_BUS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_ACHIEVEMENTS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_BASE_META_TILE_ENTITY;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_BLOCK_MACHINES;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_CLIENT_PROXY;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_ITEM_MACHINES;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_METAGENERATED_TOOL;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_METAPIPE_FLUID;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_METAPIPE_FRAME;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_METAPIPE_ITEM;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_MTE_CHARCOAL_PIT;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_MTE_HATCH_INPUTBUS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_MTE_HATCH_OUTPUTBUS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_PACKET_TILE_ENTITY;
import static gtPlusPlus.preloader.asm.ClassesToTransform.GT_UTILITY;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_BASE_TILE_ENTITY;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_CHARGEPAD;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_ELECTRIC;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_GENERATOR;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_HEAT_GENERATOR;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_KINETIC_GENERATOR;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_LUMINATOR;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_MACHINE1;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_MACHINE2;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_MACHINE3;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_PERSONAL;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_REACTOR_ACCESS_HATCH;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_REACTOR_CHAMBER;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_REACTOR_FLUID_PORT;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_REACTOR_REDSTONE_PORT;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_BLOCK_REACTOR_VESSEL;
import static gtPlusPlus.preloader.asm.ClassesToTransform.IC2_ITEM_ARMOUR_HAZMAT;
import static gtPlusPlus.preloader.asm.ClassesToTransform.LWJGL_KEYBOARD;
import static gtPlusPlus.preloader.asm.ClassesToTransform.MINECRAFT_GAMESETTINGS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.MINECRAFT_GAMESETTINGS_OBF;
import static gtPlusPlus.preloader.asm.ClassesToTransform.RAILCRAFT_FLUID_HELPER;
import static gtPlusPlus.preloader.asm.ClassesToTransform.RAILCRAFT_INVENTORY_TOOLS;
import static gtPlusPlus.preloader.asm.ClassesToTransform.RAILCRAFT_TILE_FLUID_LOADER;
import static gtPlusPlus.preloader.asm.ClassesToTransform.THAUMCRAFT_ITEM_WISP_ESSENCE;
import static gtPlusPlus.preloader.asm.ClassesToTransform.TINKERS_FLUID_BLOCK;

import java.io.File;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.DevHelper;
import gtPlusPlus.preloader.Preloader_Logger;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

public class Preloader_Transformer_Handler implements IClassTransformer {

	public static final AsmConfig mConfig;	
	public static final AutoMap<String> IC2_WRENCH_PATCH_CLASS_NAMES = new AutoMap<String>();

	static {
		mConfig = new AsmConfig(new File("config/GTplusplus/asm.cfg"));
		Preloader_Logger.INFO("Config Location: "+AsmConfig.config.getConfigFile().getAbsolutePath());
		Preloader_Logger.INFO("Is DevHelper Valid? "+DevHelper.mIsValidHelper);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_BASE_TILE_ENTITY);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_MACHINE1);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_MACHINE2);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_MACHINE3);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_KINETIC_GENERATOR);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_HEAT_GENERATOR);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_GENERATOR);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_REACTOR_ACCESS_HATCH);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_REACTOR_CHAMBER);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_REACTOR_FLUID_PORT);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_REACTOR_REDSTONE_PORT);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_REACTOR_VESSEL);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_PERSONAL);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_CHARGEPAD);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_ELECTRIC);
		IC2_WRENCH_PATCH_CLASS_NAMES.add(IC2_BLOCK_LUMINATOR);
	}

	private static Boolean mObf = null;

	public boolean checkObfuscated() {
		if (mObf != null) {
			return mObf;
		}		
		boolean obfuscated = false;
		try {
			obfuscated = !(boolean) ReflectionHelper.findField(CoreModManager.class, "deobfuscatedEnvironment").get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			byte[] bs;
			try {
				bs = Launch.classLoader.getClassBytes("net.minecraft.world.World");
				if (bs != null) {
					obfuscated = false;
				} else {
					obfuscated = true;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				obfuscated = false;
			}
		}	
		mObf = obfuscated;
		return obfuscated;
	}

	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		/*
		 * Here we patch all instances of entity.setHealth and replace them with a static function.
		 * Only EntityLivingBase is left untouched, as nothing else truly needs to be calling this method and avoiding forge hooks.
		 * May exclude all vanilla/forge class if this causes issues though.
		 */
		/*		PatchForge : if (AsmConfig.enabledFixEntitySetHealth && !transformedName.contains("gtPlusPlus.preloader")) {
					
					//Skip Vanilla/Forge Classes
					if (transformedName.contains("net.minecraft.") || transformedName.contains("cpw.")) {
						//break PatchForge;
					}
					
					ClassTransformer_Forge_EntityLivingBase_SetHealth aForgeHealthFix = new ClassTransformer_Forge_EntityLivingBase_SetHealth(transformedName, basicClass);		
					if (aForgeHealthFix.isValidTransformer() && aForgeHealthFix.didPatchClass()) {
						Preloader_Logger.INFO("Fix EntityLivingBase.setHealth misuse", "Transforming "+transformedName);				
						basicClass = aForgeHealthFix.getWriter().toByteArray();
					}
				}*/

		// Is this environment obfuscated? (Extra checks just in case some weird shit happens during the check)
		final boolean obfuscated = checkObfuscated();

		// Fix LWJGL index array out of bounds on keybinding IDs
		if ((transformedName.equals(LWJGL_KEYBOARD) || transformedName.equals(MINECRAFT_GAMESETTINGS_OBF) || transformedName.equals(MINECRAFT_GAMESETTINGS)) && AsmConfig.enabledLwjglKeybindingFix) {	
			boolean isClientSettingsClass = false;
			if (!transformedName.equals("org.lwjgl.input.Keyboard")) {
				isClientSettingsClass = true;
			}			
			Preloader_Logger.INFO("LWJGL Keybinding index out of bounds fix", "Transforming "+transformedName);
			return new ClassTransformer_LWJGL_Keyboard(basicClass, isClientSettingsClass).getWriter().toByteArray();
		}		

		//Enable mapping of Tickets and loaded chunks. - Forge
		if (transformedName.equals(FORGE_CHUNK_MANAGER) && AsmConfig.enableChunkDebugging) {	
			Preloader_Logger.INFO("Chunkloading Patch", "Transforming "+transformedName);
			return new ClassTransformer_Forge_ChunkLoading(basicClass, false).getWriter().toByteArray();
		}

		// Fix the OreDictionary - Forge
		if (transformedName.equals(FORGE_ORE_DICTIONARY) && AsmConfig.enableOreDictPatch) {
			Preloader_Logger.INFO("OreDictTransformer", "Transforming "+transformedName);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
			return classWriter.toByteArray();
		}
		// Fix the OreDictionary COFH
		if (transformedName.equals(COFH_ORE_DICTIONARY_ARBITER) && (AsmConfig.enableCofhPatch || !obfuscated)) {
			Preloader_Logger.INFO("COFH", "Transforming "+transformedName);
			return new ClassTransformer_COFH_OreDictionaryArbiter(basicClass).getWriter().toByteArray();
		}

		// Fix Tinkers Fluids
		if (transformedName.equals(TINKERS_FLUID_BLOCK) && AsmConfig.enableTiConFluidLighting) {
			Preloader_Logger.INFO("Bright Fluids", "Transforming "+transformedName);
			return new ClassTransformer_TiConFluids("getLightValue", obfuscated, basicClass).getWriter().toByteArray();
		}

		//Fix RC stuff
		//Patching PROCESS_VOLUME to allow more transfer limits
		if (transformedName.equals(RAILCRAFT_FLUID_HELPER) && (AsmConfig.enableRcFlowFix && AsmConfig.maxRailcraftTankProcessVolume != 4000)) {	
			Preloader_Logger.INFO("Railcraft PROCESS_VOLUME Patch", "Transforming "+transformedName);
			return new ClassTransformer_Railcraft_FluidHelper(basicClass, obfuscated).getWriter().toByteArray();
		}		
		//Patching TRANSFER_RATE in Fluid Loaders/Unloaders
		if ((transformedName.equals(RAILCRAFT_TILE_FLUID_LOADER) && AsmConfig.maxRailcraftFluidLoaderFlow != 20) || (transformedName.equals("mods.railcraft.common.blocks.machine.gamma.TileFluidUnloader") && AsmConfig.maxRailcraftFluidUnloaderFlow != 80)) {	
			Preloader_Logger.INFO("Railcraft TRANSFER_RATE Patch", "Transforming "+transformedName);
			return new ClassTransformer_Railcraft_FluidCartHandling(basicClass, obfuscated, transformedName).getWriter().toByteArray();
		}		
		//Fix Weird glitch involving negative itemstacks.
		if (transformedName.equals(RAILCRAFT_INVENTORY_TOOLS) && AsmConfig.enableRcItemDupeFix) {	
			Preloader_Logger.INFO("Railcraft negative ItemStack Fix", "Transforming "+transformedName);
			return new ClassTransformer_Railcraft_InvTools(basicClass, obfuscated).getWriter().toByteArray();
		}

		//Fix GC stuff
		if (AsmConfig.enableGcFuelChanges) {
			if (transformedName.equals(GALACTICRAFT_FLUID_UTILS)) {	
				Preloader_Logger.INFO("Galacticraft FluidUtils Patch", "Transforming "+transformedName);
				return new ClassTransformer_GC_FluidUtil(basicClass, false).getWriter().toByteArray();
			}
			if (transformedName.equals(GALACTICRAFT_TILE_ENTITY_FUEL_LOADER)) {
				Preloader_Logger.INFO("Galacticraft Fuel_Loader Patch", "Transforming "+transformedName);
				return new ClassTransformer_GC_FuelLoader(basicClass, false).getWriter().toByteArray();
			}
			if (transformedName.equals(GALACTICRAFT_ENTITY_AUTO_ROCKET)) {
				Preloader_Logger.INFO("Galacticraft EntityAutoRocket Patch", "Transforming "+transformedName);
				return new ClassTransformer_GC_EntityAutoRocket(basicClass, false).getWriter().toByteArray();
			}
		}






		/**
		 * Gregtech ASM Patches
		 */

		//Make GT_Utilities safer		
		if (transformedName.equals(GT_UTILITY)) {	
			Preloader_Logger.INFO("Gregtech Utilities Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_Utility(basicClass, transformedName).getWriter().toByteArray();
		}
		//Inject Custom constructors for Busses		
		if (transformedName.equals(GT_MTE_HATCH_INPUTBUS) || transformedName.equals(GT_MTE_HATCH_OUTPUTBUS) || transformedName.equals(GTPP_MTE_HATCH_SUPER_INPUT_BUS) || transformedName.equals(GTPP_MTE_HATCH_SUPER_OUTPUT_BUS)) {	
			Preloader_Logger.INFO("Gregtech Bus Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_BusPatch(basicClass, transformedName).getWriter().toByteArray();
		}
		//Try patch achievements
		if (transformedName.equals(GT_ACHIEVEMENTS)) {	
			Preloader_Logger.INFO("Gregtech Achievements Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_Achievements_CrashFix(basicClass, obfuscated).getWriter().toByteArray();
		}

		//Fix bad handling of a loop left from original decompilation
		//Also Fix Achievements, although currently disabled.
		if (transformedName.equals(GT_CLIENT_PROXY)) {	
			Preloader_Logger.INFO("Gregtech Client Proxy Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_Client(basicClass, obfuscated).getByteArray();
		}

		//Make GT packets safer, fill them with debug info.
		if (transformedName.equals(GT_PACKET_TILE_ENTITY)) {	
			Preloader_Logger.INFO("Gregtech GT_Packet_TileEntity Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_Packet_TileEntity(basicClass, obfuscated).getWriter().toByteArray();
		}
		//Make the setting of GT Tiles safer, so as not to crash the client.
		if (transformedName.equals(GT_BASE_META_TILE_ENTITY)) {	
			Preloader_Logger.INFO("Gregtech setMetaTileEntity Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_BaseMetaTileEntity(basicClass).getWriter().toByteArray();
		}
		//Add extra tools if we're in a dev environment.
		if (transformedName.equals(GT_METAGENERATED_TOOL) && CORE_Preloader.DEV_ENVIRONMENT) {	
			Preloader_Logger.INFO("Gregtech Additional Tools Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_MetaGenerated_Tool(basicClass).getWriter().toByteArray();
		}		
		//Fix log handling on the charcoal pit
		if (transformedName.equals(GT_MTE_CHARCOAL_PIT) && AsmConfig.enableGtCharcoalPitFix) {	
			Preloader_Logger.INFO("GT Charcoal Pit Fix", "Transforming "+transformedName);
			return new ClassTransformer_GT_CharcoalPit(basicClass, obfuscated).getWriter().toByteArray();
		}




		//Patching Meta Tile Tooltips
		if (transformedName.equals(GT_ITEM_MACHINES) && AsmConfig.enableGtTooltipFix) {	
			Preloader_Logger.INFO("Gregtech Tooltip Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_ItemMachines_Tooltip(basicClass, false).getWriter().toByteArray();
		}


		if (transformedName.equals(GT_BLOCK_MACHINES)) {
			//Fix GT NBT Persistency issue
			Preloader_Logger.INFO("Gregtech NBT Persistency Patch", "Transforming "+transformedName);
			byte[] g =  new ClassTransformer_GT_BlockMachines_NBT(basicClass, false).getWriter().toByteArray();			
			Preloader_Logger.INFO("Gregtech getTileEntityBaseType Patch", "Transforming "+transformedName);
			return new ClassTransformer_GT_BlockMachines_MetaPipeEntity(g, 0).getWriter().toByteArray();			
		}		
		if (transformedName.equals(GT_METAPIPE_ITEM) || transformedName.equals(GT_METAPIPE_FRAME) || transformedName.equals(GT_METAPIPE_FLUID)) {
			Preloader_Logger.INFO("Gregtech getTileEntityBaseType Patch", "Transforming "+transformedName);
			int mode = 0;
			if (transformedName.equals(GT_METAPIPE_ITEM)) {
				mode = 1;
			}
			else if (transformedName.equals(GT_METAPIPE_FRAME)) {
				mode = 2;
			}
			else {
				mode = 3;
			}			
			return new ClassTransformer_GT_BlockMachines_MetaPipeEntity(basicClass, mode).getWriter().toByteArray();
		}


		//Fix IC2 Wrench Harvesting	
		for (String y : IC2_WRENCH_PATCH_CLASS_NAMES) {
			if (transformedName.equals(y)) {
				Preloader_Logger.INFO("IC2 getHarvestTool Patch", "Transforming "+transformedName);
				return new ClassTransformer_IC2_GetHarvestTool(basicClass, obfuscated, transformedName).getWriter().toByteArray();			
			}
		}

		//Fix IC2 Hazmat		
		if (transformedName.equals(IC2_ITEM_ARMOUR_HAZMAT)) {
			Preloader_Logger.INFO("IC2 Hazmat Patch", "Transforming "+transformedName);
			return new ClassTransformer_IC2_Hazmat(basicClass, transformedName).getWriter().toByteArray();			
		}

		//Fix Thaumcraft Shit
		//Patching ItemWispEssence to allow invalid item handling
		if (transformedName.equals(THAUMCRAFT_ITEM_WISP_ESSENCE) && AsmConfig.enableTcAspectSafety) {	
			Preloader_Logger.INFO("Thaumcraft WispEssence_Patch", "Transforming "+transformedName);
			return new ClassTransformer_TC_ItemWispEssence(basicClass, obfuscated).getWriter().toByteArray();
		}


		return basicClass;
	}



}
