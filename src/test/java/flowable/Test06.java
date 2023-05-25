package flowable;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程变量
 * @author mly
 * @date 2023/2/16 9:52
 */
public class Test06 {
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
                .addClasspathResource("出差申请单.bpmn20.xml")
                .name("出差申请单")
                .deploy();  //部署流程

        System.out.println("deploy.getId() = " + deploy.getId());   //42501
        System.out.println("deploy.getName() = " + deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //在启动流程实例的时候创建了流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee0", "张三");
        variables.put("assignee1", "李四");
        variables.put("assignee2", "王五");
        variables.put("assignee3", "赵六");

        runtimeService.startProcessInstanceById("evection:1:57504", variables);
    }

    /**
     * 完成任务，同时指定流程变量
     */
    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("evection:1:57504")
                .taskAssignee("李四")
                .singleResult();

        //获取当前流程实例的所有变量
        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("num", 2);

        taskService.complete(task.getId(), processVariables);
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask1() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("evection:1:57504")
                .taskAssignee("李四")
                .singleResult();

        taskService.complete(task.getId());
    }

    /**
     * 根据task编号来修改任务的局部变量
     */
    @Test
    public void updateLocalVariable() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("evection:1:57504")
                .taskAssignee("李四")
                .singleResult();

        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("num", 5);
        //设置本地 局部变量
        taskService.setVariablesLocal(task.getId(), processVariables);
    }

    /**
     * 修改全局变量
     */
    @Test
    public void updateVariable() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("evection:1:57504")
                .taskAssignee("李四")
                .singleResult();
        //如果当前任务有局部变量，这里会拿到局部变量
        Map<String, Object> processVariables = task.getProcessVariables();
        processVariables.put("num", 1);
        //设置全局变量
        taskService.setVariables(task.getId(), processVariables);
    }
}
