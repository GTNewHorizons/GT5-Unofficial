package gtnhlanth.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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

        if (!this.descSpectrum.isEmpty())
            list.add("Suitable for the " + this.descSpectrum + " segment of the electromagnetic spectrum and lower");

        if (this.getMaxDamage() > 0) // Not a precursor.
            list.add("Max Uses: " + (this.getMaxDamage() + 1)); // maximum uses = max damage + 1 in general, as
                                                                // 0-durability masks still function

    }

    public String getDescSpectrum() {
        return descSpectrum;
    }

}
