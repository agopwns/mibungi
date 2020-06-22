package com.example.mibungi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mibungi.dto.EstimationDTO;
import com.example.mibungi.dto.NewsDTO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.news_rv_search_result_list)
    RecyclerView rv_news;
    @BindView(R.id.news_swipe_layout)
    SwipeRefreshLayout news_refresh_layout;

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

    @BindView(R.id.news_estimation)
    TextView news_estimation;

    Handler handler = new Handler();

    // 크롤링 한 시장 지수 관련 정보를 담아 리사이클러뷰 어댑터로 넘겨주기 위한 객체
    ArrayList<NewsDTO> newsList = new ArrayList<>();
    Activity mTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mTarget = this;
        // Jsoup 라이브러리를 사용해 주가 지수를 크롤링하여 marketInfoList에 담기 위한 함수
        getNews();
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
        news_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ButterKnife.bind(mTarget);
                        newsList = getNewsByHtml();

                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);

                        // 오레오 이상 부터 안드로이드 정책상 스레드 사용시
                        // runOnUiThread 를 써서 UI 스레드 컨트롤 해야 함
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentView(R.layout.activity_news);
                                ButterKnife.bind(mTarget);

                                initBar();

                                NewsAdapter adapter = new NewsAdapter(newsList, getApplicationContext());
                                rv_news.setAdapter(adapter);
                                rv_news.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                                // RetrofitExService 클래스 내부에서 선언해둔 객체를 가져와서 사용한다(싱글톤)
                                RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                                Call<EstimationDTO> call = retrofitExService.getEstimate();
                                call.enqueue(new Callback<EstimationDTO>() {
                                    @Override
                                    public void onResponse(Call<EstimationDTO> call, Response<EstimationDTO> response) {
                                        setEstimate(response);
                                    }

                                    @Override
                                    public void onFailure(Call<EstimationDTO> call, Throwable t) { }
                                });
                            }
                        });
                    }
                });
                th.start();
            }
        });

    }

    private void getNews(){

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                // 메인 스레드에서 돌리면 안드로이드 정책상 network 에러가 나기 때문에
                // 새로운 스레드를 생성하여 Runnable 객체를 통해 코드 객체를 전달하여 실행하게 함
                newsList = getNewsByHtml();

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

                // 오레오 이상 부터 안드로이드 정책상 스레드 사용시
                // runOnUiThread 를 써서 UI 스레드 컨트롤 해야 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.activity_news);
                        ButterKnife.bind(mTarget);
                        initBar();

                        NewsAdapter adapter = new NewsAdapter(newsList, getApplicationContext());
                        rv_news.setAdapter(adapter);
                        rv_news.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        // RetrofitExService 클래스 내부에서 선언해둔 객체를 가져와서 사용한다(싱글톤)
                        // 감정 분석 결과를 받아오기 위해 http 통신
                        RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                        Call<EstimationDTO> call = retrofitExService.getEstimate();
                        call.enqueue(new Callback<EstimationDTO>() {
                            @Override
                            public void onResponse(Call<EstimationDTO> call, Response<EstimationDTO> response) {
                                setEstimate(response);
                            }
                            @Override
                            public void onFailure(Call<EstimationDTO> call, Throwable t) { }
                        });
                    }
                });
            }
        });
        th.start();
    }

    private void setEstimate(Response<EstimationDTO> response) {
        String positive = response.body().getPositive();
        String negative = response.body().getNegative();
        int pos = Integer.parseInt(positive);
        int neg = Integer.parseInt(negative);
        int total = pos + neg;
        double posPer = Math.round(((double)pos / (double)total) * 100)/1;
        double negPer = Math.round(((double)neg / (double)total) * 100)/1;

        String tempText = "";
        if(pos > neg){
            tempText = "긍정이 " + posPer + "% 부정이 " + negPer + "%로 현재 긍정적인 기사가 많습니다.";
            news_estimation.setText(tempText);
        } else {
            tempText = "긍정이 " + posPer + "% 부정이 " + negPer + "%로 현재 부정적인 기사가 많습니다.";
            news_estimation.setText(tempText);
        }
    }

    private ArrayList<NewsDTO> getNewsByHtml() {

        ArrayList<NewsDTO> tempList = new ArrayList<>();

        try {
            String tempText = "";

            // 페이지의 element 요소를 받아오기 위해 url 주소로 페이지 요청
            Document doc = Jsoup.connect("https://kr.investing.com/news/stock-market-news/").get();

            // select한 결과를 담는 객체는 Element 와 Elements 두 가지가 있는데
            // select 할 내용은 tbody 안의 복수의 html 요소이므로 Elements 를 사용한다.
            Elements contents;

            // Jsoup는 css 쿼리로 select가 가능하기 때문에 지수 관련 정보가 있는
            // .genTbl.closedTbl의 tbody 안의 값들을 contents 라는 이름의 객체에 넣어놓고 리스트 형태로
            // 사용 할 수 있게 만든다
            String elementIdText = "section#leftColumn div.largeTitle article";
            contents = doc.select(elementIdText);

            // pid는 지수 id로 해당 id를 변경하며 td 내용을 찾는다
            for(int i = 0; i < contents.size(); i++){

                // contents.get(0) 은 element 요소를 반환하므로 select 함수를 사용하여 해당 객체 안의 요소를
                // select 하는 것이 가능하다
                String title = contents.get(i).select("div.textDiv a").text();
                String time = contents.get(i).select("div.textDiv span.articleDetails span.date").text();
                String content = contents.get(i).select("div.textDiv p").text();
                String imagePath = contents.get(i).select("a.img img").attr("data-src");
//                String imagePath = contents.get(i).select("a").next().attr("src");
                String url = contents.get(i).select("div.textDiv a").attr("abs:href");

                NewsDTO tempDTO = new NewsDTO(title, url, imagePath, content, time);
                tempList.add(tempDTO);
                System.out.println(title);
            }

        } catch (IOException e) {
            //e.printStackTrace();
            Log.d("getMarketScore()함수 에러 : ",e.getMessage());
        }

        return tempList;
    }
}
