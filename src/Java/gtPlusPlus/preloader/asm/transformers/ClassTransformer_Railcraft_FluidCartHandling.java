package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ASM5;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.asm.AsmConfig;

public class ClassTransformer_Railcraft_FluidCartHandling {	

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	

	//mods.railcraft.common.blocks.machine.gamma.TileFluidLoader
	//mods.railcraft.common.blocks.machine.gamma.TileFluidLoader.TRANSFER_RATE
	
	//mods.railcraft.common.blocks.machine.gamma.TileFluidUnloader
	//mods.railcraft.common.blocks.machine.gamma.TileFluidUnloader.TRANSFER_RATE

	public static final int TileFluidLoader_RATE = 20;
	public static final int TileFluidUnloader_RATE = 80;

	private static final int TYPE_LOADER = 0;
	private static final int TYPE_UNLOADER = 1;

	public ClassTransformer_Railcraft_FluidCartHandling(byte[] basicClass, boolean obfuscated, String aClassName) {
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;
		boolean aLoader = aClassName.equals("mods.railcraft.common.blocks.machine.gamma.TileFluidLoader");
		
		FMLRelaunchLog.log("[GT++ ASM] Railcraft TRANSFER_RATE Patch", Level.INFO, "Attempting to patch field TRANSFER_RATE in "+aClassName+", default value is "+(aLoader ? 20 : 80));			
		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		
		if (aLoader) {
			aTempReader.accept(new AddFieldAdapter(aTempWriter), 0);
			addField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "TRANSFER_RATE", aTempWriter, TYPE_LOADER);			
		}
		else {
			aTempReader.accept(new AddFieldAdapter(aTempWriter), 0);
			addField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "TRANSFER_RATE", aTempWriter, TYPE_UNLOADER);			
		}
		
		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
			FMLRelaunchLog.log("[GT++ ASM] Railcraft TRANSFER_RATE Patch", Level.INFO, "Valid? "+isValid+".");	
		}
		else {
			isValid = false;
		}
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

	public boolean addField(int access, String fieldName, ClassWriter cv, int aType) {
		int aValue = (aType == TYPE_LOADER ? AsmConfig.maxRailcraftFluidLoaderFlow : AsmConfig.maxRailcraftFluidUnloaderFlow);
		FMLRelaunchLog.log("[GT++ ASM] Railcraft TRANSFER_RATE Patch", Level.INFO, "Injecting " + fieldName + " with new value: "+aValue);
		FieldVisitor fv = cv.visitField(access, fieldName, "I", null, new Integer(aValue));
		if (fv != null) {
			fv.visitEnd();
			return true;
		}
		return false;
	}




	public class AddFieldAdapter extends ClassVisitor {

		public AddFieldAdapter(ClassVisitor cv) {
			super(ASM5, cv);
			this.cv = cv;
		}

		@Override
		public FieldVisitor visitField(
				int access, String name, String desc, String signature, Object value) {
			if (name.equals("TRANSFER_RATE") && desc.equals("I")) {
				FMLRelaunchLog.log("[GT++ ASM] Railcraft TRANSFER_RATE Patch", Level.INFO, "Removing "+"TRANSFER_RATE"+".");	       
				return null;
			}
			else {
				FMLRelaunchLog.log("[GT++ ASM] Railcraft TRANSFER_RATE Patch", Level.INFO, "Found Field "+name+" | "+desc);				
			}
			return cv.visitField(access, name, desc, signature, value); 
		}

	}






}
