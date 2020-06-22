package com.example.mibungi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mibungi.dto.CompanyInfoDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<CompanyInfoDTO> mData = null;
    private Context mContext = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder 에서도 버터나이프를 사용하면 바인딩이 편리하다
        @BindView(R.id.l_search_ticker_code)
        TextView l_search_ticker_code;
        @BindView(R.id.l_search_company_name)
        TextView l_search_company_name;
        @BindView(R.id.l_search_company_code)
        TextView l_search_company_code;
        @BindView(R.id.l_search_industry)
        TextView l_search_industry;
        @BindView(R.id.l_search_sector)
        TextView l_search_sector;

        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰를 바인딩 하는 작업을 해줘야 한다
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
                        CompanyInfoDTO item = mData.get(pos) ;
                        Intent intent = new Intent(mContext, SpecificInfoActivity.class);
                        intent.putExtra("ticker", item.getTicker());
                        intent.putExtra("company_name", item.getCompany_name());
                        intent.putExtra("book_mark", item.getBook_mark());
                        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    SearchResultAdapter(List<CompanyInfoDTO> list, Context context) {
        mData = list ;
        mContext = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.layout_company_list, parent, false) ;
        SearchResultAdapter.ViewHolder vh = new SearchResultAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder holder, int position) {
        String ticker = mData.get(position).getTicker();
        String company_name = mData.get(position).getCompany_name();
        String sector = mData.get(position).getSector();
        String cik = mData.get(position).getCik();
        String industry = mData.get(position).getIndustry();
        holder.l_search_ticker_code.setText(ticker) ;
        holder.l_search_company_name.setText(company_name) ;
        holder.l_search_sector.setText(sector) ;
        holder.l_search_company_code.setText(cik) ;
        holder.l_search_industry.setText(industry) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}