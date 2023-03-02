package com.minecraft7771.gtnhintergalactic.block;

import com.minecraft7771.gtnhintergalactic.GTNHIntergalactic;
import com.minecraft7771.gtnhintergalactic.item.IGItems;
import com.minecraft7771.gtnhintergalactic.item.ItemCasingSpaceElevator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class BlockCasingSpaceElevator extends GT_Block_Casings_Abstract {

    public static BlockCasingSpaceElevator INSTANCE;
    private static final int NUMBER_OF_CASINGS = 3;
    private static IIcon IconSECasing0;
    private static final IIcon[] IconSECasing1 = new IIcon[2];
    private static final IIcon[] IconSECasing2 = new IIcon[2];

    public BlockCasingSpaceElevator() {
        super(ItemCasingSpaceElevator.class, "gt.blockcasingsSE", GT_Material_Casings.INSTANCE);

        for (byte b = 0; b < NUMBER_OF_CASINGS; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[32][b] = new GT_CopiedBlockTexture(this, 6, b);
        }

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Space Elevator Base Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Space Elevator Support Structure");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Space Elevator Internal Structure");

        IGItems.SpaceElevatorCasing0 = new ItemStack(this, 1, 0);
        IGItems.SpaceElevatorCasing1 = new ItemStack(this, 1, 1);
        IGItems.SpaceElevatorCasing2 = new ItemStack(this, 1, 2);
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        IconSECasing0 = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/BaseCasing");
        IconSECasing1[0] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/SupportStructure");
        IconSECasing1[1] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/SupportStructure_Side");
        IconSECasing2[0] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/InternalStructure");
        IconSECasing2[1] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/InternalStructure_Side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return IconSECasing0;
            case 1:
                switch (aSide) {
                    case 0:
                    case 1:
                        return IconSECasing1[0];
                    default:
                        return IconSECasing1[1];
                }
            case 2:
                switch (aSide) {
                    case 0:
                    case 1:
                        return IconSECasing2[0];
                    default:
                        return IconSECasing2[1];
                }
            default:
                return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aWorld, int xCoord, int yCoord, int zCoord, int aSide) {
        int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        return getIcon(aSide, tMeta);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        for (int i = 0; i < NUMBER_OF_CASINGS; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
