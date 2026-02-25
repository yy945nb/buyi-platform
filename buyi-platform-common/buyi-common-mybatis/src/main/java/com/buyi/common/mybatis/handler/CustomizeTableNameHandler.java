package com.buyi.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.buyi.common.api.openfign.tenant.ITenantInfoServiceFeign;
import com.buyi.common.api.vo.TenantInfoVo;
import com.buyi.common.core.result.CommonResult;
import com.buyi.common.core.constant.BaseConstant;
import com.buyi.common.mybatis.util.TableNameBuildUtil;
import com.buyi.common.mybatis.util.TenantInfoHelper;
import com.buyi.common.oauth.util.AccessTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * @description: 动态表名处理器
 */
@Component
@RequiredArgsConstructor
@Slf4j

public class CustomizeTableNameHandler implements TableNameHandler {

    private final ITenantInfoServiceFeign tenantInfoServiceFeign;

    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    public String dynamicTableName(String sql, String tableName) {
        if (tableName.startsWith("sys_")) {
            return tableName;
        }

        TenantInfoVo tenantInfoVo = TenantInfoHelper.get();
        String tenantId = AccessTokenUtils.getTenantId();
        if (tenantInfoVo == null && StrUtil.isNotBlank(tenantId)) {
            CommonResult<TenantInfoVo> byTenantIdFeign = tenantInfoServiceFeign.getByTenantIdFeign(tenantId);
            if (byTenantIdFeign.getCode() == 200) {
                TenantInfoVo data = byTenantIdFeign.getData();
                if (BaseConstant.DATASOURCE_TYPE_DB.equals(data.getDatasourceType())) {
                    return TableNameBuildUtil.build(tableName, tenantId, true);
                } else if(BaseConstant.DATASOURCE_TYPE_TABLE.equals(data.getDatasourceType())){
                    return TableNameBuildUtil.build(tableName, tenantId, false);
                }
            }
        } else if (tenantInfoVo != null && StrUtil.isNotBlank(tenantInfoVo.getTenantId())) {
            TenantInfoVo data = (TenantInfoVo) redisTemplate.opsForValue().get("my:tenant:" + tenantInfoVo.getTenantId());
            if(data == null){
                return TableNameBuildUtil.build(tableName, tenantInfoVo.getTenantId(), true);
            }
            if (BaseConstant.DATASOURCE_TYPE_DB.equals(data.getDatasourceType())) {
                return TableNameBuildUtil.build(tableName, tenantInfoVo.getTenantId(), true);
            } else if(BaseConstant.DATASOURCE_TYPE_TABLE.equals(data.getDatasourceType())){
                return TableNameBuildUtil.build(tableName, tenantInfoVo.getTenantId(), false);
            }
        }

        return tableName;
    }
}
