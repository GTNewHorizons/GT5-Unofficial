package gtPlusPlus.core.item.base.dusts;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class BaseItemDustAbstract extends Item {

    protected int colour = 0;
    protected String materialName;
    protected String pileType;

    public BaseItemDustAbstract(final String unlocalizedName, final String materialName, final int colour,
            final String pileSize) {
        this.setUnlocalizedName(unlocalizedName);
        this.setMaxStackSize(64);
        if (pileSize.equalsIgnoreCase("dust")) {
            this.setTextureName(GTPlusPlus.ID + ":" + "dust");
        } else {
            this.setTextureName(GTPlusPlus.ID + ":" + "dust" + pileSize);
        }
        this.setMaxStackSize(64);
        this.colour = colour;
        this.materialName = materialName;
        this.setUnlocalizedName(unlocalizedName);
        GameRegistry.registerItem(this, unlocalizedName);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public abstract void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool);

    public abstract String getMaterialName();

    @Override
    public abstract int getColorFromItemStack(ItemStack stack, int hex);
}
