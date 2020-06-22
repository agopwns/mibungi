package com.example.mibungi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mibungi.dto.FriendDTO;
import com.example.mibungi.dto.MessageDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFriendListAdapter extends RecyclerView.Adapter<ChatFriendListAdapter.ViewHolder> {

    private List<FriendDTO> mData = null;
    private Context mContext = null;
    static String TAG = "ChatFriendListAdapter";

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chat_friend_userId_tv)
        TextView chat_friend_userId_tv;
        @BindView(R.id.chat_friend_send_btn)
        TextView chat_friend_send_btn;

        ViewHolder(View itemView) {
            super(itemView) ;
            ButterKnife.bind(this,itemView);

            chat_friend_send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 쉐어드 프리퍼런스에서 저장된 방 목록이 있는지 먼저 확인하고


                    // 없다면 서버에서 체크 후 새로운 방을 생성한다 -> 방 생성 됐으면




                    // 방을 처음 생성하는 경우를 위해 sender와 receiver 아이디 값을 각각 넘겨준다
                    String receiverId = chat_friend_userId_tv.getText().toString();
                    String senderId = mData.get(getPosition()).getMem_id();

                    // (통신) 방을 새로 만드는 경우 api 서버에 먼저 저장한다
                    MessageDTO tempDTO = new MessageDTO("checkRoom", senderId, receiverId, "", "");
                    RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                    Call<MessageDTO> call = retrofitExService.checkRoom(tempDTO);
                    call.enqueue(new Callback<MessageDTO>() {
                        @Override
                        public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {
                            Log.d(TAG, "onResponse 안쪽");
                            // 해당 결과 값으로 나온 방 번호와 메세지를 채팅 서버에 알려준다
                            // 만약 방이 있다면 기존 방 채팅 내역
                            if(response.code() == 201){
                                Log.d(TAG, "onResponse code 201 기존 방 있음");
                                String room_id = response.body().getRoom_id();

//                                // 채팅 서버 서비스에 해당 번호와 방이 새로 생성되었다는 것을 알려줘야 함
//                                // 그리고 상세 값들도 json String 으로 넘겨줘야 함
//                                String send_json = setJson("openRoom", senderId, receiverId, "", new_room_id);
//                                ChatApplication.getInstance().getServiceInterface().sendToChatServer(send_json);

                                // 채팅 액티비티로 넘겨준다
                                Intent intent = new Intent(mContext, MessageActivity.class);
                                intent.putExtra("senderId", senderId);
                                intent.putExtra("receiverId", receiverId);
                                intent.putExtra("roomId", room_id);
                                mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageDTO> call, Throwable t) {
                            // Toast.makeText(mContext,"통신 실패 : " + t.toString(), Toast.LENGTH_LONG);
                            Log.d(TAG, "onFailure 통신 실패 : " + t.toString());
                        }
                    });

                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ChatFriendListAdapter(List<FriendDTO> list, Context context) {
        mData = list ;
        mContext = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ChatFriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.layout_friend_list, parent, false) ;
        ChatFriendListAdapter.ViewHolder vh = new ChatFriendListAdapter.ViewHolder(view) ;
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ChatFriendListAdapter.ViewHolder holder, int position) {
        String friend_id = mData.get(position).getFriend_id();
        holder.chat_friend_userId_tv.setText(friend_id);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}