/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.tileentity;

import java.math.BigInteger;
import java.util.UUID;

import kubatech.api.tea.TeaNetwork;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TeaStorageTile extends TileEntity {

    public TeaStorageTile() {
        super();
    }

    private UUID tileOwner = null;
    private TeaNetwork teaNetwork = null;

    public void setTeaOwner(UUID teaOwner) {
        if (tileOwner == null) {
            tileOwner = teaOwner;
            teaNetwork = TeaNetwork.getNetwork(tileOwner);
            markDirty();
            teaNetwork.registerTeaStorageExtender(this);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound NBTData) {
        super.readFromNBT(NBTData);
        try {
            tileOwner = UUID.fromString(NBTData.getString("tileOwner"));
            teaNetwork = TeaNetwork.getNetwork(tileOwner);
            teaNetwork.registerTeaStorageExtender(this);
        } catch (Exception ignored) {}
    }

    @Override
    public void writeToNBT(NBTTagCompound NBTData) {
        super.writeToNBT(NBTData);
        NBTData.setString("tileOwner", tileOwner.toString());
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    public BigInteger teaExtendAmount() {
        return BigInteger.valueOf(Long.MAX_VALUE);
    }

    @Override
    public void onChunkUnload() {
        if (teaNetwork != null) teaNetwork.unregisterTeaStorageExtender(this);
    }

    @Override
    public void invalidate() {
        if (teaNetwork != null) teaNetwork.unregisterTeaStorageExtender(this);
    }
}
