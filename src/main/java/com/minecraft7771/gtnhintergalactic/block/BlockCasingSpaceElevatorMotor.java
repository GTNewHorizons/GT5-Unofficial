package com.minecraft7771.gtnhintergalactic.block;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import com.minecraft7771.gtnhintergalactic.GTNHIntergalactic;
import com.minecraft7771.gtnhintergalactic.item.IGItems;
import com.minecraft7771.gtnhintergalactic.item.ItemCasingSpaceElevatorMotor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;

public class BlockCasingSpaceElevatorMotor extends GT_Block_Casings_Abstract {

    public static BlockCasingSpaceElevatorMotor INSTANCE;
    private static final int NUMBER_OF_MOTORS = 5;
    private static IIcon IconSEMotorTop;
    private static IIcon[] IconSEMotorSide = new IIcon[NUMBER_OF_MOTORS];

    public BlockCasingSpaceElevatorMotor() {
        super(ItemCasingSpaceElevatorMotor.class, "gt.blockcasingsSEMotor", GT_Material_Casings.INSTANCE);

        for (byte b = 0; b < NUMBER_OF_MOTORS; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[32][b + 16] = new GT_CopiedBlockTexture(this, 6, b);
        }

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Space Elevator Motor MK-I");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Space Elevator Motor MK-II");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Space Elevator Motor MK-III");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Space Elevator Motor MK-IV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Space Elevator Motor MK-V");

        IGItems.SpaceElevatorMotorT1 = new ItemStack(this, 1, 0);
        IGItems.SpaceElevatorMotorT2 = new ItemStack(this, 1, 1);
        IGItems.SpaceElevatorMotorT3 = new ItemStack(this, 1, 2);
        IGItems.SpaceElevatorMotorT4 = new ItemStack(this, 1, 3);
        IGItems.SpaceElevatorMotorT5 = new ItemStack(this, 1, 4);
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        IconSEMotorTop = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/Motor");
        IconSEMotorSide[0] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT1_Side");
        IconSEMotorSide[1] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT2_Side");
        IconSEMotorSide[2] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT3_Side");
        IconSEMotorSide[3] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT4_Side");
        IconSEMotorSide[4] = aIconRegister.registerIcon(GTNHIntergalactic.ASSET_PREFIX + ":spaceElevator/MotorT5_Side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        if (aSide == 0 || aSide == 1) {
            return IconSEMotorTop;
        } else {
            if (aMeta < NUMBER_OF_MOTORS) {
                return IconSEMotorSide[aMeta];
            }
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
        for (int i = 0; i < NUMBER_OF_MOTORS; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
    }
}
