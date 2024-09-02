package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEOreDrillingPlant3 extends MTEOreDrillingPlantBase {

    public MTEOreDrillingPlant3(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mTier = 3;
    }

    public MTEOreDrillingPlant3(String aName) {
        super(aName);
        mTier = 3;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return createTooltip("III");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOreDrillingPlant3(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_RobustTungstenSteel;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.TungstenSteel;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 48;
    }

    @Override
    protected int getRadiusInChunks() {
        return 6;
    }

    @Override
    protected int getMinTier() {
        return 4;
    }

    @Override
    protected int getBaseProgressTime() {
        return 640;
    }
}
