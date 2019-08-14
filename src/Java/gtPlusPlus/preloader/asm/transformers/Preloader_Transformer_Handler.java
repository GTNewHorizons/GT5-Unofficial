package gtPlusPlus.preloader.asm.transformers;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

@SuppressWarnings("static-access")
public class Preloader_Transformer_Handler implements IClassTransformer {

	private final boolean mEnabled = false;
	public static final AsmConfig mConfig;	

	private static final String class_Block_Machines = "gregtech.common.blocks.GT_Block_Machines"; 
	private static final String class_GT_MetaPipeEntity_Item = "gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Item"; 
	private static final String class_GT_MetaPipeEntity_Frame = "gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Frame"; 
	private static final String class_GT_MetaPipeEntity_Fluid = "gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid"; 

	static {
		mConfig = new AsmConfig(new File("config/GTplusplus/asm.cfg"));
		System.out.println("[GT++ ASM] Asm Config Location: "+mConfig.config.getConfigFile().getAbsolutePath());
		System.out.println("[GT++ ASM] Is DevHelper Valid? "+gtPlusPlus.preloader.DevHelper.mIsValidHelper);
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
		// Is this environment obfuscated? (Extra checks just in case some weird shit happens during the check)
		boolean obfuscated = checkObfuscated();
		boolean probablyShouldBeFalse = false;

		//Enable mapping of Tickets and loaded chunks. - Forge
		if (transformedName.equals("net.minecraftforge.common.ForgeChunkManager") && mConfig.enableChunkDebugging) {	
			FMLRelaunchLog.log("[GT++ ASM] Chunkloading Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_Forge_ChunkLoading(basicClass, probablyShouldBeFalse).getWriter().toByteArray();
		}

		// Fix the OreDictionary - Forge
		if (transformedName.equals("net.minecraftforge.oredict.OreDictionary")) {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Transforming %s", transformedName);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
			return classWriter.toByteArray();
		}
		// Fix the OreDictionary COFH
		if (transformedName.equals("cofh.core.util.oredict.OreDictionaryArbiter") && (mConfig.enableCofhPatch || !obfuscated)) {
			FMLRelaunchLog.log("[GT++ ASM] COFH", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_COFH_OreDictionaryArbiter(basicClass).getWriter().toByteArray();
		}

		// Fix Tinkers Fluids
		if (transformedName.equals("tconstruct.smeltery.blocks.TConstructFluid") && mConfig.enableTiConFluidLighting) {
			FMLRelaunchLog.log("[GT++ ASM] Bright Fluids", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_TiConFluids("getLightValue", obfuscated, basicClass).getWriter().toByteArray();
		}
		
		//Fix RC stuff
		//Patching PROCESS_VOLUME to allow 4x more transfer limits
		if (transformedName.equals("mods.railcraft.common.fluids.FluidHelper") && mConfig.enableRcFlowFix) {	
			FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_Railcraft_FluidHelper(basicClass, obfuscated).getWriter().toByteArray();
		}
		//Fix Weird glitch involving negative itemstacks.
		if (transformedName.equals("mods.railcraft.common.util.inventory.InvTools")) {	
			FMLRelaunchLog.log("[GT++ ASM] Railcraft negative ItemStack Fix", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_Railcraft_InvTools(basicClass, obfuscated).getWriter().toByteArray();
		}

		//Fix GC stuff
		if (mConfig.enableGcFuelChanges) {
			if (transformedName.equals("micdoodle8.mods.galacticraft.core.util.FluidUtil")) {	
				FMLRelaunchLog.log("[GT++ ASM] Galacticraft FluidUtils Patch", Level.INFO, "Transforming %s", transformedName);
				return new ClassTransformer_GC_FluidUtil(basicClass, probablyShouldBeFalse).getWriter().toByteArray();
			}
			if (transformedName.equals("micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader")) {
				FMLRelaunchLog.log("[GT++ ASM] Galacticraft Fuel_Loader Patch", Level.INFO, "Transforming %s", transformedName);
				return new ClassTransformer_GC_FuelLoader(basicClass, probablyShouldBeFalse).getWriter().toByteArray();
			}
			if (transformedName.equals("micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket")) {
				FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Transforming %s", transformedName);
				return new ClassTransformer_GC_EntityAutoRocket(basicClass, probablyShouldBeFalse).getWriter().toByteArray();
			}
		}

		
		
		
		
		
		/**
		 * Gregtech ASM Patches
		 */
		
		//Make GT_Utilities safer		
		if (transformedName.equals("gtPlusPlus.preloader.asm.transformers.ClassTransformer_GT_Utility")) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Utilities Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_Utility(basicClass, transformedName).getWriter().toByteArray();
		}
		//Inject Custom constructors for Busses		
		if (transformedName.equals(ClassTransformer_GT_BusPatch.aInput) || transformedName.equals(ClassTransformer_GT_BusPatch.aOutput) || transformedName.equals(ClassTransformer_GT_BusPatch.aSuperInput) || transformedName.equals(ClassTransformer_GT_BusPatch.aSuperOutput)) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_BusPatch(basicClass, transformedName).getWriter().toByteArray();
		}
		//Try patch achievements
		if (transformedName.equals("gregtech.loaders.misc.GT_Achievements")) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_Achievements_CrashFix(basicClass, obfuscated).getWriter().toByteArray();
		}
		
		//Fix bad handling of a loop left from original decompilation
		//Also Fix Achievements, although currently disabled.
		if (transformedName.equals("gregtech.common.GT_Client")) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_Client(basicClass, obfuscated).getByteArray();
		}

		//Make GT packets safer, fill them with debug info.
		if (transformedName.equals("gregtech.api.net.GT_Packet_TileEntity")) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech GT_Packet_TileEntity Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_Packet_TileEntity(basicClass, obfuscated).getWriter().toByteArray();
		}
		//Make the setting of GT Tiles safer, so as not to crash the client.
		if (transformedName.equals("gregtech.api.metatileentity.BaseMetaTileEntity")) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech setMetaTileEntity Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_BaseMetaTileEntity(basicClass).getWriter().toByteArray();
		}
		
		
		
		
		//Patching Meta Tile Tooltips
		if (transformedName.equals("gregtech.common.blocks.GT_Item_Machines") && mConfig.enableGtTooltipFix) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Tooltip Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_ItemMachines_Tooltip(basicClass, probablyShouldBeFalse).getWriter().toByteArray();
		}


		if (transformedName.equals(class_Block_Machines)) {
			//Fix GT NBT Persistency issue
			FMLRelaunchLog.log("[GT++ ASM] Gregtech NBT Persistency Patch", Level.INFO, "Transforming %s", transformedName);
			byte[] g =  new ClassTransformer_GT_BlockMachines_NBT(basicClass, probablyShouldBeFalse).getWriter().toByteArray();			
			FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_BlockMachines_MetaPipeEntity(g, probablyShouldBeFalse, 0).getWriter().toByteArray();			
		}		
		if (transformedName.equals(class_GT_MetaPipeEntity_Item) || transformedName.equals(class_GT_MetaPipeEntity_Frame) || transformedName.equals(class_GT_MetaPipeEntity_Fluid)) {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Transforming %s", transformedName);
			int mode = 0;
			if (transformedName.equals(class_GT_MetaPipeEntity_Item)) {
				mode = 1;
			}
			else if (transformedName.equals(class_GT_MetaPipeEntity_Frame)) {
				mode = 2;
			}
			else {
				mode = 3;
			}			
			return new ClassTransformer_GT_BlockMachines_MetaPipeEntity(basicClass, probablyShouldBeFalse, mode).getWriter().toByteArray();
		}
			
		String[] aIC2ClassNames = new String[] {
				"ic2.core.block.BlockTileEntity",
				"ic2.core.block.machine.BlockMachine",
				"ic2.core.block.machine.BlockMachine2",
				"ic2.core.block.machine.BlockMachine3",
				"ic2.core.block.kineticgenerator.block.BlockKineticGenerator",
				"ic2.core.block.heatgenerator.block.BlockHeatGenerator",
				"ic2.core.block.generator.block.BlockGenerator",
				"ic2.core.block.reactor.block.BlockReactorAccessHatch",
				"ic2.core.block.reactor.block.BlockReactorChamber",
				"ic2.core.block.reactor.block.BlockReactorFluidPort",
				"ic2.core.block.reactor.block.BlockReactorRedstonePort", 
				"ic2.core.block.reactor.block.BlockReactorVessel",
				"ic2.core.block.personal.BlockPersonal.class",
				"ic2.core.block.wiring.BlockChargepad.class",
				"ic2.core.block.wiring.BlockElectric.class",
				"ic2.core.block.wiring.BlockLuminator.class",				
		};

		//Fix IC2 Shit	
		for (String y : aIC2ClassNames) {
			if (transformedName.equals(y)) {
				FMLRelaunchLog.log("[GT++ ASM] IC2 getHarvestTool Patch", Level.INFO, "Transforming %s", transformedName);
				return new ClassTransformer_IC2_GetHarvestTool(basicClass, obfuscated, transformedName).getWriter().toByteArray();			
			}
		}
		
		//Fix IC2 Hazmat		
		if (transformedName.equals("ic2.core.item.armor.ItemArmorHazmat")) {
			FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_IC2_Hazmat(basicClass, transformedName).getWriter().toByteArray();			
		}
		
		//Fix Thaumcraft Shit
		//Patching ItemWispEssence to allow invalid item handling
		if (transformedName.equals("thaumcraft.common.items.ItemWispEssence") && mConfig.enableTcAspectSafety) {	
			FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_TC_ItemWispEssence(basicClass, obfuscated).getWriter().toByteArray();
		}
		
		
		return basicClass;
	}

	
	
}
