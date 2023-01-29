/*
 * KubaTech - Gregtech Addon Copyright (C) 2022 - 2023 kuba6000 This library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version. This library is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this library. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package kubatech.savedata;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class PlayerDataManager extends WorldSavedData {

    private static final String playerDataName = "KubaTech_PlayerData";
    static PlayerDataManager Instance = null;
    private final HashMap<String, PlayerData> players = new HashMap<>();

    public static void Initialize(World world) {
        if (Instance != null) {
            Instance.players.clear();
        }
        Instance = (PlayerDataManager) world.mapStorage.loadData(PlayerDataManager.class, playerDataName);
        if (Instance == null) {
            Instance = new PlayerDataManager();
            world.mapStorage.setData(playerDataName, Instance);
        }
        Instance.markDirty();
    }

    @SuppressWarnings("unused")
    public PlayerDataManager(String p_i2141_1_) {
        super(p_i2141_1_);
    }

    public PlayerDataManager() {
        super(playerDataName);
    }

    @Override
    public void readFromNBT(NBTTagCompound NBTData) {
        if (!NBTData.hasKey("size")) return;
        players.clear();
        for (int i = 0, imax = NBTData.getInteger("size"); i < imax; i++) {
            NBTTagCompound playerNBTData = NBTData.getCompoundTag("Player." + i);
            players.put(playerNBTData.getString("username"), new PlayerData(playerNBTData.getCompoundTag("data")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound NBTData) {
        NBTData.setInteger("size", players.size());
        int i = 0;
        for (Map.Entry<String, PlayerData> playerDataEntry : players.entrySet()) {
            NBTTagCompound playerNBTData = new NBTTagCompound();
            playerNBTData.setString("username", playerDataEntry.getKey());
            playerNBTData.setTag("data", playerDataEntry.getValue().toNBTData());
            NBTData.setTag("Player." + (i++), playerNBTData);
        }
    }

    public static PlayerData getPlayer(String username) {
        if (Instance == null) return null;
        return Instance.players.computeIfAbsent(username, s -> new PlayerData());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote || event.world.provider.dimensionId != 0) return;
        Initialize(event.world);
    }
}
