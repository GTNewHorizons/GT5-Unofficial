/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.crossmod.galaxySpace.tileEntity;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class DysonSwarmSunReplacement extends MetaTileEntity {
    private static boolean wasBuild;
    private static long dysonObjs;
    private static long swarmControllers;

    public DysonSwarmSunReplacement(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
        super(aID, aBasicName, aRegionalName, aInvSlotCount);
    }

    private DysonSwarmSunReplacement(String aName, int aInvSlotCount) {
        super(aName, aInvSlotCount);
    }

    public void toggle() {
        if (!wasBuild) {
            GalacticraftCore.solarSystemSol
                    .getMainStar()
                    .setBodyIcon(new ResourceLocation(
                            GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/moon.png"));
            wasBuild = !wasBuild;
        } else {
            GalacticraftCore.solarSystemSol
                    .getMainStar()
                    .setBodyIcon(new ResourceLocation(
                            GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/sun.png"));
            wasBuild = !wasBuild;
        }
    }

    @Override
    public byte getTileEntityBaseType() {
        return 2;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        ++swarmControllers;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        toggle();
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new DysonSwarmSunReplacement(this.mName, this.mInventory.length);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setLong("dysonObjs", dysonObjs);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        dysonObjs = Math.max(dysonObjs, nbtTagCompound.getLong("dysonObjs"));
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        aBaseMetaTileEntity.increaseStoredEnergyUnits(
                (DysonSwarmSunReplacement.dysonObjs * 10000) / swarmControllers, true);
    }

    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity iGregTechTileEntity, byte b, byte b1, byte b2, boolean b3, boolean b4) {
        return new ITexture[0];
    }
}
