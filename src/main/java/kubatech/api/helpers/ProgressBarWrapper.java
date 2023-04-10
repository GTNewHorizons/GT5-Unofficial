package kubatech.api.helpers;

import static kubatech.api.utils.ModUtils.isClientSided;

import java.io.IOException;

import kubatech.Tags;
import kubatech.api.LoaderReference;
import alexiil.mods.load.ProgressDisplayer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ProgressManager;

@SuppressWarnings("deprecation")
public class ProgressBarWrapper {

    ProgressManager.ProgressBar internalFMLBar;
    boolean isFMLBar;
    String name;
    int maxSteps;
    int steps = 0;

    public ProgressBarWrapper(String name, int steps) {
        if (!isClientSided) return;
        maxSteps = steps;
        this.name = name;
        if (!LoaderReference.BetterLoadingScreen) {
            internalFMLBar = ProgressManager.push(name, steps);
            isFMLBar = true;
            return;
        }
        isFMLBar = false;
    }

    public void step(String message) {
        if (!isClientSided) return;
        if (isFMLBar) internalFMLBar.step(message);
        else {
            steps++;
            try {
                ProgressDisplayer
                    .displayProgress(Tags.MODNAME + ": " + name + " -> " + message, (float) steps / (float) maxSteps);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Prevent game freeze
            FMLCommonHandler.instance()
                .processWindowMessages();
        }
    }

    public void end() {
        if (!isClientSided) return;
        if (isFMLBar) ProgressManager.pop(internalFMLBar);
    }
}
