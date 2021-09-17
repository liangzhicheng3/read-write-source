package com.liangzhicheng.config.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @description MyBatis Plus配置
 */
@MapperScan(basePackages = {"com.liangzhicheng.**.dao*"}, sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
@Configuration
public class MyBatisPlusConfig {

    /**
     * 1.多数据源配置后，bean中存在3个数据源，需要为事务管理器和MyBatis指定一个明确数据源
     * 2.指定数据源后，需要查找数据源
     *   (1).定义枚举类DBTypeEnum
     *   (2).定义DBContextHolder，通过ThreadLocal将数据源设置到每个线程上下文
     *   (3).定义路由数据源MyRoutingDataSource，获取路由key
     *   (4).定义数据源切面DataSourceAop，设置路由key，默认情况下，所有的查询走从库，插入/修改/删除走主库，通过方法名来区分操作类型
     *   (5).定义注解@Master，在特殊情况下需要强制读主库，贴上该注解表示读主库
     *   (6).定义CRUD类测试
     */
    @Resource(name = "myRoutingDataSource")
    private DataSource myRoutingDataSource;

    /**
     * mapper的xml位置
     */
    public static final String[] DATASOURCE_MAPPER_LOACTIONS = {"classpath*:com/liangzhicheng/**/*Dao.xml"};

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(/*DataSource dataSource*/) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(myRoutingDataSource);
        //设置mapper的xml路径
        org.springframework.core.io.Resource[] resources = getMapperResource();
        if(resources != null){
            bean.setMapperLocations(resources);
        }
        //mybatis plus配置
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //锁机制
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //分页机制
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        Interceptor[] interceptors = {mybatisPlusInterceptor};
        bean.setPlugins(interceptors);
        GlobalConfig config = new GlobalConfig();
        //插入数据字段预处理
        config.setMetaObjectHandler(new MyBatisPlusObjectHandler());
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        //主键策略
        dbConfig.setIdType(IdType.AUTO);
//        dbConfig.setDbType(DbType.MYSQL);
        config.setDbConfig(dbConfig);
        bean.setGlobalConfig(config);
        //字段下划线映射bean以驼峰模式
        bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return bean.getObject();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(myRoutingDataSource);
    }

    /**
     * @description mapper的xml路径处理，把String[]转Resource[]
     * @return Resource[]
     */
    protected org.springframework.core.io.Resource[] getMapperResource(){
        try {
            if(DATASOURCE_MAPPER_LOACTIONS != null && DATASOURCE_MAPPER_LOACTIONS.length > 0) {
                List<org.springframework.core.io.Resource> resourceList = new ArrayList<>();
                for (String location : DATASOURCE_MAPPER_LOACTIONS) {
                    org.springframework.core.io.Resource[] resourceArr = new org.springframework.core.io.Resource[0];
                    resourceArr = new PathMatchingResourcePatternResolver().getResources(location);
                    for (org.springframework.core.io.Resource resource : resourceArr) {
                        resourceList.add(resource);
                    }
                }
                org.springframework.core.io.Resource[] resources = new org.springframework.core.io.Resource[resourceList.size()];
                for (int i = 0; i < resourceList.size(); i++) {
                    resources[i] = resourceList.get(i);
                }
                return resources;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
