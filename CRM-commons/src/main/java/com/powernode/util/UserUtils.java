package com.powernode.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.powernode.exception.ResultException;
import com.powernode.myenum.ResultEnum;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Random;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/10
 */
public class UserUtils {
    public static String idVerify(String id,String name){
        String appCode="2278acbdd94a42d6b9e8622b42119f7d";
        String url="https://eid.shumaidata.com/eid/check";
        OkHttpClient client = new OkHttpClient.Builder().build();
        FormBody.Builder formbuilder = new FormBody.Builder();
        formbuilder.add("idcard", id);
        formbuilder.add("name", name);
        FormBody body = formbuilder.build();
        Request request = new Request.Builder().url(url).addHeader("Authorization", "APPCODE " + appCode).post(body).build();
        Response response = null;
        String result= null;
        try {
            response = client.newCall(request).execute();
            System.out.println("返回状态码" + response.code() + ",message:" + response.message());
            result= response.body().string();
            if (response.code()!=1){
                throw new ResultException(ResultEnum.ID_INCONSISTENT);
            }
        } catch (IOException e) {
            throw new ResultException(ResultEnum.IDVERIFY_ERROR);
        }
        return result;
    }

    public static String verifyCode(String phone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4GFDV4wQqVK9nUEjjNuA", "uLOVkjgNUqQkWjUBJziPCaf2JnnhEx");
        IAcsClient client = new DefaultAcsClient(profile);
        String scode = String.valueOf(new Random().nextInt(899999) + 100000);
//        int code=(int)(Math.random()*100000);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "AlanLin");
        request.putQueryParameter("TemplateCode", "SMS_201455240");
        request.putQueryParameter("TemplateParam", "{\"code\":"+scode+"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ClientException e) {
            throw new ResultException(ResultEnum.VERIFYCODE_ERROR);
        }
        return scode;
    }
}
