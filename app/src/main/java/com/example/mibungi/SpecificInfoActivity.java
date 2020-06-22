package com.example.mibungi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibungi.dto.CompanyInfoDTO;
import com.example.mibungi.dto.SpecificInfoDTO;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecificInfoActivity extends AppCompatActivity {


    //region InitBind
    @BindView(R.id.specific_book_sh)
    TextView specific_book_sh;

    @BindView(R.id.specific_cash_sh)
    TextView specific_cash_sh;

    @BindView(R.id.specific_company_name)
    TextView specific_company_name;

    @BindView(R.id.specific_dividend)
    TextView specific_dividend;

    @BindView(R.id.specific_dividend_per)
    TextView specific_dividend_per;

    @BindView(R.id.specific_income)
    TextView specific_income;

    @BindView(R.id.specific_market_cap)
    TextView specific_market_cap;

    @BindView(R.id.specific_pb)
    TextView specific_pb;

    @BindView(R.id.specific_pc)
    TextView specific_pc;

    @BindView(R.id.specific_pe)
    TextView specific_pe;

    @BindView(R.id.specific_peg)
    TextView specific_peg;

    @BindView(R.id.specific_ps)
    TextView specific_ps;

    @BindView(R.id.specific_price)
    TextView specific_price;

    @BindView(R.id.specific_target_price)
    TextView specific_target_price;

    @BindView(R.id.specific_roa)
    TextView specific_roa;

    @BindView(R.id.specific_roe)
    TextView specific_roe;

    @BindView(R.id.specific_roi)
    TextView specific_roi;

    @BindView(R.id.specific_sales)
    TextView specific_sales;

    @BindView(R.id.specific_ticker)
    TextView specific_ticker;

    @BindView(R.id.specific_book_mark)
    ImageButton specific_book_mark;

    //endregion

    Intent intent;
    String ticker, company_name, book_mark;
    Activity mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mTarget = this;

        intent = getIntent();
        if(intent != null){
            ticker = intent.getStringExtra("ticker");
            ticker = ticker.replaceAll(System.getProperty("line.separator"), "");
            company_name = intent.getStringExtra("company_name");
            book_mark = intent.getStringExtra("book_mark");
        }

        // RetrofitExService 클래스 내부에서 선언해둔 객체를 가져와서 사용한다(싱글톤)
        RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
        Call<SpecificInfoDTO> call = retrofitExService.getSpecificData(ticker);
        call.enqueue(new Callback<SpecificInfoDTO>() {
            @Override
            public void onResponse(Call<SpecificInfoDTO> call, Response<SpecificInfoDTO> response) {

                setContentView(R.layout.activity_specific_info);
                ButterKnife.bind(mTarget);

                SpecificInfoDTO item = (SpecificInfoDTO)response.body();
                specific_company_name.setText(company_name);
                specific_ticker.setText(ticker);
                if(book_mark.equals("true"))
                    specific_book_mark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                else
                    specific_book_mark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);

                specific_book_sh.setText(item.getBookSh());
                specific_cash_sh.setText(item.getCashSh());
                specific_dividend.setText(item.getDividend());
                specific_dividend_per.setText(item.getDividendPer());
                specific_income.setText(item.getIncome());
                specific_market_cap.setText(item.getMarket_Cap());
                specific_pb.setText(item.getPB());
                specific_pc.setText(item.getPC());
                specific_pe.setText(item.getPE());
                specific_peg.setText(item.getPEG());
                specific_ps.setText(item.getPS());
                specific_price.setText("$ " + item.getPrice());
                specific_roa.setText(item.getROA());
                specific_roe.setText(item.getROE());
                specific_roi.setText(item.getROI());
                specific_sales.setText(item.getSales());
                specific_target_price.setText(item.getTarget_Price());

                specific_book_mark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(book_mark.equals("true")){
                            specific_book_mark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                            book_mark = "false";
                        } else {
                            specific_book_mark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                            book_mark = "true";
                        }

                        // RetrofitExService 클래스 내부에서 선언해둔 객체를 가져와서 사용한다(싱글톤)
                        RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                        Call<CompanyInfoDTO> call = retrofitExService.setBookMark(ticker);
                        call.enqueue(new Callback<CompanyInfoDTO>() {
                            @Override
                            public void onResponse(Call<CompanyInfoDTO> call, Response<CompanyInfoDTO> response) { }
                            @Override
                            public void onFailure(Call<CompanyInfoDTO> call, Throwable t) {}
                        });
                    }
                });
            }

            @Override
            public void onFailure(Call<SpecificInfoDTO> call, Throwable t) {
                System.out.println(t.fillInStackTrace());
            }
        });


    }
}
