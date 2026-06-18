package gregtech.api.util.shutdown;

import gregtech.api.util.IMachineMessage;

public interface ShutDownReason extends IMachineMessage<ShutDownReason> {

    /**
     * @return Whether the reason is critical.
     */
    boolean wasCritical();

    String getKey();
}
