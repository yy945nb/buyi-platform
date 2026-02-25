package com.buyi.common.mybatis.util;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.druid.DruidDataSourceCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @description: DynamicDataSource工具类
 */
@Component
@RequiredArgsConstructor
public class DynamicDataSourceUtil {

    private final DataSource dataSource;

    private final DruidDataSourceCreator dataSourceCreator;

    /**
     * 添加数据源
     */
    public void addDataSource(DataSourceProperty dataSourceProperty) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        if (!this.existsDataSource(dataSourceProperty.getPoolName())) {
            new JdbcTemplate(dataSource);
            DataSource dataSource1 = dataSourceCreator.createDataSource(dataSourceProperty);
            ds.addDataSource(dataSourceProperty.getPoolName(), dataSource1);
        }
    }

    /**
     * 删除数据源
     *
     * @param name
     */
    public void removeDataSource(String name) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(name);
    }


    /**
     * 是否存在数据源
     *
     * @param name
     * @return
     */
    public boolean existsDataSource(String name) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return ds.getDataSources().containsKey(name);
    }

}
