package com.cs.test_recycview3;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chenshuai on 2016/10/25.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ItemViewHolder> {

    private Context context;

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的


    //获取从Activity中传递过来每个item的数据集合
    private List<String> mDatas;

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public MyAdapter(Context context, List<String> mTitles) {
        this.context = context;
        this.mDatas = mTitles;

    }


    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);//加入头部
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);//注意这里
    }
    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {

        if (mHeaderView == null&& mFooterView==null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            return TYPE_HEADER;

        }
        if (position == getItemCount()-1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ItemViewHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ItemViewHolder(mFooterView);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        //这边可以做一些属性设置，甚至事件监听绑定
        //view.setBackgroundColor(Color.RED);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof ItemViewHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                holder.tv_content.setText(mDatas.get(position-1));
                // 如果设置了回调，则设置点击事件
                if (mOnItemClickLitener != null) {
                    ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int pos=holder.getLayoutPosition();
                            mOnItemClickLitener.onItemClick(((ItemViewHolder) holder).itemView,pos);
                        }
                    });
                    ((ItemViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                            return false;

                        }
                    });


                }

                return;
            }
        }else if (getItemViewType(position)==TYPE_HEADER) {
            holder.tv_header.setText("我是高手");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(((ItemViewHolder) holder).itemView,pos);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(view,pos);
                    return false;
                }
            });

            return;
        }else {

            holder.srf_foot.setProgressBackgroundColorSchemeResource(android.R.color.white);
            holder.srf_foot.setColorSchemeResources(android.R.color.holo_blue_light,
                    android.R.color.holo_red_light,android.R.color.holo_orange_light,
                    android.R.color.holo_green_light);
           // holder.srf_foot.setRefreshing(true);
            // 如果设置了回调，则设置点击事件
            return;
        }
    }

    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mDatas.size();
        }else if (mHeaderView == null && mFooterView != null) {
            return mDatas.size() + 1;
        }else if (mHeaderView != null && mFooterView == null) {
            return mDatas.size() + 1;
        }else {
        return mDatas.size()+2;
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        public   TextView tv_content;
        public   TextView tv_header;
        public   TextView tv_footer;
        public SwipeRefreshLayout srf_foot;
        public ItemViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView){
                tv_header= (TextView) itemView.findViewById(R.id.header);
                return;
            }
            if (itemView == mFooterView){
                srf_foot= (SwipeRefreshLayout) itemView.findViewById(R.id.item_srf);
                return;
            }
            tv_content= (TextView) itemView.findViewById(R.id.item_text);
        }
    }

}
