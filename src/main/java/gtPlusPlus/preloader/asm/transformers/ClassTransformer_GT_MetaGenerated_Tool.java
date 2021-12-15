package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import gtPlusPlus.preloader.Preloader_Logger;

public class ClassTransformer_GT_MetaGenerated_Tool {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public ClassTransformer_GT_MetaGenerated_Tool(byte[] basicClass) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new LocalClassVisitor(aTempWriter), 0);
		boolean completed = injectMethod("getSubItems", aTempWriter);
		if (aTempReader != null && aTempWriter != null && completed) {
			isValid = true;
		} else {
			isValid = false;
		}
		Preloader_Logger.LOG("GT Tool Fix", Level.INFO, "Valid? " + isValid + ".");
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

	public boolean injectMethod(String aMethodName, ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;
		Preloader_Logger.LOG("GT Tool Fix", Level.INFO, "Injecting " + aMethodName + ".");	
		if (aMethodName.equals("getSubItems")) {
			{
				mv = cw.visitMethod(ACC_PUBLIC + ACC_FINAL, "getSubItems", "(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", null, null);
				{
					AnnotationVisitor av0 = mv.visitAnnotation("Lcpw/mods/fml/relauncher/SideOnly;", true);
					av0.visitEnum("value", "Lcpw/mods/fml/relauncher/Side;", "CLIENT");
					av0.visitEnd();
				}
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(321, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitVarInsn(ALOAD, 2);
				mv.visitVarInsn(ALOAD, 3);				
				mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/helpers/MethodHelper_GT", "getSubItems", "(Lgregtech/api/items/GT_MetaGenerated_Tool;Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(322, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "Lgregtech/api/items/GT_MetaGenerated_Tool;", null, l0, l2, 0);
				mv.visitLocalVariable("var1", "Lnet/minecraft/item/Item;", null, l0, l2, 1);
				mv.visitLocalVariable("aCreativeTab", "Lnet/minecraft/creativetab/CreativeTabs;", null, l0, l2, 2);
				mv.visitLocalVariable("aList", "Ljava/util/List;", null, l0, l2, 3);
				mv.visitMaxs(4, 4);
				mv.visitEnd();
				didInject = true;
			}

		}
		Preloader_Logger.LOG("GT Tool Fix", Level.INFO, "Method injection complete.");
		return didInject;
	}

	public final class LocalClassVisitor extends ClassVisitor {

		public LocalClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {		
			MethodVisitor methodVisitor;	
			if (name.equals("getSubItems") && desc.equals("(Lnet/minecraft/item/Item;Lnet/minecraft/creativetab/CreativeTabs;Ljava/util/List;)V")) {
				methodVisitor = null;
				Preloader_Logger.LOG("GT Tool Fix", Level.INFO, "Found method "+name+", removing.");	
			}	
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);	
			}
			return methodVisitor;
		}
	}

}
