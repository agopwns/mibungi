package com.example.mibungi;

import com.example.mibungi.dto.CompanyInfoDTO;
import com.example.mibungi.dto.EstimationDTO;
import com.example.mibungi.dto.FriendDTO;
import com.example.mibungi.dto.MemberDTO;
import com.example.mibungi.dto.MessageDTO;
import com.example.mibungi.dto.SearchRoomListDTO;
import com.example.mibungi.dto.SpecificInfoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitExService {

    @GET("/search")
    Call<List<CompanyInfoDTO>> getData(@Query("search_word") String searchWord);

    @GET("/company")
    Call<SpecificInfoDTO> getSpecificData(@Query("ticker") String ticker);

    @GET("/bookmark")
    Call<CompanyInfoDTO> setBookMark(@Query("ticker") String ticker);

    @GET("/bookmarks")
    Call<List<CompanyInfoDTO>> getBookMarkList();

    @GET("/low")
    Call<List<CompanyInfoDTO>> getLowValueCompany();

    @GET("/estimate")
    Call<EstimationDTO> getEstimate();

    @GET("/id")
    Call<MemberDTO> getId(@Query("id") String id);

    @POST("/id")
    Call<MemberDTO> register(@Body MemberDTO Body);

    @POST("/login")
    Call<MemberDTO> login(@Body MemberDTO Body);

    @GET("/friends")
    Call<List<FriendDTO>> getFriends(@Query("id") String id);

    @POST("/room")
    Call<MessageDTO> checkRoom(@Body MessageDTO Body);

    @GET("/roomList")
    Call<List<SearchRoomListDTO>> getRoomList(@Query("id") String id);

    // 통신 객체 생성
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://13.124.187.219:5000")
            .addConverterFactory(ScalarsConverterFactory.create()) // 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
