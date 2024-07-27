package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_APPEND;
import static org.objectweb.asm.Opcodes.F_CHOP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.F_SAME1;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.I2L;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.L2I;
import static org.objectweb.asm.Opcodes.LDIV;
import static org.objectweb.asm.Opcodes.LREM;
import static org.objectweb.asm.Opcodes.NEW;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.AnnotationVisitor;
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
        FMLRelaunchLog.log(
            "[GT++ ASM] Thaumcraft WispEssence_Patch",
            Level.INFO,
            "Are we patching obfuscated methods? " + obfuscated2);
        String aGetColour = obfuscated2 ? "func_82790_a" : "getColorFromItemStack";
        aTempReader = new ClassReader(basicClass);
        aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
        aTempReader.accept(new AddAdapter(aTempWriter, new String[] { "getAspects", aGetColour }), 0);
        injectMethod("getAspects", aTempWriter, obfuscated2);
        injectMethod(aGetColour, aTempWriter, obfuscated2);
        if (aTempReader != null && aTempWriter != null) {
            isValid = true;
        } else {
            isValid = false;
        }
        FMLRelaunchLog.log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Valid? " + isValid + ".");
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

        String aGetColour = obfuscated ? "func_82790_a" : "getColorFromItemStack";
        String aHasTagCompound = obfuscated ? "func_77942_o" : "hasTagCompound";
        String aGetTagCompound = obfuscated ? "func_77978_p" : "getTagCompound";

        if (aMethodName.equals("getAspects")) {
            mv = cw.visitMethod(
                ACC_PUBLIC,
                "getAspects",
                "(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;",
                null,
                null);
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
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", aHasTagCompound, "()Z", false);
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
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "net/minecraft/item/ItemStack",
                aGetTagCompound,
                "()Lnet/minecraft/nbt/NBTTagCompound;",
                false);
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "thaumcraft/api/aspects/AspectList",
                "readFromNBT",
                "(Lnet/minecraft/nbt/NBTTagCompound;)V",
                false);
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
            mv.visitFrame(F_APPEND, 1, new Object[] { "thaumcraft/api/aspects/AspectList" }, 0, null);
            mv.visitInsn(ACONST_NULL);
            mv.visitLabel(l8);
            mv.visitFrame(F_SAME1, 0, null, 1, new Object[] { "thaumcraft/api/aspects/AspectList" });
            mv.visitInsn(ARETURN);
            mv.visitLabel(l3);
            mv.visitLineNumber(149, l3);
            mv.visitFrame(F_CHOP, 1, null, 0, null);
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitLocalVariable(
                "this",
                "LgtPlusPlus/preloader/asm/transformers/ClassTransformer_TC_ItemWispEssence;",
                null,
                l0,
                l9,
                0);
            mv.visitLocalVariable("itemstack", "Lnet/minecraft/item/ItemStack;", null, l0, l9, 1);
            mv.visitLocalVariable("aspects", "Lthaumcraft/api/aspects/AspectList;", null, l5, l3, 2);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
            didInject = true;
        } else if (aMethodName.equals(aGetColour)) {

            // thaumcraft/common/items/ItemWispEssence
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
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "thaumcraft/common/items/ItemWispEssence",
                "getAspects",
                "(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;",
                false);
            Label l3 = new Label();
            mv.visitJumpInsn(IFNULL, l3);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(201, l4);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "thaumcraft/common/items/ItemWispEssence",
                "getAspects",
                "(Lnet/minecraft/item/ItemStack;)Lthaumcraft/api/aspects/AspectList;",
                false);
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "thaumcraft/api/aspects/AspectList",
                "getAspects",
                "()[Lthaumcraft/api/aspects/Aspect;",
                false);
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
            mv.visitFieldInsn(
                GETSTATIC,
                "thaumcraft/common/items/ItemWispEssence",
                "displayAspects",
                "[Lthaumcraft/api/aspects/Aspect;");
            mv.visitInsn(ARRAYLENGTH);
            mv.visitInsn(I2L);
            mv.visitInsn(LREM);
            mv.visitInsn(L2I);
            mv.visitVarInsn(ISTORE, 3);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(204, l5);
            mv.visitFieldInsn(
                GETSTATIC,
                "thaumcraft/common/items/ItemWispEssence",
                "displayAspects",
                "[Lthaumcraft/api/aspects/Aspect;");
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

        FMLRelaunchLog.log(
            "[GT++ ASM] Thaumcraft WispEssence_Patch",
            Level.INFO,
            "Method injection complete. " + (obfuscated ? "Obfuscated" : "Non-Obfuscated"));
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
            } else {
                methodVisitor = null;
            }

            if (found) {
                FMLRelaunchLog
                    .log("[GT++ ASM] Thaumcraft WispEssence_Patch", Level.INFO, "Found method " + name + ", removing.");
            }
            return methodVisitor;
        }
    }
}
