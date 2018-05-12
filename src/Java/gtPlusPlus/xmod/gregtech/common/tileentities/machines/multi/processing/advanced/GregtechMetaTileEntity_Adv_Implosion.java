package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class GregtechMetaTileEntity_Adv_Implosion
        extends GregtechMeta_MultiBlockBase {
    public GregtechMetaTileEntity_Adv_Implosion(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_Adv_Implosion(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_Implosion(this.mName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Advanced Implosion Compressor",
                "Size(WxHxD): 3x3x3 (Hollow), Controller (Front centered)",
                "1x Input Bus (Any casing)",
                "1x Output Bus (Any casing)",
                "1x Maintenance Hatch (Any casing)",
                "1x Muffler Hatch (Any casing)",
                "1x Energy Hatch (Any casing)",
                "Solid Steel Machine Casings for the rest (16 at least!)",
                "Causes " + 20 * getPollutionPerTick(null) + " Pollution per second"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "ImplosionCompressor.png");
    }

    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sImplosionRecipes;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric((GT_Utility.getTier(this.getMaxInputVoltage())/2+1), 100, 100);
	}

    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 20) {
            GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(5)), 10, 1.0F, aX, aY, aZ);
        }
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
            return false;
        }
        int tAmount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if ((!addMaintenanceToMachineList(tTileEntity, 16)) && (!addMufflerToMachineList(tTileEntity, 16)) && (!addInputToMachineList(tTileEntity, 16)) && (!addOutputToMachineList(tTileEntity, 16)) && (!addEnergyInputToMachineList(tTileEntity, 16))) {
                            Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                            byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                            if (((tBlock != GregTech_API.sBlockCasings2) || (tMeta != 0)) && ((tBlock != GregTech_API.sBlockCasings3) || (tMeta != 4))) {
                                return false;
                            }
                            tAmount++;
                        }
                    }
                }
            }
        }
        return tAmount >= 16;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 250;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}