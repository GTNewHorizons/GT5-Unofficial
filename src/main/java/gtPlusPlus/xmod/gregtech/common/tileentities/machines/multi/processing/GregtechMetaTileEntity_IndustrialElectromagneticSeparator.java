package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.item.ItemStack;

public class GregtechMetaTileEntity_IndustrialElectromagneticSeparator extends
    GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialElectromagneticSeparator> implements ISurvivalConstructable {

    public GregtechMetaTileEntity_IndustrialElectromagneticSeparator(final int aID, final String aName,
                                                         final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_IndustrialElectromagneticSeparator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }

    @Override
    public String getMachineType() {
        return "";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return null;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_IndustrialElectromagneticSeparator> getStructureDefinition() {
        return null;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }
}
