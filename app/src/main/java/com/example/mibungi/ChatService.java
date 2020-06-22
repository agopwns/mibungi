package com.example.mibungi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.mibungi.dto.MessageDTO;
import com.example.mibungi.utils.BroadcastActions;
import com.example.mibungi.utils.PreferencesUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import static com.example.mibungi.utils.MyUtil.setJson;
import static com.example.mibungi.utils.MyUtil.setToast;

public class ChatService extends Service {

    private final IBinder mBinder = new ChatServiceBinder();
    private String html = "";
    private Handler mHandler;
    private Socket socket;
    private String name;
    private DataInputStream networkReader;
    private DataOutputStream networkWriter;
    private PreferencesUtility mPreferences;
    //    private String ip = "13.124.187.219"; // IP
    private String ip = "192.168.145.1"; // IP
    private int port = 4455; // PORT번호
    private static String TAG = "ChatService";

    private String jsonStringFromAct;
    private String tempRoomId;
    private String sender_id;
    private String receiver_id;

    private MessageDTO receiverMessageDTO = null;
    private MessageDTO sendMessageDTO = null;



    // 서비스 인터페이스를 통해 바깥 액티비티와 통신을 주고 받기 위함
    public class ChatServiceBinder extends Binder {
        ChatService getService() { return ChatService.this; }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mPreferences = PreferencesUtility.getInstance(getApplicationContext());
        // 생성한 스레드의 변동 사항을 받을 수 있게 핸들러를 설정해줌
        mHandler = new Handler();
        // 서비스 안에서 소켓 스레드 동작
        (new Connect()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    // 서비스가 파괴되면 소켓 메모리를 반환
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 친구 목록에 들어갈 때 api 서버와 통신하여 roomId 값이 있는지 체크하여 전달
    public void setTempRoomId(String roomId){
        tempRoomId = roomId;
        // 룸이 갱신 되었으므로 채팅 서버에 알려주기
        String sendJson = setJson("createRoom", "", "", "createRoom!!!", tempRoomId);
        jsonStringFromAct = sendJson;
//        (new SendMessage()).start();
    }

    // 액티비티에서 json String 을 전달 받아 해당 내용을 서버에 전송하는 함수
    public void sendToChatServer(String message) {
        // 전달 받은 json String을 전역 변수 jsonStringFromAct 에 저장
        jsonStringFromAct = message;
        // jsonStringFromAct 는 전역으로 갖고 있으므로 해당 스레드 안에서도 json 을 파싱하여
        // 채팅 서버에 값을 전달 할 수 있음
        (new SendMessage()).start();
    }

    public MessageDTO getSendMessageDTO(){
        return sendMessageDTO;
    }

    public MessageDTO getReceiverMessageDTO(){
        return receiverMessageDTO;
    }

    // 아이피, 포트를 인자로 소켓을 연결한다
    // networkWriter는 서버로 보내는 객체, networkReader는 서버로부터 읽어오는 객체이며
    // 소켓을 연결하면 전역 변수로 할당 되기 때문에 해당 액티비티에서 활용할 수 있다.
    // 다만, 미연결 상태에서 사용하면 에러가 날 수 있으므로 예외처리가 필요하다.
    class Connect extends Thread {
        public void run() {
            Log.d(TAG, "Run Connect");
            try {
                // --------------------------------- 최초 연결 영역 ----------------------------------
                // 해당 시점에서는 접속만 하고 아무것도 아직 일어나지 않은 상태
                socket = new Socket(ip, port);
                OutputStream out;

                // flag: connect, sender_id: LOGIN_ID, receiver_id: '', msg: '', room_id: ''
                networkWriter = new DataOutputStream(socket.getOutputStream());
                // 최초로 연결에 성공했을 때 서버에서 유저를 식별할 수 있는 아이디를 넘겨준다
                String temp_sender_id = mPreferences.getString(mPreferences.LOGIN_ID);
                sender_id = temp_sender_id; // 전역 변수에 할당

                // 값이 다 준비 되었으므로 채팅 서버와 통신
                String sendJosn = setJson("connect", temp_sender_id, "", "", "");
                byte[] b = sendJosn.getBytes("UTF-8");
                // 변환한 바이트를 송신
                networkWriter.write(b);
                networkWriter.flush();

                // --------------------------------- 메세지 대기 영역 --------------------------------
                // 서버로부터 메세지를 받을 때도 바이트로 받기 때문에 DataInputStream 객체 생성
                InputStream in;
                networkReader = new DataInputStream(socket.getInputStream());
                String line;
                Log.w(TAG, "Start Thread");
                while (true) {
                    Log.w(TAG, "chatting is running");
                    // networkReader 서버에서 전달 받은 내용을 읽을 수 있게 도와줌
                    // 전역 변수이기 때문에 미연결 상황을 고려해 null 예외처리
                    if(networkReader != null){
                        // 서버쪽에서 byte로 보낸 데이터를 String으로 변환하는 과정
                        byte[] byteArr = new byte[1024];
                        InputStream is = socket.getInputStream();
                        int readByteCount = is.read(byteArr);
                        String data = new String(byteArr, 0, readByteCount, "UTF-8");

                        if(data == null) continue;
                        Log.d(TAG, "json 파싱 전");
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(data);
                            // flag: connect, sender_id: LOGIN_ID, receiver_id: '', msg: '', room_id: ''
                            String flag = jsonObject.get("flag").toString();
                            String sender_id = jsonObject.get("sender_id").toString();
                            String receiver_id = jsonObject.get("receiver_id").toString();
                            String msg = jsonObject.get("msg").toString();
                            String room_id = jsonObject.get("room_id").toString();
                            receiverMessageDTO = new MessageDTO(flag, sender_id, receiver_id, msg, room_id);

                            // DTO에 넣은 다음 브로드 캐스트 리시버를 통해 받은 메세지값이 있다고 액티비티나 리사이클러뷰 어댑터에 전송
                            sendBroadcast(new Intent(BroadcastActions.RECEIVED));

                        } catch (JSONException e) {
                            Log.d(TAG, "json 파싱 에러 : " + e);
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "chatting is break");
                        break;
                    }
                }
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
                System.out.println(e);
                e.printStackTrace();
            }
        }

    }

    class SendMessage extends Thread {

        public void run(){

            byte[] b = new byte[0];
            try {
                b = jsonStringFromAct.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                networkWriter.write(b);
                networkWriter.flush();
                try {
                    JSONObject jsonObject = new JSONObject(jsonStringFromAct);
                    // flag: connect, sender_id: LOGIN_ID, receiver_id: '', msg: '', room_id: ''
//                    String flag = jsonObject.get("flag").toString();
                    String flag = "chat";
                    String sender_id = jsonObject.get("sender_id").toString();
                    String receiver_id = jsonObject.get("receiver_id").toString();
                    String msg = jsonObject.get("msg").toString();
                    String room_id = jsonObject.get("room_id").toString();
                    sendMessageDTO = new MessageDTO(flag, sender_id, receiver_id, msg, room_id);

                } catch (JSONException e) {
                    Log.d(TAG, "json 파싱 에러 : " + e);
                    e.printStackTrace();
                }

                sendBroadcast(new Intent(BroadcastActions.SEND));
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "SendMessage 오류 : "+ e);
                        setToast(getApplicationContext(),"서버에 보내기 실패");
                    }
                });
            }
        }
    }




}
