package com.github.technus.tectech.mechanics.tesla;

import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.github.technus.tectech.mechanics.spark.ThaumSpark;
import com.google.common.collect.Multimap;

public interface ITeslaConnectable extends ITeslaConnectableSimple {

    // Map with all Teslas in the same dimension and the distance to them //TODO Range
    Multimap<Integer, ITeslaConnectableSimple> getTeslaNodeMap();

    // ThaumCraft lighting coordinate pairs, so we can send them in bursts and save on lag
    HashSet<ThaumSpark> getSparkList();

    // -128 to -1 disables capability
    // 0 means any source or target
    // 1 to 127 must match on source and target or source/target must be 0
    byte getTeslaTransmissionCapability();

    // Transmission Range is typically 16+ in blocks
    int getTeslaTransmissionRange();

    boolean isOverdriveEnabled();

    int getTeslaEnergyLossPerBlock();

    float getTeslaOverdriveLossCoefficient();

    long getTeslaOutputVoltage();

    long getTeslaOutputCurrent();

    boolean teslaDrainEnergy(long teslaVoltageDrained);

    class TeslaUtil {

        private static final HashSet<ITeslaConnectableSimple> teslaSimpleNodeSet = new HashSet<>(); // Targets for power
                                                                                                    // transmission
        private static final HashSet<ITeslaConnectable> teslaNodeSet = new HashSet<>(); // Sources of power transmission
        private static final List<ITeslaConnectableSimple> scheduledRemove = new ArrayList<>();

        public static void teslaSimpleNodeSetAdd(ITeslaConnectableSimple target) {
            if (!teslaSimpleNodeSet.contains(target)) {
                teslaSimpleNodeSet.add(target);
                teslaNodeSet.forEach(origin -> addTargetToTeslaOrigin(target, origin));
            }
        }

        public static void teslaSimpleNodeSetRemove(ITeslaConnectableSimple target) {
            teslaSimpleNodeSet.remove(target);
            if (target instanceof ITeslaConnectable) teslaNodeSet.remove(target);
            teslaNodeSet.forEach(origin -> removeTargetFromTeslaOrigin(target, origin));
        }

        public static void teslaSimpleNodeSetRemoveScheduled(ITeslaConnectableSimple target) {
            scheduledRemove.add(target);
        }

        public static void housekeep() {
            for (ITeslaConnectableSimple e : scheduledRemove) {
                teslaSimpleNodeSet.remove(e);
            }
            scheduledRemove.clear();
        }

        private static void addTargetToTeslaOrigin(ITeslaConnectableSimple target, ITeslaConnectable origin) {
            if (origin.equals(target) || !origin.getTeslaDimension()
                .equals(target.getTeslaDimension())) {
                // Skip if looking at myself and skip if not in the same dimension
                // TODO, INTERDIM?
                return;
            } else if (origin.getTeslaTransmissionCapability() != 0 && origin.getTeslaReceptionCapability() != 0
                && origin.getTeslaTransmissionCapability() != origin.getTeslaReceptionCapability()) {
                    // Skip if incompatible
                    return;
                }
            // Range calc
            int distance = (int) sqrt(
                origin.getTeslaPosition()
                    .distanceSq(target.getTeslaPosition()));
            if (distance > origin.getTeslaTransmissionRange() * target.getTeslaReceptionCoefficient()) {
                // Skip if the range is too vast
                return;
            }
            origin.getTeslaNodeMap()
                .put(distance, target);
        }

        private static void removeTargetFromTeslaOrigin(ITeslaConnectableSimple target, ITeslaConnectable origin) {
            // Range calc TODO Remove duplicate?
            int distance = (int) sqrt(
                origin.getTeslaPosition()
                    .distanceSq(target.getTeslaPosition()));
            origin.getTeslaNodeMap()
                .remove(distance, target);
        }

        public static void generateTeslaNodeMap(ITeslaConnectable origin) {
            origin.getTeslaNodeMap()
                .clear();
            for (ITeslaConnectableSimple target : teslaSimpleNodeSet) {
                // Sanity checks
                if (target == null) {
                    // The Tesla Covers do not remove themselves from the list and this is the code that does
                    teslaSimpleNodeSet.remove(null);
                    continue;
                }
                addTargetToTeslaOrigin(target, origin);
            }
            teslaNodeSet.add(origin);
        }

        public static long powerTeslaNodeMap(ITeslaConnectable origin) {
            long remainingAmperes = origin.getTeslaOutputCurrent();
            boolean canSendPower = !origin.isTeslaReadyToReceive() && remainingAmperes > 0;

            if (canSendPower) {
                for (Map.Entry<Integer, ITeslaConnectableSimple> Rx : origin.getTeslaNodeMap()
                    .entries()) {
                    // Do we still have power left to send kind of check
                    if (origin.getTeslaStoredEnergy()
                        < (origin.isOverdriveEnabled() ? origin.getTeslaOutputVoltage() * 2
                            : origin.getTeslaOutputVoltage()))
                        break;
                    // Explicit words for the important fields
                    ITeslaConnectableSimple target = Rx.getValue();
                    int distance = Rx.getKey();
                    // Can our target receive energy?
                    if (!target.isTeslaReadyToReceive()) continue;

                    // Calculate the voltage output
                    long outputVoltageInjectable;
                    long outputVoltageConsumption;
                    if (origin.isOverdriveEnabled()) {
                        outputVoltageInjectable = origin.getTeslaOutputVoltage();
                        outputVoltageConsumption = origin.getTeslaOutputVoltage()
                            + ((long) distance * origin.getTeslaEnergyLossPerBlock())
                            + (long) Math
                                .round(origin.getTeslaOutputVoltage() * origin.getTeslaOverdriveLossCoefficient());
                    } else {
                        outputVoltageInjectable = origin.getTeslaOutputVoltage()
                            - ((long) distance * origin.getTeslaEnergyLossPerBlock());
                        outputVoltageConsumption = origin.getTeslaOutputVoltage();
                    }

                    // Break out of the loop if the cost is too high
                    // Since the next target will have an even higher cost, just quit now.
                    if (origin.getTeslaStoredEnergy() < outputVoltageConsumption) break;

                    // Now shove in as many packets as will fit~
                    while (canSendPower) {
                        if (target.teslaInjectEnergy(outputVoltageInjectable)) {
                            origin.teslaDrainEnergy(outputVoltageConsumption);
                            origin.getSparkList()
                                .add(
                                    new ThaumSpark(
                                        origin.getTeslaPosition(),
                                        target.getTeslaPosition(),
                                        origin.getTeslaDimension()));
                            remainingAmperes--;
                            // Update the can send power flag each time we send power
                            canSendPower = (origin.getTeslaStoredEnergy() < outputVoltageConsumption
                                || remainingAmperes > 0);
                        } else {
                            // Breaks out when I can't send anymore power
                            break;
                        }
                    }

                    // Break out if we can't send power anymore
                    if (!canSendPower) break;
                }
            }
            return origin.getTeslaOutputCurrent() - remainingAmperes;
        }
    }
}
