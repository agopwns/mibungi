package com.example.mibungi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mibungi.dto.MessageDTO;
import com.example.mibungi.utils.BroadcastActions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mibungi.utils.MyUtil.setJson;

public class MessageActivity extends AppCompatActivity {

    // 채팅방의 타이틀이 들어가는데 주로 상대방의 아이디
    @BindView(R.id.msg_chat_room_title_tv)
    TextView msg_chat_room_title_tv;

    // 채팅 내역이 표시되는 공간
    @BindView(R.id.msg_chat_room_rv)
    RecyclerView msg_chat_room_rv;

    // 메세지 전송 버튼
    @BindView(R.id.msg_chat_room_send_btn)
    Button msg_chat_room_send_btn;

    // 메세지 내용
    @BindView(R.id.msg_chat_room_content)
    EditText msg_chat_room_content;

    String senderId;
    String receiverId;
    // 맨 처음 1:1 채팅을 누르거나 방 목록을 누를 때 해당 값이 할당되어야 한다.
    // 서버 접속은 정말 필요할 때만 한다.
    String roomId;
    ArrayList<MessageDTO> mList = new ArrayList<>();
    MessageAdapter adapter;

    static String TAG = "MessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        registerBroadcast();

        // 1 - 1:1 채팅 버튼 눌러서 넘어오는 경우 intent로 senderId와 receiverId 값이 넘어옴
        // 해당 값으로 api 서버의 방 정보를 조회하여 새로운 방을 만들지. 기존 방으로 연결할지 결정
        if(getIntent().getExtras() != null){
            senderId = getIntent().getExtras().getString("senderId");
            receiverId = getIntent().getExtras().getString("receiverId");
            roomId = getIntent().getExtras().getString("roomId");
            // 상대방 아이디를 채팅방의 타이틀로 지정
            msg_chat_room_title_tv.setText(receiverId);
        }
        // TODO : DB에서 모든 대화 내역 가져오기


        // 리사이클러뷰 세팅
        adapter = new MessageAdapter(mList, getApplicationContext(), senderId);
        msg_chat_room_rv.setAdapter(adapter);
        msg_chat_room_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // 메세지 전송 버튼
        msg_chat_room_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_chat_room_content.getText().toString();
                // TODO : room_id 를 api 서버에서 조회해온 것으로 대체
                String send_json = setJson("chat", senderId, receiverId, msg, roomId);
                ChatApplication.getInstance().getServiceInterface().sendToChatServer(send_json);

                // TODO : 나중에 채팅 서버에서 먼저 sender와 receiver가 같은 방에 있는지 체크하는 로직 추가
                // (통신) 방을 새로 만드는 경우 api 서버에 먼저 저장한다
//                MessageDTO tempDTO = new MessageDTO("createRoom", senderId, receiverId, msg, "");
//                RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
//                Call<MessageDTO> call = retrofitExService.createRoom(tempDTO);
//                call.enqueue(new Callback<MessageDTO>() {
//                    @Override
//                    public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {
//                        Log.d(TAG, "onResponse 안쪽");
//                        // 해당 결과 값으로 나온 방 번호와 메세지를 채팅 서버에 알려준다
//
//                        // 기존 방이 없다면 새로 방을 생성한다
//                        if(response.code() == 200){
//                            Log.d(TAG, "기존 방이 없을 때");
//                            String new_room_id = response.body().getRoom_id();
//
//                            // 채팅 서버 서비스에 해당 번호와 방이 새로 생성되었다는 것을 알려줘야 함
//                            // 그리고 상세 값들도 json String 으로 넘겨줘야 함
//                            String send_json = setJson("createRoom", senderId, receiverId, msg, new_room_id);
//                            ChatApplication.getInstance().getServiceInterface().sendToChatServer(send_json);
//
//                        // 기존 방이 있는 경우
//                        } else if(response.code() == 201){
//                            Log.d(TAG, "기존 방이 있을 때");
//                            String new_room_id = response.body().getRoom_id();
//                            String send_json = setJson("chat", senderId, receiverId, msg, new_room_id);
//                            ChatApplication.getInstance().getServiceInterface().sendToChatServer(send_json);
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<MessageDTO> call, Throwable t) {
//                        // Toast.makeText(getApplicationContext(),"통신 실패 : " + t.toString(), Toast.LENGTH_LONG);
//                        Log.d(TAG, "통신 실패 : " + t.toString());
//                    }
//                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 리스트
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBroadcast();
    }

    // 재생 상태 변경을 받는 브로드캐스트 리시버
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // 채팅 서버로부터 메세지를 받으면 브로드캐스트를 통해 받음
            if (intent.getAction().equals(BroadcastActions.RECEIVED)) {
                MessageDTO tempDTO = ChatApplication.getInstance().getServiceInterface().getReceiveMessageDTO();
                // ArrayList에 추가하고 어댑터를 갱신
                if(tempDTO != null && !tempDTO.getMsg().equals("")){
                    mList.add(tempDTO);
                    adapter.notifyItemInserted(mList.size()-1);
                }

            } else if (intent.getAction().equals(BroadcastActions.SEND)){
                MessageDTO tempDTO = ChatApplication.getInstance().getServiceInterface().getSendMessageDTO();
                // ArrayList에 추가하고 어댑터를 갱신
                if(tempDTO != null && !tempDTO.getMsg().equals("")){
                    mList.add(tempDTO);
                    adapter.notifyItemInserted(mList.size()-1);
                }
            }

        }
    };

    public void registerBroadcast(){
        IntentFilter filterReceived = new IntentFilter();
        filterReceived.addAction(BroadcastActions.RECEIVED);
        IntentFilter filterSend = new IntentFilter();
        filterSend.addAction(BroadcastActions.SEND);

        registerReceiver(mBroadcastReceiver, filterReceived);
        registerReceiver(mBroadcastReceiver, filterSend);
    }

    public void unregisterBroadcast(){
        unregisterReceiver(mBroadcastReceiver);
    }


}
