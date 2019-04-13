package org.mrpaulwoods.bookmarks2web

class TagExistsException extends RuntimeException {
    String message
    String objectName
    String field
    String rejectedValue

    TagExistsException(String message, String objectName, String field, String rejectedValue) {
        super(message)
        this.message = message
        this.objectName = objectName
        this.field = field
        this.rejectedValue = rejectedValue
    }


}
