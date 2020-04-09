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
			mv.visitLineNumber(1352, l0);
			mv.visitVarInsn(ILOAD, 1);
			Label l1 = new Label();
			mv.visitJumpInsn(IFLE, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getCreatureAttribute", "()Lnet/minecraft/entity/EnumCreatureAttribute;", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/entity/EnumCreatureAttribute", "UNDEAD", "Lnet/minecraft/entity/EnumCreatureAttribute;");
			mv.visitJumpInsn(IF_ACMPEQ, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getCreatureAttribute", "()Lnet/minecraft/entity/EnumCreatureAttribute;", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/entity/EnumCreatureAttribute", "ARTHROPOD", "Lnet/minecraft/entity/EnumCreatureAttribute;");
			mv.visitJumpInsn(IF_ACMPEQ, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "ic2/core/item/armor/ItemArmorHazmat", "hasCompleteHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false);
			mv.visitJumpInsn(IFNE, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(1353, l2);
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ASTORE, 3);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1354, l3);
			mv.visitVarInsn(ALOAD, 0);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitTypeInsn(NEW, "net/minecraft/potion/PotionEffect");
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "moveSlowdown", "Lnet/minecraft/potion/Potion;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/potion/Potion", "id", "I");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitIntInsn(SIPUSH, 140);
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(IMUL);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "moveSlowdown", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getActivePotionEffect", "(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 3);
			Label l5 = new Label();
			mv.visitJumpInsn(IFNONNULL, l5);
			mv.visitInsn(ICONST_0);
			Label l6 = new Label();
			mv.visitJumpInsn(GOTO, l6);
			mv.visitLabel(l5);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 6, new Object[] {"net/minecraft/entity/EntityLivingBase", l4, l4, INTEGER, INTEGER, INTEGER});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/potion/PotionEffect", "getDuration", "()I", false);
			mv.visitLabel(l6);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 7, new Object[] {"net/minecraft/entity/EntityLivingBase", l4, l4, INTEGER, INTEGER, INTEGER, INTEGER});
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_5);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IMUL);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitInsn(IDIV);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "addPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V", false);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(1355, l7);
			mv.visitVarInsn(ALOAD, 0);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitTypeInsn(NEW, "net/minecraft/potion/PotionEffect");
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "digSlowdown", "Lnet/minecraft/potion/Potion;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/potion/Potion", "id", "I");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitIntInsn(SIPUSH, 150);
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(IMUL);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "digSlowdown", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getActivePotionEffect", "(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 3);
			Label l9 = new Label();
			mv.visitJumpInsn(IFNONNULL, l9);
			mv.visitInsn(ICONST_0);
			Label l10 = new Label();
			mv.visitJumpInsn(GOTO, l10);
			mv.visitLabel(l9);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 6, new Object[] {"net/minecraft/entity/EntityLivingBase", l8, l8, INTEGER, INTEGER, INTEGER});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/potion/PotionEffect", "getDuration", "()I", false);
			mv.visitLabel(l10);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 7, new Object[] {"net/minecraft/entity/EntityLivingBase", l8, l8, INTEGER, INTEGER, INTEGER, INTEGER});
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_5);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IMUL);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitInsn(IDIV);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "addPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V", false);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(1356, l11);
			mv.visitVarInsn(ALOAD, 0);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitTypeInsn(NEW, "net/minecraft/potion/PotionEffect");
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "confusion", "Lnet/minecraft/potion/Potion;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/potion/Potion", "id", "I");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitIntInsn(SIPUSH, 130);
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(IMUL);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "confusion", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getActivePotionEffect", "(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 3);
			Label l13 = new Label();
			mv.visitJumpInsn(IFNONNULL, l13);
			mv.visitInsn(ICONST_0);
			Label l14 = new Label();
			mv.visitJumpInsn(GOTO, l14);
			mv.visitLabel(l13);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 6, new Object[] {"net/minecraft/entity/EntityLivingBase", l12, l12, INTEGER, INTEGER, INTEGER});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/potion/PotionEffect", "getDuration", "()I", false);
			mv.visitLabel(l14);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 7, new Object[] {"net/minecraft/entity/EntityLivingBase", l12, l12, INTEGER, INTEGER, INTEGER, INTEGER});
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_5);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IMUL);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitInsn(IDIV);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "addPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V", false);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(1357, l15);
			mv.visitVarInsn(ALOAD, 0);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitTypeInsn(NEW, "net/minecraft/potion/PotionEffect");
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "weakness", "Lnet/minecraft/potion/Potion;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/potion/Potion", "id", "I");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitIntInsn(SIPUSH, 150);
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(IMUL);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "weakness", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getActivePotionEffect", "(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 3);
			Label l17 = new Label();
			mv.visitJumpInsn(IFNONNULL, l17);
			mv.visitInsn(ICONST_0);
			Label l18 = new Label();
			mv.visitJumpInsn(GOTO, l18);
			mv.visitLabel(l17);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 6, new Object[] {"net/minecraft/entity/EntityLivingBase", l16, l16, INTEGER, INTEGER, INTEGER});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/potion/PotionEffect", "getDuration", "()I", false);
			mv.visitLabel(l18);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 7, new Object[] {"net/minecraft/entity/EntityLivingBase", l16, l16, INTEGER, INTEGER, INTEGER, INTEGER});
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_5);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IMUL);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitInsn(IDIV);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "addPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V", false);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(1358, l19);
			mv.visitVarInsn(ALOAD, 0);
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitTypeInsn(NEW, "net/minecraft/potion/PotionEffect");
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "hunger", "Lnet/minecraft/potion/Potion;");
			mv.visitFieldInsn(GETFIELD, "net/minecraft/potion/Potion", "id", "I");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitIntInsn(SIPUSH, 130);
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(IMUL);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "hunger", "Lnet/minecraft/potion/Potion;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getActivePotionEffect", "(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 3);
			Label l21 = new Label();
			mv.visitJumpInsn(IFNONNULL, l21);
			mv.visitInsn(ICONST_0);
			Label l22 = new Label();
			mv.visitJumpInsn(GOTO, l22);
			mv.visitLabel(l21);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 6, new Object[] {"net/minecraft/entity/EntityLivingBase", l20, l20, INTEGER, INTEGER, INTEGER});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/potion/PotionEffect", "getDuration", "()I", false);
			mv.visitLabel(l22);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 7, new Object[] {"net/minecraft/entity/EntityLivingBase", l20, l20, INTEGER, INTEGER, INTEGER, INTEGER});
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_5);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IMUL);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitInsn(IDIV);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "addPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V", false);
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLineNumber(1359, l23);
			mv.visitVarInsn(ALOAD, 0);
			Label l24 = new Label();
			mv.visitLabel(l24);
			mv.visitTypeInsn(NEW, "net/minecraft/potion/PotionEffect");
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, "ic2/core/IC2Potion", "radiation", "Lic2/core/IC2Potion;");
			mv.visitFieldInsn(GETFIELD, "ic2/core/IC2Potion", "id", "I");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitIntInsn(SIPUSH, 180);
			mv.visitInsn(IMUL);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(IMUL);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/potion/Potion", "potionTypes", "[Lnet/minecraft/potion/Potion;");
			mv.visitIntInsn(BIPUSH, 24);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getActivePotionEffect", "(Lnet/minecraft/potion/Potion;)Lnet/minecraft/potion/PotionEffect;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 3);
			Label l25 = new Label();
			mv.visitJumpInsn(IFNONNULL, l25);
			mv.visitInsn(ICONST_0);
			Label l26 = new Label();
			mv.visitJumpInsn(GOTO, l26);
			mv.visitLabel(l25);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 6, new Object[] {"net/minecraft/entity/EntityLivingBase", l24, l24, INTEGER, INTEGER, INTEGER});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/potion/PotionEffect", "getDuration", "()I", false);
			mv.visitLabel(l26);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraft/entity/EntityLivingBase", INTEGER, INTEGER, "net/minecraft/potion/PotionEffect"}, 7, new Object[] {"net/minecraft/entity/EntityLivingBase", l24, l24, INTEGER, INTEGER, INTEGER, INTEGER});
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitInsn(IADD);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_5);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(IMUL);
			mv.visitIntInsn(BIPUSH, 7);
			mv.visitInsn(IDIV);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/potion/PotionEffect", "<init>", "(III)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "addPotionEffect", "(Lnet/minecraft/potion/PotionEffect;)V", false);
			Label l27 = new Label();
			mv.visitLabel(l27);
			mv.visitLineNumber(1360, l27);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(1362, l1);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l28 = new Label();
			mv.visitLabel(l28);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l28, 0);
			mv.visitLocalVariable("aLevel", "I", null, l0, l28, 1);
			mv.visitLocalVariable("aAmountOfItems", "I", null, l0, l28, 2);
			mv.visitLocalVariable("tEffect", "Lnet/minecraft/potion/PotionEffect;", null, l3, l1, 3);
			mv.visitMaxs(9, 4);
			mv.visitEnd();
			didInject = true;
			}

		if (aMethodName.equals("isWearingFullFrostHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullFrostHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1272, l0);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ISTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1273, l3);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getEquipmentInSlot", "(I)Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "providesProtetion_Frost", "(Lnet/minecraft/item/ItemStack;)Z", false);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1274, l5);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(1272, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitInsn(I2B);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_5);
			mv.visitJumpInsn(IF_ICMPLT, l3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1276, l6);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l7, 0);
			mv.visitLocalVariable("i", "B", null, l1, l6, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullHeatHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullHeatHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1280, l0);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ISTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1281, l3);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getEquipmentInSlot", "(I)Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "providesProtetion_Fire", "(Lnet/minecraft/item/ItemStack;)Z", false);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1282, l5);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(1280, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitInsn(I2B);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_5);
			mv.visitJumpInsn(IF_ICMPLT, l3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1284, l6);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l7, 0);
			mv.visitLocalVariable("i", "B", null, l1, l6, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullBioHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullBioHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1288, l0);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ISTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1289, l3);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getEquipmentInSlot", "(I)Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "providesProtetion_Biohazard", "(Lnet/minecraft/item/ItemStack;)Z", false);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1290, l5);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(1288, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitInsn(I2B);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_5);
			mv.visitJumpInsn(IF_ICMPLT, l3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1292, l6);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l7, 0);
			mv.visitLocalVariable("i", "B", null, l1, l6, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullRadioHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullRadioHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1296, l0);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ISTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1297, l3);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getEquipmentInSlot", "(I)Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "providesProtetion_Radiation", "(Lnet/minecraft/item/ItemStack;)Z", false);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1298, l5);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(1296, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitInsn(I2B);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_5);
			mv.visitJumpInsn(IF_ICMPLT, l3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1300, l6);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l7, 0);
			mv.visitLocalVariable("i", "B", null, l1, l6, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullElectroHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullElectroHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1304, l0);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ISTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1305, l3);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getEquipmentInSlot", "(I)Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "providesProtetion_Electricity", "(Lnet/minecraft/item/ItemStack;)Z", false);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1306, l5);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(1304, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitInsn(I2B);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_5);
			mv.visitJumpInsn(IF_ICMPLT, l3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1308, l6);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l7, 0);
			mv.visitLocalVariable("i", "B", null, l1, l6, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			didInject = true;
		}

		if (aMethodName.equals("isWearingFullGasHazmat")){
			Preloader_Logger.LOG("Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "isWearingFullGasHazmat", "(Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1312, l0);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ISTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			Label l2 = new Label();
			mv.visitJumpInsn(GOTO, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(1313, l3);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "getEquipmentInSlot", "(I)Lnet/minecraft/item/ItemStack;", false);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "providesProtetion_Gas", "(Lnet/minecraft/item/ItemStack;)Z", false);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1314, l5);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(1312, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitInsn(I2B);
			mv.visitVarInsn(ISTORE, 1);
			mv.visitLabel(l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_5);
			mv.visitJumpInsn(IF_ICMPLT, l3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1316, l6);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLocalVariable("aEntity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l7, 0);
			mv.visitLocalVariable("i", "B", null, l1, l6, 1);
			mv.visitMaxs(2, 2);
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
