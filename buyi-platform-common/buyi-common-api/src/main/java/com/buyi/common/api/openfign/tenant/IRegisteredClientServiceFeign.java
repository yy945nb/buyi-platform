package com.buyi.common.api.openfign.tenant;

import com.buyi.common.core.result.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.buyi.common.api.dto.Oauth2RegisteredClientDto;

@FeignClient(name = "buyi-tenant")
public interface IRegisteredClientServiceFeign {

    /**
     * 添加数据源信息
     * @param clientId
     * @return
     */
    @GetMapping("/oauth2-registered-client/geyByClientIdFeign")
    CommonResult<Oauth2RegisteredClientDto> geyByClientIdFeign(@RequestParam("clientId") String clientId);

    /**
     * 添加数据源信息
     * @param id
     * @return
     */
    @GetMapping("/oauth2-registered-client/getByIdIdFeign")
    CommonResult<Oauth2RegisteredClientDto> getByIdFeign(@RequestParam("id") String id);
}
