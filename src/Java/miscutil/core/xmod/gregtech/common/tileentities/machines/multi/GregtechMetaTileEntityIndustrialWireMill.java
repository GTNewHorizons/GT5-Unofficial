package miscutil.core.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;

import miscutil.core.block.ModBlocks;
import miscutil.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntityIndustrialWireMill
        extends GT_MetaTileEntity_MultiBlockBase {
    public GregtechMetaTileEntityIndustrialWireMill(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntityIndustrialWireMill(String aName) {
        super(aName);
    }

    @Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityIndustrialWireMill(this.mName);
    }

    @Override
	public String[] getDescription() {
        return new String[]{
        		"Controller Block for the Vacuum Freezer",
        		"Size: 3x3x3 (Hollow)",
        		"Controller (front centered)",
        		"1x Input (anywhere)",
        		"1x Output (anywhere)",
        		"1x Energy Hatch (anywhere)",
        		"1x Maintenance Hatch (anywhere)",
        		"Frost Proof Casings for the rest (16 at least!)",
        		"",        		
        		"Controller Block for the Large Plasma Generator",
                "Size: 3x4x3 (Hollow)", "Controller (front centered)",
                "1x Input Hatch (side centered)",
                "1x Dynamo Hatch (back centered)",
                "1x Maintenance Hatch (side centered)",
                "Turbine Casings for the rest (24 at least!)",
                "Needs a Turbine Item (inside controller GUI)"
        		};
    }

    @Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17]};
    }

    @Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "LargeTurbine.png");
    }

    @Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sWiremillRecipes;
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
        for (ItemStack tInput : tInputList) {
            long tVoltage = getMaxInputVoltage();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sWiremillRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], null, new ItemStack[]{tInput});
            if (tRecipe != null) {
                if (tRecipe.isRecipeInputEqual(true, null, new ItemStack[]{tInput})) {
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
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
                    this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                    this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
                    updateSlots();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
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
                        ((tTileEntity.getMetaTileEntity() instanceof GregtechMetaTileEntityIndustrialWireMill))) {
                	Utils.LOG_INFO("False 1");
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
                                } else if (!addToMachineList(getBaseMetaTileEntity().getIGregTechTileEntity(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)))) {
                                    Utils.LOG_INFO("False 2");
                                	return false;
                                }
                            } else if (getBaseMetaTileEntity().getBlock(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingBlock() && getBaseMetaTileEntity().getMetaID(tX + (tSide == 5 ? k : tSide == 4 ? -k : i), tY + j, tZ + (tSide == 2 ? -k : tSide == 3 ? k : i)) == getCasingMeta()) {
                            } else {
                            	Utils.LOG_INFO("False 3");
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
                    ((GT_MetaTileEntity_Hatch) tTileEntity.getMetaTileEntity()).mMachineBlock = getCasingTextureIndex();
                } else {
                	Utils.LOG_INFO("False 4");
                	return false;
                }
            }
        } else {
        	Utils.LOG_INFO("False 5");
        	return false;
        }
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
    
    public Block getCasingBlock() {
        return ModBlocks.blockCasingsMisc;
    }

    
    public byte getCasingMeta() {
        return 9;
    }

    
    public byte getCasingTextureIndex() {
        return 46;
    }
    
    private boolean addToMachineList(IGregTechTileEntity tTileEntity) {
        return ((addMaintenanceToMachineList(tTileEntity, getCasingTextureIndex())) || (addInputToMachineList(tTileEntity, getCasingTextureIndex())) || (addOutputToMachineList(tTileEntity, getCasingTextureIndex())) || (addMufflerToMachineList(tTileEntity, getCasingTextureIndex())));
    }
}
