package com.example.mibungi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mibungi.dto.CompanyInfoDTO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivity extends AppCompatActivity {

    @BindView(R.id.button_under_chart)
    ImageButton button_under_chart;
    @BindView(R.id.button_under_news)
    ImageButton button_under_news;
    @BindView(R.id.button_under_search)
    ImageButton button_under_search;
    @BindView(R.id.button_under_star)
    ImageButton button_under_star;
    @BindView(R.id.button_user_profile)
    ImageButton button_user_profile;
    @BindView(R.id.recommend_rv_search_result)
    RecyclerView recommend_rv_search_result;
//    @BindView(R.id.recommend_textview)
//    TextView recommend_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        ButterKnife.bind(this);
        initBar();

        // RetrofitExService 클래스 내부에서 선언해둔 객체를 가져와서 사용한다(싱글톤)
        RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
        Call<List<CompanyInfoDTO>> call = retrofitExService.getLowValueCompany();
        call.enqueue(new Callback<List<CompanyInfoDTO>>() {
            @Override
            public void onResponse(Call<List<CompanyInfoDTO>> call, Response<List<CompanyInfoDTO>> response) {

                SearchResultAdapter adapter = new SearchResultAdapter(response.body(), getApplicationContext());
                recommend_rv_search_result.setAdapter(adapter);
                recommend_rv_search_result.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<CompanyInfoDTO>> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),"통신 실패 : " + t.toString(), Toast.LENGTH_LONG);
            }
        });
    }

    private void initBar(){
        button_under_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        button_under_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        button_under_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        button_under_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookMarkActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        button_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecommendActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
