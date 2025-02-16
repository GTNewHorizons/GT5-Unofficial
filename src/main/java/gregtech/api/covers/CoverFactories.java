package gregtech.api.covers;

import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverControlsWorkFactory;
import gregtech.common.covers.CoverEUMeter;
import gregtech.common.covers.CoverFacadeAEFactory;
import gregtech.common.covers.CoverFacadeBase;
import gregtech.common.covers.CoverFluidLimiter;
import gregtech.common.covers.CoverFluidRegulator;
import gregtech.common.covers.CoverFluidfilter;
import gregtech.common.covers.CoverItemFilter;
import gregtech.common.covers.CoverItemMeter;
import gregtech.common.covers.CoverLiquidMeter;
import gregtech.common.covers.CoverPowerPassUpgradeFactory;
import gregtech.common.covers.IntCoverFactory;
import gregtech.common.covers.SimpleCoverFactory;

public class CoverFactories {

    public static CoverFactory<ISerializableObject.LegacyCoverData> intCoverFactory = new IntCoverFactory();
    public static CoverFactory<ISerializableObject.LegacyCoverData> coverNoneFactory = new SimpleCoverFactory();
    public static CoverFactory<CoverFacadeBase.FacadeData> coverFacadeAEFactory = new CoverFacadeAEFactory();
    public static CoverFactory<CoverFluidRegulator.FluidRegulatorData> coverFluidRegulatorFactory = CoverFactory
        .ofData(CoverFluidRegulator.FluidRegulatorData::new);
    public static CoverFactory<CoverFluidfilter.FluidFilterData> coverFluidFilterFactory = CoverFactory
        .ofData(() -> new CoverFluidfilter.FluidFilterData(-1, 0));
    public static CoverFactory<CoverItemFilter.ItemFilterData> coverItemFilterFactory = CoverFactory
        .ofData(CoverItemFilter.ItemFilterData::new);
    public static CoverFactory<CoverFluidLimiter.FluidLimiterData> coverFluidLimiterFactory = CoverFactory
        .ofData(() -> new CoverFluidLimiter.FluidLimiterData(1F));
    public static CoverFactory<ISerializableObject.LegacyCoverData> coverControlsWorkFactory = new CoverControlsWorkFactory();
    public static CoverFactory<CoverLiquidMeter.LiquidMeterData> coverLiquidMeterFactory = CoverFactory
        .ofData(CoverLiquidMeter.LiquidMeterData::new);
    public static CoverFactory<CoverItemMeter.ItemMeterData> coverItemMeterFactory = CoverFactory
        .ofData(CoverItemMeter.ItemMeterData::new);
    public static CoverFactory<CoverEUMeter.EUMeterData> coverEUMeterFactory = CoverFactory
        .ofData(CoverEUMeter.EUMeterData::new);
    public static CoverFactory<ISerializableObject.LegacyCoverData> coverPowerPassUpgradeFactory = new CoverPowerPassUpgradeFactory();

}
