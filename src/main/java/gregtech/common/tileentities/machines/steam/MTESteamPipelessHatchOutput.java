package gregtech.common.tileentities.machines.steam;

import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.ITALIC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.fluid.IFluidStore;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverSteamValve;
import gregtech.common.misc.teams.GTTeam;
import gregtech.common.misc.teams.GTTeamManager;
import gregtech.common.misc.teams.PipelessSteamManager;

public class MTESteamPipelessHatchOutput extends MTEHatchOutput
    implements IFluidStore, IWirelessEnergyHatchInformation {

    private GTTeam ownerTeam;

    public MTESteamPipelessHatchOutput(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTESteamPipelessHatchOutput(String aName, ITexture[][][] aTextures) {
        super(aName, 0, 4, new String[] { "" }, aTextures);
    }

    // spotless:off
    @Override
    public String[] getDescription() {
        return new String[] {
            GRAY               + "Stores Steam globally in a network, up to 2^(2^31) L.",
            GRAY               + "Does not connect to Pipes. This block accepts Steam into the network.",
            GRAY               + "Supports Steam, Superheated Steam, and Supercritical Steam (and their dense variants).",
            AQUA + "" + ITALIC + "In a dream, you remember something named a 'Wireless Energy Dynamo,' or something like",
            AQUA + "" + ITALIC + "that. You don't remember what it did, but the similarities here are clear. However, you",
            AQUA + "" + ITALIC + "remember laughing at their uselessness. Hard to imagine that reality, when these are so useful..."
        };
    }
    // spotless:on

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (aTick % ticks_between_energy_addition == 0) {
            tryPushingSteam();
        }
    }

    private PipelessSteamManager getSteamManager() {
        if (ownerTeam == null) {
            IGregTechTileEntity te = getBaseMetaTileEntity();
            ownerTeam = GTTeamManager.getTeam(te.getOwnerName(), te.getOwnerUuid());
        }
        return GTTeamManager.getSteamData(ownerTeam);
    }

    private void tryPushingSteam() {
        if (mFluid == null || mFluid.amount == 0) return;
        PipelessSteamManager manager = getSteamManager();

        if (GTModHandler.isAnySteam(mFluid)) {
            manager.fillSteam(mFluid.amount);
            drain(Integer.MAX_VALUE, true);
        } else if (mFluid.getFluid() == Materials.DenseSteam.mGas) {
            manager.fillDenseSteam(mFluid.amount);
            drain(Integer.MAX_VALUE, true);
        } else if (GTModHandler.isSuperHeatedSteam(mFluid)) {
            manager.fillSuperheatedSteam(mFluid.amount);
            drain(Integer.MAX_VALUE, true);
        } else if (mFluid.getFluid() == Materials.DenseSuperheatedSteam.mGas) {
            manager.fillDenseSuperheatedSteam(mFluid.amount);
            drain(Integer.MAX_VALUE, true);
        } else if (mFluid.getFluid()
            .getName()
            .equals("supercriticalsteam")) {
                manager.fillSupercriticalSteam(mFluid.amount);
                drain(Integer.MAX_VALUE, true);
            } else if (mFluid.getFluid() == Materials.DenseSupercriticalSteam.mGas) {
                manager.fillDenseSupercriticalSteam(mFluid.amount);
                drain(Integer.MAX_VALUE, true);
            }
    }

    @Override
    public boolean isEmptyAndAcceptsAnyFluid() {
        return getFluidAmount() == 0;
    }

    @Override
    public int getCapacity() {
        return 128_000 * (int) ticks_between_energy_addition;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean canStoreFluid(@NotNull FluidStack fluidStack) {
        if (mFluid != null && !GTUtility.areFluidsEqual(mFluid, fluidStack)) {
            return false;
        }
        return CoverSteamValve.isFluidCompatible(fluidStack);
    }

    // todo
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
    public boolean hasFluidLocking() {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamPipelessHatchOutput(mName, mTextures);
    }

    @Override
    protected boolean supportsFluidPushing() {
        return false;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { EnumChatFormatting.BLUE + "Output Hatch" + EnumChatFormatting.RESET, "Stored Fluid:",
            EnumChatFormatting.GOLD + (mFluid == null ? "No Fluid" : mFluid.getLocalizedName())
                + EnumChatFormatting.RESET,
            EnumChatFormatting.GREEN + GTUtility.formatNumbers(mFluid == null ? 0 : mFluid.amount)
                + " L"
                + EnumChatFormatting.RESET
                + " "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getCapacity())
                + " L"
                + EnumChatFormatting.RESET };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {}
}
