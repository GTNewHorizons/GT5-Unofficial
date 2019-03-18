package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_PlayedSound;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.loaders.misc.AssLineAchievements;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatFileWriter;

public class ClassTransformer_GT_Client {	

	private final boolean valid;
	private final ClassReader read;
	private final ClassWriter write;
	private boolean mModern;
	private byte[] mTooledClass;

	public ClassTransformer_GT_Client(byte[] basicClass) {

		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);	

		/**
		 * Let's just read the GT archive for some info
		 */
		mModern = findAssemblyLineClass();
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Found Assembly Line? "+mModern+".");		
		if (mModern) {
			aTempReader.accept(new MethodAdaptor2(aTempWriter), 0);
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Patching Client handling of Assembly Line recipe visibility for GT 5.09");	
			injectMethod(aTempWriter);
			if (aTempReader != null && aTempWriter != null) {
				valid = true;
				mTooledClass = aTempWriter.toByteArray();
			}
			else {
				valid = false;
			}	
		}
		else {
			mTooledClass = basicClass;
			valid = true;			
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Valid? "+valid+".");	
		read = aTempReader;
		write = aTempWriter;		
	}

	public boolean isValidTransformer() {
		return valid;
	}

	public ClassReader getReader() {
		return read;
	}

	public ClassWriter getWriter() {
		return write;
	}

	public boolean findAssemblyLineClass() {
		ClassLoader cl = getClass().getClassLoader();
		try {
			Set<ClassPath.ClassInfo> classesInPackage = ClassPath.from(cl).getTopLevelClassesRecursive("gregtech");
			if (classesInPackage != null && classesInPackage.size() > 0) {
				for (ClassInfo x : classesInPackage) {
					if (x.getResourceName().contains("GT_MetaTileEntity_AssemblyLine")) {
						FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO,
								"Patchable class | " + x.getResourceName());
						return true;
					}
				}
			}
		} catch (IOException e) {
		}

		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Failed to find Gregtech classes using prefered method, using backup.");

		cl = ClassLoader.getSystemClassLoader();
		ImmutableMap<File, ClassLoader> g = getClassPathEntries(cl);
		File aGregtech = null;
		if (g.size() > 0) {
			for (int i = 0; i < g.size(); i++) {
				String aName;
				try {
					File aF = g.keySet().asList().get(i);
					aName = aF.getName();
					if (aName != null && aName.length() > 0) {
						if (aName.toLowerCase().contains("gregtech")) {
							FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Patchable class | "+aName);	
							aGregtech = aF;
						}
					}
				}
				catch (Throwable t) {}				
			}
		}

		if (aGregtech != null) {
			try {
				File file = aGregtech;
				FileInputStream fis = new FileInputStream(file);
				JarInputStream jis = new JarInputStream(fis);
				System.out.println(jis.markSupported());
				JarEntry je;
				while((je=jis.getNextJarEntry())!=null){					
					if (je.getName().contains("GT_MetaTileEntity_AssemblyLine")) {
						FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Patchable class | "+je.getName());
						return true;
					}
				}
				jis.close();
				return true;
			} catch (IOException e1) {
			}
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Failed to find Gregtech classes using backup method, probably using GT 5.08");

		return false;
	}

	static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classloader) {
		LinkedHashMap<File, ClassLoader> entries = Maps.newLinkedHashMap();
		// Search parent first, since it's the order ClassLoader#loadClass() uses.
		ClassLoader parent = classloader.getParent();
		if (parent != null) {
			entries.putAll(getClassPathEntries(parent));
		}
		if (classloader instanceof URLClassLoader) {
			URLClassLoader urlClassLoader = (URLClassLoader) classloader;
			for (URL entry : urlClassLoader.getURLs()) {
				if (entry.getProtocol().equals("file")) {
					File file = new File(entry.getFile());
					if (!entries.containsKey(file)) {
						entries.put(file, classloader);
					}
				}
			}
		}
		return ImmutableMap.copyOf(entries);
	}

	public boolean injectMethod(ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Injecting " + "onPlayerTickEventClient" + ".");

		/**
		 * Inject new, safer code
		 */

		AnnotationVisitor av0;


		/**
		 * Full Patch ASM - Original Idea, now second to static injection of a custom handler.
		 */

		/*mv = cw.visitMethod(ACC_PUBLIC, "onPlayerTickEventClient", "(Lcpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent;)V", null, null);
		{
			av0 = mv.visitAnnotation("Lcpw/mods/fml/common/eventhandler/SubscribeEvent;", true);
			av0.visitEnd();
		}
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(370, l3);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "side", "Lcpw/mods/fml/relauncher/Side;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "cpw/mods/fml/relauncher/Side", "isClient", "()Z", false);
		Label l4 = new Label();
		mv.visitJumpInsn(IFEQ, l4);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "phase", "Lcpw/mods/fml/common/gameevent/TickEvent$Phase;");
		mv.visitFieldInsn(GETSTATIC, "cpw/mods/fml/common/gameevent/TickEvent$Phase", "END", "Lcpw/mods/fml/common/gameevent/TickEvent$Phase;");
		mv.visitJumpInsn(IF_ACMPNE, l4);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETFIELD, "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "player", "Lnet/minecraft/entity/player/EntityPlayer;");
		mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/player/EntityPlayer", "isDead", "Z");
		mv.visitJumpInsn(IFNE, l4);
		Label l5 = new Label();
		mv.visitLabel(l5);
		mv.visitLineNumber(371, l5);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, "gregtech/common/GT_Client", "afterSomeTime", "J");
		mv.visitInsn(LCONST_1);
		mv.visitInsn(LADD);
		mv.visitFieldInsn(PUTFIELD, "gregtech/common/GT_Client", "afterSomeTime", "J");
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLineNumber(372, l6);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, "gregtech/common/GT_Client", "afterSomeTime", "J");
		mv.visitLdcInsn(new Long(100L));
		mv.visitInsn(LCMP);
		Label l7 = new Label();
		mv.visitJumpInsn(IFLT, l7);
		Label l8 = new Label();
		mv.visitLabel(l8);
		mv.visitLineNumber(373, l8);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(LCONST_0);
		mv.visitFieldInsn(PUTFIELD, "gregtech/common/GT_Client", "afterSomeTime", "J");
		Label l9 = new Label();
		mv.visitLabel(l9);
		mv.visitLineNumber(374, l9);
		mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;", false);
		mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/client/entity/EntityClientPlayerMP;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityClientPlayerMP", "getStatFileWriter", "()Lnet/minecraft/stats/StatFileWriter;", false);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitLabel(l0);
		mv.visitLineNumber(376, l0);
		mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Recipe$GT_Recipe_Map", "sAssemblylineVisualRecipes", "Lgregtech/api/util/GT_Recipe$GT_Recipe_Map;");
		mv.visitFieldInsn(GETFIELD, "gregtech/api/util/GT_Recipe$GT_Recipe_Map", "mRecipeList", "Ljava/util/Collection;");
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Collection", "iterator", "()Ljava/util/Iterator;", true);
		mv.visitVarInsn(ASTORE, 4);
		Label l10 = new Label();
		mv.visitJumpInsn(GOTO, l10);
		Label l11 = new Label();
		mv.visitLabel(l11);
		mv.visitFrame(F_FULL, 5, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "net/minecraft/stats/StatFileWriter", TOP, "java/util/Iterator"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "gregtech/api/util/GT_Recipe");
		mv.visitVarInsn(ASTORE, 3);
		Label l12 = new Label();
		mv.visitLabel(l12);
		mv.visitLineNumber(377, l12);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEVIRTUAL, "gregtech/api/util/GT_Recipe", "getOutput", "(I)Lnet/minecraft/item/ItemStack;", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;", false);
		mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/xmod/gregtech/loaders/misc/AssLineAchievements", "getAchievement", "(Ljava/lang/String;)Lnet/minecraft/stats/Achievement;", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/stats/StatFileWriter", "hasAchievementUnlocked", "(Lnet/minecraft/stats/Achievement;)Z", false);
		Label l13 = new Label();
		mv.visitJumpInsn(IFEQ, l13);
		mv.visitInsn(ICONST_0);
		Label l14 = new Label();
		mv.visitJumpInsn(GOTO, l14);
		mv.visitLabel(l13);
		mv.visitFrame(F_FULL, 5, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "net/minecraft/stats/StatFileWriter", "gregtech/api/util/GT_Recipe", "java/util/Iterator"}, 1, new Object[] {"gregtech/api/util/GT_Recipe"});
		mv.visitInsn(ICONST_1);
		mv.visitLabel(l14);
		mv.visitFrame(F_FULL, 5, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "net/minecraft/stats/StatFileWriter", "gregtech/api/util/GT_Recipe", "java/util/Iterator"}, 2, new Object[] {"gregtech/api/util/GT_Recipe", INTEGER});
		mv.visitFieldInsn(PUTFIELD, "gregtech/api/util/GT_Recipe", "mHidden", "Z");
		mv.visitLabel(l10);
		mv.visitLineNumber(376, l10);
		mv.visitFrame(F_FULL, 5, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "net/minecraft/stats/StatFileWriter", TOP, "java/util/Iterator"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
		mv.visitJumpInsn(IFNE, l11);
		mv.visitLabel(l1);
		mv.visitLineNumber(379, l1);
		mv.visitJumpInsn(GOTO, l7);
		mv.visitLabel(l2);
		mv.visitFrame(F_FULL, 3, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "net/minecraft/stats/StatFileWriter"}, 1, new Object[] {"java/lang/Exception"});
		mv.visitVarInsn(ASTORE, 3);
		mv.visitLabel(l7);
		mv.visitLineNumber(381, l7);
		mv.visitFrame(F_CHOP,1, null, 0, null);
		mv.visitTypeInsn(NEW, "java/util/ArrayList");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false);
		mv.visitVarInsn(ASTORE, 2);
		Label l15 = new Label();
		mv.visitLabel(l15);
		mv.visitLineNumber(382, l15);
		mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Utility", "sPlayedSoundMap", "Ljava/util/Map;");
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "entrySet", "()Ljava/util/Set;", true);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;", true);
		mv.visitVarInsn(ASTORE, 4);
		Label l16 = new Label();
		mv.visitJumpInsn(GOTO, l16);
		Label l17 = new Label();
		mv.visitLabel(l17);
		mv.visitFrame(F_FULL, 5, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "java/util/ArrayList", TOP, "java/util/Iterator"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "java/util/Map$Entry");
		mv.visitVarInsn(ASTORE, 3);
		Label l18 = new Label();
		mv.visitLabel(l18);
		mv.visitLineNumber(383, l18);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
		Label l19 = new Label();
		mv.visitJumpInsn(IFGE, l19);
		Label l20 = new Label();
		mv.visitLabel(l20);
		mv.visitLineNumber(384, l20);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getKey", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "gregtech/api/util/GT_PlayedSound");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
		mv.visitInsn(POP);
		Label l21 = new Label();
		mv.visitLabel(l21);
		mv.visitLineNumber(385, l21);
		mv.visitJumpInsn(GOTO, l16);
		mv.visitLabel(l19);
		mv.visitLineNumber(386, l19);
		mv.visitFrame(F_FULL, 5, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "java/util/ArrayList", "java/util/Map$Entry", "java/util/Iterator"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "getValue", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(ISUB);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map$Entry", "setValue", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
		mv.visitInsn(POP);
		mv.visitLabel(l16);
		mv.visitLineNumber(382, l16);
		mv.visitFrame(F_FULL, 5, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent", "java/util/ArrayList", TOP, "java/util/Iterator"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
		mv.visitJumpInsn(IFNE, l17);
		Label l22 = new Label();
		mv.visitLabel(l22);
		mv.visitLineNumber(390, l22);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "iterator", "()Ljava/util/Iterator;", false);
		mv.visitVarInsn(ASTORE, 4);
		Label l23 = new Label();
		mv.visitLabel(l23);
		Label l24 = new Label();
		mv.visitJumpInsn(GOTO, l24);
		Label l25 = new Label();
		mv.visitLabel(l25);
		mv.visitLineNumber(391, l25);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
		mv.visitTypeInsn(CHECKCAST, "gregtech/api/util/GT_PlayedSound");
		mv.visitVarInsn(ASTORE, 3);
		Label l26 = new Label();
		mv.visitLabel(l26);
		mv.visitLineNumber(390, l26);
		mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Utility", "sPlayedSoundMap", "Ljava/util/Map;");
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
		mv.visitInsn(POP);
		mv.visitLabel(l24);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
		mv.visitJumpInsn(IFNE, l25);
		Label l27 = new Label();
		mv.visitLabel(l27);
		mv.visitLineNumber(393, l27);
		mv.visitFieldInsn(GETSTATIC, "gregtech/api/GregTech_API", "mServerStarted", "Z");
		mv.visitJumpInsn(IFNE, l4);
		Label l28 = new Label();
		mv.visitLabel(l28);
		mv.visitLineNumber(394, l28);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTSTATIC, "gregtech/api/GregTech_API", "mServerStarted", "Z");
		mv.visitLabel(l4);
		mv.visitLineNumber(397, l4);
		mv.visitFrame(F_FULL, 2, new Object[] {"gregtech/common/GT_Client", "cpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent"}, 0, new Object[] {});
		mv.visitInsn(RETURN);
		Label l29 = new Label();
		mv.visitLabel(l29);
		mv.visitLocalVariable("this", "Lgregtech/common/GT_Client;", null, l3, l29, 0);
		mv.visitLocalVariable("aEvent", "Lcpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent;", null, l3, l29, 1);
		mv.visitLocalVariable("sfw", "Lnet/minecraft/stats/StatFileWriter;", null, l0, l7, 2);
		mv.visitLocalVariable("recipe", "Lgregtech/api/util/GT_Recipe;", null, l12, l10, 3);
		mv.visitLocalVariable("tList", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Lgregtech/api/util/GT_PlayedSound;>;", l15, l4, 2);
		mv.visitLocalVariable("tEntry", "Ljava/util/Map$Entry;", "Ljava/util/Map$Entry<Lgregtech/api/util/GT_PlayedSound;Ljava/lang/Integer;>;", l18, l16, 3);
		mv.visitLocalVariable("tKey", "Lgregtech/api/util/GT_PlayedSound;", null, l26, l24, 3);
		mv.visitLocalVariable("i", "Ljava/util/Iterator;", "Ljava/util/Iterator<Lgregtech/api/util/GT_PlayedSound;>;", l23, l27, 4);
		mv.visitMaxs(5, 5);
		mv.visitEnd();*/

		/**
		 * Static invocation of custom handler instead
		 */


		mv = cw.visitMethod(ACC_PUBLIC, "onPlayerTickEventClient", "(Lcpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent;)V", null, null);
		av0 = mv.visitAnnotation("Lcpw/mods/fml/common/eventhandler/SubscribeEvent;", true);
		av0.visitEnd();
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(371, l0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_GT_Client", "onPlayerTickEventClient", "(Lcpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent;)V", false);
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(372, l1);
		mv.visitInsn(RETURN);
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLocalVariable("this", "Lgregtech/common/GT_Client;", null, l0, l2, 0);
		mv.visitLocalVariable("aEvent", "Lcpw/mods/fml/common/gameevent/TickEvent$PlayerTickEvent;", null, l0, l2, 1);
		mv.visitMaxs(1, 2);
		mv.visitEnd();


		didInject = true;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO, "Method injection complete.");
		return didInject;			

	}

	public class MethodAdaptor2 extends ClassVisitor {

		public MethodAdaptor2(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;
			if (name.equals("onPlayerTickEventClient")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Achievements Patch", Level.INFO,
						"Found method " + name + ", removing.");
				methodVisitor = null;
			} else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}
			return methodVisitor;
		}
	}

	public byte[] getByteArray() {
		if (mTooledClass != null) {
			return mTooledClass;
		}
		return getWriter().toByteArray();		
	}


	private static final Map<UUID, Long> aTimeMap = new HashMap<UUID, Long>();
	

	public static void onPlayerTickEventClient(PlayerTickEvent aEvent) {
		if (aEvent.side.isClient() && aEvent.phase == Phase.END && !aEvent.player.isDead) {			
			long aTime = 0;
			if (aTimeMap.get(aEvent.player.getUniqueID()) == null) {
				aTimeMap.put(aEvent.player.getUniqueID(), 0l);
			}
			else {
				aTime = aTimeMap.get(aEvent.player.getUniqueID()) + 1;				
				aTimeMap.put(aEvent.player.getUniqueID(), aTime);
			}			
			if (aTime >= 100L) {			
				aTimeMap.put(aEvent.player.getUniqueID(), 0l);				
				/**
				 * Remove original handling
				 */				
				if (StaticFields59.mAssLineVisualMapNEI != null) {
					StatFileWriter tList = Minecraft.getMinecraft().thePlayer.getStatFileWriter();				
					GT_Recipe_Map aAssLineNei;
					try {						
						aAssLineNei = (GT_Recipe_Map) StaticFields59.mAssLineVisualMapNEI.get(null);						
						for (GT_Recipe aFakeAssLineRecipe : aAssLineNei.mRecipeList) {
							String aSafeUnlocalName;
							if (aFakeAssLineRecipe.getOutput(0) == null) {
								Logger.INFO(
										"Someone tried to register an achievement for a recipe with null output. Please report this to Alkalus.");
								continue;
							}
							ItemStack aStack = aFakeAssLineRecipe.getOutput(0);
							try {
								aSafeUnlocalName = aStack.getUnlocalizedName();
							} catch (Throwable t) {
								aSafeUnlocalName = ItemUtils.getUnlocalizedItemName(aStack);
							}
							boolean aHidden = true;
							try {
								aHidden = tList.hasAchievementUnlocked(AssLineAchievements.getAchievement(aSafeUnlocalName));
								Logger.INFO("Found achievement for "+aSafeUnlocalName);
							}
							catch (NullPointerException rrr) {
								aHidden = true;
								//Logger.INFO("Exception handling achievement for "+aSafeUnlocalName);
								//rrr.printStackTrace();
							}							
							aFakeAssLineRecipe.mHidden  = !aHidden; 
						}						
					} catch (IllegalArgumentException | IllegalAccessException e) {
					}
				}
			}

			Iterator tKey;
			ArrayList arg5 = new ArrayList();
			tKey = GT_Utility.sPlayedSoundMap.entrySet().iterator();

			while (tKey.hasNext()) {
				Entry arg7 = (Entry) tKey.next();
				if (((Integer) arg7.getValue()).intValue() < 0) {
					arg5.add(arg7.getKey());
				} else {
					arg7.setValue(Integer.valueOf(((Integer) arg7.getValue()).intValue() - 1));
				}
			}

			Iterator arg8 = arg5.iterator();

			while (arg8.hasNext()) {
				GT_PlayedSound arg6 = (GT_PlayedSound) arg8.next();
				GT_Utility.sPlayedSoundMap.remove(arg6);
			}

			if (!GregTech_API.mServerStarted) {
				GregTech_API.mServerStarted = true;
			}
		}
	}







}