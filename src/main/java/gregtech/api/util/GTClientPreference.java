package gregtech.api.util;

import gregtech.common.config.Client;

public class GTClientPreference {

    private final boolean mSingleBlockInitialFilter;
    private final boolean mSingleBlockInitialMultiStack;
    private final boolean mInputBusInitialFilter;
    private final boolean wailaAverageNS;

    public GTClientPreference(boolean mSingleBlockInitialFilter, boolean mSingleBlockInitialMultiStack,
        boolean mInputBusInitialFilter, boolean wailaAverageNS) {
        this.mSingleBlockInitialFilter = mSingleBlockInitialFilter;
        this.mSingleBlockInitialMultiStack = mSingleBlockInitialMultiStack;
        this.mInputBusInitialFilter = mInputBusInitialFilter;
        this.wailaAverageNS = wailaAverageNS;
    }

    public GTClientPreference() {
        this.mSingleBlockInitialFilter = Client.preference.singleBlockInitialFilter;
        this.mSingleBlockInitialMultiStack = Client.preference.singleBlockInitialAllowMultiStack;
        this.mInputBusInitialFilter = Client.preference.inputBusInitialFilter;
        this.wailaAverageNS = Client.waila.wailaAverageNS;
    }

    public boolean isSingleBlockInitialFilterEnabled() {
        return mSingleBlockInitialFilter;
    }

    public boolean isSingleBlockInitialMultiStackEnabled() {
        return mSingleBlockInitialMultiStack;
    }

    public boolean isInputBusInitialFilterEnabled() {
        return mInputBusInitialFilter;
    }

    public boolean isWailaAverageNSEnabled() {
        return wailaAverageNS;
    }
}
