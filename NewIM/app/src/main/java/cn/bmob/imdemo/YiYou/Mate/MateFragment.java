package cn.bmob.imdemo.YiYou.Mate;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.imdemo.R;
import cn.bmob.imdemo.bean.Mate;
import cn.bmob.imdemo.bean.User;
import cn.bmob.imdemo.YiYou.ChangeFragment;
import cn.bmob.imdemo.YiYou.MyTimeDialog.DateTimePickerDialog;
import cn.bmob.imdemo.YiYou.UI.changehead;
import cn.bmob.imdemo.YiYou.UI.comfirmstate;
import cn.bmob.imdemo.YiYou.UI.matchinfo;
import cn.bmob.imdemo.YiYou.Utils.CircleImageView;
import cn.bmob.imdemo.YiYou.Utils.ImageLoader;
import cn.bmob.imdemo.YiYou.Utils.MyUtils;
import cn.bmob.imdemo.YiYou.bean.LastInfo;
import cn.bmob.imdemo.YiYou.bean.MyUser;
import cn.bmob.imdemo.YiYou.mylogin.MyApplication;
import cn.bmob.imdemo.ui.ChatActivity;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.iwgang.countdownview.CountdownView;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by DELL on 2017/5/21.
 */

@SuppressLint("ValidFragment")
public class MateFragment extends android.support.v4.app.Fragment {
    private static Long mydate;
    private RelativeLayout CatBillboard_Relayout;
    private ImageView Billboard_img;
    private Dialog cardDLG;
    private AlertDialog XMDLG;
    private String choicename;
    private TextView choicenam;
    private ImageView choiceimg;
    private TextView choicexiangmu;
    private LinearLayout choicelay;
    private TextView jiheditx;
    private  TextView nospinnertx;
    private LinearLayout nospinnerlay;
    private TextView nospinertxtx;
    private TextureMapView mMapView;
    private BaiduMap mbitmap;
    private String timetxstring;
    TextView timepicker;
    TextView timetx;
    TextView sexchoice;
     Button fgbutton;
    LinearLayout populay;
    LinearLayout newlay;
    String sex;
    ListView listView;
    RelativeLayout noinfotx;
    CountdownView countdownView;
    LinearLayout MateStatus_Llayout;
    ImageView ICONset;
    ImageView tc;
    LinearLayout lunbo;
    LinearLayout twoiconlay;
    User user;
    ImageView dianji;
    TextView title;
    TextView description;
    //动态注册广播接收器
    private IntentFilter intentFilter;
    private MyBroadReceiver broadReceiver;
    ImageView three;
    TextView Nearbynumber_tex;
    //这个是持续匹配的时间函数(最优模式)
    private CountDownTimer mostcdTimer;
    private CountDownTimer bettersecondcdTimer;
    private CountDownTimer betterthirdcdTimer;
    //这个属性表示被我占用了同步锁的用户
    private String UseOtheUsersSynchronizedID;
    private String[] LocalMateUserActivity;
    ChangeFragment changeFragment;
    private MateAnimation mateAnimation;
    View rootview;
    //数据保存类
    private MateContains mateContains;


    public MateFragment(ChangeFragment changeFragment) {
        this.changeFragment = changeFragment;
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.matefragment,container, false);
        initInflate(rootview);
        if(MyApplication.isshowtx2){
            noinfotx.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            CatBillboard_Relayout.setVisibility(View.GONE);
            MyApplication.isshowtx2=false;
        }
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user= BmobUser.getCurrentUser(User.class);
        mateAnimation=new MateAnimation(CatBillboard_Relayout,rootview);
        //这是匹配信息的界面
       if(!MyApplication.isshowtx2) {
           querylistview(false);
       }
        MyApplication.isblack=true;
        dianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().equals("开始匹配")){
                    if(!MyApplication.isblack){
                        dianji.setImageResource(R.drawable.dianji_leftbai);
                    }else{
                        dianji.setImageResource(R.drawable.dianji_left363636);
                    }

                    title.setText("正在进行");
                    description.setText("去看看您和小伙伴的约定，不能违约哦！");
                    CatBillboard_Relayout.setVisibility(View.GONE);

                }else{
                    if(!MyApplication.isblack){
                        dianji.setImageResource(R.drawable.dianjibai);
                    }else{
                        dianji.setImageResource(R.drawable.dianji363636);
                    }

                    title.setText("开始匹配");
                    description.setText("还没有小伙伴，赶快去匹配吧！");
                    CatBillboard_Relayout.setVisibility(View.VISIBLE);
                }

            }
        });

        //动态注册广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction("android");
        broadReceiver = new MyBroadReceiver();
        getActivity().registerReceiver(broadReceiver, intentFilter);
        //退出匹配的点击方法
        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //恢复重置
               MyUtils.rematch(sp.getString("userMateID",""));
                //切换fragment
                SendCancelBrocast();
                //退出的匹配方法：上传数据
                Mate mate=new Mate();
                mate.setMateItem("");
                mate.setMateSex("");
                mate.setWhetherMatching(false);
                mate.setMateUserFlag(false);
                mate.setSynchronizedID("");
                mate.setMateUserActivity(new String[]{"","",""});
                mate.update(sp.getString("Mate", ""), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            CancelTimerTask();
                        }
                    }
                });

            }
        });
        //匹配优先度的设定
        ICONset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchDialog();
            }
        });

        //正常匹配时候的动画
        normalAnimantion();

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 changeFragment.change(v.getId());
            }
        });
        MateMonitor();
        //下面是查询方法
        //头一分钟，是最优匹配
        mostcdTimer = new CountDownTimer(120000, 2000) {
            @Override
            public void onTick(long l) {
                Log.d("xzf", "第一阶段正在匹配,尚无结果" + l);
                CountTimeStart2("项目性别", mostcdTimer);
            }
            @Override
            public void onFinish() {
                CountTimerFinish(bettersecondcdTimer, 2);
            }
        };

        //第二分钟，次级匹配
        bettersecondcdTimer = new CountDownTimer(120000, 2000) {
            @Override
            public void onTick(long l) {
                Log.d("xzf", "第二阶段正在匹配,尚无结果" + l);
                if (sp.getString("SecondChoice", "性别").equals("项目")) {
                    CountTimeStart2("仅项目", bettersecondcdTimer);
                } else {
                    CountTimeStart2("仅性别", bettersecondcdTimer);
                }
            }

            @Override
            public void onFinish() {
                CountTimerFinish(betterthirdcdTimer, 3);
            }
        };

        //第三分钟，默认匹配
        betterthirdcdTimer = new CountDownTimer(120000, 2000) {
            @Override
            public void onTick(long l) {
                Log.d("xzf", "第三阶段正在匹配,尚无结果" + l);
                CountTimeStart2("无要求匹配", betterthirdcdTimer);
            }

            @Override
            public void onFinish() {
                //取消匹配任务
                betterthirdcdTimer.cancel();
                //继续监听，弹个toast提示人少
                mostcdTimer.start();
                //刷新时间
                Intent intent1 = new Intent("android");
                intent1.putExtra("te", "新的匹配");
                getActivity().sendBroadcast(intent1);
//                toast("当前用户人数较少，您可以继续匹配，或者按退出按钮退出");
            }
        };
    }





    //对话框
    public void startdialog(){
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LayoutInflater inflater=LayoutInflater.from(getContext());
        //这个是大对话框用到的view
        final View myview=inflater.inflate(R.layout.fgpopupwindow,null);
        cardDLG = new AlertDialog.Builder(getContext()).create();
        cardDLG .show();
        cardDLG .getWindow().setContentView(myview);
        // 设置弹出的动画效果
        cardDLG .getWindow().setWindowAnimations(R.style.AnimBottom);
        WindowManager.LayoutParams wlp = cardDLG.getWindow().getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width= (int) (width*0.95);
        wlp.height= (int) (height*0.95);
        cardDLG.getWindow().setAttributes(wlp);
        cardDLG.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        populay= (LinearLayout) myview.findViewById(R.id.popuplay);
        newlay= (LinearLayout) myview.findViewById(R.id.newlay);
        final Button queding = (Button) myview.findViewById(R.id.queding);
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                      pipei();
            }});

        timepicker= (TextView) myview.findViewById(R.id.timepicker);
        timetx= (TextView) myview.findViewById(R.id.timetx);
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        timetx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        jiheditx= (TextView) myview.findViewById(R.id.jiheditx);
        nospinnertx= (TextView) myview.findViewById(R.id.nospinertx);
        nospinnertx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //这里是下面百度地图显示的集合区域的显示

            }
        });
        nospinertxtx= (TextView) myview.findViewById(R.id.nospinertxtx);
        nospinnerlay= (LinearLayout) myview.findViewById(R.id.nospinerlay);
        nospinnerlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                final String[]arr={"南京财经大学操场","南京财经大学图书馆","南京财经大学校门","南京邮电大学操场","南京邮电大学图书馆",
                        "南京邮电大学校门","南京师范大学操场","南京师范大学图书馆","南京师范大学校门"};
                builder.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nospinnertx.setText(arr[which]);
                        nospinertxtx.setText("请在约定时间内在该地点集合");
                        jiheditx.setVisibility(View.GONE);
                        nospinnerlay.setVisibility(View.VISIBLE);
                        enablebutton();
                        String space="南"+arr[which].substring(2,3)+arr[which].substring(6);
                        addoverlays(MyUtils.getlatlang(space));
                    }
                });
                builder.create().show();
            }
        });
        jiheditx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                final String[]arr={"南京财经大学操场","南京财经大学图书馆","南京财经大学校门","南京邮电大学操场","南京邮电大学图书馆",
                        "南京邮电大学校门","南京师范大学操场","南京师范大学图书馆","南京师范大学校门"};
                builder.setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nospinnertx.setText(arr[which]);
                        nospinertxtx.setText("请在约定时间内在该地点集合");
                        jiheditx.setVisibility(View.GONE);
                        nospinnerlay.setVisibility(View.VISIBLE);
                        enablebutton();
                        String space="南"+arr[which].substring(2,3)+arr[which].substring(6);
                        addoverlays(MyUtils.getlatlang(space));
                    }
                });
                builder.create().show();
            }
        });
        sexchoice= (TextView) myview.findViewById(R.id.sexchoice);
        final LinearLayout sexlay= (LinearLayout) myview.findViewById(R.id.sexlay);
        final TextView sex_all= (TextView) myview.findViewById(R.id.sex_all);
        final TextView sex_boy= (TextView) myview.findViewById(R.id.sex_boy);
        final TextView sex_girl= (TextView) myview.findViewById(R.id.sex_girl);
//        List<Map<String,TextView>> list1=new ArrayList<>();
         choicenam= (TextView) myview.findViewById(R.id.choicename);
         choiceimg= (ImageView) myview.findViewById(R.id.choiceimg);
         choicelay= (LinearLayout) myview.findViewById(R.id.choicelay);
        fgbutton= (Button) myview.findViewById(R.id.fgbutton);
        choicexiangmu= (TextView) myview.findViewById(R.id.choicexiangmu);
        //第一个项目匹配的两个监听事件
        choicexiangmu.setOnClickListener(new myxiangmuclicklistener());
        choicelay.setOnClickListener(new myxiangmuclicklistener());
        mMapView= (TextureMapView) myview.findViewById(R.id.mymap);
        addoverlays(MyUtils.getlatlang("南财图书馆"));
        ImageView popuexit= (ImageView) myview.findViewById(R.id.popuexit);
        popuexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardDLG.dismiss();
            }
        });
        sexchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexchoice.setVisibility(View.GONE);
                sexlay.setVisibility(View.VISIBLE);
                sex_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication.choicesex="不限";
                        sex="不限";
                        sex_all.setTextColor(Color.BLACK);
                        sex_boy.setTextColor(getResources().getColor(R.color.colorccc));
                        sex_girl.setTextColor(getResources().getColor(R.color.colorccc));
                        enablebutton();
                    }
                });
                sex_boy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication.choicesex="男";
                        sex="男";
                        sex_boy.setTextColor(Color.BLACK);
                        sex_all.setTextColor(getResources().getColor(R.color.colorccc));
                        sex_girl.setTextColor(getResources().getColor(R.color.colorccc));
                        enablebutton();
                    }
                });
                sex_girl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication.choicesex="女";
                        sex="女";
                        sex_girl.setTextColor(Color.BLACK);
                        sex_boy.setTextColor(getResources().getColor(R.color.colorccc));
                        sex_all.setTextColor(getResources().getColor(R.color.colorccc));
                        enablebutton();
                    }
                });
            }
        });

        fgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果有信息没填，就不让你进入下一部
                if(choicexiangmu.getVisibility()==View.GONE&timepicker.getVisibility()==View.GONE&nospinnertx.getText().length()>0&sexchoice.getVisibility()==View.GONE){
                    populay.setVisibility(View.GONE);
                    newlay.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getContext(),"请填写完相关信息",Toast.LENGTH_SHORT).show();
                }

            }
        });




    }


    private class myxiangmuclicklistener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            //这里会报错
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            //这个是小-项目用到的view
            final View myview2=inflater.inflate(R.layout.sos,null);
            XMDLG=new AlertDialog.Builder(getContext()).create();
            XMDLG.setView(myview2);
            XMDLG.show();
            ImageView icon1= (ImageView) myview2.findViewById(R.id.icon1);
            ImageView icon2= (ImageView) myview2.findViewById(R.id.icon2);
            ImageView icon3= (ImageView) myview2.findViewById(R.id.icon3);
            ImageView icon4= (ImageView) myview2.findViewById(R.id.icon4);
            ImageView icon5= (ImageView) myview2.findViewById(R.id.icon5);
            ImageView icon6= (ImageView) myview2.findViewById(R.id.icon6);
            ImageView icon7= (ImageView) myview2.findViewById(R.id.icon7);
            ImageView icon8= (ImageView) myview2.findViewById(R.id.icon8);
            ImageView icon9= (ImageView) myview2.findViewById(R.id.icon9);
            icon1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("图书馆",R.drawable.icon1);
                }
            });
            icon2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("下午茶",R.drawable.icon2);
                }
            });
            icon3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("电影院",R.drawable.icon3);
                }
            });
            icon4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("ktv",R.drawable.icon4);
                }
            });
            icon5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("篮球",R.drawable.icon5);
                }
            });
            icon6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("乒乓球",R.drawable.icon6);
                }
            });
            icon7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("约牌",R.drawable.icon7);
                }
            });
            icon8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("散步",R.drawable.icon8);
                }
            });
            icon9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallDialog("羽毛球",R.drawable.icon9);
                }
            });


        }



    }

    //让下一步按钮可用方法
    private void enablebutton(){
        if(choicexiangmu.getVisibility()==View.GONE&timepicker.getVisibility()==View.GONE&nospinnertx.getText().length()>0&sexchoice.getVisibility()==View.GONE){
            fgbutton.setEnabled(true);
            fgbutton.setTextColor(Color.BLACK);
        }
    }



    public void showDialog() {
        DateTimePickerDialog dialog = new DateTimePickerDialog(getContext(),
                System.currentTimeMillis());
        /**
         * 实现接口
         */
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener() {
            @Override
            public void OnDateTimeSet(android.support.v7.app.AlertDialog dialog, long date) {
                MyApplication.myappdate=date;
                timetxstring=getStringDate(date);
                timepicker.setVisibility(View.GONE);
                timetx.setVisibility(View.VISIBLE);
                timetx.setText(timetxstring);
                enablebutton();
            }

        });
        dialog.show();
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(Long date) {
        mydate=date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    private void MatchDialog(){
        final SharedPreferences.Editor editor=sp.edit();
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        final AlertDialog dialog=builder.create();
        final String[] arr={"项目优先","性别优先"};
        builder.setTitle("匹配优先度设定");
        int size=0;
        if(sp.getString("SecondChoice","").equals("项目")){
            size=0;
        }else{
            size=1;
        }
        builder.setSingleChoiceItems(arr,size, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putString("SecondChoice", arr[i].substring(0,2));
                editor.apply();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),"设定成功，将于下次匹配开始生效",Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(),""+sp.getString("SecondChoice",""),Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
            }
        });
        builder.create().show();


    }



    //设置匹配界面的背景图片
    private void matchimageview(ImageView imageView,String items){
        if(items.equals("图书馆")){
            imageView.setBackgroundResource(R.mipmap.tushuguan1);
        }else if(items.equals("电影院")) {
            imageView.setBackgroundResource(R.mipmap.dianyingyuan2);
        }else if(items.equals("KTV")){
            imageView.setBackgroundResource(R.mipmap.ktv1);
        }else if(items.equals("约牌")){
            imageView.setBackgroundResource(R.mipmap.yuepai2);
        }else if(items.equals("散步")){
            imageView.setBackgroundResource(R.mipmap.sanbu1);
        }else if(items.equals("羽毛球")){
            imageView.setBackgroundResource(R.mipmap.yumaoqiu1);
        }else if(items.equals("乒乓球")){
            imageView.setBackgroundResource(R.mipmap.pingpangqiu1);
        }else if(items.equals("篮球")){
            imageView.setBackgroundResource(R.mipmap.lanqiu1);
        }else if(items.equals("下午茶")){
            imageView.setBackgroundResource(R.mipmap.xiawucha1);
        }

    }
    //设置匹配信息界面的文字
    private void matchTEXT(TextView textView,String item,String space,TextView textView2,Long date){
        String itemspace=item+"-"+space;
        textView.setText(itemspace);
        textView2.setText(MyUtils.getStringDate2(date));
    }

    public class fragment1adapter extends BaseAdapter{
        List<LastInfo> list;
        LayoutInflater layoutInflater;
        Context context;

        public fragment1adapter(Context context,List<LastInfo> list) {
            this.context=context;
            this.list=list;
            this.layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
                return list.size();
        }

        public String user(int i){
            if(list.get(i).getAID().equals(user.getObjectId())){
                return "A";
            }else{
                return "B";
            }
        }

        public LastInfo getlist(int i){
            return list.get(i);
        }


        @Override
        public  Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

                ViewHolder holder=null;
                if(convertView==null){
                    convertView =layoutInflater.inflate(R.layout.zuizhongxuqiu,null);
                    holder=new ViewHolder();
                    holder.imageView= (ImageView) convertView.findViewById(R.id.imageview3);
                    holder.namespace= (TextView) convertView.findViewById(R.id.pipeixiangmu3);
                    holder.time= (TextView) convertView.findViewById(R.id.pipeishijian3);
                    holder.countdownView= (CountdownView) convertView.findViewById(R.id.counttime3);
                    holder.circleImageView= (CircleImageView) convertView.findViewById(R.id.pipeitou3);
                    holder.yinzhang= (ImageView) convertView.findViewById(R.id.yinzhang);
                    convertView.setTag(holder);
                }else
                {
                    holder= (ViewHolder) convertView.getTag();
                }
                //图片
                matchimageview(holder.imageView,list.get(i).getLastItem());
                //两个textview
                matchTEXT(holder.namespace,list.get(i).getLastItem(),list.get(i).getLastSpace(),holder.time,list.get(i).getLastTime());
                //时间
                Long date=list.get(i).getLastTime()-System.currentTimeMillis();
                if(date>0){
                    holder.countdownView.start(date); // 毫秒
                }else{
                    holder.countdownView.start(1);
                }
                //头像
                if(list.get(i).getAID().equals(user.getObjectId())){
                    new ImageLoader().showImageByThread(holder.circleImageView,list.get(i).getBavater());
                }else{
                    new ImageLoader().showImageByThread(holder.circleImageView,list.get(i).getAavater());
                }

                return convertView;
            }
        }
        public class ViewHolder
        {
            ImageView yinzhang;
            ImageView imageView;
            TextView namespace;
            TextView time;
            CountdownView countdownView;
            CircleImageView circleImageView;

        }


    //listview的展示方法
private void listviewclick(List<LastInfo> mylist){
    final fragment1adapter myadapter=new fragment1adapter(getContext(),mylist);
    listView.setAdapter(myadapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(MyApplication.isblack){
                Intent  intent=new Intent();
                intent.setClass(getContext(),matchinfo.class);
                intent.putExtra("data",myadapter.getlist(i));
                intent.putExtra("hr",myadapter.user(i));
                startActivity(intent);
            }else{
                Toast.makeText(getContext(),"匹配阶段无法进入其他匹配界面",Toast.LENGTH_SHORT).show();
            }

        }
    });

}

    //牌子上下移动的动画
    private void normalAnimantion(){
       mateAnimation.startTranslateAnimation();
       Billboard_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果已经开始即时匹配，提示一下是否覆盖匹配需求，并设置JX匹配为""，让对方无法跳转
                if(MyApplication.isJXpipei){
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("你已向小伙伴发送过匹配需求，是否重新开始随机匹配");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.create().cancel();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = BmobUser.getCurrentUser(User.class);
                            //如果被冻结了
                            if(MyApplication.dongjie){
                                Toast.makeText(getContext(),"您的账号已被冻结，请联系客服处理",Toast.LENGTH_SHORT).show();
                            }else{
                                //如果是第一次
                                if(sp.getBoolean("isfirstin",false)){
                                    //只需要验证下地理位置即可
                                    if(user.isConfirmState()){
                                        startdialog();
                                    }else{
                                        tocomfirm(getContext(),"鉴于您首次匹配，仅需验证校区地理位置即可匹配");
                                    }
                                }else
                                //不是首次匹配
                                {
                                    if(user.isConfirmState()&MyApplication.studentcomfirm){
                                        startdialog();
                                    }else if(user.isConfirmState()&!MyApplication.studentcomfirm&user.getRealavatar()
                                            .length()>0&user.getStudentid().length()>0&user.getStudentkey().length()>0){
                                        Toast.makeText(getContext(),"您提交过验证信息，请稍等客服处理,验证成功后可开始匹配",Toast.LENGTH_SHORT).show();
                                    } else{
                                        tocomfirm(getContext(),"您需要验证校区位置和学号后方可匹配");
                                    }
                                }
                            }


                        }
                    });
                    builder.create().show();
                }else {
                    User user = BmobUser.getCurrentUser(User.class);
                    //如果被冻结了
                    if(MyApplication.dongjie){
                        Toast.makeText(getContext(),"您的账号已被冻结，请联系客服处理",Toast.LENGTH_SHORT).show();
                    }else{
                        //如果是第一次
                        if(sp.getBoolean("isfirstin",false)){
                            //只需要验证下地理位置即可
                            if(user.isConfirmState()){
                                startdialog();
                            }else{
                                startdialog();
//                                tocomfirm(getContext(),"鉴于您首次匹配，仅需验证校区地理位置即可匹配");
                            }
                        }else
                        //不是首次匹配
                        {
                            if(user.isConfirmState()&MyApplication.studentcomfirm){
                                startdialog();
                            }else if(user.isConfirmState()&!MyApplication.studentcomfirm&user.getRealavatar()
                                    .length()>0&user.getStudentid().length()>0&user.getStudentkey().length()>0){
                                Toast.makeText(getContext(),"您提交过验证信息，请稍等客服处理,验证成功后可开始匹配",Toast.LENGTH_SHORT).show();
                            } else{
                                tocomfirm(getContext(),"您需要验证校区位置和学号后方可匹配");
                            }
                        }
                    }
                }
            }
        });
    }



    private void smallDialog(String name,int choiceicon){
        choicename=name;
        MyApplication.item=choicename;
        choiceimg.setImageResource(choiceicon);
        choicenam.setText(choicename);
        choicelay.setVisibility(View.VISIBLE);
        choicexiangmu.setVisibility(View.GONE);
        XMDLG.dismiss();
        enablebutton();
    }


    //发送广播，让主界面刷新fragment的方法
    private void  SendCancelBrocast(){
        Intent intent = new Intent("android.text_change");
        intent.putExtra("text", "刷新");
        getActivity().sendBroadcast(intent);
    }


    //查询listview并刷新他的方法-正在进行的项目
     private void querylistview(Boolean sds){
         if(sds){
         dianji.setImageResource(R.drawable.dianji_left363636);
             title.setText("正在进行");
             description.setText("去看看您和小伙伴的约定，不能违约哦！");
             CatBillboard_Relayout.setVisibility(View.GONE);
         }
         final Long date=System.currentTimeMillis()-1200000;
         final User user = BmobUser.getCurrentUser(User.class);
         BmobQuery<LastInfo> eq1 = new BmobQuery<LastInfo>();
         eq1.addWhereEqualTo("AID", user.getObjectId());
         eq1.addWhereNotEqualTo("isDELETE",true);
         eq1.addWhereGreaterThanOrEqualTo("LastTime",date);
         BmobQuery<LastInfo> eq2 = new BmobQuery<LastInfo>();
         eq2.addWhereEqualTo("BID", user.getObjectId());
         eq2.addWhereNotEqualTo("isDELETE",true);
         eq2.addWhereGreaterThanOrEqualTo("LastTime",date);
         List<BmobQuery<LastInfo>> queries = new ArrayList<BmobQuery<LastInfo>>();
         queries.add(eq1);
         queries.add(eq2);
         BmobQuery<LastInfo> mainQuery = new BmobQuery<LastInfo>();
         mainQuery.or(queries);
         mainQuery.findObjects(new FindListener<LastInfo>() {
             @Override
             public void done(List<LastInfo> list, BmobException e) {
                 if(e==null){
                     if(list.size()==0){
                         noinfotx.setVisibility(View.VISIBLE);
                         listView.setVisibility(View.GONE);
                     }else{
                         noinfotx.setVisibility(View.GONE);
                         listView.setVisibility(View.VISIBLE);
                         listView.setDivider(null);
                         listviewclick(list);
                     }
                 }else{
                     Log.d("xzf","查询失败"+e.getErrorCode()+e.getMessage());
                 }
             }
         });
     }

    //广播接收器
    public class MyBroadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle data = intent.getExtras();
            if(data.getString("te").equals("刷")){
                querylistview(true);
            }else if(data.getString("te").equals("新的匹配")){
                countdownView.start(300000);
            }

        }


    }

    //匹配方法
    private void pipei(){
        Billboard_img.setEnabled(false);
        cardDLG.dismiss();
        mateAnimation.closeTranslateAnimation();
        mateAnimation.startMateAnimantion(CatBillboard_Relayout);
        //2000-在动画之后开始匹配
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //上传数据，这里开始匹配
                MyUser myUser =new MyUser();
                myUser.setAreJianting(true);
                myUser.setOtherObjectId("");
                myUser.setWasMated("正在匹配");
                myUser.setEnsure(false);
                myUser.setOnback(false);
                myUser.setJXpipei("");
                myUser.setItem(choicename);
                myUser.setChoicesex(sex);
                myUser.setDate(mydate);
                myUser.setSpace(nospinnertx.getText().toString());
                myUser.setSecondDemond(sp.getString("SecondChoice","性别"));
                myUser.setCountMatePeriod(1);
                myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //这个有用，留着
                            MyApplication.OtherObjectId="";
                            //开启匹配任务的控制量
                            MyApplication.StartMatch=true;
                            //是否允许匹配成功之后的跳转
                            MyApplication.MatchSuccessed=true;
                            //切换黑色界面
                            MyApplication.isblack=false;
                            Toast.makeText(getContext(),"匹配成功，正在为您寻找小伙伴",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"匹配失败"+e.getErrorCode()+"--"+e.getMessage(),Toast.LENGTH_LONG).show();
                            Log.d("xzf",e.getErrorCode()+"  "+e.getMessage());
                            //恢复重置
                            MyUtils.rematch(sp.getString("userMateID", ""));
                            //切换fragment
                            SendCancelBrocast();
                        }
                    }
                });

                //先把所有数据保存到数据类MateContains中
                 mateContains.setMateItem(choicename);
                 mateContains.setMateTime(mydate);
                 mateContains.setMateregion(nospinnertx.getText().toString());
                 mateContains.setMatesex(sex);



                //正在修改的匹配方法：上传数据
                Mate mate=new Mate();
                mate.setMateItem(choicename);
                mate.setMateSex(sex);
                mate.setWhetherMatching(true);
                mate.setMateUserFlag(false);
                mate.setSynchronizedID("");
                mate.setMateUserActivity(new String[]{"","",""});
                mate.update(sp.getString("Mate", ""), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            mostcdTimer.start();
                        }
                    }
                });
//               AnimationUtil.UpdateBmobform("Mate",AnimationUtil.inserList("",true,"",choicename,sex,"","",""),sp.getString("Mate", ""));

            }
        },2500);
    }

    private void addoverlays(List<LatLng> pts){
        mbitmap = mMapView.getMap();
        //基础地图
        mbitmap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //50米
        MapStatusUpdate factory = MapStatusUpdateFactory.zoomTo(18.5f);
        mbitmap.setMapStatus(factory);
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(4, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
        //在地图上添加多边形Option，用于显示
        mbitmap.addOverlay(polygonOption);
        //回到我的位置
        LatLng latlng=pts.get(0);
        MapStatusUpdate msu= MapStatusUpdateFactory.newLatLng(latlng);
        mbitmap.animateMapStatus(msu);
    }

    private void tocomfirm(final Context context,String text){
            final User user = BmobUser.getCurrentUser(User.class);
            final AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage(text);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.create().cancel();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.create().cancel();
                    Intent intent=new Intent();
                    if(!user.isConfirmState()){
                        intent.setClass(context,comfirmstate.class);
                    }else{
                        intent.setClass(context,changehead.class);
                        intent.putExtra("changehead",false);
                    }
                    startActivity(intent);
                }
            });
            builder.create().show();
        }



    @Override
    public void onResume() {
        super.onResume();
        Log.d("xzf","在前台");
         query();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("xzf","暂停");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            Log.d("xzf","不可见");
        }else{
            Log.d("xzf","可见");
            query();
        }
    }

    //查询在线人数
    private void query(){
        final List<MyUser> myUserList=new ArrayList<>();
        BmobQuery<MyUser> query1=new BmobQuery<>();
        query1.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if(e==null){
                    for(MyUser cb:list){
                        if((System.currentTimeMillis()-cb.getMillis()<60000)){
                            myUserList.add(cb);
                        }
                    }
                    int size=myUserList.size()+100;
                    Nearbynumber_tex.setText("附近小伙伴:"+ size);
                }else{
                    Log.d("xzf","matefragment"+e.getErrorCode()+e.getMessage());
                }
            }
        });
    }

    private void MateMonitor(){
        final BmobRealTimeData rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
                BmobQuery<Mate> query=new BmobQuery<Mate>();
                query.getObject(sp.getString("Mate", ""), new QueryListener<Mate>() {
                    @Override
                    public void done(Mate user, BmobException e) {
                        if(e==null){
                            if(user.getMateUserFlag()&&null!=user.getMateUserActivity()){
                                Log.d("xzf","Mate查询方法");
                                String[] Info=new String[3];
                                System.arraycopy(user.getMateUserActivity(),0,Info,0,3);
                                Log.d("xzf","1:"+Info[0]+"  /2:"+Info[1]+"  /3："+Info[2]);
                                    Log.d("xzf","Mate会话跳转");
                                    //进行会话的跳转
                                    BmobIMUserInfo MateInfo = new BmobIMUserInfo( Info[0], Info[1],Info[2]);
                                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(MateInfo, false, null);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("c", c);
                                    startActivity(ChatActivity.class, bundle, false);
                                   //匹配成功对表的修改
                                Mate mate=new Mate();
                                mate.setWhetherMatching(false);
                                mate.setMateUserFlag(false);
                                mate.update(sp.getString("Mate", ""), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            CancelTimerTask();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }

            @Override
            public void onConnectCompleted(Exception ex) {
                if(rtd.isConnected()){
                    rtd.subRowUpdate("Mate",sp.getString("Mate", ""));
                    Log.d("xzf", "Mate监听连接成功:"+rtd.isConnected());
                }else{
                    Log.d("xzf", "Mate监听连接失败:"+ex.getMessage());
                }
            }
        });
    }

    private void CountTimeStart2(final String panduan, final CountDownTimer nowCDtimer) {
        BmobQuery<Mate> myquery=new BmobQuery<>();
        myquery.getObject(sp.getString("Mate", ""), new QueryListener<Mate>() {
            @Override
            public void done(final Mate smate, BmobException e) {
                if(e==null){
                    //如果同步锁尚未被占用,则努力查询合适对象，自己来占用同步锁
                    if(smate.getSynchronizedID().equals("")){
                        BmobQuery<Mate> query = new BmobQuery<>();
                        query.addWhereNotEqualTo("objectId", sp.getString("Mate", ""));
                        query.addWhereEqualTo("WhetherMatching", true);
                        query.setLimit(10);
                        if (panduan.contains("项目")) {
                            query.addWhereEqualTo("MateItem", MyApplication.item);
                        }
                        if (panduan.contains("性别")) {
                            query.addWhereEqualTo("sex", MyApplication.choicesex);
                        }
                        query.findObjects(new FindListener<Mate>() {
                            @Override
                            public void done(List<Mate> list, BmobException e) {
                                if (e == null) {
                                    for(final Mate bean:list){
                                        if(bean.getMateItem().equals(smate.getMateItem())&&
                                                bean.getMateSex().equals(smate.getSex())){
                                            //开始占用双方同步锁
                                            Mate mymate=new Mate();
                                            mymate.setSynchronizedID(sp.getString("Mate", ""));
                                            mymate.update(sp.getString("Mate", ""), new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Mate othermate=new Mate();
                                                        othermate.setSynchronizedID(sp.getString("Mate", ""));
                                                        othermate.update(bean.getObjectId(), new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if(e==null){
                                                                    //被自己占用同步锁的那个用户Mate表的ID，注意不是—User表的ID
                                                                    UseOtheUsersSynchronizedID=bean.getObjectId();
                                                                    LocalMateUserActivity=bean.getCertainMateUser();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                            }
                        });
                    }
                    //如果同步锁被自己占用了,则负责自己和对方的跳转业务
                    else if(smate.getSynchronizedID().equals(sp.getString("Mate", ""))){
                        //验证一下自己占用的对方同步锁是否安全
                        BmobQuery<Mate> query=new BmobQuery<Mate>();
                        query.getObject(UseOtheUsersSynchronizedID, new QueryListener<Mate>() {
                            @Override
                            public void done(final Mate mate, BmobException e) {
                                if(e==null){
                                    //如果没被改变，负责跳转逻辑
                                    if(mate.getSynchronizedID().equals(sp.getString("Mate", ""))){
                                        Mate mate1=new Mate();
                                        mate1.setMateUserFlag(true);
                                        mate1.setMateUserActivity(LocalMateUserActivity);
                                        mate1.update(sp.getString("Mate", ""), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    Mate mate1=new Mate();
                                                    mate1.setMateUserFlag(true);
                                                    mate1.setMateUserActivity(smate.getCertainMateUser());
                                                    mate1.update(UseOtheUsersSynchronizedID, new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e==null){
                                                                mostcdTimer.cancel();
                                                                betterthirdcdTimer.cancel();
                                                                betterthirdcdTimer.cancel();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                    //如果被改变了，清空自己占用的自己的同步锁，让匹配重新开始
                                    else{
                                        Mate mate1=new Mate();
                                        mate1.setMateUserFlag(false);
                                        mate1.setSynchronizedID("");
                                        mate1.update(sp.getString("Mate", ""), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {

                                            }
                                        });
                                    }
                                }
                            }
                        });

                    }
                    //如果同步锁被别人占用了，则放弃所有匹配任务
                    else{
                        mostcdTimer.cancel();
                        bettersecondcdTimer.cancel();
                        betterthirdcdTimer.cancel();
                    }
                }else{;
                    Log.i("xzf", e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void CountTimerFinish(CountDownTimer nowTimer, final int i) {
        nowTimer.start();
//        MyUser myUser = new MyUser();
//        myUser.setAreJianting(true);
//        myUser.setCountMatePeriod(i);
//        myUser.update(sp.getString("userMateID", ""), new UpdateListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null) {
//                    int period = i - 1;
//                    Log.d("xzf", "第" + period + "阶段匹配结束，没有合适对象，即将进入下一阶段匹配");
//                }
//            }
//        });

    }
    private void CancelTimerTask(){
        mostcdTimer.cancel();
        bettersecondcdTimer.cancel();
        betterthirdcdTimer.cancel();
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), target);
        if (bundle != null)
            intent.putExtra(getActivity().getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            getActivity().finish();
    }

    public void initInflate(View myview){
        CatBillboard_Relayout= (RelativeLayout) myview.findViewById(R.id.CatBillboard_Relayout);
        Billboard_img= (ImageView) myview.findViewById(R.id.Billboard_img);
        listView= (ListView) myview.findViewById(R.id.fragment1listview);
        noinfotx= (RelativeLayout) myview.findViewById(R.id.noinfotx);
        countdownView= (CountdownView) myview.findViewById(R.id.countdownView);
        MateStatus_Llayout= (LinearLayout) myview.findViewById(R.id.MateStatus_Llayout);
        dianji= (ImageView) myview.findViewById(R.id.RightHand_img);
        ICONset= (ImageView) myview.findViewById(R.id.set);
        Nearbynumber_tex= (TextView) myview.findViewById(R.id.Nearbynumber_tex);
        twoiconlay= (LinearLayout) myview.findViewById(R.id.twoiconlay);
        lunbo= (LinearLayout) myview.findViewById(R.id.lunbo);
        title= (TextView) myview.findViewById(R.id.title);
        description= (TextView) myview.findViewById(R.id.description);
        tc= (ImageView) myview.findViewById(R.id.tc);
        three= (ImageView) myview.findViewById(R.id.opendrawlayout_img);
        CatBillboard_Relayout= (RelativeLayout) myview.findViewById(R.id.CatBillboard_Relayout);
        mateContains=new MateContains();
    }
}

