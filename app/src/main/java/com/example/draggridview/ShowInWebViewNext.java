package com.example.draggridview;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.net.URL;
import java.util.*;

public class ShowInWebViewNext extends Activity {

    java.util.List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();
    URL url;


    DragGridView mDragGridView;
    RecordAdapter mSimpleAdapter;
    String te[] = new String[100];
    private SQLiteDatabase db;
    String ArrayTextSplit[] ;
    Handler handler=new Handler();
    MyDataDB dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_in_web_view_next);
        Bundle bundle = this.getIntent().getExtras();
        final String Array = bundle.getString("Array");
        ArrayTextSplit = Array.split("----");

        dbHelper = new MyDataDB(this);

        mSimpleAdapter = new RecordAdapter(this,dataSourceList);
//		LayoutInflater inflater = getLayoutInflater();
//		View otherLayout = inflater.inflate(R.layout.grid_item, null);
//		TextView otherTv1 = (TextView)otherLayout.findViewById(R.id.words_home_function_1);


        mDragGridView = (DragGridView) findViewById(R.id.dragGridView);

        Cursor cursor = dbHelper.select();	//取得SQLite類別的回傳值:Cursor物件
        cursor.moveToFirst();
        do {

            HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
            itemHashMap.put("words", cursor.getString(1));
            dataSourceList.add(itemHashMap);
            handler.post(runnableUi);

        }while (cursor.moveToNext());
        // for (int x=1; x<ArrayTextSplit.length ;x++) {






    }

    Runnable runnableUi = new Runnable(){
        @Override
        public void run() {
            //更新界面
            mDragGridView.setAdapter(mSimpleAdapter);
            mDragGridView.setOnChangeListener(new DragGridView.OnChanageListener() {

                @Override
                public void onChange(int from, int to) {
                    HashMap<String, Object> temp = dataSourceList.get(from);
                    //直接交互item
//									dataSourceList.set(from, dataSourceList.get(to));
                    //				dataSourceList.set(to, temp);


                    //这里的处理需要注意下
                    if (from < to) {
                        for (int i = from; i < to; i++) {
                            Collections.swap(dataSourceList, i, i + 1);
                        }
                    } else if (from > to) {
                        for (int i = from; i > to; i--) {
                            Collections.swap(dataSourceList, i, i - 1);
                        }
                    }


                    dataSourceList.set(to, temp);

                    mSimpleAdapter.notifyDataSetChanged();


                }
            });
        }

    };
}
