package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.AALOAD;
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
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
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
import gtPlusPlus.preloader.asm.ClassesToTransform;

public class ClassTransformer_GT_BusPatch {

	private static final String aSuperInputFormatted = ClassesToTransform.GTPP_MTE_HATCH_SUPER_INPUT_BUS.replace(".", "/");
	private static final String aSuperOutputFormatted = ClassesToTransform.GTPP_MTE_HATCH_SUPER_OUTPUT_BUS.replace(".", "/");
	private static final String aInputFormatted = ClassesToTransform.GT_MTE_HATCH_INPUTBUS.replace(".", "/");
	private static final String aOutputFormatted = ClassesToTransform.GT_MTE_HATCH_OUTPUTBUS.replace(".", "/");

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
		if (aClassName.equals(ClassesToTransform.GT_MTE_HATCH_INPUTBUS)){

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
				mv.visitFieldInsn(PUTFIELD, ""+aInputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l5 = new Label();
				mv.visitLabel(l5);
				mv.visitLineNumber(21, l5);
				mv.visitInsn(RETURN);
				Label l6 = new Label();
				mv.visitLabel(l6);
				mv.visitLocalVariable("this", "L"+aInputFormatted+";", null, l0, l6, 0);
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
				mv.visitFieldInsn(PUTFIELD, ""+aInputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(30, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aInputFormatted+";", null, l0, l3, 0);
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
				mv.visitFieldInsn(PUTFIELD, ""+aInputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(34, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aInputFormatted+";", null, l0, l3, 0);
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
		if (aClassName.equals(ClassesToTransform.GT_MTE_HATCH_OUTPUTBUS)){

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
				mv.visitLocalVariable("this", "L"+aOutputFormatted+";", null, l0, l5, 0);
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
				mv.visitLocalVariable("this", "L"+aOutputFormatted+";", null, l0, l2, 0);
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
				mv.visitLocalVariable("this", "L"+aOutputFormatted+";", null, l0, l2, 0);
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
		if (aClassName.equals(ClassesToTransform.GTPP_MTE_HATCH_SUPER_INPUT_BUS)){

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
				mv.visitMethodInsn(INVOKESPECIAL, ""+aInputFormatted+"", "<init>", "(ILjava/lang/String;Ljava/lang/String;I)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(20, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, ""+aSuperInputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(28, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aSuperInputFormatted+";", null, l0, l3, 0);
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
				mv.visitMethodInsn(INVOKESTATIC, ""+aSuperInputFormatted+"", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, ""+aInputFormatted+"", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(20, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, ""+aSuperInputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(28, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aSuperInputFormatted+";", null, l0, l3, 0);
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
				mv.visitMethodInsn(INVOKESTATIC, ""+aSuperInputFormatted+"", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitInsn(ICONST_0);
				mv.visitInsn(AALOAD);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, ""+aInputFormatted+"", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(20, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, ""+aSuperInputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(32, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aSuperInputFormatted+";", null, l0, l3, 0);
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
		if (aClassName.equals(ClassesToTransform.GTPP_MTE_HATCH_SUPER_OUTPUT_BUS)){

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
				mv.visitMethodInsn(INVOKESPECIAL, ""+aOutputFormatted+"", "<init>", "(ILjava/lang/String;Ljava/lang/String;I)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(18, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, ""+aSuperOutputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(26, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aSuperOutputFormatted+";", null, l0, l3, 0);
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
				mv.visitMethodInsn(INVOKESTATIC, ""+aSuperOutputFormatted+"", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, ""+aOutputFormatted+"", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(18, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, ""+aSuperOutputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(26, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aSuperOutputFormatted+";", null, l0, l3, 0);
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
				mv.visitMethodInsn(INVOKESTATIC, ""+aSuperOutputFormatted+"", "getSlots", "(I)I", false);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitInsn(ICONST_0);
				mv.visitInsn(AALOAD);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitMethodInsn(INVOKESPECIAL, ""+aOutputFormatted+"", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(18, l1);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitInsn(ACONST_NULL);
				mv.visitFieldInsn(PUTFIELD, ""+aSuperOutputFormatted+"", "mRecipeMap", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLineNumber(30, l2);
				mv.visitInsn(RETURN);
				Label l3 = new Label();
				mv.visitLabel(l3);
				mv.visitLocalVariable("this", "L"+aSuperOutputFormatted+";", null, l0, l3, 0);
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
			if ((mClassName.equals(ClassesToTransform.GTPP_MTE_HATCH_SUPER_INPUT_BUS) || mClassName.equals(ClassesToTransform.GTPP_MTE_HATCH_SUPER_OUTPUT_BUS)) && access == ACC_PUBLIC && name.equals("<init>") && (desc.equals("(Ljava/lang/String;ILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V") || desc.equals("(Ljava/lang/String;I[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V"))) {
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
