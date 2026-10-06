package gregtech.loaders.shapeconsumers;

/// Registers every `Consumer*` in this package. Runs from `GTMod`'s `MaterialRegistrationEvent` handler, after
/// `gregtech.api.enums.MaterialSystem#init` declares the shapes they target. Most consumers dispatch at MaterialLib's
/// postInit (see [ShapeConsumerSupport]); `ConsumerStackSizeClamp` dispatches at init.
public final class LoaderShapeConsumers {

    private LoaderShapeConsumers() {}

    public static void register() {
        ConsumerStackSizeClamp.register();
        ConsumerBlock.register();
        ConsumerCasing.register();
        ConsumerOre.register();
        ConsumerRawOre.register();
        ConsumerIceOre.register();
        ConsumerCrushedOre.register();
        ConsumerPure.register();
        ConsumerDirty.register();
        ConsumerDust.register();
        ConsumerOreSmelting.register();

        ConsumerIngot.register();
        ConsumerPlate.register();
        ConsumerGear.register();
        ConsumerRotor.register();
        ConsumerRound.register();
        ConsumerScrew.register();
        ConsumerBolt.register();
        ConsumerStick.register();
        ConsumerStickLong.register();

        ConsumerFoil.register();
        ConsumerFineWire.register();
        ConsumerNugget.register();
        ConsumerLens.register();
        ConsumerGem.register();
        ConsumerGemImplosion.register();
        ConsumerToolHead.register();
        ConsumerToolOther.register();

        ConsumerCell.register();
        ConsumerWire.register();
        ConsumerPipe.register();
        ConsumerFrame.register();
        ConsumerSheetmetal.register();
        ConsumerShaping.register();
        ConsumerTransforming.register();
        ConsumerRecycling.register();
        ConsumerShapeRegistrators.register();
    }
}
