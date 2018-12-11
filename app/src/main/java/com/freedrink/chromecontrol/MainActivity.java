package com.freedrink.chromecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.freedrink.chromecontrol.callbacks.InitTabsView;
import com.freedrink.chromecontrol.callbacks.TabsTouchHelper;
import com.freedrink.chromecontrol.components.tabview.CenterZoomLayoutManager;
import com.freedrink.chromecontrol.components.tabview.MyTabRecyclerViewAdapter;
import com.freedrink.chromecontrol.components.tabview.OverlapViewItems;
import com.freedrink.chromecontrol.components.tabview.TabFragment;
import com.freedrink.chromecontrol.components.tabview.TabsContent;
import com.freedrink.chromecontrol.http.HttpRequest;
import com.freedrink.chromecontrol.http.ResponseCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String HOST = "http://10.0.2.2:8887";

    private InitTabsView initTabsView;
    private TabsContent tabsContent;
    MyTabRecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView tabs = (RecyclerView) findViewById(R.id.tabView);
        layoutManager = new CenterZoomLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tabs.addItemDecoration(new OverlapViewItems());
        tabs.setLayoutManager(layoutManager);

        tabsContent = new TabsContent();
        adapter = new MyTabRecyclerViewAdapter(tabsContent, new TabFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(TabsContent.TabItem item) {
                Log.d("TabRecycler", item.toString());
                new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/focusTabByIndex?value="+ item.id);
            }
        });
        tabs.setAdapter(adapter);
        tabsContent.setListener(adapter);
        new TabsTouchHelper(tabsContent).attachToRecyclerView(tabs);

        initTabsView = new InitTabsView(tabsContent);
        new HttpRequest(HOST, initTabsView).execute("GET", "/getAllWindows");
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
                new HttpRequest(HOST, initTabsView).execute("GET", "/getAllWindows");
                break;
            default:
                Log.e(TAG, "ERROR? Suspicious call to sendMessage");
                break;
        }
    }

    public void createTab(View view){
        int pos = tabsContent.size();
        tabsContent.addItem(pos,"New tab", "url");
        layoutManager.scrollToPosition(pos);
    }

}
