package org.mrpaulwoods.bookmarks2web

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
@Slf4j
class TagService {

    private TagRepository tagRepository

    TagService(
            TagRepository tagRepository
    ) {
        this.tagRepository = tagRepository
    }

    Tag create(TagForm tagForm) {
        create new Tag(name: tagForm.name)
    }

    Tag create(Tag tag) {
        tagRepository.save tag
    }

    Tag read(Long id) {
        Optional o = tagRepository.findById(id)
        if (o.isPresent()) {
            o.get()
        } else {
            throw new TagNotFoundException(id)
        }
    }

    Tag update(Long id, TagForm tagForm) {
        Tag tag = read(id)
        tag.name = tagForm.name
        tagRepository.save tag
    }

    void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id)
        }
        tagRepository.deleteById id
    }

    void delete(Tag tag) {
        if (!tagRepository.existsById(tag.id)) {
            throw new TagNotFoundException(tag.id)
        }
        tagRepository.delete tag
    }

    List<Tag> list() {
        tagRepository.findAll().toList()
    }

    Tag findByName(String name) {
        tagRepository.findByName name
    }

    void checkExists(TagForm tagForm) {
        if (null != findByName(tagForm.name)) {
            throw new TagExistsException("The tag name exists.", "tagForm", "name", tagForm.name)
        }
    }

}
