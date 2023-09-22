package gregtech.api.interfaces.metatileentity;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.BaseMetaTileEntity;

/**
 * Metrics Transmitter covers look for this interface for retrieving custom metrics for a machine. If this interface is
 * not present on the machine housing the cover, it will use {@link BaseMetaTileEntity#getInfoData()} to retrieve info
 * instead.
 */
public interface IMetricsExporter {

    /**
     * Attached metrics covers will call this method to receive reported metrics.
     * <p>
     * When reporting metrics, try to keep the number of entries small, and ordering of entries consistent. Advanced
     * Sensor Cards allow the user to selectively turn off individual lines using the panel's UI, and doing so is
     * aggravated by a metrics set that is inconsistent and/or large.
     *
     * @return A list of strings to print on the information panel the advanced sensor card is utilizing. Each item in
     *         the list will be printed on its own line.
     */
    @NotNull
    List<String> reportMetrics();
}
