package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ClassTransformer_GT_CharcoalPit {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public static boolean isWoodLog(Block log) {
		return isWoodLog(log, 0);	
	}

	public static boolean isWoodLog(Block log, int meta) {
		ItemStack aLogStack = ItemUtils.getSimpleStack(log, meta, 1);
		ArrayList<ItemStack> aData = OrePrefixes.log.mPrefixedItems;
		for (ItemStack aStack : aData) {
			if (GT_Utility.areStacksEqual(aStack, aLogStack)) {
				return true;
			}
		}
		aData.clear();
		aData = OreDictionary.getOres("logWood");
		for (ItemStack aStack : aData) {
			if (GT_Utility.areStacksEqual(aStack, aLogStack)) {
				return true;
			}
		}
		return false;
	}

	public ClassTransformer_GT_CharcoalPit(byte[] basicClass, boolean obfuscated) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		boolean aBadTime = false;
		if (ReflectionUtils.doesClassExist("aji")) {
			obfuscated = true;			
		}
		else {
			if (ReflectionUtils.doesClassExist("net.minecraft.block.Block")) {
				obfuscated = false;
			}
			else {
				// Bad... Like.. very bad..
				FMLRelaunchLog.log("[GT++ ASM] GT Charcoal Pit Fix", Level.INFO, "Unable to find Block.class/aji.class, this is BAD. Not Patching.");				
			}
		}
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		if (!aBadTime) {
			aTempReader.accept(new CustomClassVisitor(aTempWriter), 0);
			injectMethod("isWoodLog", obfuscated, aTempWriter);
			if (aTempReader != null && aTempWriter != null) {
				isValid = true;
			} else {
				isValid = false;
			}			
		}
		else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] GT Charcoal Pit Fix", Level.INFO, "Valid? " + isValid + ".");
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

	public boolean injectMethod(String aMethodName, boolean obfuscated, ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] GT Charcoal Pit Fix", Level.INFO, "Injecting " + aMethodName + ".");

		String aBlockClassName = "net/minecraft/block/Block";		
		if (obfuscated) {
			aBlockClassName = "aji";
		}		
		if (aMethodName.equals("isWoodLog")) {

			// Inject original Method with only block arg.
			mv = cw.visitMethod(ACC_PUBLIC, "isWoodLog", "(L"+aBlockClassName+";)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(197, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_GT_CharcoalPit", "isWoodLog", "(L"+aBlockClassName+";)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lgregtech/common/tileentities/machines/multi/GT_MetaTileEntity_Charcoal_Pit;", null, l0, l1, 0);
			mv.visitLocalVariable("log", "L"+aBlockClassName+";", null, l0, l1, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();


			// Inject better Method with block & meta arg.
			mv = cw.visitMethod(ACC_PUBLIC, "isWoodLog", "(L"+aBlockClassName+";I)Z", null, null);
			mv.visitCode();
			Label label0 = new Label();
			mv.visitLabel(label0);
			mv.visitLineNumber(201, label0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_GT_CharcoalPit", "isWoodLog", "(L"+aBlockClassName+";I)Z", false);
			mv.visitInsn(IRETURN);
			Label label1 = new Label();
			mv.visitLabel(label1);
			mv.visitLocalVariable("this", "Lgregtech/common/tileentities/machines/multi/GT_MetaTileEntity_Charcoal_Pit;", null, label0, label1, 0);
			mv.visitLocalVariable("log", "L"+aBlockClassName+";", null, label0, label1, 1);
			mv.visitLocalVariable("meta", "I", null, label0, label1, 2);
			mv.visitMaxs(2, 3);
			mv.visitEnd();

			didInject = true;

		}
		FMLRelaunchLog.log("[GT++ ASM] GT Charcoal Pit Fix", Level.INFO, "Method injection complete.");
		return didInject;
	}

	public class CustomClassVisitor extends ClassVisitor {

		public CustomClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		private final String[] aMethodsToStrip = new String[] { "isWoodLog" };

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
			} else {
				methodVisitor = null;
			}
			if (found) {
				FMLRelaunchLog.log("[GT++ ASM] GT Charcoal Pit Fix", Level.INFO, "Found method " + name + ", removing.");
			}
			return methodVisitor;
		}

	}

}
