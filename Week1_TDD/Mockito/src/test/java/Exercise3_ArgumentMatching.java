import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Exercise3_ArgumentMatching {

    @Test
    public void testWithAnyIntMatcher() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        when(mockApi.getDataById(anyInt())).thenReturn("some data");

        MyService service = new MyService(mockApi);
        String result = service.fetchDataById(999);

        assertNotNull(result);
        verify(mockApi).getDataById(anyInt());
    }

    @Test
    public void testWithAnyStringMatcher() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        MyService service = new MyService(mockApi);
        service.notifyUser("any message here");

        verify(mockApi).sendNotification(anyString());
    }

    @Test
    public void testWithExactArgument() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        when(mockApi.getDataById(5)).thenReturn("Product 5");

        MyService service = new MyService(mockApi);
        String result = service.fetchDataById(5);

        verify(mockApi).getDataById(5);
    }
}
