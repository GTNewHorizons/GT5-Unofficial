package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_OreDrillingPlant4 extends GT_MetaTileEntity_OreDrillingPlantBase {
    public GT_MetaTileEntity_OreDrillingPlant4(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_OreDrillingPlant4(String aName) {
        super(aName);
    }

    @Override
    public String[] getDescription() {
        return getDescriptionInternal("IV");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OreDrillingPlant4(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_MiningOsmiridium;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Osmiridium;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 62;
    }

    @Override
    protected int getRadiusInChunks() {
        return 9;
    }

    @Override
    protected int getMinTier() {
        return 5;
    }

    @Override
    protected int getBaseProgressTime() {
        return 480;
    }
}
