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
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.preloader.DevHelper;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClassTransformer_GT_BlockMachines_MetaPipeEntity {	

	//The qualified name of the class we plan to transform.
	//gregtech/common/blocks/GT_Block_Machines

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;	

	/**
	 * Utility Functions
	 */

	public static String getHarvestTool(int aMeta) {
		//FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Attempting to call getHarvestTool. Meta: "+aMeta);
		if (aMeta >= 8 && aMeta <= 11) {
			return "cutter";			
		}
		return "wrench";
	}

	/**
	 * This determines the BaseMetaTileEntity belonging to this MetaTileEntity by using the Meta ID of the Block itself.
	 * <p/>
	 * 0 = BaseMetaTileEntity, Wrench lvl 0 to dismantle
	 * 1 = BaseMetaTileEntity, Wrench lvl 1 to dismantle
	 * <p/>
	 * 2 = BaseMetaTileEntity, Wrench lvl 2 to dismantle
	 * 3 = BaseMetaTileEntity, Wrench lvl 3 to dismantle
	 * <p/>
	 * 4 = BaseMetaPipeEntity, Wrench lvl 0 to dismantle
	 * 5 = BaseMetaPipeEntity, Wrench lvl 1 to dismantle
	 * <p/>
	 * 6 = BaseMetaPipeEntity, Wrench lvl 2 to dismantle
	 * 7 = BaseMetaPipeEntity, Wrench lvl 3 to dismantle
	 * <p/>
	 * 8 = BaseMetaPipeEntity, Cutter lvl 0 to dismantle
	 * 9 = BaseMetaPipeEntity, Cutter lvl 1 to dismantle
	 * <p/>
	 * 10 = BaseMetaPipeEntity, Cutter lvl 2 to dismantle
	 * 11 = BaseMetaPipeEntity, Cutter lvl 3 to dismantle
	 * <p/> 
	 * == Reserved For Alkalus (Was previously used to allow axes on wooden blocks, but that's fucking stupid.)
	 * <p/>
	 * 12 = BaseMetaTileEntity, Wrench lvl 0 to dismantle
	 * 13 = BaseMetaTileEntity, Wrench lvl 1 to dismantle
	 * <p/>
	 * 14 = BaseMetaTileEntity, Wrench lvl 2 to dismantle
	 * 15 = BaseMetaTileEntity, Wrench lvl 3 to dismantle
	 */
	public static byte getTileEntityBaseType(Materials mMaterial) {	
		//FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Attempting to call getTileEntityBaseType.");
		return (byte) (mMaterial == null ? 4 : (byte) (4) + Math.max(0, Math.min(3, mMaterial.mToolQuality)));
	}



	/**
	 * This determines the BaseMetaTileEntity belonging to this MetaTileEntity by using the Meta ID of the Block itself.
	 * <p/>
	 * 0 = BaseMetaTileEntity, Wrench lvl 0 to dismantle
	 * 1 = BaseMetaTileEntity, Wrench lvl 1 to dismantle
	 * <p/>
	 * 2 = BaseMetaTileEntity, Wrench lvl 2 to dismantle
	 * 3 = BaseMetaTileEntity, Wrench lvl 3 to dismantle
	 * <p/>
	 * 4 = BaseMetaPipeEntity, Wrench lvl 0 to dismantle
	 * 5 = BaseMetaPipeEntity, Wrench lvl 1 to dismantle
	 * <p/>
	 * 6 = BaseMetaPipeEntity, Wrench lvl 2 to dismantle
	 * 7 = BaseMetaPipeEntity, Wrench lvl 3 to dismantle
	 * <p/>
	 * 8 = BaseMetaPipeEntity, Cutter lvl 0 to dismantle
	 * 9 = BaseMetaPipeEntity, Cutter lvl 1 to dismantle
	 * <p/>
	 * 10 = BaseMetaPipeEntity, Cutter lvl 2 to dismantle
	 * 11 = BaseMetaPipeEntity, Cutter lvl 3 to dismantle
	 * <p/> 
	 * == Reserved For Alkalus (Was previously used to allow axes on wooden blocks, but that's fucking stupid.)
	 * <p/>
	 * 12 = BaseMetaTileEntity, Wrench lvl 2 to dismantle
	 * 13 = BaseMetaTileEntity, Wrench lvl 2 to dismantle
	 * <p/>
	 * 14 = BaseMetaTileEntity, Wrench lvl 3 to dismantle
	 * 15 = BaseMetaTileEntity, Wrench lvl 3 to dismantle
	 */
	public static TileEntity createTileEntity(World aWorld, int aMeta) {
		Logger.INFO("Creating Tile Entity with Meta of "+aMeta);
		if (aMeta < 4) {
			return GregTech_API.constructBaseMetaTileEntity();
		} else if (aMeta < 12) {
			return new BaseMetaPipeEntity();
		} else {
			return Meta_GT_Proxy.constructCustomGregtechMetaTileEntityByMeta(aMeta);
		}
	}



	String aEntityPlayer;
	String aEntityPlayerMP;
	String aWorld;
	int mMode;

	public ClassTransformer_GT_BlockMachines_MetaPipeEntity(byte[] basicClass, boolean obfuscated, int aMode) {
		mMode = aMode;
		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Attempting to patch in mode "+aMode+".");	

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		aTempReader.accept(new localClassVisitor(aTempWriter), 0);		

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Valid patch? "+isValid+".");	
		reader = aTempReader;
		writer = aTempWriter;


		if (reader != null && writer != null) {		
			aEntityPlayer = obfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayer") : "net/minecraft/entity/player/EntityPlayer";
			aEntityPlayerMP = obfuscated ? DevHelper.getObfuscated("net/minecraft/entity/player/EntityPlayerMP") : "net/minecraft/entity/player/EntityPlayerMP";
			aWorld = obfuscated ? DevHelper.getObfuscated("net/minecraft/world/World") : "net/minecraft/world/World";

			FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Attempting Method Injection.");
			if (aMode == 0) {
				injectMethod("getHarvestTool");
				injectMethod("createTileEntity");
			}
			else {
				injectMethod("getTileEntityBaseType");
			}
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

	public boolean injectMethod(String aMethodName) {		
		MethodVisitor mv;	
		boolean didInject = false;
		FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Injecting "+aMethodName+".");	
		if (aMethodName.equals("getHarvestTool")) {
			mv = getWriter().visitMethod(ACC_PUBLIC, "getHarvestTool", "(I)Ljava/lang/String;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(63, l0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_GT_BlockMachines_MetaPipeEntity", "getHarvestTool", "(I)Ljava/lang/String;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lgregtech/common/blocks/GT_Block_Machines;", null, l0, l1, 0);
			mv.visitLocalVariable("aMeta", "I", null, l0, l1, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
			didInject = true;
		}
		else if (aMethodName.equals("createTileEntity")) {
			mv = getWriter().visitMethod(ACC_PUBLIC, "createTileEntity", "(Lnet/minecraft/world/World;I)Lnet/minecraft/tileentity/TileEntity;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(442, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_GT_BlockMachines_MetaPipeEntity", "createTileEntity", "(Lnet/minecraft/world/World;I)Lnet/minecraft/tileentity/TileEntity;", false);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lgregtech/common/blocks/GT_Block_Machines;", null, l0, l1, 0);
			mv.visitLocalVariable("aWorld", "Lnet/minecraft/world/World;", null, l0, l1, 1);
			mv.visitLocalVariable("aMeta", "I", null, l0, l1, 2);
			mv.visitMaxs(2, 3);
			mv.visitEnd();
			didInject = true;

		}
		else if (aMethodName.equals("getTileEntityBaseType")) {

			String aClassName = mMode == 1 ? "gregtech/api/metatileentity/implementations/GT_MetaPipeEntity_Item" : mMode == 2 ? "gregtech/api/metatileentity/implementations/GT_MetaPipeEntity_Frame" : "gregtech/api/metatileentity/implementations/GT_MetaPipeEntity_Fluid";
			mv = getWriter().visitMethod(ACC_PUBLIC, "getTileEntityBaseType", "()B", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(37, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, ""+aClassName+"", "mMaterial", "Lgregtech/api/enums/Materials;");
			mv.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/ClassTransformer_GT_BlockMachines_MetaPipeEntity", "getTileEntityBaseType", "(Lgregtech/api/enums/Materials;)B", false);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L"+aClassName+";", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
			didInject = true;

		}	
		FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Method injection complete.");		
		return didInject;
	}

	public static final class localClassVisitor extends ClassVisitor {

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {		
			MethodVisitor methodVisitor;	
			if (name.equals("getHarvestTool") || name.equals("createTileEntity") || name.equals("getTileEntityBaseType")) {
				FMLRelaunchLog.log("[GT++ ASM] Gregtech getTileEntityBaseType Patch", Level.INFO, "Found method "+name+", removing.");	
				methodVisitor = null;
			}	
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);	
			}
			return methodVisitor;
		}
	}

}
