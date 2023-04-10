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

import static kubatech.api.enums.ItemList.*;

import kubatech.loaders.item.ItemProxy;
import kubatech.loaders.item.KubaItems;
import kubatech.loaders.item.items.*;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemLoader {

    public static final KubaItems kubaitems = new KubaItems();

    public static void registerItems() {
        GameRegistry.registerItem(kubaitems, "kubaitems");

        // DON'T EVER CHANGE ORDER IN HERE, ADD NEW ITEMS ON BOTTOM

        LegendaryBlackTea.set(kubaitems.registerProxyItem(new TeaCollection("black_tea")));
        LegendaryButterflyTea.set(kubaitems.registerProxyItem(new TeaCollection("butterfly_tea")));
        LegendaryEarlGrayTea.set(kubaitems.registerProxyItem(new TeaCollection("earl_gray_tea")));
        LegendaryGreenTea.set(kubaitems.registerProxyItem(new TeaCollection("green_tea")));
        LegendaryLemonTea.set(kubaitems.registerProxyItem(new TeaCollection("lemon_tea")));
        LegendaryMilkTea.set(kubaitems.registerProxyItem(new TeaCollection("milk_tea")));
        LegendaryOolongTea.set(kubaitems.registerProxyItem(new TeaCollection("oolong_tea")));
        LegendaryPeppermintTea.set(kubaitems.registerProxyItem(new TeaCollection("peppermint_tea")));
        LegendaryPuerhTea.set(kubaitems.registerProxyItem(new TeaCollection("pu-erh_tea")));
        LegendaryRedTea.set(kubaitems.registerProxyItem(new TeaCollection("red_tea")));
        LegendaryWhiteTea.set(kubaitems.registerProxyItem(new TeaCollection("white_tea")));
        LegendaryYellowTea.set(kubaitems.registerProxyItem(new TeaCollection("yellow_tea")));
        LegendaryUltimateTea.set(kubaitems.registerProxyItem(new TeaUltimate()));

        BlackTea.set(kubaitems.registerProxyItem(new Tea("black_tea", 4, 0.3f)));
        EarlGrayTea.set(kubaitems.registerProxyItem(new Tea("earl_gray_tea", 4, 0.3f)));
        GreenTea.set(kubaitems.registerProxyItem(new Tea("green_tea", 4, 0.3f)));
        LemonTea.set(kubaitems.registerProxyItem(new Tea("lemon_tea", 4, 0.3f)));
        MilkTea.set(kubaitems.registerProxyItem(new Tea("milk_tea", 4, 0.3f)));
        OolongTea.set(kubaitems.registerProxyItem(new Tea("oolong_tea", 4, 0.3f)));
        PeppermintTea.set(kubaitems.registerProxyItem(new Tea("peppermint_tea", 4, 0.3f)));
        PuerhTea.set(kubaitems.registerProxyItem(new Tea("pu-erh_tea", 4, 0.3f)));
        WhiteTea.set(kubaitems.registerProxyItem(new Tea("white_tea", 4, 0.3f)));
        YellowTea.set(kubaitems.registerProxyItem(new Tea("yellow_tea", 4, 0.3f)));

        BlackTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("black_tea_leaf")));
        GreenTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("green_tea_leaf")));
        OolongTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("oolong_tea_leaf")));
        PuerhTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("pu-erh_tea_leaf")));
        WhiteTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("white_tea_leaf")));
        YellowTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("yellow_tea_leaf")));

        TeaLeafDehydrated.set(kubaitems.registerProxyItem(new TeaIngredient("tea_leaf_dehydrated")));
        SteamedTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("steamed_tea_leaf")));
        RolledTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("rolled_tea_leaf")));
        OxidizedTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("oxidized_tea_leaf")));
        FermentedTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("fermented_tea_leaf")));
        BruisedTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("bruised_tea_leaf")));
        PartiallyOxidizedTeaLeaf.set(kubaitems.registerProxyItem(new TeaIngredient("partially_oxidized_tea_leaf")));

        TeaAcceptorResearchNote
            .set(kubaitems.registerProxyItem(new ItemProxy("tea_acceptor_research_note", "research_note")));
        Beeeeee.set(kubaitems.registerProxyItem(new ItemProxy("beeeeee", "beeeeee")));
    }
}
