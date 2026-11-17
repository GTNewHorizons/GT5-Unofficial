package gregtech.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTUtility.ItemId;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;

class ALRecipeDataPacketTest {

    static Map<ItemId, List<RecipeAssemblyLine>> recipesByOutput;
    static List<RecipeAssemblyLine> duplicateOutputGroup;
    static RecipeAssemblyLine uniqueOutputRecipe;

    @BeforeAll
    static void findRecipeGroups() {
        recipesByOutput = new HashMap<>();
        for (RecipeAssemblyLine recipe : RecipeAssemblyLine.sAssemblylineRecipes) {
            recipesByOutput.computeIfAbsent(ItemId.create(recipe.mOutput), id -> new ArrayList<>())
                .add(recipe);
        }
        for (List<RecipeAssemblyLine> group : recipesByOutput.values()) {
            if (duplicateOutputGroup == null && group.size() >= 2) duplicateOutputGroup = group;
            if (uniqueOutputRecipe == null && group.size() == 1) uniqueOutputRecipe = group.get(0);
        }
    }

    private static ALRecipeDataPacket roundTrip(ALRecipeDataPacket packet) {
        return new ALRecipeDataPacket(packet.toNBT());
    }

    private static Set<RecipeAssemblyLine> asSet(RecipeAssemblyLine[] content) {
        return new HashSet<>(Arrays.asList(content));
    }

    @Test
    void duplicateOutputExists() {
        assertNotNull(duplicateOutputGroup, "expected at least one output with multiple assembly line recipes");
    }

    @Test
    void duplicateOutputReachesFixpoint() {
        assertNotNull(duplicateOutputGroup);
        ALRecipeDataPacket packet = new ALRecipeDataPacket(ImmutableList.of(duplicateOutputGroup.get(0)));

        ALRecipeDataPacket once = roundTrip(packet);
        assertEquals(duplicateOutputGroup.size(), once.contents.size());
        assertEquals(new HashSet<>(duplicateOutputGroup), new HashSet<>(once.contents));

        ALRecipeDataPacket twice = roundTrip(once);
        assertEquals(once.contents.size(), twice.contents.size());
        assertEquals(new HashSet<>(once.contents), new HashSet<>(twice.contents));
    }

    @Test
    void bloatedPacketCollapses() {
        assertNotNull(duplicateOutputGroup);
        RecipeAssemblyLine[] bloated = new RecipeAssemblyLine[8];
        Arrays.fill(bloated, duplicateOutputGroup.get(0));

        ALRecipeDataPacket once = roundTrip(new ALRecipeDataPacket(ImmutableList.copyOf(bloated)));
        assertEquals(duplicateOutputGroup.size(), once.contents.size());
        assertEquals(new HashSet<>(duplicateOutputGroup), new HashSet<>(once.contents));
    }

    @Test
    void uniqueOutputIsLossless() {
        assertNotNull(uniqueOutputRecipe);
        ALRecipeDataPacket once = roundTrip(new ALRecipeDataPacket(ImmutableList.of(uniqueOutputRecipe)));
        assertEquals(1, once.contents.size());
        assertSame(uniqueOutputRecipe, once.contents.getFirst());
    }

    @Test
    void allPacketsShrinkBelowRegisteredRecipeCount() {
        int total = RecipeAssemblyLine.sAssemblylineRecipes.size();
        assertTrue(total > 0);
        ALRecipeDataPacket once = roundTrip(
            new ALRecipeDataPacket(ImmutableList.copyOf(RecipeAssemblyLine.sAssemblylineRecipes)));
        assertTrue(once.contents.size() <= total);
    }
}
