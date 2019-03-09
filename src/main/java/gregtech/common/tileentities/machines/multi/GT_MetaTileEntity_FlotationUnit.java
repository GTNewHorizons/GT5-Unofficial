package gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GT_MetaTileEntity_FlotationUnit extends GT_MetaTileEntity_MultiBlockBase{

	public GT_MetaTileEntity_FlotationUnit(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_FlotationUnit(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_FlotationUnit(this.mName);
    }

    @Override
    public String[] getDescription() {
    	return new String[]{"Flotation Unit",
    			"Has the all recipes as the Ore Washer and Chemical Bath",
				"Does not lose efficiency when overclocked",
                "Size: 3x4x7",
                "Chemically Inert Machine Casing = CIMC",
                "Bottom: Controller/CIMC/Output Hatch(Bus),",
                "Middle: Reinforced Glass, PTFE Pipe Machine Casing, Reinforced Glass",
                "UpMiddle: CIMC/Energy Hatch, Titanium Gearbox Casing (above controller Maintenance Hatch), CIMC/Energy Hatch",
                "Top: CIMC/Input Hatch(Bus), CIMC/Muffler, CIMC/Input Hatch(Bus)",
                "Causes " + 20 * getPollutionPerTick(null) + " Pollution per second"};
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
        	return new ITexture[] {
					Textures.BlockIcons.casingTexturePages[1][48],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE
							: Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR) };
		}
		return new ITexture[] { Textures.BlockIcons.casingTexturePages[1][48] };
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ElectricAirFilter.png");
    }

    @Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<ItemStack> tInputList = getStoredInputs();
		int tInputList_sS = tInputList.size();
		for (int i = 0; i < tInputList_sS - 1; i++) {
			for (int j = i + 1; j < tInputList_sS; j++) {
				if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
					if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
						tInputList.remove(j--);
						tInputList_sS = tInputList.size();
					} else {
						tInputList.remove(i--);
						tInputList_sS = tInputList.size();
						break;
					}
				}
			}
		}
		tInputList.add(mInventory[1]);
		ItemStack[] inputs = tInputList.toArray(new ItemStack[tInputList.size()]);

		ArrayList<FluidStack> tFluidList = getStoredFluids();
		int tFluidList_sS = tFluidList.size();
		for (int i = 0; i < tFluidList_sS - 1; i++) {
			for (int j = i + 1; j < tFluidList_sS; j++) {
				if (GT_Utility.areFluidsEqual(tFluidList.get(i), tFluidList.get(j))) {
					if (tFluidList.get(i).amount >= tFluidList.get(j).amount) {
						tFluidList.remove(j--);
						tFluidList_sS = tFluidList.size();
					} else {
						tFluidList.remove(i--);
						tFluidList_sS = tFluidList.size();
						break;
					}
				}
			}
		}
		FluidStack[] fluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);

		if (inputs.length > 0 || fluids.length > 0) {
			long voltage = getMaxInputVoltage();
			byte tier = (byte) Math.max(1, GT_Utility.getTier(voltage));
			GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sFlotationUnitRecipes.findRecipe(getBaseMetaTileEntity(), false,
					false, gregtech.api.enums.GT_Values.V[tier], fluids, inputs);
			if (recipe != null && recipe.isRecipeInputEqual(true, fluids, inputs)) {
				this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;

				int EUt = recipe.mEUt;
				int maxProgresstime = recipe.mDuration;

				while (EUt <= gregtech.api.enums.GT_Values.V[tier - 1] && maxProgresstime > 2) {
					EUt *= 4;
					maxProgresstime /= 4;
				}
				if (maxProgresstime < 2) {
					maxProgresstime = 2;
					EUt = recipe.mEUt * recipe.mDuration / 2;
				}

				this.mEUt = -EUt;
				this.mMaxProgresstime = maxProgresstime;
				mOutputItems = new ItemStack[recipe.mOutputs.length];
 		        for (int i = 0; i < recipe.mOutputs.length; i++) {
 		            if (getBaseMetaTileEntity().getRandomNumber(10000) < recipe.getOutputChance(i)) {
 		                this.mOutputItems[i] = recipe.getOutput(i);
 		            }
 		        }
				this.mOutputFluids = recipe.mFluidOutputs;
				this.updateSlots();
				return true;
			}
		}
		return false;
	}
	
	@Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        
      //air check
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 4, zDir-1)) {
            return false;
        }
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 4, zDir)) {
            return false;
        }
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 4, zDir+1)) {
            return false;
        }
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 4, zDir+2)) {
            return false;
        }
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 4, zDir+3)) {
            return false;
        }
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 4, zDir+4)) {
            return false;
        }
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 4, zDir+5)) {
            return false;
        }
        //air check done
        //3 check
        mInputHatches.clear();
        mInputBusses.clear();
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 3, zDir-1), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir-1)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir-1)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 3, zDir), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 3, zDir+1), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir+1)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir+1)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 3, zDir+2), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir+2)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir+2)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 3, zDir+3), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir+3)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir+3)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 3, zDir+4), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir+4)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir+4)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 3, zDir+5), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir+5)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir+5)!= 0) {
            return false;
        }
        }
        // ===
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 3, zDir-1), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir-1)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir-1)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 3, zDir), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 3, zDir+1), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir+1)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir+1)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 3, zDir+2), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir+2)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir+2)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 3, zDir+3), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir+3)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir+3)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 3, zDir+4), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir+4)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir+4)!= 0) {
            return false;
        }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 3, zDir+5), 176)){
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir+5)!= GregTech_API.sBlockCasings8) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir+5)!= 0) {
            return false;
        }
        }
        if(mInputHatches.isEmpty()) {
            return false;
        }
        if(mInputBusses.isEmpty()) {
            return false;
        }
        
        // ===
            
            mMufflerHatches.clear();
            if(!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir-1), 176)){
                if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir-1)!= GregTech_API.sBlockCasings8) {
                    return false;
                }
                if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir-1)!= 0) {
                    return false;
                }
            }
            if(!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir), 176)){
                if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir)!= GregTech_API.sBlockCasings8) {
                    return false;
                }
                if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir)!= 0) {
                    return false;
                }
            }
            if(!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir+1), 176)){
                if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir+1)!= GregTech_API.sBlockCasings8) {
                    return false;
                }
                if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir+1)!= 0) {
                    return false;
                }
            }
            if(!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir+2), 176)){
                if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir+2)!= GregTech_API.sBlockCasings8) {
                    return false;
                }
                if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir+2)!= 0) {
                    return false;
                }
            }
            if(!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir+3), 176)){
                if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir+3)!= GregTech_API.sBlockCasings8) {
                    return false;
                }
                if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir+3)!= 0) {
                    return false;
                }
            }
            if(!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir+4), 176)){
                if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir+4)!= GregTech_API.sBlockCasings8) {
                    return false;
                }
                if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir+4)!= 0) {
                    return false;
                }
            }
            if(!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir+5), 176)){
                if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir+5)!= GregTech_API.sBlockCasings8) {
                    return false;
                }
                if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir+5)!= 0) {
                    return false;
                }
            }
            if(mMufflerHatches.isEmpty()) {
                return false;
            }
        //3 check done
            
        //2 check
            mEnergyHatches.clear();
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 2, zDir-1), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir-1)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir-1)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 2, zDir), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 2, zDir+1), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir+1)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir+1)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 2, zDir+2), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir+2)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir+2)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 2, zDir+3), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir+3)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir+3)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 2, zDir+4), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir+4)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir+4)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 2, zDir+5), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir+5)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir+5)!= 0) {
                return false;
            }
            }
            // ===
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 2, zDir-1), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir-1)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir-1)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 2, zDir), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 2, zDir+1), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir+1)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir+1)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 2, zDir+2), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir+2)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir+2)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 2, zDir+3), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir+3)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir+3)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 2, zDir+4), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir+4)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir+4)!= 0) {
                return false;
            }
            }
            if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 2, zDir+5), 176)){
            if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir+5)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir+5)!= 0) {
                return false;
            }
            }
            if(mEnergyHatches.isEmpty()) {
                return false;
            }
            
        // ===
            mMaintenanceHatches.clear();
            if(!addMaintenanceToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 2, zDir-1), 176)){
                    return false;
            }
            if(mMaintenanceHatches.isEmpty()) {
                return false;
            }
            
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir)!= GregTech_API.sBlockCasings2) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir)!= 4) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir+1)!= GregTech_API.sBlockCasings2) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir+1)!= 4) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir+2)!= GregTech_API.sBlockCasings2) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir+2)!= 4) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir+3)!= GregTech_API.sBlockCasings2) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir+3)!= 4) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir+4)!= GregTech_API.sBlockCasings2) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir+4)!= 4) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir+5)!= GregTech_API.sBlockCasings2) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir+5)!= 4) {
                return false;
            }
         
        //2 casing check done
            
        //1 check
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir-1).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir+1).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir+2).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir+3).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir+4).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir+5).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
        // ===
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir-1).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir+1).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir+2).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir+3).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir+4).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
            if (!aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir+5).getUnlocalizedName().equals("blockAlloyGlass")) {
                return false;
            }
            
        // ===
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir-1)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir-1)!= 1) {
                return false;
            }
            
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir)!= 1) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir+1)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir+1)!= 1) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir+2)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir+2)!= 1) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir+3)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir+3)!= 1) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir+4)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir+4)!= 1) {
                return false;
            }
         
            if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir+5)!= GregTech_API.sBlockCasings8) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir+5)!= 1) {
                return false;
            }
         
        //1 casing check done
            
        //bottom casing
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 6; j++) {
                if (xDir + i != 0 || zDir + j != 0) {//sneak exclusion of the controller block
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
                    if (!addOutputToMachineList(tTileEntity, 176)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != GregTech_API.sBlockCasings8) {
                            return false;
                        }
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 0) {
                            return false;
                        }
                    }
                }
            }
        }
        
        //bottom casing done
        return true;
    }

	@Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isClientSide() && aTick % 20L == 0L) {
            //refresh casing on state change
            int Xpos = aBaseMetaTileEntity.getXCoord() + ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
            int Ypos = aBaseMetaTileEntity.getYCoord()+3;
            int Zpos = aBaseMetaTileEntity.getZCoord() + ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
            try {
                aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(Xpos - 1, Ypos, Zpos - 1, Xpos + 1, Ypos, Zpos + 6);
            } catch (Exception ignored) {}
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 40;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}	
}
