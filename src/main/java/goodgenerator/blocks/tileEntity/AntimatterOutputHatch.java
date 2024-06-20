package goodgenerator.blocks.tileEntity;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import net.minecraftforge.common.util.ForgeDirection;

public class AntimatterOutputHatch extends GT_MetaTileEntity_Hatch_Output {

    public AntimatterOutputHatch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
        this.mDescriptionArray[1] = "Stores Antimatter";
        this.mDescriptionArray[2] = "Antimatter can be inserted from any side";
        this.mDescriptionArray[3] = "Capacity: 100000000L";
    }

    public AntimatterOutputHatch(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new AntimatterOutputHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getCapacity() {
        return 100000000;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return !(side == this.getBaseMetaTileEntity().getFrontFacing());
    }
}
