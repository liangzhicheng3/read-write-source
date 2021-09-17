package com.liangzhicheng.config.db;

import com.liangzhicheng.common.enums.DBTypeEnum;
import com.liangzhicheng.config.bean.MyRoutingDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 多数据源配置
 */
@Configuration
public class DBConfig {

    /**
     * @description 主机数据源
     * @return DataSource
     */
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * @description 从机数据源-1
     * @return DataSource
     */
    @Bean
    @ConfigurationProperties("spring.datasource.slave1")
    public DataSource slave1DataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * @description 从机数据源-2
     * @return DataSource
     */
    //...

    /**
     * @description 主数据源、从数据源、路由数据源配置
     *              主、从数据源生成路由数据源，只用到路由数据源
     * @param masterDataSource
     * @param slave1DataSource
     * @return DataSource
     */
    @Bean
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                          @Qualifier("slave1DataSource") DataSource slave1DataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE1, slave1DataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }

}
