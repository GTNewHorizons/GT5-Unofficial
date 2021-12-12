package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNCHRONIZED;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class ClassTransformer_LWJGL_Keyboard {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	private static final HashMap<String, String> mBadKeyCache = new HashMap<String, String>();

	/**
	 * Gets a key's name
	 * 
	 * @param key The key
	 * @return a String with the key's human readable name in it or null if the key
	 *         is unnamed
	 */
	public static synchronized String getKeyName(int key) {
		if (init()) {
			String[] aTemp = getKeyName();
			if (key < aTemp.length && key >= 0) {
				return aTemp[key];
			}
		}
		String aCachedValue = mBadKeyCache.get("key-"+key);
		if (aCachedValue == null) {
			FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Unable to map key code "+key+" to LWJGL keymap.");
			FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Caching key value to be empty.");
			//mBadKeyCache.put("key-"+key, getKeyName()[0x00]);
			aCachedValue = "FIX!";
			mBadKeyCache.put("key-"+key, aCachedValue);
			trySetClientKey(key);
		}
		return aCachedValue; // Return nothing
	}

	public static void trySetClientKey(int aKey) {
		if (Utils.isClient() && ReflectionUtils.doesClassExist("net.minecraft.client.Minecraft")) {
			FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Trying to set key value to be empty.");	
			GameSettings options = Minecraft.getMinecraft().gameSettings;
			KeyBinding[] akeybinding = Minecraft.getMinecraft().gameSettings.keyBindings;
			int i = akeybinding.length;
			for (int j = 0; j < i; ++j) {
				KeyBinding keybinding = akeybinding[j];
				if (keybinding != null && keybinding.getKeyCode() == aKey) {
					options.setOptionKeyBinding(keybinding, 0);
					FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Set keybind "+aKey+" to 0.");
					break;
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static Class mKeyboard;
	private static Field mKeyName;

	@SuppressWarnings("rawtypes")
	private static boolean init() {
		if (mKeyName != null) {
			return true;
		}
		Class aKeyboard = ReflectionUtils.getClass("org.lwjgl.input.Keyboard");
		if (aKeyboard != null) {
			mKeyboard = aKeyboard;
			Field aKeyName = ReflectionUtils.getField(mKeyboard, "keyName");
			if (aKeyName != null) {
				mKeyName = aKeyName;
			}
		}
		return mKeyName != null;
	}

	private static String[] getKeyName() {
		if (init()) {
			try {
				Object o = mKeyName.get(null);
				if (o instanceof String[]) {
					String[] y = (String[]) o;
					return y;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
		return new String[] {};
	}

	public ClassTransformer_LWJGL_Keyboard(byte[] basicClass, boolean isClientSettings) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);	
		if (!isClientSettings) {
			//gtPlusPlus.preloader.keyboard.BetterKeyboard.init();
			aTempReader.accept(new PatchLWJGL(aTempWriter), 0);		
			injectLWJGLPatch(aTempWriter);		
		}
		else {
			//gtPlusPlus.preloader.keyboard.BetterKeyboard.init();
			aTempReader.accept(new PatchClientSettings(aTempWriter), 0);
			injectClientSettingPatch(aTempWriter);
		}
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Valid? " + isValid + ".");
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

	private boolean isClientSettingsObfuscated = false;


	public boolean injectLWJGLPatch(ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO,
				"Injecting " + "getKeyName" + ".");
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC + ACC_SYNCHRONIZED, "getKeyName", "(I)Ljava/lang/String;", null,
				null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(49, l0);
		mv.visitVarInsn(ILOAD, 0);
		mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_LWJGL_Keyboard",
				"getKeyName", "(I)Ljava/lang/String;", false);
		mv.visitInsn(ARETURN);
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLocalVariable("key", "I", null, l0, l1, 0);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
		didInject = true;

		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Method injection complete.");
		return didInject;
	}


	public boolean injectClientSettingPatch(ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;
		String aMethodName = this.isClientSettingsObfuscated ? "func_74298_c" : "getKeyDisplayString";
		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Injecting " + aMethodName + ".");
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, aMethodName, "(I)Ljava/lang/String;", null, null);		
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(130, l0);
		mv.visitVarInsn(ILOAD, 0);
		mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/keyboard/BetterKeyboard", "getKeyDisplayString", "(I)Ljava/lang/String;", false);
		mv.visitInsn(ARETURN);
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLocalVariable("p_74298_0_", "I", null, l0, l1, 0);
		mv.visitMaxs(1, 1);
		mv.visitEnd();		
		didInject = true;
		FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Method injection complete.");
		return didInject;
	}



	public class PatchClientSettings extends ClassVisitor {

		public PatchClientSettings(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		private final String[] aMethodsToStrip = new String[] { "func_74298_c", "getKeyDisplayString" };

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;
			boolean found = false;

			for (String s : aMethodsToStrip) {
				if (name.equals(s)) {
					if (name.equals(aMethodsToStrip[0])) {
						isClientSettingsObfuscated = true;
					}
					else {
						isClientSettingsObfuscated = false;
					}
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
				FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO, "Found method " + name + ", removing.");
			}
			return methodVisitor;
		}

	}

	public class PatchLWJGL extends ClassVisitor {

		public PatchLWJGL(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		private final String[] aMethodsToStrip = new String[] { "getKeyName" };

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
				FMLRelaunchLog.log("[GT++ ASM] LWJGL Keybinding index out of bounds fix", Level.INFO,
						"Found method " + name + ", removing.");
			}
			return methodVisitor;
		}

	}

}
