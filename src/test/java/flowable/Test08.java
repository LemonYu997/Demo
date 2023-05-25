package flowable;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户和用户组的维护
 * @author mly
 * @date 2023/2/16 9:52
 */
public class Test08 {
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
                .addClasspathResource("请假流程-候选人组.bpmn20.xml")
                .name("请假流程组-候选人")
                .deploy();  //部署流程

        System.out.println("deploy.getId() = " + deploy.getId());   //42501
        System.out.println("deploy.getName() = " + deploy.getName());
    }



    /**
     * 创建用户
     */
    @Test
    public void createUser() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //identityService完成用户和用户组的管理
        IdentityService identityService = processEngine.getIdentityService();
//        User user = identityService.newUser("李飞");
//        user.setFirstName("li");
//        user.setLastName("fei");
//        user.setEmail("lifei@qq.com");

//        User user = identityService.newUser("刘备");
//        user.setFirstName("liu");
//        user.setLastName("bei");
//        user.setEmail("liubei@qq.com");

        User user = identityService.newUser("关羽");
        user.setFirstName("guan");
        user.setLastName("yu");
        user.setEmail("guanyu@qq.com");

        identityService.saveUser(user);
    }

    /**
     * 创建用户组
     */
    @Test
    public void createGroup() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //创建Group对象并指定相关的信息
//        Group group = identityService.newGroup("group1");
//        group.setName("开发部");
//        group.setType("type1");

        Group group = identityService.newGroup("group2");
        group.setName("销售部");
        group.setType("type2");

        //创建Group对应的表结构数据
        identityService.saveGroup(group);
    }

    /**
     * 将用户分配给对应的Group
     */
    @Test
    public void userGroup() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //根据组的编号找到对应的group对象
        Group group1 = identityService.createGroupQuery().groupId("group1").singleResult();
        //查询用户
        List<User> list = identityService.createUserQuery().list();

        for (User user : list) {
            //将用户分配给对应的组
            identityService.createMembership(user.getId(), group1.getId());
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        Group group1 = identityService.createGroupQuery().groupId("group1").singleResult();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        //给流程定义中的UEL表达式赋值
        Map<String, Object> variables = new HashMap<>();
        //给流程定义中的UEL表达式赋值
        variables.put("g1", group1.getId());

        runtimeService.startProcessInstanceById("holiday-candidates:1:100004", variables);
    }

    /**
     * 根据登录用户查询自己可拾取的任务
     */
    @Test
    public void queryTaskCandidateGroup() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //根据当前登录的用户找到对应的组
        IdentityService identityService = processEngine.getIdentityService();
        Group group = identityService.createGroupQuery().groupMember("刘备").singleResult();
        System.out.println("group.getId() = " + group.getId());

        //获取分配给该组的任务
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidates:1:100004")
                .taskCandidateGroup(group.getId())
                .list();

        for (Task task : list) {
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getName() = " + task.getName());
        }
    }

    /**
     * 拾取任务
     */
    @Test
    public void claimTaskCandidate() {
        String userId = "刘备";

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //根据当前登录的用户找到对应的组
        IdentityService identityService = processEngine.getIdentityService();
        Group group = identityService.createGroupQuery().groupMember(userId).singleResult();

        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidates:1:100004")
                .taskCandidateGroup(group.getId())
                .singleResult();

        if (task != null) {
            //拾取对应的任务
            taskService.claim(task.getId(), userId);
            System.out.println("任务拾取成功");
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompleteTask() {
        String userId = "刘备";

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionId("holiday-candidates:1:100004")
                .taskAssignee(userId)
                .singleResult();

        taskService.complete(task.getId());
        System.out.println("完成任务");
    }
}
