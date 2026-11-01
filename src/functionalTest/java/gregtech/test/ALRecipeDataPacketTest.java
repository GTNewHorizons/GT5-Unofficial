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
        return new ALRecipeDataPacket(packet.toNbt());
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
        ALRecipeDataPacket packet = new ALRecipeDataPacket(new RecipeAssemblyLine[] { duplicateOutputGroup.get(0) });

        ALRecipeDataPacket once = roundTrip(packet);
        assertEquals(duplicateOutputGroup.size(), once.getContent().length);
        assertEquals(new HashSet<>(duplicateOutputGroup), asSet(once.getContent()));

        ALRecipeDataPacket twice = roundTrip(once);
        assertEquals(once.getContent().length, twice.getContent().length);
        assertEquals(asSet(once.getContent()), asSet(twice.getContent()));
    }

    @Test
    void bloatedPacketCollapses() {
        assertNotNull(duplicateOutputGroup);
        RecipeAssemblyLine[] bloated = new RecipeAssemblyLine[8];
        Arrays.fill(bloated, duplicateOutputGroup.get(0));

        ALRecipeDataPacket once = roundTrip(new ALRecipeDataPacket(bloated));
        assertEquals(duplicateOutputGroup.size(), once.getContent().length);
        assertEquals(new HashSet<>(duplicateOutputGroup), asSet(once.getContent()));
    }

    @Test
    void uniqueOutputIsLossless() {
        assertNotNull(uniqueOutputRecipe);
        ALRecipeDataPacket once = roundTrip(new ALRecipeDataPacket(new RecipeAssemblyLine[] { uniqueOutputRecipe }));
        assertEquals(1, once.getContent().length);
        assertSame(uniqueOutputRecipe, once.getContent()[0]);
    }

    @Test
    void allPacketsShrinkBelowRegisteredRecipeCount() {
        int total = RecipeAssemblyLine.sAssemblylineRecipes.size();
        assertTrue(total > 0);
        RecipeAssemblyLine[] everything = RecipeAssemblyLine.sAssemblylineRecipes.toArray(new RecipeAssemblyLine[0]);
        ALRecipeDataPacket once = roundTrip(new ALRecipeDataPacket(everything));
        assertTrue(once.getContent().length <= total);
    }
}
