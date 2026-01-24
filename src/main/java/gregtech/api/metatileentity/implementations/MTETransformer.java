package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Mods.EnderIO;
import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.GOLD;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyStorage;
import crazypants.enderio.machine.capbank.TileCapBank;
import crazypants.enderio.machine.capbank.network.ICapBankNetwork;
import crazypants.enderio.power.IPowerContainer;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTETransformer extends MTETieredMachineBlock {

    public MTETransformer(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, (String) null);
    }

    public MTETransformer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_4A[mTier + 1] };
            rTextures[1][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_4A[mTier + 1] };
            rTextures[2][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_4A[mTier + 1] };
            rTextures[3][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier + 2] };
            rTextures[4][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier + 2] };
            rTextures[5][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN[mTier + 2] };
            rTextures[6][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_4A[mTier + 1] };
            rTextures[7][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_4A[mTier + 1] };
            rTextures[8][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_4A[mTier + 1] };
            rTextures[9][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier + 2] };
            rTextures[10][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier + 2] };
            rTextures[11][i + 1] = new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][i + 1],
                Textures.BlockIcons.OVERLAYS_ENERGY_OUT[mTier + 2] };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        return mTextures[Math.min(2, side.ordinal()) + (side == facingDirection ? 3 : 0)
            + (baseMetaTileEntity.isAllowedToWork() ? 0 : 6)][colorIndex + 1];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETransformer(mName, mTier, mDescriptionArray, mTextures);
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
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        ForgeDirection blockFrontFacing = getBaseMetaTileEntity().getFrontFacing();

        if (getBaseMetaTileEntity().isAllowedToWork()) {
            return side == blockFrontFacing;
        } else {
            return side != blockFrontFacing;
        }
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return !isInputFacing(side);
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier + 1];
    }

    @Override
    public long maxEUStore() {
        return Math.max(512L, 1L << (mTier + 2)) + V[mTier + 1] * 4L;
    }

    @Override
    public long maxEUInput() {
        return V[getBaseMetaTileEntity().isAllowedToWork() ? mTier + 1 : mTier];
    }

    @Override
    public long maxEUOutput() {
        return V[getBaseMetaTileEntity().isAllowedToWork() ? mTier : mTier + 1];
    }

    @Override
    public long maxAmperesOut() {
        return getBaseMetaTileEntity().isAllowedToWork() ? 4 : 1;
    }

    @Override
    public long maxAmperesIn() {
        return getBaseMetaTileEntity().isAllowedToWork() ? 1 : 4;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && GregTechAPI.mInputRF) {
            aBaseMetaTileEntity.setActive(aBaseMetaTileEntity.isAllowedToWork());
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (aBaseMetaTileEntity.getStoredEU() >= aBaseMetaTileEntity.getEUCapacity()) break;
                if (!aBaseMetaTileEntity.inputEnergyFrom(side)) continue;
                final TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
                if (tTileEntity instanceof IEnergyProvider energyProvider
                    && energyProvider.extractEnergy(side.getOpposite(), 1, true) == 1) {
                    long tEU = ((IEnergyProvider) tTileEntity).extractEnergy(
                        side.getOpposite(),
                        GTUtility.safeInt(maxEUInput() * 100L / GregTechAPI.mRFtoEU),
                        false);
                    tEU = tEU * GregTechAPI.mRFtoEU / 100;
                    aBaseMetaTileEntity.injectEnergyUnits(ForgeDirection.UNKNOWN, Math.min(tEU, maxEUInput()), 1);
                } else if (tTileEntity instanceof IEnergyStorage energyStorage
                    && energyStorage.extractEnergy(1, true) == 1) {
                        long tEU = ((IEnergyStorage) tTileEntity)
                            .extractEnergy(GTUtility.safeInt(maxEUInput() * 100L / GregTechAPI.mRFtoEU), false);
                        tEU = tEU * GregTechAPI.mRFtoEU / 100;
                        aBaseMetaTileEntity.injectEnergyUnits(ForgeDirection.UNKNOWN, Math.min(tEU, maxEUInput()), 1);
                    } else if (EnderIO.isModLoaded() && tTileEntity instanceof IPowerContainer powerContainer
                        && powerContainer.getEnergyStored() > 0) {
                            final int storedRF = powerContainer.getEnergyStored();
                            final int extractRF = GTUtility.safeInt(maxEUInput() * 100L / GregTechAPI.mRFtoEU);
                            long tEU = 0;
                            if (tTileEntity instanceof TileCapBank capBank) {
                                ICapBankNetwork network = capBank.getNetwork();
                                if (network != null && network.getEnergyStoredL() > 0) {
                                    tEU = Math.min(
                                        (Math.min(
                                            Math.min(network.getEnergyStoredL(), storedRF - extractRF),
                                            network.getMaxOutput())) * (long) GregTechAPI.mRFtoEU / 100L,
                                        maxEUInput());
                                    network.addEnergy(GTUtility.safeInt(-(tEU * 100 / GregTechAPI.mRFtoEU)));
                                }
                            } else {
                                if (storedRF > extractRF) {
                                    powerContainer.setEnergyStored(storedRF - extractRF);
                                    tEU = maxEUInput();
                                } else {
                                    powerContainer.setEnergyStored(0);
                                    tEU = storedRF * (long) GregTechAPI.mRFtoEU / 100L;
                                }
                            }
                            aBaseMetaTileEntity
                                .injectEnergyUnits(ForgeDirection.UNKNOWN, Math.min(tEU, maxEUInput()), 1);
                        }
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        //
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        //
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

    @Override
    public boolean hasAlternativeModeText() {
        return true;
    }

    @Override
    public String getAlternativeModeText() {
        return (getBaseMetaTileEntity().isAllowedToWork() ? GTUtility.trans("145", "Step Down, In: ")
            : GTUtility.trans("146", "Step Up, In: ")) + maxEUInput()
            + GTUtility.trans("148", "V ")
            + maxAmperesIn()
            + GTUtility.trans("147", "A, Out: ")
            + maxEUOutput()
            + GTUtility.trans("148", "V ")
            + maxAmperesOut()
            + GTUtility.trans("149", "A");
    }

    @Override
    public boolean shouldJoinIc2Enet() {
        return true;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final ForgeDirection facing = getBaseMetaTileEntity().getFrontFacing();
        final NBTTagCompound tag = accessor.getNBTData();
        final ForgeDirection side = accessor.getSide();
        final boolean allowedToWork = tag.getBoolean("isAllowedToWork");

        final byte inputTier = GTUtility.getTier(tag.getLong("maxEUInput"));
        final byte outputTier = GTUtility.getTier(tag.getLong("maxEUOutput"));

        currenttip.add(
            String.format(
                "%s %s(%dA) -> %s(%dA)",
                (allowedToWork ? (GREEN + StatCollector.translateToLocal("GT5U.waila.transformer.step_down"))
                    : (RED + StatCollector.translateToLocal("GT5U.waila.transformer.step_up"))) + RESET,
                GTMod.proxy.mWailaTransformerVoltageTier ? GTUtility.getColoredTierNameFromTier(inputTier)
                    : tag.getLong("maxEUInput"),
                tag.getLong("maxAmperesIn"),
                GTMod.proxy.mWailaTransformerVoltageTier ? GTUtility.getColoredTierNameFromTier(outputTier)
                    : tag.getLong("maxEUOutput"),
                tag.getLong("maxAmperesOut")));

        if ((side == facing && allowedToWork) || (side != facing && !allowedToWork)) {
            currenttip.add(
                GOLD + StatCollector.translateToLocalFormatted(
                    "GT5U.waila.transformer.input",
                    GTMod.proxy.mWailaTransformerVoltageTier ? GTUtility.getColoredTierNameFromTier(inputTier)
                        : tag.getLong("maxEUInput"),
                    tag.getLong("maxAmperesIn")));
        } else {
            currenttip.add(
                BLUE + StatCollector.translateToLocalFormatted(
                    "GT5U.waila.transformer.output",
                    GTMod.proxy.mWailaTransformerVoltageTier ? GTUtility.getColoredTierNameFromTier(outputTier)
                        : tag.getLong("maxEUOutput"),
                    tag.getLong("maxAmperesOut")));
        }

        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("isAllowedToWork", getBaseMetaTileEntity().isAllowedToWork());
        tag.setLong("maxEUInput", maxEUInput());
        tag.setLong("maxAmperesIn", maxAmperesIn());
        tag.setLong("maxEUOutput", maxEUOutput());
        tag.setLong("maxAmperesOut", maxAmperesOut());
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocalFormatted(
            "gt.blockmachines.transformer.desc",
            GTUtility.getColoredTierNameFromVoltage(maxEUInput()) + EnumChatFormatting.GRAY,
            GTUtility.getColoredTierNameFromVoltage(maxEUOutput()) + EnumChatFormatting.GRAY) };
    }
}
