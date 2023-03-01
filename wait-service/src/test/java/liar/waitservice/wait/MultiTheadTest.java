package liar.waitservice.wait;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiTheadTest extends MemberDummyInfo {
    protected void runTestWithLatch(int threadCount, Runnable runnable) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

    protected <T> void runTestWithLatch(int threadCount, TestTask<T> testTask, T[] arrays) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
                try {
                    arrays[finalIdx] = testTask.runTask();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

    protected <T> void runTestWithLatch(int threadCount, TestFinalIdxTask<T> testTask, T[] arrays) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int finalIdx = i;
            executorService.submit(() -> {
                try {
                    arrays[finalIdx] = testTask.runTask(finalIdx);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

}
