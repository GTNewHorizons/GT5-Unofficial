package gregtech.api.util;

public class GT_ClientPreference {

    private final boolean mSingleBlockInitialFilter;
    private final boolean mSingleBlockInitialMultiStack;
    private final boolean mInputBusInitialFilter;
    private final boolean wailaAverageNS;

    public GT_ClientPreference(boolean mSingleBlockInitialFilter, boolean mSingleBlockInitialMultiStack,
        boolean mInputBusInitialFilter, boolean wailaAverageNS) {
        this.mSingleBlockInitialFilter = mSingleBlockInitialFilter;
        this.mSingleBlockInitialMultiStack = mSingleBlockInitialMultiStack;
        this.mInputBusInitialFilter = mInputBusInitialFilter;
        this.wailaAverageNS = wailaAverageNS;
    }

    public GT_ClientPreference(GT_Config aClientDataFile) {
        this.mSingleBlockInitialFilter = aClientDataFile.get("preference", "mSingleBlockInitialFilter", false);
        this.mSingleBlockInitialMultiStack = aClientDataFile
            .get("preference", "mSingleBlockInitialAllowMultiStack", false);
        this.mInputBusInitialFilter = aClientDataFile.get("preference", "mInputBusInitialFilter", true);
        this.wailaAverageNS = aClientDataFile.get("waila", "WailaAverageNS", false);
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
