package gregtech.common.blocks;

import static gregtech.api.enums.Mods.NotEnoughItems;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;

public class BlockMetal extends BlockStorage {

    public Materials[] mMats;
    public OrePrefixes mPrefix;
    public IIconContainer[] mBlockIcons;
    public boolean mHideBlocks;
    public static boolean mNEIisLoaded = NotEnoughItems.isModLoaded();

    public BlockMetal(String aName, Materials[] aMats, OrePrefixes aPrefix, IIconContainer[] aBlockIcons) {
        super(ItemStorage.class, aName, Material.iron);
        mMats = aMats;
        mPrefix = aPrefix;
        mBlockIcons = aBlockIcons;
        mHideBlocks = mNEIisLoaded;

        for (int i = 0; i < aMats.length; i++) {
            if (aMats[i].mMetaItemSubID > 0 && aMats[i].mHasParentMod) {
                GTLanguageManager.addStringLocalization(
                    getUnlocalizedName() + "." + i + ".name",
                    "Block of " + (GTLanguageManager.i18nPlaceholder ? "%material" : aMats[i].mDefaultLocalName));
                if (aPrefix.mIsUnificatable) {
                    GTOreDictUnificator.set(aPrefix, aMats[i], new ItemStack(this, 1, i));
                } else {
                    GTOreDictUnificator.registerOre(aPrefix.get(aMats[i]), new ItemStack(this, 1, i));
                }
            }
        }
        if (aMats.length < 16 && mNEIisLoaded) {
            for (int i = aMats.length; i < 16; i++) codechicken.nei.api.API.hideItem(new ItemStack(this, 1, i));
        }
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16) && aMeta < mMats.length) {
            return mBlockIcons[aMeta].getIcon();
        }
        return null;
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aMetaData)) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        boolean isAvaritiaLoaded = Mods.Avaritia.isModLoaded();
        int metadata = stack.getItemDamage();

        if (this == GregTechAPI.sBlockMetal5) {
            if (metadata == 2) {
                tooltip.add(StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", 1, "piston_block"));
            }
        }

        if (this == GregTechAPI.sBlockMetal9) {
            switch (metadata) {
                case 4:
                    if (isAvaritiaLoaded) {
                        tooltip.add(
                            StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", 3, "piston_block"));
                    } else {
                        tooltip.add(
                            StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", 2, "piston_block"));
                    }
                    break;
                case 3:
                    if (isAvaritiaLoaded) {
                        tooltip.add(
                            StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", 4, "piston_block"));
                    } else {
                        tooltip.add(
                            StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", 3, "piston_block"));
                    }
                    break;
                case 8:
                    if (isAvaritiaLoaded) {
                        tooltip.add(
                            StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", 5, "piston_block"));
                    } else {
                        tooltip.add(
                            StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", 4, "piston_block"));
                    }
                    break;
            }
        }
    }
}
