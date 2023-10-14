package kubatech.mixin.mixins.minecraft;

import static kubatech.mixin.MixinsVariablesHelper.currentlyTranslating;

import java.util.regex.Matcher;

import net.minecraft.util.StringTranslate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import kubatech.Tags;

@SuppressWarnings("unused")
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
    private static String kubatech$replaceAll(Matcher matcher, String replace) {
        if (currentlyTranslating != null && currentlyTranslating.equals(Tags.MODID) && matcher.find()) {
            return matcher.replaceFirst(matcher.group());
        }
        return matcher.replaceAll(replace);
    }
}
