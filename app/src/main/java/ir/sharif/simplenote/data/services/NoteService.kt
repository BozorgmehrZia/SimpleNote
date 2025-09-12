package ir.sharif.simplenote.data.services

import ir.sharif.simplenote.data.model.NoteRequest
import ir.sharif.simplenote.data.model.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NoteService {
    @GET("notes/")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @GET("notes/{id}/")
    suspend fun getNoteById(@Path("id") id: Int): Response<NoteResponse>

    @POST("notes/")
    suspend fun createNote(@Body request: NoteRequest): Response<NoteResponse>

    @PUT("notes/{id}/")
    suspend fun updateNote(@Path("id") id: Int, @Body request: NoteRequest): Response<NoteResponse>

    @DELETE("notes/{id}/")
    suspend fun deleteNote(@Path("id") id: Int): Response<Unit>
}
