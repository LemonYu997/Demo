package flowable;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 候选人
 * @author mly
 * @date 2023/2/16 9:52
 */
public class Test07 {
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
                .addClasspathResource("请假流程-候选人.bpmn20.xml")
                .name("请假流程-候选人")
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

        //给流程定义中的UEL表达式赋值
        Map<String, Object> variables = new HashMap<>();
        variables.put("candidate1", "张三");
        variables.put("candidate2", "李四");
        variables.put("candidate3", "王五");

        runtimeService.startProcessInstanceById("holiday-candidate:1:77504", variables);
    }

    /**
     * 根据登录用户查询对应的可以拾取的任务
     */
    @Test
    public void queryTaskCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidate:1:77504")
                .taskCandidateUser("张三")
                .list();

        for (Task task : list) {
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getName() = " + task.getName());
        }
    }

    /**
     * 拾取任务
     * 一个候选人拾取了这个任务之后其他的用户就没有办法拾取这个任务了
     * 所以如果一个用户拾取了这个任务但是又不想处理了，可以退还
     */
    @Test
    public void claimTaskCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidate:1:77504")
                .taskCandidateUser("张三")
                .singleResult();

        if (task != null) {
            //拾取对应的任务
//            taskService.claim(task.getId(), "张三");
            //先退还后，再变更候选人
            taskService.claim(task.getId(), "李四");
            System.out.println("任务拾取成功");
        }
    }

    /**
     * 退还任务
     * 一个候选人拾取了这个任务之后其他的用户就没有办法拾取这个任务了
     * 所以如果一个用户拾取了这个任务但是又不想处理了，可以退还
     */
    @Test
    public void unclaimTaskCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidate:1:77504")
                //此时张三已经拾取了任务，成为了负责人
                .taskAssignee("张三")
                .singleResult();

        if (task != null) {
            //归还对应的任务
            taskService.unclaim(task.getId());
            System.out.println("任务退还成功");
        }
    }

    /**
     * 交接任务
     * 如果一个人获取了任务，但是不想执行，可以将该任务交接给其他用户
     */
    @Test
    public void changeTaskCandidate() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidate:1:77504")
                //此时李四已经拾取了任务，成为了负责人
                .taskAssignee("李四")
                .singleResult();

        if (task != null) {
            //将任务交接给王五
            taskService.setAssignee(task.getId(), "王五");
            System.out.println("任务交接成功");
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidate:1:77504")
                .taskAssignee("王五")
                .singleResult();

        taskService.complete(task.getId());
        System.out.println("完成任务");
    }


}
