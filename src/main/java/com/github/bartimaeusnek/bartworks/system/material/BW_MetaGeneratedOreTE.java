/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.system.material;

import com.github.bartimaeusnek.bartworks.common.net.OrePacket;
import com.github.bartimaeusnek.bartworks.util.Coords;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

import static com.github.bartimaeusnek.bartworks.MainMod.BW_Network_instance;

public class BW_MetaGeneratedOreTE extends TileEntity implements ITexturedTileEntity {

    public short mMetaData;

    public static boolean placeOre(World aWorld, Coords coords, Werkstoff werkstoff) {
        short meta = werkstoff.getmID();
        aWorld.setBlock(coords.x, coords.y, coords.z, WerkstoffLoader.BWOres, 0, 0);
        TileEntity tTileEntity = aWorld.getTileEntity(coords.x, coords.y, coords.z);
        if ((tTileEntity instanceof BW_MetaGeneratedOreTE)) {
            ((BW_MetaGeneratedOreTE) tTileEntity).mMetaData = meta;
        }
        return true;
    }

    public boolean canUpdate() {
        return false;
    }

    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        this.mMetaData = aNBT.getShort("m");
    }

    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        aNBT.setShort("m", this.mMetaData);
    }

    public ArrayList<ItemStack> getDrops(Block aDroppedOre, int aFortune) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        if (this.mMetaData < 0) {
            rList.add(new ItemStack(Blocks.cobblestone, 1, 0));
            return rList;
        }
        rList.add(new ItemStack(aDroppedOre, 1, this.mMetaData));
        return rList;
    }

    @Override
    public Packet getDescriptionPacket() {
        if (!this.worldObj.isRemote)
            BW_Network_instance.sendPacketToAllPlayersInRange(this.worldObj, new OrePacket(this.xCoord, (short) this.yCoord, this.zCoord, this.mMetaData), this.xCoord, this.zCoord);
        return null;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide) {
        Werkstoff aMaterial = Werkstoff.werkstoffHashMap.get(this.mMetaData);
        if ((aMaterial != null)) {
            GT_RenderedTexture aIconSet = new GT_RenderedTexture(aMaterial.getTexSet().mTextures[OrePrefixes.ore.mTextureIndex], aMaterial.getRGBA());
            return new ITexture[]{new GT_CopiedBlockTexture(Blocks.stone, 0, 0), aIconSet};
        }
        return new ITexture[]{new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_RenderedTexture(gregtech.api.enums.TextureSet.SET_NONE.mTextures[OrePrefixes.ore.mTextureIndex])};
    }
}
