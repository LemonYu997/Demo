package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author mly
 * @date 2023/2/13 15:14
 */
public class SendRejectionMail implements JavaDelegate {
    /**
     * Flowable中的触发器
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        //触发执行的逻辑，按照在流程中的定义，应该给被拒绝的员工发送通知的邮件
        System.out.println("不好意思，你的请假申请被拒绝了！");
    }
}
