package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.net.GT_Packet_TileEntityCoverNew;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
public class GT_Cover_FluidRegulator extends GT_CoverBehaviorBase<GT_Cover_FluidRegulator.FluidRegulatorData> {
	private static final int SPEED_LENGTH = 20;
	private static final int TICK_RATE_LENGTH = Integer.SIZE - SPEED_LENGTH - 1;
	private static final int TICK_RATE_MIN = 1;
	private static final int TICK_RATE_MAX = (-1 >>> (Integer.SIZE - TICK_RATE_LENGTH)) + TICK_RATE_MIN;
	private static final int TICK_RATE_BITMASK = (TICK_RATE_MAX - TICK_RATE_MIN) << SPEED_LENGTH;

	public final int mTransferRate;
	private boolean allowFluid = false;

	public GT_Cover_FluidRegulator(int aTransferRate) {
		super(FluidRegulatorData.class);
		if (aTransferRate > (-1 >>> (Integer.SIZE - SPEED_LENGTH)))
			throw new IllegalArgumentException("aTransferRate too big: " + aTransferRate);
		this.mTransferRate = aTransferRate;
	}

	@Override
	public FluidRegulatorData createDataObject(int aLegacyData) {
		return new FluidRegulatorData(aLegacyData);
	}

	@Override
	public FluidRegulatorData createDataObject() {
		return new FluidRegulatorData();
	}

	private static int generateNewCoverVariable(int aFlowRate, int aTickRate) {
		int tToStoreRaw = aTickRate - TICK_RATE_MIN;
		int tToStore = aFlowRate >= 0 ? tToStoreRaw : ~tToStoreRaw;
		return aFlowRate & ~TICK_RATE_BITMASK | (tToStore << SPEED_LENGTH);
	}

	@Override
	protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity, long aTimer) {
		return aCoverVariable.condition.isRedstoneSensitive();
	}

	@Override
    protected FluidRegulatorData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity,
                             long aTimer) {
		if (aCoverVariable.speed == 0 || !aCoverVariable.condition.isAllowedToWork(aSide, aCoverID, aTileEntity)) {
			return aCoverVariable;
		}
		if ((aTileEntity instanceof IFluidHandler)) {
			IFluidHandler tTank1;
			IFluidHandler tTank2;
			ForgeDirection directionFrom;
			ForgeDirection directionTo;
			if (aCoverVariable.speed > 0) {
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
				FluidStack tLiquid = tTank1.drain(directionFrom, Math.abs(aCoverVariable.speed), false);
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

	private void adjustSpeed(EntityPlayer aPlayer, FluidRegulatorData aCoverVariable, int scale) {
		int tSpeed = aCoverVariable.speed;
		tSpeed += scale;
		int tTickRate = aCoverVariable.tickRate;
		if (Math.abs(tSpeed) > mTransferRate * tTickRate) {
			tSpeed = mTransferRate * tTickRate * (tSpeed > 0 ? 1 : -1);
			GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("316", "Pump speed limit reached!"));
		}
		if (tTickRate == 1) {
			GT_Utility.sendChatToPlayer(aPlayer,
					GT_Utility.trans("048", "Pump speed: ") + tSpeed + GT_Utility.trans("049", "L/tick ") + tSpeed * 20 + GT_Utility.trans("050", "L/sec"));
		} else {
			GT_Utility.sendChatToPlayer(aPlayer,
					String.format(GT_Utility.trans("207", "Pump speed: %dL every %d ticks, %.2f L/sec on average"), tSpeed, tTickRate, tSpeed * 20d / tTickRate));
		}
	}

	@Override
    public FluidRegulatorData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity,
                                       EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			adjustSpeed(aPlayer, aCoverVariable, aPlayer.isSneaking() ? 256 : 16);
		} else {
			adjustSpeed(aPlayer, aCoverVariable, aPlayer.isSneaking() ? -256 : -16);
		}
		return aCoverVariable;
	}

	@Override
	protected boolean onCoverRightClickImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity,
                                     EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			adjustSpeed(aPlayer, aCoverVariable, 1);
		} else {
			adjustSpeed(aPlayer, aCoverVariable, -1);
		}
		return true;
	}

	@Override
    public boolean letsRedstoneGoInImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsEnergyInImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsEnergyOutImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsItemsInImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsItemsOutImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	@Override
    public boolean letsFluidInImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return allowFluid;
	}

	@Override
    public boolean letsFluidOutImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return allowFluid;
	}

	@Override
    protected boolean alwaysLookConnectedImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	@Override
    protected int getTickRateImpl(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity) {
		return aCoverVariable.tickRate;
	}

	/**
	 * GUI Stuff
	 */

	@Override
	public boolean hasCoverGUI() {
		return true;
	}

	@Override
	public Object getClientGUIImpl(byte aSide, int aCoverID, FluidRegulatorData coverData, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld)  {
		return new GT_Cover_FluidRegulator.GUI(aSide, aCoverID, coverData, aTileEntity);
	}

	public enum Conditional {
		Always(false) {
			@Override
			boolean isAllowedToWork(byte aSide, int aCoverID, ICoverable aTileEntity) {
				return true;
			}
		},
		Conditional(true) {
			@Override
			boolean isAllowedToWork(byte aSide, int aCoverID, ICoverable aTileEntity) {
				return !(aTileEntity instanceof IMachineProgress) || ((IMachineProgress) aTileEntity).isAllowedToWork();
			}
		},
		Inverted(true) {
			@Override
			boolean isAllowedToWork(byte aSide, int aCoverID, ICoverable aTileEntity) {
				return !(aTileEntity instanceof IMachineProgress) || !((IMachineProgress) aTileEntity).isAllowedToWork();
			}
		};

		static final Conditional[] VALUES = values();
		private final boolean redstoneSensitive;

		Conditional(boolean redstoneSensitive) {
			this.redstoneSensitive = redstoneSensitive;
		}

		abstract boolean isAllowedToWork(byte aSide, int aCoverID, ICoverable aTileEntity);
		boolean isRedstoneSensitive() {
			return redstoneSensitive;
		}
	}

	public static class FluidRegulatorData implements ISerializableObject {
		private int tickRate;
		private int speed;
		private Conditional condition;

		private static int getSpeed(int aCoverVariable) {
			// positive or 0 -> interval bits need to be set to zero
			// negative -> interval bits need to be set to one
			return aCoverVariable >= 0 ? aCoverVariable & ~TICK_RATE_BITMASK : aCoverVariable | TICK_RATE_BITMASK;
		}

		private static int getTickRate(int aCoverVariable) {
			// range: TICK_RATE_MIN ~ TICK_RATE_MAX
			return ((Math.abs(aCoverVariable) & TICK_RATE_BITMASK) >>> SPEED_LENGTH) + TICK_RATE_MIN;
		}

		public FluidRegulatorData() {
			this(0);
		}
		public FluidRegulatorData(int legacy) {
			this(getTickRate(legacy), getSpeed(legacy), Conditional.Always);
		}

		public FluidRegulatorData(int tickRate, int speed, Conditional condition) {
			this.tickRate = tickRate;
			this.speed = speed;
			this.condition = condition;
		}

		@Nonnull
		@Override
		public ISerializableObject copy() {
			return new FluidRegulatorData(tickRate, speed, condition);
		}

		@Nonnull
		@Override
		public NBTBase saveDataToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("mSpeed", speed);
			tag.setInteger("mTickRate", tickRate);
			tag.setByte("mCondition", (byte) condition.ordinal());
			return tag;
		}

		@Override
		public void writeToByteBuf(ByteBuf aBuf) {
			aBuf.writeInt(tickRate).writeInt(speed).writeByte(condition.ordinal());
		}

		@Override
		public void loadDataFromNBT(NBTBase aNBT) {
			if (!(aNBT instanceof NBTTagCompound)) return; // not very good...
			NBTTagCompound tag = (NBTTagCompound) aNBT;
			speed = tag.getInteger("mSpeed");
			tickRate = tag.getInteger("mTickRate");
			condition = Conditional.VALUES[tag.getByte("mCondition")];
		}

		@Nonnull
		@Override
		public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer) {
			return new FluidRegulatorData(aBuf.readInt(), aBuf.readInt(), Conditional.VALUES[aBuf.readUnsignedByte()]);
		}

		public int getTickRate() {
			return tickRate;
		}

		public void setTickRate(int tickRate) {
			this.tickRate = tickRate;
		}

		public int getSpeed() {
			return speed;
		}

		public void setSpeed(int speed) {
			this.speed = speed;
		}

		public Conditional getCondition() {
			return condition;
		}

		public void setCondition(Conditional condition) {
			this.condition = condition;
		}
	}

	private class GUI extends GT_GUICover {
		private final byte side;
		private final int coverID;
		private GT_GuiIntegerTextBox tBox, lBox;
		private FluidRegulatorData coverVariable;

		private static final int startX = 10;
		private static final int startY = 25;
		private static final int spaceX = 18;
		private static final int spaceY = 18;

		private boolean warn = false;

		public GUI(byte aSide, int aCoverID, FluidRegulatorData aCoverVariable, ICoverable aTileEntity) {
			super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
			this.side = aSide;
			this.coverID = aCoverID;
			this.coverVariable = aCoverVariable;

			new GT_GuiIconButton(this, 0, startX + spaceX * 0, startY + spaceY * 0, GT_GuiIcon.EXPORT).setTooltipText(GT_Utility.trans("006", "Export"));
			new GT_GuiIconButton(this, 1, startX + spaceX * 1, startY + spaceY * 0, GT_GuiIcon.IMPORT).setTooltipText(GT_Utility.trans("007", "Import"));
			new GT_GuiIconButton(this, 2, startX + spaceX * 0, startY + spaceY * 1, GT_GuiIcon.CHECKMARK).setTooltipText(GT_Utility.trans("224", "Always On"));
			new GT_GuiIconButton(this, 3, startX + spaceX * 1, startY + spaceY * 1, GT_GuiIcon.REDSTONE_ON).setTooltipText(GT_Utility.trans("225", "Active with Redstone Signal"));
			new GT_GuiIconButton(this, 4, startX + spaceX * 2, startY + spaceY * 1, GT_GuiIcon.REDSTONE_OFF).setTooltipText(GT_Utility.trans("226", "Inactive with Redstone Signal"));

			tBox = new GT_GuiIntegerTextBox(this, 2, startX + spaceX * 0, startY + spaceY * 2 + 2, spaceX * 4 - 3, 12) {
                @Override
                public boolean validChar(char c, int key) {
                    return super.validChar(c, key) || c == '-';
                }
            };
			tBox.setText(String.valueOf(this.coverVariable.speed));
			tBox.setMaxStringLength(10);

			lBox = new GT_GuiIntegerTextBox(this, 3, startX + spaceX * 5, startY + spaceY * 2 + 2, spaceX * 2 - 3, 12) {
                @Override
                public boolean validChar(char c, int key) {
                    return super.validChar(c, key) || c == '-';
                }
            };
			lBox.setText(String.valueOf(this.coverVariable.tickRate));
			lBox.setMaxStringLength(4);
		}

		@Override
		public void drawExtras(int mouseX, int mouseY, float parTicks) {
			super.drawExtras(mouseX, mouseY, parTicks);
			this.getFontRenderer().drawString(GT_Utility.trans("229", "Import/Export"), startX + spaceX * 4, 4 + startY + spaceY * 0, 0xFF555555);
			this.getFontRenderer().drawString(GT_Utility.trans("229", "Conditional"), startX + spaceX * 4, 4 + startY + spaceY * 1, 0xFF555555);
			this.getFontRenderer().drawString(GT_Utility.trans("208", " L"), startX + spaceX * 4, 4 + startY + spaceY * 2, 0xFF555555);
			this.getFontRenderer().drawString(GT_Utility.trans("209", " ticks"), startX + spaceX * 7, 4 + startY + spaceY * 2, 0xFF555555);
			this.getFontRenderer().drawString(String.format(GT_Utility.trans("210", "Average: %.2f L/sec"), coverVariable.tickRate == 0 ? 0 : coverVariable.speed * 20d / coverVariable.tickRate), startX + spaceX * 0, 4 + startY + spaceY * 3, warn ? 0xffff0000 : 0xff555555);
		}

		@Override
		protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
			updateButtons();
			tBox.setFocused(true);
		}

		@Override
        public void buttonClicked(GuiButton btn){
			if (!btn.enabled)
				return;
			switch (btn.id) {
				case 0:
				case 1:
					coverVariable.speed *= -1;
                    for (GT_GuiIntegerTextBox box : textBoxes){
                        if (box.id == 2) {
                            box.setText(String.valueOf(coverVariable.speed));
                        }
                    }
					break;
				case 2:
				case 3:
				case 4:
					coverVariable.condition = Conditional.VALUES[btn.id - 2];
					break;
				default:
					// not right, but we carry on
					return;
			}
			GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
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
				long maxFlow = (long) mTransferRate * GT_Utility.clamp(coverVariable.tickRate, TICK_RATE_MIN, TICK_RATE_MAX);
				if (i > maxFlow) {
					i = maxFlow;
					warn = true;
				} else if (i < -maxFlow) {
                    i = -maxFlow;
                    warn = true;
                }
				if (!warn && coverVariable.speed == i) return;
				coverVariable.speed = (int) i;
			} else if (box.id == 3) {
				if (i > TICK_RATE_MAX) {
					i = TICK_RATE_MAX;
                    warn = true;
				} else if (Math.abs(coverVariable.speed) > mTransferRate * i) {
					i = Math.min(TICK_RATE_MAX, (Math.abs(coverVariable.speed) + mTransferRate - 1) / mTransferRate);
					warn = true;
				} else if (i < TICK_RATE_MIN) {
					i = 1;
				}
				if (!warn && coverVariable.tickRate == i) return;
				coverVariable.tickRate = (int) i;
			}
			box.setText(String.valueOf(i));
			updateButtons();

			GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
		}

		@Override
		public void resetTextBox(GT_GuiIntegerTextBox box) {
			if (box.id == 2)
				box.setText(String.valueOf(coverVariable.speed));
			else if (box.id == 3)
				box.setText(String.valueOf(coverVariable.tickRate));
		}

		private void updateButtons(){
			GuiButton b;
			for (Object o : buttonList) {
				b = (GuiButton) o;
				b.enabled = getClickable(b.id);
			}
		}

		private boolean getClickable(int id) {
			switch (id) {
				case 0:
					return coverVariable.speed < 0;
				case 1:
					return coverVariable.speed > 0;
				case 2:
				case 3:
				case 4:
					return coverVariable.condition != Conditional.VALUES[id - 2];
			}
			return false;
		}
	}
}
