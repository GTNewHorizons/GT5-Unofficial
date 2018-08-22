package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Cleanroom extends GT_MetaTileEntity_MultiBlockBase {
    private int mHeatingCapacity = 0;

    public GT_MetaTileEntity_Cleanroom(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Cleanroom(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Cleanroom(this.mName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Cleanroom",
                "Min(WxHxD): 3x4x3 (Hollow), Max(WxHxD): 15x15x15 (Hollow)",
                "Controller (Top center)",
                "Top besides contoller and edges Filter Machine Casings",
                "1 Reinforced Door (keep closed for 100% efficency",
                "1x LV+ Energy Hatch(40EU/t startup, 4EU/t keepup), 1x Maintainance Hatch",
                "Up to 10 Machine Hulls to transfer Items & Energy through walls",
                "Remaining Blocks Plascrete"};
    }

    public boolean checkRecipe(ItemStack aStack) {
        this.mEfficiencyIncrease = 100;
        this.mMaxProgresstime = 100;
        this.mEUt = -4;
        return true;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int x = 1;
        int z = 1;
        int y = 1;
        int mDoorCount = 0;
        int mHullCount = 0;
        int mPlascreteCount = 0;
        boolean doorState = false;
        mUpdate = 100;
        for (int i = 1; i < 8; i++) {
            Block tBlock = aBaseMetaTileEntity.getBlockOffset(i, 0, 0);
            int tMeta = aBaseMetaTileEntity.getMetaIDOffset(i, 0, 0);
            if (tBlock != GregTech_API.sBlockCasings3 || tMeta != 11) {
                if (tBlock == GregTech_API.sBlockReinforced || tMeta == 2) {
                    x = i;
                    z = i;
                    break;
                } else {
                    return false;
                }
            }
        }
        for (int i = -1; i > -16; i--) {
            Block tBlock = aBaseMetaTileEntity.getBlockOffset(x, i, z);
            int tMeta = aBaseMetaTileEntity.getMetaIDOffset(x, i, z);
            if (tBlock != GregTech_API.sBlockReinforced || tMeta != 2) {
                y = i + 1;
                break;
            }
        }
        if (y > -2) {
            return false;
        }
        for (int dX = -x; dX <= x; dX++) {
            for (int dZ = -z; dZ <= z; dZ++) {
                for (int dY = 0; dY >= y; dY--) {
                    if (dX == -x || dX == x || dY == -y || dY == y || dZ == -z || dZ == z) {
                        Block tBlock = aBaseMetaTileEntity.getBlockOffset(dX, dY, dZ);
                        int tMeta = aBaseMetaTileEntity.getMetaIDOffset(dX, dY, dZ);
                        if (y == 0) {
                            if (dX == -x || dX == x || dZ == -z || dZ == z) {
                                if (tBlock != GregTech_API.sBlockReinforced || tMeta != 2) {
                                    return false;
                                }
                            } else if (dX == 0 && dZ == 0) {
                            } else {
                                if (tBlock != GregTech_API.sBlockCasings3 || tMeta != 11) {
                                    return false;
                                }
                            }
                        } else if (tBlock == GregTech_API.sBlockReinforced && tMeta == 2) {
                            mPlascreteCount++;
                        } else {
                            IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dX, dY, dZ);
                            if ((!addMaintenanceToMachineList(tTileEntity, 82)) && (!addEnergyInputToMachineList(tTileEntity, 82))) {
                                if (tBlock instanceof ic2.core.block.BlockIC2Door) {
                                    if ((tMeta & 8) == 0) {
                                        doorState = (Math.abs(dX) > Math.abs(dZ) == ((tMeta & 1) != 0)) != ((tMeta & 4) != 0);
                                    }
                                    mDoorCount++;
                                } else {
                                    if (tTileEntity == null) {
                                        return false;
                                    }
                                    IMetaTileEntity aMetaTileEntity = tTileEntity.getMetaTileEntity();
                                    if (aMetaTileEntity == null) {
                                        return false;
                                    }
                                    if (aMetaTileEntity instanceof GT_MetaTileEntity_BasicHull) {
                                        mHullCount++;
                                    } else {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (mMaintenanceHatches.size() != 1 || mEnergyHatches.size() != 1 || mDoorCount != 2 || mHullCount > 10) {
            return false;
        }
        for (int dX = -x + 1; dX <= x - 1; dX++) {
            for (int dZ = -z + 1; dZ <= z - 1; dZ++) {
                for (int dY = -1; dY >= y + 1; dY--) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dX, dY, dZ);
                    if (tTileEntity != null) {
                        IMetaTileEntity aMetaTileEntity = tTileEntity.getMetaTileEntity();
                        if (aMetaTileEntity != null && aMetaTileEntity instanceof GT_MetaTileEntity_BasicMachine_GT_Recipe) {
                            ((GT_MetaTileEntity_BasicMachine_GT_Recipe) aMetaTileEntity).mCleanroom = this;
                        }
                    }
                }
            }
        }

        if (doorState) {
            mEfficiency = Math.max(0, mEfficiency - 200);
        }
        for(byte i = 0 ; i<6 ; i++){
        	byte t = (byte) Math.max(1, (byte)(15/(10000f / mEfficiency)));
        aBaseMetaTileEntity.setInternalOutputRedstoneSignal(i, t);
        }
        
        return true;
    }
    
    @Override
    public boolean allowGeneralRedstoneOutput(){
    	return true;
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == 0 || aSide == 1) {
            return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.BLOCK_PLASCRETE),
                    new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE : Textures.BlockIcons.OVERLAY_TOP_CLEANROOM)};

        }
        return new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.BLOCK_PLASCRETE)};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MultiblockDisplay.png");
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

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public int getAmountOfOutputs() {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
