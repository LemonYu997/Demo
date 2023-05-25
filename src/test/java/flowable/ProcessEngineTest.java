package flowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.Test;

/**
 * @author mly
 * @date 2023/2/14 17:44
 */
public class ProcessEngineTest {
    @Test
    public void processEngine01() {
        //获取 ProcessEngineConfiguration 对象
        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        //配置相关数据库的连接信息
        configuration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        configuration.setJdbcUsername("root");
        configuration.setJdbcPassword("root@tomsung");
        //如果报错可以在路径后边加上 &nullCatalogMeansCurrent=true
        configuration.setJdbcUrl("jdbc:mysql://192.168.123.168:3306/flowable-demo?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai");
        //如果数据库中的表结构不存在，就新建
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        //获取ProcessEngine
        ProcessEngine processEngine = configuration.buildProcessEngine();
    }

    /**
     * 加载默认配置的流程引擎
     */
    @Test
    public void processEngine02() {
        //获取默认的ProcessEngine
        //配置信息在 resources/flowable.cfg.xml中定义
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        System.out.println("processEngine = " + processEngine);
    }

    /**
     * 加载定义名称的配置文件
     */
    @Test
    public void processEngine03() {
        //加载定义名称的配置文件
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable.cfg.xml");
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println("processEngine = " + processEngine);
    }

}
