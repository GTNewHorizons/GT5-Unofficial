package gregtech.common.misc.techtree.gui;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.PosGuiData;

import gregtech.api.enums.ItemList;
import gregtech.common.items.ItemTechAccessor;

public class TechTreeGuiFactory extends AbstractUIFactory<PosGuiData> { // todo: create class that extends GuiData for
                                                                        // custom stuff I guess

    public static final TechTreeGuiFactory INSTANCE = new TechTreeGuiFactory();

    private TechTreeGuiFactory() {
        super("gregtech:techtree:gui");
    }

    public static void open(@Nonnull EntityPlayer player, int x, int y, int z) {
        // Fuck you galacticraft
        if (player instanceof EntityPlayerMP) {
            PosGuiData data = new PosGuiData(player, x, y, z);
            GuiManager.open(INSTANCE, data, (EntityPlayerMP) player);
        }
    }

    @Override
    public @NotNull ItemTechAccessor getGuiHolder(PosGuiData data) {
        return (ItemTechAccessor) ItemList.TechTreeAccessor.getItem();
    }

    @Override
    public void writeGuiData(PosGuiData guiData, PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(guiData.getX());
        buffer.writeVarIntToBuffer(guiData.getY());
        buffer.writeVarIntToBuffer(guiData.getZ());
    }

    @NotNull
    @Override
    public PosGuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
        int x = buffer.readVarIntFromBuffer();
        int y = buffer.readVarIntFromBuffer();
        int z = buffer.readVarIntFromBuffer();
        return new PosGuiData(player, x, y, z);
    }
}
