package gtnhlanth.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gtnhlanth.Tags;

public class ItemPhotolithographicMask extends Item implements ICanFocus {

    private final String descSpectrum;

    public ItemPhotolithographicMask(String name, int maxDamage, String descSpectrum) {
        super();
        this.descSpectrum = descSpectrum;
        this.setUnlocalizedName("photomask." + name);
        this.setMaxStackSize(1);
        this.setMaxDamage(maxDamage);
        this.setNoRepair();
        this.setTextureName(Tags.MODID + ":photomask/" + name);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {

        if (!this.descSpectrum.isEmpty()) list.add(
            StatCollector.translateToLocalFormatted("tooltip.gtnhlanth.photomask.desc_spectrum", this.descSpectrum));

        if (this.getMaxDamage() > 0) // Not a precursor.
            // maximum uses = max damage + 1 in general, as 0-durability masks still function
            list.add(
                StatCollector
                    .translateToLocalFormatted("tooltip.gtnhlanth.photomask.max_uses", this.getMaxDamage() + 1));

    }

    public String getDescSpectrum() {
        return descSpectrum;
    }

}
