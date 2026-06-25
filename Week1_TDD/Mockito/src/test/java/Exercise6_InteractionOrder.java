import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.Mockito.inOrder;

public class Exercise6_InteractionOrder {

    @Test
    public void testMethodCallOrder() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        MyService service = new MyService(mockApi);
        service.processAll();

        InOrder order = inOrder(mockApi);
        order.verify(mockApi).getData();
        order.verify(mockApi).sendNotification("Processing done");
    }

    @Test
    public void testFetchThenNotify() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        MyService service = new MyService(mockApi);
        service.fetchData();
        service.notifyUser("done");

        InOrder order = inOrder(mockApi);
        order.verify(mockApi).getData();
        order.verify(mockApi).sendNotification("done");
    }
}
