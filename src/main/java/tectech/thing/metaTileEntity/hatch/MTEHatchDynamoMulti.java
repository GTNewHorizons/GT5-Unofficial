package tectech.thing.metaTileEntity.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;

import java.util.List;

import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHideTooltipEnergyInfo;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Created by danie_000 on 16.12.2016.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDynamoMulti extends MTEHatchDynamo implements IHideTooltipEnergyInfo {

    public final int maxAmperes;
    public int Amperes;

    public MTEHatchDynamoMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier);
        Amperes = maxAmperes = aAmp;
    }

    public MTEHatchDynamoMulti(int aID, String aName, String aNameRegional, int aTier, int i, String[] description,
                               int aAmp) {
        super(aID, aName, aNameRegional, aTier, description);
        Amperes = maxAmperes = aAmp;
    }

    public MTEHatchDynamoMulti(String aName, int aTier, int aAmp, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        Amperes = maxAmperes = aAmp;
    }

    public int getAmperes() {
        return Amperes;
    }

    public void setAmperes(int amperes) {
        Amperes = amperes;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        if (maxAmperes > 64) {
            // Laser hatches are separate classes, so detect by type instead of amperage.
            if (this instanceof MTEHatchDynamoTunnel) {
                return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_LASER[mTier + 1] };
            }
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_256A[mTier + 1] };
        } else if (maxAmperes > 16) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier + 1] };
        } else if (maxAmperes > 4) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1] };
        } else if (maxAmperes > 2) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_4A[mTier + 1] };
        } else {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[mTier + 1] };
        }
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        if (maxAmperes > 64) {
            if (this instanceof MTEHatchDynamoTunnel) {
                return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_LASER[mTier + 1] };
            }
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_256A[mTier + 1] };
        } else if (maxAmperes > 16) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_64A[mTier + 1] };
        } else if (maxAmperes > 4) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1] };
        } else if (maxAmperes > 2) {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_4A[mTier + 1] };
        } else {
            return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_2A[mTier + 1] };
        }
    }

    @Override
    public long getMinimumStoredEU() {
        return 128L * Amperes;
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
            GTUtility.translate(
                "gt.tileentity.throughput",
                EnumChatFormatting.YELLOW + formatNumber(
                    accessor.getNBTData()
                        .getLong("amperage") * V[mTier])
                    + EnumChatFormatting.RESET
                    + " EU/t"));
    }

    @Override
    public String[] getInfoData() {
        return new String[] { GTUtility.translate(
            "gt.tileentity.throughput",
            EnumChatFormatting.YELLOW + formatNumber(Amperes * V[mTier]) + EnumChatFormatting.RESET + " EU/t") };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDynamoMulti(mName, mTier, Amperes, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return MTEHatch.formatEnergyInfoDesc(true, mTier, maxAmperes, "gt.blockmachines.hatch.dynamomulti.desc");
    }
}
