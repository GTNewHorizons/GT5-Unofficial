package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class Preloader_ClassTransformer {	

	public static final class OreDictionaryVisitor extends ClassVisitor {

		public OreDictionaryVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			if(name.equals("registerOreImpl") && desc.equals("(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V")) {
				FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Found target method. [Unobfuscated]");
				return new RegisterOreImplVisitor(methodVisitor, false);
			}
			else if(name.equals("registerOreImpl") && desc.equals("(Ljava/lang/String;Ladd;)V")) {
				FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Found target method. [Obfuscated]");
				return new RegisterOreImplVisitor(methodVisitor, true);
			}
			return methodVisitor;
		}

	}

	private static final class RegisterOreImplVisitor extends MethodVisitor {

		private final boolean mObfuscated;
		
		public RegisterOreImplVisitor(MethodVisitor mv, boolean obfuscated) {
			super(ASM5, mv);
			this.mObfuscated = obfuscated;
		}

		@Override
		public void visitCode() {
			FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Fixing Forge's poor attempt at an oreDictionary.");
			super.visitCode();
			super.visitVarInsn(ALOAD, 0);
			super.visitVarInsn(ALOAD, 1);
			if (!mObfuscated){
				FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Injecting target method. [Unobfuscated]");				
			super.visitMethodInsn(INVOKESTATIC, 
					"gtPlusPlus/preloader/Preloader_GT_OreDict", 
					"shouldPreventRegistration",
					"(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)Z",
					false);
			}
			else {
				FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Injecting target method. [Obfuscated]");
				super.visitMethodInsn(INVOKESTATIC, 
						"gtPlusPlus/preloader/Preloader_GT_OreDict", 
						"shouldPreventRegistration",
						"(Ljava/lang/String;Ladd;)Z",
						false);
			}
			Label endLabel = new Label();
			super.visitJumpInsn(IFEQ, endLabel);
			super.visitInsn(RETURN);
			super.visitLabel(endLabel);
		}

	}

}
