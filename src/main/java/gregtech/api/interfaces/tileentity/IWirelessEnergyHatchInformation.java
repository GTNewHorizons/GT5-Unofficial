package gregtech.api.interfaces.tileentity;

public interface IWirelessEnergyHatchInformation {

    // This interface is solely for usage by wireless hatches/dynamos.

    // Ticks between energy additions to the hatch. For a dynamo this is how many ticks between energy being consumed
    // and added to the global energy map.
    long ticks_between_energy_addition = 100L * 20L;

    // Total number of energy additions this multi can store before it is full.
    long number_of_energy_additions = 4L;

    default long totalStorage(long tier_eu_per_tick) {
        return tier_eu_per_tick * ticks_between_energy_addition * number_of_energy_additions;
    }
}
