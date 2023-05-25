package flowable;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mly
 * @date 2023/2/13 9:52
 */
@Slf4j
public class Test02 {

    //获取流程引擎对象
    @Test
    public void testProcessEngine() {
//        System.out.println("processEngine = " + processEngine);
    }

    ProcessEngineConfiguration configuration = null;

    @Before
    public void before() {
        //获取 ProcessEngineConfiguration 对象
        configuration = new StandaloneProcessEngineConfiguration();
        //配置相关数据库的连接信息
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root@tomsung");
        //如果报错可以在路径后边加上 &nullCatalogMeansCurrent=true
        configuration.setJdbcUrl("jdbc:mysql://192.168.123.168:3306/flowable-demo?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai");
        //如果数据库中的表结构不存在，就新建
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        //之后通过 ProcessEngineConfiguration 构建需要的processEngine对象
    }

    /**
     * 部署流程擦欧总
     * 1、RepositoryService 流程部署
     * 2、RuntimeService
     * 3、TaskService
     * 4、HistoryService
     */
    @Test
    public void testDeploy() {
        //1、获取ProcessEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        //2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3、完成流程的部署操作
        Deployment deploy = repositoryService.createDeployment()
                //关联要部署的流程文件
                .addClasspathResource("MyHolidayUI.bpmn20.xml")
                .name("FlowableUI请假流程")
                .deploy();  //部署流程

        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());
    }

    /**
     * 查询流程定义的信息
     */
    @Test
    public void testDeployQuery() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //查询对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //查询结果对象
        ProcessDefinition processDefinition = processDefinitionQuery.deploymentId("1").singleResult();

        System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
        System.out.println("processDefinition.getName() = " + processDefinition.getName());
        System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
        System.out.println("processDefinition.getId() = " + processDefinition.getId());
    }

    /**
     * 删除流程定义
     */
    @Test
    public void testDeleteDeploy() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //删除部署的流程
        //参数1：id  如果部署的流程启动了就不允许删除了
        //参数2：级联删除，如果流程启动了，相关的任务会一并删除掉
        repositoryService.deleteDeployment("2501", true);
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testRunProcess() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        //需要通过RuntimeService来启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //构建流程变量
        Map<String, Object> varivale = new HashMap<>();
        varivale.put("employee", "张三");
        varivale.put("nrOfHolidays", "3");
        varivale.put("description", "我要请假");

        //启动流程实例
        ProcessInstance holidayRequest = runtimeService.startProcessInstanceById("MyHolidayUI:1:17504", varivale);
        System.out.println("holidayRequest.getProcessDefinitionId() = " + holidayRequest.getProcessDefinitionId());
        System.out.println("holidayRequest.getActivityId() = " + holidayRequest.getActivityId());
        System.out.println("holidayRequest.getId() = " + holidayRequest.getId());
    }

    /**
     * 测试任务查询
     */
    @Test
    public void testQueryTask() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                //指定查询的流程名称
                .processDefinitionKey("MyHolidayUI")
                //查询任务的处理人
                .taskAssignee("user1").list();

        for (Task task : list) {
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getDescription() = " + task.getDescription());
            System.out.println("task.getId() = " + task.getId());
        }
    }

    /**
     * 完成当前任务
     */
    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //查到任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("MyHolidayUI")
                .taskAssignee("user1")
                .singleResult();

        //创建流程变量
        Map<String, Object> map = new HashMap<>();
        map.put("approved", false);

        //完成任务
        taskService.complete(task.getId(), map);
    }

    /**
     * 获取流程任务的历史数据
     */
    @Test
    public void testHistory() {
        ProcessEngine processEngine = configuration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        //查询历史数据
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processDefinitionId("MyHolidayUI:1:17504")
                //查询的历史记录的状态要已完成的
                .finished()
                //根据结束时间升序排序
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();

        for (HistoricActivityInstance history : list) {
            System.out.println("history.getActivityId() = " + history.getActivityId());
            System.out.println("history.getAssignee() = " + history.getAssignee());
            System.out.println("history.getActivityName() = " + history.getActivityName());

            //任务处理过程花费的时间
            System.out.println("history.getDurationInMillis() = " + history.getDurationInMillis() + "毫秒");

        }
    }
}
