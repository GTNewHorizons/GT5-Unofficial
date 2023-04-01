package gregtech.api.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my Basic Machines
 */
@Deprecated
public class GT_GUIContainer_MultiMachine extends GT_GUIContainerMetaTile_Machine {

    String mName;
    private final int textColor = this.getTextColorOrDefault("text", 0xFAFAFF),
            textColorTitle = this.getTextColorOrDefault("title", 0xFAFAFF);

    public GT_GUIContainer_MultiMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName,
            String aTextureFile) {
        super(
                new GT_Container_MultiMachine(aInventoryPlayer, aTileEntity),
                RES_PATH_GUI + "multimachines/" + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        // If text is drawn iterate down GUI 8 pixels (height of characters).
        int line_counter = 7;
        int max_chars_per_line = 26;

        if (mName.length() > 26) {

            // Split the machine name into an array, so we can try fit it on one line but if not use more.
            String[] split = mName.split(" ");

            int total_line_length = 0;
            StringBuilder current_line = new StringBuilder();

            int index = 0;

            for (String str : split) {

                total_line_length += str.length();

                if (total_line_length > max_chars_per_line) {
                    fontRendererObj.drawString(current_line.toString(), 10, line_counter, textColorTitle);
                    line_counter += 8;
                    current_line = new StringBuilder();
                    index = 0;
                    total_line_length = str.length();
                }

                if (index == 0) {
                    current_line.append(str);
                } else {
                    current_line.append(" ")
                                .append(str);
                }
                index++;
            }
            fontRendererObj.drawString(current_line.toString(), 10, line_counter, textColorTitle);
        } else {
            fontRendererObj.drawString(mName, 10, line_counter, textColorTitle);
        }
        line_counter += 8;

        if (mContainer != null) { // (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ?
                                  // 0 : 8)
            // | (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64));
            if ((mContainer.mDisplayErrorCode & 1) != 0) {
                fontRendererObj.drawString(GT_Utility.trans("132", "Pipe is loose."), 10, line_counter, textColor);
                line_counter += 8;
            }

            if ((mContainer.mDisplayErrorCode & 2) != 0) {
                fontRendererObj.drawString(GT_Utility.trans("133", "Screws are loose."), 10, line_counter, textColor);
                line_counter += 8;
            }

            if ((mContainer.mDisplayErrorCode & 4) != 0) {
                fontRendererObj.drawString(GT_Utility.trans("134", "Something is stuck."), 10, line_counter, textColor);
                line_counter += 8;
            }

            if ((mContainer.mDisplayErrorCode & 8) != 0) {
                fontRendererObj.drawString(
                        GT_Utility.trans("135", "Platings are dented."),
                        10,
                        line_counter,
                        textColor);
                line_counter += 8;
            }

            if ((mContainer.mDisplayErrorCode & 16) != 0) {
                fontRendererObj.drawString(
                        GT_Utility.trans("136", "Circuitry burned out."),
                        10,
                        line_counter,
                        textColor);
                line_counter += 8;
            }

            if ((mContainer.mDisplayErrorCode & 32) != 0) {
                fontRendererObj.drawString(
                        GT_Utility.trans("137", "That doesn't belong there."),
                        10,
                        line_counter,
                        textColor);
                line_counter += 8;
            }

            if ((mContainer.mDisplayErrorCode & 64) != 0) {
                fontRendererObj.drawString(
                        GT_Utility.trans("138", "Incomplete Structure."),
                        10,
                        line_counter,
                        textColor);
                line_counter += 8;
            }

            if (mContainer.mDisplayErrorCode == 0) {
                if (mContainer.mActive == 0) {
                    fontRendererObj.drawString(
                            GT_Utility.trans("139", "Hit with Soft Mallet"),
                            10,
                            line_counter,
                            textColor);
                    line_counter += 8;
                    fontRendererObj.drawString(
                            GT_Utility.trans("140", "to (re-)start the Machine"),
                            10,
                            line_counter,
                            textColor);
                    line_counter += 8;
                    fontRendererObj.drawString(
                            GT_Utility.trans("141", "if it doesn't start."),
                            10,
                            line_counter,
                            textColor);
                } else {
                    fontRendererObj.drawString(
                            GT_Utility.trans("142", "Running perfectly."),
                            10,
                            line_counter,
                            textColor);
                }
                line_counter += 8;
                if (mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_DrillerBase) {
                    ItemStack tItem = mContainer.mTileEntity.getMetaTileEntity()
                                                            .getStackInSlot(1);
                    if (tItem == null
                            || !GT_Utility.areStacksEqual(tItem, GT_ModHandler.getIC2Item("miningPipe", 1L))) {
                        fontRendererObj.drawString(
                                GT_Utility.trans("143", "Missing Mining Pipe"),
                                10,
                                line_counter,
                                textColor);
                    }
                } else if (mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_LargeTurbine) {
                    ItemStack tItem = mContainer.mTileEntity.getMetaTileEntity()
                                                            .getStackInSlot(1);
                    if (tItem == null
                            || !(tItem.getItem() == GT_MetaGenerated_Tool_01.INSTANCE && tItem.getItemDamage() >= 170
                                    && tItem.getItemDamage() <= 177)) {
                        fontRendererObj.drawString(
                                GT_Utility.trans("144", "Missing Turbine Rotor"),
                                10,
                                line_counter,
                                textColor);
                    }
                }
            }
        }
    }

    @Deprecated
    public String trans(String aKey, String aEnglish) {
        return GT_Utility.trans(aKey, aEnglish);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
