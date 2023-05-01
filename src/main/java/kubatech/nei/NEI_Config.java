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
        API.hideItem(LegendaryBlackTea.get(1));
        API.hideItem(LegendaryButterflyTea.get(1));
        API.hideItem(LegendaryEarlGrayTea.get(1));
        API.hideItem(LegendaryGreenTea.get(1));
        API.hideItem(LegendaryLemonTea.get(1));
        API.hideItem(LegendaryMilkTea.get(1));
        API.hideItem(LegendaryOolongTea.get(1));
        API.hideItem(LegendaryPeppermintTea.get(1));
        API.hideItem(LegendaryPuerhTea.get(1));
        API.hideItem(LegendaryRedTea.get(1));
        API.hideItem(LegendaryWhiteTea.get(1));
        API.hideItem(LegendaryYellowTea.get(1));
        API.hideItem(LegendaryUltimateTea.get(1));
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
