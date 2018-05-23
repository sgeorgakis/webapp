package com.dotsub.webapp.web.rest.exception;

import com.dotsub.webapp.config.Constants;
import com.dotsub.webapp.exception.WebAppException;
import com.dotsub.webapp.service.impl.LocalStorageServiceImpl;
import com.dotsub.webapp.web.rest.view.ErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.time.Instant;

@ControllerAdvice
public class ExceptionTranslator {

    private final Logger log = LoggerFactory.getLogger(LocalStorageServiceImpl.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorView> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .body(createView(Constants.ErrorCode.INVALID_ARGUMENTS_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorView> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createView(Constants.ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorView> handleNotHandlerFoundException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createView(Constants.ErrorCode.NO_HANDLER_FOUND_ERROR));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorView> handleIOExceptionClass(IOException ex) {
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
