/*
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022  kuba6000
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
 *
 */

package kubatech.nei;

import static kubatech.api.enums.ItemList.*;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import kubatech.Tags;

public class NEI_Config implements IConfigureNEI {
    public static boolean isAdded = true;

    @Override
    public void loadConfig() {
        isAdded = false;
        new Mob_Handler();
        isAdded = true;
        API.hideItem(BlackTea.get(1));
        API.hideItem(ButterflyTea.get(1));
        API.hideItem(EarlGrayTea.get(1));
        API.hideItem(GreenTea.get(1));
        API.hideItem(LemonTea.get(1));
        API.hideItem(MilkTea.get(1));
        API.hideItem(OolongTea.get(1));
        API.hideItem(PeppermintTea.get(1));
        API.hideItem(PuerhTea.get(1));
        API.hideItem(RedTea.get(1));
        API.hideItem(WhiteTea.get(1));
        API.hideItem(YellowTea.get(1));
        API.hideItem(UltimateTea.get(1));
    }

    @Override
    public String getName() {
        return Tags.MODNAME + " NEI Plugin";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }
}
