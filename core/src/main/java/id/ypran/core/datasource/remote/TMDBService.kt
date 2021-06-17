package id.ypran.core.datasource.remote

import androidx.lifecycle.LiveData
import id.ypran.core.datasource.remote.response.MovieListResponse
import id.ypran.core.datasource.remote.response.MovieResponse
import id.ypran.core.datasource.remote.response.SeriesListResponse
import id.ypran.core.series.domain.model.Series
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): MovieListResponse

    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Path("movie_id") movie_id: String?,
        @Query("api_key") apiKey: String?
    ): LiveData<ApiResponse<MovieResponse>>
}

interface SeriesApi {
    @GET("tv/top_rated")
    suspend fun getTopRatedSeries(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int
    ): SeriesListResponse

    @GET("tv/{tv_id}")
    fun getDetailTvShow(
        @Path("tv_id") tv_id: String?,
        @Query("api_key") apiKey: String?
    ): LiveData<ApiResponse<Series>>
}