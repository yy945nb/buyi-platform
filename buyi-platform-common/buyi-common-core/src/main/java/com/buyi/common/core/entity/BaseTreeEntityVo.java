package com.buyi.common.core.entity;

import lombok.Data;

import java.util.List;


@Data
public class BaseTreeEntityVo<T extends BaseTreeEntity> extends BaseEntity {
    List<T> children;
}
