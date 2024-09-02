package gregtech.common.misc.techtree.gui;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.factory.GuiData;

import gregtech.common.misc.techtree.interfaces.ITechnology;

public class TechTreeGuiData extends GuiData {

    private ITechnology selectedTechnology;

    public TechTreeGuiData(EntityPlayer player, ITechnology selectedTechnology) {
        super(player);
        this.selectedTechnology = selectedTechnology;
    }

    public void setSelectedTechnology(ITechnology tech) {
        this.selectedTechnology = tech;
    }

    public ITechnology getSelectedTechnology() {
        return this.selectedTechnology;
    }

}
