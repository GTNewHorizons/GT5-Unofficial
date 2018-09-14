package gtPlusPlus.preloader.asm.transformers;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer2.GT_MetaTile_Visitor;

public class Preloader_Transformer_Handler implements IClassTransformer {

	private final boolean mEnabled = false;

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

		// Fix the OreDictionary
		if (transformedName.equals("net.minecraftforge.oredict.OreDictionary")) {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Transforming %s", transformedName);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
			return classWriter.toByteArray();
		}

		// Fix Tinkers Fluids
		if (transformedName.equals("tconstruct.smeltery.blocks.TConstructFluid")) {
			FMLRelaunchLog.log("[GT++ ASM] Bright Fluids", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_TiConFluids("getLightValue", obfuscated, basicClass).getWriter().toByteArray();
		}
		
		//Fix GC stuff
		if (transformedName.equals("micdoodle8.mods.galacticraft.core.util.FluidUtil")) {	
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft FluidUtils Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GC_FluidUtil(basicClass).getWriter().toByteArray();
		}
		if (transformedName.equals("micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader")) {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft Fuel_Loader Patch", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_GC_FuelLoader(basicClass).getWriter().toByteArray();
		}

		if (mEnabled) {
			if (transformedName.equals("gregtech.api.metatileentity.BaseMetaTileEntity")) {
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Transforming %s", transformedName);
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				ClassReader x = new ClassReader(basicClass);
				x.accept(new GT_MetaTile_Visitor(classWriter, false), ClassReader.EXPAND_FRAMES);
				return classWriter.toByteArray();
			}
			if (transformedName.equals("gregtech.common.blocks.GT_Block_Machines")) {
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Transforming %s", transformedName);
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				ClassReader x = new ClassReader(basicClass);
				x.accept(new GT_MetaTile_Visitor(classWriter, true), ClassReader.EXPAND_FRAMES);
				return classWriter.toByteArray();
			}
		}
		return basicClass;
	}

}
