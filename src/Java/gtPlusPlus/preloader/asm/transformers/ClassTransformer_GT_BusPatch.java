package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_FULL;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INTEGER;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.UNINITIALIZED_THIS;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class ClassTransformer_GT_BusPatch {

	public static final String aSuperInput = "gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SuperBus_Input";
	public static final String aSuperOutput = "gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_SuperBus_Output";
	public static final String aInput = "gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus";
	public static final String aOutput = "gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus";
	
	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	


	public ClassTransformer_GT_BusPatch(byte[] basicClass, String aClassName) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Attempting to make GT Busses bigger.");	
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Patching "+aClassName+".");	

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter), 0);		

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Valid patch? "+isValid+".");	
		reader = aTempReader;
		writer = aTempWriter;


		if (reader != null && writer != null) {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod(aClassName);
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
	
	
	public boolean injectMethod(String aClassName) {

		boolean didInject = false;		
		MethodVisitor mv;	
		ClassWriter cw = getWriter();

		//GT_MetaTileEntity_Hatch_InputBus
		//Constructor
		if (aClassName.equals(aInput)){
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(ILjava/lang/String;Ljava/lang/String;II)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(19, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(ICONST_2);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitLdcInsn("Item Input for Multiblocks");
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(20, l1);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Capacity: ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn(" stack");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(ICONST_2);
			Label l2 = new Label();
			mv.visitJumpInsn(IF_ICMPLT, l2);
			mv.visitLdcInsn("s");
			Label l3 = new Label();
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l2);
			mv.visitFrame(F_FULL, 6, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER}, 10, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER, "[Ljava/lang/String;", "[Ljava/lang/String;", INTEGER, "java/lang/StringBuilder"});
			mv.visitLdcInsn("");
			mv.visitLabel(l3);
			mv.visitFrame(F_FULL, 6, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER}, 11, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER, "[Ljava/lang/String;", "[Ljava/lang/String;", INTEGER, "java/lang/StringBuilder", "java/lang/String"});
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "gregtech/api/interfaces/ITexture");
			mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(ILjava/lang/String;Ljava/lang/String;II[Ljava/lang/String;[Lgregtech/api/interfaces/ITexture;)V", false);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(16, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(21, l5);
			mv.visitInsn(RETURN);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus;", null, l0, l6, 0);
			mv.visitLocalVariable("aID", "I", null, l0, l6, 1);
			mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l6, 2);
			mv.visitLocalVariable("aNameRegional", "Ljava/lang/String;", null, l0, l6, 3);
			mv.visitLocalVariable("aTier", "I", null, l0, l6, 4);
			mv.visitLocalVariable("aSlots", "I", null, l0, l6, 5);
			mv.visitMaxs(12, 6);
			mv.visitEnd();
			didInject = true;
		}


		//GT_MetaTileEntity_Hatch_OutputBus
		//Constructor
		if (aClassName.equals(aOutput)){
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(ILjava/lang/String;Ljava/lang/String;II)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(16, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(ICONST_2);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitLdcInsn("Item Output for Multiblocks");
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(17, l1);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Capacity: ");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
			mv.visitLdcInsn(" stack");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitInsn(ICONST_2);
			Label l2 = new Label();
			mv.visitJumpInsn(IF_ICMPLT, l2);
			mv.visitLdcInsn("s");
			Label l3 = new Label();
			mv.visitJumpInsn(GOTO, l3);
			mv.visitLabel(l2);
			mv.visitFrame(F_FULL, 6, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER}, 10, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER, "[Ljava/lang/String;", "[Ljava/lang/String;", INTEGER, "java/lang/StringBuilder"});
			mv.visitLdcInsn("");
			mv.visitLabel(l3);
			mv.visitFrame(F_FULL, 6, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER}, 11, new Object[] {UNINITIALIZED_THIS, INTEGER, "java/lang/String", "java/lang/String", INTEGER, INTEGER, "[Ljava/lang/String;", "[Ljava/lang/String;", INTEGER, "java/lang/StringBuilder", "java/lang/String"});
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, "gregtech/api/interfaces/ITexture");
			mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(ILjava/lang/String;Ljava/lang/String;II[Ljava/lang/String;[Lgregtech/api/interfaces/ITexture;)V", false);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(18, l4);
			mv.visitInsn(RETURN);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_OutputBus;", null, l0, l5, 0);
			mv.visitLocalVariable("aID", "I", null, l0, l5, 1);
			mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l5, 2);
			mv.visitLocalVariable("aNameRegional", "Ljava/lang/String;", null, l0, l5, 3);
			mv.visitLocalVariable("aTier", "I", null, l0, l5, 4);
			mv.visitLocalVariable("aSlots", "I", null, l0, l5, 5);
			mv.visitMaxs(12, 6);
			mv.visitEnd();
			didInject = true;
		}


		//GT_MetaTileEntity_SuperBus_Input
		//Constructor
		if (aClassName.equals(aSuperInput)){
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(ILjava/lang/String;Ljava/lang/String;II)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(27, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus", "<init>", "(ILjava/lang/String;Ljava/lang/String;I)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(20, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Input", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(28, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "LgtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Input;", null, l0, l3, 0);
			mv.visitLocalVariable("aID", "I", null, l0, l3, 1);
			mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 2);
			mv.visitLocalVariable("aNameRegional", "Ljava/lang/String;", null, l0, l3, 3);
			mv.visitLocalVariable("aTier", "I", null, l0, l3, 4);
			mv.visitLocalVariable("aSlots", "I", null, l0, l3, 5);
			mv.visitMaxs(5, 6);
			mv.visitEnd();
			didInject = true;
		}

		
		//GT_MetaTileEntity_SuperBus_Output
		//Constructor
		if (aClassName.equals(aSuperOutput)){
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(ILjava/lang/String;Ljava/lang/String;II)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(25, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_OutputBus", "<init>", "(ILjava/lang/String;Ljava/lang/String;I)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(18, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Output", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(26, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "LgtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Output;", null, l0, l3, 0);
			mv.visitLocalVariable("aID", "I", null, l0, l3, 1);
			mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 2);
			mv.visitLocalVariable("aNameRegional", "Ljava/lang/String;", null, l0, l3, 3);
			mv.visitLocalVariable("aTier", "I", null, l0, l3, 4);
			mv.visitLocalVariable("aSlots", "I", null, l0, l3, 5);
			mv.visitMaxs(5, 6);
			mv.visitEnd();
			didInject = true;
		}



	FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Method injection complete.");		
	return didInject;



	}
	
	public final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {		
			MethodVisitor methodVisitor;
			methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);			
			return methodVisitor;
		}
	}


}
