package liar.waitservice.wait;

@FunctionalInterface
public interface TestTask<T> {
    T runTask();
}
