package gregtech.api.modernmaterials.transition;

import com.colen.postea.API.ItemStackReplacementManager;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.items.partclasses.ItemsEnum;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Function;

import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Dust;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Ingot;

public abstract class TransitionItems {

    public static OrePrefixes getPrefixFromIDGroup(int metadata) {
        switch (metadata / 1000) {
            case 2 -> {
                return OrePrefixes.dust;
            }
            case 11 -> {
                return OrePrefixes.ingot;
            }
        }
        return null; // Not yet set-up
    }

    public static ItemsEnum getItemsEnumFromOld(OrePrefixes prefixes) {
        switch (prefixes) {
            case ingot -> {
                return Ingot;
            }
            case dust -> {
                return Dust;
            }
        }
        return null; // No equivalent, yet.
    }

    public static void fixWorld() {
        Function<NBTTagCompound, NBTTagCompound> itemTransformer = (tag) -> {

            short metadata = tag.getShort("Damage");
            if (metadata < 2000 || metadata >= 30000) return tag; // Do nothing, this is not a valid material part.

            int materialID = metadata % 1000;

            OrePrefixes prefix = getPrefixFromIDGroup(metadata);
            if (prefix == null) return tag;
            ItemsEnum itemsEnum = getItemsEnumFromOld(prefix);
            if (itemsEnum == null) return tag;

            { // Conversion occurs
                Item underlyingItem = itemsEnum.getItem();

                ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
                if (material == null) return tag;

                tag.setShort("id", (short) Item.getIdFromItem(underlyingItem));
                tag.setShort("Damage", (short) materialID);
            }

            return tag;
        };

        ItemStackReplacementManager.addItemReplacement("gregtech:gt.metaitem.01", itemTransformer);
    }

}
