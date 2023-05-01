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

package kubatech.savedata;

import net.minecraft.nbt.NBTTagCompound;

import kubatech.api.tea.TeaNetwork;

public class PlayerData {

    public String username = "";
    public TeaNetwork teaNetwork;

    PlayerData(NBTTagCompound NBTData) {
        username = NBTData.getString("username");
        if (NBTData.hasKey("teaNetwork")) teaNetwork = TeaNetwork.fromNBT(NBTData.getCompoundTag("teaNetwork"));
    }

    PlayerData() {}

    public NBTTagCompound toNBTData() {
        NBTTagCompound NBTData = new NBTTagCompound();
        NBTData.setString("username", username);
        if (teaNetwork != null) NBTData.setTag("teaNetwork", teaNetwork.toNBT());
        return NBTData;
    }

    public void markDirty() {
        PlayerDataManager.Instance.markDirty();
    }
}
