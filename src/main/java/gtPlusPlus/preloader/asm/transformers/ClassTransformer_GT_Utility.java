package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import gtPlusPlus.core.util.Utils;
import gtPlusPlus.preloader.Preloader_Logger;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ClassTransformer_GT_Utility {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;
	private final String className;
	


	public ClassTransformer_GT_Utility(byte[] basicClass, String aClassName) {
		
		className = aClassName;
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter, className), 0);

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		
		Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Valid patch? " + isValid + ".");
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("getTier");			
			injectMethod("applyRadioactivity");	
			injectMethod("isWearingFullFrostHazmat");	
			injectMethod("isWearingFullHeatHazmat");	
			injectMethod("isWearingFullBioHazmat");	
			injectMethod("isWearingFullRadioHazmat");	
			injectMethod("isWearingFullElectroHazmat");	
			injectMethod("isWearingFullGasHazmat");
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
		ClassWriter cw = getWriter();	
		String aClassNameFormatted = Utils.class.getName().replace(".", "/");	

		if (aMethodName.equals("isWearingFullFrostHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullFrostHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1273, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/EntityUtils", "isWearingFullFrostHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullHeatHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullHeatHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1277, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/EntityUtils", "isWearingFullHeatHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullBioHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullBioHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1281, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/EntityUtils", "isWearingFullBioHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullRadioHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullRadioHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1285, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/EntityUtils", "isWearingFullRadioHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullElectroHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullElectroHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1289, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/EntityUtils", "isWearingFullElectroHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullGasHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullGasHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1293, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/EntityUtils", "isWearingFullGasHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;
		}		
		
		if (aMethodName.equals("getTier")) {
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ", static replacement call to "+aClassNameFormatted+".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getTier", "(J)B", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(23, l0);
			mv.visitVarInsn(LLOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, aClassNameFormatted, "getTier", "(J)B", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("l", "J", null, l0, l1, 0);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			didInject = true;
		}		
		
		if (aMethodName.equals("applyRadioactivity")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "applyRadioactivity", "(Lnet/minecraft/entity/EntityLivingBase;II)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1342, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/EntityUtils", "applyRadioactivity", "(Lnet/minecraft/entity/EntityLivingBase;II)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			mv.visitLocalVariable("aLevel", "I", null, l0, l1, 1);
			mv.visitLocalVariable("aAmountOfItems", "I", null, l0, l1, 2);
			mv.visitMaxs(3, 3);
			mv.visitEnd();
			didInject = true;
		}
		
		Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Method injection complete.");
		return didInject;
	}

	public final class localClassVisitor extends ClassVisitor {

		String aClassName;

		public localClassVisitor(ClassVisitor cv, String aName) {
			super(ASM5, cv);
			aClassName = aName;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;

			if (name.equals("getTier")) {
				methodVisitor = null;
			} 
			else if (name.equals("applyRadioactivity")){
				methodVisitor = null;				
			}
			else if (name.equals("isWearingFullFrostHazmat")){
				methodVisitor = null;				
			}
			else if (name.equals("isWearingFullHeatHazmat")){
				methodVisitor = null;				
			}
			else if (name.equals("isWearingFullBioHazmat")){
				methodVisitor = null;				
			}
			else if (name.equals("isWearingFullRadioHazmat")){
				methodVisitor = null;				
			}
			else if (name.equals("isWearingFullElectroHazmat")){
				methodVisitor = null;				
			}
			else if (name.equals("isWearingFullGasHazmat")){
				methodVisitor = null;				
			}
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}

			if (methodVisitor == null) {
				Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Found method " + name + ", removing.");
				Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Descriptor: "+desc);
			}
			return methodVisitor;
		}
	}

}
