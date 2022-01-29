package gtPlusPlus.xmod.gregtech.common.computer;

import java.util.ArrayList;
import java.util.HashMap;

import Ic2ExpReactorPlanner.AutomationSimulator;
import Ic2ExpReactorPlanner.Reactor;
import Ic2ExpReactorPlanner.SimulationData;
import Ic2ExpReactorPlanner.components.ReactorItem;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.GT_TileEntity_ComputerCube;

public class GT_Computercube_Simulator {

	private static final HashMap<Integer, Pair<Integer, Integer>> sSlotPositions = new HashMap<Integer, Pair<Integer, Integer>>();

	static {
		int aSlot = 4;
		for (int column = 0; column < 6; column++) {
			for (int row = 0; row < 9; row++) {
				sSlotPositions.put(aSlot++, new Pair<Integer, Integer>(row, column));				
			}
		}
	}

	private final Reactor reactor = new Reactor();

	public AutomationSimulator simulator = null;
	/**
	 * The reactor that was last simulated.
	 */
	public Reactor simulatedReactor = null;    

	private String currentReactorCode = null;

	private String currentReactorOldCode = null;

	private ArrayList<String> output = new ArrayList<String>();

	private final GT_TileEntity_ComputerCube mTile;

	public GT_Computercube_Simulator(GT_TileEntity_ComputerCube aTile) {
		mTile = aTile;
	}

	public void slotClick(int aSlot, GT_ItemStack aStack) {

		/*if (selection != null) {
		    componentToPlace = ComponentFactory.createComponent(selection.getActionCommand());
		    if (componentToPlace != null) {
		        componentToPlace.setInitialHeat(((Number)componentHeatSpinner.getValue()).intValue());
		        componentToPlace.setAutomationThreshold(((Number)placingThresholdSpinner.getValue()).intValue());
		        componentToPlace.setReactorPause(((Number)placingReactorPauseSpinner.getValue()).intValue());
		    }
		}*/
		if (aSlot >= 4 && aSlot < 58) {
			Pair<Integer, Integer> aSpot = sSlotPositions.get(aSlot);
			ReactorItem aItem;
			if (aStack == null) {
				aItem = null;
			}
			else {
				
				Logger.INFO("Using lookup key: "+ItemUtils.getModId(aStack.toStack())+"."+aStack.mItem.getUnlocalizedName()+"."+aStack.mMetaData);
				aItem = ReactorItem.sComponentMap.get(ItemUtils.getModId(aStack.toStack())+"."+aStack.mItem.getUnlocalizedName()+"."+aStack.mMetaData);

			}
			int aRow = aSpot.getKey();
			int aColumn = aSpot.getValue();
			Logger.INFO("Putting "+(aItem == null ? "null" : aItem.name)+" at x:"+aRow+", y:"+aColumn);
			reactor.setComponentAt(aColumn, aRow, aItem);
			currentReactorCode = reactor.getCode();
			currentReactorOldCode = reactor.getOldCode();
			Logger.INFO("Code: "+currentReactorCode);
		}
		//maxHeatLabel.setText(formatI18n("UI.MaxHeatSpecific", reactor.getMaxHeat()));
		//heatSpinnerModel.setMaximum(reactor.getMaxHeat() - 1);
		//heatSpinnerModel.setValue(Math.min(((Number)heatSpinnerModel.getValue()).intValue(), reactor.getMaxHeat() - 1));
		//temperatureEffectsLabel.setText(formatI18n("UI.TemperatureEffectsSpecific", (int) (reactor.getMaxHeat() * 0.4), (int) (reactor.getMaxHeat() * 0.5), (int) (reactor.getMaxHeat() * 0.7), (int) (reactor.getMaxHeat() * 0.85), (int) (reactor.getMaxHeat() * 1.0)));
	}

	public void simulate() {
		/*if (Utils.isClient()) {
			return;
		}*/		
		if (simulator != null && simulator.isRunning()) {	
			Logger.INFO("Simulator Running, Stopping.");
			simulator.cancel();
		}
		Logger.INFO("Starting Simulator.");
		mTile.mHeat = 0;
		mTile.mEU = 0;
		currentReactorCode = reactor.getCode();
		currentReactorOldCode = reactor.getOldCode();
		output.clear();
		simulatedReactor = new Reactor();
		simulatedReactor.setCode(reactor.getCode());
		Logger.INFO("Making new AutomationSimulator.");
		simulator = new AutomationSimulator(simulatedReactor, output, mTile);
		Logger.INFO("Starting AutomationSimulator.process().");
		simulator.process();
		Logger.INFO("Done.");
		
		SimulationData aData = simulator.getData();
		if (aData != null && aData.totalReactorTicks > 0) {
			mTile.mEU = aData.avgEUoutput * aData.totalReactorTicks;
			mTile.mEUOut = aData.avgEUoutput;
			mTile.mHeat = aData.avgHUoutput;
			mTile.mMaxHeat = aData.maxHUoutput;
			mTile.mExplosionStrength = aData.explosionPower;
			mTile.mHEM = (float) aData.hullHeating;
			mTile.mProgress = aData.totalReactorTicks;
		}
		
		for (String s : output) {
			Logger.INFO("	"+s);			
		}
	}

	public ArrayList<String> getOutputData() {
		return output;
	}

	private void clearGrid() {
		reactor.clearGrid();
		/*for (int i = 0; i < reactorButtons.length; i++) {
		    for (int j = 0; j < reactorButtons[i].length; j++) {
		        reactorButtons[i][j].setIcon(null);
		        reactorButtons[i][j].setToolTipText(null);
		        reactorButtonPanels[i][j].setBackground(Color.LIGHT_GRAY);
		    }
		}*/
		output.clear();
		/*materialsArea.setText(reactor.getMaterials().toString());
		componentListArea.setText(reactor.getComponentList().toString());
		maxHeatLabel.setText(formatI18n("UI.MaxHeatSpecific", reactor.getMaxHeat()));
		heatSpinnerModel.setMaximum(reactor.getMaxHeat() - 1);
		heatSpinnerModel.setValue(Math.min(((Number) heatSpinnerModel.getValue()).intValue(), reactor.getMaxHeat() - 1));
		temperatureEffectsLabel.setText(formatI18n("UI.TemperatureEffectsSpecific", (int)(reactor.getMaxHeat() * 0.4), (int)(reactor.getMaxHeat() * 0.5), (int)(reactor.getMaxHeat() * 0.7), (int)(reactor.getMaxHeat() * 0.85), (int)(reactor.getMaxHeat() * 1.0)));
		lockCode = true;
		codeField.setText(null);
		lockCode = false;*/
	}


}
