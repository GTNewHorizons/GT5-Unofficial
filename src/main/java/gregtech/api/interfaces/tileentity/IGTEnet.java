package gregtech.api.interfaces.tileentity;

public interface IGTEnet {

    /**
     * @return true if this Device consumes Energy at all
     */
    default boolean isEnetInput() {
        return false;
    }

    /**
     *
     * @return true if this Device emits Energy at all
     */
    default boolean isEnetOutput() {
        return false;
    }
}
