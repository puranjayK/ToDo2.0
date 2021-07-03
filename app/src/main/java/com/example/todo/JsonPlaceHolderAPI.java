package com.example.todo;

import com.example.todo.model.ToDoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderAPI {

    @GET("auth/profile/")
    Call<Profile> getProfile(@Header("Authorization") String token);

    @POST("auth/login/")
    Call<Login> login(@Body Login login);

    @POST("auth/register/")
    Call<Register> createAcc(@Body Register register);

    @GET("todo/")
    Call<List<ToDoModel>> getToDo(@Header("Authorization") String token);

    @PATCH("todo/{id}/")
    Call<ToDoModel> updateToDo(
                                @Header("Authorization") String token,
                                @Path("id")int id,
                                @Body ToDo toDo);
    @POST("todo/create/")
    Call<Void> createToDo(
            @Header("Authorization") String token,
            @Body ToDo toDo
    );
    @DELETE("todo/{id}/")
    Call<ToDo> deleteToDo(
            @Header("Authorization") String token,
            @Path("id") int id
    );
}
