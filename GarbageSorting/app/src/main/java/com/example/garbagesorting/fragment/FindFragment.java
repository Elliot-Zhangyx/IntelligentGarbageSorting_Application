package com.example.garbagesorting.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.garbagesorting.AddActivity;
import com.example.garbagesorting.R;
import com.example.garbagesorting.adapter.BaseAdapter;
import com.example.garbagesorting.adapter.MyAdapter;
import com.example.garbagesorting.adapter.findAdapter;
import com.example.garbagesorting.dao.FindDao;
import com.example.garbagesorting.model.find;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FindFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView rec_find;
    private SwipeRefreshLayout refresh;
    private View root;
    private ArrayList<find> list=new ArrayList<>();
    private findAdapter adapter;
    private FindDao findDao=new FindDao();
    public static final int ADD = 100;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 10;
    private GridLayoutManager mLayoutManager;
    private GridLayoutManager mLayoutManager2;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    int loadCount;
    SharedPreferences sp=null;
    private LocalBroadcastManager broadcastManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_find, container, false);
        initUI();
        initData();
        //initRefreshLayout();
        new UpdateDateThd(updateDateHandler).start();
       // initRecyclerView();
        return root;
    }
    private void initUI(){
        rec_find=root.findViewById(R.id.rec_find);
        refresh=root.findViewById(R.id.refresh);
        sp=getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
    }
    private void initData(){
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                if(sp.getString("phone",null)!=null){
                    intent.setClass(getActivity(), AddActivity.class);
                    getActivity().startActivityForResult(intent, ADD);
                }
                else {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
            }
        });
       // list = findDao.queryAll();

    }
//    private void initRefreshLayout() {
//
//        refresh.setOnRefreshListener(this);
//    }
//    private List<find> getDatas(final int firstIndex, final int lastIndex) {
//        List<find> resList = new ArrayList<>();
//        for (int i = firstIndex; i < lastIndex; i++) {
//            if (i < list.size()) {
//                resList.add(list.get(i));
//            }
//        }
//        return resList;
//    }
//
//    private void updateRecyclerView(int fromIndex, int toIndex) {
//        List<find> newDatas = getDatas(fromIndex, toIndex);
//        if (newDatas.size() > 0) {
//            adapter.updateList(newDatas, true);
//        } else {
//            adapter.updateList(null, false);
//        }
//    }
//    @Override
//    public void onRefresh() {
//        refresh.setRefreshing(true);
//        adapter.resetDatas();
//        updateRecyclerView(0, PAGE_COUNT);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                refresh.setRefreshing(false);
//            }
//        }, 1000);
//    }
//
//    private void initRecyclerView() {
//
//    }
    @SuppressLint("HandlerLeak")
    private Handler updateDateHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取日期,显示
            Bundle bundle = msg.getData();
            ArrayList date = (ArrayList) bundle.getSerializable("value");
            //dateView.setText(date);
            rec_find.setLayoutManager (new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
            adapter=new findAdapter(getContext(),date);
            rec_find.setAdapter(adapter);
//            adapter = new findAdapter(getContext(),date,  getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
//            mLayoutManager = new GridLayoutManager(getContext(), 2);
//            rec_find.setLayoutManager(mLayoutManager);
//            rec_find.setAdapter(adapter);
//            rec_find.setItemAnimator(new DefaultItemAnimator());

//            rec_find.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
//                                }
//                            }, 500);
//                        }
//
//                        if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
//                            mHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
//                                }
//                            }, 500);
//                        }
//                    }
//                }
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//                }
//            });
        }
    };

    @Override
    public void onRefresh() {

    }

    //发送消息给UI线程的子线程.
    class UpdateDateThd extends Thread{
        private Handler handler;
        private boolean bool=true;

        public UpdateDateThd(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run () {

            while (bool) {
//                try {
                    //每秒向主线程发送一次消息
//                    TimeUnit.MILLISECONDS.sleep(10000);
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String date = sdf.format(new Date());

                try {


//                    //创建被装饰者类实例
//                    final MyAdapter adapter = new MyAdapter(getContext());
//                    //创建装饰者实例，并传入被装饰者和回调接口
//
//                    mAdapter = new LoadMoreAdapterWrapper(adapter, new LoadMoreAdapterWrapper.OnLoad(){
//                        @Override
//                        public void load(int pagePosition, int pageSize, final LoadMoreAdapterWrapper.ILoadCallback callback) {
//                            //此处模拟做网络操作，2s延迟，将拉取的数据更新到adpter中
//
//                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    List<String> dataSet = new ArrayList();
//
//                                    for (int i = 0; i < 4; i++) {
//                                        list = findDao.queryAll();
//                                        sendMessage(list);
//                                        bool=false;
//                                    }
//                                    //数据的处理最终还是交给被装饰的adapter来处理
//                                    adapter.appendData(dataSet);
//                                    callback.onSuccess();
//                                    //模拟加载到没有更多数据的情况，触发onFailure
//                                    if (loadCount++ == 3) {
//                                        callback.onFailure();
//                                    }
//                                }
//                            }, 2000);
//                        }
//                    });
                   // rec_find.setAdapter(mAdapter);
                  //  rec_find.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    list=findDao.queryAll();
                    sendMessage(list);
                    TimeUnit.MILLISECONDS.sleep(10000);
                    bool=false;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bool=false;
            }
        }

        private void sendMessage(ArrayList msg){
            Bundle data = new Bundle();
            data.putSerializable("value",list);
            Message message = new Message();
            message.setData(data);

            this.handler.sendMessage(message);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照后返回
        if (resultCode==8) {
            //显示图片0
            System.out.println("秋明");
            find fd=(find) data.getSerializableExtra("find");
            System.out.println(fd.getPhone());
            rec_find.setLayoutManager (new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
            adapter.notifyItemInserted(0);
            list.add(0,fd);
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            rec_find.scrollToPosition(0);
            return;
        }

    }
    public void onResume() {
        super.onResume();
        new UpdateDateThd(updateDateHandler).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        new UpdateDateThd(updateDateHandler).start();
    }

}