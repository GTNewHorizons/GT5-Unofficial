package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;

public class GT_MetaTileEntity_BronzeBlastFurnace
        extends GT_MetaTileEntity_PrimitiveBlastFurnace {
    private static final ITexture[] FACING_SIDE = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEPLATEDBRICKS)};
    private static final ITexture[] FACING_FRONT = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE)};
    private static final ITexture[] FACING_ACTIVE = {new GT_RenderedTexture(Textures.BlockIcons.MACHINE_BRONZEBLASTFURNACE_ACTIVE)};

    public GT_MetaTileEntity_BronzeBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_BronzeBlastFurnace(String aName) {
        super(aName);
    }

	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_BronzeBlastFurnace(this.mName);
	}

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Bronze Blast Furnace",
                "Useable for Steel and general Pyrometallurgy",
                "Size(WxHxD): 3x4x3 (Hollow, with opening on top)",
                "Built from 32 Bronze Plated Bricks",
                "Causes 50 Pollution per second"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return aActive ? FACING_ACTIVE : FACING_FRONT;
        }
        return FACING_SIDE;
    }

	@Override
	protected boolean isCorrectCasingBlock(Block block) {
		return block == GregTech_API.sBlockCasings1;
	}

	@Override
	protected boolean isCorrectCasingMetaID(int metaID) {
		return metaID == 10;
	}

	@Override
	public String getName() {
		return "Bronze Blast Furnace";
	}

 
}