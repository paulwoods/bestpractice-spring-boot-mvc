package org.mrpaulwoods.bookmarks2web

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@ToString
class Tag {

    @GeneratedValue
    @Id
    Long id

    @Column(length = 100, unique = true)
    String name
}
