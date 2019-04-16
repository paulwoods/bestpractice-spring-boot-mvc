package org.mrpaulwoods.bookmarks2web

import org.springframework.data.repository.CrudRepository

interface TagRepository extends CrudRepository<Tag, Long> {

    Tag findByName(String name)
}