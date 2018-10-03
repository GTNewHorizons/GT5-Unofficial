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
import gtPlusPlus.preloader.DevHelper;


public class ClassTransformer_GC_EntityAutoRocket {	

	//The qualified name of the class we plan to transform.
	private static final String className = "micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket";
	//micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;
	private final boolean isObfuscated;

	public ClassTransformer_GC_EntityAutoRocket(byte[] basicClass, boolean obfuscated) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		isObfuscated = obfuscated;
		try {
			aTempReader = new ClassReader(className);
			aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
			new ClassReader(basicClass).accept(new localClassVisitor(aTempWriter), 0);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			injectMethod();
		}
		else {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Failed to Inject new code.");	
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

	public void injectMethod() {
		
		String aEntityPlayer = isObfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayer") : "net/minecraft/entity/player/EntityPlayer";
		String aEntityPlayerMP = isObfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayerMP") : "net/minecraft/entity/player/EntityPlayerMP";
		String aWorld = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/World") : "net/minecraft/world/World";
		String aItemStack = isObfuscated ? DevHelper.getObfuscated("net/minecraft/item/ItemStack") : "net/minecraft/item/ItemStack";
		String aEntity = isObfuscated ? DevHelper.getObfuscated("net/minecraft/entity/Entity") : "net/minecraft/entity/Entity";
		String aWorldClient = isObfuscated ? DevHelper.getObfuscated("net/minecraft/client/multiplayer/WorldClient") : "net/minecraft/client/multiplayer/WorldClient";
		String aDifficultyEnum = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/EnumDifficulty") : "net/minecraft/world/EnumDifficulty";
		String aWorldInfo = isObfuscated ? DevHelper.getObfuscated("net/minecraft/world/storage/WorldInfo") : "net/minecraft/world/storage/WorldInfo";
		String aItemInWorldManager = isObfuscated ? DevHelper.getObfuscated("net/minecraft/server/management/ItemInWorldManager") : "net/minecraft/server/management/ItemInWorldManager";

		if (isValidTransformer()) {
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Injecting decodePacketdata into "+className+".");	
			MethodVisitor mv = getWriter().visitMethod(ACC_PUBLIC, "decodePacketdata", "(Lio/netty/buffer/ByteBuf;)V", null, null);			
			
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1027, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "micdoodle8/mods/galacticraft/api/prefab/entity/EntitySpaceshipBase", "decodePacketdata", "(Lio/netty/buffer/ByteBuf;)V", false);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(1028, l1);
			mv.visitTypeInsn(NEW, "net/minecraftforge/fluids/FluidStack");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/galacticraft/util/GalacticUtils", "getValidFuelForTier", "(L"+aEntity+";)Lnet/minecraftforge/fluids/FluidStack;", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/fluids/FluidStack", "<init>", "(Lnet/minecraftforge/fluids/FluidStack;I)V", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(1029, l2);
			mv.visitVarInsn(ALOAD, 2);
			Label l3 = new Label();
			mv.visitJumpInsn(IFNULL, l3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(1030, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "fuelTank", "Lnet/minecraftforge/fluids/FluidTank;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fluids/FluidTank", "setFluid", "(Lnet/minecraftforge/fluids/FluidStack;)V", false);
			mv.visitLabel(l3);
			mv.visitLineNumber(1032, l3);
			mv.visitFrame(F_APPEND,1, new Object[] {"net/minecraftforge/fluids/FluidStack"}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "landing", "Z");
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(1033, l5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "destinationFrequency", "I");
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(1035, l6);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			Label l7 = new Label();
			mv.visitJumpInsn(IFEQ, l7);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(1037, l8);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "micdoodle8/mods/galacticraft/api/vector/BlockVec3");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitMethodInsn(INVOKESPECIAL, "micdoodle8/mods/galacticraft/api/vector/BlockVec3", "<init>", "(III)V", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "targetVec", "Lmicdoodle8/mods/galacticraft/api/vector/BlockVec3;");
			mv.visitLabel(l7);
			mv.visitLineNumber(1040, l7);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "motionX", "D");
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(1041, l9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "motionY", "D");
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(1042, l10);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "motionZ", "D");
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(1043, l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "lastMotionY", "D");
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(1044, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readDouble", "()D", false);
			mv.visitLdcInsn(new Double("8000.0"));
			mv.visitInsn(DDIV);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "lastLastMotionY", "D");
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(1046, l13);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "cargoItems", "[L"+aItemStack+";");
			Label l14 = new Label();
			mv.visitJumpInsn(IFNONNULL, l14);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(1048, l15);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "getSizeInventory", "()I", false);
			mv.visitTypeInsn(ANEWARRAY, ""+aItemStack+"");
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "cargoItems", "[L"+aItemStack+";");
			mv.visitLabel(l14);
			mv.visitLineNumber(1051, l14);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "setWaitForPlayer", "(Z)V", false);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(1053, l16);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/network/ByteBufUtils", "readUTF8String", "(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(1054, l17);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			mv.visitLdcInsn("");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
			Label l18 = new Label();
			mv.visitJumpInsn(IFEQ, l18);
			mv.visitInsn(ACONST_NULL);
			Label l19 = new Label();
			mv.visitJumpInsn(GOTO, l19);
			mv.visitLabel(l18);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket"});
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			mv.visitLabel(l19);
			mv.visitFrame(F_FULL, 3, new Object[] {"micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "io/netty/buffer/ByteBuf", "net/minecraftforge/fluids/FluidStack"}, 2, new Object[] {"micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "java/lang/String"});
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessage", "Ljava/lang/String;");
			Label l20 = new Label();
			mv.visitLabel(l20);
			mv.visitLineNumber(1055, l20);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusMessageCooldown", "I");
			Label l21 = new Label();
			mv.visitLabel(l21);
			mv.visitLineNumber(1056, l21);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "lastStatusMessageCooldown", "I");
			Label l22 = new Label();
			mv.visitLabel(l22);
			mv.visitLineNumber(1057, l22);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readBoolean", "()Z", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusValid", "Z");
			Label l23 = new Label();
			mv.visitLabel(l23);
			mv.visitLineNumber(1060, l23);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "worldObj", "L"+aWorld+";");
			mv.visitFieldInsn(GETFIELD, ""+aWorld+"", "isRemote", "Z");
			Label l24 = new Label();
			mv.visitJumpInsn(IFEQ, l24);
			Label l25 = new Label();
			mv.visitLabel(l25);
			mv.visitLineNumber(1062, l25);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "io/netty/buffer/ByteBuf", "readInt", "()I", false);
			mv.visitVarInsn(ISTORE, 3);
			Label l26 = new Label();
			mv.visitLabel(l26);
			mv.visitLineNumber(1063, l26);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "riddenByEntity", "L"+aEntity+";");
			Label l27 = new Label();
			mv.visitJumpInsn(IFNONNULL, l27);
			Label l28 = new Label();
			mv.visitLabel(l28);
			mv.visitLineNumber(1065, l28);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_M1);
			mv.visitJumpInsn(IF_ICMPLE, l24);
			Label l29 = new Label();
			mv.visitLabel(l29);
			mv.visitLineNumber(1067, l29);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/client/FMLClientHandler", "instance", "()Lcpw/mods/fml/client/FMLClientHandler;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/client/FMLClientHandler", "getWorldClient", "()L"+aWorldClient+";", false);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldClient+"", "getEntityByID", "(I)L"+aEntity+";", false);
			mv.visitVarInsn(ASTORE, 4);
			Label l30 = new Label();
			mv.visitLabel(l30);
			mv.visitLineNumber(1068, l30);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitJumpInsn(IFNULL, l24);
			Label l31 = new Label();
			mv.visitLabel(l31);
			mv.visitLineNumber(1070, l31);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "dimension", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			Label l32 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l32);
			Label l33 = new Label();
			mv.visitLabel(l33);
			mv.visitLineNumber(1072, l33);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitTypeInsn(INSTANCEOF, ""+aEntityPlayer+"");
			mv.visitJumpInsn(IFEQ, l24);
			Label l34 = new Label();
			mv.visitLabel(l34);
			mv.visitLineNumber(1074, l34);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitFieldInsn(GETFIELD, ""+aWorld+"", "difficultySetting", "L"+aDifficultyEnum+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aDifficultyEnum+"", "getDifficultyId", "()I", false);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorld+"", "getWorldInfo", "()L"+aWorldInfo+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldInfo+"", "getTerrainType", "()L"+aWorld+"Type;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorld+"Type", "getWorldTypeName", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitTypeInsn(CHECKCAST, ""+aEntityPlayerMP+"");
			mv.visitFieldInsn(GETFIELD, ""+aEntityPlayerMP+"", "theItemInWorldManager", "L"+aItemInWorldManager+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aItemInWorldManager+"", "getGameType", "()L"+aWorld+"Settings$GameType;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorld+"Settings$GameType", "getID", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "forceRespawnClient", "(IILjava/lang/String;I)L"+aEntityPlayer+";", false);
			mv.visitVarInsn(ASTORE, 4);
			Label l35 = new Label();
			mv.visitLabel(l35);
			mv.visitLineNumber(1075, l35);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l36 = new Label();
			mv.visitLabel(l36);
			mv.visitLineNumber(1077, l36);
			mv.visitJumpInsn(GOTO, l24);
			mv.visitLabel(l32);
			mv.visitLineNumber(1079, l32);
			mv.visitFrame(F_APPEND,2, new Object[] {INTEGER, ""+aEntity+""}, 0, null);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l37 = new Label();
			mv.visitLabel(l37);
			mv.visitLineNumber(1082, l37);
			mv.visitJumpInsn(GOTO, l24);
			mv.visitLabel(l27);
			mv.visitLineNumber(1083, l27);
			mv.visitFrame(F_CHOP,1, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "riddenByEntity", "L"+aEntity+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "getEntityId", "()I", false);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitJumpInsn(IF_ICMPEQ, l24);
			Label l38 = new Label();
			mv.visitLabel(l38);
			mv.visitLineNumber(1085, l38);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(ICONST_M1);
			Label l39 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l39);
			Label l40 = new Label();
			mv.visitLabel(l40);
			mv.visitLineNumber(1087, l40);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "riddenByEntity", "L"+aEntity+";");
			mv.visitInsn(ACONST_NULL);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l41 = new Label();
			mv.visitLabel(l41);
			mv.visitLineNumber(1088, l41);
			mv.visitJumpInsn(GOTO, l24);
			mv.visitLabel(l39);
			mv.visitLineNumber(1091, l39);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/client/FMLClientHandler", "instance", "()Lcpw/mods/fml/client/FMLClientHandler;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/client/FMLClientHandler", "getWorldClient", "()L"+aWorldClient+";", false);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldClient+"", "getEntityByID", "(I)L"+aEntity+";", false);
			mv.visitVarInsn(ASTORE, 4);
			Label l42 = new Label();
			mv.visitLabel(l42);
			mv.visitLineNumber(1092, l42);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitJumpInsn(IFNULL, l24);
			Label l43 = new Label();
			mv.visitLabel(l43);
			mv.visitLineNumber(1094, l43);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "dimension", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			Label l44 = new Label();
			mv.visitJumpInsn(IF_ICMPEQ, l44);
			Label l45 = new Label();
			mv.visitLabel(l45);
			mv.visitLineNumber(1096, l45);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitTypeInsn(INSTANCEOF, ""+aEntityPlayer+"");
			mv.visitJumpInsn(IFEQ, l24);
			Label l46 = new Label();
			mv.visitLabel(l46);
			mv.visitLineNumber(1098, l46);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "dimension", "I");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitFieldInsn(GETFIELD, ""+aWorld+"", "difficultySetting", "L"+aDifficultyEnum+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aDifficultyEnum+"", "getDifficultyId", "()I", false);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitFieldInsn(GETFIELD, ""+aEntity+"", "worldObj", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorld+"", "getWorldInfo", "()L"+aWorldInfo+";", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorldInfo+"", "getTerrainType", "()L"+aWorld+"Type;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorld+"Type", "getWorldTypeName", "()Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitTypeInsn(CHECKCAST, ""+aEntityPlayerMP+"");
			mv.visitFieldInsn(GETFIELD, ""+aEntityPlayerMP+"", "theItemInWorldManager", "L"+aItemInWorldManager+";");
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aItemInWorldManager+"", "getGameType", "()L"+aWorld+"Settings$GameType;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aWorld+"Settings$GameType", "getID", "()I", false);
			mv.visitMethodInsn(INVOKESTATIC, "micdoodle8/mods/galacticraft/core/util/WorldUtil", "forceRespawnClient", "(IILjava/lang/String;I)L"+aEntityPlayer+";", false);
			mv.visitVarInsn(ASTORE, 4);
			Label l47 = new Label();
			mv.visitLabel(l47);
			mv.visitLineNumber(1099, l47);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			Label l48 = new Label();
			mv.visitLabel(l48);
			mv.visitLineNumber(1101, l48);
			mv.visitJumpInsn(GOTO, l24);
			mv.visitLabel(l44);
			mv.visitLineNumber(1103, l44);
			mv.visitFrame(F_APPEND,1, new Object[] {""+aEntity+""}, 0, null);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aEntity+"", "mountEntity", "(L"+aEntity+";)V", false);
			mv.visitLabel(l24);
			mv.visitLineNumber(1108, l24);
			mv.visitFrame(F_CHOP,2, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/network/ByteBufUtils", "readUTF8String", "(Lio/netty/buffer/ByteBuf;)Ljava/lang/String;", false);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusColour", "Ljava/lang/String;");
			Label l49 = new Label();
			mv.visitLabel(l49);
			mv.visitLineNumber(1109, l49);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusColour", "Ljava/lang/String;");
			mv.visitLdcInsn("");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
			Label l50 = new Label();
			mv.visitJumpInsn(IFEQ, l50);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ACONST_NULL);
			mv.visitFieldInsn(PUTFIELD, "micdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket", "statusColour", "Ljava/lang/String;");
			mv.visitLabel(l50);
			mv.visitLineNumber(1110, l50);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			Label l51 = new Label();
			mv.visitLabel(l51);
			mv.visitLocalVariable("this", "Lmicdoodle8/mods/galacticraft/api/prefab/entity/EntityAutoRocket;", null, l0, l51, 0);
			mv.visitLocalVariable("buffer", "Lio/netty/buffer/ByteBuf;", null, l0, l51, 1);
			mv.visitLocalVariable("s", "Lnet/minecraftforge/fluids/FluidStack;", null, l2, l51, 2);
			mv.visitLocalVariable("shouldBeMountedId", "I", null, l26, l24, 3);
			mv.visitLocalVariable("e", "L"+aEntity+";", null, l30, l37, 4);
			mv.visitLocalVariable("e", "L"+aEntity+";", null, l42, l24, 4);
			mv.visitMaxs(6, 5);
			mv.visitEnd();
			
		}
	}

	public static final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
			FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Inspecting Class "+className);	
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {			
			if (name.equals("decodePacketdata")) {
				FMLRelaunchLog.log("[GT++ ASM] Galacticraft EntityAutoRocket Patch", Level.INFO, "Removing method "+name);	
				return null;
			}
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return methodVisitor;
		}
	}

}
