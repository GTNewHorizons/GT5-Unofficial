package gregtech.loaders.shapeconsumers;

/// Registers every `Consumer*` in this package. Called from `GTMod`'s handler for
/// `com.ruling_0.materiallib.api.MaterialRegistrationEvent`, after `gregtech.api.enums.Materials2#init` has
/// declared every shape they target -- most consumers dispatch later, during MaterialLib's postInit (see
/// [ShapeConsumerSupport#delegate]), but must be registered during MaterialLib's preInit like everything else
/// `MaterialRegistrationEvent` drives. `ConsumerStackSizeClamp` dispatches at init instead, since it does not
/// share the postInit-forcing cross-mod class-loading hazard.
public final class LoaderShapeConsumers {

    private LoaderShapeConsumers() {}

    public static void register() {
        ConsumerStackSizeClamp.register();
        ConsumerBlock.register();
        ConsumerOre.register();
        ConsumerRawOre.register();
        ConsumerIceOre.register();
        ConsumerOrePoor.register();
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
        ConsumerToolHead.register();
        ConsumerToolOther.register();

        ConsumerCell.register();
        ConsumerShaping.register();
        ConsumerTransforming.register();
        ConsumerRecycling.register();
    }
}
