package gregtech.common.tileentities.machines.multi;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOreDrillingPlant2(mName);
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_StableTitanium;
    }

    @Override
    protected Material getFrameMaterial() {
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
