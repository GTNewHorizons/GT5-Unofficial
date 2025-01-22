package gregtech.common.tileentities.machines.steam;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.common.covers.CoverSteamValve;
import gregtech.common.misc.teams.GTTeam;
import gregtech.common.misc.teams.GTTeamManager;
import gregtech.common.misc.teams.PipelessSteamManager;

public class MTESteamPipelessHatch extends MTEHatchInput implements IWirelessEnergyHatchInformation {

    private GTTeam ownerTeam;

    public MTESteamPipelessHatch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTESteamPipelessHatch(String aName, ITexture[][][] aTextures) {
        super(aName, 0, new String[] { "" }, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamPipelessHatch(mName, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Stores steam globally in a network, up to 2^(2^31) L.",
            EnumChatFormatting.GRAY + "Does not connect to pipes. This block withdraws Steam from the network." };
    }

    // todo overlays
    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[0] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_WIRELESS_ON[0] };
    }

    @Override
    protected ITexture getBaseTexture(int colorIndex) {
        return TextureFactory.of(Textures.BlockIcons.MACHINE_BRONZE_SIDE);
    }

    @Override
    public int getCapacityPerTank(int aTier, int aSlot) {
        return getCapacity();
    }

    @Override
    public int getCapacity() {
        return 128_000 * (int) ticks_between_energy_addition;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return CoverSteamValve.isFluidCompatible(aFluid);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        tryFetchingSteam();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (aTick % ticks_between_energy_addition == 0) {
            tryFetchingSteam();
        }
    }

    private PipelessSteamManager getSteamManager() {
        if (ownerTeam == null) {
            IGregTechTileEntity te = getBaseMetaTileEntity();
            ownerTeam = GTTeamManager.getTeam(te.getOwnerName(), te.getOwnerUuid());
        }
        return GTTeamManager.getSteamData(ownerTeam);
    }

    private void tryFetchingSteam() {
        long currentSteam = getFluidAmount();
        long maxSteam = getCapacity();
        long steamToTransfer = Math.min(maxSteam - currentSteam, getCapacity());
        if (steamToTransfer <= 0) return;
        PipelessSteamManager manager = getSteamManager();
        long drained = manager.drainSteam(steamToTransfer, false);
        fill(GTModHandler.getSteam(drained), true);
    }

    // don't let pipes connect (hopefully this doesn't have consequences)
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        return new FluidTankInfo[] {};
    }
}
