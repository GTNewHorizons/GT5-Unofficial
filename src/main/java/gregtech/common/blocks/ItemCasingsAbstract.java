package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;

public abstract class ItemCasingsAbstract extends ItemBlock {

    protected final String mNoMobsToolTip = GT_LanguageManager
        .addStringLocalization("gt.nomobspawnsonthisblock", "Mobs cannot Spawn on this Block");
    protected final String mNoTileEntityToolTip = GT_LanguageManager
        .addStringLocalization("gt.notileentityinthisblock", "This is NOT a TileEntity!");
    protected final String mCoil01Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil01tooltip", "Base Heating Capacity = 1800 Kelvin");
    protected final String mCoil02Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil02tooltip", "Base Heating Capacity = 2700 Kelvin");
    protected final String mCoil03Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil03tooltip", "Base Heating Capacity = 3600 Kelvin");
    protected final String mCoil04Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil04tooltip", "Base Heating Capacity = 4500 Kelvin");
    protected final String mCoil05Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil05tooltip", "Base Heating Capacity = 5400 Kelvin");
    protected final String mCoil06Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil06tooltip", "Base Heating Capacity = 7200 Kelvin");
    protected final String mCoil07Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil07tooltip", "Base Heating Capacity = 9001 Kelvin");
    protected final String mCoil08Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil08tooltip", "Base Heating Capacity = 9900 Kelvin");
    protected final String mCoil09Tooltip = GT_LanguageManager
        .addStringLocalization("gt.coil09tooltip", "Base Heating Capacity = 10800 Kelvin");
    protected final String mBlastProofTooltip = GT_LanguageManager
        .addStringLocalization("gt.blastprooftooltip", "This Block is Blast Proof");

    public ItemCasingsAbstract(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + getDamage(aStack);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        aList.add(this.mNoMobsToolTip);
        aList.add(this.mNoTileEntityToolTip);
    }
}
