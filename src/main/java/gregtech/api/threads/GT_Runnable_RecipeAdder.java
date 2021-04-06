package gregtech.api.threads;

import gregtech.common.GT_Proxy;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import static gregtech.api.enums.GT_Values.D2;
import static gregtech.api.threads.GT_Runnable_RecipeAsyncHandler.getLatch;

public class GT_Runnable_RecipeAdder implements Runnable{
     GT_Runnable_RecipeAdder(Collection<GT_Proxy.OreDictEventContainer> mEvents) {
        this.mEvents = mEvents;
    }

    private Collection<GT_Proxy.OreDictEventContainer> mEvents;
    @Override
    public void run() {
        System.out.println("Async Processing Started!");
        int size = 5;
        int sizeStep = mEvents.size() / 20 - 1;
        int max = mEvents.size();
//                    int container = 0;
        CountDownLatch latch = new CountDownLatch(max);
        if (D2) {
            System.out.println("Initial latch @ " + latch.getCount());
        }
        for (GT_Proxy.OreDictEventContainer tEvent : mEvents) {
            sizeStep--;
//                        if (D2) {
//                            container++;
//                            System.out.println("OreDictEventContainer: " + container + " of " + max);
//                        }
            if (sizeStep == 0) {
                System.out.println("Async-Baking : " + size + "%");
                sizeStep = mEvents.size() / 20 - 1;
                size += 5;
            }
            GT_Threads.getExecutorServiceMap()
                    .get(GT_Runnable_RecipeAdder.class)
                    .submit(() -> {
                        try {
                            if (D2) {
                                System.out.println("Start " + tEvent.mEvent.Name);
                            }
                            GT_Proxy.registerRecipesAsync(tEvent);
                            if (D2) {
                                //TODO: Research why this stops @ 1146, 1147 or 1148
                                System.out.println("Latch @ :" + latch.getCount());
                                System.out.println(tEvent.mEvent.Name+" done!");
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
            getLatch().countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Async Processing done!");
    }
}
