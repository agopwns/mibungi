package com.example.mibungi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mibungi.dto.NewsDTO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private ArrayList<NewsDTO> mData = null;
    private Context mContext = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.l_news_title)
        TextView l_news_title;
        @BindView(R.id.l_news_date)
        TextView l_news_date;
        @BindView(R.id.l_news_content)
        TextView l_news_content;
        @BindView(R.id.l_news_image)
        ImageView l_news_image;

        ViewHolder(View itemView) {
            super(itemView) ;
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 검색 요청에 쓰일 ticker와 상세 화면에 사용될 회사 정보들을 전달하기 위해
                    // 클릭한 아이템의 position 위치를 알아내 해당 아이템의 정보들을 가져온다
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {

                        // 메인 액티비티가 아닌 곳에서 액티비티를 시작하면 오류가 나기 때문에
                        // intent 에 FLAG_ACTIVITY_NEW_TASK 를 설정해줘야 한다
                        String url = mData.get(pos).getUrl() ;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    NewsAdapter(ArrayList<NewsDTO> list, Context context) {
        mData = list ;
        mContext = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.layout_news_list, parent, false) ;
        NewsAdapter.ViewHolder vh = new NewsAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        String title = mData.get(position).getTitle();
        String time = mData.get(position).getTime();
        String content = mData.get(position).getContent();
        String imagePath = mData.get(position).getImage();
        holder.l_news_title.setText(title) ;
        holder.l_news_date.setText(time) ;
        holder.l_news_content.setText(content) ;
        holder.l_news_content.setText(content) ;

        Glide
                .with(holder.itemView.getContext())
                .load(imagePath)
                .into(holder.l_news_image);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}