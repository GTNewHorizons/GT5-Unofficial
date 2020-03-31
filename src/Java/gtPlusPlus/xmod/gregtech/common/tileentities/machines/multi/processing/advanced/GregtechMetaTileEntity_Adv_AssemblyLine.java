package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import java.util.ArrayList;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.LangUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Naquadah;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_Adv_AssemblyLine
        extends GregtechMeta_MultiBlockBase {

    public ArrayList<GT_MetaTileEntity_Hatch_DataAccess> mDataAccessHatches = new ArrayList<GT_MetaTileEntity_Hatch_DataAccess>();
	
    public static String[] mCasingName = new String[5];
	private final int CASING_TEXTURE_ID = TAE.getIndexFromPage(0, 13);
	private final int META_BaseCasing = 0; //4
	private final int META_ContainmentCasing = 15; //3
	private final int META_Shielding = 13; //1
	private final int META_PipeCasing = 1; //4
	private final int META_IntegralCasing = 6; //0
	private final int META_ContainmentChamberCasing = 2; //4
	
	
    public GregtechMetaTileEntity_Adv_AssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
		mCasingName[0] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 0);
		mCasingName[1] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 1);
		mCasingName[2] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 2);
		mCasingName[3] = LangUtils.getLocalizedNameOfBlock(getCasing(3), 15);
		mCasingName[4] = LangUtils.getLocalizedNameOfBlock(getCasing(1), 13);	
    }

    public GregtechMetaTileEntity_Adv_AssemblyLine(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_AssemblyLine(this.mName);
    }

    public String[] getTooltip() {
		if (mCasingName[0].toLowerCase().contains(".name")) {
			mCasingName[0] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 0);
		}
		if (mCasingName[1].toLowerCase().contains(".name")) {
			mCasingName[1] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 1);
		}
		if (mCasingName[2].toLowerCase().contains(".name")) {
			mCasingName[2] = LangUtils.getLocalizedNameOfBlock(getCasing(4), 2);
		}
		if (mCasingName[3].toLowerCase().contains(".name")) {
			mCasingName[3] = LangUtils.getLocalizedNameOfBlock(getCasing(3), 15);
		}
		if (mCasingName[4].toLowerCase().contains(".name")) {
			mCasingName[4] = LangUtils.getLocalizedNameOfBlock(getCasing(1), 13);
		}
        return new String[]{
        		"Advanced Integrated Assembly Line"
        };
    }
    
	private Block getCasing(int casingID) {
		if (casingID == 1) {
			return ModBlocks.blockCasingsMisc;
		}
		else if (casingID == 2) {
			return ModBlocks.blockCasings2Misc;
		}
		else if (casingID == 3) {
			return ModBlocks.blockCasings3Misc;
		}
		else if (casingID == 4) {
			return ModBlocks.blockCasings4Misc;
		}
		else {
			return ModBlocks.blockCasingsTieredGTPP;
		}
	}

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "AssemblyLine.png");
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return null;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    public boolean checkRecipe(ItemStack aStack) {
    	if(GT_Values.D1)System.out.println("Start ALine recipe check");
        ArrayList<ItemStack> tDataStickList = getDataItems(2);
    	if (tDataStickList.size() == 0) return false;
    	if(GT_Values.D1)System.out.println("Stick accepted, " + tDataStickList.size() + " Data Sticks found");

        ItemStack tStack[] = new ItemStack[15];
    	FluidStack[] tFluids = new FluidStack[4];
    	boolean findRecipe = false;
    	nextDS:for (ItemStack tDataStick : tDataStickList){
    		NBTTagCompound tTag = tDataStick.getTagCompound();
    		if (tTag == null) continue;
    		for (int i = 0; i < 15; i++) {
    			int count = tTag.getInteger("a"+i);
                if (!tTag.hasKey("" + i) && count <= 0) continue;
                if (mInputBusses.get(i) == null) {
                	continue nextDS;
                }
                
                ItemStack stackInSlot = mInputBusses.get(i).getBaseMetaTileEntity().getStackInSlot(0);
                boolean flag = true;
                if (count > 0) {
            		for (int j = 0; j < count; j++) {
            			tStack[i] = GT_Utility.loadItem(tTag, "a" + i + ":" + j);
            			if (tStack[i] == null) continue;
            			if(GT_Values.D1)System.out.println("Item "+i+" : "+tStack[i].getUnlocalizedName());
            			if (GT_Utility.areStacksEqual(tStack[i], stackInSlot, true) && tStack[i].stackSize <= stackInSlot.stackSize) {
            				flag = false;
            				break;
            			}
            		}
            	}
                if (flag) {
            		tStack[i] = GT_Utility.loadItem(tTag, "" + i);
            		if (tStack[i] == null) {
            			flag = false;
            			continue;
            		}
            		if(GT_Values.D1)System.out.println("Item "+i+" : "+tStack[i].getUnlocalizedName());
        			if (GT_Utility.areStacksEqual(tStack[i], stackInSlot, true) && tStack[i].stackSize <= stackInSlot.stackSize) {
        				flag = false;
        			}
            	}
                if(GT_Values.D1) System.out.println(i + (flag ? " not accepted" : " accepted"));
                if (flag) continue nextDS;
            }
    		
    		if(GT_Values.D1)System.out.println("All Items done, start fluid check");
            for (int i = 0; i < 4; i++) {
                if (!tTag.hasKey("f" + i)) continue;
                tFluids[i] = GT_Utility.loadFluid(tTag, "f" + i);
                if (tFluids[i] == null) continue;
            	if(GT_Values.D1)System.out.println("Fluid "+i+" "+tFluids[i].getUnlocalizedName());
                if (mInputHatches.get(i) == null) {
                	continue nextDS;
                }
                FluidStack fluidInHatch = mInputHatches.get(i).mFluid;
                if (fluidInHatch == null || !GT_Utility.areFluidsEqual(fluidInHatch, tFluids[i], true) || fluidInHatch.amount < tFluids[i].amount) {
                	if(GT_Values.D1)System.out.println(i+" not accepted");
                	continue nextDS;
                }
            	if(GT_Values.D1)System.out.println(i+" accepted");
            }
            
            if(GT_Values.D1)System.out.println("Input accepted, check other values");
            if (!tTag.hasKey("output")) continue;
            mOutputItems = new ItemStack[]{GT_Utility.loadItem(tTag, "output")};
            if (mOutputItems[0] == null || !GT_Utility.isStackValid(mOutputItems[0]))
                continue;
            
            if (!tTag.hasKey("time")) continue;
            mMaxProgresstime = tTag.getInteger("time");
            if (mMaxProgresstime <= 0) continue;
            
            if (!tTag.hasKey("eu")) continue;
            mEUt = tTag.getInteger("eu");
            
            if(GT_Values.D1)System.out.println("Find avaiable recipe");
            findRecipe = true;
            break;
    	}
    	if (!findRecipe) return false;

    	if(GT_Values.D1)System.out.println("All checked start consuming inputs");
        for (int i = 0; i < 15; i++) {
            if (tStack[i] == null) continue;
            ItemStack stackInSlot = mInputBusses.get(i).getBaseMetaTileEntity().getStackInSlot(0);
            stackInSlot.stackSize -= tStack[i].stackSize;
        }

        for (int i = 0; i < 4; i++) {
            if (tFluids[i] == null) continue;
            mInputHatches.get(i).mFluid.amount -= tFluids[i].amount;
            if (mInputHatches.get(i).mFluid.amount <= 0) {
                mInputHatches.get(i).mFluid = null;
            }
        }
    	if(GT_Values.D1)System.out.println("Check overclock");

        byte tTier = (byte) Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        if (mEUt <= 16) {
            this.mEUt = (mEUt * (1 << tTier - 1) * (1 << tTier - 1));
            this.mMaxProgresstime = (mMaxProgresstime / (1 << tTier - 1));
        } else {
            while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                this.mEUt *= 4;
                this.mMaxProgresstime /= 2;
            }
        }
        if (this.mEUt > 0) {
            this.mEUt = -this.mEUt;
        }
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        updateSlots();
    	if(GT_Values.D1)System.out.println("Recipe sucessfull");
        return true;
    }

    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(212), 10, 1.0F, aX, aY, aZ);
        }
    }

	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 4;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 4;

		// Counts for all Casing Types
		int aBaseCasingCount = 0;
		int aContainmentCasingCount = 0;
		int aShieldingCount = 0;
		int aPipeCount = 0;
		int aIntegralCasingCount = 0;
		int aContainmentChamberCount = 0;		

		// Bottom Layer
		aBaseCasingCount += checkEntireLayer(aBaseMetaTileEntity, getCasing(4), META_BaseCasing, -7, xDir, zDir);
		log("Bottom Layer is Valid. Moving to Layer 1.");

		// Layer 1
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -6, xDir, zDir);
		aIntegralCasingCount += checkIntegralRing(aBaseMetaTileEntity, getCasing(0), this.META_IntegralCasing, -6, xDir, zDir);
		aContainmentChamberCount += checkContainmentRing(aBaseMetaTileEntity, getCasing(4), this.META_ContainmentChamberCasing, -6, xDir, zDir);
		log("Layer 1 is Valid. Moving to Layer 2.");

		// Layer 2
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -5, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -5, xDir, zDir);
		log("Layer 2 is Valid. Moving to Layer 3.");

		// Layer 3
		aContainmentCasingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(3), this.META_ContainmentCasing, -4, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -4, xDir, zDir);
		log("Layer 3 is Valid. Moving to Layer 4.");

		// Layer 4
		aContainmentCasingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(3), this.META_ContainmentCasing, -3, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -3, xDir, zDir);
		log("Layer 4 is Valid. Moving to Layer 5.");

		// Layer 5
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -2, xDir, zDir);
		aPipeCount += checkPipes(aBaseMetaTileEntity, getCasing(4), this.META_PipeCasing, -2, xDir, zDir);
		log("Layer 5 is Valid. Moving to Layer 6.");

		// Layer 6
		aShieldingCount += checkOuterRing(aBaseMetaTileEntity, getCasing(1), this.META_Shielding, -1, xDir, zDir);
		aIntegralCasingCount += checkIntegralRing(aBaseMetaTileEntity, getCasing(0), this.META_IntegralCasing, -1, xDir, zDir);
		aContainmentChamberCount += checkContainmentRing(aBaseMetaTileEntity, getCasing(4), this.META_ContainmentChamberCasing, -1, xDir, zDir);
		log("Layer 6 is Valid. Moving to Top Layer.");

		// Top Layer
		aBaseCasingCount += checkEntireLayer(aBaseMetaTileEntity, getCasing(4), META_BaseCasing, 0, xDir, zDir);

		log("Found "+aBaseCasingCount+" "+mCasingName[0]+"s");
		log("Found "+aShieldingCount+" "+mCasingName[4]+"s");
		log("Found "+aPipeCount+" "+mCasingName[1]+"s");
		log("Found "+aContainmentCasingCount+" "+mCasingName[3]+"s");
		log("Found "+aIntegralCasingCount+" "+LangUtils.getLocalizedNameOfBlock(getCasing(0), 6)+"s");
		log("Found "+aContainmentChamberCount+" "+mCasingName[2]+"s");

		// Try mesage player
		String aOwnerName = this.getBaseMetaTileEntity().getOwnerName();
		EntityPlayer aOwner = null;
		if (aOwnerName != null && aOwnerName.length() > 0) {
			aOwner = PlayerUtils.getPlayer(aOwnerName);
		}
		
		if (aShieldingCount != 128) {
			log("Not enough "+mCasingName[4]+"s, require 128.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[4]+"s, require 128.");
			}
			return false;
		}
		if (aPipeCount != 20) {
			log("Not enough "+mCasingName[1]+"s, require 20.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[1]+"s, require 20.");
			}
			return false;
		}
		if (aContainmentCasingCount != 64) {
			log("Not enough "+mCasingName[3]+"s, require 64.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[3]+"s, require 64.");
			}
			return false;
		}
		if (aContainmentChamberCount != 42) {
			log("Not enough "+mCasingName[2]+"s, require 42.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[2]+"s, require 42.");
			}
			return false;
		}
		if (aBaseCasingCount < 140) {
			log("Not enough "+mCasingName[0]+"s, require 140 at a minimum.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+mCasingName[0]+"s, require 140 at a minimum.");
			}
			return false;
		}
		if (aIntegralCasingCount != 48) {
			log("Not enough "+LangUtils.getLocalizedNameOfBlock(getCasing(0), 6)+"s, require 48.");
			if (aOwner != null) {
				PlayerUtils.messagePlayer(aOwner, "Not enough "+LangUtils.getLocalizedNameOfBlock(getCasing(0), 6)+"s, require 48.");
			}
			return false;
		}	
		log("LNR Formed.");
		if (aOwner != null) {
			PlayerUtils.messagePlayer(aOwner, "Large Naquadah Reactor has formed successfully.");
		}
		return true;
	}

	public boolean addNaquadahHatchToMachineInput(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
        	return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
        	return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DataAccess) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDataAccessHatches.add((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity);
        }
        return false;
	}	

	public int checkEntireLayer(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -4; x < 5; x++) {
			for (int z = -4; z < 5; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 4 && z == 4) || (x == -4 && z == -4) || (x == 4 && z == -4) || (x == -4 && z == 4)) {
					continue;
				}		
				// Skip controller
				if (aY == 0 && x == 0 && z == 0) {
					continue;
				}
				
				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, true, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}		
		return aCasingCount;
	}

	public int checkOuterRing(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {		
		int aCasingCount = 0;
		for (int x = -4; x < 5; x++) {
			for (int z = -4; z < 5; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 4 && z == 4) || (x == -4 && z == -4) || (x == 4 && z == -4) || (x == -4 && z == 4)) {
					continue;
				}	

				// If we are on the 5x5 ring, proceed
				if ((x > -4 && x < 4 ) && (z > -4 && z < 4)) {	
					if ((x == 3 && z == 3) || (x == -3 && z == -3) || (x == 3 && z == -3) || (x == -3 && z == 3)) {
						//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);					
					}
					else {
						continue;
					}
				}

				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}
		return aCasingCount;
	}
	public int checkIntegralRing(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -3; x < 4; x++) {
			for (int z = -3; z < 4; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 3 && z == 3) || (x == -3 && z == -3) || (x == 3 && z == -3) || (x == -3 && z == 3)) {
					continue;
				}	

				// If we are on the 5x5 ring, proceed
				if ((x > -3 && x < 3 ) && (z > -3 && z < 3)) {	
					if ((x == 2 && z == 2) || (x == -2 && z == -2) || (x == 2 && z == -2) || (x == -2 && z == 2)) {
						//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);					
					}
					else {
						continue;
					}
				}

				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}
		return aCasingCount;
	}
	
	public int checkPipes(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 1 && z == 1) || (x == -1 && z == -1) || (x == 1 && z == -1) || (x == -1 && z == 1) || (x == 0 && z == 0)) {
					Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
					int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
					if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
						aCasingCount++;
					}				
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
					if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
						log("Pipe has error. Height: "+aY);
						//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
						return 0;
					};
				}
			}	
		}
		return aCasingCount;
	}
	
	public int checkContainmentRing(IGregTechTileEntity aBaseMetaTileEntity, Block aBlock, int aMeta, int aY, int xDir, int zDir) {
		int aCasingCount = 0;
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				int aOffsetX = this.getBaseMetaTileEntity().getXCoord() + x;
				int aOffsetY = this.getBaseMetaTileEntity().getYCoord() + aY;
				int aOffsetZ = this.getBaseMetaTileEntity().getZCoord() + z;				
				//Skip the corners
				if ((x == 2 && z == 2) || (x == -2 && z == -2) || (x == 2 && z == -2) || (x == -2 && z == 2)) {
					continue;
				}

				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + x, aY, zDir + z);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + x, aY, zDir + z);
				if (aCurrentBlock == aBlock && aCurrentMeta == aMeta) {
					aCasingCount++;
				}				
				final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + x, aY, zDir + z);					
				if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, false, aCurrentBlock, aCurrentMeta, aBlock, aMeta)) {
					log("Layer has error. Height: "+aY);
					//this.getBaseMetaTileEntity().getWorld().setBlock(aOffsetX, aOffsetY, aOffsetZ, aBlock, aMeta, 3);
					return 0;
				}
			}	
		}
		return aCasingCount;
	}
        
    /**
     * @param state using bitmask, 1 for IntegratedCircuit, 2 for DataStick, 4 for DataOrb
     */
    private boolean isCorrectDataItem(ItemStack aStack, int state){
    	if ((state & 1) != 0 && ItemList.Circuit_Integrated.isStackEqual(aStack, true, true)) return true;
    	if ((state & 2) != 0 && ItemList.Tool_DataStick.isStackEqual(aStack, false, true)) return true;
    	if ((state & 4) != 0 && ItemList.Tool_DataOrb.isStackEqual(aStack, false, true)) return true;
    	return false;
    }
    
    /**
     * @param state using bitmask, 1 for IntegratedCircuit, 2 for DataStick, 4 for DataOrb
     */
    public ArrayList<ItemStack> getDataItems(int state) {
        ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
        if (GT_Utility.isStackValid(mInventory[1]) && isCorrectDataItem(mInventory[1], state)) {
        	rList.add(mInventory[1]);
        }
        for (GT_MetaTileEntity_Hatch_DataAccess tHatch : mDataAccessHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = 0; i < tHatch.getBaseMetaTileEntity().getSizeInventory(); i++) {
                    if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) != null
                    		&& isCorrectDataItem(tHatch.getBaseMetaTileEntity().getStackInSlot(i), state))
                        rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    public boolean addDataAccessToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DataAccess) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return mDataAccessHatches.add((GT_MetaTileEntity_Hatch_DataAccess) aMetaTileEntity);
        }
        return false;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Assembly Line";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 0;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}
}
