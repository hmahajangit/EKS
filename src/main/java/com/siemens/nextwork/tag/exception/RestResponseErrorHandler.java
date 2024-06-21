package com.siemens.nextwork.tag.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class RestResponseErrorHandler implements ResponseErrorHandler {

    /**
     * Indicate whether the given response has any errors.
     * <p>Implementations will typically inspect the
     * {@link ClientHttpResponse#getStatusCode() HttpStatus} of the response.
     *
     * @param response the response to inspect
     * @return {@code true} if the response indicates an error; {@code false} otherwise
     * @throws IOException in case of I/O errors
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatusCode rawStatusCode = response.getStatusCode();
        return hasError(rawStatusCode.value());
    }

    /**
     * Handle the error in the given response.
     * <p>This method is only called when {@link #hasError(ClientHttpResponse)}
     * has returned {@code true}.
     *
     * @param response the response with the error
     * @throws IOException in case of I/O errors
     */
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = HttpStatus.resolve(response.getStatusCode().value());
        if (statusCode == null) {
            String message = getErrorMessage(getResponseBody(response), getCharset(response));
            throw new UnknownHttpStatusCodeException(message,
                    response.getStatusCode().value(), response.getStatusText(),
                    response.getHeaders(), getResponseBody(response), getCharset(response));
        }
        handleError(response, statusCode);
    }


    protected boolean hasError(HttpStatus statusCode) {
        return statusCode.isError();
    }

    protected boolean hasError(int unknownStatusCode) {
        HttpStatus.Series series = HttpStatus.Series.resolve(unknownStatusCode);
        return (series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR);
    }


    private String getErrorMessage(@Nullable byte[] responseBody, @Nullable Charset charset) {
        if (ObjectUtils.isEmpty(responseBody)) {
            return "{\"error\" : \"no body\"}";
        }

        charset = charset == null ? StandardCharsets.UTF_8 : charset;
        int maxChars = 200;

        if (responseBody.length < maxChars * 2) {
            return new String(responseBody, charset);
        }

        try {
            Reader reader = new InputStreamReader(new ByteArrayInputStream(responseBody), charset);
            CharBuffer buffer = CharBuffer.allocate(maxChars);
            reader.read(buffer);
            reader.close();
            buffer.flip();
            return buffer.toString() + "... (" + responseBody.length + " bytes)";
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        String statusText = response.getStatusText();
        HttpHeaders headers = response.getHeaders();
        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);
        String message = getErrorMessage(body, charset);

        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw HttpClientErrorException.create(message, statusCode, statusText, headers, body, charset);
            case SERVER_ERROR:
                throw HttpServerErrorException.create(message, statusCode, statusText, headers, body, charset);
            default:
                throw new UnknownHttpStatusCodeException(message, statusCode.value(), statusText, headers, body, charset);
        }
    }

    protected byte[] getResponseBody(ClientHttpResponse response) {
        try {
            return FileCopyUtils.copyToByteArray(response.getBody());
        } catch (IOException ignore) {
        }
        return new byte[0];
    }

    protected Charset getCharset(ClientHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        return (contentType != null ? contentType.getCharset() : null);
    }
}
