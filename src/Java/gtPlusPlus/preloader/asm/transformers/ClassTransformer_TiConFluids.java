package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.DevHelper;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;


public class ClassTransformer_TiConFluids {	


	//Leftover Code, in case I ever want to inject interfaces.
	//private static final String cloneableInterface = "java/lang/Cloneable";

	//The qualified name of the class we plan to transform.
	private static final String className = "tconstruct.smeltery.blocks.TConstructFluid"; 
	
	private final boolean isValid;
	private final boolean isObfuscated;
	private final String methodName;
	private final ClassReader reader;
	private final ClassWriter writer;

	public ClassTransformer_TiConFluids(String aMethodName, boolean obfuscated, byte[] basicClass) {
		isObfuscated = obfuscated;
		methodName = obfuscated ? DevHelper.getInstance().getSRG(aMethodName) : aMethodName;
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		try {
			aTempReader = new ClassReader(className);
			aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new OreDictionaryVisitor(aTempWriter), 0);
		} catch (IOException e) {}		
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		reader = aTempReader;
		writer = aTempWriter;
		
		if (reader != null && writer != null) {
			injectMethod();
		}
		
	}

	public boolean isValidTransformer() {
		return isValid;
	}

	public ClassReader getReader() {
		return reader;
	}

	public ClassWriter getWriter() {
		return writer;
	}

	public void injectMethod() {
		if (isValidTransformer()) {
			FMLRelaunchLog.log("[GT++ ASM] Bright Fluids", Level.INFO, "Injecting "+methodName+" into "+className+". ["+(isObfuscated ? "Obfuscated" : "Unobfuscated")+"]");		
			//Account for Obfuscated constructor args.
			String IBlockAccessName = isObfuscated ? "ahl" : "net/minecraft/world/IBlockAccess";
			String aConstructorTypes = "(L"+IBlockAccessName+";III)I";
			
			MethodVisitor mv = getWriter().visitMethod(ACC_PUBLIC, methodName, aConstructorTypes, null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(17, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "tconstruct/smeltery/blocks/TConstructFluid", "maxScaledLight", "I");
			mv.visitVarInsn(ISTORE, 5);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(18, l1);
			mv.visitVarInsn(ILOAD, 5);
			Label l2 = new Label();
			mv.visitJumpInsn(IFLE, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(19, l3);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(21, l2);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			//net.minecraftforge.fluids.BlockFluidClassic.getLightValue(IBlockAccess, int, int, int)
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/fluids/BlockFluidClassic", methodName, aConstructorTypes, false);
			mv.visitInsn(IRETURN);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("this", "Ltconstruct/smeltery/blocks/TConstructFluid;", null, l0, l4, 0);
			mv.visitLocalVariable("world", "L"+IBlockAccessName+";", null, l0, l4, 1);
			mv.visitLocalVariable("x", "I", null, l0, l4, 2);
			mv.visitLocalVariable("y", "I", null, l0, l4, 3);
			mv.visitLocalVariable("z", "I", null, l0, l4, 4);
			mv.visitLocalVariable("maxLight", "I", null, l1, l4, 5);
			mv.visitMaxs(5, 6);
			mv.visitEnd();
			FMLRelaunchLog.log("[GT++ ASM] Bright Fluids", Level.INFO, "Method injection complete.");		
			
		}
	}


}
