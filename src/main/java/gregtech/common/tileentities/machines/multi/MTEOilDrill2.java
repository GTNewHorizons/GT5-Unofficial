package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEOilDrill2 extends MTEOilDrillBase {

    public MTEOilDrill2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrill2(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return createTooltip("II");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOilDrill2(mName);
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
        return GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1);
    }

    @Override
    protected int getRangeInChunks() {
        return 2;
    }

    @Override
    protected int getMinTier() {
        return 3;
    }
}
