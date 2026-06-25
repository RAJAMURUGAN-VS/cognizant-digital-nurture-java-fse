import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class Exercise5_MultipleReturns {

    @Test
    public void testConsecutiveReturnValues() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        when(mockApi.getData())
            .thenReturn("First Call")
            .thenReturn("Second Call")
            .thenReturn("Third Call");

        MyService service = new MyService(mockApi);

        assertEquals("First Call", service.fetchData());
        assertEquals("Second Call", service.fetchData());
        assertEquals("Third Call", service.fetchData());
        assertEquals("Third Call", service.fetchData());
    }

    @Test
    public void testTwoConsecutiveIdLookups() {
        ExternalApi mockApi = Mockito.mock(ExternalApi.class);

        when(mockApi.getDataById(1))
            .thenReturn("Alpha")
            .thenReturn("Alpha Updated");

        MyService service = new MyService(mockApi);

        assertEquals("Alpha", service.fetchDataById(1));
        assertEquals("Alpha Updated", service.fetchDataById(1));
    }
}
