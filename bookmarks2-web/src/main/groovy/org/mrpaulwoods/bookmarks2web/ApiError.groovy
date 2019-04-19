package org.mrpaulwoods.bookmarks2web

import java.time.OffsetDateTime

final class ApiError {
    OffsetDateTime timestamp = OffsetDateTime.now()
    String status
    String error
    String message
    final List<Map> errors = []

}
