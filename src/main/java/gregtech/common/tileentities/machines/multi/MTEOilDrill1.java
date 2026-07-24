package gregtech.common.tileentities.machines.multi;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEOilDrill1 extends MTEOilDrillBase {

    public MTEOilDrill1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrill1(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOilDrill1(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_SolidSteel;
    }

    @Override
    protected Material getFrameMaterial() {
        return Materials2Materials.Steel;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 16;
    }

    @Override
    protected int getRangeInChunks() {
        return 1;
    }

    @Override
    protected int getMinTier() {
        return 2;
    }
}
