package com;

import java.util.Collections;
import java.util.Random;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class SpringRT {
	public  static Boolean vpmsRetryCoupon(final String userId) {
        // ��������ģ��ʵ��
        RetryTemplate retryTemplate = new RetryTemplate();
        // �������Բ��ԣ���Ҫ�������Դ���
        SimpleRetryPolicy policy = new SimpleRetryPolicy(10, Collections.<Class<? extends Throwable>, Boolean> singletonMap(Exception.class, true));
        // �������Ի��˲������ԣ���Ҫ�������Լ��ʱ��
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(100);
        retryTemplate.setRetryPolicy(policy);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        // ͨ��RetryCallback ���Իص�ʵ����װ�����߼��߼�����һ��ִ�к�����ִ��ִ�еĶ�������߼�
        final RetryCallback<Object, Exception> retryCallback = new RetryCallback<Object, Exception>() {
            //RetryContext ���Բ���������Լ����ͳһspring-try��װ
            public Object doWithRetry(RetryContext context) throws Exception {
               boolean result = pushCouponByVpmsaa(userId);
               if(!result){
                   throw new RuntimeException();//������ر�ע�⣬���Եĸ�Դͨ��Exception����
               }
               return true;
            }
        };
        // ͨ��RecoveryCallback �������������������ߴﵽ�������޺���˳��ָ�����ʵ��
        final RecoveryCallback<Object> recoveryCallback = new RecoveryCallback<Object>() {
            public Object recover(RetryContext context) throws Exception {
//                logger.info("�������Է�ȯ::::::::::::"+userId);
                return null;
            }
        };
        try {
            // ��retryTemplate ִ��execute������ʼ�߼�ִ��
            retryTemplate.execute(retryCallback, recoveryCallback);
        } catch (Exception e) {
//            logger.info("��ȯ�����쳣========"+e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        vpmsRetryCoupon("43333");
    }


    public static Boolean pushCouponByVpmsaa(String userId){
        Random random = new Random();
        int a= random.nextInt(10);
        System.out.println("a��"+a);
        if(a==8){
            return  true;
        }else{
            return false;
        }
    }
}
