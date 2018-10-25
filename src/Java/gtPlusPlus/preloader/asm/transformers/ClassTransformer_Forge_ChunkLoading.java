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

public class ClassTransformer_Forge_ChunkLoading {	

	//The qualified name of the class we plan to transform.
	private static final String className = "net.minecraftforge.common.ForgeChunkManager"; 
	//net/minecraftforge/common/ForgeChunkManager
	
	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	

	String aChunkCoordIntPair;
	String aItemStack;
	String aWorld;
	String aEntity;

	private static boolean doesMethodAlreadyExist = false;

	public ClassTransformer_Forge_ChunkLoading(byte[] basicClass, boolean obfuscated) {
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

		if (reader != null && writer != null && !doesMethodAlreadyExist) {	
			
			aChunkCoordIntPair = obfuscated ? DevHelper.getObfuscated("net/minecraft/world/ChunkCoordIntPair") : "net/minecraft/world/ChunkCoordIntPair";
			aWorld = obfuscated ? DevHelper.getObfuscated("net/minecraft/world/World") : "net/minecraft/world/World";
			aEntity = obfuscated ? DevHelper.getObfuscated("net/minecraft/entity/Entity") : "net/minecraft/entity/Entity";
			
			injectMethod("forceChunk");
			injectMethod("unforceChunk");
			injectMethod("requestTicket");
			injectMethod("releaseTicket");
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
		FMLRelaunchLog.log("[GT++ ASM] Chunkloading Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+".");	
		if (aMethodName.equals("forceChunk")) {

			mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "forceChunk", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aChunkCoordIntPair+";)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(730, l0);
			mv.visitVarInsn(ALOAD, 0);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitVarInsn(ALOAD, 1);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNONNULL, l2);
			mv.visitLabel(l1);
			mv.visitLineNumber(732, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(734, l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$500", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Lnet/minecraftforge/common/ForgeChunkManager$Type;", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager$Type", "ENTITY", "Lnet/minecraftforge/common/ForgeChunkManager$Type;");
			Label l3 = new Label();
			mv.visitJumpInsn(IF_ACMPNE, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$600", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)L"+aEntity+";", false);
			mv.visitJumpInsn(IFNONNULL, l3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(736, l4);
			mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Attempted to use an entity ticket to force a chunk, without an entity");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitInsn(ATHROW);
			mv.visitLabel(l3);
			mv.visitLineNumber(738, l3);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/common/ForgeChunkManager$Ticket", "isPlayerTicket", "()Z", false);
			Label l5 = new Label();
			mv.visitJumpInsn(IFEQ, l5);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "playerTickets", "Lcom/google/common/collect/SetMultimap;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/SetMultimap", "containsValue", "(Ljava/lang/Object;)Z", true);
			Label l6 = new Label();
			mv.visitJumpInsn(IFNE, l6);
			Label l7 = new Label();
			mv.visitJumpInsn(GOTO, l7);
			mv.visitLabel(l5);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "tickets", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$200", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/Multimap", "containsEntry", "(Ljava/lang/Object;Ljava/lang/Object;)Z", true);
			mv.visitJumpInsn(IFNE, l6);
			mv.visitLabel(l7);
			mv.visitLineNumber(740, l7);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitLdcInsn("The mod %s attempted to force load a chunk with an invalid ticket. This is not permitted.");
			mv.visitInsn(ICONST_1);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$200", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/lang/String;", false);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/FMLLog", "severe", "(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(741, l8);
			mv.visitInsn(RETURN);
			mv.visitLabel(l6);
			mv.visitLineNumber(743, l6);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$700", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/util/LinkedHashSet;", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashSet", "add", "(Ljava/lang/Object;)Z", false);
			mv.visitInsn(POP);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(744, l9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/ChunkDebugger", "storeLoadChunkToCache", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aChunkCoordIntPair+";)V", false);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(745, l10);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;");
			mv.visitTypeInsn(NEW, "net/minecraftforge/common/ForgeChunkManager$ForceChunkEvent");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/common/ForgeChunkManager$ForceChunkEvent", "<init>", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aChunkCoordIntPair+";)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false);
			mv.visitInsn(POP);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(747, l11);
			mv.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableSetMultimap", "builder", "()Lcom/google/common/collect/ImmutableSetMultimap$Builder;", false);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "forcedChunks", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableSetMultimap$Builder", "putAll", "(Lcom/google/common/collect/Multimap;)Lcom/google/common/collect/ImmutableSetMultimap$Builder;", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableSetMultimap$Builder", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSetMultimap$Builder;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableSetMultimap$Builder", "build", "()Lcom/google/common/collect/ImmutableSetMultimap;", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(748, l12);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "forcedChunks", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(749, l13);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$800", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)I", false);
			Label l14 = new Label();
			mv.visitJumpInsn(IFLE, l14);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$700", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/util/LinkedHashSet;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashSet", "size", "()I", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$800", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)I", false);
			mv.visitJumpInsn(IF_ICMPLE, l14);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(751, l15);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$700", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/util/LinkedHashSet;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashSet", "iterator", "()Ljava/util/Iterator;", false);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, ""+aChunkCoordIntPair+"");
			mv.visitVarInsn(ASTORE, 3);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(752, l16);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager", "unforceChunk", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aChunkCoordIntPair+";)V", false);
			mv.visitLabel(l14);
			mv.visitLineNumber(754, l14);
			mv.visitFrame(F_APPEND,1, new Object[] {"com/google/common/collect/ImmutableSetMultimap"}, 0, null);
			mv.visitInsn(RETURN);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLocalVariable("ticket", "Lnet/minecraftforge/common/ForgeChunkManager$Ticket;", null, l0, l17, 0);
			mv.visitLocalVariable("chunk", "L"+aChunkCoordIntPair+";", null, l0, l17, 1);
			mv.visitLocalVariable("newMap", "Lcom/google/common/collect/ImmutableSetMultimap;", "Lcom/google/common/collect/ImmutableSetMultimap<L"+aChunkCoordIntPair+";Lnet/minecraftforge/common/ForgeChunkManager$Ticket;>;", l12, l17, 2);
			mv.visitLocalVariable("removed", "L"+aChunkCoordIntPair+";", null, l16, l14, 3);
			mv.visitMaxs(5, 4);
			mv.visitEnd();

		}
		else if (aMethodName.equals("unforceChunk")) {

			mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "unforceChunk", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aChunkCoordIntPair+";)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(781, l0);
			mv.visitVarInsn(ALOAD, 0);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitVarInsn(ALOAD, 1);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNONNULL, l2);
			mv.visitLabel(l1);
			mv.visitLineNumber(783, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(785, l2);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$700", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/util/LinkedHashSet;", false);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/LinkedHashSet", "remove", "(Ljava/lang/Object;)Z", false);
			mv.visitInsn(POP);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(786, l3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/ChunkDebugger", "removeLoadedChunkFromCache", "(L"+aChunkCoordIntPair+";)V", false);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(787, l4);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;");
			mv.visitTypeInsn(NEW, "net/minecraftforge/common/ForgeChunkManager$UnforceChunkEvent");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/common/ForgeChunkManager$UnforceChunkEvent", "<init>", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aChunkCoordIntPair+";)V", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false);
			mv.visitInsn(POP);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(788, l5);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "forcedChunks", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/LinkedHashMultimap", "create", "(Lcom/google/common/collect/Multimap;)Lcom/google/common/collect/LinkedHashMultimap;", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(789, l6);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/LinkedHashMultimap", "remove", "(Ljava/lang/Object;Ljava/lang/Object;)Z", false);
			mv.visitInsn(POP);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(790, l7);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableSetMultimap", "copyOf", "(Lcom/google/common/collect/Multimap;)Lcom/google/common/collect/ImmutableSetMultimap;", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(791, l8);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "forcedChunks", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitInsn(POP);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(792, l9);
			mv.visitInsn(RETURN);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLocalVariable("ticket", "Lnet/minecraftforge/common/ForgeChunkManager$Ticket;", null, l0, l10, 0);
			mv.visitLocalVariable("chunk", "L"+aChunkCoordIntPair+";", null, l0, l10, 1);
			mv.visitLocalVariable("copy", "Lcom/google/common/collect/LinkedHashMultimap;", "Lcom/google/common/collect/LinkedHashMultimap<L"+aChunkCoordIntPair+";Lnet/minecraftforge/common/ForgeChunkManager$Ticket;>;", l6, l10, 2);
			mv.visitLocalVariable("newMap", "Lcom/google/common/collect/ImmutableSetMultimap;", "Lcom/google/common/collect/ImmutableSetMultimap<L"+aChunkCoordIntPair+";Lnet/minecraftforge/common/ForgeChunkManager$Ticket;>;", l8, l10, 3);
			mv.visitMaxs(5, 4);
			mv.visitEnd();

		}

		else if (aMethodName.equals("requestTicket")) {

			mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "requestTicket", "(Ljava/lang/Object;L"+aWorld+";Lnet/minecraftforge/common/ForgeChunkManager$Type;)Lnet/minecraftforge/common/ForgeChunkManager$Ticket;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(656, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager", "getContainer", "(Ljava/lang/Object;)Lcpw/mods/fml/common/ModContainer;", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(657, l1);
			mv.visitVarInsn(ALOAD, 3);
			Label l2 = new Label();
			mv.visitJumpInsn(IFNONNULL, l2);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(659, l3);
			mv.visitFieldInsn(GETSTATIC, "org/apache/logging/log4j/Level", "ERROR", "Lorg/apache/logging/log4j/Level;");
			mv.visitLdcInsn("Failed to locate the container for mod instance %s (%s : %x)");
			mv.visitInsn(ICONST_3);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;", false);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "identityHashCode", "(Ljava/lang/Object;)I", false);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/FMLLog", "log", "(Lorg/apache/logging/log4j/Level;Ljava/lang/String;[Ljava/lang/Object;)V", false);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(660, l4);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			mv.visitLabel(l2);
			mv.visitLineNumber(662, l2);
			mv.visitFrame(F_APPEND,1, new Object[] {"cpw/mods/fml/common/ModContainer"}, 0, null);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE, "cpw/mods/fml/common/ModContainer", "getModId", "()Ljava/lang/String;", true);
			mv.visitVarInsn(ASTORE, 4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(663, l5);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "callbacks", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "containsKey", "(Ljava/lang/Object;)Z", true);
			Label l6 = new Label();
			mv.visitJumpInsn(IFNE, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(665, l7);
			mv.visitLdcInsn("The mod %s has attempted to request a ticket without a listener in place");
			mv.visitInsn(ICONST_1);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/FMLLog", "severe", "(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(666, l8);
			mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("Invalid ticket request");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
			mv.visitInsn(ATHROW);
			mv.visitLabel(l6);
			mv.visitLineNumber(669, l6);
			mv.visitFrame(F_APPEND,1, new Object[] {"java/lang/String"}, 0, null);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager", "getMaxTicketLengthFor", "(Ljava/lang/String;)I", false);
			mv.visitVarInsn(ISTORE, 5);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(671, l9);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "tickets", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/Multimap", "get", "(Ljava/lang/Object;)Ljava/util/Collection;", true);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Collection", "size", "()I", true);
			mv.visitVarInsn(ILOAD, 5);
			Label l10 = new Label();
			mv.visitJumpInsn(IF_ICMPLT, l10);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(673, l11);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "warnedMods", "Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "contains", "(Ljava/lang/Object;)Z", true);
			Label l12 = new Label();
			mv.visitJumpInsn(IFNE, l12);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(675, l13);
			mv.visitLdcInsn("The mod %s has attempted to allocate a chunkloading ticket beyond it's currently allocated maximum : %d");
			mv.visitInsn(ICONST_2);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitInsn(AASTORE);
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_1);
			mv.visitVarInsn(ILOAD, 5);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitInsn(AASTORE);
			mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/FMLLog", "info", "(Ljava/lang/String;[Ljava/lang/Object;)V", false);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(676, l14);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "warnedMods", "Ljava/util/Set;");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "add", "(Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			mv.visitLabel(l12);
			mv.visitLineNumber(678, l12);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			mv.visitLabel(l10);
			mv.visitLineNumber(680, l10);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitTypeInsn(NEW, "net/minecraftforge/common/ForgeChunkManager$Ticket");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraftforge/common/ForgeChunkManager$Ticket", "<init>", "(Ljava/lang/String;Lnet/minecraftforge/common/ForgeChunkManager$Type;L"+aWorld+";)V", false);
			mv.visitVarInsn(ASTORE, 6);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(681, l15);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/ChunkDebugger", "storeTicketToCache", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aWorld+";)V", false);
			Label l16 = new Label();
			mv.visitLabel(l16);
			mv.visitLineNumber(682, l16);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "tickets", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitVarInsn(ALOAD, 4);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/Multimap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLineNumber(683, l17);
			mv.visitVarInsn(ALOAD, 6);
			mv.visitInsn(ARETURN);
			Label l18 = new Label();
			mv.visitLabel(l18);
			mv.visitLocalVariable("mod", "Ljava/lang/Object;", null, l0, l18, 0);
			mv.visitLocalVariable("world", "L"+aWorld+";", null, l0, l18, 1);
			mv.visitLocalVariable("type", "Lnet/minecraftforge/common/ForgeChunkManager$Type;", null, l0, l18, 2);
			mv.visitLocalVariable("container", "Lcpw/mods/fml/common/ModContainer;", null, l1, l18, 3);
			mv.visitLocalVariable("modId", "Ljava/lang/String;", null, l5, l18, 4);
			mv.visitLocalVariable("allowedCount", "I", null, l9, l18, 5);
			mv.visitLocalVariable("ticket", "Lnet/minecraftforge/common/ForgeChunkManager$Ticket;", null, l15, l18, 6);
			mv.visitMaxs(6, 7);
			mv.visitEnd();

		}


		else if (aMethodName.equals("releaseTicket")) {

			mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "releaseTicket", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(693, l0);
			mv.visitVarInsn(ALOAD, 0);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(695, l2);
			mv.visitInsn(RETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(697, l1);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/common/ForgeChunkManager$Ticket", "isPlayerTicket", "()Z", false);
			Label l3 = new Label();
			mv.visitJumpInsn(IFEQ, l3);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "playerTickets", "Lcom/google/common/collect/SetMultimap;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/SetMultimap", "containsValue", "(Ljava/lang/Object;)Z", true);
			Label l4 = new Label();
			mv.visitJumpInsn(IFNE, l4);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l3);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "tickets", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$200", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/Multimap", "containsEntry", "(Ljava/lang/Object;Ljava/lang/Object;)Z", true);
			mv.visitJumpInsn(IFNE, l4);
			mv.visitLabel(l5);
			mv.visitLineNumber(699, l5);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			mv.visitLabel(l4);
			mv.visitLineNumber(701, l4);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$700", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/util/LinkedHashSet;", false);
			Label l6 = new Label();
			mv.visitJumpInsn(IFNULL, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(703, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$700", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/util/LinkedHashSet;", false);
			mv.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableSet", "copyOf", "(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableSet;", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableSet", "iterator", "()Ljava/util/Iterator;", false);
			mv.visitVarInsn(ASTORE, 2);
			Label l8 = new Label();
			mv.visitJumpInsn(GOTO, l8);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitFrame(F_FULL, 3, new Object[] {"net/minecraftforge/common/ForgeChunkManager$Ticket", TOP, "java/util/Iterator"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, ""+aChunkCoordIntPair+"");
			mv.visitVarInsn(ASTORE, 1);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(705, l10);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager", "unforceChunk", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;L"+aChunkCoordIntPair+";)V", false);
			mv.visitLabel(l8);
			mv.visitLineNumber(703, l8);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
			mv.visitJumpInsn(IFNE, l9);
			mv.visitLabel(l6);
			mv.visitLineNumber(708, l6);
			mv.visitFrame(F_FULL, 1, new Object[] {"net/minecraftforge/common/ForgeChunkManager$Ticket"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/ChunkDebugger", "removeTicketFromCache", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)V", false);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(709, l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/common/ForgeChunkManager$Ticket", "isPlayerTicket", "()Z", false);
			Label l12 = new Label();
			mv.visitJumpInsn(IFEQ, l12);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(711, l13);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "playerTickets", "Lcom/google/common/collect/SetMultimap;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$100", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/SetMultimap", "remove", "(Ljava/lang/Object;Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLineNumber(712, l14);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "tickets", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitLdcInsn("Forge");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/Multimap", "remove", "(Ljava/lang/Object;Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			Label l15 = new Label();
			mv.visitLabel(l15);
			mv.visitLineNumber(713, l15);
			Label l16 = new Label();
			mv.visitJumpInsn(GOTO, l16);
			mv.visitLabel(l12);
			mv.visitLineNumber(716, l12);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeChunkManager", "tickets", "Ljava/util/Map;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "net/minecraftforge/common/ForgeChunkManager$Ticket", "world", "L"+aWorld+";");
			mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			mv.visitTypeInsn(CHECKCAST, "com/google/common/collect/Multimap");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/ForgeChunkManager$Ticket", "access$200", "(Lnet/minecraftforge/common/ForgeChunkManager$Ticket;)Ljava/lang/String;", false);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEINTERFACE, "com/google/common/collect/Multimap", "remove", "(Ljava/lang/Object;Ljava/lang/Object;)Z", true);
			mv.visitInsn(POP);
			mv.visitLabel(l16);
			mv.visitLineNumber(718, l16);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			Label l17 = new Label();
			mv.visitLabel(l17);
			mv.visitLocalVariable("ticket", "Lnet/minecraftforge/common/ForgeChunkManager$Ticket;", null, l0, l17, 0);
			mv.visitLocalVariable("chunk", "L"+aChunkCoordIntPair+";", null, l10, l8, 1);
			mv.visitMaxs(3, 3);
			mv.visitEnd();

		}


		FMLRelaunchLog.log("[GT++ ASM] Chunkloading Patch", Level.INFO, "Method injection complete.");		

	}

	public static final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {	
			if (name.equals("forceChunk") || name.equals("unforceChunk") || name.equals("requestTicket") || name.equals("releaseTicket")) {
				FMLRelaunchLog.log("[GT++ ASM] Chunkloading Patch", Level.INFO, "Found method "+name+", Patching.");
				return null;
			}			
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			return methodVisitor;
		}
	}

}
