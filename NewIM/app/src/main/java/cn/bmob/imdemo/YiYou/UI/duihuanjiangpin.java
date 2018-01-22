package cn.bmob.imdemo.YiYou.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.base.BaseActivity;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.Utils.ImageLoaderAsync2;
import cn.bmob.imdemo.YiYou.bean.complain;
import cn.bmob.imdemo.YiYou.bean.exchange;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.imdemo.R.id.leftnumber_dh;

/**
 * Created by DELL on 2017/6/1.
 */

public class duihuanjiangpin extends BaseActivity {
    ListView mylistview;
    TextView myjifen;
    ImageView dhsetback;
    RelativeLayout duihuanset;
    RelativeLayout duihuanset2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duihuanjiangpin);
        Window window=getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        mylistview= (ListView) findViewById(R.id.dhlistview);
        myjifen= (TextView) findViewById(R.id.myjifen);
        dhsetback= (ImageView) findViewById(R.id.dhsetback);
        duihuanset= (RelativeLayout) findViewById(R.id.duihuanset);
        duihuanset2= (RelativeLayout) findViewById(R.id.duihuanset2);
        duihuanset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(duihuanjiangpin.this,duihuanjiangpinset.class));
            }
        });
        duihuanset2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(duihuanjiangpin.this,Integraltask.class));
            }
        });
        dhsetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final User user = BmobUser.getCurrentUser(User.class);
        myjifen.setText(user.getMywallet()+"");
        BmobQuery<exchange> query=new BmobQuery<>();
        query.order("paixu");
        query.findObjects(new FindListener<exchange>() {
            @Override
            public void done(List<exchange> list, BmobException e) {
                if (e == null) {
                    mylistview.setAdapter(new adapter(getApplicationContext(), list, mylistview));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class adapter extends BaseAdapter implements AbsListView.OnScrollListener {
        List<exchange> list;
        LayoutInflater layoutInflater;
        Context context;
        private ImageLoaderAsync2 imageLoaderAsync;
        private int mstart,mend;
        //通过第一项和最后一项，获取当前可见项的所有图片的url地址
        String[] urls;
        String[] tags;
        private boolean mFirstIn;
        HashMap<Integer, Boolean> isCheck;

        public adapter(Context context,List<exchange> list,ListView listView) {
            this.context=context;
            this.list=list;
            this.layoutInflater=LayoutInflater.from(context);
            urls=new String[list.size()];
            tags=new String[list.size()];
            for(int i=0;i<list.size();i++){
                urls[i]=list.get(i).getUrl();
            }
            for(int i=0;i<list.size();i++){
                tags[i]=list.get(i).getObjectId();
            }
            isCheck = new HashMap<>();
            mFirstIn=true;
            imageLoaderAsync=new ImageLoaderAsync2(mylistview,getApplicationContext());
            listView.setOnScrollListener(this);
            initData();
        }

        private void initData() {
            for (int i = 0; i < list.size(); i++) {
                isCheck.put(i, false);
            }
        }
        @Override
        public int getCount() {
            return list.size();

        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        //下面两个方法禁止listview的点击事件
        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder=null;
            if(convertView==null){
                holder=new ViewHolder();
                convertView =layoutInflater.inflate(R.layout.item_jiangpin,null);
                holder.name_dh= (TextView) convertView.findViewById(R.id.name_dh);
                holder.leftnumber_dh= (TextView) convertView.findViewById(leftnumber_dh);
                holder.integral_dh= (TextView) convertView.findViewById(R.id.integral_dh);
                holder.beizhu_dh= (TextView) convertView.findViewById(R.id.beizhu_dh);
                holder.url_dh= (ImageView) convertView.findViewById(R.id.url_dh);
                holder.duihuanlay= (RelativeLayout) convertView.findViewById(R.id.duihuanlay);
                holder.dhtxt= (TextView) convertView.findViewById(R.id.dhtxt);
                convertView.setTag(holder);
            }else
            {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.beizhu_dh.setText(list.get(i).getBeizhu());
            holder.leftnumber_dh.setText("本周剩余"+list.get(i).getLeftnumber()+"份");
            holder.integral_dh.setText(list.get(i).getIntegral()+"积分");
            holder.name_dh.setText(list.get(i).getName());
            holder.url_dh.setTag(list.get(i).getObjectId());
            imageLoaderAsync.showImageByAsync(holder.url_dh,list.get(i).getUrl());
            if(isCheck.get(i)){
                holder.duihuanlay.setBackgroundResource(R.color.ededed);
                holder.dhtxt.setText("已兑换");
            }else{
                holder.duihuanlay.setBackgroundResource(R.drawable.jiangpin_drawable);
                holder.dhtxt.setText("兑换");
            }
            final ViewHolder finalHolder = holder;
            holder.duihuanlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final User user = BmobUser.getCurrentUser(User.class);
                    if(list.get(i).getLeftnumber()<=0){
                        toast("可兑换数量不足，请等待补货");
                    }else {
                        if (user.getMywallet() >= list.get(i).getIntegral()) {
                            if (!isCheck.get(i)) {
                                isCheck.put(i, true);
                            }
                            finalHolder.duihuanlay.setBackgroundResource(R.color.ededed);
                            finalHolder.dhtxt.setText("已兑换");
                            toast("恭喜您兑换成功，客服稍候会把奖品发送给您");
                            Integer left = user.getMywallet() - list.get(i).getIntegral();
                            Integer left2=list.get(i).getLeftnumber()-1;
                            myjifen.setText(left + "");
                            if(left2<0){
                                finalHolder.leftnumber_dh.setText("本周剩余0份");
                            }else{
                                finalHolder.leftnumber_dh.setText("本周剩余"+left2+"份");
                            }
                            User user1 = new User();
                            user1.setMywallet(left);
                            user1.update(user.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        complain complain = new complain();
                                        complain.setUSERID(user.getObjectId());
                                        complain.setUSERimage(user.getAvatar());
                                        complain.setUSERname(user.getUsername());
                                        complain.setCompliainTEXT("对方兑换了" + list.get(i).getName());
                                        complain.setType("兑换");
                                        complain.setCompleted(false);
                                        complain.setExchangeUrl(list.get(i).getUrl());
                                        complain.setIntegral(list.get(i).getIntegral());
                                        complain.setBeizhu_dh(list.get(i).getBeizhu());
                                        complain.setPhonenumber(user.getMobilePhoneNumber());
                                        complain.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e != null) {
                                                    toast("哪里出错了，请重新进入界面点击兑换");
                                                    myjifen.setText(user.getMywallet() + "");
                                                    User user2 = new User();
                                                    user2.setMywallet(user.getMywallet());
                                                    user2.update(user.getObjectId(), new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                        }
                                                    });
                                                }else{
                                                    //可兑换数量减少一份
                                                    exchange exchang=new exchange();
                                                    exchang.increment("leftnumber",-1);
                                                    exchang.update(list.get(i).getObjectId(),new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        toast("哪里出错了，请重新进入界面点击兑换");
                                    }
                                }
                            });
                        } else {
                            //积分不足，无法兑换
                            toast("您的积分不足，无法兑换");
                        }
                    }
                }
            });
            return convertView;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState==SCROLL_STATE_IDLE) {
                //开始加载任务
                imageLoaderAsync.loadimage(mstart, mend,urls,tags);
                Log.d("xzf6","滑动停止");
            } else{
                //停止加载任务
                imageLoaderAsync.cancellALLTask();
                Log.d("xzf6","开始滑动");
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //第一个可见元素和最后一个可见元素
            mstart=firstVisibleItem;
            mend=firstVisibleItem+visibleItemCount;
            Log.d("zpf","mstart:"+mstart);
            Log.d("zpf","mend:"+mend);
            //这个是初始化的预加载
            if(mFirstIn&&visibleItemCount>0){
                imageLoaderAsync.loadimage(mstart,mend,urls,tags);
                mFirstIn=false;
            }
        }

    }
    public class ViewHolder
    {
        RelativeLayout duihuanlay;
        TextView name_dh;
        TextView leftnumber_dh;
        TextView integral_dh;
        TextView beizhu_dh;
        ImageView url_dh;
        TextView dhtxt;
    }




}

