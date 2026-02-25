package com.buyi.common.api.openfign.tenant;

import com.buyi.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.buyi.common.api.vo.TenantInfoVo;

@FeignClient(name = "buyi-tenant")
public interface ITenantInfoServiceFeign {

    /**
     * 根据租户id查询租户信息
     * @param tenantId
     * @return
     */
    @GetMapping("/tenant/getByTenantIdFeign")
    CommonResult<TenantInfoVo> getByTenantIdFeign(@RequestParam("tenantId") String tenantId);
}
