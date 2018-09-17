package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import java.util.ArrayList;

import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechMetaTileEntity_LargerTurbineBase extends GregtechMeta_MultiBlockBase {

    protected int baseEff = 0;
    protected int optFlow = 0;
    protected double realOptFlow = 0;
    protected int storedFluid = 0;
    protected int counter = 0;
    protected boolean looseFit=false;
    
	private final int mCasingTextureID;
	public static String mCasingName = "Tempered Arc Furnace Casing";

    public GregtechMetaTileEntity_LargerTurbineBase(int aID, String aName, String aNameRegional, int mTAE) {
        super(aID, aName, aNameRegional);
        mCasingTextureID = mTAE;
    }
    public GregtechMetaTileEntity_LargerTurbineBase(String aName, int mTAE) {
        super(aName);
        mCasingTextureID = mTAE;
    }
    
    public abstract int getTAE();

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeTurbine.png");
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {/*
        byte tSide = getBaseMetaTileEntity().getBackFacing();
        if ((getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 1)) && (getBaseMetaTileEntity().getAirAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 2))) {
            int tAirCount = 0;
            for (byte i = -1; i < 2; i = (byte) (i + 1)) {
                for (byte j = -1; j < 2; j = (byte) (j + 1)) {
                    for (byte k = -1; k < 2; k = (byte) (k + 1)) {
                        if (getBaseMetaTileEntity().getAirOffset(i, j, k)) {
                            tAirCount++;
                        }
                    }
                }
            }
            if (tAirCount != 10) {
                return false;
            }
            for (byte i = 2; i < 6; i = (byte) (i + 1)) {
                IGregTechTileEntity tTileEntity;
                if ((null != (tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(i, 2))) &&
                        (tTileEntity.getFrontFacing() == getBaseMetaTileEntity().getFrontFacing()) && (tTileEntity.getMetaTileEntity() != null) &&
                        ((tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntity_LargerTurbineBase))) {
                    return false;
                }
            }
            int tX = getBaseMetaTileEntity().getXCoord();
            int tY = getBaseMetaTileEntity().getYCoord();
            int tZ = getBaseMetaTileEntity().getZCoord();
            for (byte i = -1; i < 2; i = (byte) (i + 1)) {
                for (byte j = -1; j < 2; j = (byte) (j + 1)) {
                    if ((i != 0) || (j != 0)) {
                        for (byte k = 0; k < 4; k = (byte) (k + 1)) {
                            if (((i == 0) || (j == 0)) && ((k == 1) || (k == 2))) {
                                if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingMeta()) {
                                } else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)), TAE.getIndexFromPage(3, 6))) {
                                    return false;
                                }
                            } else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingMeta()) {
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
            this.mDynamoHatches.clear();
            IGregTechTileEntity tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance(getBaseMetaTileEntity().getBackFacing(), 3);
            if ((tTileEntity != null) && (tTileEntity.getMetaTileEntity() != null)) {
                if ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Hatch_Dynamo)) {
                    this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo) tTileEntity.getMetaTileEntity());
                    ((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).updateTexture(getCasingTextureIndex());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    */
    	
    return checkMachine2(aBaseMetaTileEntity, aStack);
    
    }
    
    
    public boolean checkMachine2(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		int x = 1;
		int z = 1;
		int depth = 1;
		//9 high
		//7x7
		
		//Get Height
		for (int i=0;i<=9;i++) {
			Block tBlock = aBaseMetaTileEntity.getBlockOffset(i, 0, 0);
			int tMeta = aBaseMetaTileEntity.getMetaIDOffset(i, 0, 0);			
			if (isValidCasingBlock(tBlock, tMeta)) {
				depth--;
			}
			else {
				break;
			}			
		}

        this.mDynamoHatches.clear();
		for (int i=0;i>=depth;i--) {
			if (!getLayer(i)) {
				return false;
			}
		}			
			
		if (mMaintenanceHatches.size() != 1 || mDynamoHatches.size() < 1) {
			Logger.INFO("Bad Hatches");
			return false;
		}

		Logger.INFO("Built Structure");
		return true;
	}
    
    
    public boolean getLayer(int aY) {
    	if (aY == 0 || aY == 2 || aY == 3 || aY == 5 || aY == 6 || aY == 8) {
    		return checkNormalLayer(aY);
    	}
    	else {
    		return checkTurbineLayer(aY);
    	}
    }
    
    public boolean checkNormalLayer(int aY) {
		Block tBlock;
		int tMeta;
    	for (int x = -3; x <= 3; x++) {
    		for (int z = -3; z <= 3; z++) {
    			tBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
    			tMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);    			
    			if (isValidCasingBlock(tBlock, tMeta)) {
    				continue;
    			}
    			else {
    				
    				if (x == 0 || z == 0) {
						IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
    					if (this.addToMachineList(tTileEntity, this.mCasingTextureID)) {
    						continue;
    					}
    					else {
    						return false;
    					}
    				}    				
    				return false;
    			}    			
        	}	
    	}    	
    	return true;
    }
    

    public boolean checkTurbineLayer(int aY) {
    	if (!checkTurbineLayerX(aY)) {
    		return checkTurbineLayerZ(aY);
    	}
    	else {
    		return true;
    	}
    }
    
    public boolean checkTurbineLayerX(int aY) {
		Block tBlock;
		int tMeta;
    	for (int x = -3; x <= 3; x++) {
    		for (int z = -3; z <= 3; z++) {
    			tBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
    			tMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);   

				if (x == 0 || z == 0) {
					IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
					if (this.addToMachineList(tTileEntity, this.mCasingTextureID)) {
						continue;
					}    	
				}
    			
    			if (x == -2 || x == 2) {
    				if (isValidTurbineBlock(tBlock, tMeta)) {
    					continue;
    				}
    				else {
    					return false;
    				}
    			}
    			else {
    				if (isValidCasingBlock(tBlock, tMeta)) {
        				continue;
        			}
        			else {
        				return false;
        			}  
    			}    			  			
        	}	
    	}    	
    	return true;
    }
    
    public boolean checkTurbineLayerZ(int aY) {
		Block tBlock;
		int tMeta;
    	for (int x = -3; x <= 3; x++) {
    		for (int z = -3; z <= 3; z++) {
    			tBlock = this.getBaseMetaTileEntity().getBlockOffset(x, aY, z);
    			tMeta = this.getBaseMetaTileEntity().getMetaIDOffset(x, aY, z);
    			
    			if (x == 0 || z == 0) {
					IGregTechTileEntity tTileEntity = this.getBaseMetaTileEntity().getIGregTechTileEntityOffset(x, aY, z);    					
					if (this.addToMachineList(tTileEntity, this.mCasingTextureID)) {
						continue;
					}    	
				}
    			
    			if (z == -2 || z == 2) {
    				if (isValidTurbineBlock(tBlock, tMeta)) {
    					continue;
    				}
    				else {
    					return false;
    				}
    			}
    			else {
    				if (isValidCasingBlock(tBlock, tMeta)) {
        				continue;
        			}
        			else {
        				return false;
        			}  
    			}    			  			
        	}	
    	}    	
    	return true;
    }   

	public boolean isValidCasingBlock(Block aBlock, int aMeta) {
		if (aBlock == getCasingBlock() && aMeta == getCasingMeta()) {
			return true;
		}
		Logger.INFO("Found "+(aBlock != null ? aBlock.getLocalizedName() : "Air") + "With Meta "+aMeta);
		return false;
	}
	
	public boolean isValidTurbineBlock(Block aBlock, int aMeta) {
		if (aBlock == getCasingBlock() && aMeta == getCasingMetaTurbine()) {
			return true;
		}
		Logger.INFO("Found "+(aBlock != null ? aBlock.getLocalizedName() : "Air") + "With Meta "+aMeta);
		return false;
	}

    public Block getCasingBlock() {
    	return ModBlocks.blockCasings4Misc;
    }

    public abstract byte getCasingMeta();

    public byte getCasingMetaTurbine() {
    	return 7;
    }

    public abstract byte getCasingTextureIndex();

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
    	if((counter&7)==0 && (aStack==null || !(aStack.getItem() instanceof GT_MetaGenerated_Tool)  || aStack.getItemDamage() < 170 || aStack.getItemDamage() >179)) {
    	    stopMachine();
    	    return false;
        }
        ArrayList<FluidStack> tFluids = getStoredFluids();
        if (tFluids.size() > 0) {
            if (baseEff == 0 || optFlow == 0 || counter >= 512 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()
                    || this.getBaseMetaTileEntity().hasInventoryBeenModified()) {
                counter = 0;
                baseEff = GT_Utility.safeInt((long)((5F + ((GT_MetaGenerated_Tool) aStack.getItem()).getToolCombatDamage(aStack)) * 1000F));
                optFlow = GT_Utility.safeInt((long)Math.max(Float.MIN_NORMAL,
                        ((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack).getSpeedMultiplier()
                                * ((GT_MetaGenerated_Tool) aStack.getItem()).getPrimaryMaterial(aStack).mToolSpeed
                                * 50));
                if(optFlow<=0 || baseEff<=0){
                    stopMachine();//in case the turbine got removed
                    return false;
                }
            } else {
                counter++;
            }
        }

        int newPower = fluidIntoPower(tFluids, optFlow, baseEff);  // How much the turbine should be producing with this flow
        int difference = newPower - this.mEUt; // difference between current output and new output

        // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the difference in power level (per tick)
        // This is how much the turbine can actually change during this tick
        int maxChangeAllowed = Math.max(10, GT_Utility.safeInt((long)Math.abs(difference)/100));

        if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed change
            int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
            this.mEUt += change; // Apply the change
        } else
            this.mEUt = newPower;

        if (this.mEUt <= 0) {
            //stopMachine();
            this.mEUt=0;
            this.mEfficiency=0;
            return false;
        } else {
            this.mMaxProgresstime = 1;
            this.mEfficiencyIncrease = 10;
            if(this.mDynamoHatches.size()>0){
                for(GT_MetaTileEntity_Hatch dynamo:mDynamoHatches)
            	    if(isValidMetaTileEntity(dynamo) && dynamo.maxEUOutput() < mEUt)
            	        explodeMultiblock();
            }
            return true;
        }
    }

    abstract int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow, int aBaseEff);

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return 0;
        }
        if (aStack.getItem() instanceof GT_MetaGenerated_Tool_01) {
            return 10000;
        }
        return 0;
    }
    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    @Override
    public String[] getExtraInfoData() {
        int mPollutionReduction=0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                mPollutionReduction=Math.max(tHatch.calculatePollutionReduction(100),mPollutionReduction);
            }
        }

        String tRunning = mMaxProgresstime>0 ?

                EnumChatFormatting.GREEN+StatCollector.translateToLocal("GT5U.turbine.running.true")+EnumChatFormatting.RESET :
                EnumChatFormatting.RED+StatCollector.translateToLocal("GT5U.turbine.running.false")+EnumChatFormatting.RESET;
        String tMaintainance = getIdealStatus() == getRepairStatus() ?
                EnumChatFormatting.GREEN+StatCollector.translateToLocal("GT5U.turbine.maintenance.false")+EnumChatFormatting.RESET :
                EnumChatFormatting.RED+StatCollector.translateToLocal("GT5U.turbine.maintenance.true")+EnumChatFormatting.RESET ;
        int tDura = 0;

        if (mInventory[1] != null && mInventory[1].getItem() instanceof GT_MetaGenerated_Tool_01) {
            tDura = GT_Utility.safeInt((long)(100.0f / GT_MetaGenerated_Tool.getToolMaxDamage(mInventory[1]) * (GT_MetaGenerated_Tool.getToolDamage(mInventory[1]))+1));
        }

        long storedEnergy=0;
        long maxEnergy=0;
        for(GT_MetaTileEntity_Hatch_Dynamo tHatch : mDynamoHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy+=tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy+=tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        String[] ret = new String[]{
                // 8 Lines available for information panels
                tRunning + ": " + EnumChatFormatting.RED+mEUt+EnumChatFormatting.RESET+" EU/t", /* 1 */
                tMaintainance, /* 2 */
                StatCollector.translateToLocal("GT5U.turbine.efficiency")+": "+EnumChatFormatting.YELLOW+(mEfficiency/100F)+EnumChatFormatting.RESET+"%", /* 2 */
                StatCollector.translateToLocal("GT5U.multiblock.energy")+": " + EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET +" EU / "+ /* 3 */
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET +" EU", 
                StatCollector.translateToLocal("GT5U.turbine.flow")+": "+EnumChatFormatting.YELLOW+GT_Utility.safeInt((long)realOptFlow)+EnumChatFormatting.RESET+" L/t" + /* 4 */
                        EnumChatFormatting.YELLOW+" ("+(looseFit?StatCollector.translateToLocal("GT5U.turbine.loose"):StatCollector.translateToLocal("GT5U.turbine.tight"))+")", /* 5 */
                StatCollector.translateToLocal("GT5U.turbine.fuel")+": "+EnumChatFormatting.GOLD+storedFluid+EnumChatFormatting.RESET+"L", /* 6 */
                StatCollector.translateToLocal("GT5U.turbine.dmg")+": "+EnumChatFormatting.RED+Integer.toString(tDura)+EnumChatFormatting.RESET+"%", /* 7 */
                StatCollector.translateToLocal("GT5U.multiblock.pollution")+": "+ EnumChatFormatting.GREEN + mPollutionReduction+ EnumChatFormatting.RESET+" %" /* 8 */
        };
        if (!this.getClass().getName().contains("Steam"))
        	ret[4]=StatCollector.translateToLocal("GT5U.turbine.flow")+": "+EnumChatFormatting.YELLOW+GT_Utility.safeInt((long)realOptFlow)+EnumChatFormatting.RESET+" L/t";
        return ret;
       
        	
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

}
