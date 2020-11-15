package com.github.technus.tectech.mechanics.tesla;

import com.github.technus.tectech.mechanics.spark.ThaumSpark;
import com.google.common.collect.Multimap;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.sqrt;

public interface ITeslaConnectable extends ITeslaConnectableSimple {
    //Map with all Teslas in the same dimension and the distance to them //TODO Range
    Multimap<Integer, ITeslaConnectableSimple> getTeslaNodeMap();

    //ThaumCraft lighting coordinate pairs, so we can send them in bursts and save on lag
    HashSet<ThaumSpark> getSparkList();

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
        private static final HashSet<ITeslaConnectableSimple> teslaSimpleNodeSet = new HashSet<>();//Targets for power transmission
        private static final HashSet<ITeslaConnectable> teslaNodeSet = new HashSet<>();//Sources of power transmission

        public static void teslaSimpleNodeSetAdd(ITeslaConnectableSimple target){
            teslaSimpleNodeSet.add(target);
            teslaNodeSet.forEach(origin -> addTargetToTeslaOrigin(target, origin));
        }

        public static void teslaSimpleNodeSetRemove(ITeslaConnectableSimple target){
            teslaSimpleNodeSet.remove(target);
            if (target instanceof ITeslaConnectable)teslaNodeSet.remove(target);
            teslaNodeSet.forEach(origin -> removeTargetFromTeslaOrigin(target, origin));
        }

        private static void addTargetToTeslaOrigin(ITeslaConnectableSimple target, ITeslaConnectable origin){
            if (origin.equals(target) || !origin.getTeslaDimension().equals(target.getTeslaDimension())) {
                //Skip if looking at myself and skip if not in the same dimension
                //TODO, INTERDIM?
                return;
            } else if (origin.getTeslaTransmissionCapability() != 0 && origin.getTeslaReceptionCapability() != 0 &&
                    origin.getTeslaTransmissionCapability() != origin.getTeslaReceptionCapability()) {
                //Skip if incompatible
                return;
            }
            //Range calc
            int distance = (int) sqrt(origin.getTeslaPosition().distanceSq(target.getTeslaPosition()));
            if (distance > origin.getTeslaTransmissionRange() * target.getTeslaReceptionCoefficient()) {
                //Skip if the range is too vast
                return;
            }
            origin.getTeslaNodeMap().put(distance, target);
        }

        private static void removeTargetFromTeslaOrigin(ITeslaConnectableSimple target, ITeslaConnectable origin){
            //Range calc TODO Remove duplicate?
            int distance = (int) sqrt(origin.getTeslaPosition().distanceSq(target.getTeslaPosition()));
            origin.getTeslaNodeMap().remove(distance, target);
        }

        public static void generateTeslaNodeMap(ITeslaConnectable origin) {
            if(!teslaNodeSet.contains(origin)) {
                origin.getTeslaNodeMap().clear();
                for (ITeslaConnectableSimple target : teslaSimpleNodeSet) {
                    //Sanity checks
                    if (target == null) {
                        //The Tesla Covers do not remove themselves from the list and this is the code that does
                        teslaSimpleNodeSet.remove(null);
                        continue;
                    }
                    addTargetToTeslaOrigin(target, origin);
                }
            }
            teslaNodeSet.add(origin);
        }

        public static long powerTeslaNodeMap(ITeslaConnectable origin) {
            //Teslas can only send OR receive
            if (origin.isTeslaReadyToReceive()) {
                return 0L;//TODO Negative values to indicate charging?
            }
            long remainingAmperes = origin.getTeslaOutputCurrent();
            while (remainingAmperes > 0) {
                long startingAmperes = remainingAmperes;
                for (Map.Entry<Integer, ITeslaConnectableSimple> Rx : origin.getTeslaNodeMap().entries()) {
                    if (origin.getTeslaStoredEnergy() < (origin.isOverdriveEnabled() ? origin.getTeslaOutputVoltage() * 2 : origin.getTeslaOutputVoltage())) {
                        //Return and end the tick if we're out of energy to send
                        return origin.getTeslaOutputCurrent() - remainingAmperes;
                    }

                    ITeslaConnectableSimple target = Rx.getValue();

                    //Continue if the target can't receive
                    if(!target.isTeslaReadyToReceive()) continue;

                    int distance = Rx.getKey();

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
                        origin.getSparkList().add(new ThaumSpark(origin.getTeslaPosition(), target.getTeslaPosition(), origin.getTeslaDimension()));
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
