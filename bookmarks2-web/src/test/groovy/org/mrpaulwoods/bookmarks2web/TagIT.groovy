package org.mrpaulwoods.bookmarks2web


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import spock.lang.Specification

//@RunWith(SpringRunner)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagIT extends Specification {

    @Autowired
    TestRestTemplate testRestTemplate

    def "create a tag successfully returns CREATED and the tag"() {

        TagForm tagForm = new TagForm(name: "Alpha")

        when:
        ResponseEntity re = testRestTemplate.postForEntity("/tag", tagForm, Tag)

        then:
        re.statusCode == HttpStatus.CREATED
        re.body.id == 1
        re.body.name == "Alpha"
    }

    def "create a tag with a null body returns bad request"() {

        when:
        ResponseEntity re = testRestTemplate.postForEntity("/tag", null, Object)

        then:
        re.statusCode == HttpStatus.UNSUPPORTED_MEDIA_TYPE
    }

    def "create a tag with a null name returns bad request"() {

        TagForm tagForm = new TagForm(name: null)

        when:
        ResponseEntity<Object> re = testRestTemplate.postForEntity("/tag", tagForm, Object)

        then:
        re.statusCode == HttpStatus.BAD_REQUEST
    }

    def "create a tag with a name > 100 chars returns bad request"() {

        TagForm tagForm = new TagForm(name: "a" * 101)

        when:
        ResponseEntity<Object> re = testRestTemplate.postForEntity("/tag", tagForm, Object)

        then:
        re.statusCode == HttpStatus.BAD_REQUEST
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
        ResponseEntity<Object> re = testRestTemplate.getForEntity("/tag/99999", Object)

        then:
        re.statusCode == HttpStatus.NOT_FOUND
    }

    def "read a tag by invalid id (not a number) returns bad request"() {
        when:
        ResponseEntity<Object> re = testRestTemplate.getForEntity("/tag/alpha", Object)

        then:
        re.statusCode == HttpStatus.BAD_REQUEST
    }

    def "update a tag (success) return ok and the updated tag"() {
        Long id = create("Charlie").id
        TagForm tagForm2 = new TagForm(id: id, name: "Delta")

        when:
        ResponseEntity<Tag> re = put(id, tagForm2)

        then:
        re.statusCode == HttpStatus.OK
        re.body.id == id
        re.body.name == "Delta"

        when:
        re = testRestTemplate.getForEntity("/tag/${id}", Tag)

        then:
        re.statusCode == HttpStatus.OK
        re.body.id == id
        re.body.name == "Delta"
    }

    def "update a tag (name is null) return bad request"() {
        Long id = create("Echo").id
        TagForm tagForm = new TagForm(id: id, name: null)

        when:
        ResponseEntity<Object> re = put(id, tagForm)

        then:
        re.statusCode == HttpStatus.OK
        re.body.id == id
        re.body.name == "Delta"
    }

    protected Tag create(String name) {
        TagForm tagForm1 = new TagForm(name: name)
        def re = testRestTemplate.postForEntity("/tag", tagForm1, Tag)
        assert re.statusCode == HttpStatus.CREATED
        re.body
    }

    protected ResponseEntity<Tag> put(Long id, TagForm tagForm) {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        HttpEntity<TagForm> request = new HttpEntity<>(tagForm, httpHeaders)

        testRestTemplate.exchange("/tag/$id".toString(), HttpMethod.PUT, request, Tag)

    }

}
