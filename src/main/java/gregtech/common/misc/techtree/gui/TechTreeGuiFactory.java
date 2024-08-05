package gregtech.common.misc.techtree.gui;

import java.io.IOException;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiManager;

import gregtech.api.enums.ItemList;
import gregtech.common.items.ItemTechAccessor;
import gregtech.common.misc.techtree.TechnologyRegistry;
import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TechTreeGuiFactory extends AbstractUIFactory<TechTreeGuiData> {

    public static final TechTreeGuiFactory INSTANCE = new TechTreeGuiFactory();

    private TechTreeGuiFactory() {
        super("gregtech:techtree:gui");
    }

    public static void open(@Nonnull EntityPlayer player, int x, int y, int z) {
        // Fuck you galacticraft
        if (player instanceof EntityPlayerMP) {
            TechTreeGuiData data = new TechTreeGuiData(player, null);
            GuiManager.open(INSTANCE, data, (EntityPlayerMP) player);
        }
    }

    @Override
    public @NotNull ItemTechAccessor getGuiHolder(TechTreeGuiData data) {
        return (ItemTechAccessor) ItemList.TechTreeAccessor.getItem();
    }

    @Override
    public void writeGuiData(TechTreeGuiData guiData, PacketBuffer buffer) {
        // Write data to buffer
        NBTTagCompound dataNBT = new NBTTagCompound();
        if (guiData.getSelectedTechnology() != null) {
            dataNBT.setString(
                "selectedTechnology",
                guiData.getSelectedTechnology()
                    .getInternalName());
        }

        try {
            buffer.writeNBTTagCompoundToBuffer(dataNBT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Override
    public TechTreeGuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
        try {
            NBTTagCompound data = buffer.readNBTTagCompoundFromBuffer();
            // Read currently selected technology from nbt data buffer
            ITechnology selectedTechnology = null;
            if (data.hasKey("selectedTechnology")) {
                selectedTechnology = TechnologyRegistry.findTechnology(data.getString("selectedTechnology"));
            }
            return new TechTreeGuiData(player, selectedTechnology);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
