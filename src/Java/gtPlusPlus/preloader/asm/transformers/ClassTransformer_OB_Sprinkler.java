package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.io.IOException;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;


public class ClassTransformer_OB_Sprinkler {	

	//The qualified name of the class we plan to transform.
	private static final String className = "openblocks.common.tileentity.TileEntitySprinkler"; 
	//openblocks/common/tileentity/TileEntitySprinkler

	private final boolean isValid;
	private final boolean isObfuscated;
	private final ClassReader reader;
	private final ClassWriter writer;
	private final String mItemStackName;
	private final String mWorldName;
	private final String mItemName;
	private final String mItemsName;
	private final String mIInventoryName;
	private final String mTileEntityName;

	public ClassTransformer_OB_Sprinkler(boolean obfuscated, byte[] basicClass) {
		isObfuscated = obfuscated;
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;		

		mItemStackName = isObfuscated ? "add" : "net/minecraft/item/ItemStack";	
		mWorldName = isObfuscated ? "ahc" : "net/minecraft/world/World";	
		mItemName = isObfuscated ? "adb" : "net/minecraft/item/Item";		
		mItemsName = isObfuscated ? "ade" : "net/minecraft/init/Items";		
		mIInventoryName = isObfuscated ? "rb" : "net/minecraft/inventory/IInventory";	
		mTileEntityName = isObfuscated ? "aor" : "net/minecraft/tileentity/TileEntity";

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
			injectField("inventory");
			injectMethod("getFertArray");
			injectMethod("getInventory");
			injectMethod("getRealInventory");
			injectMethod("generateInventory");
			injectMethod("updateEntity");
			//injectMethod("createInventoryCallback");
			//injectMethod("registerInventoryCallback");
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

	public void injectField(String aFieldName) {
		FieldVisitor fv;
		if (aFieldName.equals("inventory")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aFieldName+" into "+className+".");
			if (true) {
				fv = getWriter().visitField(ACC_PRIVATE + ACC_FINAL, "inventory", "Lopenmods/inventory/GenericInventory;", null, null);
				fv.visitEnd();
			}
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Field injection complete.");	
		}
	}

	public void injectMethod(String aMethodName) {				
		MethodVisitor mv;					
		AnnotationVisitor av0;		
		if (aMethodName.equals("getFertArray")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "getFertArray", "()[L"+mItemStackName+";", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(256, l0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/ob/SprinklerHandler", "getValidFerts", "()Ljava/util/HashMap;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "isEmpty", "()Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(257, l2);
			mv.visitTypeInsn(NEW, ""+mItemStackName+"");
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETSTATIC, ""+mItemsName+"", "dye", "L"+mItemName+";");
			mv.visitInsn(ICONST_1);
			mv.visitIntInsn(BIPUSH, 15);
			mv.visitMethodInsn(INVOKESPECIAL, ""+mItemStackName+"", "<init>", "(L"+mItemName+";II)V", false);
			mv.visitVarInsn(ASTORE, 0);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(258, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/ob/SprinklerHandler", "registerSprinklerFertilizer", "(L"+mItemStackName+";)V", false);
			mv.visitLabel(l1);
			mv.visitLineNumber(260, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/ob/SprinklerHandler", "getValidFerts", "()Ljava/util/HashMap;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "size", "()I", false);
			mv.visitTypeInsn(ANEWARRAY, ""+mItemStackName+"");
			mv.visitVarInsn(ASTORE, 0);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(261, l4);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(262, l5);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/ob/SprinklerHandler", "getValidFerts", "()Ljava/util/HashMap;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "values", "()Ljava/util/Collection;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Collection", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 3);
			Label l6 = new Label();
			mv.visitJumpInsn(GOTO, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitFrame(F_FULL, 4, new Object[] {"[L"+mItemStackName+";", INTEGER, TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, ""+mItemStackName+"");
			mv.visitVarInsn(ASTORE, 2);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(263, l8);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitIincInsn(1, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+mItemStackName+"", "copy", "()L"+mItemStackName+";", false);
			mv.visitInsn(AASTORE);
			mv.visitLabel(l6);
			mv.visitLineNumber(262, l6);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l7);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(265, l9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitInsn(ICONST_1);
			Label l10 = new Label();
			mv.visitJumpInsn(IF_ICMPGE, l10);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(266, l11);
			mv.visitInsn(ICONST_0);
			mv.visitTypeInsn(ANEWARRAY, ""+mItemStackName+"");
			mv.visitVarInsn(ASTORE, 0);
			mv.visitLabel(l10);
			mv.visitLineNumber(268, l10);
			mv.visitFrame(F_FULL, 2, new Object[] {"[L"+mItemStackName+";", INTEGER}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARETURN);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLocalVariable("aBoneMeal", "L"+mItemStackName+";", null, l3, l1, 0);
			mv.visitLocalVariable("aFerts", "[L"+mItemStackName+";", null, l4, l12, 0);
			mv.visitLocalVariable("aSlot", "I", null, l5, l12, 1);
			mv.visitLocalVariable("i", "L"+mItemStackName+";", null, l8, l6, 2);
			mv.visitMaxs(5, 4);
			mv.visitEnd();

		}
		else if (aMethodName.equals("updateEntity")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PUBLIC, "updateEntity", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(156, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/tileentity/TileEntity", "updateEntity", "()V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(157, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "worldObj", "L"+mWorldName+";");
			mv.visitFieldInsn(GETFIELD, ""+mWorldName+"", "isRemote", "Z");
			Label l2 = new Label();
			mv.visitJumpInsn(IFNE, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(158, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "tank", "Lopenmods/sync/SyncableTank;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "openmods/sync/SyncableTank", "getFluidAmount", "()I", false);
			Label l4 = new Label();
			mv.visitJumpInsn(IFGT, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(159, l5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "needsTankUpdate", "Z");
			Label l6 = new Label();
			mv.visitJumpInsn(IFEQ, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(160, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "tank", "Lopenmods/sync/SyncableTank;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "worldObj", "L"+mWorldName+";");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openblocks/common/tileentity/TileEntitySprinkler", "getPosition", "()Lopenmods/utils/Coord;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openmods/sync/SyncableTank", "updateNeighbours", "(L"+mWorldName+";Lopenmods/utils/Coord;)V", false);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(161, l8);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "needsTankUpdate", "Z");
			mv.visitLabel(l6);
			mv.visitLineNumber(163, l6);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "tank", "Lopenmods/sync/SyncableTank;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "worldObj", "L"+mWorldName+";");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openblocks/common/tileentity/TileEntitySprinkler", "getPosition", "()Lopenmods/utils/Coord;", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/util/ForgeDirection", "DOWN", "Lnet/minecraftforge/common/util/ForgeDirection;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "openmods/sync/SyncableTank", "fillFromSide", "(L"+mWorldName+";Lopenmods/utils/Coord;Lnet/minecraftforge/common/util/ForgeDirection;)I", false);
			mv.visitInsn(POP);
			mv.visitLabel(l4);
			mv.visitLineNumber(166, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "ticks", "I");
			mv.visitFieldInsn(GETSTATIC, "openblocks/Config", "sprinklerBonemealConsumeRate", "I");
			mv.visitInsn(IREM);
			Label l9 = new Label();
			mv.visitJumpInsn(IFNE, l9);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(167, l10);
			mv.visitMethodInsn(INVOKESTATIC, "openblocks/common/tileentity/TileEntitySprinkler", "getFertArray", "()[L"+mItemStackName+";", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 4);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitVarInsn(ISTORE, 3);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 2);
			Label l11 = new Label();
			mv.visitJumpInsn(GOTO, l11);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitFrame(F_FULL, 5, new Object[] {"openblocks/common/tileentity/TileEntitySprinkler", TOP, INTEGER, INTEGER, "[L"+mItemStackName+";"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 1);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(168, l13);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "openblocks/common/tileentity/TileEntitySprinkler", "getRealInventory", "()Lopenmods/inventory/GenericInventory;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openmods/inventory/GenericInventory", "contents", "()Ljava/util/List;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true);
			mv.visitVarInsn(ASTORE, 6);
			Label l14 = new Label();
			mv.visitJumpInsn(GOTO, l14);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitFrame(F_FULL, 7, new Object[] {"openblocks/common/tileentity/TileEntitySprinkler", ""+mItemStackName+"", INTEGER, INTEGER, "[L"+mItemStackName+";", TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, ""+mItemStackName+"");
			mv.visitVarInsn(ASTORE, 5);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(169, l16);
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, ""+mItemStackName+"", "areItemStacksEqual", "(L"+mItemStackName+";L"+mItemStackName+";)Z", false);
			mv.visitJumpInsn(IFEQ, l14);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(170, l17);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "openblocks/common/tileentity/TileEntitySprinkler", "getRealInventory", "()Lopenmods/inventory/GenericInventory;", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "openmods/inventory/legacy/ItemDistribution", "consumeFirstInventoryItem", "(L"+mIInventoryName+";L"+mItemStackName+";)Z", false);
			mv.visitFieldInsn(PUTFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "hasBonemeal", "Z");
			Label l18 = new Label();
			mv.visitLabel(l18);
			mv.visitLineNumber(171, l18);
			mv.visitJumpInsn(GOTO, l9);
			mv.visitLabel(l14);
			mv.visitLineNumber(168, l14);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l15);
			Label l19 = new Label();
			mv.visitLabel(l19);
			mv.visitLineNumber(167, l19);
			mv.visitIincInsn(2, 1);
			mv.visitLabel(l11);
			mv.visitFrame(F_FULL, 5, new Object[] {"openblocks/common/tileentity/TileEntitySprinkler", TOP, INTEGER, INTEGER, "[L"+mItemStackName+";"}, 0, new Object[] {});
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitJumpInsn(IF_ICMPLT, l12);
			mv.visitLabel(l9);
			mv.visitLineNumber(178, l9);
			mv.visitFrame(F_FULL, 1, new Object[] {"openblocks/common/tileentity/TileEntitySprinkler"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "ticks", "I");
			mv.visitFieldInsn(GETSTATIC, "openblocks/Config", "sprinklerWaterConsumeRate", "I");
			mv.visitInsn(IREM);
			mv.visitJumpInsn(IFNE, l2);
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(179, l20);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "tank", "Lopenmods/sync/SyncableTank;");
			mv.visitInsn(ICONST_1);
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openmods/sync/SyncableTank", "drain", "(IZ)Lnet/minecraftforge/fluids/FluidStack;", false);
			Label l21 = new Label();
			mv.visitJumpInsn(IFNULL, l21);
			mv.visitInsn(ICONST_1);
			Label l22 = new Label();
			mv.visitJumpInsn(GOTO, l22);
			mv.visitLabel(l21);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"openblocks/common/tileentity/TileEntitySprinkler"});
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l22);
			mv.visitFrame(F_FULL, 1, new Object[] {"openblocks/common/tileentity/TileEntitySprinkler"}, 2, new Object[] {"openblocks/common/tileentity/TileEntitySprinkler", INTEGER});
			mv.visitMethodInsn(INVOKESPECIAL, "openblocks/common/tileentity/TileEntitySprinkler", "setEnabled", "(Z)V", false);
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLineNumber(180, l23);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openblocks/common/tileentity/TileEntitySprinkler", "sync", "()V", false);
			mv.visitLabel(l2);
			mv.visitLineNumber(183, l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "ticks", "I");
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitFieldInsn(PUTFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "ticks", "I");
			Label l24 = new Label();
			mv.visitLabel(l24);
			mv.visitLineNumber(186, l24);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "openblocks/common/tileentity/TileEntitySprinkler", "isEnabled", "()Z", false);
			Label l25 = new Label();
			mv.visitJumpInsn(IFEQ, l25);
			Label l26 = new Label();
			mv.visitLabel(l26);
			mv.visitLineNumber(187, l26);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "worldObj", "L"+mWorldName+";");
			mv.visitFieldInsn(GETFIELD, ""+mWorldName+"", "isRemote", "Z");
			Label l27 = new Label();
			mv.visitJumpInsn(IFEQ, l27);
			Label l28 = new Label();
			mv.visitLabel(l28);
			mv.visitLineNumber(188, l28);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "openblocks/common/tileentity/TileEntitySprinkler", "sprayParticles", "()V", false);
			mv.visitJumpInsn(GOTO, l25);
			mv.visitLabel(l27);
			mv.visitLineNumber(190, l27);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "openblocks/common/tileentity/TileEntitySprinkler", "attemptFertilize", "()V", false);
			mv.visitLabel(l25);
			mv.visitLineNumber(192, l25);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			Label l29 = new Label();
			mv.visitLabel(l29);
			mv.visitLocalVariable("this", "Lopenblocks/common/tileentity/TileEntitySprinkler;", null, l0, l29, 0);
			mv.visitLocalVariable("f", "L"+mItemStackName+";", null, l13, l19, 1);
			mv.visitLocalVariable("g", "L"+mItemStackName+";", null, l16, l14, 5);
			mv.visitMaxs(4, 7);
			mv.visitEnd();

		}
		else if (aMethodName.equals("generateInventory")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PRIVATE, "generateInventory", "()Lopenmods/inventory/GenericInventory;", null, null);

			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(281, l0);
			mv.visitTypeInsn(NEW, "gtPlusPlus/xmod/ob/CustomSprinklerInventory");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn("sprinkler");
			mv.visitInsn(ICONST_1);
			mv.visitIntInsn(BIPUSH, 9);
			mv.visitMethodInsn(INVOKESPECIAL, "gtPlusPlus/xmod/ob/CustomSprinklerInventory", "<init>", "(L"+mTileEntityName+";Ljava/lang/String;ZI)V", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(282, l1);
			mv.visitVarInsn(ALOAD, 1);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNULL, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(283, l3);
			mv.visitLdcInsn("Created Custom Inventory for OB Sprinkler.");
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/api/objects/Logger", "INFO", "(Ljava/lang/String;)V", false);
			mv.visitLabel(l2);
			mv.visitLineNumber(285, l2);
			mv.visitFrame(F_APPEND,1, new Object[] {"openmods/inventory/GenericInventory"}, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNULL, l4);
			mv.visitVarInsn(ALOAD, 1);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ACONST_NULL);
			mv.visitLabel(l5);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"openmods/inventory/GenericInventory"});
			mv.visitInsn(ARETURN);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLocalVariable("this", "Lopenblocks/common/tileentity/TileEntitySprinkler;", null, l0, l6, 0);
			mv.visitLocalVariable("x", "Lopenmods/inventory/GenericInventory;", null, l1, l6, 1);
			mv.visitMaxs(6, 2);
			mv.visitEnd();
		}
		else if (aMethodName.equals("getRealInventory")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PRIVATE, "getRealInventory", "()Lopenmods/inventory/GenericInventory;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(274, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openblocks/common/tileentity/TileEntitySprinkler", "getInventory", "()L"+mIInventoryName+";", false);
			mv.visitTypeInsn(CHECKCAST, "openmods/inventory/GenericInventory");
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lopenblocks/common/tileentity/TileEntitySprinkler;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		else if (aMethodName.equals("getInventory")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PUBLIC, "getInventory", "()L"+mIInventoryName+";", null, null);
			{
				av0 = mv.visitAnnotation("Lopenmods/include/IncludeInterface;", false);
				av0.visitEnd();
			}
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(225, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "inventory", "Lopenmods/inventory/GenericInventory;");
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(226, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "openblocks/common/tileentity/TileEntitySprinkler", "generateInventory", "()Lopenmods/inventory/GenericInventory;", false);
			mv.visitFieldInsn(PUTFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "inventory", "Lopenmods/inventory/GenericInventory;");
			mv.visitLabel(l1);
			mv.visitLineNumber(228, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "openblocks/common/tileentity/TileEntitySprinkler", "inventory", "Lopenmods/inventory/GenericInventory;");
			mv.visitInsn(ARETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lopenblocks/common/tileentity/TileEntitySprinkler;", null, l0, l3, 0);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}
		else if (aMethodName.equals("createInventoryCallback")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PROTECTED, "createInventoryCallback", "()Lopenmods/api/IInventoryCallback;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(289, l0);
			mv.visitTypeInsn(NEW, "gtPlusPlus/xmod/ob/CallbackObject");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "gtPlusPlus/xmod/ob/CallbackObject", "<init>", "(L"+mTileEntityName+";)V", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lopenblocks/common/tileentity/TileEntitySprinkler;", null, l0, l1, 0);
			mv.visitMaxs(3, 1);
			mv.visitEnd();
		}
		else if (aMethodName.equals("registerInventoryCallback")) {
			FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");
			mv = getWriter().visitMethod(ACC_PROTECTED, "registerInventoryCallback", "(Lopenmods/inventory/GenericInventory;)Lopenmods/inventory/GenericInventory;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(293, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openblocks/common/tileentity/TileEntitySprinkler", "createInventoryCallback", "()Lopenmods/api/IInventoryCallback;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "openmods/inventory/GenericInventory", "addCallback", "(Lopenmods/api/IInventoryCallback;)Lopenmods/inventory/GenericInventory;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lopenblocks/common/tileentity/TileEntitySprinkler;", null, l0, l1, 0);
			mv.visitLocalVariable("inventory", "Lopenmods/inventory/GenericInventory;", null, l0, l1, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}


		FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Method injection complete.");		
	}

	public static final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			FieldVisitor fieldVisitor;			
			if (access == (ACC_PRIVATE + ACC_FINAL) && name.equals("inventory") && desc.equals("Lopenmods/inventory/GenericInventory;")) {
				FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Removing field "+name);
				return null;
			}			
			fieldVisitor = super.visitField(access, name, desc, signature, value);
			return fieldVisitor;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {				
			if (name.equals("updateEntity")) {
				FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Removing method "+name);
				return null;
			}		
			if (name.equals("getInventory")) {
				FMLRelaunchLog.log("[GT++ ASM] OpenBlocks Sprinkler Patch", Level.INFO, "Removing method "+name);
				return null;
			}
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return methodVisitor;
		}
	}

}
