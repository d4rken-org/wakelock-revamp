package eu.thedarken.wldonate.common.timber;


import com.bugsnag.android.BeforeNotify;

import eu.thedarken.wldonate.BuildConfig;
import eu.thedarken.wldonate.main.core.GeneralSettings;

public class BugsnagErrorHandler implements BeforeNotify {
    private final GeneralSettings settings;
    private final BugsnagTree bugsnagTree;
    private static final String TAB_APP = "app";

    public BugsnagErrorHandler(GeneralSettings settings, BugsnagTree bugsnagTree) {
        this.settings = settings;
        this.bugsnagTree = bugsnagTree;
    }

    @Override
    public boolean run(com.bugsnag.android.Error error) {
        if (!settings.isBugTrackingEnabled()) return false;

        bugsnagTree.update(error);

        error.addToTab(TAB_APP, "gitSha", BuildConfig.GITSHA);
        error.addToTab(TAB_APP, "buildTime", BuildConfig.BUILDTIME);

        return !BuildConfig.DEBUG;
    }
}
