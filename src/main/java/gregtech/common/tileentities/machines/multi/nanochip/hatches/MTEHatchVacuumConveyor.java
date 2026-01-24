package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VACUUM_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VACUUM_HATCH_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_VACUUM_PIPE_PORT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTEHatchVacuumConveyorGui;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryElement;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryGrid;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryNetwork;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;

public abstract class MTEHatchVacuumConveyor extends MTEHatch implements VacuumFactoryElement {

    public static final int VACUUM_MOVE_TICK = 17;
    public VacuumFactoryNetwork network;
    protected MTENanochipAssemblyComplex mainController;
    public CircuitComponentPacket contents;

    // Identifier used to identify this hatch uniquely inside a multiblock.
    public String identifier = null;

    protected MTEHatchVacuumConveyor(int aID, String aName, String aNameRegional, int aTier, String[] descr) {
        super(aID, aName, aNameRegional, aTier, 1, descr);
    }

    protected MTEHatchVacuumConveyor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(OVERLAY_VACUUM_HATCH_ACTIVE, Dyes.getModulation(getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(OVERLAY_VACUUM_PIPE_PORT) };
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        VacuumFactoryGrid.INSTANCE.addElement(this);
        super.onColorChangeServer(aColor);
    }

    @Override
    public void onFacingChange() {
        VacuumFactoryGrid.INSTANCE.addElement(this);
        super.onFacingChange();
    }

    @Override
    public byte getColorization() {
        return this.getBaseMetaTileEntity()
            .getColorization();
    }

    @Override
    public void onRemoval() {
        VacuumFactoryGrid.INSTANCE.removeElement(this);
        super.onRemoval();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity base) {
        VacuumFactoryGrid.INSTANCE.addElement(this);
        super.onFirstTick(base);
    }

    @Override
    public VacuumFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(VacuumFactoryNetwork network) {
        this.network = network;
    }

    public void setMainController(MTENanochipAssemblyComplex main) {
        this.mainController = main;
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory.of(OVERLAY_VACUUM_HATCH, Dyes.getModulation(getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(OVERLAY_VACUUM_PIPE_PORT) };
    }

    public void unifyPacket(CircuitComponentPacket packet) {
        if (contents == null) contents = packet;
        else contents.unifyWith(packet);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == VACUUM_MOVE_TICK) {
                if (contents == null) {
                    getBaseMetaTileEntity().setActive(false);
                } else {
                    getBaseMetaTileEntity().setActive(true);
                    this.getNetwork()
                        .addElement(this);
                }
            }
        }
    }


    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
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
    public void getNeighbours(Collection<VacuumFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) return;

        if (base.getTileEntityAtSide(base.getFrontFacing()) instanceof IGregTechTileEntity igte
            && igte.getColorization() == base.getColorization()
            && igte.getMetaTileEntity() instanceof VacuumFactoryElement element) {
            neighbours.add(element);
        }
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (this.contents != null) {
            aNBT.setTag("vacuumContents", this.contents.writeToNBT());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("vacuumContents")) {
            this.contents = new CircuitComponentPacket(aNBT.getCompoundTag("vacuumContents"));
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchVacuumConveyorGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected GTGuiTheme getGuiTheme() {
        return GTGuiThemes.NANOCHIP;
    }

    // for the niche case if someone wants to create a future expanded hatch
    public final int getRowCount() {
        return 4;
    }

    public final int getColumnCount() {
        return 4;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add("Contents: ");
        if (identifier != null) {
            info.add("Hatch ID: " + identifier);
        }
        if (contents != null) {
            Map<CircuitComponent, Long> components = contents.getComponents();
            for (Map.Entry<CircuitComponent, Long> component : components.entrySet()) {
                info.add(
                    EnumChatFormatting.YELLOW + component.getKey()
                        .getLocalizedName()
                        + ": "
                        + EnumChatFormatting.WHITE
                        + GTUtility.formatNumbers(component.getValue()));
            }
        }
        return info.toArray(new String[] {});
    }
}
