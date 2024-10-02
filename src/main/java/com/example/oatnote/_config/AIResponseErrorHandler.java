package com.example.oatnote._config;

import java.io.IOException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.example.oatnote.web.exception.server.OatExternalServiceException;

@Component
public class AIResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.is4xxClientError()) {
            throw OatExternalServiceException.withDetail("BE Client 오류", statusCode.toString());
        } else if (statusCode.is5xxServerError()) {
            throw OatExternalServiceException.withDetail("AI Server 오류", statusCode.toString());
        }
    }
}
