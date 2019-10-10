package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ClassTransformer_GT_CharcoalPit {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public static boolean isWoodLog(Block log) {
		String tTool = log.getHarvestTool(0);
		boolean isLog1 = OrePrefixes.log.contains(new ItemStack(log, 1)) && tTool != null && tTool.equals("axe")	&& log.getMaterial() == Material.wood;
		ArrayList<ItemStack> oredict = OreDictionary.getOres("logWood");
		if (oredict.contains(ItemUtils.getSimpleStack(log))) {
			return true;
		}		
		return isLog1;
	}
	
	public ClassTransformer_GT_CharcoalPit(byte[] basicClass, boolean obfuscated) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new AddFieldAdapter(aTempWriter), 0);
		injectMethod("isWoodLog", obfuscated, aTempWriter);
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] GT Charcoal Pit Fix", Level.INFO,
				"Valid? " + isValid + ".");
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
			mv = cw.visitMethod(ACC_PUBLIC, "woodLog", "(L"+aBlockClassName+";)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(49, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_GT_CharcoalPit", "isWoodLog", "(L"+aBlockClassName+";)Z", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lgregtech/common/tileentities/machines/multi/GT_MetaTileEntity_Charcoal_Pit;", null, l0, l1, 0);
			mv.visitLocalVariable("log", "L"+aBlockClassName+";", null, l0, l1, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
			didInject = true;
		}
		FMLRelaunchLog.log("[GT++ ASM] GT Charcoal Pit Fix", Level.INFO, "Method injection complete.");
		return didInject;
	}

	public class AddFieldAdapter extends ClassVisitor {

		public AddFieldAdapter(ClassVisitor cv) {
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
