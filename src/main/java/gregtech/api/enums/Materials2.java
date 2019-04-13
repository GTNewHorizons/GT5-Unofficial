package gregtech.api.enums;

import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.IMaterialHandler;

import java.util.Arrays;

//import static gregtech.GT_Mod.GT_FML_LOGGER;


public class Materials2 {

	public static Materials Signalum = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1|2, 255, 255, 255, 0, "Signalum", "Signalum", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Lumium = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1|2, 255, 255, 255, 0, "Lumium", "Lumium", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials EnrichedCopper = new Materials(-1, TextureSet.SET_NONE, 1.0F, 0, 2, 1|2, 255, 255, 255, 0, "EnrichedCopper", "Enriched Copper", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials DiamondCopper = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1|2, 255, 255, 255, 0, "DiamondCopper", "Diamond Copper", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials TarPitch = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1|2, 255, 255, 255, 0, "TarPitch", "Tar Pitch", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Prismarine = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1|4, 255, 255, 255, 0, "Prismarine", "Prismarine", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials GraveyardDirt = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1, 255, 255, 255, 0, "GraveyardDirt", "Graveyard Dirt", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Teslatite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1, 60, 180, 200, 0, "Teslatite", "Teslatite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Tennantite = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1, 255, 255, 255, 0, "Tennantite", "Tennantite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials DarkThaumium = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1|2, 255, 255, 255, 0, "DarkThaumium", "Dark Thaumium", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Alfium = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1, 255, 255, 255, 0, "Alfium", "Alfium", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Ryu = new Materials(-1, TextureSet.SET_NONE, 			1.0F, 0, 2, 1, 255, 255, 255, 0, "Ryu", "Ryu", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Mutation = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1, 255, 255, 255, 0, "Mutation", "Mutation", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Aquamarine = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1|4, 255, 255, 255, 0, "Aquamarine", "Aquamarine", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Ender = new Materials(-1, TextureSet.SET_NONE, 			1.0F, 0, 2, 1, 255, 255, 255, 0, "Ender", "Ender", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Fairy = new Materials(-1, TextureSet.SET_NONE, 			1.0F, 0, 2, 1|2, 255, 255, 255, 0, "Fairy", "Fairy", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Ludicrite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1|2, 255, 255, 255, 0, "Ludicrite", "Ludicrite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Pokefennium = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1|2, 255, 255, 255, 0, "Pokefennium", "Pokefennium", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials PurpleAlloy = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 0 , 100, 180, 255, 0, "PurpleAlloy", "Purple Alloy", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials InfusedTeslatite = new Materials(-1, TextureSet.SET_NONE,1.0F, 0, 2, 0 , 100, 180, 255, 0, "InfusedTeslatite", "Infused Teslatite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials AquaRegia = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 0, 255, 255, 255, 0, "AquaRegia", "Aqua Regia", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials SolutionBlueVitriol  = new Materials(-1, TextureSet.SET_NONE, 1.0F, 0, 2, 0, 255, 255, 255, 0, "SolutionBlueVitriol", "Blue Vitriol Solution", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials SolutionNickelSulfate  = new Materials(-1, TextureSet.SET_NONE, 1.0F, 0, 2, 0, 255, 255, 255, 0, "SolutionNickelSulfate", "Nickel Sulfate Solution", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Adamite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 3, 1, 255, 255, 255, 0, "Adamite", "Adamite", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray);
    public static Materials Adluorite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1 | 8, 255, 255, 255, 0, "Adluorite", "Adluorite", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes._NULL);
    public static Materials Agate = new Materials(-1, TextureSet.SET_NONE, 			1.0F, 0, 2, 1, 255, 255, 255, 0, "Agate", "Agate", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Wimalite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 8 , 255, 255, 255, 0, "Wimalite", "Wimalite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes.dyeYellow);
    public static Materials Yellorite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 8 , 255, 255, 255, 0, "Yellorite", "Yellorite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes.dyeYellow);
    public static Materials Chrysocolla = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1, 255, 255, 255, 0, "Chrysocolla", "Chrysocolla", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Energized = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 0, 255, 255, 255, 0, "Energized", "Energized", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes._NULL);
    public static Materials Voidstone = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 1, 0, 255, 255, 255, 200, "Voidstone", "Voidstone", 0, 0, -1, 0, false, true, 1, 1, 1, Dyes._NULL, Arrays.asList(new TC_Aspects.TC_AspectStack(TC_Aspects.VITREUS, 1), new TC_AspectStack(TC_Aspects.VACUOS, 1)));
    public static Materials Turquoise = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 1, 1, 255, 255, 255, 0, "Turquoise", "Turquoise", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Sugilite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 1, 1, 255, 255, 255, 0, "Sugilite", "Sugilite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Spinel = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 1, 0, 255, 255, 255, 0, "Spinel", "Spinel", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Citrine = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1, 255, 255, 255, 0, "Citrine", "Citrine", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    public static Materials Demonite = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1, 255, 255, 255, 0, "Demonite", "Demonite", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes.dyeRed);
    public static Materials Infernal = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 0, 255, 255, 255, 0, "Infernal", "Infernal", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes._NULL);
    public static Materials Bloodstone = new Materials(-1, TextureSet.SET_NONE, 	1.0F, 0, 2, 1, 255, 255, 255, 0, "Bloodstone", "Bloodstone", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes.dyeRed);
    public static Materials DarkStone = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1, 255, 255, 255, 0, "DarkStone", "Dark Stone", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes.dyeBlack);
    public static Materials Drulloy = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1|16 , 255, 255, 255, 0, "Drulloy", "Drulloy", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeRed);
    public static Materials Invisium = new Materials(-1, TextureSet.SET_NONE, 		1.0F, 0, 2, 1, 255, 255, 255, 0, "Invisium", "Invisium", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL);
    
    private static void initSubTags() {
        SubTag.METAL.addTo(Signalum, Lumium, EnrichedCopper, DiamondCopper, DarkThaumium);
        SubTag.NO_SMASHING.addTo(TarPitch);
        
        Teslatite.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.UNBURNABLE, SubTag.SMELTING_TO_FLUID);
        DarkThaumium.add(SubTag.MAGICAL);
        
    }

     public void onMaterialsInit() {
        initSubTags();
        Teslatite				.setOreMultiplier( 5).setSmeltingMultiplier( 5);
        
        
    }

     public void onComponentInit() {
    }

     public void onComponentIteration(Materials aMaterial) {

    }
}
