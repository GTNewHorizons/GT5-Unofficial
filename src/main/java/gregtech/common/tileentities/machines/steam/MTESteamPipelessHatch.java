package gregtech.common.tileentities.machines.steam;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.ITALIC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessEnergyHatchInformation;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverSteamValve;
import gregtech.common.misc.teams.GTTeam;
import gregtech.common.misc.teams.GTTeamManager;
import gregtech.common.misc.teams.PipelessSteamManager;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsUI;

public class MTESteamPipelessHatch extends MTEHatchCustomFluidBase implements IWirelessEnergyHatchInformation {

    private static final int STEAM_SELECTOR_WINDOW_ID = 11;

    private GTTeam ownerTeam;
    private SteamType selectedSteam = SteamType.STEAM;

    public MTESteamPipelessHatch(int aID, String aName, String aNameRegional) {
        super(
            FluidUtils.getSteam(1)
                .getFluid(),
            128_000 * (int) ticks_between_energy_addition,
            aID,
            aName,
            aNameRegional,
            0);
    }

    public MTESteamPipelessHatch(String aName, ITexture[][][] aTexture) {
        super(
            FluidUtils.getSteam(1)
                .getFluid(),
            128_000 * (int) ticks_between_energy_addition,
            aName,
            0,
            new String[] { "" },
            aTexture);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamPipelessHatch(this.mName, this.mTextures);
    }

    // spotless:off
    @Override
    public String[] getDescription() {
        return new String[] {
            GRAY               + "Stores steam globally in a network, up to 2^(2^31) L.",
            GRAY               + "Does not connect to pipes. This block withdraws Steam from the network.",
            GRAY               + "Supports Steam, Superheated Steam and Supercritical Steam (and their dense variants).",
            AQUA + "" + ITALIC + "Where does it come from? Capable of extracting Steam from seemingly nowhere, and even",
            AQUA + "" + ITALIC + "without any type of Pipes, you begin to question what you thought you knew about this",
            AQUA + "" + ITALIC + "simple action of boiling water. One thing's for sure though, it still may not be enough..."
        };
    }
    // spotless:on

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
    public boolean doesEmptyContainers() {
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
        if (aTick % 20 == 0) {
            // Validate the proper type
            if (getFluid() != null && getFluidAmount() > 0) {
                if (selectedSteam.getFluid() == getFluid().getFluid()) {
                    // Refresh the steam type
                    flushSteam();
                    tryFetchingSteam();
                }
            }
        }
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
        long drained = selectedSteam.drainNetwork(manager, steamToTransfer);
        fill(selectedSteam.getFluidStack(drained), true);
    }

    private void flushSteam() {
        if (getFluid() == null || getFluidAmount() == 0) {
            return;
        }

        for (SteamType type : SteamType.VALUES) {
            if (type.getFluid() == getFluid().getFluid()) {
                type.fillNetwork(getSteamManager(), getFluidAmount());
                drain(Integer.MAX_VALUE, true);
                return;
            }
        }
    }

    @Override
    public void onBlockDestroyed() {
        if (getBaseMetaTileEntity().isServerSide()) {
            flushSteam();
        }
        super.onBlockDestroyed();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("SteamType", selectedSteam.ordinal());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        selectedSteam = SteamType.VALUES[aNBT.getInteger("SteamType")];
    }

    // don't let pipes connect (hopefully this doesn't have consequences)
    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        return new FluidTankInfo[] {};
    }

    @Override
    protected FluidSlotWidget createFluidSlot() {
        return super.createFluidSlot().setFilter(f -> isFluidInputAllowed(new FluidStack(f, 1)));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext ctx) {
        super.addUIWidgets(builder, ctx);
        ctx.addSyncedWindow(STEAM_SELECTOR_WINDOW_ID, this::createSteamSelectorWindow);

        builder.widget(
            new ButtonWidget()
                .setOnClick((data, widget) -> ForgeOfGodsUI.reopenWindow(widget, STEAM_SELECTOR_WINDOW_ID))
                .setBackground(GTUITextures.BUTTON_STANDARD)
                .addTooltip("Choose Steam Type")
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setSize(18, 18)
                .setPos(7, 63));
        builder.widget(
            new ItemDrawable(GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 1)).asWidget()
                .setPos(8, 64)
                .setSize(16, 16));
    }

    protected ModularWindow createSteamSelectorWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(180, 150);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());

        builder.widget(
            new FakeSyncWidget.IntegerSyncer(
                () -> selectedSteam.ordinal(),
                val -> selectedSteam = SteamType.VALUES[val]));

        builder.widget(
            new ItemDrawable(GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 1)).asWidget()
                .setPos(3, 3)
                .setSize(16, 16));
        builder.widget(
            new TextWidget(new Text("Steam Type")).setTextAlignment(Alignment.CenterLeft)
                .setPos(20, 3)
                .setSize(160, 20));
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(getGUIWidth() - 15, 3));

        MultiChildWidget typeColumn = new MultiChildWidget();
        typeColumn.setSize(174, 120);
        typeColumn.setPos(6, 24);
        builder.widget(typeColumn);

        for (int i = 0; i < SteamType.VALUES.length; i++) {
            SteamType type = SteamType.VALUES[i];

            typeColumn.addChild(new ButtonWidget().setOnClick((data, widget) -> {
                if (widget.isClient()) return;
                selectedSteam = type;
            })
                .setBackground(() -> {
                    if (selectedSteam == type) {
                        return new IDrawable[] { TecTechUITextures.SLOT_OUTLINE_GREEN };
                    }
                    return new IDrawable[] { IDrawable.EMPTY };
                })
                .setSize(18, 18)
                .setPos(0, 20 * i));
            typeColumn.addChild(
                new ItemDrawable(GTUtility.getFluidDisplayStack(type.getFluidStack(1), false)).asWidget()
                    .setPos(1, i * 20 + 1)
                    .setSize(16, 16));
            typeColumn.addChild(
                new TextWidget(new Text(type.getName())).setTextAlignment(Alignment.CenterLeft)
                    .setPos(22, i * 20)
                    .setSize(152, 20));
        }
        return builder.build();
    }

    private enum SteamType {

        STEAM {

            private Fluid fluid;

            @Override
            Fluid getFluid() {
                if (this.fluid == null) {
                    this.fluid = FluidRegistry.getFluid("steam");
                }
                return fluid;
            }

            @Override
            String getName() {
                return "Steam";
            }

            @Override
            void fillNetwork(PipelessSteamManager manager, long amount) {
                manager.fillSteam(amount);
            }

            @Override
            long drainNetwork(PipelessSteamManager manager, long amount) {
                return manager.drainSteam(amount, false);
            }
        },
        DENSE_STEAM {

            private Fluid fluid;

            @Override
            Fluid getFluid() {
                if (this.fluid == null) {
                    this.fluid = Materials.DenseSteam.mGas;
                }
                return fluid;
            }

            @Override
            String getName() {
                return "Dense Steam";
            }

            @Override
            void fillNetwork(PipelessSteamManager manager, long amount) {
                manager.fillDenseSteam(amount);
            }

            @Override
            long drainNetwork(PipelessSteamManager manager, long amount) {
                return manager.drainDenseSteam(amount, false);
            }
        },
        SH_STEAM {

            private Fluid fluid;

            @Override
            Fluid getFluid() {
                if (this.fluid == null) {
                    this.fluid = FluidRegistry.getFluid("ic2superheatedsteam");
                }
                return fluid;
            }

            @Override
            String getName() {
                return "Superheated Steam";
            }

            @Override
            void fillNetwork(PipelessSteamManager manager, long amount) {
                manager.fillSuperheatedSteam(amount);
            }

            @Override
            long drainNetwork(PipelessSteamManager manager, long amount) {
                return manager.drainSuperheatedSteam(amount, false);
            }
        },
        DENSE_SH_STEAM {

            private Fluid fluid;

            @Override
            Fluid getFluid() {
                if (this.fluid == null) {
                    this.fluid = Materials.DenseSuperheatedSteam.mGas;
                }
                return fluid;
            }

            @Override
            String getName() {
                return "Dense Superheated Steam";
            }

            @Override
            void fillNetwork(PipelessSteamManager manager, long amount) {
                manager.fillDenseSuperheatedSteam(amount);
            }

            @Override
            long drainNetwork(PipelessSteamManager manager, long amount) {
                return manager.drainDenseSuperheatedSteam(amount, false);
            }
        },
        SC_STEAM {

            private Fluid fluid;

            @Override
            Fluid getFluid() {
                if (this.fluid == null) {
                    this.fluid = FluidRegistry.getFluid("supercriticalsteam");
                }
                return fluid;
            }

            @Override
            String getName() {
                return "Supercritical Steam";
            }

            @Override
            void fillNetwork(PipelessSteamManager manager, long amount) {
                manager.fillSupercriticalSteam(amount);
            }

            @Override
            long drainNetwork(PipelessSteamManager manager, long amount) {
                return manager.drainSupercriticalSteam(amount, false);
            }
        },
        DENSE_SC_STEAM {

            private Fluid fluid;

            @Override
            Fluid getFluid() {
                if (this.fluid == null) {
                    this.fluid = Materials.DenseSupercriticalSteam.mGas;
                }
                return this.fluid;
            }

            @Override
            String getName() {
                return "Dense Supercritical Steam";
            }

            @Override
            void fillNetwork(PipelessSteamManager manager, long amount) {
                manager.fillDenseSupercriticalSteam(amount);
            }

            @Override
            long drainNetwork(PipelessSteamManager manager, long amount) {
                return manager.drainDenseSupercriticalSteam(amount, false);
            }
        };

        static final SteamType[] VALUES = values();

        FluidStack getFluidStack(long amount) {
            return new FluidStack(getFluid(), (int) amount);
        }

        abstract Fluid getFluid();

        abstract String getName();

        abstract void fillNetwork(PipelessSteamManager manager, long amount);

        abstract long drainNetwork(PipelessSteamManager manager, long amount);
    }
}
