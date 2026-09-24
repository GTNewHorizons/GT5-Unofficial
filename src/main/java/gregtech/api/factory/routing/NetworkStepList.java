package gregtech.api.factory.routing;

import java.util.ArrayDeque;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record NetworkStepList<N, R> (ArrayDeque<NetworkStep<N, R>> steps) implements StepLike {

}
