import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class Exercise4_VoidMethods {

    @Test
    public void testVoidMethodDoesNothing() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        doNothing().when(mockApi).sendNotification("alert");

        MyService service = new MyService(mockApi);
        service.notifyUser("alert");

        verify(mockApi).sendNotification("alert");
    }

    @Test
    public void testVoidDeleteRecord() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        doNothing().when(mockApi).deleteRecord(10);

        MyService service = new MyService(mockApi);
        service.removeRecord(10);

        verify(mockApi).deleteRecord(10);
    }
}
