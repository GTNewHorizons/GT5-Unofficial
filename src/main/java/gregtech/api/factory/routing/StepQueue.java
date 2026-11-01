package gregtech.api.factory.routing;

import java.util.ArrayDeque;

import gregtech.api.factory.IRouteInfo;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;

@SuppressWarnings("unchecked")
public class StepQueue<N, R extends IRouteInfo<R>> extends Object2ObjectRBTreeMap<R, StepLike> {

    public NetworkStep<N, R> takeFront() {
        if (isEmpty()) return null;

        R firstKey = this.firstKey();

        StepLike front = this.get(firstKey);

        if (front instanceof NetworkStep) {
            NetworkStep<N, R> step = (NetworkStep<N, R>) front;
            this.remove(firstKey);
            return step;
        } else if (front instanceof NetworkStepList) {
            NetworkStepList<N, R> list = (NetworkStepList<N, R>) front;
            NetworkStep<N, R> step = list.steps()
                .removeFirst();

            if (list.steps()
                .isEmpty()) {
                this.remove(firstKey);
            }

            return step;
        } else {
            throw new IllegalStateException("Invalid StepQueue value: " + front);
        }
    }

    public void add(NetworkStep<N, R> step) {
        StepLike existing = this.remove(step.route());

        if (existing instanceof NetworkStep) {
            NetworkStep<N, R> existingStep = (NetworkStep<N, R>) existing;

            ArrayDeque<NetworkStep<N, R>> steps = new ArrayDeque<>();

            steps.addLast(existingStep);
            steps.addLast(step);

            this.put(step.route(), new NetworkStepList<>(steps));
        } else if (existing instanceof NetworkStepList) {
            NetworkStepList<N, R> list = (NetworkStepList<N, R>) existing;
            list.steps()
                .addLast(step);

            this.put(step.route(), list);
        } else {
            this.put(step.route(), step);
        }
    }
}
