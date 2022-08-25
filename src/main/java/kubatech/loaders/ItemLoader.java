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

package kubatech.loaders;

import static kubatech.api.enums.ItemList.*;

import cpw.mods.fml.common.registry.GameRegistry;
import kubatech.loaders.item.KubaItems;
import kubatech.loaders.item.items.Tea;
import kubatech.loaders.item.items.TeaUltimate;

public class ItemLoader {
    public static final KubaItems kubaitems = new KubaItems();

    public static void RegisterItems() {
        GameRegistry.registerItem(kubaitems, "kubaitems");
        BlackTea.set(kubaitems.registerProxyItem(new Tea("black_tea")));
        ButterflyTea.set(kubaitems.registerProxyItem(new Tea("butterfly_tea")));
        EarlGrayTea.set(kubaitems.registerProxyItem(new Tea("earl_gray_tea")));
        GreenTea.set(kubaitems.registerProxyItem(new Tea("green_tea")));
        LemonTea.set(kubaitems.registerProxyItem(new Tea("lemon_tea")));
        MilkTea.set(kubaitems.registerProxyItem(new Tea("milk_tea")));
        OolongTea.set(kubaitems.registerProxyItem(new Tea("oolong_tea")));
        PeppermintTea.set(kubaitems.registerProxyItem(new Tea("peppermint_tea")));
        PuerhTea.set(kubaitems.registerProxyItem(new Tea("pu-erh_tea")));
        RedTea.set(kubaitems.registerProxyItem(new Tea("red_tea")));
        WhiteTea.set(kubaitems.registerProxyItem(new Tea("white_tea")));
        YellowTea.set(kubaitems.registerProxyItem(new Tea("yellow_tea")));
        UltimateTea.set(kubaitems.registerProxyItem(new TeaUltimate()));
    }
}
