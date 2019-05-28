package com;

import java.util.concurrent.ExecutionException;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;

public class MyRetryListener implements RetryListener {

	@Override
    public <Boolean> void onRetry(Attempt<Boolean> attempt) {

        // �ڼ�������,(ע��:��һ��������ʵ�ǵ�һ�ε���)
        System.out.print("[retry]time=" + attempt.getAttemptNumber());

        // �����һ�����Ե��ӳ�
        System.out.print(",delay=" + attempt.getDelaySinceFirstAttempt());

        // ���Խ��: ���쳣��ֹ, ������������
        System.out.print(",hasException=" + attempt.hasException());
        System.out.print(",hasResult=" + attempt.hasResult());

        // ��ʲôԭ�����쳣
        if (attempt.hasException()) {
            System.out.print(",causeBy=" + attempt.getExceptionCause().toString());
        } else {
            // ��������ʱ�Ľ��
            System.out.print(",result=" + attempt.getResult());
        }

        // bad practice: �����˶�����쳣�������
        try {
            Boolean result = attempt.get();
            System.out.print(",rude get=" + result);
        } catch (ExecutionException e) {
            System.err.println("this attempt produce exception." + e.getCause().toString());
        }

        System.out.println();
    }

}
