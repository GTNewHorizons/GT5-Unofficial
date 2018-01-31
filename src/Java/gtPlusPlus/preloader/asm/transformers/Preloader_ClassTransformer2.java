package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class Preloader_ClassTransformer2 {

	private final static Class<BaseMetaTileEntity> customTransformer2 = BaseMetaTileEntity.class;

	public static ArrayList<ItemStack> getDrops(BaseMetaTileEntity o) {
		Logger.INFO("DROP!");
		try {
			short tID = (short) ReflectionUtils.getField(customTransformer2, "mID").get(o);
			NBTTagCompound tRecipeStuff = (NBTTagCompound) ReflectionUtils.getField(customTransformer2, "mRecipeStuff").get(o);
			boolean tMuffler = (boolean) ReflectionUtils.getField(customTransformer2, "mMuffler").get(o);
			boolean tLockUpgrade = (boolean) ReflectionUtils.getField(customTransformer2, "mLockUpgrade").get(o);
			boolean tSteamConverter = (boolean) ReflectionUtils.getField(customTransformer2, "mSteamConverter").get(o);
			byte tColor = (byte) ReflectionUtils.getField(customTransformer2, "mColor").get(o);
			byte tOtherUpgrades = (byte) ReflectionUtils.getField(customTransformer2, "mOtherUpgrades").get(o);
			byte tStrongRedstone = (byte) ReflectionUtils.getField(customTransformer2, "mStrongRedstone").get(o);
			int[] tCoverSides = (int[]) ReflectionUtils.getField(customTransformer2, "mCoverSides").get(o);
			int[] tCoverData = (int[]) ReflectionUtils.getField(customTransformer2, "mCoverData").get(o);
			FluidStack tFluid = null;
			if (GT_MetaTileEntity_BasicTank.class.isInstance(o)) {
				try {
				 tFluid = (FluidStack) ReflectionUtils.getField(GT_MetaTileEntity_BasicTank.class, "mFluid").get(o);	
				}
				catch (Throwable t) {
					Logger.REFLECTION("mFluid was not set.");					
				}
			}
			else {
				Logger.REFLECTION("mFluid was not found.");
			}
			BaseMetaTileEntity tMetaTileEntity = o;

			Logger.REFLECTION("tID: "+(tID != 0));
			Logger.REFLECTION("tRecipeStuff: "+(tRecipeStuff != null));
			Logger.REFLECTION("tMuffler: "+(tMuffler != false));
			Logger.REFLECTION("tLockUpgrade: "+(tLockUpgrade != false));
			Logger.REFLECTION("tSteamConverter: "+(tSteamConverter != false));
			Logger.REFLECTION("tColor: "+(tColor != 0));
			Logger.REFLECTION("tOtherUpgrades: "+(tOtherUpgrades != 0));
			Logger.REFLECTION("tCoverSides: "+(tCoverSides != null));
			Logger.REFLECTION("tCoverData: "+(tCoverData != null));
			Logger.REFLECTION("tMetaTileEntity: "+(tMetaTileEntity != null));
			
			ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, tID);
			NBTTagCompound tNBT = new NBTTagCompound();

			if (tRecipeStuff != null && !tRecipeStuff.hasNoTags()) tNBT.setTag("GT.CraftingComponents", tRecipeStuff);
			if (tMuffler) tNBT.setBoolean("mMuffler", tMuffler);
			if (tLockUpgrade) tNBT.setBoolean("mLockUpgrade", tLockUpgrade);
			if (tSteamConverter) tNBT.setBoolean("mSteamConverter", tSteamConverter);
			if (tColor > 0) tNBT.setByte("mColor", tColor);
			//if (tFluid != null) tFluid.writeToNBT(tNBT);
			if (tFluid != null) tNBT.setTag("mFluid", tFluid.tag);
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
				catch (Throwable t){
					Logger.REFLECTION("getDropsHack1");
					t.printStackTrace();
					}				
			}

			//Set stack NBT
			if (!tNBT.hasNoTags()) rStack.setTagCompound(tNBT);
			return new ArrayList<ItemStack>(Arrays.asList(rStack));
		}		
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException a){
			Logger.REFLECTION("getDropsHack2");
			a.printStackTrace();
		}
		ArrayList<ItemStack> u = new ArrayList<ItemStack>(Arrays.asList(new ItemStack[]{ItemUtils.getSimpleStack(Blocks.bedrock)}));
		return u;
	}


	public static final class GT_MetaTile_Visitor extends ClassVisitor {

		public GT_MetaTile_Visitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);		
			if(name.equals("getDrops") && desc.equals("()Ljava/util/ArrayList;")) {
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Found target method. Access OpCode: "+access);
				//return new TrySwapMethod(methodVisitor, access, name, desc);
				return new SwapTwo(methodVisitor);				
			}
			return methodVisitor;
		}

	}

	/*private static final class TrySwapMethod extends GeneratorAdapter {

		public TrySwapMethod(MethodVisitor mv, int access, String name, String desc) {
			super(Opcodes.ASM5, mv, access, name, desc);
			FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Created method swapper for '"+name+"' in 'gregtech/api/metatileentity/BaseMetaTileEntity'. Desc: "+desc);  
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			//gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer2
			if(opcode==INVOKESPECIAL && owner.equals("net/minecraft/item/ItemStack")
					&& name.equals("<init>") && desc.equals("(Lnet/minecraft/block/Block;II)V")) {
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Trying to proccess '"+name+"' | Opcode: "+opcode+" | Desc: "+desc+" | Owner: "+owner);
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Fixing Greg & Blood's poor attempt at setItemNBT().");
				// not relaying the original instruction to super effectively removes the original 
				// instruction, instead we're producing a different instruction:
				super.visitVarInsn(ALOAD, 0);
				super.visitMethodInsn(INVOKESTATIC, "gtPlusPlus/preloader/asm/transformers/Preloader_ClassTransformer2",
						"getDrops", "()Ljava/util/ArrayList;", false);
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Method should now be replaced.");
			}
			else { // relaying to super will reproduce the same instruction
				//FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Original method call.");
				super.visitMethodInsn(opcode, owner, name, desc, itf);
			}
		}

		// all other, not overridden visit methods reproduce the original instructions
	}*/

	private static final class SwapTwo extends MethodVisitor {

		public SwapTwo(MethodVisitor mv) {
			super(ASM5, mv);
		}

		@Override
		public void visitCode() {
			FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Fixing Greg & Blood's poor attempt at setItemNBT().");
			super.visitCode();			
			//ALOAD 0
			//INVOKESTATIC gtPlusPlus/preloader/asm/transformers/Preloader_ClassTransformer2 getDrops (Lgregtech/api/metatileentity/BaseMetaTileEntity;)Ljava/util/ArrayList;
			//ARETURN
			
			super.visitVarInsn(ALOAD, 0);
			super.visitMethodInsn(INVOKESTATIC,
					"gtPlusPlus/preloader/asm/transformers/Preloader_ClassTransformer2",
					"getDrops",
					"(Lgregtech/api/metatileentity/BaseMetaTileEntity;)Ljava/util/ArrayList;",
					false);
			FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Method should now be replaced.");
			//super.visitVarInsn(ARETURN, 0);
			super.visitInsn(ARETURN);
		}

	}

}
