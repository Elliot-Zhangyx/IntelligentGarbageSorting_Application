package com.example.garbagesorting.person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.garbagesorting.CirclePhotoView;
import com.example.garbagesorting.ForgetActivity;
import com.example.garbagesorting.LoginActivity;
import com.example.garbagesorting.R;
import com.example.garbagesorting.adapter.collectionAdapter;
import com.example.garbagesorting.dao.UserDao;
import com.example.garbagesorting.dao.collectionDao;
import com.example.garbagesorting.dao.followDao;
import com.example.garbagesorting.model.find;
import com.example.garbagesorting.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class Visit extends Activity implements View.OnClickListener {

    String followphone;
    String phone;
    Bundle bundle;
    TextView tv_phone;
    TextView tv_follow;
    ImageView iv_head;
    ListView listView;
    List<find> finds;
    private collectionAdapter adapter;
    SharedPreferences sp = null;
    Handler h = null;
    private UserDao userDao = new UserDao();
    private RequestOptions requestOptions = RequestOptions
            .circleCropTransform()//圆形剪裁
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.light_blue_theme);
        setContentView(R.layout.visit);
        bundle = getIntent().getExtras();
        listView = findViewById(R.id.list_find);
        finds = new ArrayList<>();
        adapter = new collectionAdapter(this, finds);
        listView.setAdapter(adapter);
        h = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                // call update gui method.
                adapter.notifyDataSetChanged();
            }
        };
        initUiti();
        initData();
    }

    private void initData() {
        tv_follow.setOnClickListener(this);
        followphone = bundle.getString("followphone");
        phone = bundle.getString("phone");
        tv_phone.setText(followphone.substring(0, 3) + "****" + followphone.substring(7, 11));
        String icon = userDao.getIcon(followphone);
        if (icon == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smssdk_failure_bg);
            Drawable drawable = new CirclePhotoView(bitmap);
            iv_head.setImageDrawable(drawable);
        } else {
            final String encodedString = "data:image/png;base64,";
            String pureBase64Encoded = icon.replace(encodedString, "");
            Bitmap pic = BitmapUtils.base64ToBitmap(pureBase64Encoded);
            System.out.println(pic);
            Glide.with(getApplication()).load(pic).apply(requestOptions).into(iv_head);
        }
        new Thread() {
            @Override
            public void run() {
                List<find> list = new ArrayList<>();
                super.run();
                Looper.prepare();
                try {
                    collectionDao collectdao = new collectionDao();
                    if (!followphone.equals("")) {
                        System.out.println("Visit");
                        list = collectdao.visitByPhone(followphone);//查询结果放到list<Garbage>中
                        finds.clear();
                        finds.addAll(list);
                        //Toast.makeText(CollectionList.this, list.size()+"hhhh", Toast.LENGTH_SHORT).show();
                        //在线程中不能直接修改ui，调用runnable，通过post修改ui
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        };
                        h.post(runnable);
                    }
                } catch (Exception e) {
                    Log.e("error", e.toString());
                }
                Looper.loop();
            }
        }.start();
    }

    private void initUiti() {
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        iv_head = findViewById(R.id.iv_head);
        tv_follow = findViewById(R.id.tv_follow);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();
        switch (id) {
            case R.id.tv_follow:
                String a = "";
                if (tv_follow.getText().equals("已关注")) {
                    followDao followdao = new followDao();
                    a = followdao.deleteByPhone(phone, followphone);
                    tv_follow.setText("+关注");
                } else {
                    followDao followdao = new followDao();
                    a = followdao.followByPhone(phone, followphone);
                    tv_follow.setText("已关注");
                }
                Toast.makeText(Visit.this, a, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}