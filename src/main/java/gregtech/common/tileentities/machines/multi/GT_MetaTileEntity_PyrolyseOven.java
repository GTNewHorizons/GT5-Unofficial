package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class GT_MetaTileEntity_PyrolyseOven extends GT_MetaTileEntity_MultiBlockBase {

	private int coilMetaID;
	public static GT_CopiedBlockTexture mTextureULV = new GT_CopiedBlockTexture(Block.getBlockFromItem(ItemList.Casing_ULV.get(1).getItem()), 6, 0,Dyes.MACHINE_METAL.mRGBa);
	
	//private final int CASING_INDEX = 22;
	
    public GT_MetaTileEntity_PyrolyseOven(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PyrolyseOven(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Pyrolyse Oven",
                "Industrial Charcoal producer and Oil from Plants",
                "Size(WxHxD): 5x4x5, Controller (Bottom center)",
                "3x1x3 of Coil Blocks (At the center of the bottom layer)",
                "1x Input Hatch/Bus (Centered 3x1x3 area in Top layer)",
                "1x Output Hatch/Bus (Any bottom layer casing)",
                "1x Maintenance Hatch (Any bottom layer casing)",
                "1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
                "1x Energy Hatch (Any bottom layer casing)",
                "ULV Machine Casings for the rest (60 at least!)",
                "Processing speed scales linearly with Coil tier:",
                "CuNi: 50%, FeAlCr: 100%, Ni4Cr: 150%, Fe50CW: 200%, etc.",
                "EU/t is not affected by Coil tier",
                "Causes " + 20 * getPollutionPerTick(null) + " Pollution per second"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{mTextureULV, new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN)};
        }
        return new ITexture[]{mTextureULV};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "PyrolyseOven.png");
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        ArrayList<FluidStack> tFluidInputs = getStoredFluids();
        for (ItemStack tInput : tInputList) {
            long tVoltage = getMaxInputVoltage();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluidInputs.isEmpty() ? null : new FluidStack[]{tFluidInputs.get(0)}, new ItemStack[]{mInventory[1], tInput});
            if (tRecipe != null) {
                if (tRecipe.isRecipeInputEqual(true, tFluidInputs.isEmpty() ? null : new FluidStack[]{tFluidInputs.get(0)}, new ItemStack[]{tInput, mInventory[1]})) {
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;

                    this.mEUt = tRecipe.mEUt;
                    if (tRecipe.mEUt <= 16) {
                        this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
                        this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
                    } else {
                        this.mEUt = tRecipe.mEUt;
                        this.mMaxProgresstime = tRecipe.mDuration;
                        while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                            this.mEUt *= 4;
                            this.mMaxProgresstime /= 2;
                        }
                    }
                    if (this.mEUt > 0) {
                        this.mEUt = (-this.mEUt);
                    }
                    this.mMaxProgresstime = mMaxProgresstime * 2 / (1 + coilMetaID);
                    this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                    if (tRecipe.mOutputs.length > 0) this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
                    if (tRecipe.mFluidOutputs.length > 0)
                        this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
                    updateSlots();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
        replaceDeprecatedCoils(aBaseMetaTileEntity);
        boolean firstCoil = true;
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                for (int h = 0; h < 4; h++) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                    if ((i != -2 && i != 2) && (j != -2 && j != 2)) {// innerer 3x3 ohne hoehe
                        if (h == 0) {// innen boden (kanthal coils)
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings5) {
                                return false;
                            }
                            int metaID = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                            if (metaID > 6) {
                                return false;
                            } else {
                            	if (firstCoil) {
                            		this.coilMetaID = metaID;
                            		firstCoil = false;
                            	} else if (metaID != this.coilMetaID) {
                            		return false;
                            	}
                            }
                        } else if (h == 3) {// innen decke (ulv casings + input + muffler)
                            if ((!addInputToMachineList(tTileEntity, 22)) && (!addMufflerToMachineList(tTileEntity, 22))) {
                                if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings1) {
                                    return false;
                                }
                                if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
                                    return false;
                                }
                            }
                        } else {// innen air
                            if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
                                return false;
                            }
                        }
                    } else {// Aeusserer 5x5 ohne hoehe
                        if (h == 0) {// aussen boden (controller, output, energy, maintainance, rest ulv casings)
                            if ((!addMaintenanceToMachineList(tTileEntity, 22)) && (!addOutputToMachineList(tTileEntity, 22)) && (!addEnergyInputToMachineList(tTileEntity, 22))) {
                                if ((xDir + i != 0) || (zDir + j != 0)) {//no controller
                                    if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings1) {
                                        return false;
                                    }
                                    if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
                                        return false;
                                    }
                                }
                            }
                        } else {// aussen ueber boden (ulv casings)
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings1) {
                                return false;
                            }
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 0) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
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
        return 10;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PyrolyseOven(this.mName);
    }

    private void replaceDeprecatedCoils(IGregTechTileEntity aBaseMetaTileEntity) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        int tX = aBaseMetaTileEntity.getXCoord() + xDir * 2;
        int tY = (int) aBaseMetaTileEntity.getYCoord();
        int tZ = aBaseMetaTileEntity.getZCoord() + zDir * 2;
        for (int xPos = tX - 1; xPos <= tX + 1; xPos++) {
            for (int zPos = tZ - 1; zPos <= tZ + 1; zPos++) {
                if (aBaseMetaTileEntity.getBlock(xPos, tY, zPos) == GregTech_API.sBlockCasings1 &&
                    aBaseMetaTileEntity.getMetaID(xPos, tY, zPos) == 13)
                {
                    aBaseMetaTileEntity.getWorld().setBlock(xPos, tY, zPos, GregTech_API.sBlockCasings5, 1, 3);
                }
            }
        }
    }
}
