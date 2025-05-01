package gregtech.common.blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import net.minecraft.util.StatCollector;

import java.util.List;

public class BlockCoilACR extends BlockCasingsAbstract {
    public BlockCoilACR() {
        super(ItemCasings.class, "gt.blockACR", MaterialCasings.INSTANCE, 8);

        register(0, ItemList.AdvancedChemicalReactor_cooling_block_0, "ACR Freezer Tier 1");
        register(1, ItemList.AdvancedChemicalReactor_cooling_block_1, "ACR Freezer Tier 2");
        register(2, ItemList.AdvancedChemicalReactor_cooling_block_2, "ACR Freezer Tier 3");
        register(3, ItemList.AdvancedChemicalReactor_cooling_block_3, "ACR Freezer Tier 4");
        register(4, ItemList.AdvancedChemicalReactor_heating_block_0, "ACR Heater Tier 1");
        register(5, ItemList.AdvancedChemicalReactor_heating_block_1, "ACR Heater Tier 2");
        register(6, ItemList.AdvancedChemicalReactor_heating_block_2, "ACR Heater Tier 3");
        register(7, ItemList.AdvancedChemicalReactor_heating_block_3, "ACR Heater Tier 4");
    }
        @Override
        @SideOnly(Side.CLIENT)
        public IIcon getIcon(int ordinalSide, int aMeta) {
            return switch (aMeta) {
                case 0 -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_COOLING_BLOCK_0.getIcon();
                case 1 -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_COOLING_BLOCK_1.getIcon();
                case 2 -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_COOLING_BLOCK_2.getIcon();
                case 3 -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_COOLING_BLOCK_3.getIcon();
                case 4 -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_HEATING_BLOCK_0.getIcon();
                case 5 -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_HEATING_BLOCK_1.getIcon();
                case 6 -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_HEATING_BLOCK_2.getIcon();
                default -> Textures.BlockIcons.ADVANCED_CHEMICAL_REACTOR_HEATING_BLOCK_3.getIcon();
            };
        }
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);
        switch (stack.getItemDamage()) {
            case 0 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 200K to 400K"));
            case 1 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 100K to 200K"));
            case 2 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 30K to 100K"));
            case 3 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 0K to 30K"));
            case 4 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 300K to 2300K"));
            case 5 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 2300K to 5300K"));
            case 6 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 5300K to 10000K"));
            case 7 -> tooltip.add(StatCollector.translateToLocal(EnumChatFormatting.GOLD + "Temperature range: 10000K to 20000K"));
        }
    }
}
