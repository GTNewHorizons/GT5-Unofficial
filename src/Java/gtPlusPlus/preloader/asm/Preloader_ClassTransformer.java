package gtPlusPlus.preloader.asm;

import static org.objectweb.asm.Opcodes.*;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.preloader.CORE_Preloader;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.config.Configuration;

public class Preloader_ClassTransformer implements IClassTransformer {

	public boolean getConfig(){
		final Configuration config = new Configuration(	new File(Utils.getMcDir(), "config/GTplusplus/GTplusplus.cfg"));
		if (config != null){		
			config.load();
			// Circuits
			CORE_Preloader.enableOldGTcircuits = config.getBoolean("enableOldGTcircuits", "gregtech", false,
					"Restores circuits and their recipes from Pre-5.09.28 times.");
			Utils.LOG_INFO("GT++ ASM - Loaded the configuration file.");
			return CORE_Preloader.enableOldGTcircuits;
		}
		Utils.LOG_INFO("GT++ ASM - Failed loading the configuration file.");
		return false;
	}


	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
			if(transformedName.equals("net.minecraftforge.oredict.OreDictionary")) {
				FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "Transforming %s", transformedName);
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
				return classWriter.toByteArray();
			}
		return basicClass;
	}

	private static final class OreDictionaryVisitor extends ClassVisitor {

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
