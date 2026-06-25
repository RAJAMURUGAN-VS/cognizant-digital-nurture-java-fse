import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class Exercise1_MockingStubbing {

    @Test
    public void testExternalApi() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        when(mockApi.getData()).thenReturn("Mock Data");

        MyService service = new MyService(mockApi);
        String result = service.fetchData();

        assertEquals("Mock Data", result);
    }

    @Test
    public void testGetDataById() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        when(mockApi.getDataById(42)).thenReturn("Item 42");

        MyService service = new MyService(mockApi);
        String result = service.fetchDataById(42);

        assertEquals("Item 42", result);
    }
}
