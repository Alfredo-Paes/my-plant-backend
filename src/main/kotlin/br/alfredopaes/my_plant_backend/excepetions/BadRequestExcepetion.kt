package br.alfredopaes.my_plant_backend.excepetions

import org.aspectj.bridge.Message
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(BAD_REQUEST)
class BadRequestExcepetion(
    message: String = BAD_REQUEST.reasonPhrase,
    cause: Throwable? = null
): IllegalArgumentException(message, cause)