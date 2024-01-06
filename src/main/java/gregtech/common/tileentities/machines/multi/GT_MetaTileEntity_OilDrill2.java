package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_OilDrill2 extends GT_MetaTileEntity_OilDrillBase {

    public GT_MetaTileEntity_OilDrill2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_OilDrill2(String aName) {
        super(aName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return createTooltip("II");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OilDrill2(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_CleanStainlessSteel;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.StainlessSteel;
    }

    @Override
    protected int getCasingTextureIndex() {
        return GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 1);
    }

    @Override
    protected int getRangeInChunks() {
        return 4;
    }

    @Override
    protected int getMinTier() {
        return 3;
    }
}
