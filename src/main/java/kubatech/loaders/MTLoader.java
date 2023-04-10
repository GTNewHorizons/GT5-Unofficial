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

package kubatech.loaders;

import kubatech.Tags;
import minetweaker.MineTweakerImplementationAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MTLoader {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[MT Loader]");
    public static MTLoader instance = null;

    public static void init() {
        if (instance == null) {
            instance = new MTLoader();
            MineTweakerImplementationAPI.onPostReload(instance::MTOnPostReload);
        }
    }

    public void MTOnPostReload(MineTweakerImplementationAPI.ReloadEvent reloadEvent) {
        LOG.info("MT Recipes Loaded!");
    }
}
