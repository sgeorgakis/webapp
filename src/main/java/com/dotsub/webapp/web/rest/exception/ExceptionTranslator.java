package com.dotsub.webapp.web.rest.exception;

import com.dotsub.webapp.config.Constants;
import com.dotsub.webapp.exception.WebAppException;
import com.dotsub.webapp.web.rest.view.ErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.Instant;

@RestControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(createView(Constants.ErrorCode.INVALID_ARGUMENTS_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(createView(Constants.ErrorCode.INVALID_ARGUMENTS_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(createView(Constants.ErrorCode.MEDIA_TYPE_NOT_SUPPORTED_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(createView(Constants.ErrorCode.INVALID_ARGUMENTS_ERROR));

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createView(Constants.ErrorCode.NO_HANDLER_FOUND_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(createView(Constants.ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorView> handleIOExceptionClass(IOException ex, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createView(Constants.ErrorCode.IO_ERROR_ERROR));
    }

    @ExceptionHandler(WebAppException.class)
    public ResponseEntity<ErrorView> handleWebAppException(WebAppException ex) {
        log.error(ex.getMessage());
        ErrorView errorView = new ErrorView();
        errorView.setTimestamp(Instant.now());
        errorView.setCode(ex.getCode());
        errorView.setMessage(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(errorView);
    }

    private ErrorView createView(Constants.ErrorCode error) {
        ErrorView errorView = new ErrorView();
        errorView.setTimestamp(Instant.now());
        errorView.setCode(error.getCode());
        errorView.setMessage(error.getMessage());
        return errorView;
    }
}
