package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import gtPlusPlus.preloader.Preloader_Logger;

public class ClassTransformer_TC_ThaumcraftCraftingManager {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public ClassTransformer_TC_ThaumcraftCraftingManager(byte[] basicClass) {
		
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		localClassVisitor aTempMethodRemover = new localClassVisitor(aTempWriter);
		aTempReader.accept(aTempMethodRemover, 0);
		boolean wasMethodObfuscated = aTempMethodRemover.getObfuscatedRemoval();

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		
		Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Valid patch? " + isValid + ".");
		reader = aTempReader;
		writer = aTempWriter;

		if (reader != null && writer != null) {
			Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Attempting Method Injection.");
			injectMethod("generateTags", wasMethodObfuscated);
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

	public boolean injectMethod(String aMethodName, boolean wasMethodObfuscated) {
		MethodVisitor mv;
		boolean didInject = false;
		ClassWriter cw = getWriter();
		String aitemClassName = wasMethodObfuscated ? "adb" : "net/minecraft/item/Item";
		String aClassName = "thaumcraft.common.lib.crafting.ThaumcraftCraftingManager";		
		if (aMethodName.equals("generateTags")) {
			Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Injecting " + aMethodName + ", static replacement call to "+aClassName+".");
			mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "generateTags", "(L"+aitemClassName+";ILjava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;", "(L"+aitemClassName+";ILjava/util/ArrayList<Ljava/util/List;>;)Lthaumcraft/api/aspects/AspectList;", null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(23, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/helpers/MethodHelper_TC", "generateTags", "(L"+aitemClassName+";ILjava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("item", "L"+aitemClassName+";", null, l0, l1, 0);
			mv.visitLocalVariable("meta", "I", null, l0, l1, 1);
			mv.visitLocalVariable("history", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Ljava/util/List;>;", l0, l1, 2);
			mv.visitMaxs(3, 3);
			mv.visitEnd();
			didInject = true;
		}		
		
		Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Method injection complete.");
		return didInject;
	}

	public final class localClassVisitor extends ClassVisitor {

		boolean obfuscated = false;
		
		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		public boolean getObfuscatedRemoval() {
			return obfuscated;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;
			String aDeObfName = "net/minecraft/item/Item";
			String aObfName = "adb";
			String aDesc1 = "(L+aDeObfName+;ILjava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;";
			String aDesc2 = "(L"+aObfName+";ILjava/util/ArrayList;)Lthaumcraft/api/aspects/AspectList;";
			
			if (name.equals("generateTags") && signature != null) {	
				if (desc.equals(aDesc1)) {
					Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Found generateTags to remove: "+desc+" | "+signature);
					Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Is not obfuscated.");
					obfuscated = false;
					methodVisitor = null;					
				}
				else if (desc.equals(aDesc2)) {
					Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Found generateTags to remove: "+desc+" | "+signature);
					Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Is obfuscated.");	
					obfuscated = true;
					methodVisitor = null;					
				}
				else {
					Preloader_Logger.INFO("Found generateTags: "+desc+" | "+signature);
					if (desc.toLowerCase().contains("item")) {
						obfuscated = false;		
						Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Is not obfuscated.");
					}
					else {
						obfuscated = true;	
						Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Is obfuscated.");					
					}	
					methodVisitor = null;				
				}
			}
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}

			if (methodVisitor == null) {
				Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Found method " + name + ", removing.");
				Preloader_Logger.LOG("TC CraftingManager Patch", Level.INFO, "Descriptor: "+desc);
			}
			return methodVisitor;
		}
	}

}
