package com.lurenjia.pets_adoption.utils;


import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * @Author lurenjia
 * @Date 2023/3/20-23:45
 * @Description 使用阿里云发送验证码
 */
@Slf4j
public class SendSmsUtils {
    /**
     *短信签名代码
     */
    public static final String SIGN_NAME = "路人甲的提示";

    /**
     * 短信模板代码
     */
    public static final String TEMPLATE_CODE = "SMS_274615496";

    /**
     * 阿里云账户Api密钥id
     */
    public static final String ACCESS_KEY_ID = "LTAI5tA89PDrYEbWckbu3HWR";

    /**
     * 阿里云账户Api密钥
     */
    public static final String ACCESS_KEY_SECRET = "8JRBV8AmaSosHhElOzFiRdZxWmQLrv";

    private SendSmsUtils(){}

    /**
     * 发送一个随机的验证码到指定手机号
     * @return
     */
    public static String sendCode(String code,String phone) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(ACCESS_KEY_ID)
                .accessKeySecret(ACCESS_KEY_SECRET)
                .build());

        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();

        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(SIGN_NAME)
                .templateCode(TEMPLATE_CODE)
                .phoneNumbers(phone)
                .templateParam("{\"code\":\"" + code + "\"}")
                .build();

        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = null;
        try {
            resp = response.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        //响应结果
        String message = resp.getBody().getMessage();
        log.info("响应数据为：{}",new Gson().toJson(resp));

        client.close();
        return message;
    }
}


