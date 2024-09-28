package gregtech.mixin.mixins.early.minecraft;

import java.util.regex.Matcher;

import net.minecraft.util.StringTranslate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.mixin.hooks.MixinsVariablesHelper;
import kubatech.Tags;

@Mixin(value = StringTranslate.class)
public class StringTranslateMixin {

    @Redirect(
        method = "parseLangFile",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/regex/Matcher;replaceAll(Ljava/lang/String;)Ljava/lang/String;",
            remap = false),
        remap = false,
        require = 1)
    private static String gt5u$replaceAll(Matcher matcher, String replace) {
        if (MixinsVariablesHelper.currentlyTranslating != null
            && MixinsVariablesHelper.currentlyTranslating.equals(Tags.MODID)
            && matcher.find()) {
            return matcher.replaceFirst(matcher.group());
        }
        return matcher.replaceAll(replace);
    }
}
