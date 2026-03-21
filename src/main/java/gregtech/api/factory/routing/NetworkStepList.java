package gregtech.api.factory.routing;

import com.github.bsideup.jabel.Desugar;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

@Desugar
public record NetworkStepList<N, R> (ObjectArrayList<NetworkStep<N, R>> steps) implements StepLike {

}
