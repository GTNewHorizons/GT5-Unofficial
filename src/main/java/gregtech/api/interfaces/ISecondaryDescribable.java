package gregtech.api.interfaces;

/**
 * To get a tooltip with a secondary description
 */
public interface ISecondaryDescribable extends IDescribable {
    /**
     * Convenient to call when overriding the `String[] getDescription()` method.
     */
    default String[] getCurrentDescription() {
        if (isDisplaySecondaryDescription() && getSecondaryDescription() != null) {
            return getSecondaryDescription();
        }
        return getPrimaryDescription();
    }

    String[] getPrimaryDescription();

    String[] getSecondaryDescription();

    /**
     * This method will only be called on client side
     *
     * @return whether the secondary description should be display. default is false
     */
    default boolean isDisplaySecondaryDescription() {
        return false;
    }
}
