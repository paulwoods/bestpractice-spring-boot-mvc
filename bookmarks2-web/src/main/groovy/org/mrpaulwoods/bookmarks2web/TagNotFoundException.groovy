package org.mrpaulwoods.bookmarks2web

class TagNotFoundException extends DomainNotFoundException {

    TagNotFoundException(Long id) {
        super("Tag not found $id")
    }

}
