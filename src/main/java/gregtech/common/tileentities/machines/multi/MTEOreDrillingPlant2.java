package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEOreDrillingPlant2 extends MTEOreDrillingPlantBase {

    public MTEOreDrillingPlant2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mTier = 2;
    }

    public MTEOreDrillingPlant2(String aName) {
        super(aName);
        mTier = 2;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return createTooltip("II");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOreDrillingPlant2(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_StableTitanium;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Titanium;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 50;
    }

    @Override
    protected int getRadiusInChunks() {
        return 4;
    }

    @Override
    protected int getMinTier() {
        return 3;
    }

    @Override
    protected int getBaseProgressTime() {
        return 800;
    }
}
