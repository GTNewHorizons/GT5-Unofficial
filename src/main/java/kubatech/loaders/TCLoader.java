package kubatech.loaders;

import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import kubatech.api.LoaderReference;
import kubatech.api.enums.ItemList;
import kubatech.api.utils.ItemID;
import kubatech.loaders.item.items.TeaUltimate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TCLoader {
    private static boolean lateLoaded = false;

    public static void load() {}

    public static void lateLoad() {
        if (lateLoaded) return;
        lateLoaded = true;
        if (!LoaderReference.GTNHCoreMod || !LoaderReference.DraconicEvolution) return;

        final ItemStack[] components = new ItemStack[] {
            // ItemList.LegendaryBlackTea.get(1),
            // ItemList.LegendaryButterflyTea.get(1),
            // ItemList.LegendaryEarlGrayTea.get(1),
            ItemList.LegendaryGreenTea.get(1), // EIG
            // ItemList.LegendaryLemonTea.get(1),
            // ItemList.LegendaryMilkTea.get(1),
            // ItemList.LegendaryOolongTea.get(1),
            ItemList.LegendaryPeppermintTea.get(1), // HTGR
            ItemList.LegendaryPuerhTea.get(1), // EEC
            // ItemList.LegendaryRedTea.get(1),
            // ItemList.LegendaryWhiteTea.get(1),
            ItemList.LegendaryYellowTea.get(1), // IApiary
            ItemList.BlackTea.get(1),
            ItemList.EarlGrayTea.get(1),
            ItemList.GreenTea.get(1),
            ItemList.LemonTea.get(1),
            ItemList.MilkTea.get(1),
            ItemList.OolongTea.get(1),
            ItemList.PeppermintTea.get(1),
            ItemList.PuerhTea.get(1),
            ItemList.WhiteTea.get(1),
            ItemList.YellowTea.get(1)
        };

        final HashSet<ItemID> componentsHashed = Arrays.stream(components)
                .map(stack -> ItemID.create_NoCopy(stack, true, false, true))
                .collect(Collectors.toCollection(HashSet::new));

        InfusionRecipe ultimateTeaRecipe;
        //noinspection unchecked
        ThaumcraftApi.getCraftingRecipes()
                .add(
                        ultimateTeaRecipe =
                                new InfusionRecipe(
                                        "KT_UltimateTea",
                                        ItemList.LegendaryUltimateTea.get(1),
                                        10,
                                        new AspectList()
                                                .add(Aspect.MAGIC, 100)
                                                .add(Aspect.HEAL, 100)
                                                .add(Aspect.PLANT, 100)
                                                .add(Aspect.EXCHANGE, 100),
                                        GameRegistry.findItemStack("DraconicEvolution", "dezilsMarshmallow", 1),
                                        components) {
                                    @Override
                                    public boolean matches(
                                            ArrayList<ItemStack> input,
                                            ItemStack central,
                                            World world,
                                            EntityPlayer player) {
                                        if (!central.isItemEqual(getRecipeInput())) return false;
                                        if (!ThaumcraftApiHelper.isResearchComplete(
                                                player.getCommandSenderName(), this.research)) return false;
                                        if (componentsHashed.size() > input.size()) return false;
                                        HashSet<ItemID> hashedInputs = input.stream()
                                                .map(stack -> ItemID.create_NoCopy(stack, true, false, true))
                                                .collect(Collectors.toCollection(HashSet::new));
                                        return hashedInputs.containsAll(componentsHashed);
                                    }
                                });
        ResearchItem research =
                new ResearchItem(
                        "KT_UltimateTea",
                        "NEWHORIZONS",
                        new AspectList()
                                .add(Aspect.MAGIC, 1)
                                .add(Aspect.HEAL, 1)
                                .add(Aspect.PLANT, 1)
                                .add(Aspect.EXCHANGE, 1),
                        -2,
                        4,
                        2,
                        ItemList.LegendaryUltimateTea.get(1)) {
                    @Override
                    public String getName() {
                        return TeaUltimate.getUltimateTeaDisplayName(super.getName());
                    }
                };
        research.setPages(
                new ResearchPage("KT.research.ultimatetea") {
                    @Override
                    public String getTranslatedText() {
                        return TeaUltimate.getUltimateTeaDisplayName(super.getTranslatedText());
                    }
                },
                new ResearchPage(ultimateTeaRecipe));
        research.setParents("INFUSION", "DEZILSMARSHMALLOW");
        ThaumcraftApi.addWarpToResearch("KT_UltimateTea", 20);
        ResearchCategories.addResearch(research);
    }
}
