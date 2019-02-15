package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class ClassTransformer_GT_Packet_TileEntity {	

	//The qualified name of the class we plan to transform.
	//gregtech/common/blocks/GT_Block_Machines

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	


	public ClassTransformer_GT_Packet_TileEntity(byte[] basicClass, boolean obfuscated) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		FMLRelaunchLog.log("[GT++ ASM] Gregtech GT_Packet_TileEntity Patch", Level.INFO, "Attempting to make GT Packets safer.");	

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter), 0);		

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech GT_Packet_TileEntity Patch", Level.INFO, "Valid patch? "+isValid+".");	
		reader = aTempReader;
		writer = aTempWriter;


		if (reader != null && writer != null) {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech GT_Packet_TileEntity Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("process", obfuscated);
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

	public boolean injectMethod(String aMethodName, boolean obfuscated) {		
		MethodVisitor mv;	
		boolean didInject = false;		
		String aGetTile = obfuscated ? "func_147438_o" : "getTileEntity";
		
		FMLRelaunchLog.log("[GT++ ASM] Gregtech GT_Packet_TileEntity Patch", Level.INFO, "Injecting "+aMethodName+".");	
		if (aMethodName.equals("process")) {
			mv = getWriter().visitMethod(ACC_PUBLIC, "process", "(Lnet/minecraft/world/IBlockAccess;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(93, l3);
			mv.visitVarInsn(ALOAD, 1);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNULL, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(94, l5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mX", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mY", "S");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mZ", "I");
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", ""+aGetTile+"", "(III)Lnet/minecraft/tileentity/TileEntity;", true);
			mv.visitVarInsn(ASTORE, 2);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(95, l6);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitJumpInsn(IFNULL, l4);
			mv.visitLabel(l0);
			mv.visitLineNumber(97, l0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(INSTANCEOF, "gregtech/api/metatileentity/BaseMetaTileEntity");
			Label l7 = new Label();
			mv.visitJumpInsn(IFEQ, l7);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(98, l8);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(CHECKCAST, "gregtech/api/metatileentity/BaseMetaTileEntity");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mID", "S");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC0", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC1", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC2", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC3", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC4", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC5", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mTexture", "B");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mTexturePage", "B");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mUpdate", "B");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mRedstone", "B");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mColor", "B");
			mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/metatileentity/BaseMetaTileEntity", "receiveMetaTileEntityData", "(SIIIIIIBBBBB)V", false);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(99, l9);
			mv.visitJumpInsn(GOTO, l4);
			mv.visitLabel(l7);
			mv.visitLineNumber(101, l7);
			mv.visitFrame(F_APPEND,1, new Object[] {"net/minecraft/tileentity/TileEntity"}, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(INSTANCEOF, "gregtech/api/metatileentity/BaseMetaPipeEntity");
			mv.visitJumpInsn(IFEQ, l4);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(102, l10);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(CHECKCAST, "gregtech/api/metatileentity/BaseMetaPipeEntity");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mID", "S");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC0", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC1", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC2", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC3", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC4", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mC5", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mTexture", "B");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mUpdate", "B");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mRedstone", "B");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mColor", "B");
			mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/metatileentity/BaseMetaPipeEntity", "receiveMetaTileEntityData", "(SIIIIIIBBBB)V", false);
			mv.visitLabel(l1);
			mv.visitLineNumber(104, l1);
			mv.visitJumpInsn(GOTO, l4);
			mv.visitLabel(l2);
			mv.visitLineNumber(105, l2);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
			mv.visitVarInsn(ASTORE, 3);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(106, l11);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("[GTPTE] Bad Tile Entity set in world, your game would have crashed if not for me! Was Null? ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 2);
			Label l12 = new Label();
			mv.visitJumpInsn(IFNONNULL, l12);
			mv.visitInsn(ICONST_1);
			Label l13 = new Label();
			mv.visitJumpInsn(GOTO, l13);
			mv.visitLabel(l12);
			mv.visitFrame(F_FULL, 4, new Object[] {"gregtech/api/net/GT_Packet_TileEntity", "net/minecraft/world/IBlockAccess", "net/minecraft/tileentity/TileEntity", "java/lang/Throwable"}, 2, new Object[] {"java/io/PrintStream", "java/lang/StringBuilder"});
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l13);
			mv.visitFrame(F_FULL, 4, new Object[] {"gregtech/api/net/GT_Packet_TileEntity", "net/minecraft/world/IBlockAccess", "net/minecraft/tileentity/TileEntity", "java/lang/Throwable"}, 3, new Object[] {"java/io/PrintStream", "java/lang/StringBuilder", INTEGER});
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Z)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(107, l14);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Tile location [");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mX", "I");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn("][");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mY", "S");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn("][");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "gregtech/api/net/GT_Packet_TileEntity", "mZ", "I");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn("]");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(108, l15);
			mv.visitVarInsn(ALOAD, 2);
			Label l16 = new Label();
			mv.visitJumpInsn(IFNULL, l16);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(109, l17);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(INSTANCEOF, "gregtech/api/metatileentity/BaseMetaPipeEntity");
			Label l18 = new Label();
			mv.visitJumpInsn(IFEQ, l18);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(110, l19);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("Type: Pipe");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(111, l20);
			mv.visitJumpInsn(GOTO, l16);
			mv.visitLabel(l18);
			mv.visitLineNumber(112, l18);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(INSTANCEOF, "gregtech/api/metatileentity/BaseMetaTileEntity");
			Label l21 = new Label();
			mv.visitJumpInsn(IFEQ, l21);
			Label l22 = new Label();
			mv.visitLabel(l22);
			mv.visitLineNumber(113, l22);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("Type: Machine");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLineNumber(114, l23);
			mv.visitJumpInsn(GOTO, l16);
			mv.visitLabel(l21);
			mv.visitLineNumber(116, l21);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("Type: Non-GT (Could be GT++/TT/BW)");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			mv.visitLabel(l16);
			mv.visitLineNumber(119, l16);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
			mv.visitLabel(l4);
			mv.visitLineNumber(123, l4);
			mv.visitFrame(F_CHOP,2, null, 0, null);
			mv.visitInsn(RETURN);
			Label l24 = new Label();
			mv.visitLabel(l24);
			mv.visitLocalVariable("this", "Lgregtech/api/net/GT_Packet_TileEntity;", null, l3, l24, 0);
			mv.visitLocalVariable("aWorld", "Lnet/minecraft/world/IBlockAccess;", null, l3, l24, 1);
			mv.visitLocalVariable("tTileEntity", "Lnet/minecraft/tileentity/TileEntity;", null, l6, l4, 2);
			mv.visitLocalVariable("t", "Ljava/lang/Throwable;", null, l11, l4, 3);
			mv.visitMaxs(13, 4);
			mv.visitEnd();


			didInject = true;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech GT_Packet_TileEntity Patch", Level.INFO, "Method injection complete.");		
		return didInject;
	}

	public final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {		
			MethodVisitor methodVisitor;	
			if (name.equals("process")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech GT_Packet_TileEntity Patch", Level.INFO, "Found method "+name+", removing.");	
				methodVisitor = null;
			}	
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);	
			}
			return methodVisitor;
		}
	}

}
