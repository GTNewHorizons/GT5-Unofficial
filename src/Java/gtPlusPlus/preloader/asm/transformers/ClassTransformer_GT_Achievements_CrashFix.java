package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class ClassTransformer_GT_Achievements_CrashFix {	

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;
	private static boolean mDidRemoveAssLineRecipeAdder = false;

	public ClassTransformer_GT_Achievements_CrashFix(byte[] basicClass) {

		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);	

		aTempReader.accept(new MethodAdaptor(aTempWriter), 0);

		if (mDidRemoveAssLineRecipeAdder) {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Patching GT .09");	
			injectMethod(aTempWriter);
		}
		else {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Patch not required, skipping.");				
		}

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}		

		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Valid? "+isValid+".");	
		reader = aTempReader;
		writer = aTempWriter;		
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

	public boolean injectMethod(ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Injecting " + "registerAssAchievement" + ".");

		/**
		 * Inject new, safer code
		 */


		mv = cw.visitMethod(ACC_PUBLIC, "registerAssAchievement", "(Lgregtech/api/util/GT_Recipe;)Lnet/minecraft/stats/Achievement;", null, null);
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(293, l3);
		mv.visitVarInsn(ALOAD, 1);
		Label l4 = new Label();
		mv.visitJumpInsn(IFNONNULL, l4);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLineNumber(295, l5);
		mv.visitLdcInsn("Someone tried to register an achievement for an invalid recipe. Please report this to Alkalus.");
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLineNumber(294, l6);
		mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/api/objects/Logger", "INFO", "(Ljava/lang/String;)V", false);
		Label l7 = new Label();
		mv.visitLabel(l7);
		mv.visitLineNumber(296, l7);
		mv.visitInsn(ACONST_NULL);
		mv.visitInsn(ARETURN);
		mv.visitLabel(l4);
		mv.visitLineNumber(298, l4);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
		Label l8 = new Label();
		mv.visitJumpInsn(IFNONNULL, l8);
		Label l9 = new Label();
		mv.visitLabel(l9);
		mv.visitLineNumber(300, l9);
		mv.visitLdcInsn("Someone tried to register an achievement for a recipe with null output. Please report this to Alkalus.");
		Label l10 = new Label();
		mv.visitLabel(l10);
		mv.visitLineNumber(299, l10);
		mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/api/objects/Logger", "INFO", "(Ljava/lang/String;)V", false);
		Label l11 = new Label();
		mv.visitLabel(l11);
		mv.visitLineNumber(301, l11);
		mv.visitInsn(ACONST_NULL);
		mv.visitInsn(ARETURN);
		mv.visitLabel(l8);
		mv.visitLineNumber(303, l8);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
		mv.visitVarInsn(ASTORE, 3);
		mv.visitLabel(l0);
		mv.visitLineNumber(305, l0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitLabel(l1);
		mv.visitLineNumber(306, l1);
		Label l12 = new Label();
		mv.visitJumpInsn(GOTO, l12);
		mv.visitLabel(l2);
		mv.visitFrame(F_FULL, 4, new Object[] {"gregtech/loaders/misc/GT_Achievements", "gregtech/api/util/GT_Recipe", TOP, "net/minecraft/item/ItemStack"}, 1, new Object[] {"java/lang/Throwable"});
		mv.visitVarInsn(ASTORE, 4);
		Label l13 = new Label();
		mv.visitLabel(l13);
		mv.visitLineNumber(307, l13);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/ItemUtils", "getUnlocalizedItemName", "(Lnet/minecraft/item/ItemStack;)Ljava/lang/String;", false);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitLabel(l12);
		mv.visitLineNumber(311, l12);
		mv.visitFrame(F_FULL, 4, new Object[] {"gregtech/loaders/misc/GT_Achievements", "gregtech/api/util/GT_Recipe", "java/lang/String", "net/minecraft/item/ItemStack"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "gregtech/loaders/misc/GT_Achievements", "achievementList", "Ljava/util/concurrent/ConcurrentHashMap;");
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/concurrent/ConcurrentHashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
		Label l14 = new Label();
		mv.visitJumpInsn(IFNONNULL, l14);
		Label l15 = new Label();
		mv.visitLabel(l15);
		mv.visitLineNumber(312, l15);
		mv.visitFieldInsn(GETSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);
		mv.visitFieldInsn(PUTSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
		Label l16 = new Label();
		mv.visitLabel(l16);
		mv.visitLineNumber(313, l16);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
		mv.visitIntInsn(BIPUSH, 11);
		mv.visitFieldInsn(GETSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
		mv.visitInsn(ICONST_5);
		mv.visitInsn(IREM);
		mv.visitInsn(IADD);
		mv.visitInsn(INEG);
		Label l17 = new Label();
		mv.visitLabel(l17);
		mv.visitLineNumber(314, l17);
		mv.visitFieldInsn(GETSTATIC, "gregtech/loaders/misc/GT_Achievements", "assReg", "I");
		mv.visitInsn(ICONST_5);
		mv.visitInsn(IDIV);
		mv.visitIntInsn(BIPUSH, 8);
		mv.visitInsn(ISUB);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
		mv.visitLdcInsn("NO_REQUIREMENT");
		mv.visitInsn(ICONST_0);
		Label l18 = new Label();
		mv.visitLabel(l18);
		mv.visitLineNumber(313, l18);
		mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/loaders/misc/GT_Achievements", "registerAchievement", "(Ljava/lang/String;IILnet/minecraft/item/ItemStack;Ljava/lang/String;Z)Lnet/minecraft/stats/Achievement;", false);
		mv.visitVarInsn(ASTORE, 4);
		Label l19 = new Label();
		mv.visitLabel(l19);
		mv.visitLineNumber(315, l19);
		Label l20 = new Label();
		mv.visitJumpInsn(GOTO, l20);
		mv.visitLabel(l14);
		mv.visitLineNumber(317, l14);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ASTORE, 4);
		mv.visitLabel(l20);
		mv.visitLineNumber(319, l20);
		mv.visitFrame(F_APPEND,1, new Object[] {"net/minecraft/stats/Achievement"}, 0, null);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitInsn(ARETURN);
		Label l21 = new Label();
		mv.visitLabel(l21);
		mv.visitLocalVariable("this", "Lgregtech/loaders/misc/GT_Achievements;", null, l3, l21, 0);
		mv.visitLocalVariable("recipe", "Lgregtech/api/util/GT_Recipe;", null, l3, l21, 1);
		mv.visitLocalVariable("aSafeUnlocalName", "Ljava/lang/String;", null, l1, l2, 2);
		mv.visitLocalVariable("aSafeUnlocalName", "Ljava/lang/String;", null, l12, l21, 2);
		mv.visitLocalVariable("aStack", "Lnet/minecraft/item/ItemStack;", null, l0, l21, 3);
		mv.visitLocalVariable("t", "Ljava/lang/Throwable;", null, l13, l12, 4);
		mv.visitLocalVariable("aYouDidSomethingInGT", "Lnet/minecraft/stats/Achievement;", null, l19, l14, 4);
		mv.visitLocalVariable("aYouDidSomethingInGT", "Lnet/minecraft/stats/Achievement;", null, l20, l21, 4);
		mv.visitMaxs(7, 5);
		mv.visitEnd();



		didInject = true;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Method injection complete.");
		return didInject;
	}



	public class MethodAdaptor extends ClassVisitor {

		public MethodAdaptor(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;
			if (name.equals("registerAssAchievement")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Found method " + name + ", removing.");
				methodVisitor = null;
				mDidRemoveAssLineRecipeAdder = true;				
			} else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}
			return methodVisitor;
		}
	}

}
