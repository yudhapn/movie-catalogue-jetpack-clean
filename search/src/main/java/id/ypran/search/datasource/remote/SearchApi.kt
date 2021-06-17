package id.ypran.search.datasource.remote

import id.ypran.search.datasource.remote.response.SearchListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search/multi")
    suspend fun getSearchResult(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): SearchListResponse

}