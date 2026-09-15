package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEConcreteBackfiller2 extends MTEConcreteBackfillerBase {

    public MTEConcreteBackfiller2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEConcreteBackfiller2(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return createTooltip("Advanced Concrete Backfiller");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEConcreteBackfiller2(mName);
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
    protected int getRadius() {
        int tConfig = getTotalConfigValue() * 2;
        return tConfig >= 128 ? 128 : tConfig <= 0 ? 64 : tConfig;
    }

    @Override
    protected int getMinTier() {
        return 4;
    }
}
