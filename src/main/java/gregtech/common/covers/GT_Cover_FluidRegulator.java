package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Cover variable
 * <pre>
 * 1111 1111 1111 1111 1111 1111 1111 1111
 *  |- interval-| |- flow rate 2 compl. -|
 * ^ export?
 * </pre>
 * Concat export and flow rate 2 compl. together to get actual flow rate.
 * A positive actual flow rate is export, and vice versa.
 * <p>
 * Interval is an unsigned 11 bit integer minus 1, so the range is 1~2048.
 * The stored bits will be flipped bitwise if speed is negative.
 * This way, `0` means 1tick interval, while `-1` means 1 tick interval as well, preserving the legacy behavior.
 */
public class GT_Cover_FluidRegulator extends GT_CoverBehavior {
	private static final int SPEED_LENGTH = 20;
	private static final int TICK_RATE_LENGTH = Integer.SIZE - SPEED_LENGTH - 1;
	private static final int TICK_RATE_MIN = 1;
	private static final int TICK_RATE_MAX = (-1 >>> (Integer.SIZE - TICK_RATE_LENGTH)) + TICK_RATE_MIN;
	private static final int TICK_RATE_BITMASK = (TICK_RATE_MAX - TICK_RATE_MIN) << SPEED_LENGTH;

	public final int mTransferRate;
	private boolean allowFluid = false;

	public GT_Cover_FluidRegulator(int aTransferRate) {
		if (aTransferRate > (-1 >>> (Integer.SIZE - SPEED_LENGTH)))
			throw new IllegalArgumentException("aTransferRate too big: " + aTransferRate);
		this.mTransferRate = aTransferRate;
	}

	private static int getSpeed(int aCoverVariable) {
		// positive or 0 -> interval bits need to be set to zero
		// negative -> interval bits need to be set to one
		return aCoverVariable >= 0 ? aCoverVariable & ~TICK_RATE_BITMASK : aCoverVariable | TICK_RATE_BITMASK;
	}

	private static int getTickRate(int aCoverVariable) {
		// range: TICK_RATE_MIN ~ TICK_RATE_MAX
		return ((Math.abs(aCoverVariable) & TICK_RATE_BITMASK) >>> SPEED_LENGTH) + TICK_RATE_MIN;
	}

	private static int generateNewCoverVariable(int aFlowRate, int aTickRate) {
		int tToStoreRaw = aTickRate - TICK_RATE_MIN;
		int tToStore = aFlowRate >= 0 ? tToStoreRaw : ~tToStoreRaw;
		return aFlowRate & ~TICK_RATE_BITMASK | (tToStore << SPEED_LENGTH);
	}

	@Override
	public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
		return false;
	}

	@Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
                             long aTimer) {
		int tSpeed = getSpeed(aCoverVariable);
		if (tSpeed == 0) {
			return aCoverVariable;
		}
		if ((aTileEntity instanceof IFluidHandler)) {
			IFluidHandler tTank1;
			IFluidHandler tTank2;
			ForgeDirection directionFrom;
			ForgeDirection directionTo;
			if (tSpeed > 0) {
				tTank2 = aTileEntity.getITankContainerAtSide(aSide);
				tTank1 = (IFluidHandler) aTileEntity;
				directionFrom = ForgeDirection.getOrientation(aSide);
				directionTo = ForgeDirection.getOrientation(aSide).getOpposite();
			} else {
				tTank1 = aTileEntity.getITankContainerAtSide(aSide);
				tTank2 = (IFluidHandler) aTileEntity;
				directionFrom = ForgeDirection.getOrientation(aSide).getOpposite();
				directionTo = ForgeDirection.getOrientation(aSide);
			}
			if (tTank1 != null && tTank2 != null) {
				allowFluid = true;
				FluidStack tLiquid = tTank1.drain(directionFrom, Math.abs(tSpeed), false);
				if (tLiquid != null) {
					tLiquid = tLiquid.copy();
					tLiquid.amount = tTank2.fill(directionTo, tLiquid, false);
					if (tLiquid.amount > 0) {
						tTank2.fill(directionTo, tTank1.drain(directionFrom, tLiquid.amount, true), true);
					}
				}
				allowFluid = false;
			}
		}
		return aCoverVariable;
	}

	private int adjustSpeed(EntityPlayer aPlayer, int aCoverVariable, int scale) {
		int tSpeed = getSpeed(aCoverVariable);
		tSpeed += scale;
		int tTickRate = getTickRate(aCoverVariable);
		if (Math.abs(tSpeed) > mTransferRate * tTickRate) {
			tSpeed = mTransferRate * tTickRate * (tSpeed > 0 ? 1 : -1);
			GT_Utility.sendChatToPlayer(aPlayer, trans("219", "Pump speed limit reached!"));
		}
		if (tTickRate == 1) {
			GT_Utility.sendChatToPlayer(aPlayer,
					trans("048", "Pump speed: ") + tSpeed + trans("049", "L/tick ") + tSpeed * 20 + trans("050", "L/sec"));
		} else {
			GT_Utility.sendChatToPlayer(aPlayer,
					String.format(trans("207", "Pump speed: %dL every %d ticks, %.2f L/sec on average"), tSpeed, tTickRate, tSpeed * 20d / tTickRate));
		}
		return generateNewCoverVariable(tSpeed, tTickRate);
	}

	@Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
                                       EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			return adjustSpeed(aPlayer, aCoverVariable, aPlayer.isSneaking() ? 256 : 16);
		} else {
			return adjustSpeed(aPlayer, aCoverVariable, aPlayer.isSneaking() ? -256 : -16);
		}
	}

	@Override
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
                                     EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			aCoverVariable = adjustSpeed(aPlayer, aCoverVariable, 1);
		} else {
			aCoverVariable = adjustSpeed(aPlayer, aCoverVariable, -1);
		}
		aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
		return true;
	}

	@Override
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return allowFluid;
	}

	@Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return allowFluid;
	}

	@Override
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return getTickRate(aCoverVariable);
	}

	/**
	 * GUI Stuff
	 */

	@Override
	public boolean hasCoverGUI() {
		return true;
	}

	@Override
	public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity)  {
		return new GT_Cover_FluidRegulator.GUI(aSide, aCoverID, coverData, aTileEntity);
	}

	private class GUI extends GT_GUICover {
		private final byte side;
		private final int coverID;
		private GT_GuiIntegerTextBox tBox, lBox;
		private int coverVariable;

		private static final int startX = 10;
		private static final int startY = 25;
		private static final int spaceX = 18;
		private static final int spaceY = 18;

		private int speed;
		private boolean export;
		private int tickRate;

		private boolean warn = false;

		public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
			super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
			this.side = aSide;
			this.coverID = aCoverID;
			this.coverVariable = aCoverVariable;

			int speed = getSpeed(coverVariable);
			this.speed = Math.abs(speed);
			this.export = speed >= 0;
			this.tickRate = getTickRate(coverVariable);
			new GT_GuiIconButton(this, 0, startX + spaceX * 0, startY + spaceY * 0, GT_GuiIcon.EXPORT).setTooltipText(trans("006", "Export"));
			new GT_GuiIconButton(this, 1, startX + spaceX * 1, startY + spaceY * 0, GT_GuiIcon.IMPORT).setTooltipText(trans("007", "Import"));

			tBox = new GT_GuiIntegerTextBox(this, 2, startX + spaceX * 0, startY + spaceY * 1 + 2, spaceX * 4 - 3, 12);
			tBox.setText(String.valueOf(this.speed));
			tBox.setMaxStringLength(10);

			lBox = new GT_GuiIntegerTextBox(this, 3, startX + spaceX * 0, startY + spaceY * 2 + 2, spaceX * 4 - 3, 12);
			lBox.setText(String.valueOf(this.tickRate));
			lBox.setMaxStringLength(4);
		}

		@Override
		public void drawExtras(int mouseX, int mouseY, float parTicks) {
			super.drawExtras(mouseX, mouseY, parTicks);
			this.getFontRenderer().drawString(trans("229", "Import/Export"), startX + spaceX * 4, 4 + startY + spaceY * 0, 0xFF555555);
			this.getFontRenderer().drawString(trans("200", " L"), startX + spaceX * 4, 4 + startY + spaceY * 1, 0xFF555555);
			this.getFontRenderer().drawString(trans("209", " ticks"), startX + spaceX * 4, 4 + startY + spaceY * 2, 0xFF555555);
			if (warn)
				this.getFontRenderer().drawString(String.format(trans("210", "Average: %.2f L/sec"), speed * 20d / tickRate), startX + spaceX * 0, 4 + startY + spaceY * 3, 0xffff0000);
			else
				this.getFontRenderer().drawString(String.format(trans("210", "Average: %.2f L/sec"), speed * 20d / tickRate), startX + spaceX * 0, 4 + startY + spaceY * 3, 0xFF555555);
		}

		@Override
		protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
			updateButtons();
			tBox.setFocused(true);
		}

		@Override
        public void buttonClicked(GuiButton btn){
			if (getClickable(btn.id)){
				coverVariable = getNewCoverVariable(btn.id);
				GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
			}
			updateButtons();
		}

		@Override
		public void onMouseWheel(int x, int y, int delta) {
			for (GT_GuiIntegerTextBox box : textBoxes){
				if (box.isFocused()) {
					int step = Math.max(1, Math.abs(delta / 120));
					step = (isShiftKeyDown() ? 50 : isCtrlKeyDown() ? 5 : 1) * (delta > 0 ? step : -step);
					long i;
					try {
						i = Long.parseLong(box.getText());
					} catch (NumberFormatException e) {
						return;
					}
					if (i > (Long.MAX_VALUE-1000))
						break;

					i = i + step;
					if (i <= 0)
						i = 0;
					box.setText(String.valueOf(i));
					break;
				}
			}
		}

		@Override
		public void applyTextBox(GT_GuiIntegerTextBox box) {
			long i;
			String s = box.getText().trim();
			try {
				i = Long.parseLong(s);
			} catch (NumberFormatException e) {
				resetTextBox(box);
				return;
			}

			warn = false;
			if (box.id == 2) {
				if (i > (long) mTransferRate * tickRate) {
					i = (long) mTransferRate * tickRate;
					warn = true;
				} else if (i < 0) {
					i = 0;
				}
				speed = (int) i;
			} else if (box.id == 3) {
				if (i > TICK_RATE_MAX) {
					i = tickRate;
				} else if (speed > mTransferRate * i) {
					i = Math.min(TICK_RATE_MAX, (speed + mTransferRate - 1) / mTransferRate);
					warn = true;
				} else if (i < TICK_RATE_MIN) {
					i = 1;
				}
				tickRate = (int) i;
			}
			box.setText(String.valueOf(i));

			coverVariable = getNewCoverVariable(2);
			GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
		}

		@Override
		public void resetTextBox(GT_GuiIntegerTextBox box) {
			if (box.id == 2)
				box.setText(String.valueOf(speed));
			else if (box.id == 3)
				box.setText(String.valueOf(tickRate));
		}

		private void updateButtons(){
			GuiButton b;
			for (Object o : buttonList) {
				b = (GuiButton) o;
				b.enabled = getClickable(b.id);
			}
		}

		private int getNewCoverVariable(int id) {
			switch (id) {
				case 0:
					export = true;
					return generateNewCoverVariable(speed, tickRate);
				case 1:
					export = false;
					return generateNewCoverVariable(-speed, tickRate);
				case 2:
					return generateNewCoverVariable(export ? speed : -speed, tickRate);
			}
			return coverVariable;
		}

		private boolean getClickable(int id) {
			return (id == 0) ^ (export);
		}
	}
}
