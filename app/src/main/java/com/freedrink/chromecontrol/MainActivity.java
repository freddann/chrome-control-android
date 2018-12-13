package com.freedrink.chromecontrol;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.freedrink.chromecontrol.callbacks.InitTabsView;
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
        layoutManager = new CenterZoomLayoutManager(tabs.getContext());
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
        tabsContent.setSelectedItemListener(new TabsContent.SelectedItemListener() {
            @Override
            public void onUpdate(TabsContent.TabItem item, int position) {
                View currentTab = findViewById(R.id.currentTab);
                TextView currentTitle = (TextView) currentTab.findViewById(R.id.title);
                TextView currentUrl = (TextView) currentTab.findViewById(R.id.url);
                currentTitle.setText(item.title);
                currentUrl.setText(item.url);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.d("Swipe", viewHolder.toString() + String.valueOf(i));
                int pos = viewHolder.getAdapterPosition();
                new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/closeTabByIndex?value=" + pos);
                tabsContent.removeItem(pos);
            }
        }).attachToRecyclerView(tabs);

        initTabsView = new InitTabsView(tabsContent);
        new HttpRequest(HOST, initTabsView).execute("GET", "/getAllWindows");

        ((EditText)(findViewById(R.id.currentTab).findViewById(R.id.url))).setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE){
                            int index = tabsContent.getSelectedIndex();
                            String url = String.valueOf(v.getText());
                            TabsContent.TabItem item = tabsContent.get(index);
                            tabsContent.replaceItem(index, item.title, url);
                            Log.d("EditUrlDone", url);
                            new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/setUrl?url=" + url);
                            return true;
                        }
                        return false;
                    }
                });
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

        new HttpRequest(HOST, ResponseCallback.LOG).execute("POST", "/createNewTab");
    }

    public void toggleTabsView(View view){
        View tabs = findViewById(R.id.tabView);
        View currentTab = findViewById(R.id.currentTab);
        if (tabs.getVisibility() == View.VISIBLE){
            tabs.setVisibility(View.INVISIBLE);
            currentTab.setVisibility(View.VISIBLE);
        } else {
            tabs.setVisibility(View.VISIBLE);
            currentTab.setVisibility(View.INVISIBLE);
        }
    }

}
