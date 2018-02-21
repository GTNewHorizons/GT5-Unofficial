package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.blocks.GT_Block_Machines;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.objects.random.XSTR;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.nbt.NBTUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Preloader_ClassTransformer2 {

	/**
	 * 
	 * So what I'd try is something like patch a new field into BaseMetaTileEntity to hold the ItemNBT, 
	 * then patch GT_Block_Machines.breakBlock to store the ItemNBT into that field by calling setItemNBT, 
	 * and then patch BaseMetaTileEntity.getDrops to retrieve that field instead of calling setItemNBT
	 * But there's probably a simpler solution if all you want to do is fix this 
	 * for your super tanks rather than for all GT machines 
	 * (which would only include saving the output count for chest buffers and item distributors...)
	 *
	 */ 



	public static boolean mHasSetField = false;

	private final static Class<BaseMetaTileEntity> customTransformer2 = BaseMetaTileEntity.class;
	public static final class GT_MetaTile_Visitor extends ClassVisitor {
		private boolean isGt_Block_Machines = false;

		public GT_MetaTile_Visitor(ClassVisitor cv, boolean isGt_Block_Machines) {
			super(ASM5, cv);
			this.isGt_Block_Machines = isGt_Block_Machines;
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			FieldVisitor j =  super.visitField(access, name, desc, signature, value);
			if (!mHasSetField && !isGt_Block_Machines) {
				mHasSetField = true;				
				FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Injecting field 'mItemStorageNBT' into BaseMetaTileEntity.java. Access OpCode: "+access);
				j = cv.visitField(0, "mItemStorageNBT", "Lnet/minecraft/nbt/NBTTagCompound;", null, null);
				j.visitEnd();
			}
			return j;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

			if (isGt_Block_Machines) {                      //Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)V
				if(name.equals("breakBlock") && desc.equals("(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)V")) {
					FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Found target method 'breakBlock' [Unobfuscated]. Access OpCode: "+access);
					return new swapBreakBlock(methodVisitor);				
				}
				else if (name.equals("breakBlock") && !desc.equals("(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)V")) {
					FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Found target method 'breakBlock' [Obfuscated]. Access OpCode: "+access);
					return new swapBreakBlock(methodVisitor);	
				}
			}
			else {
				if(name.equals("getDrops") && desc.equals("()Ljava/util/ArrayList;")) {
					FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Found target method 'getDrops'. Access OpCode: "+access);
					return new swapGetDrops(methodVisitor);				
				}
			}			
			return methodVisitor;
		}

	}




	private static final class swapGetDrops extends MethodVisitor {

		public swapGetDrops(MethodVisitor mv) {
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

	private static final class swapBreakBlock extends MethodVisitor {

		public swapBreakBlock(MethodVisitor mv) {
			super(ASM5, mv);
		}

		@Override
		public void visitCode() {
			FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Fixing breakBlock() in GT_Block_Machines.class");
			super.visitCode();
			//super.visitVarInsn(ALOAD, 0);			

			super.visitVarInsn(ALOAD, 1);
			super.visitVarInsn(ILOAD, 2);
			super.visitVarInsn(ILOAD, 3);
			super.visitVarInsn(ILOAD, 4);
			super.visitVarInsn(ALOAD, 5);
			super.visitVarInsn(ILOAD, 6);

			super.visitMethodInsn(INVOKESTATIC,
					"gtPlusPlus/preloader/asm/transformers/Preloader_ClassTransformer2",
					"breakBlock",
					"(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;I)V",
					false);
			FMLRelaunchLog.log("[GT++ ASM] NBTFixer", Level.INFO, "Method should now be replaced.");
			super.visitInsn(RETURN);
		}

	}
























	public static ArrayList<ItemStack> getDrops(BaseMetaTileEntity o) {
		Logger.INFO("BaseMetaTileEntity.getDrops(BaseMetaTileEntity(this))");
		try {
			short tID = (short) ReflectionUtils.getField(customTransformer2, "mID").get(o);
			ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, tID);
			
			NBTTagCompound i = new NBTTagCompound();

			i = stupidFuckingNBTMap.get(new BlockPos(o.xCoord, o.yCoord, o.zCoord));
			Logger.INFO("Got NBT Tag Value from map.");			
			
			NBTTagCompound tNBT = i;
			if (tNBT == null) {
				Logger.INFO("Map tag was null.");
				tNBT = generateGetDropsNBT(o);
			}
			if (!tNBT.hasNoTags()) {
				rStack.setTagCompound(tNBT);	
				Logger.INFO("Iterating rStack NBT.");
				NBTUtils.tryIterateNBTData(rStack);
			}
			return new ArrayList<ItemStack>(Arrays.asList(rStack));
		}		
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException a){
			Logger.REFLECTION("getDropsHack2");
			a.printStackTrace();
		}
		ArrayList<ItemStack> u = new ArrayList<ItemStack>(Arrays.asList(new ItemStack[]{ItemUtils.getSimpleStack(Blocks.bedrock)}));
		return u;
	}

	public static Map<BlockPos, NBTTagCompound> stupidFuckingNBTMap = new HashMap<BlockPos, NBTTagCompound>();
	public static void breakBlock(final World aWorld, final int aX, final int aY, final int aZ, final Block block,
			final int meta) {
		Logger.INFO("GT_Block_Machines.breakBlock()");
		GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
		final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity instanceof IGregTechTileEntity) {
			final IGregTechTileEntity tGregTechTileEntity = (IGregTechTileEntity) tTileEntity;			
			final Random tRandom = new XSTR();
			GT_Block_Machines.mTemporaryTileEntity.set(tGregTechTileEntity);

			//Try inject this
			Logger.INFO("Hopefully saving ItemNBT data.");

			NBTTagCompound tNBT = new NBTTagCompound();
			tNBT = generateGetDropsNBT(tGregTechTileEntity.getMetaTileEntity().getBaseMetaTileEntity());
			tGregTechTileEntity.getMetaTileEntity().setItemNBT(tNBT);
			Field fffff;
			try {
				fffff = ReflectionUtils.getField(tGregTechTileEntity.getClass(), "mItemStorageNBT");
				if (fffff == null) {	
					Logger.REFLECTION("Injected field is null.");	
				}
				else {
					fffff.set(tGregTechTileEntity.getMetaTileEntity().getBaseMetaTileEntity(), tNBT);
					Logger.REFLECTION("Hopefully injected field data.");
					stupidFuckingNBTMap.put(new BlockPos(aX, aY, aZ), tNBT);		
					Logger.INFO("Set NBT Tag Value to map.");			
				}
			}
			catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			for (int i = 0; i < tGregTechTileEntity.getSizeInventory(); ++i) {
				final ItemStack tItem = tGregTechTileEntity.getStackInSlot(i);
				if (tItem != null && tItem.stackSize > 0 && tGregTechTileEntity.isValidSlot(i)) {



					final EntityItem tItemEntity = new EntityItem(aWorld,
							(double) (aX + tRandom.nextFloat() * 0.8f + 0.1f),
							(double) (aY + tRandom.nextFloat() * 0.8f + 0.1f),
							(double) (aZ + tRandom.nextFloat() * 0.8f + 0.1f),
							new ItemStack(tItem.getItem(), tItem.stackSize, tItem.getItemDamage()));
					if (tItem.hasTagCompound()) {
						tItemEntity.getEntityItem().setTagCompound((NBTTagCompound) tItem.getTagCompound().copy());
					}
					tItemEntity.motionX = tRandom.nextGaussian() * 0.0500000007450581;
					tItemEntity.motionY = tRandom.nextGaussian() * 0.0500000007450581 + 0.2000000029802322;
					tItemEntity.motionZ = tRandom.nextGaussian() * 0.0500000007450581;
					aWorld.spawnEntityInWorld((Entity) tItemEntity);
					tItem.stackSize = 0;
					tGregTechTileEntity.setInventorySlotContents(i, (ItemStack) null);
				}
			}
		}

		//gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer2.breakBlockWorld(aWorld, aX, aY, aZ, block, meta);		
		aWorld.removeTileEntity(aX, aY, aZ);
	}

	public static void breakBlockWorld(World world, int aX, int aY, int aZ, Block block, int meta){
		if (block.hasTileEntity(meta) && !(block instanceof BlockContainer))
		{
			world.removeTileEntity(aX, aY, aZ);
		}
	}

	public static NBTTagCompound generateGetDropsNBT(IGregTechTileEntity iGregTechTileEntity) {
			Logger.INFO("generateGetDropsNBT()");
			try {
				short tID = (short) ReflectionUtils.getField(customTransformer2, "mID").get(iGregTechTileEntity);
				NBTTagCompound tRecipeStuff = (NBTTagCompound) ReflectionUtils.getField(customTransformer2, "mRecipeStuff").get(iGregTechTileEntity);
				boolean tMuffler = (boolean) ReflectionUtils.getField(customTransformer2, "mMuffler").get(iGregTechTileEntity);
				boolean tLockUpgrade = (boolean) ReflectionUtils.getField(customTransformer2, "mLockUpgrade").get(iGregTechTileEntity);
				boolean tSteamConverter = (boolean) ReflectionUtils.getField(customTransformer2, "mSteamConverter").get(iGregTechTileEntity);
				byte tColor = (byte) ReflectionUtils.getField(customTransformer2, "mColor").get(iGregTechTileEntity);
				byte tOtherUpgrades = (byte) ReflectionUtils.getField(customTransformer2, "mOtherUpgrades").get(iGregTechTileEntity);
				byte tStrongRedstone = (byte) ReflectionUtils.getField(customTransformer2, "mStrongRedstone").get(iGregTechTileEntity);
				int[] tCoverSides = (int[]) ReflectionUtils.getField(customTransformer2, "mCoverSides").get(iGregTechTileEntity);
				int[] tCoverData = (int[]) ReflectionUtils.getField(customTransformer2, "mCoverData").get(iGregTechTileEntity);

				NBTTagCompound mItemStorageNBT;
				MetaTileEntity tMetaTileEntity = (MetaTileEntity) ReflectionUtils.getField(customTransformer2, "mMetaTileEntity").get(iGregTechTileEntity);
				Field fffff = ReflectionUtils.getField(customTransformer2, "mItemStorageNBT");
				if (fffff == null) {
					Logger.REFLECTION("Injected field is null.");
					mItemStorageNBT =  new NBTTagCompound();
				}
				else {
					Logger.REFLECTION("Injected field exists.");	
					if (fffff.get(iGregTechTileEntity) != null) {
						Logger.REFLECTION("Injected field has value.");	
						mItemStorageNBT = (NBTTagCompound) fffff.get(iGregTechTileEntity);
					}
					else {
						Logger.REFLECTION("Injected field has no value.");	
						mItemStorageNBT =  null;					
					}
				}

				//BaseMetaTileEntity tMetaTileEntity = o;

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
				Logger.REFLECTION("mItemStorageNBT: "+(mItemStorageNBT != null));

				//mItemStorageNBT

				ItemStack rStack = new ItemStack(GregTech_API.sBlockMachines, 1, tID);
				NBTTagCompound tNBT = new NBTTagCompound();

				if (tRecipeStuff != null && !tRecipeStuff.hasNoTags()) tNBT.setTag("GT.CraftingComponents", tRecipeStuff);
				if (mItemStorageNBT != null && !mItemStorageNBT.hasNoTags()) tNBT.setTag("mItemStorageNBT", mItemStorageNBT);
				if (tMuffler) tNBT.setBoolean("mMuffler", tMuffler);
				if (tLockUpgrade) tNBT.setBoolean("mLockUpgrade", tLockUpgrade);
				if (tSteamConverter) tNBT.setBoolean("mSteamConverter", tSteamConverter);
				if (tColor > 0) tNBT.setByte("mColor", tColor);
				//if (tFluid != null) tFluid.writeToNBT(tNBT);
				//if (tFluid != null) tNBT.setTag("mFluid", tFluid);
				if (tOtherUpgrades > 0) tNBT.setByte("mOtherUpgrades", tOtherUpgrades);
				if (tStrongRedstone > 0) tNBT.setByte("mStrongRedstone", tStrongRedstone);
				for (byte i = 0; i < tCoverSides.length; i++) {
					if (tCoverSides[i] != 0) {
						tNBT.setIntArray("mCoverData", tCoverData);
						tNBT.setIntArray("mCoverSides", tCoverSides);
						break;
					}
				}
				
				//Set stack NBT
				if (!tNBT.hasNoTags()) {
					Logger.INFO("Returning Valid NBT data");
					return tNBT;
				}
				else {
					Logger.INFO("Returning Invalid NBT data");					
				}
			}		
			catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException a){
				Logger.REFLECTION("getDropsHack2");
				a.printStackTrace();
			}
			return new NBTTagCompound();
		}

}
