package com.example.mibungi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mibungi.dto.FriendDTO;
import com.example.mibungi.utils.PreferencesUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFriendListActivity extends AppCompatActivity {

    @BindView(R.id.topbar_room_list_button)
    ImageButton topbar_room_list_button;

    @BindView(R.id.topbar_search_list_button)
    ImageButton topbar_search_list_button;

    @BindView(R.id.chat_friend_rv)
    RecyclerView chat_friend_rv;

    PreferencesUtility mPref;
    String tempRoomId;

    static String TAG = "ChatFriendListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_friend_list);
        ButterKnife.bind(this);
        mPref = PreferencesUtility.getInstance(getApplicationContext());

        // 뷰 초기화
        init();


    }

    private void init() {
        topbar_room_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatRoomListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // 친구 목록 가져오기
        getFriendListByServer();


    }

    private void getFriendListByServer() {
        RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
        Call<List<FriendDTO>> call = retrofitExService.getFriends(mPref.getString(mPref.LOGIN_ID));
        call.enqueue(new Callback<List<FriendDTO>>() {
            @Override
            public void onResponse(Call<List<FriendDTO>> call, Response<List<FriendDTO>> response) {

                if(response.body() != null && response.body().size() > 0){
                    Log.d(TAG, "getFriendListByServer() 통신 성공 response.body 존재");
                    ChatFriendListAdapter adapter = new ChatFriendListAdapter(response.body(), getApplicationContext());
                    chat_friend_rv.setAdapter(adapter);
                    chat_friend_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }

            }
            @Override
            public void onFailure(Call<List<FriendDTO>> call, Throwable t) {
                Log.d(TAG, "getFriendListByServer() 통신 실패");
                getFriendListByServer();
            }
        });
    }


}
