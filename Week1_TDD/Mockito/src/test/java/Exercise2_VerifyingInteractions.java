import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class Exercise2_VerifyingInteractions {

    @Test
    public void testVerifyInteraction() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        MyService service = new MyService(mockApi);
        service.fetchData();

        verify(mockApi).getData();
    }

    @Test
    public void testVerifyCalledExactTimes() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        MyService service = new MyService(mockApi);
        service.fetchData();
        service.fetchData();

        verify(mockApi, times(2)).getData();
    }

    @Test
    public void testVerifyNotificationSent() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        MyService service = new MyService(mockApi);
        service.notifyUser("Hello");

        verify(mockApi).sendNotification("Hello");
    }
}
