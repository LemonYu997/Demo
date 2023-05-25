package flowable;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 并行网关
 * @author mly
 * @date 2023/2/16 9:52
 */
public class Test10 {
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
                .addClasspathResource("请假流程-并行网关.bpmn20.xml")
                .name("请假流程-并行网关")
                .deploy();  //部署流程

        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //给流程定义中的UEL表达式赋值
        Map<String, Object> variables = new HashMap<>();
        variables.put("num", 3);

        runtimeService.startProcessInstanceById("holiday-parallel:1:122504", variables);
    }

    /**
     * 完成任务
     * 当执行了创建请假单后，到并行网关时就会产生两条并行的任务
     */
    @Test
    public void testCompleteTask() {
        String userId = "zhangsan";

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-parallel:1:122504")
                .taskAssignee(userId)
                .singleResult();

        taskService.complete(task.getId());
        System.out.println("完成任务");
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask1() {
        String userId = "wangwu";

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-parallel:1:122504")
                .taskAssignee(userId)
                .singleResult();

        taskService.complete(task.getId());
        System.out.println("完成任务");
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask2() {
        String userId = "lisi";

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-parallel:1:122504")
                .taskAssignee(userId)
                .singleResult();

        taskService.complete(task.getId());
        System.out.println("完成任务");
    }
}
