package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARRAYLENGTH;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.F_APPEND;
import static org.objectweb.asm.Opcodes.F_FULL;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.F_SAME1;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INTEGER;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.LCONST_1;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.RETURN;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Found Assembly Line? "+mModern+".");		
		if (mModern) {
			aTempReader.accept(new MethodAdaptor2(aTempWriter), 0);
			FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Patching Client handling of Assembly Line recipe visibility for GT 5.09");	
			injectMethod(aTempWriter, "onPlayerTickEventClient");
			injectMethod(aTempWriter, "onPostLoad");
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
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Valid? "+valid+".");	
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
						FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO,
								"Patchable class | " + x.getResourceName());
						return true;
					}
				}
			}
		} catch (IOException e) {
		}

		FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Failed to find Gregtech classes using prefered method, using backup.");

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
							FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Patchable class | "+aName);	
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
						FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Patchable class | "+je.getName());
						return true;
					}
				}
				jis.close();
				return true;
			} catch (IOException e1) {
			}
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Failed to find Gregtech classes using backup method, probably using GT 5.08");

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

	public boolean injectMethod(ClassWriter cw, String string) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Injecting " + string + ".");

		if (string.equals("onPlayerTickEventClient")) {
			/**
			 * Inject new, safer code
			 */
			AnnotationVisitor av0;
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
		}
		else if (string.equals("onPostLoad")) {

			String aItemStackClassName;
			String aEntityPlayerClassName;
			
			try {
				aItemStackClassName = Class.forName("net/minecraft/item/ItemStack") != null ? "net/minecraft/item/ItemStack" : "add";
			} catch (ClassNotFoundException e) {
				aItemStackClassName = "add";
			}
			
			try {
				aEntityPlayerClassName = Class.forName("net/minecraft/entity/player/EntityPlayer") != null ? "net/minecraft/entity/player/EntityPlayer" : "yz";
			} catch (ClassNotFoundException e) {
				aEntityPlayerClassName = "yz";				
			}
			
			mv = cw.visitMethod(ACC_PUBLIC, "onPostLoad", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
			Label l3 = new Label();
			Label l4 = new Label();
			Label l5 = new Label();
			mv.visitTryCatchBlock(l3, l4, l5, "java/lang/Throwable");
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(315, l6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "gregtech/common/GT_Proxy", "onPostLoad", "()V", false);
			mv.visitLabel(l3);
			mv.visitLineNumber(317, l3);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);
			Label l7 = new Label();
			mv.visitLabel(l7);
			Label l8 = new Label();
			mv.visitJumpInsn(GOTO, l8);
			mv.visitLabel(l0);
			mv.visitLineNumber(319, l0);
			mv.visitFrame(F_APPEND,1, new Object[] {INTEGER}, 0, null);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/GregTech_API", "METATILEENTITIES", "[Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(AALOAD);
			Label l9 = new Label();
			mv.visitJumpInsn(IFNULL, l9);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(320, l10);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/GregTech_API", "METATILEENTITIES", "[Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(AALOAD);
			mv.visitInsn(LCONST_1);
			mv.visitMethodInsn(INVOKEINTERFACE, "gregtech/api/interfaces/metatileentity/IMetaTileEntity", "getStackForm", "(J)L"+aItemStackClassName+";", true);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ICONST_1);
			mv.visitMethodInsn(INVOKEVIRTUAL, ""+aItemStackClassName+"", "getTooltip", "(L"+aEntityPlayerClassName+";Z)Ljava/util/List;", false);
			mv.visitInsn(POP);
			mv.visitLabel(l1);
			mv.visitLineNumber(322, l1);
			mv.visitJumpInsn(GOTO, l9);
			mv.visitLabel(l2);
			mv.visitLineNumber(323, l2);
			mv.visitFrame(F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
			mv.visitVarInsn(ASTORE, 2);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(324, l11);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/util/GT_Log", "err", "Ljava/io/PrintStream;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "(Ljava/io/PrintStream;)V", false);
			mv.visitLabel(l9);
			mv.visitLineNumber(317, l9);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitIincInsn(1, 1);
			mv.visitLabel(l8);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(GETSTATIC, "gregtech/api/GregTech_API", "METATILEENTITIES", "[Lgregtech/api/interfaces/metatileentity/IMetaTileEntity;");
			mv.visitInsn(ARRAYLENGTH);
			mv.visitJumpInsn(IF_ICMPLT, l0);
			mv.visitLabel(l4);
			mv.visitLineNumber(327, l4);
			Label l12 = new Label();
			mv.visitJumpInsn(GOTO, l12);
			mv.visitLabel(l5);
			mv.visitFrame(F_FULL, 1, new Object[] {"gregtech/common/GT_Client"}, 1, new Object[] {"java/lang/Throwable"});
			mv.visitVarInsn(ASTORE, 1);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLineNumber(328, l13);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
			mv.visitLabel(l12);
			mv.visitLineNumber(330, l12);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			Label l14 = new Label();
			mv.visitLabel(l14);
			mv.visitLocalVariable("this", "Lgregtech/common/GT_Client;", null, l6, l14, 0);
			mv.visitLocalVariable("i", "I", null, l7, l4, 1);
			mv.visitLocalVariable("t", "Ljava/lang/Throwable;", null, l11, l9, 2);
			mv.visitLocalVariable("var2", "Ljava/lang/Throwable;", null, l13, l12, 1);
			mv.visitMaxs(3, 3);
			mv.visitEnd();
		}
		
		



		didInject = true;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO, "Method injection complete.");
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
			if (name.equals("onPlayerTickEventClient") || name.equals("onPostLoad")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech Client Proxy Patch", Level.INFO,
						"Found method " + name + ", removing.");
				methodVisitor = null;
			}
			else {
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