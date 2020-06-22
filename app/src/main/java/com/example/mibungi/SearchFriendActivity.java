package com.example.mibungi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class SearchFriendActivity extends AppCompatActivity {

    @BindView(R.id.btn_search)
    Button btn_search;
    @BindView(R.id.ed_search_text)
    EditText ed_search_text;
    @BindView(R.id.rv_search_result_list)
    RecyclerView rv_search_result_list;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        ButterKnife.bind(this);
        initBar();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = ed_search_text.getText().toString();

                // RetrofitExService 클래스 내부에서 선언해둔 객체를 가져와서 사용한다(싱글톤)
                // TODO : api 작성 후 조회하는 함수 바꾸기
                RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                Call<List<CompanyInfoDTO>> call = retrofitExService.getData(inputText);
                call.enqueue(new Callback<List<CompanyInfoDTO>>() {
                    @Override
                    public void onResponse(Call<List<CompanyInfoDTO>> call, Response<List<CompanyInfoDTO>> response) {

//                        Toast.makeText(getApplicationContext(),"통신 성공 : " + response.body().toString(), Toast.LENGTH_LONG);
                        // TODO : 어댑터 변경
                        SearchResultAdapter adapter = new SearchResultAdapter(response.body(), getApplicationContext());
                        rv_search_result_list.setAdapter(adapter);
                        rv_search_result_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }

                    @Override
                    public void onFailure(Call<List<CompanyInfoDTO>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"통신 실패 : " + t.toString(), Toast.LENGTH_LONG);
                    }
                });



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
                Intent intent = new Intent(getApplicationContext(), SearchFriendActivity.class);
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
