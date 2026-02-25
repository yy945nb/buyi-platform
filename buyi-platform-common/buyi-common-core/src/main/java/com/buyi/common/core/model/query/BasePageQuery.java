package com.buyi.common.core.model.query;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class BasePageQuery {

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private long pageNum = 1;

    @Schema(description = "每页记录数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private long pageSize = 10;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "查询开始时间")
    private DateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "查询截止时间")
    private DateTime endDate;
}
