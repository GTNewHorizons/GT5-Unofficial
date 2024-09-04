package gtPlusPlus.plugin.fixes.vanilla;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.manager.CoreManager;

// Called by Core_Manager#veryEarlyInit
@SuppressWarnings("unused")
public class VanillaFixesCore implements IPlugin {

    static final VanillaFixesCore mInstance;
    static final VanillaBedHeightFix mBedFixInstance;
    static final VanillaBackgroundMusicFix mMusicFixInstance;

    static {
        mInstance = new VanillaFixesCore();
        mBedFixInstance = new VanillaBedHeightFix(mInstance);
        mMusicFixInstance = new VanillaBackgroundMusicFix(mInstance);
        mInstance.log("Preparing " + mInstance.getPluginName() + " for use.");
    }

    VanillaFixesCore() {
        CoreManager.registerPlugin(this);
    }

    @Override
    public boolean preInit() {
        return fixVanillaOD();
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public boolean postInit() {
        return true;
    }

    @Override
    public boolean serverStart() {
        mMusicFixInstance.manage();
        return true;
    }

    @Override
    public boolean serverStop() {
        return true;
    }

    @Override
    public String getPluginName() {
        return "GT++ Vanilla Fixes Module";
    }

    @Override
    public String getPluginAbbreviation() {
        return "VFIX";
    }

    private boolean fixVanillaOD() {
        registerToOreDict(ItemUtils.getSimpleStack(Items.blaze_rod), "rodBlaze");
        registerToOreDict(ItemUtils.getSimpleStack(Items.nether_wart), "cropNetherWart");
        registerToOreDict(ItemUtils.getSimpleStack(Items.reeds), "sugarcane");
        registerToOreDict(ItemUtils.getSimpleStack(Items.paper), "paper");
        registerToOreDict(ItemUtils.getSimpleStack(Items.ender_pearl), "enderpearl");
        registerToOreDict(ItemUtils.getSimpleStack(Items.bone), "bone");
        registerToOreDict(ItemUtils.getSimpleStack(Items.gunpowder), "gunpowder");
        registerToOreDict(ItemUtils.getSimpleStack(Items.string), "string");
        registerToOreDict(ItemUtils.getSimpleStack(Items.nether_star), "netherStar");
        registerToOreDict(ItemUtils.getSimpleStack(Items.leather), "leather");
        registerToOreDict(ItemUtils.getSimpleStack(Items.feather), "feather");
        registerToOreDict(ItemUtils.getSimpleStack(Items.egg), "egg");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.end_stone), "endstone");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.vine), "vine");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.cactus), "blockCactus");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.grass), "grass");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.obsidian), "obsidian");
        registerToOreDict(ItemUtils.getSimpleStack(Blocks.crafting_table), "workbench");
        return true;
    }

    private void registerToOreDict(ItemStack aStack, String aString) {
        if (aStack.getItem() == Items.blaze_rod) {
            mInstance
                .log("Registering " + aStack.getDisplayName() + " to OreDictionary under the tag '" + aString + "'.");
        } else {
            mInstance.log(
                "Registering " + aStack.getDisplayName()
                    + " to OreDictionary under the tag '"
                    + aString
                    + "'. (Added to Forge in 1.8.9)");
        }
        ItemUtils.addItemToOreDictionary(aStack, aString);
    }
}
