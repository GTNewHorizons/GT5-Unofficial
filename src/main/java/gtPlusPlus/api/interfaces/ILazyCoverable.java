package gtPlusPlus.api.interfaces;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface ILazyCoverable extends ICoverable {

	@Override
	default byte getColorization() {
		return 0;
	}

	@Override
	default byte setColorization(byte arg0) {
		return 0;
	}

	@Override
	default byte getInputRedstoneSignal(byte arg0) {
		return 0;
	}

	@Override
	default byte getStrongestRedstone() {
		return 0;
	}

	@Override
	default boolean getRedstone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	default boolean getRedstone(byte arg0) {
		return false;
	}

	@Override
	default boolean isUniversalEnergyStored(long arg0) {
		return false;
	}

	@Override
	default long getUniversalEnergyStored() {
		return 0;
	}

	@Override
	default long getUniversalEnergyCapacity() {
		return 0;
	}

	@Override
	default long getStoredSteam() {
		return 0;
	}

	@Override
	default long getSteamCapacity() {
		return 0;
	}

	@Override
	default boolean increaseStoredSteam(long arg0, boolean arg2) {
		return false;
	}

	@Override
	default byte getOutputRedstoneSignal(byte arg0) {
		return 0;
	}

	@Override
	default void setOutputRedstoneSignal(byte arg0, byte arg1) {
		
	}

	@Override
	default byte getStrongOutputRedstoneSignal(byte arg0) {
		return 0;
	}

	@Override
	default void setStrongOutputRedstoneSignal(byte arg0, byte arg1) {
		
	}

	@Override
	default byte getComparatorValue(byte arg0) {
		return 0;
	}

	@Override
	default IGregTechTileEntity getIGregTechTileEntity(int arg0, int arg1, int arg2) {
		return null;
	}

	@Override
	default IGregTechTileEntity getIGregTechTileEntityOffset(int arg0, int arg1, int arg2) {
		return null;
	}

	@Override
	default IGregTechTileEntity getIGregTechTileEntityAtSide(byte arg0) {
		return null;
	}

	@Override
	default IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(byte arg0, int arg1) {
		return null;
	}

	@Override
	default byte getMetaID(int arg0, int arg1, int arg2) {
		return 0;
	}

	@Override
	default byte getMetaIDOffset(int arg0, int arg1, int arg2) {
		return 0;
	}

	@Override
	default byte getMetaIDAtSide(byte arg0) {
		return 0;
	}

	@Override
	default byte getMetaIDAtSideAndDistance(byte arg0, int arg1) {
		return 0;
	}

	@Override
	default boolean isDead() {
		return false;
	}

	@Override
	default void setLightValue(byte arg0) {
		
	}

}
