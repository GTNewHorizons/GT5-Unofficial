package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ClassTransformer_Railcraft_InvTools {	

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	
	private boolean obfuscated = true;


	public static ItemStack depleteItem(ItemStack stack) {
		if (stack == null) {
			return GT_Values.NI;
		} else {
			if (stack.stackSize <= 1) {
				ItemStack container = stack.getItem().getContainerItem(stack);
				if (container != null) {
					return container;
				} else {
					return GT_Values.NI;
				}
			} else {
				ItemUtils.depleteStack(stack);
				return stack;
			}
		}
	}

	public ClassTransformer_Railcraft_InvTools(byte[] basicClass, boolean obfuscated2) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;	

		boolean devEnv = false;
		boolean a1 = false;
		boolean a2 = false;

		//Find Non-Obf method
		try {
			Method aGetStackInSlot = IInventory.class.getDeclaredMethod("getStackInSlot", int.class);
			if (aGetStackInSlot != null) {
				a1 = true;
			}
		} catch (NoSuchMethodException | SecurityException e) {}

		//Find Obf method
		try {
			Method aGetStackInSlotObf = IInventory.class.getDeclaredMethod("func_70301_a", int.class);
			if (aGetStackInSlotObf != null) {
				a2 = true;
			}
		} catch (NoSuchMethodException | SecurityException e) {}


		if (a1) {
			devEnv = false;
		}
		else if (a2) {
			devEnv = true;
		}
		else {
			//Fallback
			devEnv = false;
		}	

		if (obfuscated && devEnv) {
			
		}
		else {
			
		
		}



		FMLRelaunchLog.log("[GT++ ASM] Railcraft negative ItemStack Fix", Level.INFO, "Are we patching obfuscated method? "+obfuscated);	

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);	
		aTempReader.accept(new AddFieldAdapter(aTempWriter), 0);

		injectMethod("depleteItem", aTempWriter, obfuscated);

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}		

		FMLRelaunchLog.log("[GT++ ASM] Railcraft negative ItemStack Fix", Level.INFO, "Valid? "+isValid+".");	
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
		FMLRelaunchLog.log("[GT++ ASM] Railcraft negative ItemStack Fix", Level.INFO, "Injecting " + aMethodName + ".");		
		if (aMethodName.equals("depleteItem") && !obfuscated) {			
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "depleteItem", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(36, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_Railcraft_InvTools", "depleteItem", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();		
			didInject = true;
		}
		else if (aMethodName.equals("depleteItem") && obfuscated) {
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "depleteItem", "(Ladd;)Ladd;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(36, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_Railcraft_InvTools", "depleteItem", "(Ladd;)Ladd;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("stack", "Ladd;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();	
			didInject = true;
		}
		FMLRelaunchLog.log("[GT++ ASM] Railcraft negative ItemStack Fix", Level.INFO, "Method injection complete. "+(obfuscated ? "Obfuscated" : "Non-Obfuscated"));
		return didInject;
	}









	public class AddFieldAdapter extends ClassVisitor {

		public AddFieldAdapter(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		private final String[] aMethodsToStrip = new String[] {"depleteItem"};


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
				if (desc.equals("(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;")) {
					obfuscated = false;
				}
				else if (desc.equals("(Ladd;)Ladd;")) {
					obfuscated = true;
				}
				else {
					obfuscated = false;
				}
				FMLRelaunchLog.log("[GT++ ASM] Railcraft negative ItemStack Fix", Level.INFO,
						"Method desc " + desc + ", using "+(obfuscated ? "obfuscated method handler" : "deobfuscated method handler"));
				methodVisitor = null;
			}

			if (found) {
				FMLRelaunchLog.log("[GT++ ASM] Railcraft negative ItemStack Fix", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}


	}






}
