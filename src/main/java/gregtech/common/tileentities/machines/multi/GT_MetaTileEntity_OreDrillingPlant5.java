package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_OreDrillingPlant5 extends GT_MetaTileEntity_OreDrillingPlantBase {
    public GT_MetaTileEntity_OreDrillingPlant5(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        mTier=5;
    }

    public GT_MetaTileEntity_OreDrillingPlant5(String aName) {
        super(aName);
        mTier=5;
    }

    @Override
    public String[] getDescription() {
        return getDescriptionInternal("V");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_OreDrillingPlant5(mName);
    }
    
    @Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive,
			boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] {
					Textures.BlockIcons.casingTexturePages[1][66],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL_ACTIVE
							: Textures.BlockIcons.OVERLAY_FRONT_ORE_DRILL) };
		}
		return new ITexture[] { Textures.BlockIcons.casingTexturePages[1][66] };
	}
    
    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_RobustNeutronium;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Neutronium;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 194;
    }

    @Override
    protected int getRadiusInChunks() {
        return 16;
    }

    @Override
    protected int getMinTier() {
        return 6;
    }

    @Override
    protected int getBaseProgressTime() {
        return 320;
    }
    
}
