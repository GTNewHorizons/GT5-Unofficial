package gregtech.api.util;

import static gregtech.api.enums.GT_Values.E;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;

public class GT_Config implements Runnable {

    public static boolean troll = false;

    public static Configuration sConfigFileIDs;
    public static Configuration cleanroomFile;
    public static Configuration undergroundFluidsFile;
    public final Configuration mConfig;

    public GT_Config(Configuration aConfig) {
        mConfig = aConfig;
        mConfig.load();
        mConfig.save();
        GregTech_API.sAfterGTPreload.add(this); // in case of crash on startup
        GregTech_API.sAfterGTLoad.add(this); // in case of crash on startup
        GregTech_API.sAfterGTPostload.add(this);
        if (GT_Values.lateConfigSave) GregTech_API.sFirstWorldTick.add(this);
    }

    private static boolean shouldSave() {
        return GT_Values.lateConfigSave ? GT_Values.worldTickHappened : GregTech_API.sPostloadFinished;
    }

    public static int addIDConfig(Object aCategory, String aName, int aDefault) {
        if (GT_Utility.isStringInvalid(aName)) return aDefault;
        Property tProperty = sConfigFileIDs.get(
            aCategory.toString()
                .replaceAll("\\|", "."),
            aName.replaceAll("\\|", "."),
            aDefault);
        int rResult = tProperty.getInt(aDefault);
        sConfigFileIDs.save();
        return rResult;
    }

    public static String getStackConfigName(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return E;
        Object rName = GT_OreDictUnificator.getAssociation(aStack);
        if (rName != null) return rName.toString();
        try {
            if (GT_Utility.isStringValid(rName = aStack.getUnlocalizedName())) return rName.toString();
        } catch (Throwable e) {
            /* Do nothing */
        }
        String sName = aStack.getItem()
            .toString();
        String[] tmp = sName.split("@");
        if (tmp.length > 0) sName = tmp[0];
        return sName + "." + aStack.getItemDamage();
    }

    public boolean get(Object aCategory, ItemStack aStack, boolean aDefault) {
        String aName = getStackConfigName(aStack);
        return get(aCategory, aName, aDefault);
    }

    public boolean get(Object aCategory, String aName, boolean aDefault) {
        if (GT_Utility.isStringInvalid(aName)) return aDefault;
        Property tProperty = mConfig.get(
            aCategory.toString()
                .replaceAll("\\|", "_"),
            (aName + "_" + aDefault).replaceAll("\\|", "_"),
            aDefault);
        boolean rResult = tProperty.getBoolean(aDefault);
        if (!tProperty.wasRead() && shouldSave()) mConfig.save();
        return rResult;
    }

    public int get(Object aCategory, ItemStack aStack, int aDefault) {
        return get(aCategory, getStackConfigName(aStack), aDefault);
    }

    public int get(Object aCategory, String aName, int aDefault) {
        if (GT_Utility.isStringInvalid(aName)) return aDefault;
        Property tProperty = mConfig.get(
            aCategory.toString()
                .replaceAll("\\|", "_"),
            (aName + "_" + aDefault).replaceAll("\\|", "_"),
            aDefault);
        int rResult = tProperty.getInt(aDefault);
        if (!tProperty.wasRead() && shouldSave()) mConfig.save();
        return rResult;
    }

    public double get(Object aCategory, ItemStack aStack, double aDefault) {
        return get(aCategory, getStackConfigName(aStack), aDefault);
    }

    public double get(Object aCategory, String aName, double aDefault) {
        if (GT_Utility.isStringInvalid(aName)) return aDefault;
        Property tProperty = mConfig.get(
            aCategory.toString()
                .replaceAll("\\|", "_"),
            (aName + "_" + aDefault).replaceAll("\\|", "_"),
            aDefault);
        double rResult = tProperty.getDouble(aDefault);
        if (!tProperty.wasRead() && shouldSave()) mConfig.save();
        return rResult;
    }

    public String get(Object aCategory, ItemStack aStack, String aDefault) {
        return get(aCategory, getStackConfigName(aStack), aDefault);
    }

    public String get(Object aCategory, String aName, String aDefault) {
        if (GT_Utility.isStringInvalid(aName)) return aDefault;
        Property tProperty = mConfig.get(
            aCategory.toString()
                .replaceAll("\\|", "_"),
            (aName + "_" + aDefault).replaceAll("\\|", "_"),
            aDefault);
        String rResult = tProperty.getString();
        if (!tProperty.wasRead() && shouldSave()) mConfig.save();
        return rResult;
    }

    public String[] get(Object aCategory, ItemStack aStack, String... aDefault) {
        return get(aCategory, getStackConfigName(aStack), aDefault);
    }

    public String[] get(Object aCategory, String aName, String... aDefault) {
        if (GT_Utility.isStringInvalid(aName)) return aDefault;
        Property tProperty = mConfig.get(
            aCategory.toString()
                .replaceAll("\\|", "_"),
            aName.replaceAll("\\|", "_"),
            aDefault);
        String[] rResult = tProperty.getStringList();
        if (!tProperty.wasRead() && GregTech_API.sPostloadFinished) mConfig.save();
        return rResult;
    }

    public String getWithValidValues(Object aCategory, String aName, String[] validValues, String aDefault) {
        if (GT_Utility.isStringInvalid(aName)) return aDefault;
        Property tProperty = mConfig.get(
            aCategory.toString()
                .replaceAll("\\|", "_"),
            aName.replaceAll("\\|", "_"),
            aDefault,
            null,
            validValues);
        String rResult = tProperty.getString();
        if (!tProperty.wasRead() && GregTech_API.sPostloadFinished) mConfig.save();
        return rResult;
    }

    @Override
    public void run() {
        mConfig.save();
    }
}
