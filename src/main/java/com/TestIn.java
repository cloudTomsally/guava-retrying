package com;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.github.rholder.retry.AttemptTimeLimiters;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;

public class TestIn {


private static Callable<Integer> updateReimAgentsCall = new Callable<Integer>() {
    @Override
    public Integer  call() throws Exception {
    	int i=0;
       try{
    	   System.out.println("1111");
    	   i=2;
    	   throw new Exception("��ѯ�쳣");
    	   
       }
       catch(Exception e ){
    	   System.out.println("2222");
    	   
       }
       // List<OAReimAgents> oaReimAgents = JSON.parseArray(result, OAReimAgents.class);
       /* if(CollectionUtils.isNotEmpty(oaReimAgents)){
            CacheUtil.put(Constants.REIM_AGENT_KEY,oaReimAgents);
            return true;
        }*/
        return i;
    }

};
public static void main(String[] args) {
	
	Retryer<Integer> retryer = RetryerBuilder
			.<Integer>newBuilder()
			//�׳�runtime�쳣��checked�쳣ʱ�������ԣ������׳�error�������ԡ� //����쳣������
			.retryIfException()
			.retryIfRuntimeException()// �����쳣����Դ
			//����falseҲ��Ҫ����
			//.retryIfResult(Predicates.equalTo(false))
			.retryIfResult(Predicates.equalTo(2))
			//�ص�����
			//.withWaitStrategy(WaitStrategies.fixedWait(10, TimeUnit.SECONDS))// ����ÿ�����Լ����5��
			.withWaitStrategy(WaitStrategies.incrementingWait(3, TimeUnit.SECONDS,1,TimeUnit.SECONDS))
			//.withAttemptTimeLimiter(AttemptTimeLimiters.<Integer>fixedTimeLimit(100, TimeUnit.SECONDS))
			.withRetryListener(new MyRetryListener())
			//���Դ���
			.withStopStrategy(StopStrategies.stopAfterAttempt(3))// ��������5�Σ�ͬ�������������Գ�ʱʱ��
			.build();
             Map map = new HashMap<>();
			try {
				int result = retryer.call(new Callable<Integer>() {  
			        @Override
			        public Integer call() throws Exception { 
			          try { 
			            //�ر�ע�⣺����false˵���������ԣ�����true˵����Ҫ�������� 
			            //return uploadToOdps(map); 
			        	  return 2; 
			          } catch (Exception e) { 
			            throw new Exception(e); 
			          } 
			        } 
			      }); 
			} catch (ExecutionException e) {
			    System.out.println("------");
			} catch (RetryException e) {
			  // logger.error("���¿ɴ��������쳣,��Ҫ���������ʼ�");
				System.out.println("--���¿ɴ��������쳣,��Ҫ���������ʼ�--");
			}
}

	
}
