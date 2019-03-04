package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.preloader.DevHelper;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ClassTransformer_TC_ItemWispEssence {	

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public ClassTransformer_TC_ItemWispEssence(byte[] basicClass, boolean obfuscated2) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		boolean obfuscated = obfuscated2;
		FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Are we patching obfuscated methods? "+obfuscated);
		String aGetColour = obfuscated ? DevHelper.getSRG("getColorFromItemStack") : DevHelper.getForge("getColorFromItemStack");
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);	
		aTempReader.accept(new AddAdapter(aTempWriter, new String[] {"getAspects", aGetColour}), 0);
		injectMethod("getAspects", aTempWriter, obfuscated);
		injectMethod(aGetColour, aTempWriter, obfuscated);
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

		String aGetColour = obfuscated ? DevHelper.getSRG("getColorFromItemStack") : DevHelper.getForge("getColorFromItemStack");

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
		else if (aMethodName.equals(aGetColour)) {

			//thaumcraft/common/items/ItemWispEssence
			mv = cw.visitMethod(ACC_PUBLIC, aGetColour, "(Lnet/minecraft/item/ItemStack;I)I", null, null);
			AnnotationVisitor av0;
			{
				av0 = mv.visitAnnotation("Lcpw/mods/fml/relauncher/SideOnly;", true);
				av0.visitEnum("value", "Lcpw/mods/fml/relauncher/Side;", "CLIENT");
				av0.visitEnd();
			}
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(197, l0);
			mv.visitVarInsn(ALOAD, 1);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(198, l2);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(200, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/common/items/ItemWispEssence", "getAspects", "(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;", false);
			Label l3 = new Label();
			mv.visitJumpInsn(IFNULL, l3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(201, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/common/items/ItemWispEssence", "getAspects", "(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "getAspects", "()[Lthaumcraft/api/aspects/Aspect;", false);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/Aspect", "getColor", "()I", false);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l3);
			mv.visitLineNumber(203, l3);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
			mv.visitLdcInsn(new Long(500L));
			mv.visitInsn(LDIV);
			mv.visitFieldInsn(GETSTATIC, "thaumcraft/common/items/ItemWispEssence", "displayAspects", "[Lthaumcraft/api/aspects/Aspect;");
			mv.visitInsn(ARRAYLENGTH);
			mv.visitInsn(I2L);
			mv.visitInsn(LREM);
			mv.visitInsn(L2I);
			mv.visitVarInsn(ISTORE, 3);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(204, l5);
			mv.visitFieldInsn(GETSTATIC, "thaumcraft/common/items/ItemWispEssence", "displayAspects", "[Lthaumcraft/api/aspects/Aspect;");
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/Aspect", "getColor", "()I", false);
			mv.visitInsn(IRETURN);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLocalVariable("this", "Lthaumcraft/common/items/ItemWispEssence;", null, l0, l6, 0);
			mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l6, 1);
			mv.visitLocalVariable("par2", "I", null, l0, l6, 2);
			mv.visitLocalVariable("idx", "I", null, l5, l6, 3);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
			didInject = true;				
		}

		FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Method injection complete. "+(obfuscated ? "Obfuscated" : "Non-Obfuscated"));
		return didInject;
	}

	public class AddAdapter extends ClassVisitor {

		public AddAdapter(ClassVisitor cv, String[] aMethods) {
			super(ASM5, cv);
			this.cv = cv;
			this.aMethodsToStrip = aMethods;
		}

		private final String[] aMethodsToStrip;


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
	static Aspect[] displayAspects;

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int par2) {
		if (stack == null) {
			return 0;
		}		
		if (this.getAspects(stack) != null) {
			return this.getAspects(stack).getAspects()[0].getColor();
		} else {
			int idx = (int) (System.currentTimeMillis() / 500L % (long) displayAspects.length);
			return displayAspects[idx].getColor();
		}
	}

	public AspectList getAspects(ItemStack itemstack) {
		if (itemstack.hasTagCompound()) {
			AspectList aspects = new AspectList();
			aspects.readFromNBT(itemstack.getTagCompound());
			return aspects.size() > 0 ? aspects : null;
		} else {
			return null;
		}
	}




}
