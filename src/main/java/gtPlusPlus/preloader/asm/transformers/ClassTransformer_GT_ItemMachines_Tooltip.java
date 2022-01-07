package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.DevHelper;

public class ClassTransformer_GT_ItemMachines_Tooltip {	

	//The qualified name of the class we plan to transform.
	private static final String className = "gregtech.common.blocks.GT_Item_Machines"; 
	//gregtech/common/blocks/GT_Item_Machines

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	
	
	String aEntityPlayer;
	String aItemStack;
	String aWorld;
	
	private static boolean doesMethodAlreadyExist = false;

	public ClassTransformer_GT_ItemMachines_Tooltip(byte[] basicClass, boolean obfuscated) {
		
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter), 0);	
		
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null && !doesMethodAlreadyExist) {			
			aEntityPlayer = obfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayer") : "net/minecraft/entity/player/EntityPlayer";
			aItemStack = obfuscated ? DevHelper.getObfuscated("net/minecraft/item/ItemStack") : "net/minecraft/item/ItemStack";
			aWorld = obfuscated ? DevHelper.getObfuscated("net/minecraft/world/World") : "net/minecraft/world/World";
						
			injectMethod("addInformation");
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

	public void injectMethod(String aMethodName) {		
		MethodVisitor mv;	
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Tooltip Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");	
		if (aMethodName.equals("addInformation")) {
			
			mv = getWriter().visitMethod(ACC_PUBLIC, "addInformation", "(L"+aItemStack+";L"+aEntityPlayer+";Ljava/util/List;Z)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
			mv.visitLabel(l0);
			mv.visitLineNumber(120, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/gregtech/common/Meta_GT_Proxy", "conStructGtTileBlockTooltip", "(L"+aItemStack+";L"+aEntityPlayer+";Ljava/util/List;Z)V", false);
			mv.visitLabel(l1);
			mv.visitLineNumber(121, l1);
			Label l3 = new Label();
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
			mv.visitVarInsn(ASTORE, 5);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(122, l4);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "(Ljava/io/PrintStream;)V", false);
			mv.visitLabel(l3);
			mv.visitLineNumber(124, l3);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLocalVariable("this", "Lgregtech/common/blocks/GT_Item_Machines;", null, l0, l5, 0);
			mv.visitLocalVariable("aStack", "L"+aItemStack+";", null, l0, l5, 1);
			mv.visitLocalVariable("aPlayer", "L"+aEntityPlayer+";", null, l0, l5, 2);
			mv.visitLocalVariable("aList", "Ljava/util/List;", null, l0, l5, 3);
			mv.visitLocalVariable("par4", "Z", null, l0, l5, 4);
			mv.visitLocalVariable("e", "Ljava/lang/Throwable;", null, l4, l3, 5);
			mv.visitMaxs(4, 6);
			mv.visitEnd();
}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Tooltip Patch", Level.INFO, "Method injection complete.");		

	}

	public static final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {			
			if (name.equals("addInformation")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Tooltip Patch", Level.INFO, "Found method "+name+", Patching.");
				return null;
			}			
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return methodVisitor;
		}
	}

}
