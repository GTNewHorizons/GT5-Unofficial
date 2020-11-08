package com.github.technus.tectech.mechanics.tesla;

import com.github.technus.tectech.mechanics.spark.ThaumSpark;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import static com.github.technus.tectech.util.Util.entriesSortedByValues;
import static java.lang.Math.sqrt;

public interface ITeslaConnectable extends ITeslaConnectableSimple {
    //Map with all Teslas in the same dimension and the distance to them //TODO Range
    HashMap<ITeslaConnectableSimple, Integer> teslaNodeMap = new HashMap<>();
    //ThaumCraft lighting coordinate pairs, so we can send them in bursts and save on lag
    HashSet<ThaumSpark> sparkList = new HashSet<>();

    //-128 to -1 disables capability
    //0 means any source or target
    //1 to 127 must match on source and target or source/target must be 0
    byte getTeslaTransmissionCapability();

    //Transmission Range is typically 16+ in blocks
    int getTeslaTransmissionRange();
    boolean isOverdriveEnabled();

    int getTeslaEnergyLossPerBlock();
    float getTeslaOverdriveLossCoefficient();

    long getTeslaOutputVoltage();
    long getTeslaOutputCurrent();

    boolean teslaDrainEnergy(long teslaVoltageDrained);

    class TeslaUtil {
        public static final HashSet<ITeslaConnectableSimple> teslaNodeSet = new HashSet<>();//Targets for power transmission

        public static void generateTeslaNodeMap(ITeslaConnectable origin) {
            origin.teslaNodeMap.clear();
            for (ITeslaConnectableSimple target : teslaNodeSet) {
                //Sanity checks
                if (target == null) {
                    //The Tesla Covers do not remove themselves from the list and this is the code that does
                    teslaNodeSet.remove(null);
                    continue;
                } else if (origin.equals(target) || !origin.getTeslaDimension().equals(target.getTeslaDimension())) {
                    //Skip if looking at myself and skip if not in the same dimension
                    //TODO, INTERDIM?
                    continue;
                } else if (origin.getTeslaTransmissionCapability() != 0 && origin.getTeslaReceptionCapability() != 0 &&
                        origin.getTeslaTransmissionCapability() != origin.getTeslaReceptionCapability()) {
                    //Skip if incompatible
                    continue;
                }

                //Range calc
                int distance = (int) sqrt(origin.getTeslaPosition().distanceSq(target.getTeslaPosition()));
                if (distance > origin.getTeslaTransmissionRange() * target.getTeslaReceptionCoefficient()) {
                    //Skip if the range is too vast
                    continue;
                }
                origin.teslaNodeMap.put(target, distance);
            }
        }

        public static void cleanTeslaNodeMap(ITeslaConnectable origin) {
            //Wipes all null objects, in practice this is unloaded or improperly removed tesla objects
            origin.teslaNodeMap.keySet().removeIf(Objects::isNull);
        }

        public static long powerTeslaNodeMap(ITeslaConnectable origin) {
            //Teslas can only send OR receive
            if (origin.isTeslaReadyToReceive()) {
                return 0L;//TODO Negative values to indicate charging?
            }
            long remainingAmperes = origin.getTeslaOutputCurrent();
            while (remainingAmperes > 0) {
                long startingAmperes = remainingAmperes;
                for (HashMap.Entry<ITeslaConnectableSimple, Integer> Rx : entriesSortedByValues(teslaNodeMap)) {
                    if (origin.getTeslaStoredEnergy() < (origin.isOverdriveEnabled() ? origin.getTeslaOutputVoltage() * 2 : origin.getTeslaOutputVoltage())) {
                        //Return and end the tick if we're out of energy to send
                        return origin.getTeslaOutputCurrent() - remainingAmperes;
                    }

                    ITeslaConnectableSimple target = Rx.getKey();

                    //Continue if the target can't receive
                    if(!target.isTeslaReadyToReceive()) continue;

                    int distance = Rx.getValue();

                    //Calculate the voltage output
                    long outputVoltageInjectable;
                    long outputVoltageConsumption;

                    if (origin.isOverdriveEnabled()) {
                        outputVoltageInjectable = origin.getTeslaOutputVoltage();
                        outputVoltageConsumption = origin.getTeslaOutputVoltage() + (distance * origin.getTeslaEnergyLossPerBlock()) +
                                (long) Math.round(origin.getTeslaOutputVoltage() * origin.getTeslaOverdriveLossCoefficient());
                    } else {
                        outputVoltageInjectable = origin.getTeslaOutputVoltage() - (distance * origin.getTeslaEnergyLossPerBlock());
                        outputVoltageConsumption = origin.getTeslaOutputVoltage();
                    }

                    //Skip the target if the cost is too high
                    if (origin.getTeslaStoredEnergy() < outputVoltageConsumption) {
                        continue;
                    }

                    if (target.teslaInjectEnergy(outputVoltageInjectable)) {
                        origin.teslaDrainEnergy(outputVoltageConsumption);
                        sparkList.add(new ThaumSpark(origin.getTeslaPosition(), target.getTeslaPosition(), origin.getTeslaDimension()));
                        remainingAmperes--;
                    }
                    if (remainingAmperes == 0) {
                        return origin.getTeslaOutputCurrent();
                    }
                }
                //End the tick after one iteration with no transmissions
                if (remainingAmperes == startingAmperes) {
                    return origin.getTeslaOutputCurrent() - remainingAmperes;
                }
            }
            return origin.getTeslaOutputCurrent() - remainingAmperes;
        }
    }
}
