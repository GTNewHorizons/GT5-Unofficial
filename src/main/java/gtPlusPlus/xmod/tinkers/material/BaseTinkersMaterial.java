package gtPlusPlus.xmod.tinkers.material;

import static gregtech.api.enums.Mods.TinkerConstruct;
import static gtPlusPlus.core.util.math.MathUtils.safeCast_LongToInt;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.common.event.FMLInterModComms;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.tinkers.HANDLER_Tinkers;
import gtPlusPlus.xmod.tinkers.util.TinkersUtils;

public class BaseTinkersMaterial {

    private static HashMap<String, Integer> aInternalMaterialIdMap = new HashMap<>();
    private static int aNextFreeID;

    public final String mLocalName;

    private final String mUnlocalName;
    private final int mID;
    private final Material mMaterial;

    static {
        aNextFreeID = (Short.MAX_VALUE / 2) + 420;
    }

    public BaseTinkersMaterial(Material aMaterial) {
        mLocalName = aMaterial.getLocalizedName();
        mUnlocalName = "material.gtpp." + Utils.sanitizeString(mLocalName);
        mMaterial = aMaterial;
        mID = aNextFreeID++;
        Logger.INFO("[TiCon] Assigning ID " + mID + " to " + mLocalName + ".");
        aInternalMaterialIdMap.put(mUnlocalName, mID);
        HANDLER_Tinkers.mTinkerMaterials.put(this);
    }

    public String getUnlocalName() {
        return mUnlocalName;
    }

    private static int calcDurability(Material aMaterial) {
        return safeCast_LongToInt(aMaterial.vDurability);
    }

    private static int calcMiningSpeed(Material aMaterial) {
        return (aMaterial.vHarvestLevel * 2) + aMaterial.vTier;
    }

    private static int calcHarvestLevel(Material aMaterial) {
        return aMaterial.vHarvestLevel;
    }

    private static int calcAttack(Material aMaterial) {
        return aMaterial.vHarvestLevel + aMaterial.vTier + aMaterial.vRadiationLevel;
    }

    private static float calcHandleModifier(Material aMaterial) {
        return 1f;
    }

    private static int calcReinforced(Material aMaterial) {
        return aMaterial.getMeltingPointC() / 3600;
    }

    private static int calcBowProjectileSpeed(Material aMaterial) {
        return aMaterial.vHarvestLevel + 2;
    }

    private static int calcBowDrawSpeed(Material aMaterial) {
        return aMaterial.vHarvestLevel + 8;
    }

    private static float calcProjectileMass(Material aMaterial) {
        return (aMaterial.getMeltingPointC() / 1800) * 0.1f;
    }

    private static float calcProjectileFragility(Material aMaterial) {
        return 0f;
    }

    private static String calcStyle(Material aMaterial) {
        String aReturn;
        int aTemp = aMaterial.getMeltingPointC();
        if (aTemp < 3600) {
            aReturn = "" + EnumChatFormatting.WHITE;
        } else if (aTemp >= 3600) {
            aReturn = "" + EnumChatFormatting.YELLOW;
        } else if (aTemp >= (3600 * 2)) {
            aReturn = "" + EnumChatFormatting.GREEN;
        } else if (aTemp >= (3600 * 3)) {
            aReturn = "" + EnumChatFormatting.RED;
        } else if (aTemp >= (3600 * 4)) {
            aReturn = "" + EnumChatFormatting.DARK_RED;
        } else {
            aReturn = "" + EnumChatFormatting.GOLD;
        }
        return aReturn;
    }

    private static int calcColour(Material aMaterial) {
        return aMaterial.getRgbAsHex();
    }

    public void generate() {

        Logger.INFO("[TiCon] Trying to generate Material: " + mLocalName);
        int id = mID;
        if (id > 0) {

            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("Id", id);
            tag.setString("Name", mUnlocalName);
            tag.setString("localizationString", mLocalName);
            tag.setInteger("Durability", calcDurability(mMaterial)); // 97
            tag.setInteger("MiningSpeed", calcMiningSpeed(mMaterial)); // 150
            tag.setInteger("HarvestLevel", calcHarvestLevel(mMaterial)); // 1
            tag.setInteger("Attack", calcAttack(mMaterial)); // 0
            tag.setFloat("HandleModifier", calcHandleModifier(mMaterial)); // 1.0f
            tag.setInteger("Reinforced", calcReinforced(mMaterial)); // 0
            tag.setFloat("Bow_ProjectileSpeed", calcBowProjectileSpeed(mMaterial)); // 3.0f
            tag.setInteger("Bow_DrawSpeed", calcBowDrawSpeed(mMaterial)); // 18
            tag.setFloat("Projectile_Mass", calcProjectileMass(mMaterial)); // 0.69f
            tag.setFloat("Projectile_Fragility", calcProjectileFragility(mMaterial)); // 0.2f
            tag.setString("Style", calcStyle(mMaterial));
            tag.setInteger("Color", calcColour(mMaterial));

            boolean generate = generateRecipes(mMaterial, id);

            if (generate) {
                Logger.INFO("[TiCon] Sending IMC: addMaterial - " + mLocalName + ".");
                FMLInterModComms.sendMessage(TinkerConstruct.ID, "addMaterial", tag);

                ItemStack itemstack = mMaterial.getIngot(1);
                tag = new NBTTagCompound();
                tag.setInteger("MaterialId", id);
                NBTTagCompound item = new NBTTagCompound();
                itemstack.writeToNBT(item);
                tag.setTag("Item", item);
                tag.setInteger("Value", 2); // What is value for?

                Logger.INFO("[TiCon] Sending IMC: addPartBuilderMaterial - " + mLocalName + ".");
                FMLInterModComms.sendMessage(TinkerConstruct.ID, "addPartBuilderMaterial", tag);

                tag = new NBTTagCompound();
                tag.setInteger("MaterialId", id);
                tag.setInteger("Value", 2); // What is value for?
                item = new NBTTagCompound();
                itemstack.writeToNBT(item);
                tag.setTag("Item", item);

                Logger.INFO("[TiCon] Sending IMC: addMaterialItem - " + mLocalName + ".");
                FMLInterModComms.sendMessage(TinkerConstruct.ID, "addMaterialItem", tag);
            }
        }
    }

    private boolean generateRecipes(Material aMaterial, int aID) {

        Block aMatBlock;
        Integer aMelt;
        Fluid aFluid;

        try {
            aMatBlock = aMaterial.getBlock();
            aMelt = aMaterial.getMeltingPointC();
            aFluid = aMaterial.getFluidStack(0).getFluid();
        } catch (Throwable t) {
            return false;
        }

        if (aMatBlock == null || aMelt == null || aFluid == null) {
            return false;
        }

        TinkersUtils.registerFluidType(mLocalName, aMatBlock, 0, aMelt, aFluid, true);
        TinkersUtils.addMelting(aMaterial.getBlock(1), aMatBlock, 0, aMelt, aMaterial.getFluidStack(144 * 9));
        TinkersUtils.addMelting(aMaterial.getIngot(1), aMatBlock, 0, aMelt, aMaterial.getFluidStack(144));
        if (aMelt <= 3600) {
            ItemStack ingotcast = TinkersUtils.getPattern(1);
            TinkersUtils.addBasinRecipe(
                    aMaterial.getBlock(1),
                    aMaterial.getFluidStack(144 * 9),
                    (ItemStack) null,
                    true,
                    100);
            TinkersUtils
                    .addCastingTableRecipe(aMaterial.getIngot(1), aMaterial.getFluidStack(144), ingotcast, false, 50);
        }

        TinkersUtils.generateCastingRecipes(aMaterial, aID);

        return true;
    }
}
