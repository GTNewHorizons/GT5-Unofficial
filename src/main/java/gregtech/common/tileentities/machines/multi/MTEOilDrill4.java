package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEOilDrill4 extends MTEOilDrillBase {

    public MTEOilDrill4(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrill4(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return createTooltip("IV");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOilDrill4(mName);
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
        return GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 0);
    }

    @Override
    protected int getRangeInChunks() {
        return 8;
    }

    @Override
    protected int getMinTier() {
        return 5;
    }
}
