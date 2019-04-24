package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.core.util.minecraft.HazmatUtils;
import net.minecraft.entity.EntityLivingBase;

public class ClassTransformer_IC2_Hazmat {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;
	private final String className;
	
	public static boolean hasCompleteHazmat(EntityLivingBase living) {
		return HazmatUtils.hasCompleteHazmat(living);
	}

	public ClassTransformer_IC2_Hazmat(byte[] basicClass, String aClassName) {
		className = aClassName;
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;			

		FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO, "Attempting to patch in mode " + className + ".");

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter, className), 0);

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		
		FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO, "Valid patch? " + isValid + ".");
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("hasCompleteHazmat");			
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
		
		boolean isObf;
		try {
			isObf = Class.forName("net.minecraft.entity.EntityLivingBase") == null;
		} catch (ClassNotFoundException e) {
			isObf = true;
		}
		String aEntityLivingBase = "net/minecraft/entity/EntityLivingBase";
		if (isObf) {
			aEntityLivingBase = "sv";
		}
		FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO, "Method Handler: "+aEntityLivingBase);
		
		
		FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO, "Injecting " + aMethodName + ".");		
		if (aMethodName.equals("hasCompleteHazmat")) {
			
			//Bad Local Variable - https://pastebin.com/TUCfdHqS
			/*mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "hasCompleteHazmat", "(L"+aEntityLivingBase+";)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(23, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "hasCompleteHazmat", "(L"+aEntityLivingBase+";)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);			
			mv.visitLocalVariable("this", "Lic2/core/item/armor/ItemArmorHazmat;", null, l0, l1, 0);
			mv.visitLocalVariable("living", "L"+aEntityLivingBase+";", null, l0, l1, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();*/
			
			
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "hasCompleteHazmat", "(L"+aEntityLivingBase+";)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(24, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/core/util/minecraft/HazmatUtils", "hasCompleteHazmat", "(L"+aEntityLivingBase+";)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("living", "L"+aEntityLivingBase+";", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO, "Method injection complete.");
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

			if (name.equals("hasCompleteHazmat")) {
				methodVisitor = null;
			} else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}

			if (methodVisitor == null) {
				FMLRelaunchLog.log("[GT++ ASM] IC2 Hazmat Patch", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}
	}

}
