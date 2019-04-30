package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

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
		aTempReader.accept(new localClassVisitor(aTempWriter, aClassName), 0);		

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
		int aConID = 1;

		//GT_MetaTileEntity_Hatch_InputBus
		//Constructor
		if (aClassName.equals(aInput)){

			//Constructor 1
			{
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
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}

			//Constructor 2
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(29, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitVarInsn(ALOAD, 5);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(16, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(30, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus;", null, l0, l3, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l3, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l3, 3);
				mv.visitLocalVariable("aDescription", "Ljava/lang/String;", null, l0, l3, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l3, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}

			//Third constructor with String[] for GT 5.09
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(33, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitVarInsn(ALOAD, 5);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(16, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(34, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus;", null, l0, l3, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l3, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l3, 3);
				mv.visitLocalVariable("aDescription", "[Ljava/lang/String;", null, l0, l3, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l3, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}



			didInject = true;
		}


		//GT_MetaTileEntity_Hatch_OutputBus
		//Constructor
		if (aClassName.equals(aOutput)){

			{
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
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));	
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(26, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitVarInsn(ALOAD, 5);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(27, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_OutputBus;", null, l0, l2, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 3);
				mv.visitLocalVariable("aDescription", "Ljava/lang/String;", null, l0, l2, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l2, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(30, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitVarInsn(ALOAD, 5);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(31, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_OutputBus;", null, l0, l2, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 3);
				mv.visitLocalVariable("aDescription", "[Ljava/lang/String;", null, l0, l2, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l2, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}

			didInject = true;
		}


		//GT_MetaTileEntity_SuperBus_Input
		//Constructor
		if (aClassName.equals(aSuperInput)){

			{
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
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;ILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(27, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Input", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
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
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l3, 2);
				mv.visitLocalVariable("aDescription", "Ljava/lang/String;", null, l0, l3, 3);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l3, 4);
				mv.visitMaxs(6, 5);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;I[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(31, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Input", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitInsn(ICONST_0);
				mv.visitInsn(AALOAD);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_InputBus", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(20, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Input", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(32, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "LgtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Input;", null, l0, l3, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l3, 2);
				mv.visitLocalVariable("aDescription", "[Ljava/lang/String;", null, l0, l3, 3);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l3, 4);
				mv.visitMaxs(6, 5);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}


			didInject = true;
		}


		//GT_MetaTileEntity_SuperBus_Output
		//Constructor
		if (aClassName.equals(aSuperOutput)){

			{
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
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;ILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(25, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Output", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_OutputBus", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
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
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l3, 2);
				mv.visitLocalVariable("aDescription", "Ljava/lang/String;", null, l0, l3, 3);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l3, 4);
				mv.visitMaxs(6, 5);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;I[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(29, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Output", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitInsn(ICONST_0);
				mv.visitInsn(AALOAD);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_OutputBus", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(18, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, "gtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Output", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(30, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "LgtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_SuperBus_Output;", null, l0, l3, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l3, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l3, 2);
				mv.visitLocalVariable("aDescription", "[Ljava/lang/String;", null, l0, l3, 3);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l3, 4);
				mv.visitMaxs(6, 5);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}

			didInject = true;
		}



		FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Method injection complete. Successful? "+didInject);		
		return didInject;



	}

	public final class localClassVisitor extends ClassVisitor {

		private final String mClassName;

		public localClassVisitor(ClassVisitor cv, String aClassName) {
			super(ASM5, cv);
			mClassName = aClassName;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {		
			MethodVisitor methodVisitor;
			if ((mClassName.equals(aSuperInput) || mClassName.equals(aSuperOutput)) && access == ACC_PUBLIC && name.equals("<init>") && (desc.equals("(Ljava/lang/String;ILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V") || desc.equals("(Ljava/lang/String;I[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V"))) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Found Constructor, "+"'"+access+"', "+"'"+name+"', "+"'"+desc+"', "+"'"+signature+"'");					
				methodVisitor = null;
			}	
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);							
			}	
			if (methodVisitor == null) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Bus Patch", Level.INFO, "Removed Constructor with descriptor '"+desc+"' from "+mClassName);					
			}
			return methodVisitor;
		}
	}


}
