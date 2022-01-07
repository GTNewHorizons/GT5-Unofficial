package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import gtPlusPlus.preloader.Preloader_Logger;

public class ClassTransformer_CC_GuiContainerManager {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public ClassTransformer_CC_GuiContainerManager(byte[] basicClass) {
		
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		localClassVisitor aTempMethodRemover = new localClassVisitor(aTempWriter);
		aTempReader.accept(aTempMethodRemover, 0);

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		
		Preloader_Logger.LOG("CodeChicken GuiContainerManager Patch", Level.INFO, "Valid patch? " + isValid + ".");
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			Preloader_Logger.LOG("CodeChicken GuiContainerManager Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("mouseUp");
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
		String aClassName = "codechicken/nei/guihook/GuiContainerManager";
		ClassWriter cw = getWriter();	
		if (aMethodName.equals("mouseUp")) {
			Preloader_Logger.LOG("CodeChicken GuiContainerManager Patch", Level.INFO, "Injecting " + aMethodName + ", static replacement call to "+aClassName+".");
			mv = cw.visitMethod(ACC_PUBLIC, "mouseUp", "(III)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(12, l0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/helpers/MethodHelper_CC", "mouseUp", "(III)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(13, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "L+aClassName+;", null, l0, l2, 0);
			mv.visitLocalVariable("mousex", "I", null, l0, l2, 1);
			mv.visitLocalVariable("mousey", "I", null, l0, l2, 2);
			mv.visitLocalVariable("button", "I", null, l0, l2, 3);
			mv.visitMaxs(3, 4);
			mv.visitEnd();
			didInject = true;
		}		
		
		Preloader_Logger.LOG("CodeChicken GuiContainerManager Patch", Level.INFO, "Method injection complete.");
		return didInject;
	}

	public final class localClassVisitor extends ClassVisitor {

		boolean obfuscated = false;
		
		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		public boolean getObfuscatedRemoval() {
			return obfuscated;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;
			
			if (name.equals("mouseUp")) {	
				methodVisitor = null;
			}
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}

			if (methodVisitor == null) {
				Preloader_Logger.LOG("CodeChicken GuiContainerManager Patch", Level.INFO, "Found method " + name + ", removing.");
				Preloader_Logger.LOG("CodeChicken GuiContainerManager Patch", Level.INFO, "Descriptor: "+desc);
			}
			return methodVisitor;
		}
	}

}
