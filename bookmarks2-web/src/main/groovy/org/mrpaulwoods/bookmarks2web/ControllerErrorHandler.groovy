package org.mrpaulwoods.bookmarks2web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import java.time.ZonedDateTime

@ControllerAdvice
class ControllerErrorHandler {

    @ExceptionHandler(TagExistsException)
    ResponseEntity handle(TagExistsException ex) {
        ApiError error = new ApiError(status: HttpStatus.CONFLICT, error: HttpStatus.CONFLICT.name(), message: ex.message)
        error.errors << [
                codes         : [],
                arguments     : [],
                defaultMessage: ex.message,
                objectName    : ex.objectName,
                field         : ex.field,
                rejectedValue : ex.rejectedValue,
                bindingFailure: false,
                code          : "Exists"
        ]
        ResponseEntity.status(HttpStatus.CONFLICT).body(error)
    }

    @ExceptionHandler(DomainNotFoundException)
    ResponseEntity handle(DomainNotFoundException ex) {
        ApiError error = new ApiError(status: HttpStatus.NOT_FOUND, error: HttpStatus.NOT_FOUND.name(), message: ex.message)
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }

    class ApiError {
        ZonedDateTime timestamp = ZonedDateTime.now()
        String status
        String error
        final List<Map> errors = []
        String message
    }

}
