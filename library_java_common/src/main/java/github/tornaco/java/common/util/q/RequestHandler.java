package github.tornaco.java.common.util.q;

public interface RequestHandler<T> {
    void handleRequest(T request);
}
