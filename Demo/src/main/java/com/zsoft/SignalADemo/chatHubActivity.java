package com.zsoft.SignalADemo;

import android.app.Activity;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zsoft.signala.hubs.HubConnection;
import com.zsoft.signala.hubs.HubInvokeCallback;
import com.zsoft.signala.hubs.HubOnDataCallback;
import com.zsoft.signala.hubs.IHubProxy;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import org.json.JSONArray;

/**
 * Created by King on 2016/8/3.
 */
public class chatHubActivity extends Activity {

    private final static String HUB_URL1 = "http://192.168.1.110:8022/signalr/hubs";
    private final static String TAG = "KING";
    private final static String HUB_URL = "http://192.168.1.75:8080/signalr/hubs";
    private TextView chatText;

    // private final static String HUB_URL = "http://192.168.1.75:8080/api/SingalerApi/AskData";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_hub);
        chatText = findViewById(R.id.chat_text);
        beginConnect();
    }

    /**
     * hub链接
     */
    private HubConnection conn = new HubConnection(HUB_URL, this, new LongPollingTransport()) {
        @Override
        public void OnError(Exception exception) {
            Log.d(TAG, "OnError=" + exception.getMessage());
        }

        @Override
        public void OnMessage(String message) {
            Log.d(TAG, "message=" + message);
        }

        @Override
        public void OnStateChanged(StateBase oldState, StateBase newState) {
            Log.d(TAG, "OnStateChanged=" + oldState.getState() + " -> " + newState.getState());
        }
    };

    /*
     * hub代理 panderman 2013-10-25
     */
    private IHubProxy hub = null;

    /**
     * 开启推送服务 panderman
     */
    private void beginConnect() {
        try {
            hub = conn.CreateHubProxy("ChatHub");
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(chatText.getText().toString());
        hub.On("displayDatas", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray args) {
             //   chatText.setText(args.toString());
                for (int i = 0; i < args.length(); i++) {
                  //  chatText.append(args.opt(i).toString());
                }
            }
        });
        conn.Start();
    }

    public void dianji(View view) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("210112194606100216");
        jsonArray.put("00161");
        hub.Invoke("send",jsonArray, new HubInvokeCallback() {
            @Override
            public void OnResult(boolean succeeded, String response) {
                Log.d(TAG,response);
            }

            @Override
            public void OnError(Exception ex) {
                Log.d(TAG,ex.getMessage().toString());
            }
        });
    }
}
