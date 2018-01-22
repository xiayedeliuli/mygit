package cn.bmob.imdemo.YiYou.Mate;

import cn.bmob.imdemo.bean.Mate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.imdemo.YiYou.mylogin.welcome.sp;

/**
 * Created by Administrator on 2018/1/19.
 */

public class MateMethod {

    public static  void StartMate(MateContains mateContains){
        Mate mate=new Mate();
        mate.setMateItem(mateContains.getMateItem());
        mate.setMateSex(mateContains.getMatesex());
        mate.setWhetherMatching(true);
        mate.setMateUserFlag(false);
        mate.setSynchronizedID("");
        mate.setMateUserActivity(new String[]{"","",""});
        mate.update(sp.getString("Mate", ""), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){

                }
            }
        });

    };
}
