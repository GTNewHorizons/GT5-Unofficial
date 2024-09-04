package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;

public class MTEHatchDynamo extends MTEHatch {

    public MTEHatchDynamo(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { "Generating electric Energy from Multiblocks", "Puts out up to 1 Amp" });
    }

    public MTEHatchDynamo(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, 0, aDescription);
    }

    public MTEHatchDynamo(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public MTEHatchDynamo(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[mTier] };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 512;
    }

    @Override
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier + 1] * 2L;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDynamo(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }
}
