package com.freedrink.chromecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.freedrink.chromecontrol.components.MyTabRecyclerViewAdapter;
import com.freedrink.chromecontrol.components.TabFragment;
import com.freedrink.chromecontrol.components.dummy.TabsContent;
import com.freedrink.chromecontrol.http.HttpRequest;
import com.freedrink.chromecontrol.callbacks.InitCallback;
import com.freedrink.chromecontrol.http.ResponseCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String HOST = "http://10.0.2.2:8887";

    private InitCallback initCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView tabs = (RecyclerView) findViewById(R.id.tabView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tabs.setLayoutManager(linearLayoutManager);

        TabsContent content = new TabsContent();
        MyTabRecyclerViewAdapter adapter = new MyTabRecyclerViewAdapter(content, new TabFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(TabsContent.TabItem item) {
                Log.d("TabRecycler", item.toString());
                new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/focusTabByIndex?value="+ item.id);
            }
        });
        tabs.setAdapter(adapter);

        initCallback = new InitCallback(content, adapter);
        new HttpRequest(HOST, initCallback).execute("GET", "/getAllWindows");
    }

    public void sendMessage(View view){
        final int id = view.getId();
        switch(id){
            case R.id.prevTab:
                new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/focusPreviousTab");
                break;
            case R.id.nextTab:
                new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/focusNextTab");
                break;
                case R.id.getAll:
                new HttpRequest(HOST, initCallback).execute("GET", "/getAllWindows");
                break;
            default:
                Log.e(TAG, "ERROR? Suspicious call to sendMessage");
                break;
        }
    }

}
