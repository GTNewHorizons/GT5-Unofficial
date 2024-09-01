/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
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

import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kuba6000.mobsinfo.api.utils.ItemID;

import cpw.mods.fml.common.registry.GameRegistry;
import kubatech.api.enums.ItemList;
import kubatech.loaders.item.items.ItemTeaUltimate;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TCLoader {

    public static final String TCCategoryKey = "KUBATECH";

    public static void init() {
        /*
         * ResearchCategories.registerCategory(
         * TCCategoryKey,
         * new ResourceLocation(Tags.MODID, "textures/gui/green_tea.png"),
         * new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png"));
         */
        if (!NewHorizonsCoreMod.isModLoaded() || !DraconicEvolution.isModLoaded()) return;
        registerRecipe();
        registerResearch();
    }

    private static InfusionRecipe ultimateTeaRecipe = null;

    @SuppressWarnings("unchecked")
    private static void registerRecipe() {
        if (ultimateTeaRecipe != null) return;
        final ItemStack[] components = new ItemStack[] {
            // ItemList.LegendaryBlackTea.get(1),
            // ItemList.LegendaryButterflyTea.get(1),
            ItemList.LegendaryEarlGrayTea.get(1), // MApiary
            ItemList.LegendaryGreenTea.get(1), // EIG
            // ItemList.LegendaryLemonTea.get(1),
            // ItemList.LegendaryMilkTea.get(1),
            // ItemList.LegendaryOolongTea.get(1),
            ItemList.LegendaryPeppermintTea.get(1), // HTGR
            ItemList.LegendaryPuerhTea.get(1), // EEC
            // ItemList.LegendaryRedTea.get(1),
            // ItemList.LegendaryWhiteTea.get(1),
            ItemList.LegendaryYellowTea.get(1), // IApiary
            ItemList.BlackTea.get(1), ItemList.EarlGrayTea.get(1), ItemList.GreenTea.get(1), ItemList.LemonTea.get(1),
            ItemList.MilkTea.get(1), ItemList.OolongTea.get(1), ItemList.PeppermintTea.get(1), ItemList.PuerhTea.get(1),
            ItemList.WhiteTea.get(1), ItemList.YellowTea.get(1) };

        final HashSet<ItemID> componentsHashed = Arrays.stream(components)
            .map(stack -> ItemID.createNoCopy(stack, true, false, true))
            .collect(Collectors.toCollection(HashSet::new));

        // noinspection unchecked
        ThaumcraftApi.getCraftingRecipes()
            .add(
                ultimateTeaRecipe = new InfusionRecipe(
                    "KT_UltimateTea",
                    ItemList.LegendaryUltimateTea.get(1),
                    10,
                    new AspectList().add(Aspect.MAGIC, 100)
                        .add(Aspect.HEAL, 100)
                        .add(Aspect.PLANT, 100)
                        .add(Aspect.EXCHANGE, 100),
                    GameRegistry.findItemStack("DraconicEvolution", "dezilsMarshmallow", 1),
                    components) {

                    @Override
                    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world,
                        EntityPlayer player) {
                        if (!central.isItemEqual(getRecipeInput())) return false;
                        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), this.research))
                            return false;
                        if (componentsHashed.size() > input.size()) return false;
                        HashSet<ItemID> hashedInputs = input.stream()
                            .map(stack -> ItemID.createNoCopy(stack, true, false, true))
                            .collect(Collectors.toCollection(HashSet::new));
                        return hashedInputs.containsAll(componentsHashed);
                    }
                });
    }

    private static ResearchItem ultimateTeaResearch = null;

    private static void registerResearch() {
        if (ultimateTeaResearch == null) {
            ultimateTeaResearch = new ResearchItem(
                "KT_UltimateTea",
                "NEWHORIZONS" /* TCCategoryKey */,
                new AspectList().add(Aspect.MAGIC, 1)
                    .add(Aspect.HEAL, 1)
                    .add(Aspect.PLANT, 1)
                    .add(Aspect.EXCHANGE, 1),
                -2,
                4,
                2,
                ItemList.LegendaryUltimateTea.get(1)) {

                @Override
                public String getName() {
                    return ItemTeaUltimate.getUltimateTeaDisplayName(super.getName());
                }
            };
            ultimateTeaResearch.setPages(new ResearchPage("KT.research.ultimatetea") {

                @Override
                public String getTranslatedText() {
                    return ItemTeaUltimate.getUltimateTeaDisplayName(super.getTranslatedText());
                }
            }, new ResearchPage(ultimateTeaRecipe));
            ultimateTeaResearch.setParents("INFUSION", "DEZILSMARSHMALLOW");
            ThaumcraftApi.addWarpToResearch("KT_UltimateTea", 20);
        }
        ResearchCategories.addResearch(ultimateTeaResearch);
    }
}
