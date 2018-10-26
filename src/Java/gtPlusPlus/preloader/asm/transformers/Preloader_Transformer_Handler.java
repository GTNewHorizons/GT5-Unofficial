package gtPlusPlus.preloader.asm.transformers;

import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.ReflectionHelper;
import galaxyspace.SolarSystem.core.configs.GSConfigDimensions;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;

public class Preloader_Transformer_Handler implements IClassTransformer {

	private final boolean mEnabled = false;
	public static final AsmConfig mConfig;
	
	static {
		mConfig = new AsmConfig(new File("config/GTplusplus/asm.cfg"));
		System.out.println("[GT++ ASM] Asm Config Location: "+mConfig.config.getConfigFile().getAbsolutePath());
		System.out.println("[GT++ ASM] Is DevHelper Valid? "+gtPlusPlus.preloader.DevHelper.mIsValidHelper);
	}

	public byte[] transform(String name, String transformedName, byte[] basicClass) {


		// Is this environment obfuscated? (Extra checks just in case some weird shit happens during the check)
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

		//Enable mapping of Tickets and loaded chunks. - Forge
		if (transformedName.equals("net.minecraftforge.common.ForgeChunkManager") && mConfig.enableChunkDebugging) {	
			FMLRelaunchLog.log("[GT++ ASM] Chunkloading Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_Forge_ChunkLoading(basicClass, obfuscated).getWriter().toByteArray();
		}

		// Fix the OreDictionary - Forge
		if (transformedName.equals("net.minecraftforge.oredict.OreDictionary")) {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Transforming %s", transformedName);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
			return classWriter.toByteArray();
		}
		// Fix the OreDictionary COFH
		if (transformedName.equals("cofh.core.util.oredict.OreDictionaryArbiter") && mConfig.enableCofhPatch) {
			FMLRelaunchLog.log("[GT++ ASM] COFH", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_COFH_OreDictionaryArbiter(basicClass, obfuscated).getWriter().toByteArray();
		}

		// Fix Tinkers Fluids
		if (transformedName.equals("tconstruct.smeltery.blocks.TConstructFluid") && mConfig.enableTiConFluidLighting) {
			FMLRelaunchLog.log("[GT++ ASM] Bright Fluids", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_TiConFluids("getLightValue", obfuscated, basicClass).getWriter().toByteArray();
		}

		//Fix GC stuff
		if (transformedName.equals("micdoodle8.mods.galacticraft.core.util.FluidUtil")) {	
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft FluidUtils Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GC_FluidUtil(basicClass, obfuscated).getWriter().toByteArray();
		}
		if (transformedName.equals("micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader")) {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft Fuel_Loader Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GC_FuelLoader(basicClass, obfuscated).getWriter().toByteArray();
		}
		if (transformedName.equals("micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket")) {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GC_EntityAutoRocket(basicClass, obfuscated).getWriter().toByteArray();
		}

		//Fix GT NBT Persistency issue
		if (transformedName.equals("gregtech.common.blocks.GT_Block_Machines") && mConfig.enableGtNbtFix) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech NBT Persistency Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_BlockMachines_NBT(basicClass, obfuscated).getWriter().toByteArray();
		}
		//Patching Meta Tile Tooltips
		if (transformedName.equals("gregtech.common.blocks.GT_Item_Machines") && mConfig.enableGtTooltipFix) {	
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Tooltip Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GT_ItemMachines_Tooltip(basicClass, obfuscated).getWriter().toByteArray();
		}
		

		return basicClass;
	}

}
