package gregtech.api.modernmaterials.transition;

import static gregtech.api.modernmaterials.blocks.registration.BlocksEnum.FrameBox;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Bolt;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.CrushedCentrifugedOre;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.CrushedOre;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.DensePlate;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.DoublePlate;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Dust;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Foil;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Gem;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.ImpureDust;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Ingot;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Lens;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Nugget;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Plate;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.PurifiedDust;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.PurifiedOre;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.QuadruplePlate;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.QuintuplePlate;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Ring;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Rod;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Round;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.Screw;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.SmallDust;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.TinyDust;
import static gregtech.api.modernmaterials.items.partclasses.ItemsEnum.TriplePlate;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.colen.postea.API.ItemStackReplacementManager;

import akka.japi.Pair;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import gregtech.api.modernmaterials.items.partclasses.ItemsEnum;

public abstract class TransitionItems {

    private static Pair<OrePrefixes, ItemsEnum> getPrefixFromIDGroup01(int metadata) {
        switch (metadata / 1000) {
            case 0 -> {
                return new Pair<>(OrePrefixes.dustTiny, TinyDust);
            }
            case 1 -> {
                return new Pair<>(OrePrefixes.dustSmall, SmallDust);
            }
            case 2 -> {
                return new Pair<>(OrePrefixes.dust, Dust);
            }
            case 3 -> {
                return new Pair<>(OrePrefixes.dustImpure, ImpureDust);
            }
            case 4 -> {
                return new Pair<>(OrePrefixes.dustPure, PurifiedDust);
            }
            case 5 -> {
                return new Pair<>(OrePrefixes.crushed, CrushedOre);
            }
            case 6 -> {
                return new Pair<>(OrePrefixes.crushedPurified, PurifiedOre);
            }
            case 7 -> {
                return new Pair<>(OrePrefixes.crushedCentrifuged, CrushedCentrifugedOre);
            }
            case 8 -> {
                return new Pair<>(OrePrefixes.gem, Gem);
            }
            case 9 -> {
                return new Pair<>(OrePrefixes.nugget, Nugget);
            }
            case 11 -> {
                return new Pair<>(OrePrefixes.ingot, Ingot);
            }
            case 17 -> {
                return new Pair<>(OrePrefixes.plate, Plate);
            }
            case 18 -> {
                return new Pair<>(OrePrefixes.plateDouble, DoublePlate);
            }
            case 19 -> {
                return new Pair<>(OrePrefixes.plateTriple, TriplePlate);
            }
            case 20 -> {
                return new Pair<>(OrePrefixes.plateQuadruple, QuadruplePlate);
            }
            case 21 -> {
                return new Pair<>(OrePrefixes.plateQuintuple, QuintuplePlate);
            }
            case 22 -> {
                return new Pair<>(OrePrefixes.plateDense, DensePlate);
            }
            case 23 -> {
                return new Pair<>(OrePrefixes.stick, Rod);
            }
            case 24 -> {
                return new Pair<>(OrePrefixes.lens, Lens);
            }
            case 25 -> {
                return new Pair<>(OrePrefixes.round, Round);
            }
            case 26 -> {
                return new Pair<>(OrePrefixes.bolt, Bolt);
            }
            case 27 -> {
                return new Pair<>(OrePrefixes.screw, Screw);
            }
            case 28 -> {
                return new Pair<>(OrePrefixes.ring, Ring);
            }
            case 29 -> {
                return new Pair<>(OrePrefixes.foil, Foil);
            }
            case 30 -> {
                return new Pair<>(OrePrefixes.cell, null); // Todo resolve this case.
            }
            case 31 -> {
                return new Pair<>(OrePrefixes.cellPlasma, null); // Todo resolve this case.
            }
        }
        return null; // Not yet set-up
    }

    private static Pair<OrePrefixes, ItemsEnum> getPrefixFromIDGroup02(int metadata) {
        switch (metadata / 1000) {
            case 16 -> {
                return new Pair<>(OrePrefixes.turbineBlade, ItemsEnum.TurbineBlade);
            }
            case 18 -> {
                return new Pair<>(OrePrefixes.itemCasing, ItemsEnum.ItemCasing);
            }
            case 19 -> {
                return new Pair<>(OrePrefixes.wireFine, ItemsEnum.FineWire);
            }
            case 20 -> {
                return new Pair<>(OrePrefixes.gearGtSmall, ItemsEnum.SmallGear);
            }
            case 21 -> {
                return new Pair<>(OrePrefixes.rotor, ItemsEnum.Rotor);
            }
            case 22 -> {
                return new Pair<>(OrePrefixes.stickLong, ItemsEnum.LongRod);
            }
            case 23 -> {
                return new Pair<>(OrePrefixes.springSmall, ItemsEnum.SmallSpring);
            }
            case 24 -> {
                return new Pair<>(OrePrefixes.spring, ItemsEnum.Spring);
            }
            case 25 -> {
                return new Pair<>(OrePrefixes.arrowGtWood, null);
            }
            case 26 -> {
                return new Pair<>(OrePrefixes.arrowGtPlastic, null);
            }
            case 27 -> {
                return new Pair<>(OrePrefixes.gemChipped, ItemsEnum.ChippedGem);
            }
            case 28 -> {
                return new Pair<>(OrePrefixes.gemFlawed, ItemsEnum.FlawedGem);
            }
            case 29 -> {
                return new Pair<>(OrePrefixes.gemFlawless, ItemsEnum.FlawlessGem);
            }
            case 30 -> {
                return new Pair<>(OrePrefixes.gemExquisite, ItemsEnum.ExquisiteGem);
            }
            case 31 -> {
                return new Pair<>(OrePrefixes.gearGt, ItemsEnum.Gear);
            }
        }
        return null; // Not yet set-up
    }

    private static Pair<OrePrefixes, ItemsEnum> getPrefixFromIDGroup03(int metadata) {
        switch (metadata / 1000) {
            case 4 -> {
                return new Pair<>(OrePrefixes.nanite, ItemsEnum.Nanites);
            }
        }
        return null; // Not yet set-up
    }

    private static NBTTagCompound convertMeta01(NBTTagCompound tag) {
        short metadata = tag.getShort("Damage");
        if (metadata >= 32000) return tag; // Do nothing, this is not a valid material part.

        int materialID = metadata % 1000;

        Pair<OrePrefixes, ItemsEnum> prefix = getPrefixFromIDGroup01(metadata);
        if (prefix == null) return tag;

        if (prefix.second() == null) {
            // Invalid items we want effectively "purged"
            // e.g. arrows as these are deprecated now.
            return convertToCobblestone(tag);
        }

        { // Conversion occurs
            Item underlyingItem = prefix.second()
                .getItem();

            ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
            if (material == null) return tag;

            tag.setShort("id", (short) Item.getIdFromItem(underlyingItem));
            tag.setShort("Damage", (short) materialID);
        }

        return tag;
    }

    private static NBTTagCompound convertMeta02(NBTTagCompound tag) {
        short metadata = tag.getShort("Damage");
        if (metadata < 16000 || metadata >= 32000) return tag; // Do nothing, this is not a valid material part.

        int materialID = metadata % 1000;

        Pair<OrePrefixes, ItemsEnum> prefix = getPrefixFromIDGroup02(metadata);
        if (prefix == null) return tag;

        if (prefix.second() == null) {
            // Invalid items we want effectively "purged"
            // e.g. arrows as these are deprecated now.
            return convertToCobblestone(tag);
        }

        { // Conversion occurs
            Item underlyingItem = prefix.second()
                .getItem();

            ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
            if (material == null) return tag;

            tag.setShort("id", (short) Item.getIdFromItem(underlyingItem));
            tag.setShort("Damage", (short) materialID);
        }

        return tag;
    }

    private static NBTTagCompound convertMeta03(NBTTagCompound tag) {
        short metadata = tag.getShort("Damage");
        if (metadata < 4000 || metadata >= 5000) return tag; // Do nothing, this is not a valid material part.

        int materialID = metadata % 1000;

        Pair<OrePrefixes, ItemsEnum> prefix = getPrefixFromIDGroup03(metadata);
        if (prefix == null) return tag;

        if (prefix.second() == null) {
            // Invalid items we want effectively "purged"
            // e.g. arrows as these are deprecated now.
            return convertToCobblestone(tag);
        }

        { // Conversion occurs
            Item underlyingItem = prefix.second()
                .getItem();

            ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
            if (material == null) return tag;

            tag.setShort("id", (short) Item.getIdFromItem(underlyingItem));
            tag.setShort("Damage", (short) materialID);
        }

        return tag;
    }

    private static final short cobbleStoneID = (short) Item.getIdFromItem(Item.getItemFromBlock(Blocks.cobblestone));

    private static NBTTagCompound convertToCobblestone(NBTTagCompound tag) {
        tag.setShort("id", cobbleStoneID);
        tag.setShort("Damage", (short) 0);
        tag.setTag("tag", new NBTTagCompound()); // Remove extraneous info.

        return tag;
    }

    public static void fixWorldItems() {
        // Register replacers.

        // This is all major material parts, gears, plates etc.
        ItemStackReplacementManager.addItemReplacement("gregtech:gt.metaitem.01", TransitionItems::convertMeta01);
        ItemStackReplacementManager.addItemReplacement("gregtech:gt.metaitem.02", TransitionItems::convertMeta02);
        ItemStackReplacementManager.addItemReplacement("gregtech:gt.metaitem.03", TransitionItems::convertMeta03);

        // E.g. frames
        ItemStackReplacementManager
            .addItemReplacement("gregtech:gt.blockmachines", TransitionItems::convertMachineItemBlocks);

        // All normal and small ores.
        ItemStackReplacementManager.addItemReplacement("gregtech:gt.blockores", TransitionItems::convertOreItemBlocks);

    }

    private static NBTTagCompound convertMachineItemBlocks(NBTTagCompound tag) {

        int metadata = tag.getShort("Damage");

        if (metadata > 4096 && metadata <= 5095) {
            return convertFrameBoxItemBlocks(tag);
        }

        return tag;
    }

    private static NBTTagCompound convertFrameBoxItemBlocks(NBTTagCompound tag) {
        int metadata = tag.getShort("Damage");
        int materialID = metadata - 4096;

        ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
        if (material == null) return tag;

        Item newItem = FrameBox.getItem(material);
        int newID = Item.getIdFromItem(newItem);

        tag.setShort("id", (short) newID);
        tag.setShort("Damage", (short) materialID);

        return tag;
    }

    // Converts ore blocks of all varieties, including small.
    private static NBTTagCompound convertOreItemBlocks(NBTTagCompound tag) {
        int metadata = tag.getShort("Damage");
        int materialID = metadata % 1000;

        ModernMaterial material = ModernMaterial.getMaterialFromID(materialID);
        if (material == null) return tag;

        BlocksEnum newBlockEnum = getPrefixFromIDGroupOres(metadata);
        if (newBlockEnum == null) return tag; // No conversion exists for this block type currently.
        Item newItem = newBlockEnum.getItem(material);
        if (newItem == null) return tag; // This material ID has no conversion registered.

        int newID = Item.getIdFromItem(newItem);

        tag.setShort("id", (short) newID);
        tag.setShort("Damage", (short) materialID);

        return tag;
    }

    private static BlocksEnum getPrefixFromIDGroupOres(int metadata) {
        switch (metadata / 1000) {

            // Normal ores
            case 0 -> {
                return BlocksEnum.EarthNormalOre;
            }
            case 1 -> {
                return BlocksEnum.NetherNormalOre;
            }
            case 2 -> {
                return BlocksEnum.EndNormalOre;
            }
            case 3 -> {
                return BlocksEnum.BlackGraniteNormalOre;
            }
            case 4 -> {
                return BlocksEnum.RedGraniteNormalOre;
            }
            case 5 -> {
                return BlocksEnum.MarbleNormalOre;
            }
            case 6 -> {
                return BlocksEnum.BasaltNormalOre;
            }

            // Small ores
            case 16 -> {
                return BlocksEnum.EarthSmallOre;
            }
            case 17 -> {
                return BlocksEnum.NetherSmallOre;
            }
            case 18 -> {
                return BlocksEnum.EndSmallOre;
            }
            case 19 -> {
                return BlocksEnum.BlackGraniteSmallOre;
            }
            case 20 -> {
                return BlocksEnum.RedGraniteSmallOre;
            }
            case 21 -> {
                return BlocksEnum.MarbleSmallOre;
            }
            case 22 -> {
                return BlocksEnum.BasaltSmallOre;
            }
        }
        return null;
    }

}
