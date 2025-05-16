package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.common.modularui2.util.CommonGuiComponents.gridTemplate1by1;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;
import gregtech.api.enums.ItemList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.tileentities.machines.multi.nanochip.util.ReadOnlyItemSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponentPacket;
import gregtech.common.tileentities.machines.multi.nanochip.util.IConnectsToVacuumConveyor;

public abstract class MTEHatchVacuumConveyor extends MTEHatch implements IConnectsToVacuumConveyor {

    public static final int VACUUM_MOVE_TICK = 17;

    public CircuitComponentPacket contents;

    private ItemStackHandler fakeItemHandler;

    // Identifier used to identify this hatch uniquely inside a multiblock.
    public String identifier = null;

    protected MTEHatchVacuumConveyor(int aID, String aName, String aNameRegional, int aTier, String[] descr) {
        super(aID, aName, aNameRegional, aTier, 27, descr);
    }

    protected MTEHatchVacuumConveyor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 27, aDescription, aTextures);
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
        for(int i=0; i<27; i++){
            mInventory[i] = ItemList.Battery_Buffer_1by1_LV.get(i);
        }
        syncManager.registerSlotGroup("item_inv", 27);
        ModularPanel panel = GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings).build();
        Grid grid = new Grid().coverChildren()
            .pos(7, 7)
            .mapTo(9, 27, index -> new ReadOnlyItemSlot().slot(new ModularSlot(inventoryHandler, index).slotGroup("item_inv")));
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
            // TODO: Would be neat to get a gui that displays these in item form I suppose (using some fake items or
            // something)
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
