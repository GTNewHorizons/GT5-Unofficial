package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import tectech.thing.metaTileEntity.Textures;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by danie_000 on 16.12.2016.
 */
public class MTEHatchDynamoMulti extends MTEHatch {

    public final int maxAmperes;
    public int Amperes;

    public MTEHatchDynamoMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL,
                translateToLocal("gt.blockmachines.hatch.dynamomulti.desc.0") }); // Multiple Ampere Energy
                                                                                  // Extractor for Multiblocks
        Amperes = maxAmperes = aAmp;
        TTUtility.setTier(aTier, this);
    }

    public MTEHatchDynamoMulti(String aName, int aTier, int aAmp, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        Amperes = maxAmperes = aAmp;
    }

    public MTEHatchDynamoMulti(int aID, String aName, String aNameRegional, int aTier, int i, String[] description,
        int aAmp) {
        super(aID, aName, aNameRegional, aTier, 0, description);
        Amperes = maxAmperes = aAmp;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.OVERLAYS_ENERGY_IN_POWER_TT[mTier] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.OVERLAYS_ENERGY_IN_POWER_TT[mTier] };
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
        return 128L * Amperes;
    }

    @Override
    public long maxEUOutput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 4L * Amperes;
    }

    @Override
    public long maxAmperesOut() {
        return Amperes;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDynamoMulti(mName, mTier, Amperes, mDescriptionArray, mTextures);
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
