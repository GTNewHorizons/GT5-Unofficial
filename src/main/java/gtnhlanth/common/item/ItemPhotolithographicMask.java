package gtnhlanth.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gtnhlanth.Tags;

public class ItemPhotolithographicMask extends Item implements ICanFocus {

    private final String descSpectrum;

    public ItemPhotolithographicMask(String name, String descSpectrum) {
        super();
        this.descSpectrum = descSpectrum;
        this.setUnlocalizedName("photomask." + name);
        this.setMaxStackSize(64);
        this.setNoRepair();
        this.setTextureName(Tags.MODID + ":photomask/" + name);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {

        if (!this.descSpectrum.isEmpty()) list.add(
            StatCollector.translateToLocalFormatted("tooltip.gtnhlanth.photomask.desc_spectrum", this.descSpectrum));

    }

    public String getDescSpectrum() {
        return descSpectrum;
    }

}
