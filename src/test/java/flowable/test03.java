package flowable;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author mly
 * @date 2023/2/15 10:27
 */
public class test03 {

    /**
     * 部署流程操作，涉及三张表
     * 流程部署表：ACT_RE_DEPLOYMENT，一次流程部署操作就会生成一张表结构
     * 流程定义表：ACT_RE_PROCDEF：一次部署操作中包含几个流程定义文件就会产生几条记录
     * 流程定义资源文件表：ACT_GE_BYTEARRAY：有多少资源就会生成几条记录
     */
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
                .addClasspathResource("processes/Test001.bpmn20.xml")
                .name("测试流程001")
                .deploy();  //部署流程

        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
    }

    /**
     * 流程的挂起和激活
     * 部署流程的默认状态为激活，如果挂起，该流程定义不允许启动新的流程实例，同时该流程定义下的所有流程实例都将全部挂起暂停执行
     */
    @Test
    public void testSuspended() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //获取对应的流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("Test001:1:25004")
                .singleResult();
        //获取当前的流程定义的状态信息
        boolean suspended = processDefinition.isSuspended();
        if (suspended) {
            //当前的流程被挂起了
            //可以激活流程
            System.out.println("激活流程：" + processDefinition.getId() + ":" + processDefinition.getName());
            repositoryService.activateProcessDefinitionById("Test001:1:25004");
        } else {
            //当前的流程是激活状态
            //可以挂起流程
            System.out.println("挂起流程：" + processDefinition.getId() + ":" + processDefinition.getName());
            repositoryService.suspendProcessDefinitionById("Test001:1:25004");
        }
    }

    /**
     * 启动流程实例 涉及到4张表
     * ACT_RU_EXECUTION 运行时流程执行实例
     * ACT_RU_IDENTITYLINK 运行时用户关系信息
     * ACT_RU_TASK 运行时任务表
     * ACT_RU_VARIABLE 运行时变量表
     */
    @Test
    public void testTunProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过RuntimeService启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", "张三");
        variables.put("nrOfHolidays", 3);
        variables.put("description", "累了，请假");

        //启动流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceById("Test001:1:25004", "order100002", variables);
        System.out.println("instance.getProcessDefinitionId() = " + instance.getProcessDefinitionId());
        System.out.println("instance.getActivityId() = " + instance.getActivityId());
        System.out.println("instance.getId() = " + instance.getId());
    }

    /**
     * 处理流程
     */
    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //查到任务
        Task task = taskService.createTaskQuery()
                .processInstanceId("30001")     //按照流程实例编号，来查找对应的task
                .taskAssignee("user2")
                .singleResult();
        //获取当前流程实例绑定的流程变量
        Map<String, Object> processVariables = task.getProcessVariables();
        Set<String> strings = processVariables.keySet();
        for (String key : strings) {
            System.out.println(key + ":" + processVariables.get(key));
        }


        //修改流程变量
//        Map<String, Object> map = new HashMap<>();
        processVariables.put("approved", true);
        processVariables.put("description", "我要出去");

        //完成任务
        taskService.complete(task.getId(), processVariables);
    }

    /**
     * 结束流程
     */
    @Test
    public void testCompleteTask2() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //查到任务
        Task task = taskService.createTaskQuery()
                .processInstanceId("30001")     //按照流程实例编号，来查找对应的task
                .taskAssignee("user1")
                .singleResult();

        //完成任务
        taskService.complete(task.getId());
    }
}
