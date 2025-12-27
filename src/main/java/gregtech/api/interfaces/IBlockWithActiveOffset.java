package gregtech.api.interfaces;

/**
 * For use with {@link gregtech.api.net.GTCoilStatus}, allows meta to be switched by the offset in case the machine
 * connected is active.
 */
public interface IBlockWithActiveOffset {

    int ACTIVE_OFFSET = 16;

    // can override this, but typically not necessary
    default int getActiveOffset() {
        return ACTIVE_OFFSET;
    }

}
