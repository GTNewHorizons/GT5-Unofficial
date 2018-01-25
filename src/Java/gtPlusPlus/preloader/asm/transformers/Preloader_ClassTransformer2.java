package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Preloader_ClassTransformer2 {


	public ArrayList<ItemStack> getDrops(BaseMetaTileEntity o) {
		try {
			int tID = (int) ReflectionUtils.getField(getClass(), "mID").get(o);
			NBTTagCompound tRecipeStuff = (NBTTagCompound) ReflectionUtils.getField(getClass(), "mRecipeStuff").get(o);
			boolean tMuffler = (boolean) ReflectionUtils.getField(getClass(), "mMuffler").get(o);
			boolean tLockUpgrade = (boolean) ReflectionUtils.getField(getClass(), "mLockUpgrade").get(o);
			boolean tSteamConverter = (boolean) ReflectionUtils.getField(getClass(), "mSteamConverter").get(o);
			byte tColor = (byte) ReflectionUtils.getField(getClass(), "mColor").get(o);
			byte tOtherUpgrades = (byte) ReflectionUtils.getField(getClass(), "mOtherUpgrades").get(o);
			byte tStrongRedstone = (byte) ReflectionUtils.getField(getClass(), "mStrongRedstone").get(o);
			int[] tCoverSides = (int[]) ReflectionUtils.getField(getClass(), "mCoverSides").get(o);
			int[] tCoverData = (int[]) ReflectionUtils.getField(getClass(), "mCoverData").get(o);
			BaseMetaTileEntity tMetaTileEntity = (BaseMetaTileEntity) ReflectionUtils.getField(getClass(), "mMetaTileEntity").get(o);

			ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, tID);
			NBTTagCompound tNBT = new NBTTagCompound();

			if (tRecipeStuff != null && !tRecipeStuff.hasNoTags()) tNBT.setTag("GT.CraftingComponents", tRecipeStuff);
			if (tMuffler) tNBT.setBoolean("mMuffler", tMuffler);
			if (tLockUpgrade) tNBT.setBoolean("mLockUpgrade", tLockUpgrade);
			if (tSteamConverter) tNBT.setBoolean("mSteamConverter", tSteamConverter);
			if (tColor > 0) tNBT.setByte("mColor", tColor);
			if (tOtherUpgrades > 0) tNBT.setByte("mOtherUpgrades", tOtherUpgrades);
			if (tStrongRedstone > 0) tNBT.setByte("mStrongRedstone", tStrongRedstone);
			for (byte i = 0; i < tCoverSides.length; i++) {
				if (tCoverSides[i] != 0) {
					tNBT.setIntArray("mCoverData", tCoverData);
					tNBT.setIntArray("mCoverSides", tCoverSides);
					break;
				}
			}

			//Set Item NBT
			if (!o.isInvalid()){
				((IMetaTileEntity) tMetaTileEntity).setItemNBT(tNBT); //Valid? Idk
			}
			else {
				try {
					Logger.REFLECTION("Trying to set NBT data to Itemstack from invalid tile, World might explode.");
					((IMetaTileEntity) tMetaTileEntity).setItemNBT(tNBT); //Valid? Idk
				}
				catch (Throwable t){}				
			}
			
			//Set stack NBT
			if (!tNBT.hasNoTags()) rStack.setTagCompound(tNBT);
			return new ArrayList<ItemStack>(Arrays.asList(rStack));
		}		
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException a){}
		return null;
	}


	public static final class GT_MetaTile_Visitor extends ClassVisitor {

		public GT_MetaTile_Visitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);		
			if(name.equals("getDrops") && desc.equals("()Ljava/util/ArrayList;")) {
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Found target method. [Unobfuscated]");
				return new RegisterOreImplVisitor(methodVisitor, false);
			}
			else if(name.equals("getDrops") && desc.equals("(Ljava/lang/String;Ladd;)V")) {
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Found target method. [Obfuscated]");
				return new RegisterOreImplVisitor(methodVisitor, true);
			}
			return methodVisitor;
		}

	}

	private static final class RegisterOreImplVisitor extends MethodVisitor {

		private final boolean mObfuscated;

		public RegisterOreImplVisitor(MethodVisitor mv, boolean obfuscated) {
			super(ASM5, mv);
			this.mObfuscated = obfuscated;
		}

		@Override
		public void visitCode() {
			FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Fixing Greg & Blood's poor attempt at setItemNBT().");
			/*super.visitCode();
			super.visitVarInsn(ALOAD, 0);
			super.visitVarInsn(ALOAD, 1);
			if (!mObfuscated){
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Injecting target method. [Unobfuscated]");				
			super.visitMethodInsn(INVOKESTATIC, 
					"gtPlusPlus/preloader/Preloader_GT_OreDict", 
					"shouldPreventRegistration",
					"(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)Z",
					false);
			}
			else {
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Injecting target method. [Obfuscated]");
				super.visitMethodInsn(INVOKESTATIC, 
						"gtPlusPlus/preloader/Preloader_GT_OreDict", 
						"shouldPreventRegistration",
						"(Ljava/lang/String;Ladd;)Z",
						false);
			}
			Label endLabel = new Label();
			super.visitJumpInsn(IFEQ, endLabel);
			super.visitInsn(RETURN);
			super.visitLabel(endLabel);*/
		}

	}

}
