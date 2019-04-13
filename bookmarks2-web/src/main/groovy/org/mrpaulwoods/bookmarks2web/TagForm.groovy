package org.mrpaulwoods.bookmarks2web

import groovy.transform.Canonical

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Canonical
class TagForm implements Serializable {

    private static final long serialVersionUID = 0

    Long id

    @NotNull(message = "The name must be set.")
    @Size(min = 1, max = 100, message = "The length of the name must be between 1 and 100 characters")
    String name
}
