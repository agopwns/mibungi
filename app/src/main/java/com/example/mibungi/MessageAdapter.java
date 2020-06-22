package com.example.mibungi;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mibungi.dto.MessageDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageDTO> mData = null;
    private Context mContext = null;
    private String mId = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chat_layout_userId_tv)
        TextView chat_layout_userId_tv;
        @BindView(R.id.chat_layout_content_tv)
        TextView chat_layout_content_tv;
        @BindView(R.id.chat_layout_linear)
        LinearLayout chat_layout_linear;

        ViewHolder(View itemView) {
            super(itemView) ;
            ButterKnife.bind(this,itemView);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    MessageAdapter(ArrayList<MessageDTO> list, Context context, String id) {
        mData = list;
        mContext = context;
        mId = id;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.layout_chat_other, parent, false) ;
        MessageAdapter.ViewHolder vh = new MessageAdapter.ViewHolder(view) ;
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {
        String sender_id = mData.get(position).getSender_id();
        String receiver_id = mData.get(position).getReceiver_id();
        String msg = mData.get(position).getMsg();
        holder.chat_layout_userId_tv.setText(receiver_id);
        holder.chat_layout_content_tv.setText(msg);

        // 만약 자기 아이디라면 오른쪽 정렬 및 노란색 표시
        if(mId.equals(sender_id)){
            holder.chat_layout_content_tv.setBackgroundColor(Color.parseColor("#F0DC4B"));
            holder.chat_layout_linear.setGravity(Gravity.RIGHT);
            holder.chat_layout_content_tv.setTextColor(Color.BLACK);
        }

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}