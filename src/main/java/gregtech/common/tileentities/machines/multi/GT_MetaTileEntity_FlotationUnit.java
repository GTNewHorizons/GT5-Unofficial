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
        return new String[]{
        		"Controller block for the Flotation Unit",
				"Has the all recipes as the Ore Washer and Chemical Bath",
				"Does not lose efficiency when overclocked",
				"Size(WxHxD): 3x4x3 (Hollow), Controller (Front middle bottom)",
				"8x Titanium Pipe Casing (Two middle Layers, corners)",
                "8x Nichrome Coil (Two middle Layers, sides)",
                "Stable Titanium Machine Casings for the rest",
				"1x Input Bus/Hatch (Bottom casing)",
				"1x Output Bus/Hatch (Bottom casing)",
				"1x Maintenance Hatch (Bottom casing)",
				"1x Muffler Hatch (Only Top casing)",
				"1x Energy Hatch (Botton casing)",
				"Causes " + 20 * getPollutionPerTick(null) + " Pollution per second"};
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[50], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_DIESEL_ENGINE)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[50]};
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
        int one = 1;
        int two = 2;

      //air check and top casing check
        if (!aBaseMetaTileEntity.getAirOffset(xDir, one, zDir) || !aBaseMetaTileEntity.getAirOffset(xDir, two, zDir)) {//check air inside
            return false;
        }
        for (int i = -one; i < two; i++) {
            for (int j = -one; j < two; j++) {
                if (xDir + i != 0 || zDir + j != 0) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j);
                    if (!addMufflerToMachineList(tTileEntity, 50)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j) != GregTech_API.sBlockCasings4) {
                            return false;
                        }
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j) != 2) {
                            return false;
                        }
                    }
                }
            }
        }
        //air check and top casing check done
        //coil check
        if(aBaseMetaTileEntity.getBlockOffset(one+xDir, one, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(one+xDir, one, zDir)!= 2) {
                return false;
            }
        if(aBaseMetaTileEntity.getBlockOffset(one+xDir, two, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(one+xDir, two, zDir)!= 2) {
                return false;
            }

        if(aBaseMetaTileEntity.getBlockOffset(xDir-one, one, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-one, one, zDir)!= 2) {
                return false;
            }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-one, two, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-one, two, zDir)!= 2) {
                return false;
            }

        if(aBaseMetaTileEntity.getBlockOffset(xDir, one, one+zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, one, one+zDir)!= 2) {
                return false;
            }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, two, one+zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, two, one+zDir)!= 2) {
                return false;
            }

        if(aBaseMetaTileEntity.getBlockOffset(xDir, one, zDir-one)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, one, zDir-one)!= 2) {
                return false;
            }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, two, zDir-one)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, two, zDir-one)!= 2) {
                return false;
            }
        //coil check done
        //pipe casing check
        if(aBaseMetaTileEntity.getBlockOffset(one+xDir, one, one+zDir)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(one+xDir, one, one+zDir)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(one+xDir, two, one+zDir)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(one+xDir, two, one+zDir)!= 14) {
            return false;
        }

        if(aBaseMetaTileEntity.getBlockOffset(xDir-one, one, one+zDir)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-one, one, one+zDir)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-one, two, one+zDir)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-one, two, one+zDir)!= 14) {
            return false;
        }

        if(aBaseMetaTileEntity.getBlockOffset(one+xDir, one, zDir-one)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(one+xDir, one, zDir-one)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(one+xDir, two, zDir-one)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(one+xDir, two, zDir-one)!= 14) {
            return false;
        }

        if(aBaseMetaTileEntity.getBlockOffset(xDir-one, one, zDir-one)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-one, one, zDir-one)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-one, two, zDir-one)!= GregTech_API.sBlockCasings2) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-one, two, zDir-one)!= 14) {
            return false;
        }
        //pipe casing check done
        //bottom casing
        for (int i = -one; i < two; i++) {
            for (int j = -one; j < two; j++) {
                if (xDir + i != 0 || zDir + j != 0) {//sneak exclusion of the controller block
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
                    if (!addMaintenanceToMachineList(tTileEntity, 50) && !addInputToMachineList(tTileEntity, 50) && !addOutputToMachineList(tTileEntity, 50) && !addMufflerToMachineList(tTileEntity, 50) && !addEnergyInputToMachineList(tTileEntity, 50)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != GregTech_API.sBlockCasings4) {
                            return false;
                        }
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 2) {
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
                aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(Xpos - 1, Ypos, Zpos - 1, Xpos + 1, Ypos, Zpos + 1);
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
