package gtPlusPlus.xmod.gregtech.api.metatileentity.custom.power;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.metatileentity.BaseCustomTileEntity;
import ic2.api.Direction;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class BaseCustomPower_MTE extends BaseCustomTileEntity {

	public long injectEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
		//Logger.INFO("Injecting Energy Units");
		
		if (mMetaTileEntity == null) {
			Logger.INFO("Bad Tile");
			
		}
		
		if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.inputEnergyFrom(aSide) && aAmperage > 0L
				&& aVoltage > 0L && this.getStoredEU() < this.getEUCapacity()
				&& this.mMetaTileEntity.maxAmperesIn() > this.mAcceptedAmperes) {
			Logger.INFO("Injecting Energy Units");
			if (aVoltage > this.getInputVoltage()) {
				this.doExplosion(aVoltage);
				return 0L;
			} else if (this
					.increaseStoredEnergyUnits(
							aVoltage * (aAmperage = Math
									.min(aAmperage,
											Math.min(this.mMetaTileEntity.maxAmperesIn() - this.mAcceptedAmperes,
													1L + (this.getEUCapacity() - this.getStoredEU()) / aVoltage))),
							true)) {
				Logger.INFO("Injecting Energy Units");
				this.mAverageEUInput[this.mAverageEUInputIndex] = (int) ((long) this.mAverageEUInput[this.mAverageEUInputIndex]
						+ aVoltage * aAmperage);
				this.mAcceptedAmperes += aAmperage;
				return aAmperage;
			} else {
				return 0L;
			}
		} else {
			Logger.INFO("canAccessData(): "+canAccessData());
			Logger.INFO("isElectric(): "+this.mMetaTileEntity.isElectric());
			Logger.INFO("InputEnergyFromSide("+aSide+"): "+this.inputEnergyFrom(aSide));
			Logger.INFO("aAmperage: "+aAmperage);
			Logger.INFO("aVoltage: "+aVoltage);
			Logger.INFO("this.getStoredEU() < this.getEUCapacity(): "+(this.getStoredEU() < this.getEUCapacity()));
			Logger.INFO("this.mMetaTileEntity.maxAmperesIn() > this.mAcceptedAmperes: "+(this.mMetaTileEntity.maxAmperesIn() > this.mAcceptedAmperes));
			return 0L;
		}
	}

	public boolean drainEnergyUnits(byte aSide, long aVoltage, long aAmperage) {
		Logger.INFO("Draining Energy Units");
		if (this.canAccessData() && this.mMetaTileEntity.isElectric() && this.outputsEnergyTo(aSide)
				&& this.getStoredEU() - aVoltage * aAmperage >= this.mMetaTileEntity.getMinimumStoredEU()) {
			if (this.decreaseStoredEU(aVoltage * aAmperage, false)) {
				this.mAverageEUOutput[this.mAverageEUOutputIndex] = (int) ((long) this.mAverageEUOutput[this.mAverageEUOutputIndex]
						+ aVoltage * aAmperage);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean decreaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooLessEnergy) {
		// TODO Auto-generated method stub
		return super.decreaseStoredEnergyUnits(aEnergy, aIgnoreTooLessEnergy);
	}

	@Override
	public boolean increaseStoredEnergyUnits(long aEnergy, boolean aIgnoreTooMuchEnergy) {
		// TODO Auto-generated method stub
		return super.increaseStoredEnergyUnits(aEnergy, aIgnoreTooMuchEnergy);
	}

	@Override
	public boolean inputEnergyFrom(byte aSide) {
		// TODO Auto-generated method stub
		return super.inputEnergyFrom(aSide);
	}

	@Override
	public boolean outputsEnergyTo(byte aSide) {
		// TODO Auto-generated method stub
		return super.outputsEnergyTo(aSide);
	}

	@Override
	public long getOutputAmperage() {
		// TODO Auto-generated method stub
		return super.getOutputAmperage();
	}

	@Override
	public long getOutputVoltage() {
		// TODO Auto-generated method stub
		return super.getOutputVoltage();
	}

	@Override
	public long getInputAmperage() {
		// TODO Auto-generated method stub
		return super.getInputAmperage();
	}

	@Override
	public long getInputVoltage() {
		// TODO Auto-generated method stub
		return super.getInputVoltage();
	}

	@Override
	public long getUniversalEnergyStored() {
		// TODO Auto-generated method stub
		return super.getUniversalEnergyStored();
	}

	@Override
	public long getUniversalEnergyCapacity() {
		// TODO Auto-generated method stub
		return super.getUniversalEnergyCapacity();
	}

	@Override
	public long getStoredEU() {
		// TODO Auto-generated method stub
		return super.getStoredEU();
	}

	@Override
	public long getEUCapacity() {
		// TODO Auto-generated method stub
		return super.getEUCapacity();
	}

	@Override
	public boolean setStoredEU(long aEnergy) {
		// TODO Auto-generated method stub
		return super.setStoredEU(aEnergy);
	}

	@Override
	public boolean decreaseStoredEU(long aEnergy, boolean aIgnoreTooLessEnergy) {
		// TODO Auto-generated method stub
		return super.decreaseStoredEU(aEnergy, aIgnoreTooLessEnergy);
	}

	@Override
	public boolean decreaseStoredSteam(long aEnergy, boolean aIgnoreTooLessEnergy) {
		// TODO Auto-generated method stub
		return super.decreaseStoredSteam(aEnergy, aIgnoreTooLessEnergy);
	}

	@Override
	public void doEnergyExplosion() {
		// TODO Auto-generated method stub
		super.doEnergyExplosion();
	}

	@Override
	public void doExplosion(long aAmount) {
		// TODO Auto-generated method stub
		super.doExplosion(aAmount);
	}

	@Override
	public byte getLightValue() {
		// TODO Auto-generated method stub
		return super.getLightValue();
	}

	@Override
	public long getAverageElectricInput() {
		// TODO Auto-generated method stub
		return super.getAverageElectricInput();
	}

	@Override
	public long getAverageElectricOutput() {
		// TODO Auto-generated method stub
		return super.getAverageElectricOutput();
	}

	@Override
	public double getOutputEnergyUnitsPerTick() {
		// TODO Auto-generated method stub
		return super.getOutputEnergyUnitsPerTick();
	}

	@Override
	public double demandedEnergyUnits() {
		// TODO Auto-generated method stub
		return super.demandedEnergyUnits();
	}

	@Override
	public double injectEnergyUnits(ForgeDirection aDirection, double aAmount) {
		// TODO Auto-generated method stub
		return super.injectEnergyUnits(aDirection, aAmount);
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity aEmitter, ForgeDirection aDirection) {
		// TODO Auto-generated method stub
		return super.acceptsEnergyFrom(aEmitter, aDirection);
	}

	@Override
	public boolean emitsEnergyTo(TileEntity aReceiver, ForgeDirection aDirection) {
		// TODO Auto-generated method stub
		return super.emitsEnergyTo(aReceiver, aDirection);
	}

	@Override
	public double getOfferedEnergy() {
		// TODO Auto-generated method stub
		return super.getOfferedEnergy();
	}

	@Override
	public void drawEnergy(double amount) {
		// TODO Auto-generated method stub
		super.drawEnergy(amount);
	}

	@Override
	public int injectEnergy(ForgeDirection aForgeDirection, int aAmount) {
		// TODO Auto-generated method stub
		return super.injectEnergy(aForgeDirection, aAmount);
	}

	@Override
	public int addEnergy(int aEnergy) {
		// TODO Auto-generated method stub
		return super.addEnergy(aEnergy);
	}

	@Override
	public boolean isAddedToEnergyNet() {
		// TODO Auto-generated method stub
		return super.isAddedToEnergyNet();
	}

	@Override
	public int demandsEnergy() {
		// TODO Auto-generated method stub
		return super.demandsEnergy();
	}

	@Override
	public int getMaxSafeInput() {
		// TODO Auto-generated method stub
		return super.getMaxSafeInput();
	}

	@Override
	public int getMaxEnergyOutput() {
		// TODO Auto-generated method stub
		return super.getMaxEnergyOutput();
	}

	@Override
	public int injectEnergy(Direction aDirection, int aAmount) {
		// TODO Auto-generated method stub
		return super.injectEnergy(aDirection, aAmount);
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity aReceiver, Direction aDirection) {
		// TODO Auto-generated method stub
		return super.acceptsEnergyFrom(aReceiver, aDirection);
	}

	@Override
	public boolean emitsEnergyTo(TileEntity aReceiver, Direction aDirection) {
		// TODO Auto-generated method stub
		return super.emitsEnergyTo(aReceiver, aDirection);
	}

	@Override
	public boolean isUniversalEnergyStored(long aEnergyAmount) {
		// TODO Auto-generated method stub
		return super.isUniversalEnergyStored(aEnergyAmount);
	}
	
}
