package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cofh.core.util.oredict.OreDictionaryArbiter;
import cofh.lib.util.ItemWrapper;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.DevHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ClassTransformer_COFH_OreDictionaryArbiter {

	//The qualified name of the class we plan to transform.
		private static final String className = "cofh.core.util.oredict.OreDictionaryArbiter"; 
		//cofh/core/util/oredict/OreDictionaryArbiter
		
		private final boolean isValid;
		private final ClassReader reader;
		private final ClassWriter writer;	
		private final boolean isObfuscated;

		public ClassTransformer_COFH_OreDictionaryArbiter(byte[] basicClass, boolean obfuscated) {
			
			ClassReader aTempReader = null;
			ClassWriter aTempWriter = null;
			
			isObfuscated = obfuscated;
			
			aTempReader = new ClassReader(basicClass);
			aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
			aTempReader.accept(new localClassVisitor(aTempWriter), 0);	
			
			if (aTempReader != null && aTempWriter != null) {
				isValid = true;
			}
			else {
				isValid = false;
			}
			reader = aTempReader;
			writer = aTempWriter;
			
			if (reader != null && writer != null) {
				injectMethod("registerOreDictionaryEntry");
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
			String aItemStack = isObfuscated ? DevHelper.getObfuscated("net/minecraft/item/ItemStack") : "net/minecraft/item/ItemStack";
			MethodVisitor mv;		
			if (aMethodName.equals("registerOreDictionaryEntry")) {
				FMLRelaunchLog.log("[GT++ ASM] COFH OreDictionaryArbiter Patch", Level.INFO, "Injecting "+aMethodName+" into "+className+". ItemStack: "+aItemStack);
				mv = getWriter().visitMethod(ACC_PUBLIC + ACC_STATIC, "registerOreDictionaryEntry", "(L"+aItemStack+";Ljava/lang/String;)V", null, null);
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitLineNumber(61, l0);
				mv.visitVarInsn(ALOAD, 0);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_COFH_OreDictionaryArbiter$FixCOFH", "registerOreDictionaryEntry", "(L"+aItemStack+";Ljava/lang/String;)V", false);
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitLineNumber(62, l1);
				mv.visitInsn(RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("arg", "L"+aItemStack+";", null, l0, l2, 0);
				mv.visitLocalVariable("arg0", "Ljava/lang/String;", null, l0, l2, 1);
				mv.visitMaxs(2, 2);
				mv.visitEnd();			
			}
			FMLRelaunchLog.log("[GT++ ASM] COFH OreDictionaryArbiter Patch", Level.INFO, "Method injection complete.");		
			
		}
		
		public static final class localClassVisitor extends ClassVisitor {
				
			public localClassVisitor(ClassVisitor cv) {
				super(ASM5, cv);
			}

			@Override
			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {			
				if (name.equals("registerOreDictionaryEntry")) {
					FMLRelaunchLog.log("[GT++ ASM] COFH OreDictionaryArbiter Patch", Level.INFO, "Removing method "+name);
					return null;
				}		
				MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
				return methodVisitor;
			}
		}

	
	
	
	@SuppressWarnings("unchecked")
	public static class FixCOFH{		
		
		private static BiMap<String, Integer> oreIDs;
		private static TMap<Integer, ArrayList<ItemStack>> oreStacks;
		private static TMap<ItemWrapper, ArrayList<Integer>> stackIDs;
		private static TMap<ItemWrapper, ArrayList<String>> stackNames;
		
		static {
			try {
			oreIDs = (BiMap<String, Integer>) ReflectionUtils.getField(OreDictionaryArbiter.class, "oreIDs").get(null);
			oreStacks = (TMap<Integer, ArrayList<ItemStack>>) ReflectionUtils.getField(OreDictionaryArbiter.class, "oreStacks").get(null);
			stackIDs = (TMap<ItemWrapper, ArrayList<Integer>>) ReflectionUtils.getField(OreDictionaryArbiter.class, "stackIDs").get(null);
			stackNames = (TMap<ItemWrapper, ArrayList<String>>) ReflectionUtils.getField(OreDictionaryArbiter.class, "stackNames").get(null);
			}
			catch (Throwable t) {
				oreIDs = HashBiMap.create();
				oreStacks = new THashMap<Integer, ArrayList<ItemStack>>();
				stackIDs = new THashMap<ItemWrapper, ArrayList<Integer>>();
				stackNames = new THashMap<ItemWrapper, ArrayList<String>>();
			}
		}
		
		public static void registerOreDictionaryEntry(ItemStack arg, String arg0) {	
			try {
			if (arg == null) {
				return;
			}			
			if (arg.getItem() != null && !Strings.isNullOrEmpty(arg0)) {
				int arg1 = OreDictionary.getOreID(arg0);
				oreIDs.put(arg0, Integer.valueOf(arg1));
				if (!oreStacks.containsKey(Integer.valueOf(arg1))) {
					oreStacks.put(Integer.valueOf(arg1), new ArrayList<ItemStack>());
				}
				((ArrayList<ItemStack>) oreStacks.get(Integer.valueOf(arg1))).add(arg);
				ItemWrapper arg2 = ItemWrapper.fromItemStack(arg);
				if (!stackIDs.containsKey(arg2)) {
					stackIDs.put(arg2, new ArrayList<Integer>());
					stackNames.put(arg2, new ArrayList<String>());
				}
				((ArrayList<Integer>) stackIDs.get(arg2)).add(Integer.valueOf(arg1));
				((ArrayList<String>) stackNames.get(arg2)).add(arg0);
			}
			}
			catch (Throwable t) {
				return;
			}
		}
	}
	

	
	
}
