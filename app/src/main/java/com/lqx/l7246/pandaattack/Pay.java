package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lqx.l7246.pandaattack.util.OrderInfoUtil2_0;

import java.util.Map;

/**
 * Created by l7246 on 2017/12/2.
 */

public class Pay extends Activity {
    public int width,height;
    private ImageView pay_6,pay_18,pay_60;
    private ImageView pay_alipaylogo;

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017120100306238";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088821940080655";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMaCZgivZdO3yKCRGC8OO6Hkzhpsru2HsSUg8otRia7piupqCAWbXDzoHD2cwl2Itkaux/Zi2uT7WCYLoupOYfVXjfPtA/DMuBOagfzgNE2apubyLT2PTmolkL/FhDXskObxUKEQ4Relrxkq5geKkFxZ2XIlvIXmw/ujNyLMGfRTkc9Lh3mUU2E/nJHR/ZgFSpKqEGmhcb0/rZdt1dGFOabTMjrpk38gga8LhibbV4iKQcpskj/3u8CNHfhx/aYiipoTp9j3ttZRLkB20/MYZ1J/OAkGwzTB691j8ONV+Azt4s6YDvw3z9hD2msKVqdGi+NziqyONggK0oYjjSoDcJAgMBAAECggEAaZLOeH1ErenSxP/4mNqEgSfTa0C9Ckbgk2/8UgGJlMWfLIPB34CU+bxHOnqD1Fk0xrbXSRwNcSoiMvyEQhpfpmPgVQyoDSCTzIEL944ySwzX593M9DoIO3sLNcXyoAWfbCj+Sul0CJRV2ZsPXx6bCOoEiuWc2daV0n0xvcIFfLm1D7Tt3+MWElejYOLfCMvSX9VGk0KZLsa8HpBL7/BuIntedXLsLENUBaAns60e2Ek45vnMZGFI4Q/QQcqqb+eHvnjwgJEskkLzRW/DGZ/23ryEyrxRu0I817ZgRi6iM83cKWKaxxi8cBW1OAXyRoEB4awOBRQ/g4dve7j7vfQ+YQKBgQC+yX0SteR/niA0v5nToOTNNPVUpYP9knxxhvkUZ6nDUnA8FBmJZ5nfgcf5LEhv1IEvUs0OdQvYwcZDkZnJPMYxZCIsmoIw8un6HXV8ggyedJoJuGWH4I8X25VAvTEHsgxQTJw3MQ3o18ZoOZbRMj77+tqRlMS3VxgQljr6z4mLRQKBgQC8ZjivoiLgD1h4qEVyQBgkHdvD7JZSIl3j9Ue7wxxuFTbybDPFf4iF0Ax9UZzRKUKZuVj1XYl+9BsBKxL68QKcYSQAV0QfpK1gZwyU35TjMBzZ8OQE4JqipsO93TxyegIW5LldRPYfvl7PYW1FEDife9ZXo08XaegE8XK/104W9QKBgEHlfhm0UZPyndUaSJtb+ysVm5b9BDXVHr5njCiMFYqtMq7NknRvICqevN9HeE2H7IbX5pOTwd/ALiawZ5mmMB5nI5acV1No03/cJPQ4RvuD8qBg3FCuMz5eTZLqxcCOssoZa+t0OgWuYY0mNv1SiYanAk8+PyPEJT+1Y/G1NSCxAoGAGaUsiDPg3XEZUOtO8LtqzdfGp4fX+nw1HaewyQ1JHG8E2MED1xmCIvzsBk8KsqoUJ8rEIjpGUiK2XlVXsxMFkPXkKau3WTtYYmwEmWtJ4BcXhk81KrjMM+HReVHZPZgAGs+tr9WIT0/hpgcUEbegjY0U4z8WsBA5hhq7zQNo3NUCgYEAijiKWR6xEgX5DLN/OLwY5rWdWnB2PV5NYOGbgm6mDQw9YX+Ntt86aMrAhHLWWGa293TG1JKgz0hOMHdDxhgvMkHxCLYJeXrUX47ezmoMj8eYjfTuopX8Y/CYevMSbp5XyURuaZAePB4+C0tvg+24meEPg9B11h0yaDMJ6O9UqVo=";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private int chosenmoney=0;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.pay);

        pay_6 = (ImageView) findViewById(R.id.pay_6);
        pay_18 = (ImageView) findViewById(R.id.pay_18);
        pay_60 = (ImageView) findViewById(R.id.pay_60);
        pay_alipaylogo = (ImageView) findViewById(R.id.pay_alipaylogo);

        setMargins(pay_6, (int) (width * 0.067), (int) (height * 0.155), (int) (width * 0.7), (int) (height * 0.189));
        setMargins(pay_18, (int) (width * 0.367), (int) (height * 0.155), (int) (width * 0.4), (int) (height * 0.189));
        setMargins(pay_60, (int) (width * 0.667), (int) (height * 0.155), (int) (width * 0.1), (int) (height * 0.189));
        setMargins(pay_alipaylogo, (int) (width * 0.803), (int) (height * 0.876), (int) (width * 0.024), (int) (height * 0.035));

        pay_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Pay.this);  //先得到构造器
                builder.setTitle("熊猫博士提示您:请适度娱乐理性消费"); //设置标题
                builder.setMessage("您确定支付6元购买600个竹子？"); //设置内容
                builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                builder.setPositiveButton("买买买", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        chosenmoney = 600;
                        payV2(6, "6元购买600个竹子", "熊猫打天下游戏充值");
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }
        });
        pay_18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Pay.this);  //先得到构造器
                builder.setTitle("熊猫博士提示您:请适度娱乐理性消费"); //设置标题
                builder.setMessage("您确定支付18元购买2000个竹子？"); //设置内容
                builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                builder.setPositiveButton("买买买", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        chosenmoney = 2000;
                        payV2(18, "18元购买1800+200个竹子", "熊猫打天下游戏充值");
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }
        });
        pay_60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Pay.this);  //先得到构造器
                builder.setTitle("熊猫博士提示您:请适度娱乐理性消费"); //设置标题
                builder.setMessage("您确定支付60元购买7000个竹子？"); //设置内容
                builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                builder.setPositiveButton("买买买", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        chosenmoney = 7000;
                        payV2(60, "60元购买6000+1000个竹子", "熊猫打天下游戏充值");
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }
        });
        pay_alipaylogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Pay.this);  //先得到构造器
                builder.setTitle("熊猫博士提示您:请适度娱乐理性消费"); //设置标题
                builder.setMessage("您确定支付0.01元购买1个竹子？"); //设置内容
                builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                builder.setPositiveButton("买买买", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        chosenmoney = 1;
                        payV2(0.01, "0.01元购买1个竹子", "测试数据");
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }
        });
    }
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        STATIC.myplayer.money+=chosenmoney;
                        if(STATIC.UpdateUserData())
                        {
                            Toast.makeText(Pay.this, "成功充值"+chosenmoney+"个竹子", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Pay.this, "充值成功，请前往主页面手动存储", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(Pay.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(Pay.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(Pay.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    public void payV2(double amount,String subject,String body) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,amount,subject,body);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(Pay.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent=new Intent(Pay.this,MapChoosePOI.class);
        startActivity(intent);
    }
}
