package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.text.StringKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.IConnectsToVacuumConveyor;

public abstract class MTEHatchVacuumConveyor extends MTEHatch implements IConnectsToVacuumConveyor {

    public static final int VACUUM_MOVE_TICK = 17;
    private static final int UI_SLOT_COUNT = 72;

    public CircuitComponentPacket contents;

    // Identifier used to identify this hatch uniquely inside a multiblock.
    public String identifier = null;

    protected MTEHatchVacuumConveyor(int aID, String aName, String aNameRegional, int aTier, String[] descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
    }

    protected MTEHatchVacuumConveyor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory
            .of(EM_D_ACTIVE, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory
                .of(EM_D_SIDES, Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(EM_D_CONN) };
    }

    public void unifyPacket(CircuitComponentPacket packet) {
        if (contents == null) contents = packet;
        else contents.unifyWith(packet);
    }

    public abstract void moveAround(IGregTechTileEntity aBaseMetaTileEntity);

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == VACUUM_MOVE_TICK) {
                if (contents == null) {
                    getBaseMetaTileEntity().setActive(false);
                } else {
                    getBaseMetaTileEntity().setActive(true);
                    moveAround(aBaseMetaTileEntity);
                }
            }
        }
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
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public byte getColorization() {
        return getBaseMetaTileEntity().getColorization();
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

        // Sync the contents between server and client
        GenericSyncValue<CircuitComponentPacket> contentsSyncHandler = new GenericSyncValue<>(
            () -> contents != null ? contents : new CircuitComponentPacket(),
            val -> contents = val,
            buf -> {
                CircuitComponentPacket packet = new CircuitComponentPacket(
                    (NBTTagCompound) NetworkUtils.readNBTBase(buf));
                return packet;
            },
            (buf, item) -> { NetworkUtils.writeNBTBase(buf, item.writeToNBT()); });
        syncManager.syncValue("contents", contentsSyncHandler);

        // Create the panel
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .doesBindPlayerInventory(false)
            .doesAddGregTechLogo(false)
            .build();

        // Create grid and create customly rendered items that can only show a tooltip and amount
        Grid grid = new Grid().coverChildren()
            .pos(7, 7)
            .mapTo(9, UI_SLOT_COUNT, i -> {
                SingleChildWidget<?> slot = new SingleChildWidget<>().background(GTGuiTextures.SLOT_ITEM_STANDARD)
                    .size(18);
                Widget<?> slotChild = new Widget<>().size(16)
                    .pos(1, 1);
                if (contents != null) {
                    List<ItemStack> representations = contents.getItemRepresentations();
                    if (i < representations.size()) {
                        ItemStack item = representations.get(i);
                        ItemDrawable itemDraw = new ItemDrawable(item);
                        slotChild.background(itemDraw)
                            .overlay(
                                new StringKey(
                                    NumberFormat.format(
                                        contents.getComponents()
                                            .get(CircuitComponent.getFromFakeStackUnsafe(item)),
                                        NumberFormat.AMOUNT_TEXT)).scale(0.6f)
                                            .alignment(Alignment.BottomRight)
                                            .style(EnumChatFormatting.WHITE));
                        slotChild.tooltip(
                            t -> t.clearText()
                                .addStringLines(item.getTooltip(data.getPlayer(), false)));
                    }
                }
                slot.child(slotChild);
                return slot;
            });

        // Make sure to update the slots each time the contents gets updated
        contentsSyncHandler.setChangeListener(() -> {
            if (contents == null) {
                return;
            }

            for (int i = 0; i < UI_SLOT_COUNT; i++) {
                SingleChildWidget<?> slot = (SingleChildWidget<?>) grid.getChildren()
                    .get(i);
                Widget<?> slotChild = (Widget<?>) slot.getChildren()
                    .get(0);
                if (contents != null) {
                    List<ItemStack> representations = contents.getItemRepresentations();
                    if (i < representations.size()) {
                        ItemStack item = representations.get(i);
                        ItemDrawable itemDraw = new ItemDrawable(item);
                        slotChild.background(itemDraw)
                            .overlay(
                                new StringKey(
                                    NumberFormat.format(
                                        contents.getComponents()
                                            .get(CircuitComponent.getFromFakeStackUnsafe(item)),
                                        NumberFormat.AMOUNT_TEXT)).scale(0.6f)
                                            .alignment(Alignment.BottomRight)
                                            .style(EnumChatFormatting.WHITE));
                        slotChild.tooltip(
                            t -> t.clearText()
                                .addStringLines(item.getTooltip(data.getPlayer(), false)));
                        continue;
                    }
                }
                slotChild.overlay();
                slotChild.tooltip(t -> t.clearText());

            }
        });

        return panel.child(grid);
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
