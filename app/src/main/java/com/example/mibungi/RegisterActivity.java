package com.example.mibungi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mibungi.dto.MemberDTO;
import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mibungi.utils.AES256Chiper.AES_Encode;

// AES256 암호화를 위한 클래스 import

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_button_back)
    ImageButton register_button_back;

    @BindView(R.id.register_button_confirm)
    ImageButton register_button_confirm;

    @BindView(R.id.register_edit_id)
    TextInputEditText register_edit_id;

    @BindView(R.id.register_edit_password1)
    TextInputEditText register_edit_password1;

    @BindView(R.id.register_edit_password2)
    TextInputEditText register_edit_password2;

    // 아이디, 비밀번호를 다른 스레드에서 사용하기 위해 전역 변수 선언
    String mId, mPass1, mPass2;
    // 아이디 존재 여부를 체크하기 위한 변수와 비밀번호 검사를 위한 변수 선언
    boolean isNotExistId, isEqualPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        register_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 유저로부터 입력 받은 값을 변수에 할당함
                mId = register_edit_id.getText().toString();
                mPass1 = register_edit_password1.getText().toString();
                mPass2 = register_edit_password2.getText().toString();

                // 아이디 빈칸 검사
                if(mId.isEmpty()){
                    register_edit_id.setError("아이디를 다시 확인해주세요.");
                    return;
                }
                // 비밀번호 빈칸 검사
                if(mPass1.isEmpty()){
                    register_edit_password1.setError("비밀번호를 다시 확인해주세요.");
                    return;
                }
                // 비밀번호 확인 빈칸 검사
                if(mPass2.isEmpty()){
                    register_edit_password2.setError("비밀번호를 다시 확인해주세요.");
                    return;
                }

                // retrofit http 통신 준비
                RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                Call<MemberDTO> call = retrofitExService.getId(mId);

                // 함수가 너무 길고 복잡해 함수로 따로 뺌
                // 레트로핏 call 객체를 매개변수로 넘겨주어 아이디 중복체크(통신)
                // 비밀번호 비교, 암호화 후 등록(통신)하는 함수
                register(call);
            }
        });
    }

    private void register(Call<MemberDTO> call) {
        // 오류시 예외 처리를 위해 try catch 구문 안에서 작성
        try {
            // 레트로핏 call 객체를 통해 통신을 스레드로 실행할 수 있다
            call.enqueue(new Callback<MemberDTO>() {
                @Override
                public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {

                    // 아이디가 있는지 사전에 검사하고 없을 경우 isNotExistId true로 변경하여
                    // 다음 if 문을 통과시킨다
                    if(response.body().getId().equals("")){
                        isNotExistId = true;
                    } else {
                        register_edit_id.setError("이미 존재하는 아이디입니다.");
                        isNotExistId = false;
                        return;
                    }

                    // 패스워드 검사
                    if(!mPass1.isEmpty() && !mPass2.isEmpty() && mPass1.equals(mPass2)){
                        isEqualPassword = true;
                    } else {
                        isEqualPassword = false;
                        register_edit_password1.setError("비밀번호를 다시 확인해주세요.");
                        return;
                    }

                    // 모두 성공일 때만 회원 정보 전송
                    if(!isNotExistId || !isEqualPassword) return;


                    // 암호화를 진행함
                    String aesPass = "";
                    try {
                        aesPass = AES_Encode(mPass1);
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

                    // 만약 암호화에 실패하면 register http 요청을 하면 안되기 때문에 return
                    if(aesPass.equals("")) return;

                    // 비밀번호가 이상하게 암호화 되지 않았는지 검사하기 위함
                    // 평상시는 주석 처리 해둠
//                    String test = "";
//                    try {
//                        test = AES_Decode(aesPass);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchPaddingException e) {
//                        e.printStackTrace();
//                    } catch (InvalidKeyException e) {
//                        e.printStackTrace();
//                    } catch (InvalidAlgorithmParameterException e) {
//                        e.printStackTrace();
//                    } catch (IllegalBlockSizeException e) {
//                        e.printStackTrace();
//                    } catch (BadPaddingException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(test);

                    // id 와 암호화된 aesPass를 매개변수로 register http 요청

                    RetrofitExService retrofitExService = RetrofitExService.retrofit.create(RetrofitExService.class);
                    MemberDTO member = new MemberDTO(mId, aesPass);
                    // post로 요청시 retrofit이 내부에서 json 객체로 치환하기 때문에
                    // 서버에서 request.body 를 json으로 변환해서 사용하고 싶다면
                    // Call<T> 안의 T가 json으로 변환될 수 있는 객체가 들어가야 함.
                    // 대신 request 와 response 가 항상 같은 객체여야 하는 불편함이 있음.
                    Call<MemberDTO> registerCall = retrofitExService.register(member);

                    registerCall.enqueue(new Callback<MemberDTO>() {
                        @Override
                        public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                            final Object message = response.body();
                            Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT ).show();
                            // 통신 성공시 로그인 페이지로 id 넘겨주기
                            Intent moveIntent = new Intent(getApplicationContext(), LoginActivity.class);
                            moveIntent.putExtra("id", mId);
                            moveIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(moveIntent);
                        }
                        @Override
                        public void onFailure(Call<MemberDTO> call, Throwable t) { t.printStackTrace(); }
                    });
                }

                @Override
                public void onFailure(Call<MemberDTO> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "통신 실패 발생", Toast.LENGTH_SHORT ).show();
                    Log.d("RegisterActivity", "통신 실패 발생 : " + t.toString());
                }
            });
        } catch (Exception e) {
//                    Log.d(TAG, "통신 에러 발생 : " + e);
        }
    }
}
