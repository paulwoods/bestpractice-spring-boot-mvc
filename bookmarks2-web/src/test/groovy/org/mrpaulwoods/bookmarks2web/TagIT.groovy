package org.mrpaulwoods.bookmarks2web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagIT extends Specification {

    @Autowired
    TestRestTemplate testRestTemplate

    def "create a tag successfully returns CREATED and the tag"() {

        TagForm tagForm = new TagForm(name: "Alpha")

        when:
        def re = testRestTemplate.postForEntity("/tag", tagForm, Tag)

        then:
        re.statusCode == HttpStatus.CREATED
        re.body.id == 1
        re.body.name == "Alpha"
    }

    def "create a tag with a null body returns bad request"() {

        when:
        def re = testRestTemplate.postForEntity("/tag", null, ApiError)

        then:
        re.statusCode == HttpStatus.UNSUPPORTED_MEDIA_TYPE
        re.body.timestamp != null
        re.body.status == HttpStatus.UNSUPPORTED_MEDIA_TYPE.value().toString()
        re.body.error == "Unsupported Media Type"
        re.body.message == "Content type 'application/x-www-form-urlencoded;charset=UTF-8' not supported"
        re.body.errors == []
    }

    def "create a tag with a null name returns bad request"() {

        TagForm tagForm = new TagForm(name: null)

        when:
        def re = testRestTemplate.postForEntity("/tag", tagForm, ApiError)

        then:
        re.statusCode == HttpStatus.BAD_REQUEST
        re.body.timestamp != null
        re.body.status == HttpStatus.BAD_REQUEST.value().toString()
        re.body.error == "Bad Request"
        re.body.message == "Validation failed for object='tagForm'. Error count: 1"
        re.body.errors[0].objectName == "tagForm"
        re.body.errors[0].field == "name"
        re.body.errors[0].rejectedValue == null
        re.body.errors[0].code == "NotNull"
    }

    def "create a tag with a name > 100 chars returns bad request"() {

        TagForm tagForm = new TagForm(name: "a" * 101)

        when:
        def re = testRestTemplate.postForEntity("/tag", tagForm, ApiError)

        then:
        re.statusCode == HttpStatus.BAD_REQUEST
        re.body.timestamp != null
        re.body.status == HttpStatus.BAD_REQUEST.value().toString()
        re.body.error == "Bad Request"
        re.body.message == "Validation failed for object='tagForm'. Error count: 1"
        re.body.errors[0].objectName == "tagForm"
        re.body.errors[0].field == "name"
        re.body.errors[0].rejectedValue == "a" * 101
        re.body.errors[0].code == "Size"
    }

    def "read a tag by id"() {
        Tag bravo = create("Bravo")

        when:
        ResponseEntity<Tag> re = testRestTemplate.getForEntity("/tag/${bravo.id}", Tag)

        then:
        re.statusCode == HttpStatus.OK
        re.body.id == bravo.id
        re.body.name == "Bravo"
    }

    def "read a tag by invalid id (not found) returns not found"() {
        when:
        def re = testRestTemplate.getForEntity("/tag/99999", ApiError)

        then:
        re.statusCode == HttpStatus.NOT_FOUND
        re.body.timestamp != null
        re.body.status == HttpStatus.NOT_FOUND.toString()
        re.body.error == "NOT_FOUND"
        re.body.message == "Tag not found 99999"
        re.body.errors == []
    }

    def "read a tag by invalid id (not a number) returns bad request"() {
        when:
        ResponseEntity<Object> re = testRestTemplate.getForEntity("/tag/alpha", Object)

        then:
        re.statusCode == HttpStatus.BAD_REQUEST
        re.body.timestamp != null
        re.body.status == HttpStatus.BAD_REQUEST.toString()
        re.body.error == "xxx"
        re.body.message == "Tag not found 99999"
        re.body.errors == []
    }

    def "update a tag (success) return ok and the updated tag"() {
        Long id = create("Charlie").id
        TagForm tagForm2 = new TagForm(id: id, name: "Delta")

        when:
        ResponseEntity<Tag> re = put(id, tagForm2, Tag)

        then:
        re.statusCode == HttpStatus.OK
        re.body.id == id
        re.body.name == "Delta"

        when:
        re = testRestTemplate.getForEntity("/tag/$id", Tag)

        then:
        re.statusCode == HttpStatus.OK
        re.body.id == id
        re.body.name == "Delta"
    }

    def "update a tag (name is null) return bad request"() {
        Long id = create("Echo").id
        TagForm tagForm = new TagForm(id: id, name: null)

        when:
        ResponseEntity<ApiError> re = put(id, tagForm, ApiError)

        then:
        re.statusCode == HttpStatus.BAD_REQUEST
        re.body.id == id
        re.body.name == "Delta"
    }

    def "delete a tag (successfully) returns no content"() {
        Long id = create("Foxtrot").id
        TagForm tagForm = new TagForm(id: id, name: null)

        when:
        ResponseEntity<Object> re = testRestTemplate.execute(
                "/tag/$id",
                HttpMethod.DELETE,
                null,
                null,
                null);

        then:
        re.statusCode == HttpStatus.OK
        re.body.id == id
        re.body.name == "Delta"
    }


    /*
    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Long id) {
        tagService.delete id
        ResponseEntity.noContent().build()
    }

     */
    protected Tag create(String name) {
        TagForm tagForm1 = new TagForm(name: name)
        def re = testRestTemplate.postForEntity("/tag", tagForm1, Tag)
        assert re.statusCode == HttpStatus.CREATED
        re.body
    }

    protected <T> ResponseEntity<T> put(Long id, TagForm tagForm, Class<T> clazz) {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        HttpEntity<TagForm> request = new HttpEntity<>(tagForm, httpHeaders)
        testRestTemplate.exchange("/tag/$id".toString(), HttpMethod.PUT, request, clazz)
    }

    protected ResponseEntity<Tag> delete(Long id) {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        HttpEntity<TagForm> request = new HttpEntity<>(httpHeaders)

        testRestTemplate.exchange("/tag/$id".toString(), HttpMethod.PUT, request, Tag)
    }

}
