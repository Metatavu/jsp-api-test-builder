package fi.metatavu.jsp.api.test.builder.auth

import fi.metatavu.jaxrs.test.functional.builder.auth.AccessTokenProvider
import fi.metatavu.jaxrs.test.functional.builder.auth.AuthorizedTestBuilderAuthentication
import fi.metatavu.jsp.api.client.infrastructure.ApiClient
import fi.metatavu.jsp.api.test.builder.TestBuilder
import fi.metatavu.jsp.api.test.builder.impl.*
import java.io.IOException

/**
 * Test builder authentication
 *
 * @author Antti Leppä
 * @author Jari Nykänen
 *
 * Constructor
 *
 * @param testBuilder test builder instance
 * @param accessTokenProvider access token provider
 */
class TestBuilderAuthentication(private val testBuilder: TestBuilder, accessTokenProvider: AccessTokenProvider) : AuthorizedTestBuilderAuthentication<ApiClient>(testBuilder, accessTokenProvider) {

  private var accessTokenProvider: AccessTokenProvider? = accessTokenProvider
  private var tags: TagTestBuilderResource? = null

  /**
   * Returns test builder resource for tags
   *
   * @return test builder resource for tags
   * @throws IOException thrown when authentication fails
   */
  @kotlin.jvm.Throws(IOException::class)
  fun tags(): TagTestBuilderResource {
    if (tags == null) {
      tags = TagTestBuilderResource(testBuilder, this.accessTokenProvider, createClient())
    }

    return tags!!
  }

  /**
   * Creates a API client
   *
   * @param accessToken access token
   * @return API client
   */
  override fun createClient(accessToken: String): ApiClient {
    val result = ApiClient(testBuilder.settings.apiBasePath)
    ApiClient.accessToken = accessToken
    return result
  }

}