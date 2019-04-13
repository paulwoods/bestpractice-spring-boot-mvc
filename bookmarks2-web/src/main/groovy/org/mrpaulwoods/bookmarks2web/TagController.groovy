package org.mrpaulwoods.bookmarks2web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
@RequestMapping("/tag")
class TagController {

    private TagService tagService

    TagController(
            TagService tagService
    ) {
        this.tagService = tagService
    }

    @GetMapping
    ResponseEntity<List<Tag>> index() {
        ResponseEntity.ok tagService.list()
    }

    @PostMapping
    ResponseEntity<Tag> create(@RequestBody @Valid TagForm tagForm) {
        tagService.checkExists tagForm
        Tag tag = tagService.create(tagForm)
        ResponseEntity.created("/tag/${tag.id}".toURI()).body(tag)
    }

    @GetMapping("/{id}")
    ResponseEntity<Tag> read(@PathVariable Long id) {
        ResponseEntity.ok tagService.read(id)
    }

    @PutMapping("/{id}")
    ResponseEntity<Tag> update(@PathVariable Long id, @RequestBody @Valid TagForm tagForm) {
        ResponseEntity.ok tagService.update(id, tagForm)
    }

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        tagService.delete id
        ResponseEntity.noContent().build()
    }

    @GetMapping("/findByName")
    ResponseEntity findByName(@RequestParam("name") String name) {
        ResponseEntity.ok tagService.findByName(name)
    }

}
