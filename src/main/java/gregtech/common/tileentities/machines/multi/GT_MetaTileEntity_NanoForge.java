package gregtech.common.tileentities.machines.multi;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_NanoForge 
extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_NanoForge> 
implements ISurvivalConstructable{

    public GT_MetaTileEntity_NanoForge(int aID, String aName, String aNameRegional){
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_NanoForge(String aName) {
        super(aName);
    }

    private static GT_Recipe.GT_Recipe_Map GetRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sNanoForge;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_NanoForge> getStructureDefinition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        // TODO Auto-generated method stub
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
    
}