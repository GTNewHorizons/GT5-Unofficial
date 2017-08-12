package gtPlusPlus.preloader.asm;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.launchwrapper.IClassTransformer;

public class Preloader_ClassTransformer implements IClassTransformer {

	@Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(transformedName.equals("net.minecraftforge.oredict.OreDictionary")) {
            FMLRelaunchLog.log("[GT++] OreDictTransformer", Level.INFO, "Transforming %s", transformedName);
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
                FMLRelaunchLog.log("[GT++] OreDictTransformer", Level.INFO, "Found target method.");
                return new RegisterOreImplVisitor(methodVisitor);
            }
            return methodVisitor;
        }
        
    }
    
    private static final class RegisterOreImplVisitor extends MethodVisitor {

        public RegisterOreImplVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @SuppressWarnings("deprecation")
		@Override
        public void visitCode() {
            super.visitCode();
            super.visitVarInsn(ALOAD, 0);
            super.visitVarInsn(ALOAD, 1);
            super.visitMethodInsn(INVOKESTATIC, 
            		//"gtPlusPlus/preloader/Preloader_GT_OreDict", "removeCircuit", "(Lnet/minecraft/item/ItemStack;)Z, false");
            		 "gtPlusPlus/preloader/Preloader_GT_OreDict", 
                     "shouldPreventRegistration",
                     "(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)Z",
                     false);
            Label endLabel = new Label();
            super.visitJumpInsn(IFEQ, endLabel);
            super.visitInsn(RETURN);
            super.visitLabel(endLabel);
        }
        
    }

}
