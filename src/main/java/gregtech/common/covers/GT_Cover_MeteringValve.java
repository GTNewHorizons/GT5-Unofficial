package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;

public class GT_Cover_MeteringValve extends GT_Cover_FluidRegulator {
	public GT_Cover_MeteringValve(int aTransferRate) {
		super(aTransferRate);
	}

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 20;
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
		return new GT_Cover_MeteringValve.GUI(aSide, aCoverID, coverData, aTileEntity);
	}

	private class GUI extends GT_GUICover {
		private final byte side;
		private final int coverID;
		private final GT_GuiIntegerTextBox lBox;
		private int coverVariable;

		private final static int startX = 10;
		private final static int startY = 25;
		private final static int spaceX = 18;
		private final static int spaceY = 18;

		private int speed;
		private boolean export;

		public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
			super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
			this.side = aSide;
			this.coverID = aCoverID;
			this.coverVariable = aCoverVariable;

			this.speed = Math.abs(coverVariable);
			this.export = coverVariable >= 0;
			new GT_GuiIconButton(this, 0, startX, startY, GT_GuiIcon.EXPORT).setTooltipText(trans("006","Export"));
			new GT_GuiIconButton(this, 1,startX + spaceX, startY, GT_GuiIcon.IMPORT).setTooltipText(trans("007","Import"));

			lBox = new GT_GuiIntegerTextBox(this, 2, startX,startY+ spaceY + 2, spaceX*4-3,12);
			lBox.setText(String.valueOf(speed));
			lBox.setMaxStringLength(10);
		}

		@Override
		public void drawExtras(int mouseX, int mouseY, float parTicks) {
			super.drawExtras(mouseX, mouseY, parTicks);
			this.getFontRenderer().drawString(trans("229","Import/Export" ),  startX + spaceX*4, 4 + startY, 0xFF555555);
			this.getFontRenderer().drawString(trans("050", "L/sec"),  startX + spaceX*4, 4+startY+ spaceY, 0xFF555555);
		}

		@Override
		protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
			updateButtons();
			lBox.setFocused(true);
		}

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

			if (box.id == 3)
				i = i / 20;

			if (i > mTransferRate)
				i = mTransferRate;
			else if (i <= 0)
				i = 0;

			speed = (int) i;

			lBox.setText(String.valueOf(speed));

			coverVariable = getNewCoverVariable(2);
			GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
		}

		@Override
		public void resetTextBox(GT_GuiIntegerTextBox box) {
			box.setText(String.valueOf(speed));
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
					return speed;
				case 1:
					export = false;
					return -speed;
				case 2:
					if (export)
						return speed;
					else
						return -speed;
			}
			return coverVariable;
		}

		private boolean getClickable(int id) {
			return (id == 0) ^ (export);
		}
	}
}
