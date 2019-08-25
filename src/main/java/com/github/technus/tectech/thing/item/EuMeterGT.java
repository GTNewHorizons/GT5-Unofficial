package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Reference;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;

import static com.github.technus.tectech.Reference.MODID;

public class EuMeterGT extends Item {
    public static EuMeterGT INSTANCE;

    private EuMeterGT() {
        setUnlocalizedName("em.EuMeterGT");
        setTextureName(MODID + ":itemEuMeterGT");
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity == null || aPlayer instanceof FakePlayer) {
            return aPlayer instanceof EntityPlayerMP;
        }
        if (aPlayer instanceof EntityPlayerMP && !aPlayer.isSneaking() && tTileEntity instanceof IGregTechTileEntity) {
            if (tTileEntity instanceof BaseMetaTileEntity) {
                GT_Utility.sendChatToPlayer(aPlayer, EnumChatFormatting.AQUA + "----- X:" + aX + " Y:" + aY + " Z:" + aZ + " D:" + aWorld.provider.dimensionId + " S:" + aSide + " -----");
                GT_Utility.sendChatToPlayer(aPlayer, "Stored energy: " + EnumChatFormatting.YELLOW + (((BaseMetaTileEntity) tTileEntity).getUniversalEnergyStored()) + EnumChatFormatting.RESET + '/' + EnumChatFormatting.GREEN + (((BaseMetaTileEntity) tTileEntity).getUniversalEnergyCapacity()));
                GT_Utility.sendChatToPlayer(aPlayer, "Stored EU: " + EnumChatFormatting.YELLOW + (((BaseMetaTileEntity) tTileEntity).getStoredEU()) + EnumChatFormatting.RESET + '/' + EnumChatFormatting.GREEN + (((BaseMetaTileEntity) tTileEntity).getEUCapacity()));
                GT_Utility.sendChatToPlayer(aPlayer, "Average I/O: " + EnumChatFormatting.YELLOW + (((BaseMetaTileEntity) tTileEntity).getAverageElectricInput()) + EnumChatFormatting.RESET + '/' + EnumChatFormatting.YELLOW + (((BaseMetaTileEntity) tTileEntity).getAverageElectricOutput()));
                GT_Utility.sendChatToPlayer(aPlayer, "Voltage I/O (max): " + EnumChatFormatting.GOLD + (((BaseMetaTileEntity) tTileEntity).getInputVoltage()) + EnumChatFormatting.RESET + '/' + EnumChatFormatting.GOLD + (((BaseMetaTileEntity) tTileEntity).getOutputVoltage()));
                GT_Utility.sendChatToPlayer(aPlayer, "Voltage I/O max: " + EnumChatFormatting.RED + (((BaseMetaTileEntity) tTileEntity).getMaxSafeInput()) + EnumChatFormatting.RESET + '/' + EnumChatFormatting.RED + (((BaseMetaTileEntity) tTileEntity).getMaxEnergyOutput()));
                GT_Utility.sendChatToPlayer(aPlayer, "Amperage I/O (max): " + EnumChatFormatting.GOLD + (((BaseMetaTileEntity) tTileEntity).getInputAmperage()) + EnumChatFormatting.RESET + '/' + EnumChatFormatting.GOLD + (((BaseMetaTileEntity) tTileEntity).getOutputAmperage()));
                GT_Utility.sendChatToPlayer(aPlayer, "Side capabilities: " + (((BaseMetaTileEntity) tTileEntity).inputEnergyFrom((byte) aSide) ? "input " : "") + (((BaseMetaTileEntity) tTileEntity).outputsEnergyTo((byte) aSide) ? "output " : ""));
                return true;
            } else if (tTileEntity instanceof BaseMetaPipeEntity) {
                if (((BaseMetaPipeEntity) tTileEntity).getMetaTileEntity() instanceof GT_MetaPipeEntity_Cable) {
                    ArrayList<String> tList = new ArrayList<>();
                    GT_Utility.getCoordinateScan(tList, aPlayer, aWorld, 1, aX, aY, aZ, aSide, hitX, hitY, hitZ);
                    for (String str : tList) {
                        GT_Utility.sendChatToPlayer(aPlayer, str);
                    }
                }
                return true;
            }
        }
        if (!(aPlayer instanceof EntityPlayerMP)) {
            GT_Utility.doSoundAtClient(Reference.MODID + ":fx_scan", 1, 1.0F, (double) aX, (double) aY, (double) aZ);
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_GENERAL);
        aList.add("Measures basic EU related stuff");
        aList.add(EnumChatFormatting.BLUE + "Just right click on blocks.");
    }

    public static void run() {
        INSTANCE = new EuMeterGT();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}