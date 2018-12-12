package com.freedrink.chromecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
                int index = tabsContent.indexOf(item);
                Log.d("TabRecycler", item.toString() + index);
                tabsContent.setSelected(index);
                new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/focusTabByIndex?value="+ index);
                toggleTabsView(null);
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
                tabsContent.selectPrevious();
                break;
            case R.id.nextTab:
                new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/focusNextTab");
                tabsContent.selectNext();
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
        tabsContent.addItem("New tab", "url");
        layoutManager.scrollToPosition(pos);
        tabsContent.setSelected(pos);

        View currentTab = findViewById(R.id.currentTab);
        if (currentTab.getVisibility() == View.VISIBLE) {
            replaceCurrentTab(currentTab, pos);
        }
    }

    public void toggleTabsView(View view){
        View tabs = findViewById(R.id.tabView);
        View currentTab = findViewById(R.id.currentTab);
        if (tabs.getVisibility() == View.VISIBLE){
            tabs.setVisibility(View.INVISIBLE);
            currentTab.setVisibility(View.VISIBLE);

            replaceCurrentTab(currentTab, tabsContent.getSelectedIndex());
        } else {
            tabs.setVisibility(View.VISIBLE);
            currentTab.setVisibility(View.INVISIBLE);
        }
    }

    public void replaceCurrentTab(View currentTab, int index){
        TabsContent.TabItem currentItem = tabsContent.get(index);
        TextView currentTitle = (TextView) currentTab.findViewById(R.id.title);
        TextView currentUrl = (TextView) currentTab.findViewById(R.id.url);
        currentTitle.setText(currentItem.title);
        currentUrl.setText(currentItem.url);
    }

}
