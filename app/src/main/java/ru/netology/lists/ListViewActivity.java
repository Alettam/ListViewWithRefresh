package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListViewActivity extends AppCompatActivity {

    private static final String KEY_TITLE = "key_title";
    private static final String KEY_COUNT = "key_count";
    private static String SAVE_TEXT = "note_text";
    private SharedPreferences saveTxt;
    private BaseAdapter listContentAdapter;
    private List<Map<String,String>> values;
    private SwipeRefreshLayout refreshLayout;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
       //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        saveTxt = getSharedPreferences("MySavedText", MODE_PRIVATE);

        initviews();


        values = prepareContent();

        listContentAdapter = createAdapter(values);

        list.setAdapter(listContentAdapter);
    }

    private void initviews() {
        list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                values.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }

        });
        refreshLayout = findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                values = prepareContent();
                listContentAdapter = createAdapter(values);
                list.setAdapter(listContentAdapter);
                refreshLayout.setRefreshing(false);
                listContentAdapter.notifyDataSetChanged();
            }
        });
    }



    @NonNull
    private BaseAdapter createAdapter(List<Map<String,String>> values) {
        return new SimpleAdapter(this,values,R.layout.main_activity, new String[]{KEY_TITLE,KEY_COUNT},
                new int[]{R.id.textView,R.id.symbCountTv});
    }

    @NonNull
    private List<Map<String,String>> prepareContent() {
        List<Map<String,String>> result = new ArrayList<>();
        String noteText = saveTxt.getString(SAVE_TEXT,"");
        if(noteText == "") {
            SharedPreferences.Editor myEditor = saveTxt.edit();
            myEditor.putString(SAVE_TEXT, getString(R.string.large_text));
            myEditor.apply();

            String[] titles = getString(R.string.large_text).split("\n\n");
            for (String title:titles){
                Map<String,String> map = new HashMap<>();
                map.put(KEY_TITLE,title);
                map.put(KEY_COUNT, title.length() +"");
                result.add(map);
            }
        }else {
            String[] titles = noteText.split("\n\n");
            for (String title : titles) {
                Map<String, String> map = new HashMap<>();
                map.put(KEY_TITLE, title);
                map.put(KEY_COUNT, title.length() + "");
                result.add(map);
            }
        }
        return result;
    }
}
