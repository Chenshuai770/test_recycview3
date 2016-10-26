package com.cs.test_recycview3;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * 刷新
 * setOnRefreshListener(OnRefreshListener):添加下拉刷新监听器

 *setRefreshing(boolean):显示或者隐藏刷新进度条

 *isRefreshing():检查是否处于刷新状态
 * RecyclerView
 * findFirstVisibleItemPosition()
 *findFirstCompletlyVisibleItemPosition()
 *findLastVisibleItemPosition()
 *findLastCompletlyVisibleItemPosition()
 */

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recycle;
    private SwipeRefreshLayout refresh;
    private boolean isLoading;
    private Handler handler = new Handler();
    private List<String > data = new ArrayList<>();
    private MyAdapter adapter=new MyAdapter(this,data);
    private SwipeRefreshLayout refresh1;
    private LinearLayoutManager layoutManager;
    private int lastVisibleItemPosition;
    private Button btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn= (Button) findViewById(R.id.btn);
        initView();
        recycle.addItemDecoration(new MyDecoration(getApplicationContext(),MyDecoration.VERTICAL_LIST));
        MyAnimator animator = new MyAnimator();
        animator.setChangeDuration(2000);
        recycle .setItemAnimator(animator);
        adapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "你点击了"+position+"个", Toast.LENGTH_SHORT).show();
            }



            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


    }

    private void initView() {
        refresh= (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light,
                android.R.color.holo_green_light);


        recycle = (RecyclerView) findViewById(R.id.recycle);
        layoutManager = new LinearLayoutManager(this);
        setData();
        recycle.setLayoutManager(layoutManager);
        recycle.setAdapter(adapter);


        //为RecyclerView添加HeaderView和FooterView   
        setHeaderView(recycle);
        setFooterView(recycle);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable
                        .timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                        .map(new Func1<Long, Object>() {
                            @Override
                            public Object call(Long aLong) {
                                data.add(0,"下拉刷新的数据");

                                refresh.setRefreshing(false);
                                adapter.notifyDataSetChanged();

                                return null;
                            }
                        }).subscribe();
            }

        });
        //为RecyclerView添加HeaderView和FooterView   
       
        //RecyclerView滑动监听
       recycle.addOnScrollListener(new EndLessOnScrollListener(layoutManager) {
           @Override
           public void onLoadMore(int currentPage) {
               simulateLoadMoreData();


           }
       });


       




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.clear();
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void setFooterView(RecyclerView view) {
        View footer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_foot, view, false);
        adapter.setFooterView(footer);

    }

    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_head, view, false);
        adapter.setHeaderView(header);
    }

    private void simulateLoadMoreData() {
        Observable
                .timer(1,TimeUnit.SECONDS,AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        List<String> moreList = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            moreList.add("加载更多的数据"+i);
                        }
                        data.addAll(moreList);

                        adapter.notifyDataSetChanged();

                        return null;
                    }
                }).subscribe();
    }

    private void setData() {
        for (int i = 0; i < 20; i++) {
            data.add("原始网路数据"+i);
        }
    }


}
