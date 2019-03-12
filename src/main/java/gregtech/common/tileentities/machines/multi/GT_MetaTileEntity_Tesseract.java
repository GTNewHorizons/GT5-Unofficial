package gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_MultiMachine;
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

public class GT_MetaTileEntity_Tesseract extends GT_MetaTileEntity_MultiBlockBase{

	public GT_MetaTileEntity_Tesseract(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Tesseract(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Tesseract(this.mName);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
        		"Small WORMHOLE on The Earth!!!",
                "Size(WxHxD): 9x9x9",
                "92 x Robust Naquadah Alloy Machine Casing",
                "24 x Dyson Ring Casing",
                "4 x Fusion Casing MKII",
                "1 x Fusion Coil Block",
                "16 x Core Chamber Casing",
                "30 x Intermix Chamber Casing",
                "1/1 x Input/Output Hatch (UHV or better)",
                "1/1 x Input/Output Bus (UHV or better)",
                "2 x Energy Hatch (UHV or better)",
                "1 x Maintenance Hatch"};
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] sTexture;
        if (aSide == aFacing) {
            sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FUSION1)};
        } else {
            if (!aActive) {
                sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
            } else {
                sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
            }
        }
        return sTexture;
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "Tesseract.png");
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
			GT_Recipe recipe = GT_Recipe.GT_Recipe_Map.sTesseractRecipes.findRecipe(getBaseMetaTileEntity(), false,
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
        
        //8 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir-3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir-3)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir-3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir-3)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir-2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir-2)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir-2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir-2)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir-1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir-1)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir-1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir-1)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir+1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir+1)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir+1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir+1)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir+2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir+2)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir+2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir+2)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 8, zDir+3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 8, zDir+3)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 8, zDir+3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 8, zDir+3)!= 0) {
            return false;
        }
        
        //8 check done
        //7 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 7, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 7, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 7, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 7, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 7, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 7, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 7, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 7, zDir+4)!= 0) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 7, zDir-3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 7, zDir-3)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 7, zDir+3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 7, zDir+3)!= 14) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 7, zDir-3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 7, zDir-3)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 7, zDir+3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 7, zDir+3)!= 14) {
            return false;
        }
        
        //7 check done
        //6 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 6, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 6, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 6, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 6, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 6, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 6, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 6, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 6, zDir+4)!= 0) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 6, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 6, zDir-2)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 6, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 6, zDir+2)!= 14) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 6, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 6, zDir-2)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 6, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 6, zDir+2)!= 14) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir)!= 8) {
            return false;
        }
        mInputHatches.clear();
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 6, zDir+1), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir, 6, zDir+1)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 6, zDir+1)!= 8) {
                return false;
            }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 6, zDir-1), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir, 6, zDir-1)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 6, zDir-1)!= 8) {
                return false;
            }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 6, zDir), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 6, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 6, zDir)!= 8) {
                return false;
            }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 6, zDir), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 6, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 6, zDir)!= 8) {
                return false;
            }
        }
        if(mInputHatches.isEmpty()) {
                return false;
        }
        
        //===
        
        mInputBusses.clear();
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 6, zDir+1), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir, 6, zDir+1)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 6, zDir+1)!= 8) {
                return false;
            }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 6, zDir-1), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir, 6, zDir-1)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 6, zDir-1)!= 8) {
                return false;
            }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+1, 6, zDir), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 6, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 6, zDir)!= 8) {
                return false;
            }
        }
        if(!addInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-1, 6, zDir), 136)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 6, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 6, zDir)!= 8) {
                return false;
            }
        }
        if(mInputBusses.isEmpty()) {
                return false;
        }
        
        //6 check done
        //5 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 5, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 5, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 5, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 5, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 5, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 5, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 5, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 5, zDir+4)!= 0) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 5, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 5, zDir-1)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 5, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 5, zDir+1)!= 14) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 5, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 5, zDir-1)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 5, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 5, zDir+1)!= 14) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 5, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 5, zDir)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 5, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 5, zDir)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 5, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 5, zDir-2)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 5, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 5, zDir+2)!= 8) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 5, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 5, zDir)!= 15) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 5, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 5, zDir)!= 15) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 5, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 5, zDir-1)!= 15) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 5, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 5, zDir+1)!= 15) {
            return false;
        }
        
        //5 check done
        //4 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 4, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 4, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 4, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 4, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 4, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 4, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 4, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 4, zDir+4)!= 0) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 4, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 4, zDir)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 4, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 4, zDir)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 4, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 4, zDir+1)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 4, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 4, zDir+1)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 4, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 4, zDir-1)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 4, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 4, zDir-1)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 4, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 4, zDir-2)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 4, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 4, zDir-2)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 4, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 4, zDir+2)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 4, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 4, zDir+2)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 4, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 4, zDir+2)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 4, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 4, zDir-2)!= 8) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 4, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 4, zDir-1)!= 15) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 4, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 4, zDir+1)!= 15) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 4, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 4, zDir-1)!= 15) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 4, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 4, zDir+1)!= 15) {
            return false;
        }
        
        //4 check done
        //3 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 3, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 3, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 3, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 3, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 3, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 3, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 3, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 3, zDir+4)!= 0) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir-1)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir+1)!= 14) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir-1)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir+1)!= 14) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 3, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 3, zDir)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 3, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 3, zDir)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir-2)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir+2)!= 8) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 3, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 3, zDir)!= 15) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 3, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 3, zDir)!= 15) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir-1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir-1)!= 15) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 3, zDir+1)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 3, zDir+1)!= 15) {
            return false;
        }
        
        //3 check done
        //2 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 2, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 2, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 2, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 2, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 2, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 2, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 2, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 2, zDir+4)!= 0) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 2, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 2, zDir-2)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 2, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 2, zDir+2)!= 14) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 2, zDir-2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 2, zDir-2)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 2, zDir+2)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 2, zDir+2)!= 14) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir)!= 8) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir+1)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir+1)!= 8) {
                return false;
            }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 2, zDir-1)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 2, zDir-1)!= 8) {
                return false;
            }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 2, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 2, zDir)!= 8) {
                return false;
            }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 2, zDir)!= GregTech_API.sBlockCasings5) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 2, zDir)!= 8) {
                return false;
            }
        
        
        //2 check done
        //1 check
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 1, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 1, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 1, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 1, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 1, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 1, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 1, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 1, zDir+4)!= 0) {
            return false;
        }
        
        //===
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 1, zDir-3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 1, zDir-3)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 1, zDir+3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 1, zDir+3)!= 14) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 1, zDir-3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 1, zDir-3)!= 14) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 1, zDir+3)!= GregTech_API.sBlockCasings5) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 1, zDir+3)!= 14) {
            return false;
        }
        
        //==
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir)!= GregTech_API.sBlockCasings4) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir)!= 7) {
            return false;
        }
        
        //==
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 1, zDir)!= GregTech_API.sBlockCasings4) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 1, zDir)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 1, zDir)!= GregTech_API.sBlockCasings4) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 1, zDir)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir-1)!= GregTech_API.sBlockCasings4) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir-1)!= 8) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir, 1, zDir+1)!= GregTech_API.sBlockCasings4) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 1, zDir+1)!= 8) {
            return false;
        }
        
        //1 check done
        //0 casing
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-3, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-3, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-2, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-2, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir-1, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-1, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+1, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+1, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+2, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+2, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+3, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+3, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir-4)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir+4)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir-3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir-3)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir-3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir-3)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir-2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir-2)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir-2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir-2)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir-1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir-1)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir-1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir-1)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir+1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir+1)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir+1)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir+1)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir+2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir+2)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir+2)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir+2)!= 0) {
            return false;
        }
        
        if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir+3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir+3)!= 0) {
            return false;
        }
        if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir+3)!= GregTech_API.sBlockCasings7) {
            return false;
        }
        if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir+3)!= 0) {
            return false;
        }
        
        //==
        
        mEnergyHatches.clear();
        if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir+4, 0, zDir), 192)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir-4, 0, zDir)!= GregTech_API.sBlockCasings7) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir-4, 0, zDir)!= 0) {
                return false;
            }
        }
        if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir-4, 0, zDir), 192)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir+4, 0, zDir)!= GregTech_API.sBlockCasings7) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir+4, 0, zDir)!= 0) {
                return false;
            }
        }
        if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 0, zDir-4), 192)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir, 0, zDir-4)!= GregTech_API.sBlockCasings7) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 0, zDir-4)!= 0) {
                return false;
            }
        }
        if(!addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 0, zDir+4), 192)){
        	if(aBaseMetaTileEntity.getBlockOffset(xDir, 0, zDir+4)!= GregTech_API.sBlockCasings7) {
                return false;
            }
            if(aBaseMetaTileEntity.getMetaIDOffset(xDir, 0, zDir+4)!= 0) {
                return false;
            }
        }
        
        if(mEnergyHatches.isEmpty()) {
                return false;
        }
        
        //0 check done
        //bottom casing
        if (this.mEnergyHatches != null) {
            for (int i = 0; i < this.mEnergyHatches.size(); i++) {
                if (this.mEnergyHatches.get(i).mTier < 8)
                    return false;
            }
        }
        if (this.mInputBusses != null) {
            for (int i = 0; i < this.mInputBusses.size(); i++) {
                if (this.mInputBusses.get(i).mTier < 8)
                    return false;
            }
        }
        if (this.mOutputBusses != null) {
            for (int i = 0; i < this.mOutputBusses.size(); i++) {
                if (this.mOutputBusses.get(i).mTier < 8)
                    return false;
            }
        }
        if (this.mInputHatches != null) {
            for (int i = 0; i < this.mInputHatches.size(); i++) {
                if (this.mInputHatches.get(i).mTier < 8)
                    return false;
            }
        }
        if (this.mOutputHatches != null) {
            for (int i = 0; i < this.mOutputHatches.size(); i++) {
                if (this.mOutputHatches.get(i).mTier < 8)
                    return false;
            }
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
            	if ((xDir + i != 0) || (zDir + j != 0)) {//sneak exclusion of the controller block
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
                    if (!addMaintenanceToMachineList(tTileEntity, 192) && !addOutputToMachineList(tTileEntity, 192)) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != GregTech_API.sBlockCasings7) {
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
}