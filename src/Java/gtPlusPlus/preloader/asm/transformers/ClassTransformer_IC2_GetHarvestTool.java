package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.util.Random;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ClassTransformer_IC2_GetHarvestTool {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;
	private final String className;

	public static String getHarvestTool(int aMeta) {
		return "wrench";
	}

	public static Item getItemDropped(Block aBlock, int meta, Random random, int fortune) {
		return Item.getItemFromBlock(aBlock);
	}

	public static int damageDropped(int aMeta) {
		return aMeta;
	}

	public ClassTransformer_IC2_GetHarvestTool(byte[] basicClass, boolean obfuscated, String aClassName) {
		className = aClassName;
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		FMLRelaunchLog.log("[GT++ ASM] IC2 getHarvestTool Patch", Level.INFO,
				"Attempting to patch in mode " + className + ".");

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter, className), 0);

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] IC2 getHarvestTool Patch", Level.INFO, "Valid patch? " + isValid + ".");
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			FMLRelaunchLog.log("[GT++ ASM] IC2 getHarvestTool Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("getHarvestTool");

			if (aClassName.equals("ic2.core.block.machine.BlockMachine2")
					|| aClassName.equals("ic2.core.block.machine.BlockMachine3")) {
				injectMethod("getItemDropped");
				injectMethod("damageDropped");
			}
			else if (aClassName.equals("ic2.core.block.generator.block.BlockGenerator")
					|| aClassName.equals("ic2.core.block.machine.BlockMachine")) {
				injectMethod("damageDropped");
			}
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
		String aFormattedClassName = className.replace('.', '/');
		ClassWriter cw = getWriter();


		FMLRelaunchLog.log("[GT++ ASM] IC2 getHarvestTool Patch", Level.INFO, "Injecting " + aMethodName + ".");
		if (aMethodName.equals("getHarvestTool")) {
			mv = getWriter().visitMethod(ACC_PUBLIC, "getHarvestTool", "(I)Ljava/lang/String;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(63, l0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC,
					"gtPlusPlus/preloader/asm/transformers/ClassTransformer_IC2_GetHarvestTool", "getHarvestTool",
					"(I)Ljava/lang/String;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L" + aFormattedClassName + ";", null, l0, l1, 0);
			mv.visitLocalVariable("aMeta", "I", null, l0, l1, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
			didInject = true;
		}
		else if (aMethodName.equals("getItemDropped")) {
			mv = cw.visitMethod(ACC_PUBLIC, "getItemDropped", "(ILjava/util/Random;I)Lnet/minecraft/item/Item;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(44, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_IC2_GetHarvestTool", "getItemDropped", "(Lnet/minecraft/block/Block;ILjava/util/Random;I)Lnet/minecraft/item/Item;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L"+aFormattedClassName+";", null, l0, l1, 0);
			mv.visitLocalVariable("meta", "I", null, l0, l1, 1);
			mv.visitLocalVariable("random", "Ljava/util/Random;", null, l0, l1, 2);
			mv.visitLocalVariable("fortune", "I", null, l0, l1, 3);
			mv.visitMaxs(4, 4);
			mv.visitEnd();
			didInject = true;
		}
		else if (aMethodName.equals("damageDropped")) {
			mv = cw.visitMethod(ACC_PUBLIC, "damageDropped", "(I)I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(48, l0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_IC2_GetHarvestTool", "damageDropped", "(I)I", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L"+aFormattedClassName+";", null, l0, l1, 0);
			mv.visitLocalVariable("meta", "I", null, l0, l1, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
			didInject = true;
		}
		FMLRelaunchLog.log("[GT++ ASM] IC2 getHarvestTool Patch", Level.INFO, "Method injection complete.");
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
			if (aClassName.equals("ic2.core.block.machine.BlockMachine2")
					|| aClassName.equals("ic2.core.block.machine.BlockMachine3")) {
				if (name.equals("getItemDropped")) {
					methodVisitor = null;
				} else if (name.equals("damageDropped")) {
					methodVisitor = null;
				} else if (name.equals("getHarvestTool")) {
					methodVisitor = null;
				} else {
					methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
				}
			}
			else if (aClassName.equals("ic2.core.block.generator.block.BlockGenerator")
					|| aClassName.equals("ic2.core.block.machine.BlockMachine")) {
				if (name.equals("damageDropped")) {
					methodVisitor = null;
				} else if (name.equals("getHarvestTool")) {
					methodVisitor = null;
				} else {
					methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
				}
			}
			else {
				if (name.equals("getHarvestTool")) {
					methodVisitor = null;
				} else {
					methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
				}
			}
			if (methodVisitor == null) {
				FMLRelaunchLog.log("[GT++ ASM] IC2 getHarvestTool Patch", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}
	}

}
