package liar.gamemvcservice.game.service;

public class ThreadServiceOnlyTest {
    protected int num;
    protected Thread[] threads;

    protected void setUpThead() {
        this.num = 5;
        threads = new Thread[num];
    }

    protected void runThreads() throws InterruptedException {
        for (int i = 0; i < num; i++) threads[i].start();
        for (int i = 0; i < num; i++) threads[i].join();
    }
}
