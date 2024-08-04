package gregtech.common.misc.techtree.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiData;

public class TechTreeGuiFactory extends AbstractUIFactory {

    @Override
    public @NotNull IGuiHolder getGuiHolder(GuiData data) {
        return null;
    }

    @Override
    public void writeGuiData(GuiData guiData, PacketBuffer buffer) {

    }

    @NotNull
    @Override
    public GuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
        return null;
    }
}
