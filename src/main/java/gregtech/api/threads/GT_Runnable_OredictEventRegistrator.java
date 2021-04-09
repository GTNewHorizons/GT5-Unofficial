package gregtech.api.threads;

import gregtech.common.GT_Proxy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import static gregtech.api.enums.GT_Values.D2;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GT_Runnable_OredictEventRegistrator implements Runnable {

    public static void registerRecipes(@NonNull Collection<GT_Proxy.OreDictEventContainer> mEvents) {
        GT_Threads.getOREDICT_EVENT_REGISTRATOR_POOL()
                .submit(new GT_Runnable_OredictEventRegistrator(mEvents));
    }

    private final Collection<GT_Proxy.OreDictEventContainer> mEvents;

    @Getter
    private static final CountDownLatch LATCH = new CountDownLatch(1);

    @Override
    public void run() {
        System.out.println("Async Processing Started!");
        int size = 5;
        int sizeStep = mEvents.size() / 20 - 1;
        final int max = mEvents.size();
        CountDownLatch latch = new CountDownLatch(max);
        if (D2) {
            System.out.println("Initial latch @ " + latch.getCount());
        }
        for (GT_Proxy.OreDictEventContainer tEvent : mEvents) {
            sizeStep--;
            if (sizeStep == 0) {
                System.out.println("Async-Baking : " + size + "%");
                sizeStep = mEvents.size() / 20 - 1;
                size += 5;
            }
            GT_Threads.getOREDICT_EVENT_REGISTRATOR_POOL()
                    .submit(() -> {
                        try {
                            if (D2) {
                                System.out.println("Start " + tEvent.mEvent.Name);
                            }
                            GT_Proxy.registerRecipesAsync(tEvent);
                            if (D2) {
                                System.out.println("Latch @ :" + latch.getCount());
                                System.out.println(tEvent.mEvent.Name + " done!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    });
        }

        System.out.println("Await Async Processing!");
        try {
            latch.await();
            getLATCH().countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Async Processing done!");
    }
}
