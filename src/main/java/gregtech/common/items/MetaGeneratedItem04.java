package gregtech.common.items;

import static gregtech.client.GTTooltipHandler.Tier.HV;
import static gregtech.client.GTTooltipHandler.Tier.IV;
import static gregtech.client.GTTooltipHandler.Tier.LuV;
import static gregtech.client.GTTooltipHandler.Tier.ZPM;
import static gregtech.client.GTTooltipHandler.registerTieredTooltip;

import gregtech.api.enums.ItemList;
import gregtech.api.items.MetaGeneratedItem;

public class MetaGeneratedItem04 extends MetaGeneratedItem {

    public static MetaGeneratedItem04 INSTANCE;

    public MetaGeneratedItem04() {
        super("metaitem.04", (short) 0, Short.MAX_VALUE);
        INSTANCE = this;

        init();
    }

    private void init() {
        addItems();
        registerAllTieredTooltips();
    }

    private void addItems() {
        addManipulatorParts();
    }

    public void addRecipes() {

    }

    private void addManipulatorParts() {
        ItemList.MatterManipulatorHologram
            .set(addItem(IDMetaItem04.MatterManipulatorHologram.ID, "Matter Manipulator Plan", ""));
        ItemList.MatterManipulatorPowerCore0
            .set(addItem(IDMetaItem04.MatterManipulatorPowerCore0.ID, "Prototype Matter Manipulator Power Core", ""));
        ItemList.MatterManipulatorComputerCore0.set(
            addItem(
                IDMetaItem04.MatterManipulatorComputerCore0.ID,
                "Prototype Matter Manipulator 'Computer' Core",
                ""));
        ItemList.MatterManipulatorTeleporterCore0.set(
            addItem(
                IDMetaItem04.MatterManipulatorTeleporterCore0.ID,
                "Prototype Matter Manipulator Teleporter Core",
                ""));
        ItemList.MatterManipulatorFrame0
            .set(addItem(IDMetaItem04.MatterManipulatorFrame0.ID, "Prototype Matter Manipulator Frame", ""));
        ItemList.MatterManipulatorLens0
            .set(addItem(IDMetaItem04.MatterManipulatorLens0.ID, "Prototype Matter Manipulator Lens Assembly", ""));
        ItemList.MatterManipulatorPowerCore1
            .set(addItem(IDMetaItem04.MatterManipulatorPowerCore1.ID, "Matter Manipulator Power Core MKI", ""));
        ItemList.MatterManipulatorComputerCore1
            .set(addItem(IDMetaItem04.MatterManipulatorComputerCore1.ID, "Matter Manipulator Computer Core MKI", ""));
        ItemList.MatterManipulatorTeleporterCore1.set(
            addItem(IDMetaItem04.MatterManipulatorTeleporterCore1.ID, "Matter Manipulator Teleporter Core MKI", ""));
        ItemList.MatterManipulatorFrame1
            .set(addItem(IDMetaItem04.MatterManipulatorFrame1.ID, "Matter Manipulator Frame MKI", ""));
        ItemList.MatterManipulatorLens1
            .set(addItem(IDMetaItem04.MatterManipulatorLens1.ID, "Matter Manipulator Lens Assembly MKI", ""));
        ItemList.MatterManipulatorPowerCore2
            .set(addItem(IDMetaItem04.MatterManipulatorPowerCore2.ID, "Matter Manipulator Power Core MKII", ""));
        ItemList.MatterManipulatorComputerCore2
            .set(addItem(IDMetaItem04.MatterManipulatorComputerCore2.ID, "Matter Manipulator Computer Core MKII", ""));
        ItemList.MatterManipulatorTeleporterCore2.set(
            addItem(IDMetaItem04.MatterManipulatorTeleporterCore2.ID, "Matter Manipulator Teleporter Core MKII", ""));
        ItemList.MatterManipulatorFrame2
            .set(addItem(IDMetaItem04.MatterManipulatorFrame2.ID, "Matter Manipulator Frame MKII", ""));
        ItemList.MatterManipulatorLens2
            .set(addItem(IDMetaItem04.MatterManipulatorLens2.ID, "Matter Manipulator Lens Assembly MKII", ""));
        ItemList.MatterManipulatorPowerCore3
            .set(addItem(IDMetaItem04.MatterManipulatorPowerCore3.ID, "Matter Manipulator Power Core MKIII", ""));
        ItemList.MatterManipulatorComputerCore3
            .set(addItem(IDMetaItem04.MatterManipulatorComputerCore3.ID, "Matter Manipulator Computer Core MKIII", ""));
        ItemList.MatterManipulatorTeleporterCore3.set(
            addItem(IDMetaItem04.MatterManipulatorTeleporterCore3.ID, "Matter Manipulator Teleporter Core MKIII", ""));
        ItemList.MatterManipulatorFrame3
            .set(addItem(IDMetaItem04.MatterManipulatorFrame3.ID, "Matter Manipulator Frame MKIII", ""));
        ItemList.MatterManipulatorLens3
            .set(addItem(IDMetaItem04.MatterManipulatorLens3.ID, "Matter Manipulator Lens Assembly MKIII", ""));
        ItemList.MatterManipulatorAEDownlink
            .set(addItem(IDMetaItem04.MatterManipulatorAEDownlink.ID, "Matter Manipulator ME Downlink", ""));
        ItemList.MatterManipulatorQuantumDownlink
            .set(addItem(IDMetaItem04.MatterManipulatorQuantumDownlink.ID, "Matter Manipulator Quantum Downlink", ""));
    }

    private void registerAllTieredTooltips() {
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorFrame0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorLens0.get(1), HV);
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorFrame1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorLens1.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorFrame2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorLens2.get(1), LuV);
        registerTieredTooltip(ItemList.MatterManipulatorPowerCore3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorComputerCore3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorTeleporterCore3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorFrame3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorLens3.get(1), ZPM);
        registerTieredTooltip(ItemList.MatterManipulatorAEDownlink.get(1), IV);
        registerTieredTooltip(ItemList.MatterManipulatorQuantumDownlink.get(1), ZPM);
    }
}
