package tectech.thing.metaTileEntity.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 16.12.2016.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchEnergyMulti extends MTEHatch {

    public final int maxAmperes;
    public int Amperes;

    public int getAmperes() {
        return Amperes;
    }

    public void setAmperes(int amperes) {
        Amperes = amperes;
    }

    public MTEHatchEnergyMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.hatch.energymulti.desc.0"),
                translateToLocalFormatted("gt.blockmachines.hatch.energymulti.desc.2", aAmp + (aAmp >> 2)),
                translateToLocalFormatted("gt.blockmachines.hatch.energymulti.desc.3", aAmp) });
        Amperes = maxAmperes = aAmp;
    }

    public MTEHatchEnergyMulti(String aName, int aTier, int aAmp, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        Amperes = maxAmperes = aAmp;
    }

    public MTEHatchEnergyMulti(int aID, String aName, String aNameRegional, int aTier, int i, String[] description,
        int aAmp) {
        super(aID, aName, aNameRegional, aTier, 0, description);
        Amperes = maxAmperes = aAmp;
    }

    public int getHatchType() {
        return 1;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (maxAmperes > 64) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_LASER[mTier + 1] };
        } else if (maxAmperes > 16) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier + 1] };
        } else if (maxAmperes > 4) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1] };
        } else if (maxAmperes > 2) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_4A[mTier + 1] };
        } else {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_2A[mTier + 1] };
        }
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        if (maxAmperes > 64) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_LASER[mTier + 1] };
        } else if (maxAmperes > 16) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_64A[mTier + 1] };
        } else if (maxAmperes > 4) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1] };
        } else if (maxAmperes > 2) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_4A[mTier + 1] };
        } else {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_2A[mTier + 1] };
        }
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
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
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 4L * Amperes;
    }

    @Override
    public long maxAmperesIn() {
        return Amperes + (Amperes >> 2);
    }

    @Override
    public long maxWorkingAmperesIn() {
        return Amperes;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("amperage", Amperes);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        currenttip.add(
            translateToLocal("gt.blockmachines.hatch.energytunnel.desc.1") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(
                    accessor.getNBTData()
                        .getLong("amperage") * V[mTier])
                + EnumChatFormatting.RESET
                + " EU/t");
    }

    @Override
    public String[] getInfoData() {
        return new String[] { translateToLocal("gt.blockmachines.hatch.energytunnel.desc.1") + ": "
            + EnumChatFormatting.YELLOW
            + formatNumber(Amperes * V[mTier])
            + EnumChatFormatting.RESET
            + " EU/t" };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchEnergyMulti(mName, mTier, Amperes, mDescriptionArray, mTextures);
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
