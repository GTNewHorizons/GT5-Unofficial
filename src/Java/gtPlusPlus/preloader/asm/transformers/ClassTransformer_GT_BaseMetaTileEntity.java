package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class ClassTransformer_GT_BaseMetaTileEntity {	

	//The qualified name of the class we plan to transform.
	//gregtech/common/blocks/GT_Block_Machines

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	


	public ClassTransformer_GT_BaseMetaTileEntity(byte[] basicClass) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		FMLRelaunchLog.log("[GT++ ASM] Gregtech setMetaTileEntity Patch", Level.INFO, "Attempting to make setMetaTileEntity(IMetaTileEntity) safer.");	

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter), 0);		

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech setMetaTileEntity Patch", Level.INFO, "Valid patch? "+isValid+".");	
		reader = aTempReader;
		writer = aTempWriter;


		if (reader != null && writer != null) {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech setMetaTileEntity Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("setMetaTileEntity");
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

	public boolean injectMethod(String aMethodName) {		
		MethodVisitor mv;	
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech setMetaTileEntity Patch", Level.INFO, "Injecting "+aMethodName+".");	
		if (aMethodName.equals("setMetaTileEntity")) {

			mv = getWriter().visitMethod(ACC_PUBLIC, "setMetaTileEntity", "(Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
			mv.visitLabel(l0);
			mv.visitLineNumber(1568, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, "gregtech/api/metatileentity/MetaTileEntity");
			mv.visitFieldInsn(PUTFIELD, "gregtech/api/metatileentity/BaseMetaTileEntity", "mMetaTileEntity", "Lgregtech/api/metatileentity/MetaTileEntity;");
			mv.visitLabel(l1);
			mv.visitLineNumber(1569, l1);
			Label l3 = new Label();
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l2);
			mv.visitLineNumber(1570, l2);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
			mv.visitVarInsn(ASTORE, 2);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(1571, l4);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("[BMTE] Bad Tile Entity set in world, your game would have crashed if not for me!");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1572, l5);
			mv.visitVarInsn(ALOAD, 1);
			Label l6 = new Label();
			mv.visitJumpInsn(IFNULL, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(1573, l7);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Tile was of type: ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "gregtech/api/interfaces/metatileentity/IMetaTileEntity", "getInventoryName", "()Ljava/lang/String;", true);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			mv.visitLabel(l6);
			mv.visitLineNumber(1574, l6);
			mv.visitFrame(F_APPEND,1, new Object[] {"java/lang/Throwable"}, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
			mv.visitLabel(l3);
			mv.visitLineNumber(1576, l3);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitInsn(RETURN);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/BaseMetaTileEntity;", null, l0, l8, 0);
			mv.visitLocalVariable("aMetaTileEntity", "Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;", null, l0, l8, 1);
			mv.visitLocalVariable("t", "Ljava/lang/Throwable;", null, l4, l3, 2);
			mv.visitMaxs(4, 3);
			mv.visitEnd();

			didInject = true;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech setMetaTileEntity Patch", Level.INFO, "Method injection complete.");		
		return didInject;
	}

	public final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {		
			MethodVisitor methodVisitor;	
			if (name.equals("setMetaTileEntity")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech setMetaTileEntity Patch", Level.INFO, "Found method "+name+", removing.");	
				methodVisitor = null;
			}	
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);	
			}
			return methodVisitor;
		}
	}

}
