package gregtech.mixin.mixins.early.minecraft;

import java.util.regex.Matcher;

import net.minecraft.client.resources.Locale;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.mixin.hooks.MixinsVariablesHelper;
import kubatech.Tags;

@Mixin(value = Locale.class)
public class LocaleMixin {

    @ModifyArg(
        method = "loadLocaleDataFiles",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/IResourceManager;getAllResources(Lnet/minecraft/util/ResourceLocation;)Ljava/util/List;"),
        index = 0,
        require = 1)
    private ResourceLocation gt5u$loadLocaleDataFiles(ResourceLocation resourceLocation) {
        MixinsVariablesHelper.currentlyTranslating = resourceLocation.getResourceDomain();
        return resourceLocation;
    }

    @Redirect(
        method = "loadLocaleData(Ljava/io/InputStream;)V",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/regex/Matcher;replaceAll(Ljava/lang/String;)Ljava/lang/String;",
            remap = false),
        require = 1)
    private String gt5u$replaceAll(Matcher matcher, String replace) {
        if (MixinsVariablesHelper.currentlyTranslating != null
            && MixinsVariablesHelper.currentlyTranslating.equals(Tags.MODID)
            && matcher.find()) {
            return matcher.replaceFirst(matcher.group());
        }
        return matcher.replaceAll(replace);
    }

}
