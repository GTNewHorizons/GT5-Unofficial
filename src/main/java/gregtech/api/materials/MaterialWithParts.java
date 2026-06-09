package gregtech.api.materials;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.OrePrefixes;

public interface MaterialWithParts {

    @Nullable
    ItemStack getPart(OrePrefixes prefix, int amount);

    default ItemStack getIngotHot(int amount) {
        return getPart(OrePrefixes.ingotHot, amount);
    }

    default ItemStack getIngot(int amount) {
        return getPart(OrePrefixes.ingot, amount);
    }

    default ItemStack getGemChipped(int amount) {
        return getPart(OrePrefixes.gemChipped, amount);
    }

    default ItemStack getGemFlawed(int amount) {
        return getPart(OrePrefixes.gemFlawed, amount);
    }

    default ItemStack getGemFlawless(int amount) {
        return getPart(OrePrefixes.gemFlawless, amount);
    }

    default ItemStack getGemExquisite(int amount) {
        return getPart(OrePrefixes.gemExquisite, amount);
    }

    default ItemStack getGem(int amount) {
        return getPart(OrePrefixes.gem, amount);
    }

    default ItemStack getDustTiny(int amount) {
        return getPart(OrePrefixes.dustTiny, amount);
    }

    default ItemStack getDustSmall(int amount) {
        return getPart(OrePrefixes.dustSmall, amount);
    }

    default ItemStack getDustImpure(int amount) {
        return getPart(OrePrefixes.dustImpure, amount);
    }

    default ItemStack getDustRefined(int amount) {
        return getPart(OrePrefixes.dustRefined, amount);
    }

    default ItemStack getDustPure(int amount) {
        return getPart(OrePrefixes.dustPure, amount);
    }

    default ItemStack getDust(int amount) {
        return getPart(OrePrefixes.dust, amount);
    }

    default ItemStack getNugget(int amount) {
        return getPart(OrePrefixes.nugget, amount);
    }

    default ItemStack getPlateDense(int amount) {
        return getPart(OrePrefixes.plateDense, amount);
    }

    default ItemStack getPlateSuperdense(int amount) {
        return getPart(OrePrefixes.plateSuperdense, amount);
    }

    default ItemStack getPlateQuintuple(int amount) {
        return getPart(OrePrefixes.plateQuintuple, amount);
    }

    default ItemStack getPlateQuadruple(int amount) {
        return getPart(OrePrefixes.plateQuadruple, amount);
    }

    default ItemStack getPlateTriple(int amount) {
        return getPart(OrePrefixes.plateTriple, amount);
    }

    default ItemStack getPlateDouble(int amount) {
        return getPart(OrePrefixes.plateDouble, amount);
    }

    default ItemStack getPlate(int amount) {
        return getPart(OrePrefixes.plate, amount);
    }

    default ItemStack getItemCasing(int amount) {
        return getPart(OrePrefixes.itemCasing, amount);
    }

    default ItemStack getFoil(int amount) {
        return getPart(OrePrefixes.foil, amount);
    }

    default ItemStack getStickLong(int amount) {
        return getPart(OrePrefixes.stickLong, amount);
    }

    default ItemStack getStick(int amount) {
        return getPart(OrePrefixes.stick, amount);
    }

    default ItemStack getRound(int amount) {
        return getPart(OrePrefixes.round, amount);
    }

    default ItemStack getBolt(int amount) {
        return getPart(OrePrefixes.bolt, amount);
    }

    default ItemStack getScrew(int amount) {
        return getPart(OrePrefixes.screw, amount);
    }

    default ItemStack getRing(int amount) {
        return getPart(OrePrefixes.ring, amount);
    }

    default ItemStack getSpringSmall(int amount) {
        return getPart(OrePrefixes.springSmall, amount);
    }

    default ItemStack getSpring(int amount) {
        return getPart(OrePrefixes.spring, amount);
    }

    default ItemStack getWireFine(int amount) {
        return getPart(OrePrefixes.wireFine, amount);
    }

    default ItemStack getRotor(int amount) {
        return getPart(OrePrefixes.rotor, amount);
    }

    default ItemStack getGearGtSmall(int amount) {
        return getPart(OrePrefixes.gearGtSmall, amount);
    }

    default ItemStack getGearGt(int amount) {
        return getPart(OrePrefixes.gearGt, amount);
    }

    default ItemStack getLens(int amount) {
        return getPart(OrePrefixes.lens, amount);
    }

    default ItemStack getCellPlasma(int amount) {
        return getPart(OrePrefixes.cellPlasma, amount);
    }

    default ItemStack getCellMolten(int amount) {
        return getPart(OrePrefixes.cellMolten, amount);
    }

    default ItemStack getCell(int amount) {
        return getPart(OrePrefixes.cell, amount);
    }
}
