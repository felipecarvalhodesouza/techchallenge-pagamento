package br.com.postech.pagamento.infraestrutura.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HttpHelperTest {

    @Mock
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveEnviarRequisicaoPost() throws IOException {
        String requestBody = "{\"id\": \"1\", \"valorTotal\": \"50\"}";
        String expectedResponse = "Mocked Response";

        URL mockUrl = mock(URL.class);
        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(expectedResponse.getBytes()));

        when(mockConnection.getOutputStream()).thenReturn(new TestOutputStream());
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

        HttpHelper httpHelper = new HttpHelper();
        String actualResponse = httpHelper.sendPostRequest(requestBody, mockUrl);

        verify(mockConnection).setRequestMethod("POST");
        verify(mockConnection).setRequestProperty("Content-Type", "application/json");
        verify(mockConnection).setDoOutput(true);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    private class TestOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {}
    }
}
