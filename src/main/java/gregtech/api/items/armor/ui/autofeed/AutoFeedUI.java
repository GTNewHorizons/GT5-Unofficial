package gregtech.api.items.armor.ui.autofeed;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

public class AutoFeedUI extends AbstractUIFactory<GuiData> implements IGuiHolder<GuiData> {

    public static final AutoFeedUI INSTANCE = new AutoFeedUI();

    private AutoFeedUI() {
        super("gregtech:armor_autofeedgui");
    }

    public void open(EntityPlayer player) {
        if (!player.getEntityWorld().isRemote && player instanceof EntityPlayerMP playerMP) {
            GuiManager.open(this, new GuiData(player), playerMP);
        }
    }

    @Override
    public @NotNull IGuiHolder<GuiData> getGuiHolder(final GuiData data) {
        return this;
    }

    @Override
    public void writeGuiData(final GuiData guiData, final PacketBuffer buffer) {}

    @Override
    public @NotNull GuiData readGuiData(final EntityPlayer player, final PacketBuffer buffer) {
        return new GuiData(player);
    }

    @Override
    public ModularScreen createScreen(final GuiData data, final ModularPanel mainPanel) {
        return new ModularScreen(GregTech.ID, mainPanel);
    }

    @Override
    public ModularPanel buildUI(final GuiData data, final PanelSyncManager syncManager, final UISettings settings) {
        AutoFeedUIFactory gui = new AutoFeedUIFactory();
        return gui.createMainPanel(data, syncManager, settings);
    }
}
