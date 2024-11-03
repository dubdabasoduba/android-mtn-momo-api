package io.rekast.sdk.repository

import io.rekast.sdk.model.ProviderCallBackHost
import io.rekast.sdk.model.authentication.ApiUser
import io.rekast.sdk.model.authentication.credentials.AccessTokenCredentials
import io.rekast.sdk.model.authentication.credentials.BasicAuthCredentials
import io.rekast.sdk.network.service.products.CollectionService
import io.rekast.sdk.network.service.products.CommonService
import io.rekast.sdk.network.service.products.DisbursementsService
import io.rekast.sdk.repository.data.NetworkResult
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class DefaultRepositoryTest {

    private lateinit var defaultSource: DefaultSource
    private lateinit var commonService: CommonService
    private lateinit var disbursementsService: DisbursementsService
    private lateinit var collectionService: CollectionService
    private lateinit var repository: DefaultRepository

    @Before
    fun setUp() {
        defaultSource = mock()
        commonService = mock()
        disbursementsService = mock()
        collectionService = mock()
        repository = DefaultRepository(
            defaultSource,
            commonService,
            disbursementsService,
            collectionService,
            BasicAuthCredentials("",""),
            AccessTokenCredentials("")
        )
    }

    @Test
    @Ignore
    fun `test createApiUser success`() = runBlocking {
        val providerCallBackHost = ProviderCallBackHost("http://callback.host")
        val apiVersion = "v1"
        val uuid = "unique-uuid"
        val productSubscriptionKey = "subscription-key"

        //whenever(defaultSource.createApiUser(any(), any(), any(), any())).thenReturn(ApiUser())

        val result = repository.createApiUser(providerCallBackHost, apiVersion, uuid, productSubscriptionKey)

        result.collect { networkResult ->
            assert(networkResult is NetworkResult.Success)
        }
    }

    @Test
    @Ignore
    fun `test checkApiUser success`() = runBlocking {
        val apiVersion = "v1"
        val productSubscriptionKey = "subscription-key"

        //whenever(defaultSource.getApiUser( any(), any(), any())).thenReturn(ApiUser())

        val result = repository.checkApiUser(apiVersion, productSubscriptionKey)

        result.collect { networkResult ->
            assert(networkResult is NetworkResult.Success)
        }
    }

    @Test
    fun `test setUpBasicAuth updates credentials`() {
        val basicAuthCredentials = BasicAuthCredentials("userId", "apiKey")

        repository.setUpBasicAuth(basicAuthCredentials)

        assert(repository.getBasicAuth().apiUserId == "userId")
        assert(repository.getBasicAuth().apiKey == "apiKey")
    }

    @Test
    fun `test setUpAccessTokenAuth updates credentials`() {
        val accessTokenCredentials = AccessTokenCredentials("accessToken")

        repository.setUpAccessTokenAuth(accessTokenCredentials)

        assert(repository.getAccessTokenAuth().accessToken == "accessToken")
    }

}
