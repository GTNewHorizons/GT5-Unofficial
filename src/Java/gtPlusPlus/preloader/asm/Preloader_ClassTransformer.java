package gtPlusPlus.preloader.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.Iterator;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import gtPlusPlus.core.util.Utils;
import net.minecraft.launchwrapper.IClassTransformer;

public class Preloader_ClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {

		/*if (name.equals("abq")) {
			Utils.LOG_ASM("[ASM] INSIDE OBFUSCATED EXPLOSION TRANSFORMER ABOUT TO PATCH: " + name);
			return patchClassASM(name, basicClass, true);
		}

		else if (name.equals("net.minecraftforge.oredict.OreDictionary")) {
			Utils.LOG_ASM("[ASM] INSIDE OREDICT TRANSFORMER ABOUT TO PATCH: " + name);
			return patchClassASM(name, basicClass, false);
		}*/
		return basicClass;
	}

	public byte[] patchClassASM(String name, byte[] bytes, boolean obfuscated) {

		String targetMethodName = "";

		if(obfuscated == true)
			targetMethodName ="a";
		else
			targetMethodName ="registerOreImpl";


		//set up ASM class manipulation stuff. Consult the ASM docs for details
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);



		//Now we loop over all of the methods declared inside the Explosion class until we get to the targetMethodName "doExplosionB"

		// find method to inject into
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while(methods.hasNext())
		{
			MethodNode m = methods.next();
			Utils.LOG_ASM("[ASM] Method Name: "+m.name + " Desc:" + m.desc);

			//Check if this is doExplosionB and it's method signature is (Z)V which means that it accepts a boolean (Z) and returns a void (V)
			if ((m.name.equals(targetMethodName) && m.desc.equals("(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)V")))
			{
				Utils.LOG_ASM("[ASM] Inside target method!");
				// find interesting instructions in method, there is a single FDIV instruction we use as target
				
				// make new instruction list
				InsnList toInject = new InsnList();

				//toInject.add(new VarInsnNode(ALOAD, 0));
				
				toInject.add(new VarInsnNode(ALOAD, 1));
				toInject.add(new MethodInsnNode(INVOKESTATIC, "gtPlusPlus/preloader/Preloader_GT_OreDict", "removeCircuit", "(Lnet/minecraft/item/ItemStack;)Z, false"));
				toInject.add(new VarInsnNode(Opcodes.IFEQ, 1));
				toInject.add(new VarInsnNode(Opcodes.RETURN, 0));
				//toInject.add(new VarInsnNode(org.objectweb.asm.Opcodes.LABEL END, 0));

				// inject new instruction list into method instruction list
				m.instructions.insert(toInject);

				Utils.LOG_ASM("Patching Complete!");
				break;
			}
		}

		//ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}

}
