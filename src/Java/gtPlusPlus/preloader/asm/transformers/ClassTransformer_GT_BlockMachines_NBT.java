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

public class ClassTransformer_GT_BlockMachines_NBT {	

	//The qualified name of the class we plan to transform.
	private static final String className = "gregtech.common.blocks.GT_Block_Machines"; 
	//gregtech/common/blocks/GT_Block_Machines

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	
	
	String aEntityPlayer;
	String aEntityPlayerMP;
	String aWorld;
	
	private static boolean doesMethodAlreadyExist = false;

	public ClassTransformer_GT_BlockMachines_NBT(byte[] basicClass, boolean obfuscated) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		try {
			aTempReader = new ClassReader(className);
			aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new localClassVisitor(aTempWriter), 0);
		} catch (IOException e) {}		
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
			aEntityPlayerMP = obfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayerMP") : "net/minecraft/entity/player/EntityPlayerMP";
			aWorld = obfuscated ? DevHelper.getObfuscated("net/minecraft/world/World") : "net/minecraft/world/World";
						
			injectMethod("removedByPlayer");
			injectMethod("harvestBlock");
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
		FMLRelaunchLog.log("[GT++ ASM] Gregtech NBT Persistency Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");	
		if (aMethodName.equals("removedByPlayer")) {
			
			mv = getWriter().visitMethod(ACC_PUBLIC, "removedByPlayer", "(L"+aWorld+";L"+aEntityPlayer+";IIIZ)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(535, l0);
			mv.visitVarInsn(ILOAD, 6);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(536, l2);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(538, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/items/GT_Generic_Block", "removedByPlayer", "(L"+aWorld+";L"+aEntityPlayer+";IIIZ)Z", false);
			mv.visitInsn(IRETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lgregtech/common/blocks/GT_Block_Machines;", null, l0, l3, 0);
			mv.visitLocalVariable("aWorld", "L"+aWorld+";", null, l0, l3, 1);
			mv.visitLocalVariable("aPlayer", "L"+aEntityPlayer+";", null, l0, l3, 2);
			mv.visitLocalVariable("aX", "I", null, l0, l3, 3);
			mv.visitLocalVariable("aY", "I", null, l0, l3, 4);
			mv.visitLocalVariable("aZ", "I", null, l0, l3, 5);
			mv.visitLocalVariable("aWillHarvest", "Z", null, l0, l3, 6);
			mv.visitMaxs(7, 7);
			mv.visitEnd();


		}
		else if (aMethodName.equals("harvestBlock")) {

			mv = getWriter().visitMethod(ACC_PUBLIC, "harvestBlock", "(L"+aWorld+";L"+aEntityPlayer+";IIII)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(544, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitVarInsn(ILOAD, 6);
			mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/items/GT_Generic_Block", "harvestBlock", "(L"+aWorld+";L"+aEntityPlayer+";IIII)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(545, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorld+"", "setBlockToAir", "(III)Z", false);
			mv.visitInsn(POP);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(546, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lgregtech/common/blocks/GT_Block_Machines;", null, l0, l3, 0);
			mv.visitLocalVariable("aWorld", "L"+aWorld+";", null, l0, l3, 1);
			mv.visitLocalVariable("aPlayer", "L"+aEntityPlayer+";", null, l0, l3, 2);
			mv.visitLocalVariable("aX", "I", null, l0, l3, 3);
			mv.visitLocalVariable("aY", "I", null, l0, l3, 4);
			mv.visitLocalVariable("aZ", "I", null, l0, l3, 5);
			mv.visitLocalVariable("aMeta", "I", null, l0, l3, 6);
			mv.visitMaxs(7, 7);
			mv.visitEnd();

		}	
		FMLRelaunchLog.log("[GT++ ASM] Gregtech NBT Persistency Patch", Level.INFO, "Method injection complete.");		

	}

	public static final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {			
			if (name.equals("removedByPlayer")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech NBT Persistency Patch", Level.INFO, "Found method "+name+", skipping patch.");
				doesMethodAlreadyExist = true;
			}	
			if (name.equals("harvestBlock")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech NBT Persistency Patch", Level.INFO, "Found method "+name+", skipping patch.");
				doesMethodAlreadyExist = true;
			}			
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return methodVisitor;
		}
	}

}
