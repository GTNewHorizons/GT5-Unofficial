package gregtech.api.modularui2;

import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.data.drone.CameraViewportClientManager;
import gregtech.common.data.drone.CameraViewportManager;
import gregtech.common.gui.modularui.hatch.MTEHatchCraftingInputSlaveGui;

/**
 * This GUI may be opened when the corresponding TileEntity is not loaded on the client!
 * On the client side, the mte is completely fictional
 * On the server side, the value must be synced to the actual tile entity
 */
@FieldsAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class ProxiedMteGui implements IGuiHolder<ProxiedMteGui.ProxiedMteGuiData> {

    public static final ProxiedMteGuiFactory GUI = new ProxiedMteGuiFactory();

    private final MetaTileEntity mte;

    public ProxiedMteGui(MetaTileEntity mte) {
        this.mte = mte;
    }

    public static void open(MetaTileEntity mte, EntityPlayerMP player) {
        GuiManager.open(GUI, new ProxiedMteGuiData(player, mte), player);
    }

    /** Opens the GUI of an MTE for a player who interacted with another MTE, like the Crafting Input Proxy does. */
    public static void open(MetaTileEntity mte, EntityPlayerMP player, MetaTileEntity originMTE) {
        GuiManager.open(GUI, new ProxiedMteGuiData(player, mte, originMTE), player);
    }

    @SideOnly(Side.CLIENT)
    public ModularScreen createScreen(ProxiedMteGuiData data, ModularPanel mainPanel) {
        return new GTModularScreen(mainPanel, mte.getGuiTheme());
    }

    public ModularPanel buildUI(ProxiedMteGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.addCloseListener(player -> {
            if (syncManager.isClient()) {
                if (GTMod.proxy.cameraViewportManager != null
                    && GTMod.proxy.cameraViewportManager.isObservingActive()) {
                    GTMod.proxy.cameraViewportManager.setSwitchingToRemoteGui(true);
                    ((CameraViewportClientManager) GTMod.proxy.cameraViewportManager).returningFromRemoteGui = true;
                }
            } else if (player instanceof EntityPlayerMP playerMP
                && CameraViewportManager.sessions.containsKey(player.getUniqueID())) {
                    CameraViewportManager.reopenDroneGuiOnServer(playerMP);
                }
        });
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        ModularPanel panel = mte.buildUI(
            new PosGuiData(data.getPlayer(), base.getXCoord(), base.getYCoord(), base.getZCoord()),
            syncManager,
            uiSettings);

        if (data.hasOrigin()) {
            MTEHatchCraftingInputSlaveGui.addRecipeOrderButton(panel, syncManager, data.getOriginMTE());
        }

        UUID playerUUID = (syncManager.isClient()) ? null
            : data.getPlayer()
                .getUniqueID();
        boolean isRemote = GTMod.proxy.cameraViewportManager != null
            && GTMod.proxy.cameraViewportManager.isObservingActive(playerUUID);

        if (isRemote) {
            syncManager.onCommonTick(new Runnable() {

                private boolean initialized = false;

                // Lock every slot on remote GUI
                @Override
                public void run() {
                    if (initialized) {
                        return;
                    }
                    EntityPlayer player = syncManager.getPlayer();
                    if (player != null && player.openContainer instanceof ModularContainer mContainer) {
                        if (mContainer.isInitialized()) {
                            for (Object slotObj : mContainer.inventorySlots) {
                                if (slotObj instanceof ModularSlot slot) {
                                    slot.accessibility(false, false);
                                    slot.canDragInto(false);
                                }
                            }
                            initialized = true;
                        }
                    }
                }
            });
        }

        return panel;
    }

    final public static class ProxiedMteGuiData extends GuiData {

        final private @Nullable MetaTileEntity serverMTE;
        /** The MTE the player interacted with, if it is not the MTE whose GUI is shown. Server side only. */
        final private @Nullable MetaTileEntity originMTE;
        final private boolean hasOrigin;
        final private int mid;
        final private int x;
        final private int y;
        final private int z;

        public ProxiedMteGuiData(EntityPlayer player, MetaTileEntity serverMTE) {
            this(player, serverMTE, null);
        }

        public ProxiedMteGuiData(EntityPlayer player, MetaTileEntity serverMTE, @Nullable MetaTileEntity originMTE) {
            super(player);
            this.serverMTE = serverMTE;
            this.originMTE = originMTE;
            this.hasOrigin = originMTE != null;
            IGregTechTileEntity base = serverMTE.getBaseMetaTileEntity();
            this.mid = base.getMetaTileID();
            this.x = base.getXCoord();
            this.y = base.getYCoord();
            this.z = base.getZCoord();
        }

        public ProxiedMteGuiData(EntityPlayer player, int mid, int x, int y, int z, boolean hasOrigin) {
            super(player);
            this.serverMTE = null;
            this.originMTE = null;
            this.hasOrigin = hasOrigin;
            this.mid = mid;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public @Nullable MetaTileEntity getServerMTE() {
            return serverMTE;
        }

        public @Nullable MetaTileEntity getOriginMTE() {
            return originMTE;
        }

        public boolean hasOrigin() {
            return hasOrigin;
        }

        public int getMetaId() {
            return mid;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }

    final public static class ProxiedMteGuiFactory extends AbstractUIFactory<ProxiedMteGuiData> {

        ProxiedMteGuiFactory() {
            super("gregtech:crafting_input");
        }

        @Override
        public IGuiHolder<ProxiedMteGuiData> getGuiHolder(ProxiedMteGuiData data) {
            MetaTileEntity mte = data.getServerMTE();
            if (mte == null) {
                // On the server, we must sync to the real MTE. A check that the real MTE is supplied.
                assert NetworkUtils.isClient();
                // Create empty fake MTE on client
                BaseMetaTileEntity fakeBase = new BaseMetaTileEntity();
                fakeBase.xCoord = data.getX();
                fakeBase.yCoord = data.getY();
                fakeBase.zCoord = data.getZ();
                fakeBase.setInitialValuesAsNBT(null, (short) data.getMetaId());
                mte = (MetaTileEntity) fakeBase.getMetaTileEntity();
                assert mte != null;
            }
            return new ProxiedMteGui(mte);
        }

        @Override
        public boolean canInteractWith(EntityPlayer player, ProxiedMteGuiData guiData) {
            IMetaTileEntity mte = guiData.getServerMTE();
            // Note: seems only called server side and accessing the server only mte is safe, and we can assume it is
            // never null
            assert mte != null;
            IGregTechTileEntity base = mte.getBaseMetaTileEntity();
            return super.canInteractWith(player, guiData) && base != null && base.canAccessData();
        }

        @Override
        public void writeGuiData(ProxiedMteGuiData guiData, PacketBuffer buffer) {
            buffer.writeInt(guiData.getMetaId());
            buffer.writeInt(guiData.getX());
            buffer.writeInt(guiData.getY());
            buffer.writeInt(guiData.getZ());
            buffer.writeBoolean(guiData.hasOrigin());
        }

        @Override
        public ProxiedMteGuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
            return new ProxiedMteGuiData(
                player,
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readBoolean());
        }
    }
}
