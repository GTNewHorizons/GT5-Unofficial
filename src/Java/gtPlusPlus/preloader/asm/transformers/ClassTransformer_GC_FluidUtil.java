package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;


public class ClassTransformer_GC_FluidUtil {	

	//The qualified name of the class we plan to transform.
	private static final String className = "micdoodle8.mods.galacticraft.core.util.FluidUtil"; 
	//"micdoodle8/mods/galacticraft/core/util/FluidUtil
	
	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	

	public ClassTransformer_GC_FluidUtil(byte[] basicClass, boolean obfuscated) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		try {
			aTempReader = new ClassReader(className);
			aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new localClassVisitor(aTempWriter), 0);
		} catch (IOException e) {}		
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		reader = aTempReader;
		writer = aTempWriter;
		
		if (reader != null && writer != null) {
			injectMethod("testFuel");
			injectMethod("fillWithGCFuel");
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

	public void injectMethod(String aMethodName) {		
		MethodVisitor mv;		
		if (aMethodName.equals("testFuel")) {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft FluidUtils Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "testFuel", "(Ljava/lang/String;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(37, l0);
			mv.visitFieldInsn(GETSTATIC, "gtPlusPlus/core/item/chemistry/RocketFuels", "mValidRocketFuelNames", "Ljava/util/HashSet;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashSet", "iterator", "()Ljava/util/Iterator;", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l1 = new Label();
			mv.visitJumpInsn(GOTO, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitFrame(F_FULL, 3, new Object[] {"java/lang/String", TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "java/lang/String");
			mv.visitVarInsn(ASTORE, 1);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(38, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
			mv.visitJumpInsn(IFEQ, l1);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(39, l4);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(37, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l2);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(42, l5);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLocalVariable("name", "Ljava/lang/String;", null, l0, l6, 0);
			mv.visitLocalVariable("aFuelname", "Ljava/lang/String;", null, l3, l1, 1);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
		}
		else if (aMethodName.equals("fillWithGCFuel")) {
			mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "fillWithGCFuel", "(Lnet/minecraftforge/fluids/FluidTank;Lnet/minecraftforge/fluids/FluidStack;Z)I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(46, l0);
			mv.visitVarInsn(ALOAD, 1);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/fluids/FluidRegistry", "getFluidName", "(Lnet/minecraftforge/fluids/FluidStack;)Ljava/lang/String;", false);
			mv.visitMethodInsn(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/FluidUtil", "testFuel", "(Ljava/lang/String;)Z", false);
			mv.visitJumpInsn(IFEQ, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(47, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/FluidTank", "getFluid", "()Lnet/minecraftforge/fluids/FluidStack;", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(48, l3);
			mv.visitVarInsn(ALOAD, 3);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNONNULL, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(49, l5);
			mv.visitFieldInsn(GETSTATIC, "gtPlusPlus/core/item/chemistry/RocketFuels", "mValidRocketFuels", "Ljava/util/HashMap;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "values", "()Ljava/util/Collection;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Collection", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 5);
			Label l6 = new Label();
			mv.visitJumpInsn(GOTO, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitFrame(F_FULL, 6, new Object[] {"net/minecraftforge/fluids/FluidTank", "net/minecraftforge/fluids/FluidStack", INTEGER, "net/minecraftforge/fluids/FluidStack", TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "net/minecraftforge/fluids/Fluid");
			mv.visitVarInsn(ASTORE, 4);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(50, l8);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/FluidStack", "getFluid", "()Lnet/minecraftforge/fluids/Fluid;", false);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitJumpInsn(IF_ACMPNE, l6);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(51, l9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "net/minecraftforge/fluids/FluidStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/fluids/FluidStack", "<init>", "(Lnet/minecraftforge/fluids/Fluid;I)V", false);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/FluidTank", "fill", "(Lnet/minecraftforge/fluids/FluidStack;Z)I", false);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l6);
			mv.visitLineNumber(49, l6);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l7);
			mv.visitLabel(l4);
			mv.visitLineNumber(55, l4);
			mv.visitFrame(F_FULL, 4, new Object[] {"net/minecraftforge/fluids/FluidTank", "net/minecraftforge/fluids/FluidStack", INTEGER, "net/minecraftforge/fluids/FluidStack"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/FluidTank", "getCapacity", "()I", false);
			mv.visitJumpInsn(IF_ICMPGE, l1);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(56, l10);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "net/minecraftforge/fluids/FluidStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/fluids/FluidStack", "<init>", "(Lnet/minecraftforge/fluids/FluidStack;I)V", false);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/FluidTank", "fill", "(Lnet/minecraftforge/fluids/FluidStack;Z)I", false);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(59, l1);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLocalVariable("tank", "Lnet/minecraftforge/fluids/FluidTank;", null, l0, l11, 0);
			mv.visitLocalVariable("liquid", "Lnet/minecraftforge/fluids/FluidStack;", null, l0, l11, 1);
			mv.visitLocalVariable("doFill", "Z", null, l0, l11, 2);
			mv.visitLocalVariable("liquidInTank", "Lnet/minecraftforge/fluids/FluidStack;", null, l3, l1, 3);
			mv.visitLocalVariable("aFuelType", "Lnet/minecraftforge/fluids/Fluid;", null, l8, l6, 4);
			mv.visitMaxs(5, 6);
			mv.visitEnd();
		}	
		FMLRelaunchLog.log("[GT++ ASM] Galacticraft FluidUtils Patch", Level.INFO, "Method injection complete.");		
		
	}
	
	public static final class localClassVisitor extends ClassVisitor {
			
		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {			
			if (name.equals("testFuel")) {
				FMLRelaunchLog.log("[GT++ ASM] Galacticraft FluidUtils Patch", Level.INFO, "Removing method "+name);
				return null;
			}	
			if (name.equals("fillWithGCFuel")) {
				FMLRelaunchLog.log("[GT++ ASM] Galacticraft FluidUtils Patch", Level.INFO, "Removing method "+name);
				return null;
			}			
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return methodVisitor;
		}
	}

}
