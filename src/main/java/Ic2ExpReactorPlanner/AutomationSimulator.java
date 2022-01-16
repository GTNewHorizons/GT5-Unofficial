package Ic2ExpReactorPlanner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Ic2ExpReactorPlanner.components.ReactorItem;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.GT_TileEntity_ComputerCube;

/**
 *
 * @author Brian McCloud
 */
public class AutomationSimulator {

	private final Reactor reactor;

	private final ArrayList<String> output;

	private final GT_TileEntity_ComputerCube mReactor;

	private final boolean[][] needsCooldown = new boolean[6][9];

	private final int initialHeat;

	private double minEUoutput = Double.MAX_VALUE;
	private double maxEUoutput = 0.0;
	private double minHeatOutput = Double.MAX_VALUE;
	private double maxHeatOutput = 0.0;

	private final int onPulseDuration;
	private final int offPulseDuration;
	private final int clockPeriod;
	private final int suspendTemp;
	private final int resumeTemp;
	private final int maxSimulationTicks;

	private boolean reachedBelow50;
	private boolean reachedBurn;
	private boolean reachedEvaporate;
	private boolean reachedHurt;
	private boolean reachedLava;
	private boolean reachedExplode;

	private boolean allFuelRodsDepleted = false;
	private boolean componentsIntact = true;
	private boolean anyRodsDepleted = false;

	private int activeTime = 0;
	private int inactiveTime = 0;
	private int currentActiveTime = 0;
	private int minActiveTime = Integer.MAX_VALUE;
	private int maxActiveTime = 0;
	private int currentInactiveTime = 0;
	private int minInactiveTime = Integer.MAX_VALUE;
	private int maxInactiveTime = 0;

	private double totalHullHeating = 0;
	private double totalComponentHeating = 0;
	private double totalHullCooling = 0;
	private double totalVentCooling = 0;

	private boolean showHeatingCoolingCalled = false;

	private boolean active = true;

	private int pauseTimer = 0;

	private int redstoneUsed = 0;

	private int lapisUsed = 0;


	private boolean completed = false;
	
	private boolean mRunning = false;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.##");

	private final SimulationData data = new SimulationData();
	public SimulationData getData() {
		if (completed) {
			return data;
		}
		return null;
	}

	public AutomationSimulator(final Reactor reactor, final ArrayList<String> output2, final GT_TileEntity_ComputerCube aTile) {
		this.reactor = reactor;
		this.output = output2;
		this.mReactor = aTile;
		this.initialHeat = (int) reactor.getCurrentHeat();
		this.onPulseDuration = reactor.getOnPulse();
		this.offPulseDuration = reactor.getOffPulse();
		this.clockPeriod = onPulseDuration + offPulseDuration;
		this.suspendTemp = reactor.getSuspendTemp();
		this.resumeTemp = reactor.getResumeTemp();
		this.maxSimulationTicks = reactor.getMaxSimulationTicks();
	}

	public void process() {

		mRunning = true;
		completed = false;
		long startTime = System.nanoTime();
		int reactorTicks = 0;
		int cooldownTicks = 0;
		int totalRodCount = 0;

		publish(""); // NOI18N
		publish("Simulation.Started");
		reactor.setCurrentHeat(initialHeat);
		reactor.clearVentedHeat();
		double minReactorHeat = initialHeat;
		double maxReactorHeat = initialHeat;
		reachedBelow50 = false;
		reachedBurn = initialHeat >= 0.4 * reactor.getMaxHeat();
		reachedEvaporate = initialHeat >= 0.5 * reactor.getMaxHeat();
		reachedHurt = initialHeat >= 0.7 * reactor.getMaxHeat();
		reachedLava = initialHeat >= 0.85 * reactor.getMaxHeat();
		reachedExplode = false;
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 9; col++) {
				ReactorItem component = reactor.getComponentAt(row, col);
				if (component != null) {
					component.clearCurrentHeat();
					component.clearDamage();
					totalRodCount += component.getRodCount();
				}
				publish(String.format("R%dC%d:0xC0C0C0", row, col)); // NOI18N
			}
		}
		data.totalRodCount = totalRodCount;
		double lastEUoutput = 0.0;
		double totalEUoutput = 0.0;
		double lastHeatOutput = 0.0;
		double totalHeatOutput = 0.0;
		double maxGeneratedHeat = 0.0;
		double explosionPower = 10.0;
		allFuelRodsDepleted = false;
		componentsIntact = true;
		anyRodsDepleted = false;
		Logger.INFO("Reactor Current Heat: "+reactor.getCurrentHeat());
		Logger.INFO("Reactor Max Heat: "+reactor.getMaxHeat());
		Logger.INFO("Least EU Output: "+lastEUoutput);
		Logger.INFO("Least Heat Output: "+lastHeatOutput);
		Logger.INFO("Reactor Max Ticks: "+maxSimulationTicks);
		Logger.INFO("All Fuel Depleted: "+allFuelRodsDepleted);
		Logger.INFO("Running: "+isRunning());
		Logger.INFO("Stopped: "+hasStopped());
		while (reactor.getCurrentHeat() < reactor.getMaxHeat() && (!allFuelRodsDepleted || lastEUoutput > 0 || lastHeatOutput > 0) && reactorTicks < maxSimulationTicks && isRunning()) {
			//Logger.INFO("Reactor Tick: "+reactorTicks);
			reactorTicks++;
			reactor.clearEUOutput();
			reactor.clearVentedHeat();
			for (int row = 0; row < 6; row++) {
				for (int col = 0; col < 9; col++) {
					ReactorItem component = reactor.getComponentAt(row, col);
					if (component != null) {
						component.preReactorTick();
					}
				}
			}
			if (active) {
				allFuelRodsDepleted = true; // assume rods depleted until one is
											// found that isn't.
			}
			double generatedHeat = 0.0;
			for (int row = 0; row < 6; row++) {
				for (int col = 0; col < 9; col++) {
					ReactorItem component = reactor.getComponentAt(row, col);
					if (component != null && !component.isBroken()) {
						if (allFuelRodsDepleted && component.getRodCount() > 0) {
							allFuelRodsDepleted = false;
						}
						if (active) {
							generatedHeat += component.generateHeat();
						}
						component.dissipate();
						component.transfer();
					}
				}
			}
			maxReactorHeat = Math.max(reactor.getCurrentHeat(), maxReactorHeat);
			minReactorHeat = Math.min(reactor.getCurrentHeat(), minReactorHeat);
			checkReactorTemperature(reactorTicks);
			maxGeneratedHeat = Math.max(generatedHeat, maxGeneratedHeat);
			if (active) {
				for (int row = 0; row < 6; row++) {
					for (int col = 0; col < 9; col++) {
						ReactorItem component = reactor.getComponentAt(row, col);
						if (component != null && !component.isBroken()) {
							component.generateEnergy();
						}
					}
				}
			}
			lastEUoutput = reactor.getCurrentEUoutput();
			totalEUoutput += lastEUoutput;
			lastHeatOutput = reactor.getVentedHeat();
			totalHeatOutput += lastHeatOutput;
			if (reactor.getCurrentHeat() <= reactor.getMaxHeat()) {
				if (reactor.isPulsed() || reactor.isAutomated()) {
					if (active) {
						activeTime++;
						currentActiveTime++;
						if (reactor.isPulsed() && (reactor.getCurrentHeat() >= suspendTemp || (reactorTicks % clockPeriod) >= onPulseDuration)) {
							active = false;
							minActiveTime = Math.min(currentActiveTime, minActiveTime);
							maxActiveTime = Math.max(currentActiveTime, maxActiveTime);
							currentActiveTime = 0;
						}
					}
					else {
						inactiveTime++;
						currentInactiveTime++;
						if (reactor.isAutomated() && pauseTimer > 0) {
							pauseTimer--;
						}
						else if ((reactor.isPulsed() && reactor.getCurrentHeat() <= resumeTemp && (reactorTicks % clockPeriod) < onPulseDuration)) {
							active = true;
							minInactiveTime = Math.min(currentInactiveTime, minInactiveTime);
							maxInactiveTime = Math.max(currentInactiveTime, maxInactiveTime);
							currentInactiveTime = 0;
						}
					}
				}
				minEUoutput = Math.min(lastEUoutput, minEUoutput);
				maxEUoutput = Math.max(lastEUoutput, maxEUoutput);
				minHeatOutput = Math.min(lastHeatOutput, minHeatOutput);
				maxHeatOutput = Math.max(lastHeatOutput, maxHeatOutput);
			}
			calculateHeatingCooling(reactorTicks);
			handleAutomation(reactorTicks);

		}

		if (hasStopped()) {
			publish("Simulation.CancelledAtTick", reactorTicks);
		}
		data.minTemp = (int) minReactorHeat;
		data.maxTemp = (int) maxReactorHeat;
		publish("Simulation.ReactorMinTemp", minReactorHeat);
		publish("Simulation.ReactorMaxTemp", maxReactorHeat);
		if (reactor.getCurrentHeat() < reactor.getMaxHeat()) {
			publish("Simulation.TimeWithoutExploding", reactorTicks);
			if (reactor.isPulsed()) {
				String rangeString = "";
				if (maxActiveTime > minActiveTime) {
					rangeString = rangeString("Simulation.ActiveTimeRange", minActiveTime, maxActiveTime);
				}
				else if (minActiveTime < activeTime) {
					rangeString = "Simulation.ActiveTimeSingle "+minActiveTime;
				}
				publish("Simulation.ActiveTime", activeTime, rangeString);
				rangeString = "";
				if (maxInactiveTime > minInactiveTime) {
					rangeString = rangeString("Simulation.InactiveTimeRange", minInactiveTime, maxInactiveTime);
				}
				else if (minInactiveTime < inactiveTime) {
					rangeString = "Simulation.InactiveTimeSingle " + minInactiveTime;
				}
				publish("Simulation.InactiveTime", inactiveTime, rangeString);
			}

			if (reactorTicks > 0) {
				data.totalReactorTicks = reactorTicks;
				if (reactor.isFluid()) {
					data.totalHUoutput = (int) (40 * totalHeatOutput);
					data.avgHUoutput = (int) (2 * totalHeatOutput / reactorTicks);
					data.minHUoutput = 2 * minHeatOutput;
					data.maxHUoutput = (int) (2 * maxHeatOutput);
					if (totalHeatOutput > 0) {
						publish("Simulation.HeatOutputs", DECIMAL_FORMAT.format(40 * totalHeatOutput), DECIMAL_FORMAT.format(2 * totalHeatOutput / reactorTicks), DECIMAL_FORMAT.format(2
								* minHeatOutput), DECIMAL_FORMAT.format(2 * maxHeatOutput));
						if (totalRodCount > 0) {
							publish("Simulation.Efficiency", totalHeatOutput / reactorTicks / 4 / totalRodCount, minHeatOutput / 4 / totalRodCount, maxHeatOutput / 4 / totalRodCount);
						}
					}
				}
				else {
					data.totalEUoutput = (int) totalEUoutput;
					data.avgEUoutput = MathUtils.roundToClosestInt(Math.ceil(totalEUoutput / (reactorTicks * 20)));
					data.minEUoutput = minEUoutput / 20.0;
					data.maxEUoutput = (int) (maxEUoutput / 20.0);
					if (totalEUoutput > 0) {
						publish("Simulation.EUOutputs", DECIMAL_FORMAT.format(totalEUoutput), DECIMAL_FORMAT.format(totalEUoutput / (reactorTicks * 20)), DECIMAL_FORMAT.format(minEUoutput
								/ 20.0), DECIMAL_FORMAT.format(maxEUoutput / 20.0));
						if (totalRodCount > 0) {
							publish("Simulation.Efficiency", totalEUoutput / reactorTicks / 100 / totalRodCount, minEUoutput / 100 / totalRodCount, maxEUoutput / 100 / totalRodCount);
						}
					}
				}
			}

			if (reactor.getCurrentHeat() > 0.0) {
				publish("Simulation.ReactorRemainingHeat", reactor.getCurrentHeat());
			}
			double prevReactorHeat = reactor.getCurrentHeat();
			double prevTotalComponentHeat = 0.0;
			for (int row = 0; row < 6; row++) {
				for (int col = 0; col < 9; col++) {
					ReactorItem component = reactor.getComponentAt(row, col);
					if (component != null && !component.isBroken()) {
						if (component.getCurrentHeat() > 0.0) {
							prevTotalComponentHeat += component.getCurrentHeat();
							publish(String.format("R%dC%d:0xFFA500", row, col)); // NOI18N
							component.info.append("ComponentInfo.RemainingHeat " + component.getCurrentHeat());
						}
					}
				}
			}
			if (prevReactorHeat == 0.0 && prevTotalComponentHeat == 0.0) {
				publish("Simulation.NoCooldown");
			}
			else if (reactor.getCurrentHeat() < reactor.getMaxHeat()) {
				double currentTotalComponentHeat = prevTotalComponentHeat;
				int reactorCooldownTime = 0;
				do {
					reactor.clearVentedHeat();
					prevReactorHeat = reactor.getCurrentHeat();
					if (prevReactorHeat == 0.0) {
						reactorCooldownTime = cooldownTicks;
					}
					prevTotalComponentHeat = currentTotalComponentHeat;
					for (int row = 0; row < 6; row++) {
						for (int col = 0; col < 9; col++) {
							ReactorItem component = reactor.getComponentAt(row, col);
							if (component != null && !component.isBroken()) {
								component.dissipate();
								component.transfer();
							}
						}
					}
					lastHeatOutput = reactor.getVentedHeat();
					totalHeatOutput += lastHeatOutput;
					minEUoutput = Math.min(lastEUoutput, minEUoutput);
					maxEUoutput = Math.max(lastEUoutput, maxEUoutput);
					minHeatOutput = Math.min(lastHeatOutput, minHeatOutput);
					maxHeatOutput = Math.max(lastHeatOutput, maxHeatOutput);
					cooldownTicks++;
					currentTotalComponentHeat = 0.0;
					for (int row = 0; row < 6; row++) {
						for (int col = 0; col < 9; col++) {
							ReactorItem component = reactor.getComponentAt(row, col);
							if (component != null && !component.isBroken()) {
								currentTotalComponentHeat += component.getCurrentHeat();
								if (component.getCurrentHeat() == 0.0 && needsCooldown[row][col]) {
									component.info.append("ComponentInfo.CooldownTime " + cooldownTicks);
									needsCooldown[row][col] = false;
								}
							}
						}
					}
				}
				while (lastHeatOutput > 0 && cooldownTicks < 50000);
				if (reactor.getCurrentHeat() < reactor.getMaxHeat()) {
					if (reactor.getCurrentHeat() == 0.0) {
						publish("Simulation.ReactorCooldownTime", reactorCooldownTime);
					}
					else if (reactorCooldownTime > 0) {
						publish("Simulation.ReactorResidualHeat", reactor.getCurrentHeat(), reactorCooldownTime);
					}
					publish("Simulation.TotalCooldownTime", cooldownTicks);
				}
			}
		}
		else {
			publish("Simulation.ReactorOverheatedTime", reactorTicks);
			explosionPower = 10.0;
			double explosionPowerMult = 1.0;
			for (int row = 0; row < 6; row++) {
				for (int col = 0; col < 9; col++) {
					ReactorItem component = reactor.getComponentAt(row, col);
					if (component != null) {
						explosionPower += component.getExplosionPowerOffset();
						explosionPowerMult *= component.getExplosionPowerMultiplier();
					}
				}
			}
			explosionPower *= explosionPowerMult;
			publish("Simulation.ExplosionPower", explosionPower);
		}
		double totalEffectiveVentCooling = 0.0;
		double totalVentCoolingCapacity = 0.0;
		double totalCellCooling = 0.0;
		double totalCondensatorCooling = 0.0;

		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 9; col++) {
				ReactorItem component = reactor.getComponentAt(row, col);
				if (component != null) {
					if (component.getVentCoolingCapacity() > 0) {
						component.info.append("ComponentInfo.UsedCooling " + component.getBestVentCooling() + " | " + component.getVentCoolingCapacity());
						totalEffectiveVentCooling += component.getBestVentCooling();
						totalVentCoolingCapacity += component.getVentCoolingCapacity();
					}
					else if (component.getBestCellCooling() > 0) {
						component.info.append("ComponentInfo.ReceivedHeat " + component.getBestCellCooling());
						totalCellCooling += component.getBestCellCooling();
					}
					else if (component.getBestCondensatorCooling() > 0) {
						component.info.append("ComponentInfo.ReceivedHeat " + component.getBestCondensatorCooling());
						totalCondensatorCooling += component.getBestCondensatorCooling();
					}
					else if (component.getMaxHeatGenerated() > 0) {
						if (!reactor.isFluid() && component.getMaxEUGenerated() > 0) {
							component.info.append("ComponentInfo.GeneratedEU " + component.getMinEUGenerated() + " | " + component.getMaxEUGenerated());
						}
						component.info.append("ComponentInfo.GeneratedHeat " + component.getMinHeatGenerated() + " | " + component.getMaxHeatGenerated());
					}
					if (component.getMaxReachedHeat() > 0) {
						component.info.append("ComponentInfo.ReachedHeat " + component.getMaxReachedHeat() + " | " + component.getMaxHeat());
					}
				}
			}
		}

		// if (totalVentCoolingCapacity > 0) {
		// publish("Simulation.TotalVentCooling",
		// totalEffectiveVentCooling, totalVentCoolingCapacity);
		// }
		showHeatingCooling(reactorTicks); // Call to show this info in case it
											// hasn't already been shown, such
											// as for an automated reactor.
		if (totalCellCooling > 0) {
			publish("Simulation.TotalCellCooling", totalCellCooling);
		}
		if (totalCondensatorCooling > 0) {
			publish("Simulation.TotalCondensatorCooling", totalCondensatorCooling);
		}
		if (maxGeneratedHeat > 0) {
			publish("Simulation.MaxHeatGenerated", maxGeneratedHeat);
		}
		if (redstoneUsed > 0) {
			publish("Simulation.RedstoneUsed", redstoneUsed);
		}
		if (lapisUsed > 0) {
			publish("Simulation.LapisUsed", lapisUsed);
		}
		// double totalCooling = totalEffectiveVentCooling + totalCellCooling +
		// totalCondensatorCooling;
		// if (totalCooling >= maxGeneratedHeat) {
		// publish("Simulation.ExcessCooling", totalCooling -
		// maxGeneratedHeat);
		// } else {
		// publish("Simulation.ExcessHeating", maxGeneratedHeat -
		// totalCooling);
		// }
		// return null;

		/* catch (Throwable e) {
		if (cooldownTicks == 0) {
			publish("Simulation.ErrorReactor", reactorTicks);
		} else {
			publish("Simulation.ErrorCooldown", cooldownTicks);
		}
		publish(e.toString(), " ", Arrays.toString(e.getStackTrace()); // NO18N
		
		}*/
		data.explosionPower = (int) explosionPower;
		data.totalReactorTicks = reactorTicks;
		long endTime = System.nanoTime();
		publish("Simulation.ElapsedTime", (endTime - startTime) / 1e9);
		mRunning = false;
		completed = true;
	}


	public boolean hasStopped() {
		return !mRunning;
	}
	
	public boolean isRunning() {
		return mRunning;
	}

	private void handleAutomation(final int reactorTicks) {
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 9; col++) {
				ReactorItem component = reactor.getComponentAt(row, col);
				if (component != null && reactor.isAutomated()) {
					if (component.getMaxHeat() > 1) {
						if (component.getAutomationThreshold() > component.getInitialHeat() && component.getCurrentHeat() >= component.getAutomationThreshold()) {
							component.clearCurrentHeat();
							component.info.append("ComponentInfo.ReplacedTime | " + reactorTicks);
							if (component.getReactorPause() > 0) {
								active = false;
								pauseTimer = Math.max(pauseTimer, component.getReactorPause());
								minActiveTime = Math.min(currentActiveTime, minActiveTime);
								maxActiveTime = Math.max(currentActiveTime, maxActiveTime);
								currentActiveTime = 0;
							}
						}
						else if (component.getAutomationThreshold() < component.getInitialHeat() && component.getCurrentHeat() <= component.getAutomationThreshold()) {
							component.clearCurrentHeat();
							component.info.append("ComponentInfo.ReplacedTime | " +reactorTicks);
							if (component.getReactorPause() > 0) {
								active = false;
								pauseTimer = Math.max(pauseTimer, component.getReactorPause());
								minActiveTime = Math.min(currentActiveTime, minActiveTime);
								maxActiveTime = Math.max(currentActiveTime, maxActiveTime);
								currentActiveTime = 0;
							}
						}
					}
					else if (component.isBroken() || (component.getMaxDamage() > 1 && component.getCurrentDamage() >= component.getAutomationThreshold())) {
						component.clearDamage();
						component.info.append("ComponentInfo.ReplacedTime | " +reactorTicks);
						if (component.getReactorPause() > 0) {
							active = false;
							pauseTimer = Math.max(pauseTimer, component.getReactorPause());
							minActiveTime = Math.min(currentActiveTime, minActiveTime);
							maxActiveTime = Math.max(currentActiveTime, maxActiveTime);
							currentActiveTime = 0;
						}
					}
				}
				if (reactor.isUsingReactorCoolantInjectors() && component != null && component.needsCoolantInjected()) {
					component.injectCoolant();
					if ("rshCondensator".equals(component.baseName)) {
						redstoneUsed++;
					}
					else if ("lzhCondensator".equals(component.baseName)) {
						lapisUsed++;
					}
				}
			}
		}
	}

	private void checkReactorTemperature(final int reactorTicks) {
		if (reactor.getCurrentHeat() < 0.5 * reactor.getMaxHeat() && !reachedBelow50 && reachedEvaporate) {
			publish("Simulation.TimeToBelow50", reactorTicks);
			reachedBelow50 = true;
			data.timeToBelow50 = reactorTicks;
		}
		if (reactor.getCurrentHeat() >= 0.4 * reactor.getMaxHeat() && !reachedBurn) {
			publish("Simulation.TimeToBurn", reactorTicks);
			reachedBurn = true;
			data.timeToBurn = reactorTicks;
		}
		if (reactor.getCurrentHeat() >= 0.5 * reactor.getMaxHeat() && !reachedEvaporate) {
			publish("Simulation.TimeToEvaporate", reactorTicks);
			reachedEvaporate = true;
			data.timeToEvaporate = reactorTicks;
		}
		if (reactor.getCurrentHeat() >= 0.7 * reactor.getMaxHeat() && !reachedHurt) {
			publish("Simulation.TimeToHurt", reactorTicks);
			reachedHurt = true;
			data.timeToHurt = reactorTicks;
		}
		if (reactor.getCurrentHeat() >= 0.85 * reactor.getMaxHeat() && !reachedLava) {
			publish("Simulation.TimeToLava", reactorTicks);
			reachedLava = true;
			data.timeToLava = reactorTicks;
		}
		if (reactor.getCurrentHeat() >= reactor.getMaxHeat() && !reachedExplode) {
			publish("Simulation.TimeToXplode", reactorTicks);
			reachedExplode = true;
			data.timeToXplode = reactorTicks;
		}
	}

	private void calculateHeatingCooling(final int reactorTicks) {
		if (reactorTicks > 20) {
			for (int row = 0; row < 6; row++) {
				for (int col = 0; col < 9; col++) {
					ReactorItem component = reactor.getComponentAt(row, col);
					if (component != null) {
						totalHullHeating += component.getCurrentHullHeating();
						totalComponentHeating += component.getCurrentComponentHeating();
						totalHullCooling += component.getCurrentHullCooling();
						totalVentCooling += component.getCurrentVentCooling();
					}
				}
			}
		}
	}

	private void showHeatingCooling(final int reactorTicks) {
		if (!showHeatingCoolingCalled) {
			showHeatingCoolingCalled = true;
			if (reactorTicks >= 40) {
				double totalHullCoolingCapacity = 0;
				double totalVentCoolingCapacity = 0;
				for (int row = 0; row < 6; row++) {
					for (int col = 0; col < 9; col++) {
						ReactorItem component = reactor.getComponentAt(row, col);
						if (component != null) {
							totalHullCoolingCapacity += component.getHullCoolingCapacity();
							totalVentCoolingCapacity += component.getVentCoolingCapacity();
						}
					}
				}
				data.hullHeating = totalHullHeating / (reactorTicks - 20);
				data.componentHeating = totalComponentHeating / (reactorTicks - 20);
				data.hullCooling = totalHullCooling / (reactorTicks - 20);
				data.hullCoolingCapacity = totalHullCoolingCapacity;
				data.ventCooling = totalVentCooling / (reactorTicks - 20);
				data.ventCoolingCapacity = totalVentCoolingCapacity;
				if (totalHullHeating > 0) {
					publish("Simulation.HullHeating", totalHullHeating / (reactorTicks - 20));
				}
				if (totalComponentHeating > 0) {
					publish("Simulation.ComponentHeating", totalComponentHeating / (reactorTicks - 20));
				}
				if (totalHullCoolingCapacity > 0) {
					publish("Simulation.HullCooling | " +totalHullCooling / (reactorTicks - 20), totalHullCoolingCapacity);
				}
				if (totalVentCoolingCapacity > 0) {
					publish("Simulation.VentCooling | " +totalVentCooling / (reactorTicks - 20), totalVentCoolingCapacity);
				}
			}
		}
	}
	
	private void publish(String string, double currentHeat, int reactorCooldownTime) {
		publish(string + " | "+currentHeat+" | "+reactorCooldownTime);
	}

	private void publish(String string, double d, double e, double f) {
		publish(string + " | "+d+" | "+e+" | "+f);
	}

	private void publish(String string, String format, String format2, String format3, String format4) {
		publish(string + " | "+format+" | "+format2+" | "+format3+" | "+format4);
	}

	private void publish(String string, int activeTime2, String rangeString) {
		publish(string + " | "+activeTime2+" | "+rangeString);
	}

	private void publish(String aString, double aData) {
		publish(aString+":"+aData);
	}

	private void publish(String aString, long aData) {
		publish(aString+":"+aData);
	}
	
	private void publish(String aString) {
		output.add(aString);
	}	

	private String rangeString(String string, int aMin, int aMax) {
		return string+" ("+aMin+"-"+aMax+")";		
	}


	protected void process(List<String> chunks) {
		/*
		for (String chunk : chunks) {
		if (chunk.isEmpty()) {
		output.add(""); // NO18N
		}
		else {
		if (chunk.matches("R\\dC\\d:.*")) { // NO18N
		String temp = chunk.substring(5);
		int row = chunk.charAt(1) - '0';
		int col = chunk.charAt(3) - '0';
		if (temp.startsWith("0x")) { // NO18N
		mReactorComponents[row][col].setBackground(Color.decode(temp));
		if ("0xC0C0C0".equals(temp)) {
		mReactorComponents[row][col].setToolTipText(null);
		}
		else if ("0xFF0000".equals(temp)) {
		mReactorComponents[row][col].setToolTipText(getI18n("ComponentTooltip.Broken"));
		}
		else if ("0xFFA500".equals(temp)) {
		mReactorComponents[row][col].setToolTipText(getI18n("ComponentTooltip.ResidualHeat"));
		}
		}
		}
		else {
		output.add(chunk);
		}
		}
		}
		*/
		}

	public void cancel() {
		Logger.INFO("Stopping Simulation.");
		mRunning = false;
		completed = true;		
	}
	

}
