package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEConcreteBackfiller1 extends MTEConcreteBackfillerBase {

    public MTEConcreteBackfiller1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEConcreteBackfiller1(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return createTooltip("Concrete Backfiller");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEConcreteBackfiller1(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_SolidSteel;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Steel;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 16;
    }

    @Override
    protected int getRadius() {
        return 16;
    }

    @Override
    protected int getMinTier() {
        return 2;
    }
}
