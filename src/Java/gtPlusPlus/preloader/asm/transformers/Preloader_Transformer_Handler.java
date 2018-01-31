package gtPlusPlus.preloader.asm.transformers;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer2.GT_MetaTile_Visitor;
import net.minecraft.launchwrapper.IClassTransformer;

public class Preloader_Transformer_Handler implements IClassTransformer {

	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("net.minecraftforge.oredict.OreDictionary")) {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Transforming %s", transformedName);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
			return classWriter.toByteArray();
		}
		if(transformedName.equals("gregtech.api.metatileentity.BaseMetaTileEntity")) {
			FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Transforming %s", transformedName);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			ClassReader x = new ClassReader(basicClass);
			x.accept(new GT_MetaTile_Visitor(classWriter), ClassReader.EXPAND_FRAMES);
			return classWriter.toByteArray();
		}
		return basicClass;
	}

}
