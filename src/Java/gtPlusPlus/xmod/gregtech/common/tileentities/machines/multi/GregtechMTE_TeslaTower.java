package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.GT_Values.W;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.entity.EntityTeslaTowerLightning;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMTE_TeslaTower extends GregtechMeta_MultiBlockBase {

	private Block casingBlock;
	private int casingMeta;
	private int frameMeta;
	private int casingTextureIndex;

	private ForgeDirection back;

	private int xLoc, yLoc, zLoc;

	protected int mRange;
	/**
	 * Machine Mode, 
	 * {@value false} Attacks all entities, 
	 * {@value true} Only attacks players. 
	 */
	protected volatile boolean mMode = false;

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_TeslaTower(mName);
	}

	public GregtechMTE_TeslaTower(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		initFields();
	}

	public GregtechMTE_TeslaTower(String aName) {
		super(aName);
		initFields();
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String[] getDescription() {
		String casings = getCasingBlockItem().get(0).getDisplayName();
		return new String[]{
				"Controller Block for the Tesla Defence Tower Mk3200",
				"Enemies within "+this.mRange+"m are blasted with a high energy plasma.",
				"This uses 5,000,000EU per blast.",
				"Can screwdriver to toggle mode between Players and all Entities.",
				"Size(WxHxD): 3x7x3", "Controller (Front middle at bottom)",
				"3x1x3 Base of " + casings,
				"1x3x1 " + casings + " pillar (Center of base)",
				"1x3x1 " + MaterialUtils.getMaterialName(getFrameMaterial()) + " Frame Boxes (Each pillar side and on top)",
				"1x Maintenance Hatch (One of base casings)",
				"1x " + VN[getMinTier()] + "+ Energy Hatch (Any bottom layer casing)"};
	}

	private final void initFields() {
		casingBlock = ModBlocks.blockCasings2Misc;
		casingMeta = getCasingBlockItem().get(0).getItemDamage();
		int frameId = 4096 + getFrameMaterial().mMetaItemSubID;
		frameMeta = GregTech_API.METATILEENTITIES[frameId] != null ? GregTech_API.METATILEENTITIES[frameId].getTileEntityBaseType() : W;
		casingTextureIndex = getCasingTextureIndex();
		mRange = 50;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getCasingTextureIndex()]};
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setBoolean("mMode", this.mMode);
		aNBT.setInteger("mRange", this.mRange);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mMode = aNBT.getBoolean("mMode");
		this.mRange = aNBT.getInteger("mRange");
		super.loadNBTData(aNBT);
	}

	private boolean isEnergyEnough() {
		if (this.getEUVar() >= 5000000){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		/*if (!isEnergyEnough()) {
			this.mProgresstime = 0;
			this.mMaxProgresstime = 20;
			this.getBaseMetaTileEntity().setActive(false);
			stopMachine();
		}
		else {*//*
			this.mProgresstime = 1;
			this.mMaxProgresstime = 100;
			this.getBaseMetaTileEntity().setActive(true);*/
		//}
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		updateCoordinates();
		//check base layer
		for (int xOff = -1 + back.offsetX; xOff <= 1 + back.offsetX; xOff++) {
			for (int zOff = -1 + back.offsetZ; zOff <= 1 + back.offsetZ; zOff++) {
				if (xOff == 0 && zOff == 0) continue;

				IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xOff, 0, zOff);
				if (!checkCasingBlock(xOff, 0, zOff)
						&& !addMaintenanceToMachineList(tTileEntity, casingTextureIndex)
						&& !addEnergyInputToMachineList(tTileEntity, casingTextureIndex)){
					Logger.INFO("bad block");
					return false;
				}
			}
		}
		if(!checkHatches()){
			Logger.INFO("bad Hatches");
			return false;
		}
		if (GT_Utility.getTier(getMaxInputVoltage()) < getMinTier()){
			Logger.INFO("bad Voltage");
			return false;
		}
		//check tower
		for (int yOff = 1; yOff < 4; yOff++) {
			if (!checkCasingBlock(back.offsetX, yOff, back.offsetZ)
					|| !checkFrameBlock(back.offsetX + 1, yOff, back.offsetZ)
					|| !checkFrameBlock(back.offsetX - 1, yOff, back.offsetZ)
					|| !checkFrameBlock(back.offsetX, yOff, back.offsetZ + 1)
					|| !checkFrameBlock(back.offsetX, yOff, back.offsetZ - 1)
					|| !checkFrameBlock(back.offsetX, yOff + 3, back.offsetZ)){
				Logger.INFO("bad frame?");
				return false;
			}
		}
		Logger.INFO("good");
		return true;
	}

	private void updateCoordinates() {
		casingTextureIndex = getCasingTextureIndex();
		mRange = 50;
		xLoc = getBaseMetaTileEntity().getXCoord();
		yLoc = getBaseMetaTileEntity().getYCoord();
		zLoc = getBaseMetaTileEntity().getZCoord();
		back = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing());
	}

	protected boolean checkCasingBlock(int xOff, int yOff, int zOff) {
		Logger.INFO("Looking For Casing.");
		return checkBlockAndMetaOffset(xOff, yOff, zOff, casingBlock, casingMeta);
	}
	//meta of frame is getTileEntityBaseType; frame should be checked using its drops (possible a high weight operation)
	protected boolean checkFrameBlock(int xOff, int yOff, int zOff) {
		Logger.INFO("Looking For Frame.");
		return checkBlockAndMetaOffset(xOff, yOff, zOff, GregTech_API.sBlockMachines, frameMeta);
	}

	protected boolean checkBlockAndMetaOffset(int xOff, int yOff, int zOff, Block block, int meta) {
		return checkBlockAndMeta(xLoc + xOff, yLoc + yOff, zLoc + zOff, block, meta);
	}

	private boolean checkBlockAndMeta(int x, int y, int z, Block block, int meta) {
		Logger.INFO("Found: "+getBaseMetaTileEntity().getBlock(x, y, z).getLocalizedName()+" | Meta: "+getBaseMetaTileEntity().getMetaID(x, y, z));
		Logger.INFO("Expected: "+block.getLocalizedName()+" | Meta: "+meta);
		return (meta == W || getBaseMetaTileEntity().getMetaID(x, y, z) == meta)
				&& getBaseMetaTileEntity().getBlock(x, y, z) == block;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
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
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	protected int getMinTier() {
		return 7;
	}

	protected boolean checkHatches() {
		return !mMaintenanceHatches.isEmpty() && !mEnergyHatches.isEmpty();
	}

	private Map<Pair<Long, Long>, Entity> mInRange = new HashMap<Pair<Long, Long>, Entity>();

	@SuppressWarnings("unchecked")
	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		try {			
			if (this.getBaseMetaTileEntity().isServerSide()){
				if (this.mEnergyHatches.size() > 0) {
					for (final GT_MetaTileEntity_Hatch_Energy tHatch : this.mEnergyHatches){
						if (isValidMetaTileEntity(tHatch)) {
							long mHT = tHatch.getBaseMetaTileEntity().getInputVoltage();
							if (tHatch.getEUVar() >= mHT) {
								for (int o=0;o<(tHatch.getEUVar()/mHT);o++){
									//1A
									if (this.getEUVar()<(this.maxEUStore()-mHT)){
										tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(mHT, false);
										this.setEUVar(this.getEUVar()+mHT);
									}
									//2A
									if (this.getEUVar()<(this.maxEUStore()-mHT)){
										tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(mHT, false);
										this.setEUVar(this.getEUVar()+mHT);
									}
								}
							}
						}
					}
				}

				if (aTick % 10 == 0){
					if (this.getEUVar() >= 5000000){
						//Logger.INFO("Can Zap.");
						this.getBaseMetaTileEntity().enableWorking();
						this.getBaseMetaTileEntity().setActive(true);
						if (this.mProgresstime <= 0){
							this.mProgresstime++;
						}
						else if (this.mProgresstime >= 100){
							this.mProgresstime = 0;
						}
						this.mMaxProgresstime = 10000;
					}
					//Logger.INFO("Allowed to be Working? "+this.getBaseMetaTileEntity().isAllowedToWork());
					//Logger.INFO("Working? "+this.getBaseMetaTileEntity().isActive());
					//Logger.INFO("Has Working just been enabled? "+this.getBaseMetaTileEntity().hasWorkJustBeenEnabled());
				}


				if (aTick % 20 == 0){
					List<Object> o = aBaseMetaTileEntity.getWorld().loadedEntityList;			
					//Clean up old entities first
					if (this.mInRange.size() > 0){
						for (Entity j : this.mInRange.values()){
							if (((Entity) j).getDistance(this.xLoc, this.yLoc, this.zLoc) > this.mRange){
								mInRange.remove(new Pair<Long, Long>(((Entity) j).getUniqueID().getMostSignificantBits(), ((Entity) j).getUniqueID().getLeastSignificantBits()), (Entity) j);
							}					
						}
					}			
					//Add new entities
					if (o.size() > 0){
						for (Object r : o){
							if (r instanceof Entity){
								if (!((Entity) r).getUniqueID().equals(getOwner())){
									if (((Entity) r).isEntityAlive() || r instanceof EntityLiving){
										if (((Entity) r).getDistance(this.xLoc, this.yLoc, this.zLoc) <= this.mRange){
											if (r instanceof EntityItem){
												//Do nothing
											}
											else {
												if (!this.mMode){
													mInRange.put(new Pair<Long, Long>(((Entity) r).getUniqueID().getMostSignificantBits(), ((Entity) r).getUniqueID().getLeastSignificantBits()), (Entity) r);
												}
												else {
													if (r instanceof EntityPlayer){
														mInRange.put(new Pair<Long, Long>(((Entity) r).getUniqueID().getMostSignificantBits(), ((Entity) r).getUniqueID().getLeastSignificantBits()), (Entity) r);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}	
			}
		}
		catch (Throwable r){

		}	
		super.onPreTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		try {
			if (this.getBaseMetaTileEntity().isServerSide()){
				//Handle Progress Time
				if (this.getEUVar() >= 0 && !this.getBaseMetaTileEntity().isAllowedToWork()){
					this.mProgresstime = 20;
					this.mMaxProgresstime = 40;	
				}
				else if (this.getEUVar() >= 0 && this.getBaseMetaTileEntity().isAllowedToWork()){
					this.mProgresstime = 20;
					this.mMaxProgresstime = 40;	
				}

				if (aTick % 10 == 0){
					if (this.mInRange.size() > 0){
						if (this.getEUVar() >= 5000000){						

							for (Entity f : mInRange.values()){
								if (f instanceof EntityLiving){
									int j1 = (int) f.posX;
									int l1 = (int) f.posY;
									int k1 = (int) f.posZ;
									World world = aBaseMetaTileEntity.getWorld();

									if (f.isEntityAlive() && !f.getUniqueID().equals(getOwner())){
										//if (world.canLightningStrikeAt(j1, l1+1, k1)){
										//if (isEnergyEnough() && world.addWeatherEffect(new EntityTeslaTowerLightning(world, (double)j1, (double)l1, (double)k1))){								
										if (isEnergyEnough() && world.addWeatherEffect(new EntityTeslaTowerLightning(world, (double)j1, (double)l1, (double)k1, f, getOwner()))){	
											if (f == null || f.isDead || !f.isEntityAlive()){
												this.mInRange.remove(new Pair<Long, Long>(f.getUniqueID().getMostSignificantBits(),  f.getUniqueID().getLeastSignificantBits()));
											}
											this.setEUVar(this.getEUVar()-5000000);
										}
										//}
									}

								}
							}
						}
					}
				}
			}
		}
		catch (Throwable r){

		}
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}


	protected GregtechItemList getCasingBlockItem() {
		return GregtechItemList.Casing_TeslaTower;
	}

	protected Materials getFrameMaterial() {		
		/*casingBlock = getCasingBlockItem().getBlock();
		casingMeta = getCasingBlockItem().get(0).getItemDamage();
		int frameId = 4096 + getFrameMaterial().mMetaItemSubID;
		frameMeta = GregTech_API.METATILEENTITIES[frameId] != null ? GregTech_API.METATILEENTITIES[frameId].getTileEntityBaseType() : W;		
		 */return Materials.get("TungstenCarbide");
	}

	protected int getCasingTextureIndex() {
		return TAE.GTPP_INDEX(30);
	}

	public UUID getOwner(){
		return PlayerUtils.getPlayersUUIDByName(this.getBaseMetaTileEntity().getOwnerName());
	}

	@Override
	public boolean isEnetInput() {
		return false;
	}

	@Override
	public long maxAmperesIn() {
		return 32;
	}

	@Override
	public long maxEUInput() {
		return 131072;
	}

	@Override
	public long maxEUStore() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mMode = Utils.invertBoolean(mMode);		
		this.mInRange.clear();
		if (mMode){
			PlayerUtils.messagePlayer(aPlayer, "[Tesla Tower] Now only targetting players.");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "[Tesla Tower] Targetting all types of entities.");
		}		
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}


}
