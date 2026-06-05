package gregtech.api.modularui2;

import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_ME;
import static gregtech.api.enums.MetaTileEntityIDs.CRAFTING_INPUT_ME_BUS;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;

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

    private final MTEHatchCraftingInputME mte;

    public ProxiedMteGui(MTEHatchCraftingInputME mte) {
        this.mte = mte;
    }

    public static void open(MTEHatchCraftingInputME mte, EntityPlayerMP player) {
        GuiManager.open(GUI, new ProxiedMteGuiData(player, mte), player);
    }

    @SideOnly(Side.CLIENT)
    public ModularScreen createScreen(ProxiedMteGuiData data, ModularPanel mainPanel) {
        return new GTModularScreen(mainPanel, mte.getColoredTheme());
    }

    public ModularPanel buildUI(ProxiedMteGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        return mte.buildUI(
            new PosGuiData(data.getPlayer(), base.getXCoord(), base.getYCoord(), base.getZCoord()),
            syncManager,
            uiSettings);
    }

    final public static class ProxiedMteGuiData extends GuiData {

        final private @Nullable MTEHatchCraftingInputME serverMTE;
        final private boolean supportsFluids;
        final private int x;
        final private int y;
        final private int z;

        public ProxiedMteGuiData(EntityPlayer player, MTEHatchCraftingInputME serverMTE) {
            super(player);
            this.serverMTE = serverMTE;
            this.supportsFluids = serverMTE.supportsFluids();
            IGregTechTileEntity base = serverMTE.getBaseMetaTileEntity();
            this.x = base.getXCoord();
            this.y = base.getYCoord();
            this.z = base.getZCoord();
        }

        public ProxiedMteGuiData(EntityPlayer player, boolean supportsFluids, int x, int y, int z) {
            super(player);
            this.serverMTE = null;
            this.supportsFluids = supportsFluids;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public @Nullable MTEHatchCraftingInputME getServerMTE() {
            return serverMTE;
        }

        public boolean supportsFluids() {
            return supportsFluids;
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
            MTEHatchCraftingInputME mte = data.getServerMTE();
            if (mte == null) {
                // On the server, we must sync to the real MTE. A check that the real MTE is supplied.
                assert NetworkUtils.isClient();
                // Create empty fake MTE on client
                BaseMetaTileEntity fakeBase = new BaseMetaTileEntity();
                fakeBase.setInitialValuesAsNBT(
                    null,
                    (short) (data.supportsFluids() ? CRAFTING_INPUT_ME.ID : CRAFTING_INPUT_ME_BUS.ID));
                NBTTagCompound tag = new NBTTagCompound();
                fakeBase.writeToNBT(tag);
                tag.setInteger("x", data.getX());
                tag.setInteger("y", data.getY());
                tag.setInteger("z", data.getZ());
                BaseMetaTileEntity newBase = new BaseMetaTileEntity();
                newBase.readFromNBT(tag);
                mte = (MTEHatchCraftingInputME) newBase.getMetaTileEntity();
                assert mte != null;
            }
            return new ProxiedMteGui(mte);
        }

        @Override
        public boolean canInteractWith(EntityPlayer player, ProxiedMteGuiData guiData) {
            // Note: seems only called server side and accessing the server only mte is safe
            return super.canInteractWith(player, guiData) && guiData.getServerMTE()
                .isValid();
        }

        @Override
        public void writeGuiData(ProxiedMteGuiData guiData, PacketBuffer buffer) {
            buffer.writeBoolean(guiData.supportsFluids());
            buffer.writeInt(guiData.getX());
            buffer.writeInt(guiData.getY());
            buffer.writeInt(guiData.getZ());
        }

        @Override
        public ProxiedMteGuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
            return new ProxiedMteGuiData(
                player,
                buffer.readBoolean(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt());
        }
    }
}
