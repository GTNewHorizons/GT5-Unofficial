package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

public class GregtechMetaTileEntity_Adv_DistillationTower extends GregtechMeta_MultiBlockBase {
	
	private static final int CASING_INDEX = 49;
	
    private short mControllerY = 0;
    
    private byte mMode = 0;

    public GregtechMetaTileEntity_Adv_DistillationTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_Adv_DistillationTower(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_DistillationTower(this.mName);
    }

    public String[] getTooltip() {
		String s = "Max parallel dictated by tower tier and mode";
		String s1 = "DTower Mode: T1=4, T2=12";
		String s2 = "Distilery Mode: Tower Tier * (4*InputTier)";
        return new String[]{
                "Controller Block for the Advanced Distillation Tower",
                "T1 constructed identical to standard DT",
                "T2 is currently disabled.",
                "T2 is not variable height",
                "Size(WxHxD): 3x26x3",
                "Controller (Front bottom)",
                "1x Input Hatch (Any bottom layer casing)",
                "24x Output Hatch (One per layer except bottom/top layer)",
                "1x Output Bus (Any bottom layer casing)",
                "1x Maintenance Hatch (Any casing)",
                "1x Energy Hatch (Any casing)",
                "Integral Framework I's for the rest",
                s,
                s1,
                s2};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_INDEX], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_INDEX]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "DistillationTower.png");
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return mMode == 0 ? GT_Recipe.GT_Recipe_Map.sDistillationRecipes : GT_Recipe.GT_Recipe_Map.sDistilleryRecipes;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }
    
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
		return this.mMode == 1 ? 12 : 24;
    }
    
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setByte("mMode", mMode);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mMode = aNBT.getByte("mMode");
		super.loadNBTData(aNBT);
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(203));
	}

	@Override
	public void startProcess() {
		this.sendLoopStart((byte) 1);
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mMode++;		
		if (mMode > 1){
			mMode = 0;
			PlayerUtils.messagePlayer(aPlayer, "Now running in Distillation Tower Mode.");
		}
		else {
			PlayerUtils.messagePlayer(aPlayer, "Now running in Distillery Mode.");
		}		
	}

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
    
    @Override
    public boolean addOutput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        FluidStack tLiquid = aLiquid.copy();
        for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
            if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid) ? tHatch.outputsSteam() : tHatch.outputsLiquids()) {
            	if (tHatch.getBaseMetaTileEntity().getYCoord() == this.mControllerY + 1) {
            		int tAmount = tHatch.fill(tLiquid, false);
                	if (tAmount >= tLiquid.amount) {
                    	return tHatch.fill(tLiquid, true) >= tLiquid.amount;
                	} else if (tAmount > 0) {
                    	tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
                	}
            	}
            }
        }
        return false;
    }

    @Override
    protected void addFluidOutputs(FluidStack[] mOutputFluids2) {
        for (int i = 0; i < mOutputFluids2.length; i++) {
            if (mOutputHatches.size() > i && mOutputHatches.get(i) != null && mOutputFluids2[i] != null && isValidMetaTileEntity(mOutputHatches.get(i))) {
            	if (mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord() == this.mControllerY + 1 + i) {
            		mOutputHatches.get(i).fill(mOutputFluids2[i], true);
            	}
            }
        }

    }

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "DistillationTower";
	}

	@Override
	public String getMachineType() {
		return "Distillery, Distillation Tower";
	}
	
	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
			ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
			tBus.mRecipeMap = getRecipeMap();
			if (isValidMetaTileEntity(tBus)) {
				for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
						tBusItems.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
				}
			}			
			ItemStack[] inputs = new ItemStack[tBusItems.size()];
			int slot = 0;
			for (ItemStack g : tBusItems) {
				inputs[slot++] = g;
			}			
			if (inputs.length > 0) {				
				int para = (4* GT_Utility.getTier(this.getMaxInputVoltage()));
				log("Recipe. ["+inputs.length+"]["+para+"]");				
				if (checkRecipeGeneric(inputs, new FluidStack[]{}, para, 100, 250, 10000)) {
					log("Recipe 2.");
					return true;
				}
			}			

		}
		return false;
	}

	@Override
	public int getMaxParallelRecipes() {		
		if (this.mMode == 0) {
			return getTierOfTower() == 1 ? 4 : getTierOfTower() == 2 ? 12 : 0;			
		}
		else if (this.mMode == 1) {
			return getTierOfTower() * (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
		}		
		return 0;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 15;
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int aTier = getTierOfTower();
		if (aTier > 0) {
			if (aTier == 1) {
				return checkTierOneTower(aBaseMetaTileEntity, aStack);
			}
			else if (aTier == 2) {
				return checkTierTwoTower(aBaseMetaTileEntity, aStack);				
			}
		}
		return false;
	}
	
	private int getTierOfTower() {
		return 1;
	}
	
	private boolean checkTierOneTower(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
    	mControllerY = aBaseMetaTileEntity.getYCoord();
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int y = 0; //height
        int casingAmount = 0;
        boolean reachedTop = false;

        for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
			for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
				if (x != 0 || z != 0) {
					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
					if (!addInputToMachineList(tileEntity, CASING_INDEX) 
							&& !addOutputToMachineList(tileEntity, CASING_INDEX) 
							&& !addMaintenanceToMachineList(tileEntity, CASING_INDEX) 
							&& !addEnergyInputToMachineList(tileEntity, CASING_INDEX)) {
						if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
							casingAmount++;
						} else {
							return false;
						}        						
					}
				}
			}
		}
        y++;
        
        while (y < 12 && !reachedTop) {
       		for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
       			for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
   					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
   					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
   					if (aBaseMetaTileEntity.getAirOffset(x, y, z)) {
   						if (x != xDir || z != zDir) {
   							return false;
   						}
   					} else {
   						if (x == xDir && z == zDir) {
       						reachedTop = true;
       					}
   						if (!addOutputToMachineList(tileEntity, CASING_INDEX) 
   								&& !addMaintenanceToMachineList(tileEntity, CASING_INDEX) 
   								&& !addEnergyInputToMachineList(tileEntity, CASING_INDEX)) {
   							if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
       							casingAmount++;
       						} else {
       							return false;
       						}
   						}
   					}
       			}
       		}
        	y++;
        }        
        return casingAmount >= 7 * y - 5 && y >= 3 && y <= 12 && reachedTop;    
	}
	
	private boolean checkTierTwoTower(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
    	mControllerY = aBaseMetaTileEntity.getYCoord();
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int y = 0; //height
        int casingAmount = 0;
        boolean reachedTop = false;

        for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
			for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
				if (x != 0 || z != 0) {
					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
					if (!addInputToMachineList(tileEntity, CASING_INDEX) 
							&& !addOutputToMachineList(tileEntity, CASING_INDEX) 
							&& !addMaintenanceToMachineList(tileEntity, CASING_INDEX) 
							&& !addEnergyInputToMachineList(tileEntity, CASING_INDEX)) {
						if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
							casingAmount++;
						} else {
							return false;
						}        						
					}
				}
			}
		}
        y++;
        
        while (y < 12 && !reachedTop) {
       		for (int x = xDir - 1; x <= xDir + 1; x++) { //x=width
       			for (int z = zDir - 1; z <= zDir + 1; z++) { //z=depth
   					IGregTechTileEntity tileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(x, y, z);
   					Block block = aBaseMetaTileEntity.getBlockOffset(x, y, z);
   					if (aBaseMetaTileEntity.getAirOffset(x, y, z)) {
   						if (x != xDir || z != zDir) {
   							return false;
   						}
   					} else {
   						if (x == xDir && z == zDir) {
       						reachedTop = true;
       					}
   						if (!addOutputToMachineList(tileEntity, CASING_INDEX) 
   								&& !addMaintenanceToMachineList(tileEntity, CASING_INDEX) 
   								&& !addEnergyInputToMachineList(tileEntity, CASING_INDEX)) {
   							if (block == GregTech_API.sBlockCasings4 && aBaseMetaTileEntity.getMetaIDOffset(x, y, z) == 1) {
       							casingAmount++;
       						} else {
       							return false;
       						}
   						}
   					}
       			}
       		}
        	y++;
        }        
        return casingAmount >= 7 * y - 5 && y >= 3 && y <= 12 && reachedTop;    
	}
    
}