package com.example.mibungi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mibungi.dto.MarketPriceDTO;
import com.example.mibungi.dto.SearchRoomListDTO;
import com.example.mibungi.utils.PreferencesUtility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_market_list)
    RecyclerView rv;
    @BindView(R.id.main_refresh_layout)
    SwipeRefreshLayout main_refresh_layout;

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
    @BindView(R.id.topbar_chat_button)
    ImageButton topbar_chat_button;

    Handler handler = new Handler();

    // 크롤링 한 시장 지수 관련 정보를 담아 리사이클러뷰 어댑터로 넘겨주기 위한 객체
    ArrayList<MarketPriceDTO> marketInfoList = new ArrayList<>();
    Activity mTarget;
    PreferencesUtility mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mPreferences = PreferencesUtility.getInstance(getApplicationContext());

        mTarget = this;
        // Jsoup 라이브러리를 사용해 주가 지수를 크롤링하여 marketInfoList에 담기 위한 함수
        getMarketScore();
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
        main_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ButterKnife.bind(mTarget);

                        marketInfoList = getFinanceHtml();

                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);

                        // 오레오 이상 부터 안드로이드 정책상 스레드 사용시
                        // runOnUiThread 를 써서 UI 스레드 컨트롤 해야 함
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MarketPriceAdapter adapter = new MarketPriceAdapter(marketInfoList, getApplicationContext());
                                rv.setAdapter(adapter);
                                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                main_refresh_layout.setRefreshing(false);
                            }
                        });
                    }
                });
                th.start();
            }
        });
        topbar_chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(getApplicationContext(), ChatFriendListActivity.class);
                moveIntent.putExtra("id", mPreferences.getString(mPreferences.LOGIN_ID));
                moveIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(moveIntent);
                finish();
            }
        });
    }

    private void getMarketScore(){

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                // 메인 스레드에서 돌리면 안드로이드 정책상 network 에러가 나기 때문에
                // 새로운 스레드를 생성하여 Runnable 객체를 통해 코드 객체를 전달하여 실행하게 함
                marketInfoList = getFinanceHtml();

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);

                // 오레오 이상 부터 안드로이드 정책상 스레드 사용시
                // runOnUiThread 를 써서 UI 스레드 컨트롤 해야 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.activity_main);
                        ButterKnife.bind(mTarget);

                        initBar();

                        MarketPriceAdapter adapter = new MarketPriceAdapter(marketInfoList, getApplicationContext());
                        rv.setAdapter(adapter);
                        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        // WARN : 해당 부분에서 이미 유저 소켓 커넥션이 연결 되어있음
                        // 연결된 소켓에 api 서버와 통신하여 원래 유저가 들어가있던 방을 알려줌
                        // 연결은 최초이나 room_id 를 이미 여러개 가진 유저일 수 있으므로 api 서버와 통신 후 값을 가져옴
                        getRoomList();
                    }
                });
            }
        });
        th.start();
    }

    private void getRoomList() {
        RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
        Call<List<SearchRoomListDTO>> call2 = retrofitExService.getRoomList(mPreferences.getString(mPreferences.LOGIN_ID));
        call2.enqueue(new Callback<List<SearchRoomListDTO>>() {
            @Override
            public void onResponse(Call<List<SearchRoomListDTO>> call, Response<List<SearchRoomListDTO>> response) {

                if(response.code() != 200) return;
                String tempRoomId = "";
                // 만약 이미 속해 있는 room_id가 있다면
                for(int i = 0; i < response.body().size(); i++){
                    // 마지막일 때는 , 없음
                    if(response.body().get(i).toString().isEmpty()) continue;

                    if(i == response.body().size()-1)
                        tempRoomId += response.body().get(i).getRoom_id();
                    else
                        tempRoomId += response.body().get(i).getRoom_id() + ",";
                }
                ChatApplication.getInstance().getServiceInterface().setTempRoomId(tempRoomId);
            }
            @Override
            public void onFailure(Call<List<SearchRoomListDTO>> call, Throwable t) {
                getRoomList();
            }
        });
    }

    private ArrayList<MarketPriceDTO> getFinanceHtml() {

        ArrayList<MarketPriceDTO> tempList = new ArrayList<>();

        try {
            String tempText = "";

            // 페이지의 element 요소를 받아오기 위해 url 주소로 페이지 요청
            Document doc = Jsoup.connect("https://kr.investing.com/indices/major-indices").get();

            // select한 결과를 담는 객체는 Element 와 Elements 두 가지가 있는데
            // select 할 내용은 tbody 안의 복수의 html 요소이므로 Elements 를 사용한다.
            Elements contents;

            // Jsoup는 css 쿼리로 select가 가능하기 때문에 지수 관련 정보가 있는
            // .genTbl.closedTbl의 tbody 안의 값들을 contents 라는 이름의 객체에 넣어놓고 리스트 형태로
            // 사용 할 수 있게 만든다
            String elementIdText = "table.genTbl.closedTbl tbody";
            contents = doc.select(elementIdText);

            // pid는 지수 id로 해당 id를 변경하며 td 내용을 찾는다
            for(int i = 0; i < 15; i++){

                String pidNum = "169";
                String marketName = "다우존스";

                //region if else Company Code
                if(i == 1){
                    pidNum = "166";
                    marketName = "S&P500";
                } else if(i == 2){
                    pidNum = "14958";
                    marketName = "나스닥 종합";
                } else if(i == 3){
                    pidNum = "170";
                    marketName = "Russell 2000";
                } else if(i == 4){
                    pidNum = "44336";
                    marketName = "CBOE VIX";
                } else if(i == 5){
                    pidNum = "24441";
                    marketName = "캐나다 S&P/TSX";
                } else if(i == 6){
                    pidNum = "17920";
                    marketName = "브라질 보베스파";
                } else if(i == 7){
                    pidNum = "172";
                    marketName = "독일 DAX";
                } else if(i == 8){
                    pidNum = "27";
                    marketName = "영국 FTSE";
                } else if(i == 9){
                    pidNum = "178";
                    marketName = "일본 닛케이";
                } else if(i == 10){
                    pidNum = "40820";
                    marketName = "상하이 종합";
                } else if(i == 11){
                    pidNum = "167";
                    marketName = "프랑스 CAC";
                } else if(i == 12){
                    pidNum = "175";
                    marketName = "EU Stoxx 50";
                } else if(i == 13){
                    pidNum = "168";
                    marketName = "네덜란드 AEX";
                } else if(i == 14){
                    pidNum = "174";
                    marketName = "스페인 IBEX";
                }
                //endregion

                // contents.get(0) 은 element 요소를 반환하므로 select 함수를 사용하여 해당 객체 안의 요소를
                // select 하는 것이 가능하다
                String lastPrice = contents.get(0).select("tr td.pid-" + pidNum + "-last").text();
                String changePrice = contents.get(0).select("tr td.pid-" + pidNum + "-pc").text();
                String changePercent = contents.get(0).select("tr td.pid-" + pidNum + "-pcp").text();
                String time = contents.get(0).select("tr td.pid-" + pidNum + "-time").text();
                // 기존 time은 외국식 표기 dd/MM 형태로 되어있으므로 MM/dd 형태로 바꿔준다
                // 만약 dd/MM 형태가 아닌 hh:mm:ss 일 경우는 그대로 표기해준다
                if(time.length() == 5)
                    time = time.substring(3,5) + "/" + time.substring(0,2);

                MarketPriceDTO tempDTO = new MarketPriceDTO(marketName, lastPrice, changePrice, changePercent, time);
                tempList.add(tempDTO);
            }

        } catch (IOException e) {
            //e.printStackTrace();
            Log.d("getMarketScore()함수 에러 : ",e.getMessage());
        }

        return tempList;
    }


}
