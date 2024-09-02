package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEOreDrillingPlant4 extends MTEOreDrillingPlantBase {

    public MTEOreDrillingPlant4(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mTier = 4;
    }

    public MTEOreDrillingPlant4(String aName) {
        super(aName);
        mTier = 4;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return createTooltip("IV");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOreDrillingPlant4(mName);
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
