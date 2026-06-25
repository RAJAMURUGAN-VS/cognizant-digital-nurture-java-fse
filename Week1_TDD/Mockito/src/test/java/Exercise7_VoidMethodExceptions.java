import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class Exercise7_VoidMethodExceptions {

    @Test
    public void testVoidMethodThrowsException() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        doThrow(new RuntimeException("Notification failed"))
            .when(mockApi).sendNotification("bad message");

        MyService service = new MyService(mockApi);

        assertThrows(RuntimeException.class, () -> {
            service.notifyUser("bad message");
        });

        verify(mockApi).sendNotification("bad message");
    }

    @Test
    public void testDeleteThrowsException() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        doThrow(new IllegalArgumentException("Record not found"))
            .when(mockApi).deleteRecord(999);

        MyService service = new MyService(mockApi);

        assertThrows(IllegalArgumentException.class, () -> {
            service.removeRecord(999);
        });

        verify(mockApi).deleteRecord(999);
    }
}
