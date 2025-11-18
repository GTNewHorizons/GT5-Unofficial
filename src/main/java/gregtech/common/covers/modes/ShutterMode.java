package gregtech.common.covers.modes;

/**
 * Encodes the operation mode of a shutter cover using the enum ordinal. TODO: Stop using ordinal values directly
 */
public enum ShutterMode {

    OPEN_IF_ENABLED,
    OPEN_IF_DISABLED,
    ONLY_OUTPUT,
    ONLY_INPUT;

}
