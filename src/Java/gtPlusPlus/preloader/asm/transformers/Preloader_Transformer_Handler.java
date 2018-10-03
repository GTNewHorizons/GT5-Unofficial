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

		// Fix the OreDictionary - Forge
		if (transformedName.equals("net.minecraftforge.oredict.OreDictionary")) {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Transforming %s", transformedName);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
			return classWriter.toByteArray();
		}
		// Fix the OreDictionary COFH
		if (transformedName.equals("cofh.core.util.oredict.OreDictionaryArbiter")) {
			FMLRelaunchLog.log("[GT++ ASM] COFH", Level.INFO, "Transforming %s", transformedName);
			return new ClassTransformer_COFH_OreDictionaryArbiter(basicClass, obfuscated).getWriter().toByteArray();
		}

		// Fix Tinkers Fluids
			if (transformedName.equals("tconstruct.smeltery.blocks.TConstructFluid")) {
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
			
			//Improve OB Sprinklers
			/*if (transformedName.equals("openblocks.common.tileentity.TileEntitySprinkler")) {
				FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Transforming %s", transformedName);
				try {
					ClassLoader aCustom = new gtPlusPlus.preloader.CustomClassLoader();
					Class aCustomClass = aCustom.loadClass(gtPlusPlus.xmod.ob.TileEntitySprinkler_ASM.class.getName());
					if (aCustomClass == null) {
						aCustomClass = aCustom.loadClass(gtPlusPlus.xmod.ob.TileEntitySprinkler_ASM.class.getCanonicalName());
					}
					if (aCustomClass == null) {
						aCustomClass = aCustom.loadClass(gtPlusPlus.xmod.ob.TileEntitySprinkler_ASM.class.getSimpleName());
					}
					if (aCustomClass == null) {
						byte[] mCustomClassData = GetBytecode.getClassFile(aCustomClass);
						if (mCustomClassData != null) {
							FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Custom Class Loaded in place.");
							return mCustomClassData;
						}
					}

				} catch (ClassNotFoundException | UnmodifiableClassException e) {
					e.printStackTrace();
				}
				return new ClassTransformer_OB_Sprinkler(obfuscated, basicClass).getWriter().toByteArray();
			}*/
		}
		return basicClass;
	}
	
}
