package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.asm.AsmConfig;
import net.minecraft.inventory.IInventory;

public class ClassTransformer_Railcraft_FluidHelper {	

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	

	public static int PROCESS_VOLUME;	

	public ClassTransformer_Railcraft_FluidHelper(byte[] basicClass, boolean obfuscated2) {
		
		PROCESS_VOLUME = AsmConfig.maxRailcraftTankProcessVolume;
		
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO, "Attempting to patch field PROCESS_VOLUME in mods.railcraft.common.fluids.FluidHelper with new value: "+PROCESS_VOLUME);	

		boolean obfuscated = false;
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
			obfuscated = false;
		}
		else if (a2) {
			obfuscated = true;
		}
		else {
			//Fallback
			obfuscated = false;
		}	
		FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO, "Are we patching obfuscated methods? "+obfuscated);	
		
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);	
		aTempReader.accept(new AddFieldAdapter(aTempWriter), 0);
		
		addField(ACC_PUBLIC + ACC_FINAL + ACC_STATIC, "PROCESS_VOLUME", aTempWriter);
		injectMethod("fillContainers", aTempWriter, obfuscated);
		injectMethod("drainContainers", aTempWriter, obfuscated);

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}		

		FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO, "Valid? "+isValid+".");	
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

	public boolean addField(int access, String fieldName, ClassWriter cv) {
		FMLRelaunchLog.log(
				"[GT++ ASM] Railcraft PROCESS_VOLUME Patch",
				Level.INFO,	
				"Injecting " + fieldName + " with new value.");
		FieldVisitor fv = cv.visitField(access, fieldName, "I", null, PROCESS_VOLUME);
		if (fv != null) {
			fv.visitEnd();
			return true;
		}
		return false;
	}

	public boolean injectMethod(String aMethodName, ClassWriter cw, boolean obfuscated) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO, "Injecting " + aMethodName + ".");

		if (aMethodName.equals("fillContainers") && !obfuscated) {
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "fillContainers", "(Lnet/minecraftforge/fluids/IFluidHandler;Lnet/minecraft/inventory/IInventory;IILnet/minecraftforge/fluids/Fluid;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(126, l0);
			mv.visitVarInsn(ALOAD, 4);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(127, l2);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(128, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "getStackInSlot", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 5);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(129, l3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "getStackInSlot", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 6);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(130, l4);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitTypeInsn(NEW, "net/minecraftforge/fluids/FluidStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitIntInsn(SIPUSH, PROCESS_VOLUME);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/fluids/FluidStack", "<init>", "(Lnet/minecraftforge/fluids/Fluid;I)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidItemHelper", "fillContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lmods/railcraft/common/fluids/FluidItemHelper$FillReturn;", false);
			mv.visitVarInsn(ASTORE, 7);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(131, l5);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			Label l6 = new Label();
			mv.visitJumpInsn(IFNULL, l6);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "hasPlaceToPutContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false);
			mv.visitJumpInsn(IFEQ, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(132, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "drain", "(Lnet/minecraftforge/common/util/ForgeDirection;IZ)Lnet/minecraftforge/fluids/FluidStack;", true);
			mv.visitVarInsn(ASTORE, 8);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(133, l8);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitJumpInsn(IFNULL, l6);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitJumpInsn(IF_ICMPNE, l6);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(134, l9);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidItemHelper", "fillContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lmods/railcraft/common/fluids/FluidItemHelper$FillReturn;", false);
			mv.visitVarInsn(ASTORE, 7);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(135, l10);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			Label l11 = new Label();
			mv.visitJumpInsn(IFNULL, l11);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitVarInsn(ALOAD, 8);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitJumpInsn(IF_ICMPNE, l11);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(136, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "drain", "(Lnet/minecraftforge/common/util/ForgeDirection;IZ)Lnet/minecraftforge/fluids/FluidStack;", true);
			mv.visitInsn(POP);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(137, l13);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "storeContainer", "(Lnet/minecraft/inventory/IInventory;IILnet/minecraft/item/ItemStack;)V", false);
			mv.visitLabel(l11);
			mv.visitLineNumber(139, l11);
			mv.visitFrame(F_FULL, 9, new Object[] {"net/minecraftforge/fluids/IFluidHandler", "net/minecraft/inventory/IInventory", INTEGER, INTEGER, "net/minecraftforge/fluids/Fluid", "net/minecraft/item/ItemStack", "net/minecraft/item/ItemStack", "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "net/minecraftforge/fluids/FluidStack"}, 0, new Object[] {});
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l6);
			mv.visitLineNumber(142, l6);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLocalVariable("drain", "Lnet/minecraftforge/fluids/FluidStack;", null, l8, l6, 8);
			mv.visitLocalVariable("fluidHandler", "Lnet/minecraftforge/fluids/IFluidHandler;", null, l0, l14, 0);
			mv.visitLocalVariable("inv", "Lnet/minecraft/inventory/IInventory;", null, l0, l14, 1);
			mv.visitLocalVariable("inputSlot", "I", null, l0, l14, 2);
			mv.visitLocalVariable("outputSlot", "I", null, l0, l14, 3);
			mv.visitLocalVariable("fluidToFill", "Lnet/minecraftforge/fluids/Fluid;", null, l0, l14, 4);
			mv.visitLocalVariable("input", "Lnet/minecraft/item/ItemStack;", null, l3, l14, 5);
			mv.visitLocalVariable("output", "Lnet/minecraft/item/ItemStack;", null, l4, l14, 6);
			mv.visitLocalVariable("fill", "Lmods/railcraft/common/fluids/FluidItemHelper$FillReturn;", null, l5, l14, 7);
			mv.visitMaxs(5, 9);
			mv.visitEnd();
			didInject = true;
		}
		else if (aMethodName.equals("fillContainers") && obfuscated) {
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "fillContainers", "(Lnet/minecraftforge/fluids/IFluidHandler;Lnet/minecraft/inventory/IInventory;IILnet/minecraftforge/fluids/Fluid;)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(126, l0);
			mv.visitVarInsn(ALOAD, 4);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(127, l2);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(128, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "func_70301_a", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 5);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(129, l3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "func_70301_a", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 6);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(130, l4);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitTypeInsn(NEW, "net/minecraftforge/fluids/FluidStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitIntInsn(SIPUSH, PROCESS_VOLUME);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/fluids/FluidStack", "<init>", "(Lnet/minecraftforge/fluids/Fluid;I)V", false);
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidItemHelper", "fillContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lmods/railcraft/common/fluids/FluidItemHelper$FillReturn;", false);
			mv.visitVarInsn(ASTORE, 7);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(131, l5);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			Label l6 = new Label();
			mv.visitJumpInsn(IFNULL, l6);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "hasPlaceToPutContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false);
			mv.visitJumpInsn(IFEQ, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(132, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "drain", "(Lnet/minecraftforge/common/util/ForgeDirection;IZ)Lnet/minecraftforge/fluids/FluidStack;", true);
			mv.visitVarInsn(ASTORE, 8);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(133, l8);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitJumpInsn(IFNULL, l6);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitJumpInsn(IF_ICMPNE, l6);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(134, l9);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 8);
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidItemHelper", "fillContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lmods/railcraft/common/fluids/FluidItemHelper$FillReturn;", false);
			mv.visitVarInsn(ASTORE, 7);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(135, l10);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			Label l11 = new Label();
			mv.visitJumpInsn(IFNULL, l11);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitVarInsn(ALOAD, 8);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitJumpInsn(IF_ICMPNE, l11);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(136, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "amount", "I");
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "drain", "(Lnet/minecraftforge/common/util/ForgeDirection;IZ)Lnet/minecraftforge/fluids/FluidStack;", true);
			mv.visitInsn(POP);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(137, l13);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 7);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "storeContainer", "(Lnet/minecraft/inventory/IInventory;IILnet/minecraft/item/ItemStack;)V", false);
			mv.visitLabel(l11);
			mv.visitLineNumber(139, l11);
			mv.visitFrame(F_FULL, 9, new Object[] {"net/minecraftforge/fluids/IFluidHandler", "net/minecraft/inventory/IInventory", INTEGER, INTEGER, "net/minecraftforge/fluids/Fluid", "net/minecraft/item/ItemStack", "net/minecraft/item/ItemStack", "mods/railcraft/common/fluids/FluidItemHelper$FillReturn", "net/minecraftforge/fluids/FluidStack"}, 0, new Object[] {});
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l6);
			mv.visitLineNumber(142, l6);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLocalVariable("drain", "Lnet/minecraftforge/fluids/FluidStack;", null, l8, l6, 8);
			mv.visitLocalVariable("fluidHandler", "Lnet/minecraftforge/fluids/IFluidHandler;", null, l0, l14, 0);
			mv.visitLocalVariable("inv", "Lnet/minecraft/inventory/IInventory;", null, l0, l14, 1);
			mv.visitLocalVariable("inputSlot", "I", null, l0, l14, 2);
			mv.visitLocalVariable("outputSlot", "I", null, l0, l14, 3);
			mv.visitLocalVariable("fluidToFill", "Lnet/minecraftforge/fluids/Fluid;", null, l0, l14, 4);
			mv.visitLocalVariable("input", "Lnet/minecraft/item/ItemStack;", null, l3, l14, 5);
			mv.visitLocalVariable("output", "Lnet/minecraft/item/ItemStack;", null, l4, l14, 6);
			mv.visitLocalVariable("fill", "Lmods/railcraft/common/fluids/FluidItemHelper$FillReturn;", null, l5, l14, 7);
			mv.visitMaxs(5, 9);
			mv.visitEnd();
			didInject = true;
		}
		else if (aMethodName.equals("drainContainers") && !obfuscated) {
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "drainContainers", "(Lnet/minecraftforge/fluids/IFluidHandler;Lnet/minecraft/inventory/IInventory;II)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(146, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "getStackInSlot", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 4);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(147, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "getStackInSlot", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 5);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(148, l2);
			mv.visitVarInsn(ALOAD, 4);
			Label l3 = new Label();
			mv.visitJumpInsn(IFNULL, l3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(149, l4);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitIntInsn(SIPUSH, PROCESS_VOLUME);
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidItemHelper", "drainContainer", "(Lnet/minecraft/item/ItemStack;I)Lmods/railcraft/common/fluids/FluidItemHelper$DrainReturn;", false);
			mv.visitVarInsn(ASTORE, 6);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(150, l5);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitJumpInsn(IFNULL, l3);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "container", "Lnet/minecraft/item/ItemStack;");
			Label l6 = new Label();
			mv.visitJumpInsn(IFNULL, l6);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "hasPlaceToPutContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false);
			mv.visitJumpInsn(IFEQ, l3);
			mv.visitLabel(l6);
			mv.visitLineNumber(151, l6);
			mv.visitFrame(F_APPEND,3, new Object[] {"net/minecraft/item/ItemStack", "net/minecraft/item/ItemStack", "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn"}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "fill", "(Lnet/minecraftforge/common/util/ForgeDirection;Lnet/minecraftforge/fluids/FluidStack;Z)I", true);
			mv.visitVarInsn(ISTORE, 7);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(152, l7);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "isAtomic", "Z");
			Label l8 = new Label();
			mv.visitJumpInsn(IFEQ, l8);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			Label l9 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l9);
			mv.visitLabel(l8);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "isAtomic", "Z");
			mv.visitJumpInsn(IFNE, l3);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitJumpInsn(IFLE, l3);
			mv.visitLabel(l9);
			mv.visitLineNumber(153, l9);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "fill", "(Lnet/minecraftforge/common/util/ForgeDirection;Lnet/minecraftforge/fluids/FluidStack;Z)I", true);
			mv.visitInsn(POP);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(154, l10);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "storeContainer", "(Lnet/minecraft/inventory/IInventory;IILnet/minecraft/item/ItemStack;)V", false);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(155, l11);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l3);
			mv.visitLineNumber(159, l3);
			mv.visitFrame(F_CHOP,2, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLocalVariable("used", "I", null, l7, l3, 7);
			mv.visitLocalVariable("drain", "Lmods/railcraft/common/fluids/FluidItemHelper$DrainReturn;", null, l5, l3, 6);
			mv.visitLocalVariable("fluidHandler", "Lnet/minecraftforge/fluids/IFluidHandler;", null, l0, l12, 0);
			mv.visitLocalVariable("inv", "Lnet/minecraft/inventory/IInventory;", null, l0, l12, 1);
			mv.visitLocalVariable("inputSlot", "I", null, l0, l12, 2);
			mv.visitLocalVariable("outputSlot", "I", null, l0, l12, 3);
			mv.visitLocalVariable("input", "Lnet/minecraft/item/ItemStack;", null, l1, l12, 4);
			mv.visitLocalVariable("output", "Lnet/minecraft/item/ItemStack;", null, l2, l12, 5);
			mv.visitMaxs(4, 8);
			mv.visitEnd();
			didInject = true;
		}
		else if (aMethodName.equals("drainContainers") && obfuscated) {
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "drainContainers", "(Lnet/minecraftforge/fluids/IFluidHandler;Lnet/minecraft/inventory/IInventory;II)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(146, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "func_70301_a", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 4);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(147, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/inventory/IInventory", "func_70301_a", "(I)Lnet/minecraft/item/ItemStack;", true);
			mv.visitVarInsn(ASTORE, 5);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(148, l2);
			mv.visitVarInsn(ALOAD, 4);
			Label l3 = new Label();
			mv.visitJumpInsn(IFNULL, l3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(149, l4);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitIntInsn(SIPUSH, PROCESS_VOLUME);
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidItemHelper", "drainContainer", "(Lnet/minecraft/item/ItemStack;I)Lmods/railcraft/common/fluids/FluidItemHelper$DrainReturn;", false);
			mv.visitVarInsn(ASTORE, 6);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(150, l5);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitJumpInsn(IFNULL, l3);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "container", "Lnet/minecraft/item/ItemStack;");
			Label l6 = new Label();
			mv.visitJumpInsn(IFNULL, l6);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "hasPlaceToPutContainer", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", false);
			mv.visitJumpInsn(IFEQ, l3);
			mv.visitLabel(l6);
			mv.visitLineNumber(151, l6);
			mv.visitFrame(F_APPEND,3, new Object[] {"net/minecraft/item/ItemStack", "net/minecraft/item/ItemStack", "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn"}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitInsn(ICONST_0);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "fill", "(Lnet/minecraftforge/common/util/ForgeDirection;Lnet/minecraftforge/fluids/FluidStack;Z)I", true);
			mv.visitVarInsn(ISTORE, 7);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(152, l7);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "isAtomic", "Z");
			Label l8 = new Label();
			mv.visitJumpInsn(IFEQ, l8);
			mv.visitVarInsn(ILOAD, 7);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			Label l9 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l9);
			mv.visitLabel(l8);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "isAtomic", "Z");
			mv.visitJumpInsn(IFNE, l3);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/fluids/FluidStack", "amount", "I");
			mv.visitJumpInsn(IFLE, l3);
			mv.visitLabel(l9);
			mv.visitLineNumber(153, l9);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "UNKNOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "fluidDrained", "Lnet/minecraftforge/fluids/FluidStack;");
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/fluids/IFluidHandler", "fill", "(Lnet/minecraftforge/common/util/ForgeDirection;Lnet/minecraftforge/fluids/FluidStack;Z)I", true);
			mv.visitInsn(POP);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(154, l10);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitFieldInsn(GETFIELD, "mods/railcraft/common/fluids/FluidItemHelper$DrainReturn", "container", "Lnet/minecraft/item/ItemStack;");
			mv.visitMethodInsn(INVOKESTATIC, "mods/railcraft/common/fluids/FluidHelper", "storeContainer", "(Lnet/minecraft/inventory/IInventory;IILnet/minecraft/item/ItemStack;)V", false);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(155, l11);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l3);
			mv.visitLineNumber(159, l3);
			mv.visitFrame(F_CHOP,2, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLocalVariable("used", "I", null, l7, l3, 7);
			mv.visitLocalVariable("drain", "Lmods/railcraft/common/fluids/FluidItemHelper$DrainReturn;", null, l5, l3, 6);
			mv.visitLocalVariable("fluidHandler", "Lnet/minecraftforge/fluids/IFluidHandler;", null, l0, l12, 0);
			mv.visitLocalVariable("inv", "Lnet/minecraft/inventory/IInventory;", null, l0, l12, 1);
			mv.visitLocalVariable("inputSlot", "I", null, l0, l12, 2);
			mv.visitLocalVariable("outputSlot", "I", null, l0, l12, 3);
			mv.visitLocalVariable("input", "Lnet/minecraft/item/ItemStack;", null, l1, l12, 4);
			mv.visitLocalVariable("output", "Lnet/minecraft/item/ItemStack;", null, l2, l12, 5);
			mv.visitMaxs(4, 8);
			mv.visitEnd();
			didInject = true;
		}

		FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO, "Method injection complete. "+(obfuscated ? "Obfuscated" : "Non-Obfuscated"));
		return didInject;
	}









	public class AddFieldAdapter extends ClassVisitor {

		public AddFieldAdapter(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		@Override
		public FieldVisitor visitField(
				int access, String name, String desc, String signature, Object value) {
			if (name.equals("PROCESS_VOLUME") && desc.equals("I")) {
				FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO, "Removing "+"PROCESS_VOLUME"+".");	       
				return null;
			}
			return cv.visitField(access, name, desc, signature, value); 
		}


		private final String[] aMethodsToStrip = new String[] {"fillContainers", "drainContainers"};


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
				methodVisitor = null;
			}

			if (found) {
				FMLRelaunchLog.log("[GT++ ASM] Railcraft PROCESS_VOLUME Patch", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}


	}






}
