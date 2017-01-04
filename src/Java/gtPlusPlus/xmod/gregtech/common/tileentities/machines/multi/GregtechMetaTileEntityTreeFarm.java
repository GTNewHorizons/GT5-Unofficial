package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkPosition;

public class GregtechMetaTileEntityTreeFarm extends GT_MetaTileEntity_MultiBlockBase {


	private static final ITexture[] FACING_SIDE = {new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Tumbaga)};
	private static final ITexture[] FACING_FRONT = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE)};
	private static final ITexture[] FACING_ACTIVE = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE_ACTIVE)};


	private boolean running = false;
	private boolean p1, p2, p3, p4, p5, p6;
	public int mMaxProgresstime = 0;
	public int mUpdate = 5;
	public int mProgresstime = 0;
	public boolean mMachine = false;
	public ItemStack mOutputItem1;
	public ItemStack mOutputItem2;
	private Block Humus;

	public GregtechMetaTileEntityTreeFarm(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntityTreeFarm(String aName) {
		super(aName);
	}   

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Tree Farmer",
				"How to get your first logs without an axe.",
				"Max Size(WxHxD): 9x1x9 (Controller, with upto 4 dirt out each direction on a flat plane.)",
				"Dirt for the rest! [D = Dirt, X = Controller]",
				"DDDDDDDDD",
				"DDDDDDDDD",
				"DDDDDDDDD",
				"DDDDDDDDD",
				"DDDDXDDDD",
				"DDDDDDDDD",
				"DDDDDDDDD",
				"DDDDDDDDD",
		"DDDDDDDDD"};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == 1) {
			return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Tumbaga), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Vent_Fast : TexturesGtBlock.Overlay_Machine_Vent_Fast)};
		}
		return new ITexture[]{new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Tumbaga)};
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public boolean isTeleporterCompatible() {
		return false;
	}

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isAccessAllowed(EntityPlayer aPlayer) {
		return true;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCoverID) {
		return (GregTech_API.getCoverBehavior(aCoverID.toStack()).isSimpleCover()) && (super.allowCoverOnSide(aSide, aCoverID));
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityTreeFarm(this.mName);
	}

	@Override
	public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()) return true;

		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		Utils.LOG_INFO("Working");
		if (!checkRecursiveBlocks()) {
			this.mEfficiency = 0;
			this.mEfficiencyIncrease = 0;
			this.mMaxProgresstime = 0;
			running = false;
			return false;
		}

		if (mEfficiency == 0) {
			this.mEfficiency = 10000;
			this.mEfficiencyIncrease = 10000;
			this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
			//GT_Pollution.addPollution(new ChunkPosition(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord()), mMaxProgresstime*5);
			return true;
		}
		this.mEfficiency = 0;
		this.mEfficiencyIncrease = 0;
		this.mMaxProgresstime = 0;
		return false;
	}

	private boolean checkRecursiveBlocks() {
		ArrayList<ChunkPosition> tList1 = new ArrayList<ChunkPosition>();
		ArrayList<ChunkPosition> tList2 = new ArrayList<ChunkPosition>();

		Block tBlock = this.getBaseMetaTileEntity().getBlockOffset(0, +1, 0);
		Utils.LOG_INFO("Looking for air above the controller.");
		if (!isAirBlock(tBlock)) {
			Utils.LOG_INFO("Did not find air above the controller.");
			return false;
		} else {
			Utils.LOG_INFO("Adding spot to a list?");
			tList2.add(new ChunkPosition(0, 0, 0));
		}
		while (!tList2.isEmpty()) {
			ChunkPosition tPos = (ChunkPosition) tList2.get(0);
			tList2.remove(0);
			if (!checkAllBlockSides(tPos.chunkPosX, tPos.chunkPosY, tPos.chunkPosZ, tList1, tList2)) {
				return false;
			}
		}
		if (running) {
			for (ChunkPosition tPos : tList1) {
				if (isWoodLog(this.getBaseMetaTileEntity().getBlockOffset(tPos.chunkPosX, tPos.chunkPosY, tPos.chunkPosZ))){
					//This does the block change to brittle charcoal.
					//this.getBaseMetaTileEntity().getWorld().setBlock(this.getBaseMetaTileEntity().getXCoord() + tPos.chunkPosX, this.getBaseMetaTileEntity().getYCoord() + tPos.chunkPosY, this.getBaseMetaTileEntity().getZCoord() + tPos.chunkPosZ, GregTech_API.sBlockConcretes, 4, 3);

				}                   
			}
			running = false;
			return false;
		} else {
			this.mMaxProgresstime = (int) Math.sqrt(tList1.size() * 240000);
		}
		running = true;
		return true;
	}

	private boolean checkAllBlockSides(int aX, int aY, int aZ, ArrayList<ChunkPosition> aList1, ArrayList<ChunkPosition> aList2) {
		p1 = false;
		p2 = false;
		p3 = false;
		p4 = false;
		p5 = false;
		p6 = false;
		Utils.LOG_INFO("checkAllBlockSides");
		
		Utils.LOG_INFO("Testing Side 1");
		Block tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX + 1, aY, aZ);
		if (aX + 1 < 6 && (isDirtBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX + 1, aY, aZ)) && (!aList2.contains(new ChunkPosition(aX + 1, aY, aZ)))){
				p1 = true;
				Utils.LOG_INFO("set p1 true");
			}
		} else if (!(tBlock == Blocks.dirt || tBlock == Blocks.grass)) {
			Utils.LOG_INFO("1- Looking for Dirt at X:"+(aX+1)+" Y:"+aY+" Z:"+aZ+" but instead found "+tBlock.getLocalizedName());
			return false;
		}

		Utils.LOG_INFO("Testing Side 2");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX - 1, aY, aZ);
		if (aX - 1 > -6 && (isDirtBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX - 1, aY, aZ)) && (!aList2.contains(new ChunkPosition(aX - 1, aY, aZ)))){
				p2 = true;
				Utils.LOG_INFO("set p2 true");
			}
		} else if (!(tBlock == Blocks.dirt || tBlock == Blocks.grass)) {
			Utils.LOG_INFO("2- Looking for Dirt at X:"+(aX-1)+" Y:"+aY+" Z:"+aZ+" but instead found "+tBlock.getLocalizedName());
			return false;
		}

		Utils.LOG_INFO("Testing For Fence X+1");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX+1, aY+1, aZ);
		if ((aY+1) == 1 && (isFenceBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX+1, aY+1, aZ)) && (!aList2.contains(new ChunkPosition(aX+1, aY+1, aZ)))){
				//p3 = true;
				Utils.LOG_INFO("set p3 true");
			}
		} else if (!(isFenceBlock(tBlock)) || !this.getBaseMetaTileEntity().getAirOffset(aX+1, aY+1, aZ)) {
			Utils.LOG_INFO("3- Looking for Fence at X:"+(aX+1)+" Y:"+(aY+1)+" Z:"+aZ+" but instead found "+tBlock.getLocalizedName());
			return false;
		}
		Utils.LOG_INFO("Testing For Fence X-1");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX-1, aY+1, aZ);
		if ((aY+1) == 1 && (isFenceBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX-1, aY+1, aZ)) && (!aList2.contains(new ChunkPosition(aX-1, aY+1, aZ)))){
				//p3 = true;
				Utils.LOG_INFO("set p3 true");
			}
		} else if (!(isFenceBlock(tBlock)) || !this.getBaseMetaTileEntity().getAirOffset(aX-1, aY+1, aZ)) {
			Utils.LOG_INFO("3- Looking for Fence at X:"+(aX-1)+" Y:"+(aY+1)+" Z:"+aZ+" but instead found "+tBlock.getLocalizedName());
			return false;
		}
		Utils.LOG_INFO("Testing For Fence Z+1");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX, aY+1, aZ+1);
		if ((aY+1) == 1 && (isFenceBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX, aY+1, aZ+1)) && (!aList2.contains(new ChunkPosition(aX, aY+1, aZ+1)))){
				//p3 = true;
				Utils.LOG_INFO("set p3 true");
			}
		} else if (!(isFenceBlock(tBlock)) || !this.getBaseMetaTileEntity().getAirOffset(aX, aY+1, aZ+1)) {
			Utils.LOG_INFO("3- Looking for Fence at X:"+(aX)+" Y:"+(aY+1)+" Z:"+(aZ+1)+" but instead found "+tBlock.getLocalizedName());
			return false;
		}
		Utils.LOG_INFO("Testing For Fence Z-1");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX, aY+1, aZ-1);
		if ((aY+1) == 1 && (isFenceBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX, aY+1, aZ-1)) && (!aList2.contains(new ChunkPosition(aX, aY+1, aZ-1)))){
				//p3 = true;
				Utils.LOG_INFO("set p3 true");
			}
		} else if (!(isFenceBlock(tBlock)) || !this.getBaseMetaTileEntity().getAirOffset(aX, aY+1, aZ-1)) {
			Utils.LOG_INFO("3- Looking for Fence at X:"+(aX)+" Y:"+(aY+1)+" Z:"+(aZ-1)+" but instead found "+tBlock.getLocalizedName());
			return false;
		}

		/*Utils.LOG_INFO("Testing Side 4");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX, aY - 1, aZ);
		if (aY - 1 > -6 && (isDirtBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX, aY - 1, aZ)) && (!aList2.contains(new ChunkPosition(aX, aY - 1, aZ)))){
				p4 = true;
				Utils.LOG_INFO("set p4 true");
			}
		} else if (tBlock != GregTech_API.sBlockCasings1) { //The Base Layer Check
			Utils.LOG_INFO("Bottom Layer is not Machine Casings HV");
			return false;
		}
		else if (this.getBaseMetaTileEntity().getMetaIDOffset(aX, aY - 1, aZ) != 3) {
			Utils.LOG_INFO("Bottom Layer has wrong meta, expecting 3. Got "+this.getBaseMetaTileEntity().getMetaIDOffset(aX, aY - 1, aZ));
			return false;
		}*/

		Utils.LOG_INFO("Testing Side 5");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX, aY, aZ + 1);
		if (aZ + 1 < 6 && (isDirtBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX, aY, aZ + 1)) && (!aList2.contains(new ChunkPosition(aX, aY, aZ + 1)))){
				p5 = true;
				Utils.LOG_INFO("set p5 true");
			}
		} else if (!(tBlock == Blocks.dirt || tBlock == Blocks.grass)) {
			Utils.LOG_INFO("Looking for Dirt at X:"+(aX)+" Y:"+aY+" Z:"+(aZ+1)+" but instead found "+tBlock.getLocalizedName());
			return false;
		}

		Utils.LOG_INFO("Testing Side 6");
		tBlock = this.getBaseMetaTileEntity().getBlockOffset(aX, aY, aZ - 1);
		if (aZ - 1 > -6 && (isDirtBlock(tBlock))) {
			if (!aList1.contains(new ChunkPosition(aX, aY, aZ - 1)) && (!aList2.contains(new ChunkPosition(aX, aY, aZ - 1)))){
				p6 = true;
				Utils.LOG_INFO("set p6 true");
			}
		} else if (!(tBlock == Blocks.dirt || tBlock == Blocks.grass)) {
			Utils.LOG_INFO("Looking for Dirt at X:"+(aX)+" Y:"+aY+" Z:"+(aZ-1)+" but instead found "+tBlock.getLocalizedName());
			return false;
		}
		aList1.add(new ChunkPosition(aX, aY, aZ));
		if (p1) aList2.add(new ChunkPosition(aX + 1, aY, aZ));
		if (p2) aList2.add(new ChunkPosition(aX - 1, aY, aZ));
		if (p3) aList2.add(new ChunkPosition(aX, aY, aZ));
		if (p4) aList2.add(new ChunkPosition(aX, aY - 1, aZ));
		if (p5) aList2.add(new ChunkPosition(aX, aY, aZ + 1));
		if (p6) aList2.add(new ChunkPosition(aX, aY, aZ - 1));
		return true;
	}

	public boolean isWoodLog(Block log){
		String tTool = log.getHarvestTool(0);
		return  OrePrefixes.log.contains(new ItemStack(log, 1))&& ((tTool != null) && (tTool.equals("axe"))) || (log.getMaterial() == Material.wood);
	}

	public boolean isDirtBlock(Block dirt){    	
		return  (dirt == Blocks.dirt ? true : (dirt == Blocks.grass ? true : (getHumus() == null ? false : (dirt == Humus ? true : false))));
	}

	public boolean isFenceBlock(Block fence){
		return  (fence == Blocks.fence ? true : (fence == Blocks.fence_gate ? true : (fence == Blocks.nether_brick_fence ? true : false)));		
	}
	
	public boolean isAirBlock(Block air){
		return (air == Blocks.air ? true : (air instanceof BlockAir ? true : false));
	}

	private Block getHumus(){
		if (!LoadedMods.Forestry){
			return null;
		}
		else if(this.Humus != null){
			return this.Humus;
		}
		else if (ReflectionUtils.doesClassExist("forestry.core.blocks.BlockSoil")){
			try {				
				Class<?> humusClass = Class.forName("forestry.core.blocks.BlockSoil");
				ItemStack humusStack = ItemUtils.getCorrectStacktype("Forestry:soil", 1);
				if (humusClass != null){
					this.Humus = Block.getBlockFromItem(humusStack.getItem());
					return Block.getBlockFromItem(humusStack.getItem());
				}
			} catch (ClassNotFoundException e) {}
		}
		return null;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mWrench = true;
		mScrewdriver = true;
		mSoftHammer = true;
		mHardHammer = true;
		mSolderingTool = true;
		mCrowbar = true;
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

}