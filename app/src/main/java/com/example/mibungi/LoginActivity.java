package com.example.mibungi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibungi.dto.MemberDTO;
import com.example.mibungi.utils.PreferencesUtility;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mibungi.utils.AES256Chiper.AES_Encode;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText textId, textPass1;
    ImageButton backButton;
    LinearLayout area_login, area_register;
    String mId, mPass;
    boolean isExistId, isEqualPassword;
    PreferencesUtility mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPreferences = PreferencesUtility.getInstance(getApplicationContext());

        // 만약 로그인 상태라면 아래 구문들을 실행할 필요 없이 바로 채팅 영역으로 넘겨준다
        if(mPreferences.isLoginStatus()){

            Intent moveIntent = new Intent(getApplicationContext(), MainActivity.class);
            moveIntent.putExtra("id", mPreferences.getString(mPreferences.LOGIN_ID));
            startActivity(moveIntent);
            finish();
        }

        textId = findViewById(R.id.login_id);
        // 회원가입에 성공해서 해당 액티비티로 이동했다면 아이디 값 뿌려주기
        if(getIntent().getExtras() != null)
            textId.setText(getIntent().getExtras().getString("id"));

        textPass1 = findViewById(R.id.login_password);

        backButton = findViewById(R.id.login_button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        area_register = findViewById(R.id.login_area_register);
        area_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                moveIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(moveIntent);
            }
        });
        area_login = findViewById(R.id.login_area_login);
        area_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 1. 아이디 체크
                mId = textId.getText().toString();
                mPass = textPass1.getText().toString();

                // 암호화를 진행함
                String aesPass = "";
                try {
                    aesPass = AES_Encode(mPass);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

                // 로그인 API 서버와 통신하기 위한 로직
                RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                MemberDTO member = new MemberDTO(mId, aesPass);
                Call<MemberDTO> loginCall = retrofitExService.login(member);
                loginCall.enqueue(new Callback<MemberDTO>() {
                    @Override
                    public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {

                        // 응답이 있더라도 서버에서 200 코드를 받을 때만 로그인을 하고 나머지는 전부 return 처리함
                        if(response.code() != 200){
//                            Toast.makeText(getApplicationContext(), "통신 실패 발생", Toast.LENGTH_SHORT ).show();
                            textId.setError("아이디와 비밀번호를 다시 확인해주세요.");
                            return;
                        }
                        // 쉐어드로 아이디와 로그인 상태 저장
                        mPreferences.setString(PreferencesUtility.LOGIN_ID, mId);
                        mPreferences.setLoginStatus(true);

                        // 로그인 성공시 메인 액티비티로 id 넘겨주기
                        Intent moveIntent = new Intent(getApplicationContext(), MainActivity.class);
                        moveIntent.putExtra("id", mId);
                        moveIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(moveIntent);
                        finish();
                    }
                    @Override
                    public void onFailure(Call<MemberDTO> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "통신 실패 발생", Toast.LENGTH_SHORT ).show();
                        Log.d("RegisterActivity", "통신 실패 발생 : " + t.toString());
                    }
                });
            }
        });
    }


}
