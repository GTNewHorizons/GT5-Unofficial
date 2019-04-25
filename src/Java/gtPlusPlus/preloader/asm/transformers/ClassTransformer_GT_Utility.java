package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LLOAD;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

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
		
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Utilities Patch", Level.INFO, "Valid patch? " + isValid + ".");
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Utilities Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("getTier");			
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
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Utilities Patch", Level.INFO, "Injecting " + aMethodName + ".");		
		if (aMethodName.equals("getTier")) {
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getTier", "(J)B", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(23, l0);
			mv.visitVarInsn(LLOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/Utils", "getTier", "(J)B", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("l", "J", null, l0, l1, 0);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
			didInject = true;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Utilities Patch", Level.INFO, "Method injection complete.");
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
			} else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}

			if (methodVisitor == null) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Utilities Patch", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}
	}

}
