package gregtech.api.util;

import gregtech.common.config.Client;

public class GTClientPreference {

    private final boolean mSingleBlockInitialFilter;
    private final boolean mSingleBlockInitialMultiStack;
    private final boolean mInputBusInitialFilter;
    private final boolean mInputHatchInitialFilter;
    private final boolean wailaAverageNS;
    private final boolean skipLSCWirelessWarningDialog;

    public GTClientPreference(boolean mSingleBlockInitialFilter, boolean mSingleBlockInitialMultiStack,
        boolean mInputBusInitialFilter, boolean wailaAverageNS, boolean mInputHatchInitialFilter,
        boolean skipLSCWirelessWarningDialog) {
        this.mSingleBlockInitialFilter = mSingleBlockInitialFilter;
        this.mSingleBlockInitialMultiStack = mSingleBlockInitialMultiStack;
        this.mInputBusInitialFilter = mInputBusInitialFilter;
        this.wailaAverageNS = wailaAverageNS;
        this.mInputHatchInitialFilter = mInputHatchInitialFilter;
        this.skipLSCWirelessWarningDialog = skipLSCWirelessWarningDialog;
    }

    public GTClientPreference() {
        this.mSingleBlockInitialFilter = Client.preference.singleBlockInitialFilter;
        this.mSingleBlockInitialMultiStack = Client.preference.singleBlockInitialAllowMultiStack;
        this.mInputBusInitialFilter = Client.preference.inputBusInitialFilter;
        this.wailaAverageNS = Client.waila.wailaAverageNS;
        this.mInputHatchInitialFilter = Client.preference.inputHatchInitialFilter;
        this.skipLSCWirelessWarningDialog = Client.preference.skipLSCWirelessWarningDialog;
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

    public boolean isInputHatchInitialFilterEnabled() {
        return mInputHatchInitialFilter;
    }

    public boolean isWailaAverageNSEnabled() {
        return wailaAverageNS;
    }

    public boolean isSkipLSCWirelessWarningDialogEnabled() {
        return skipLSCWirelessWarningDialog;
    }
}
