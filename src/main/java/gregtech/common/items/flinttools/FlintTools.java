package gregtech.common.items.flinttools;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.EnumHelper;

public class FlintTools {
    public static Item.ToolMaterial FLINT_MATERIAL = EnumHelper.addToolMaterial("FLINT", 1, 128, 4.0F, 1.0F, 5);

    public static Item axe;
    public static Item sword;
    public static Item pickaxe;
    public static Item shovel;
    public static Item hoe;

    public static void registerFlintTools() {
        // Create items.
        axe = new FlintAxe();
        sword = new FlintSword();
        pickaxe = new FlintPickaxe();
        shovel = new FlintShovel();
        hoe = new FlintHoe();

        // Register them with minecraft.
        GameRegistry.registerItem(axe, "flintAxe");
        GameRegistry.registerItem(sword, "flintSword");
        GameRegistry.registerItem(pickaxe, "flintPickaxe");
        GameRegistry.registerItem(shovel, "flintShovel");
        GameRegistry.registerItem(hoe, "flintHoe");
    }

    // Below is transition code, to move from old GT tools, to new flint tools. Eventually, this can be removed.
    public static NBTTagCompound transformFlintTool(NBTTagCompound tagCompound) {
        int oldId = tagCompound.getShort("Damage");
        int id;

        switch (oldId) {
            case 0:
                id = Item.getIdFromItem(FlintTools.sword);
                break;
            case 2:
                id = Item.getIdFromItem(FlintTools.pickaxe);
                break;
            case 4:
                id = Item.getIdFromItem(FlintTools.shovel);
                break;
            case 6:
                id = Item.getIdFromItem(FlintTools.axe);
                break;
            case 8:
                id = Item.getIdFromItem(FlintTools.hoe);
                break;
            case 40: // Sense tool
                id = 0;
                break;
            default:
                return tagCompound; // No transform needed.
        }

        tagCompound.setShort("id", (short) id);
        tagCompound.setShort("Damage", (short) 0);
        tagCompound.setTag("tag", new NBTTagCompound()); // Erase old GT data.

        return tagCompound;
    }


    public static void registerTransformFlintTools() {
        ItemStackReplacementManager.addItemReplacement("gregtech:gt.metatool.01", FlintTools::transformFlintTool);
    }
}
