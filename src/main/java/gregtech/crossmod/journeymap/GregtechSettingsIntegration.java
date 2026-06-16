package gregtech.crossmod.journeymap;

import java.util.ArrayList;
import java.util.List;

import gregtech.common.config.Client;
import journeymap.client.api.settings.DoubleBinding;
import journeymap.client.api.settings.ExternalSettingEntry;
import journeymap.client.api.settings.SettingsPageDefinition;
import journeymap.client.api.settings.SettingsPageRegistry;

public class GregtechSettingsIntegration {

    private static boolean registered;
    private static SettingsPageDefinition pageDefinition;

    public static void register() {
        if (registered) {
            return;
        }
        SettingsPageRegistry.getInstance()
            .registerPage(getPageDefinition());
        registered = true;
    }

    private static SettingsPageDefinition getPageDefinition() {
        if (pageDefinition == null) {
            pageDefinition = new SettingsPageDefinition(
                "gregtech",
                "gregtech.settings.page",
                "gregtech.settings.page.tooltip",
                120,
                createEntries());
        }
        return pageDefinition;
    }

    private static List<ExternalSettingEntry> createEntries() {
        List<ExternalSettingEntry> entries = new ArrayList<>();
        entries.add(
            ExternalSettingEntry.doubleSlider(
                "powerfail_map_icon_scale",
                "gregtech.settings.powerfail_map_icon_scale",
                "gregtech.settings.powerfail_map_icon_scale.tooltip",
                10,
                createScaleBinding(new ScaleAccessor() {

                    @Override
                    public double get() {
                        return Client.render.mapPowerfailIconScale;
                    }

                    @Override
                    public void set(double value) {
                        Client.render.mapPowerfailIconScale = Client.render.clampMapPowerfailScale(value);
                    }
                })));
        entries.add(
            ExternalSettingEntry.doubleSlider(
                "powerfail_map_label_scale",
                "gregtech.settings.powerfail_map_label_scale",
                "gregtech.settings.powerfail_map_label_scale.tooltip",
                20,
                createScaleBinding(new ScaleAccessor() {

                    @Override
                    public double get() {
                        return Client.render.mapPowerfailLabelScale;
                    }

                    @Override
                    public void set(double value) {
                        Client.render.mapPowerfailLabelScale = Client.render.clampMapPowerfailScale(value);
                    }
                })));
        return entries;
    }

    private static DoubleBinding createScaleBinding(ScaleAccessor accessor) {
        return new DoubleBinding() {

            @Override
            public double get() {
                return accessor.get();
            }

            @Override
            public void set(double value) {
                accessor.set(value);
            }

            @Override
            public double getDefaultValue() {
                return 1.0D;
            }

            @Override
            public double getMinValue() {
                return 0.1D;
            }

            @Override
            public double getMaxValue() {
                return 10.0D;
            }

            @Override
            public double getStep() {
                return 0.1D;
            }

            @Override
            public int getPrecision() {
                return 1;
            }

            @Override
            public String getSuffix() {
                return "x";
            }

            @Override
            public void commit() {
                Client.save();
            }
        };
    }

    private interface ScaleAccessor {

        double get();

        void set(double value);
    }
}
