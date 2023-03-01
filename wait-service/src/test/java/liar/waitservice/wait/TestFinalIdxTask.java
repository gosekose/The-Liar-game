package liar.waitservice.wait;

@FunctionalInterface
public interface TestFinalIdxTask<T> {

    T runTask(int idx);

}
