package gtPlusPlus.xmod.gregtech.common.covers;

import gregtech.api.gui.GT_GUICover;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.minecraft.LangUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.enums.GT_Values;
import gregtech.api.net.GT_Packet_TileEntityCover;


public class GTPP_Cover_Overflow extends GT_CoverBehavior {

	public final int mTransferRate;
	public final int mInitialTransferRate;
	public final int mMaxTransferRate;

	public GTPP_Cover_Overflow(int aTransferRate) {
		this.mTransferRate = aTransferRate * 1000 / 10;
		this.mInitialTransferRate = aTransferRate;
		this.mMaxTransferRate = aTransferRate * 1000;
	}

	public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			long aTimer) {
		if (aCoverVariable == 0) {
			return aCoverVariable;
		}
		if ((aTileEntity instanceof IFluidHandler)) {
			//Logger.INFO("Trying to Void via Overflow.");
			IFluidHandler tTank1;
			ForgeDirection directionFrom;
			directionFrom = ForgeDirection.UNKNOWN;
			tTank1 = (IFluidHandler) aTileEntity;			
			if (tTank1 != null) {
				//Logger.INFO("Found Self. "+aSide);
				//FluidStack aTankStack = tTank1.drain(ForgeDirection.UNKNOWN, 1, false);
				FluidStack aTankStack = tTank1.getTankInfo(directionFrom)[0].fluid;
				if (aTankStack != null) {
					//Logger.INFO("Found Fluid inside self - "+aTankStack.getLocalizedName()+", overflow point set at "+aCoverVariable+"L and we have "+aTankStack.amount+"L inside.");
					if (aTankStack.amount > aCoverVariable) {
						int aAmountToDrain = aTankStack.amount - aCoverVariable;
						//Logger.INFO("There is "+aAmountToDrain+" more fluid in the tank than we would like.");
						if (aAmountToDrain > 0) {
							FluidStack tLiquid = tTank1.drain(directionFrom, Math.abs(aAmountToDrain), true);	
							if (tLiquid != null) {
								//Logger.INFO("Drained "+aAmountToDrain+"L.");
							}
						}
					}
				}
				else {
					//Logger.INFO("Could not simulate drain on self.");
				}
			}
		}
		return aCoverVariable;
	}

	public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			aCoverVariable += (mMaxTransferRate * (aPlayer.isSneaking() ? 0.1f : 0.01f));
		} else {
			aCoverVariable -= (mMaxTransferRate * (aPlayer.isSneaking() ? 0.1f : 0.01f));
		}
		if (aCoverVariable > mMaxTransferRate) {
			aCoverVariable = mInitialTransferRate;
		}
		if (aCoverVariable <= 0) {
			aCoverVariable = mMaxTransferRate;
		}
		GT_Utility.sendChatToPlayer(aPlayer, LangUtils.trans("322", "Overflow point: ") + aCoverVariable + trans("323", "L"));
		return aCoverVariable;
	}

	public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
			EntityPlayer aPlayer, float aX, float aY, float aZ) {
		boolean aShift = aPlayer.isSneaking();
		int aAmount = aShift ? 128 : 8;
		if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
			aCoverVariable += aAmount;
		} else {
			aCoverVariable -= aAmount;
		}
		if (aCoverVariable > mMaxTransferRate) {
			aCoverVariable = mInitialTransferRate;
		}
		if (aCoverVariable <= 0) {
			aCoverVariable = mMaxTransferRate;
		}
		GT_Utility.sendChatToPlayer(aPlayer, LangUtils.trans("322", "Overflow point: ") + aCoverVariable + trans("323", "L"));
		aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
		return true;
	}

	public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
		return true;
	}

	public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return false;
	}

	public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
		return true;
	}

	public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return true;
	}

	public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
		return 5;
	}

	//GUI
	@Override
	public boolean hasCoverGUI() {
		return true;
	}

	@Override
	public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity) {
		return new GTPP_Cover_Overflow.GUI(aSide, aCoverID, coverData, aTileEntity);
	}

	private class GUI extends GT_GUICover {
		private final byte side;
		private final int coverID;
		private GT_GuiIntegerTextBox tBox;
		private int coverVariable;

		private static final int startX = 10;
		private static final int startY = 25;
		private static final int spaceX = 18;
		private static final int spaceY = 18;

		private boolean warn = false;

		public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
			super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
			this.side = aSide;
			this.coverID = aCoverID;
			this.coverVariable = aCoverVariable;

			tBox = new GT_GuiIntegerTextBox(this, 2, startX + spaceX * 0, startY + spaceY * 0 + 8, spaceX * 4 - 3, 12);
			tBox.setText(String.valueOf(this.coverVariable));
			tBox.setMaxStringLength(10);
		}

		@Override
		protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
			tBox.setFocused(true);
		}

		@Override
		public void drawExtras(int mouseX, int mouseY, float parTicks) {
			super.drawExtras(mouseX, mouseY, parTicks);
			this.getFontRenderer().drawString(trans("323", "L"), startX + spaceX * 4, 4 + startY + spaceY * 0 + 8, 0xFF555555);
			if (warn)
				this.getFontRenderer().drawString(trans("325","Max")+": "+coverVariable+"/"+mMaxTransferRate+" "+trans("323", "L"), startX + spaceX * 0, 4 + startY + spaceY * 1 + 6, 0xffff0000);
			else
				this.getFontRenderer().drawString(trans("324","Now")+": "+coverVariable+"/"+mMaxTransferRate+" "+trans("323", "L"), startX + spaceX * 0, 4 + startY + spaceY * 1 + 6, 0xFF555555);
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
				if (i > (long) mMaxTransferRate) {
					i = mMaxTransferRate;
					warn = true;
				} else if (i < 0) {
					i = 0;
				}
				coverVariable = (int) i;
			}
			box.setText(String.valueOf(i));
			GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
		}

		@Override
		public void resetTextBox(GT_GuiIntegerTextBox box) {
			if (box.id == 2)
				box.setText(String.valueOf(coverVariable));
		}
	}
}
