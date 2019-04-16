package org.mrpaulwoods.bookmarks2web

import java.time.ZonedDateTime

final class ApiError {
    ZonedDateTime timestamp = ZonedDateTime.now()
    String status
    String error
    final List<Map> errors = []
    String message

}
