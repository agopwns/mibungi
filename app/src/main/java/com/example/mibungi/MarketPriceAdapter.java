package com.example.mibungi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mibungi.dto.MarketPriceDTO;

import java.util.ArrayList;

public class MarketPriceAdapter extends RecyclerView.Adapter<MarketPriceAdapter.ViewHolder> {

    private ArrayList<MarketPriceDTO> mData = null;
    private Context mContext = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMarketName;
        TextView tvMarketTime;
        TextView tvLastPrice;
        TextView tvChangePrice;
        TextView tvChangePercent;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            tvMarketName = itemView.findViewById(R.id.tv_market_name) ;
            tvMarketTime = itemView.findViewById(R.id.tv_market_time) ;
            tvLastPrice = itemView.findViewById(R.id.tv_market_last_price) ;
            tvChangePrice = itemView.findViewById(R.id.tv_market_change_price) ;
            tvChangePercent = itemView.findViewById(R.id.tv_market_change_percent) ;
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    MarketPriceAdapter(ArrayList<MarketPriceDTO> list, Context context) {
        mData = list ;
        mContext = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public MarketPriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.layout_market_list, parent, false) ;
        MarketPriceAdapter.ViewHolder vh = new MarketPriceAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MarketPriceAdapter.ViewHolder holder, int position) {
        String marketName = mData.get(position).getMarketName();
        String marketTime = mData.get(position).getTime();
        String marketLastPrice = mData.get(position).getLastPrice();
        String marketChangePrice = mData.get(position).getChangePrice();
        String marketChangePercent = mData.get(position).getChangePercent();
        holder.tvMarketName.setText(marketName) ;
        holder.tvMarketTime.setText(marketTime) ;

        // 지수 값이 +인 경우는 빨간색 -인 경우는 파란색으로 텍스트 색상 변경
        if(marketChangePrice.substring(0,1).equals("+")){

            holder.tvChangePrice.setTextColor(mContext.getResources().getColor(R.color.colorRedOrange));
            holder.tvChangePercent.setTextColor(mContext.getResources().getColor(R.color.colorRedOrange));

        } else {
            holder.tvChangePrice.setTextColor(mContext.getResources().getColor(R.color.colorPantoneBlue));
            holder.tvChangePercent.setTextColor(mContext.getResources().getColor(R.color.colorPantoneBlue));
        }


        holder.tvLastPrice.setText(marketLastPrice) ;
        holder.tvChangePrice.setText(marketChangePrice) ;
        holder.tvChangePercent.setText(marketChangePercent) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}