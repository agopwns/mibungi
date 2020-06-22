package com.example.mibungi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomListActivity extends AppCompatActivity {

    @BindView(R.id.topbar_friend_list_button)
    ImageButton topbar_friend_list_button;

    @BindView(R.id.topbar_search_list_button)
    ImageButton topbar_search_list_button;

    @BindView(R.id.chat_room_rv)
    ImageButton chat_room_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);
        ButterKnife.bind(this);
        // 뷰 요소 초기화
        init();
    }

    private void init() {
        topbar_friend_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatFriendListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
