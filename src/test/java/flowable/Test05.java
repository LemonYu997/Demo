package flowable;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听器
 * @author mly
 * @date 2023/2/15 16:19
 */
public class Test05 {
    //部署流程
    @Test
    public void testDeploy() {
        //获取默认的ProcessEngine
        //配置信息在 resources/flowable.cfg.xml中定义
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3、完成流程的部署操作
        Deployment deploy = repositoryService.createDeployment()
                //关联要部署的流程文件
                .addClasspathResource("Test003.bpmn20.xml")
                .name("测试流程003")
                .deploy();  //部署流程

        System.out.println("deploy.getId() = " + deploy.getId());   //42501
        System.out.println("deploy.getName() = " + deploy.getName());
    }

    /**
     * 启动流程实例
     * 给UEL表达式赋值
     */
    @Test
    public void testRunProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance instance = runtimeService.startProcessInstanceById("Test003:1:50004");
        System.out.println("instance = " + instance);
    }

    /**
     * 完成当前任务
     */
    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //查到任务
        Task task = taskService.createTaskQuery()
                .processInstanceId("52501")
                .taskAssignee("小明")
                .singleResult();

        //完成任务
        taskService.complete(task.getId());
    }

}
