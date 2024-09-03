/**
 *
 * Inspired/ported from GregTech 6 under the LGPL license
 *
 * Copyright (c) 2020 GregTech-6 Team
 *
 * This file is part of GregTech.
 *
 * GregTech is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * GregTech is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with GregTech. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package gregtech.common.tileentities.machines.long_distance;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_ITEM_BACK;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_ITEM_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_ITEM_SIDE_LEFT_RIGHT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_ITEM_SIDE_LEFT_RIGHT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_ITEM_SIDE_UP_DOWN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_ITEM_SIDE_UP_DOWN_GLOW;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

public class MTELongDistancePipelineItem extends MTELongDistancePipelineBase {

    public MTELongDistancePipelineItem(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Sends Items over long distances");
    }

    public MTELongDistancePipelineItem(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isSameClass(MTELongDistancePipelineBase other) {
        return other instanceof MTELongDistancePipelineItem;
    }

    @Override
    public int getPipeMeta() {
        return 1;
    }

    public IInventory getInventory() {
        final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
        TileEntity invTile = tTile.getTileEntityAtSide(tTile.getBackFacing());
        if (invTile instanceof IInventory) return (IInventory) invTile;
        else return null;
    }

    @Override
    public ItemStack decrStackSize(int aSlot, int aDecrement) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.decrStackSize(aSlot, aDecrement);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int aSlot) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.getStackInSlotOnClosing(aSlot);
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlot(int aSlot) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.getStackInSlot(aSlot);
        }
        return null;
    }

    @Override
    public String getInventoryName() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.getInventoryName();
        }
        return super.getInventoryName();
    }

    @Override
    public int getSizeInventory() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.getSizeInventory();
        }
        return 0;
    }

    @Override
    public int getInventoryStackLimit() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.getInventoryStackLimit();
        }
        return 0;
    }

    @Override
    public void setInventorySlotContents(int aSlot, ItemStack aStack) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) iInventory.setInventorySlotContents(aSlot, aStack);
        }
    }

    @Override
    public boolean hasCustomInventoryName() {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.hasCustomInventoryName();
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int aSlot, ItemStack aStack) {
        if (checkTarget()) {
            IInventory iInventory = getInventory();
            if (iInventory != null) return iInventory.isItemValidForSlot(aSlot, aStack);
        }
        return false;
    }

    // // Relay Sided Inventories
    //

    @Override
    public int[] getAccessibleSlotsFromSide(int ordinalSide) {
        if (checkTarget()) {
            final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
            final IInventory iInventory = getInventory();
            if (iInventory instanceof ISidedInventory inv) return inv.getAccessibleSlotsFromSide(
                tTile.getFrontFacing()
                    .ordinal());
            if (iInventory != null) {
                final int[] tReturn = new int[iInventory.getSizeInventory()];
                for (int i = 0; i < tReturn.length; i++) tReturn[i] = i;
                return tReturn;
            }
        }

        return GTValues.emptyIntArray;
    }

    @Override
    public boolean canInsertItem(int aSlot, ItemStack aStack, int ordinalSide) {
        if (checkTarget()) {
            final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
            IInventory iInventory = getInventory();
            if (iInventory instanceof ISidedInventory iSidedInventory) return iSidedInventory.canInsertItem(
                aSlot,
                aStack,
                tTile.getFrontFacing()
                    .ordinal());
            return iInventory != null;
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int aSlot, ItemStack aStack, int ordinalSide) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELongDistancePipelineItem(mName, mTier, getDescription()[0], mTextures);
    }

    @Override
    public ITexture[] getTextureOverlays() {
        ITexture[] overlays = new ITexture[4];
        overlays[INPUT_INDEX] = TextureFactory.of(OVERLAY_PIPELINE_ITEM_FRONT);
        overlays[OUTPUT_INDEX] = TextureFactory.of(OVERLAY_PIPELINE_ITEM_BACK);
        overlays[SIDE_UP_DOWN_INDEX] = TextureFactory.of(
            TextureFactory.of(OVERLAY_PIPELINE_ITEM_SIDE_UP_DOWN),
            TextureFactory.builder()
                .addIcon(OVERLAY_PIPELINE_ITEM_SIDE_UP_DOWN_GLOW)
                .glow()
                .build());
        overlays[SIDE_LEFT_RIGHT_INDEX] = TextureFactory.of(
            TextureFactory.of(OVERLAY_PIPELINE_ITEM_SIDE_LEFT_RIGHT),
            TextureFactory.builder()
                .addIcon(OVERLAY_PIPELINE_ITEM_SIDE_LEFT_RIGHT_GLOW)
                .glow()
                .build());

        return overlays;
    }
}
