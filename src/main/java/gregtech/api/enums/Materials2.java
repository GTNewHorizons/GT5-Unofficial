package gregtech.api.enums;

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
