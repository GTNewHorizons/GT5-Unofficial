package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class ClassTransformer_TC_ItemWispEssence {	

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public ClassTransformer_TC_ItemWispEssence(byte[] basicClass, boolean obfuscated2) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;	

		boolean obfuscated = false;
		boolean a1 = false;
		boolean a2 = false;		

		if (a1) {
			obfuscated = false;
		}
		else if (a2) {
			obfuscated = true;
		}
		else {
			//Fallback
			obfuscated = false;
		}	
		FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Are we patching obfuscated methods? "+obfuscated);	

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);	
		aTempReader.accept(new AddAdapter(aTempWriter), 0);

		injectMethod("getAspects", aTempWriter, obfuscated);

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}		

		FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Valid? "+isValid+".");	
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

	public boolean injectMethod(String aMethodName, ClassWriter cw, boolean obfuscated) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Injecting " + aMethodName + ".");


		if (aMethodName.equals("getAspects")) {
			mv = cw.visitMethod(ACC_PUBLIC, "getAspects", "(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(141, l0);
			mv.visitVarInsn(ALOAD, 1);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(142, l2);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(144, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "hasTagCompound", "()Z", false);
			Label l3 = new Label();
			mv.visitJumpInsn(IFEQ, l3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(145, l4);
			mv.visitTypeInsn(NEW, "thaumcraft/api/aspects/AspectList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "thaumcraft/api/aspects/AspectList", "<init>", "()V", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(146, l5);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getTagCompound", "()Lnet/minecraft/nbt/NBTTagCompound;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "readFromNBT", "(Lnet/minecraft/nbt/NBTTagCompound;)V", false);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(147, l6);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "size", "()I", false);
			Label l7 = new Label();
			mv.visitJumpInsn(IFLE, l7);
			mv.visitVarInsn(ALOAD, 2);
			Label l8 = new Label();
			mv.visitJumpInsn(GOTO, l8);
			mv.visitLabel(l7);
			mv.visitFrame(F_APPEND,1, new Object[] {"thaumcraft/api/aspects/AspectList"}, 0, null);
			mv.visitInsn(ACONST_NULL);
			mv.visitLabel(l8);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"thaumcraft/api/aspects/AspectList"});
			mv.visitInsn(ARETURN);
			mv.visitLabel(l3);
			mv.visitLineNumber(149, l3);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLocalVariable("this", "LgtPlusPlus/preloader/asm/transformers/ClassTransformer_TC_ItemWispEssence;", null, l0, l9, 0);
			mv.visitLocalVariable("itemstack", "Lnet/minecraft/item/ItemStack;", null, l0, l9, 1);
			mv.visitLocalVariable("aspects", "Lthaumcraft/api/aspects/AspectList;", null, l5, l3, 2);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
			didInject = true;
		}

		FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Method injection complete. "+(obfuscated ? "Obfuscated" : "Non-Obfuscated"));
		return didInject;
	}

	public class AddAdapter extends ClassVisitor {

		public AddAdapter(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		private final String[] aMethodsToStrip = new String[] {"getAspects"};


		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

			MethodVisitor methodVisitor;			
			boolean found = false;

			for (String s : aMethodsToStrip) {
				if (name.equals(s)) {
					found = true;
					break;
				}
			}
			if (!found) {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);				
			}
			else {
				methodVisitor = null;
			}

			if (found) {
				FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}

	}




}
