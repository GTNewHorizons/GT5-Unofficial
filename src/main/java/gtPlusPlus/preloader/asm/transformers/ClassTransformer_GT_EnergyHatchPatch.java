package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.asm.ClassesToTransform;

public class ClassTransformer_GT_EnergyHatchPatch {

	private static final String aRtgInputFormatted = ClassesToTransform.GTPP_MTE_HATCH_RTG.replace(".", "/");
	private static final String aEnergyFormatted = ClassesToTransform.GT_MTE_HATCH_ENERGY.replace(".", "/");

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	


	public ClassTransformer_GT_EnergyHatchPatch(byte[] basicClass, String aClassName) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Attempting to add slots capabilities to GT Energy Hatches.");	
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Patching "+aClassName+".");	

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter, aClassName), 0);		

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Valid patch? "+isValid+".");	
		reader = aTempReader;
		writer = aTempWriter;


		if (reader != null && writer != null) {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Attempting Method Injection.");
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

		//GT_MetaTileEntity_Hatch_Energy
		//Constructor
		if (aClassName.equals(ClassesToTransform.GT_MTE_HATCH_ENERGY)){
			
			
			//Constructor 1
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(ILjava/lang/String;Ljava/lang/String;II[Ljava/lang/String;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(26, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ILOAD, 1);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitVarInsn(ALOAD, 3);
				mv.visitVarInsn(ILOAD, 4);
				mv.visitVarInsn(ILOAD, 5);
				mv.visitVarInsn(ALOAD, 6);
				mv.visitInsn(ICONST_0);
				mv.visitTypeInsn(ANEWARRAY, "gregtech/api/interfaces/ITexture");
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(ILjava/lang/String;Ljava/lang/String;II[Ljava/lang/String;[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(27, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy;", null, l0, l2, 0);
				mv.visitLocalVariable("aID", "I", null, l0, l2, 1);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 2);
				mv.visitLocalVariable("aNameRegional", "Ljava/lang/String;", null, l0, l2, 3);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 4);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 5);
				mv.visitLocalVariable("aDesc", "[Ljava/lang/String;", null, l0, l2, 6);
				mv.visitMaxs(8, 7);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}

			//Constructor 2
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
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
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(31, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy;", null, l0, l2, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 3);
				mv.visitLocalVariable("aDescription", "Ljava/lang/String;", null, l0, l2, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l2, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}

			//Third constructor with String[] for GT 5.09
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(34, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitVarInsn(ALOAD, 5);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch", "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(35, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "Lgregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy;", null, l0, l2, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 3);
				mv.visitLocalVariable("aDescription", "[Ljava/lang/String;", null, l0, l2, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l2, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			didInject = true;
		}

		//GT_MetaTileEntity_Hatch_Energy_RTG
		//Constructor
		if (aClassName.equals(ClassesToTransform.GTPP_MTE_HATCH_RTG)){

			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(ILjava/lang/String;Ljava/lang/String;II)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(38, l0);
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
				mv.visitLdcInsn("Energy Injector for Multiblocks");
				mv.visitInsn(AASTORE);
				mv.visitInsn(DUP);
				mv.visitInsn(ICONST_1);
				mv.visitLdcInsn("Accepts RTG pellets for Fuel");
				mv.visitInsn(AASTORE);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy", "<init>", "(ILjava/lang/String;Ljava/lang/String;II[Ljava/lang/String;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(39, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "LgtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy_RTG;", null, l0, l2, 0);
				mv.visitLocalVariable("aID", "I", null, l0, l2, 1);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 2);
				mv.visitLocalVariable("aNameRegional", "Ljava/lang/String;", null, l0, l2, 3);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 4);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 5);
				mv.visitMaxs(10, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(42, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitVarInsn(ALOAD, 5);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy", "<init>", "(Ljava/lang/String;IILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(43, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "LgtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy_RTG;", null, l0, l2, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 3);
				mv.visitLocalVariable("aDescription", "Ljava/lang/String;", null, l0, l2, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l2, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}
			{
				mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(46, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ILOAD, 2);
				mv.visitVarInsn(ILOAD, 3);
				mv.visitVarInsn(ALOAD, 4);
				mv.visitVarInsn(ALOAD, 5);
				mv.visitMethodInsn(INVOKESPECIAL, "gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy", "<init>", "(Ljava/lang/String;II[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(47, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "LgtPlusPlus/xmod/gregtech/api/metatileentity/implementations/GT_MetaTileEntity_Hatch_Energy_RTG;", null, l0, l2, 0);
				mv.visitLocalVariable("aName", "Ljava/lang/String;", null, l0, l2, 1);
				mv.visitLocalVariable("aTier", "I", null, l0, l2, 2);
				mv.visitLocalVariable("aSlots", "I", null, l0, l2, 3);
				mv.visitLocalVariable("aDescription", "[Ljava/lang/String;", null, l0, l2, 4);
				mv.visitLocalVariable("aTextures", "[[[Lgregtech/api/interfaces/ITexture;", null, l0, l2, 5);
				mv.visitMaxs(6, 6);
				mv.visitEnd();
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Injection new constructor "+(aConID++));
			}


			didInject = true;
		}

		FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Method injection complete. Successful? "+didInject);		
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
			MethodVisitor methodVisitor = null;
			if ((mClassName.equals(ClassesToTransform.GTPP_MTE_HATCH_RTG)) && access == ACC_PUBLIC && name.equals("<init>") && (desc.equals("(Ljava/lang/String;ILjava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V") || desc.equals("(Ljava/lang/String;I[Ljava/lang/String;[[[Lgregtech/api/interfaces/ITexture;)V"))) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Found Constructor, "+"'"+access+"', "+"'"+name+"', "+"'"+desc+"', "+"'"+signature+"'");					
				methodVisitor = null;
			}			
			else {	
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);					
			}	
			if (methodVisitor == null) {
				if (mClassName.equals(ClassesToTransform.GT_MTE_HATCH_ENERGY)){
					FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Removed method from "+mClassName);						
				}
				else {
					FMLRelaunchLog.log("[GT++ ASM] Gregtech Energy Hatch Patch", Level.INFO, "Removed Constructor with descriptor '"+desc+"' from "+mClassName);					
				}
			}
			return methodVisitor;
		}
	}


}
