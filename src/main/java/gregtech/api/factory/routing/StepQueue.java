package gregtech.api.factory.routing;

import gregtech.api.factory.IRouteInfo;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
            // Remove the front and shift all later elements down one.
            // This is more correct than removing the last element and the chances of a NetworkStepList existing are
            // very low.
            NetworkStep<N, R> step = list.steps()
                .remove(0);

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

            ObjectArrayList<NetworkStep<N, R>> steps = new ObjectArrayList<>();

            steps.add(existingStep);
            steps.add(step);

            this.put(step.route(), new NetworkStepList<>(steps));
        } else if (existing instanceof NetworkStepList) {
            NetworkStepList<N, R> list = (NetworkStepList<N, R>) existing;
            list.steps()
                .add(step);

            this.put(step.route(), list);
        } else {
            this.put(step.route(), step);
        }
    }
}
