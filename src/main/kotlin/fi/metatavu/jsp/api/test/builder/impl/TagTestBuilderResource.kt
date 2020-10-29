package fi.metatavu.jsp.api.test.builder.impl

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.jsp.api.client.apis.TagsApi
import fi.metatavu.jsp.api.client.infrastructure.ApiClient
import fi.metatavu.jsp.api.client.infrastructure.ClientException
import fi.metatavu.jsp.api.client.models.Tag
import fi.metatavu.jsp.api.test.builder.TestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.util.*

/**
 * Test builder resource for handling tags
 */
class TagTestBuilderResource(testBuilder: TestBuilder, val accessTokenProvider: AccessTokenProvider?, apiClient: ApiClient) : ApiTestBuilderResource<Tag, ApiClient?>(testBuilder, apiClient) {

    /**
     * Creates a new tag with default name
     *
     * @param payload payload tag
     * @return created tag
     */
    fun create(): Tag {
        return create("Test Tag")
    }

    /**
     * Creates a new tag
     *
     * @param payload payload tag
     * @return created tag
     */
    fun create(name: String): Tag {
        val payload = Tag(name)
        val result: Tag = this.api.createTag(payload)
        addClosable(result)
        return result
    }

    /**
     * Lists tags
     *
     * @return tags
     */
    fun listTags(): Array<Tag> {
        return api.listTags()
    }

    /**
     * Updates tag
     *
     * @param body update body
     * @return updated tag
     */
    fun updateTag(body: Tag): Tag? {
        return api.updateTag(body)
    }

    /**
     * Deletes a tag from the API
     *
     * @param tag tag id
     */
    fun delete(tag: Tag) {
        api.deleteTag(tag)
        removeCloseable { closable: Any ->
            if (closable !is Tag) {
                return@removeCloseable false
            }

            val closeableExhibitionRoom: Tag = closable
            closeableExhibitionRoom.id!!.equals(tag.id)
        }
    }

    /**
     * Asserts tag count within the system
     *
     * @param expected expected count
     */
    fun assertCount(expected: Int) {
        assertEquals(expected, api.listTags().size)
    }

    /**
     * Asserts list status fails with given status code
     *
     * @param expectedStatus expected status
     */
    fun assertListFail(expectedStatus: Int) {
        try {
            api.listTags()
            fail(String.format("Expected list to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts create fails with given status
     *
     * @param expectedStatus expected status
     * @param name tag name
     */
    fun assertCreateFail(expectedStatus: Int, name: String) {
        try {
            create(name)
            fail(String.format("Expected create to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts update fails with given status
     *
     * @param expectedStatus expected status
     * @param body body
     */
    fun assertUpdateFail(expectedStatus: Int,  body: Tag) {
        try {
            updateTag(body)
            fail(String.format("Expected update to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    /**
     * Asserts delete fails with given status
     *
     * @param expectedStatus expected status
     * @param tag tag
     * @param id id
     */
    fun assertDeleteFail(expectedStatus: Int, tag: Tag) {
        try {
            delete(tag)
            fail(String.format("Expected delete to fail with message %d", expectedStatus))
        } catch (e: ClientException) {
            assertClientExceptionStatus(expectedStatus, e)
        }
    }

    override fun clean(tag: Tag) {
        this.api.deleteTag(tag)
    }

    override fun getApi(): TagsApi {
        ApiClient.accessToken = accessTokenProvider?.accessToken
        return TagsApi(testBuilder.settings.apiBasePath)
    }
}