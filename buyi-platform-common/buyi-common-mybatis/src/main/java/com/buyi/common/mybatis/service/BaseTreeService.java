package com.buyi.common.mybatis.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.buyi.common.core.entity.BaseTreeEntity;
import com.buyi.common.mybatis.listen.QueryListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 树形数据操作
 */
public class BaseTreeService<D extends BaseMapper<T>, T extends BaseTreeEntity> {

    protected D baseMapper;

    protected T treeEntity;

    public BaseTreeService(D baseMapper) {
        this.baseMapper = baseMapper;
        this.treeEntity = null;
    }

    public BaseTreeService(D baseMapper, T treeEntity) {
        this.baseMapper = baseMapper;
        this.treeEntity = treeEntity;
    }


    private void saveTree(T entity) {
        baseMapper.insert(entity);
        //查询父级内容
        T parent = null;
        if (StrUtil.isNotEmpty(entity.getParentId())) {
            parent = baseMapper.selectById(entity.getParentId());
        }
        if (parent != null) {
            entity.setParentIds(parent.getParentIds() + "," + entity.getId());
            entity.setParentNames(parent.getParentNames() + "," + entity.getName());
            entity.setTreeLevel(parent.getTreeLevel() + 1);
            if ("1".equals(parent.getTreeLeaf())) {
                // 修改父级节点为非叶子节点
                parent.setTreeLeaf("0");
                baseMapper.updateById(parent);
            }
        } else {
            entity.setParentId("0");
            entity.setParentIds(entity.getId().toString());
            entity.setParentNames(entity.getName());
            entity.setTreeLevel(1);
        }
        baseMapper.updateById(entity);
    }

    public void updateTree(T entity) {
        //查询父级内容
        T parent = baseMapper.selectById(entity.getParentId());
        // 查询原来的内容
        T self = baseMapper.selectById(entity.getId());
        // 更新前的父级编号
        String oldParentId = self.getParentId();
        // 更新前的父级编号列表
        String oldParentIds = self.getParentIds();
        // 更新前的父级名称列表
        String oldParentNames = self.getParentNames();
        // 更新前的树层级
        Integer oldTreeLevel = self.getTreeLevel();
        // 更新当前节点信息
        if (parent != null) {
            entity.setParentId(entity.getParentId());
            entity.setParentIds(parent.getParentIds() + "," + entity.getId());
            entity.setTreeLevel(parent.getTreeLevel() + 1);
            entity.setParentNames(parent.getParentNames() + "," + entity.getName());
        } else {
            entity.setParentId("0");
            entity.setParentIds(entity.getId().toString());
            entity.setTreeLevel(1);
            entity.setParentNames(entity.getName());
        }
        baseMapper.updateById(entity);
        //
        if (!oldParentId.equals(entity.getParentId())) {
            // 父级发生改变时 1、更新当前数据，2、需要连带子节点一起更新 3、检查原父节点下是否还存在其他数据，不存在，则更新叶子节点字段为1  4、将新的父节点的叶子节点字段更新为0
            if (parent != null) {
                // 检查新的父级节点是否为叶子节点,如果是叶子节点，则更新为非叶子节点
                if (parent.getTreeLeaf().equals("1")) {
                    parent.setTreeLeaf("0");
                    baseMapper.updateById(parent);
                }
                // 检查原父级节点下是否还存在其他数据
                QueryWrapper<T> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("parent_id", oldParentId);
                Long count = baseMapper.selectCount(queryWrapper);
                if (count == 0) {
                    // 原父级节点下不存在其他数据，更新叶子节点字段为1
                    T oldParent = baseMapper.selectById(oldParentId);
                    oldParent.setTreeLeaf("1");
                    oldParent.setTreeLevel(1);
                    baseMapper.updateById(oldParent);
                }
                // 查询该节点下的所有子节点
                QueryWrapper<T> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.like("parent_ids", entity.getId() + ",");
                List<T> childList = baseMapper.selectList(queryWrapper1);
                childList.forEach(item -> {
                    item.setParentIds(item.getParentIds().replace(oldParentIds, entity.getParentIds()));
                    item.setParentNames(item.getParentNames().replace(oldParentNames, entity.getParentNames()));
                    item.setTreeLevel(item.getTreeLevel() - oldTreeLevel + entity.getTreeLevel());
                    baseMapper.updateById(item);
                });
            }
        }
    }

    /**
     * 删除树形数据
     *
     * @param id 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteTree(Long id) {
        // 查询当前节点
        T self = baseMapper.selectById(id);
        // 查询父级节点
        T parent = baseMapper.selectById(self.getParentId());
        // 查询子节点
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("parent_ids", id + ",");
        List<T> childList = baseMapper.selectList(queryWrapper);
        // 删除当前节点
        baseMapper.deleteById(id);
        // 更新父级节点
        if (parent != null) {
            // 检查父级节点下是否还存在其他数据
            QueryWrapper<T> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("parent_id", parent.getId() + ",");
            Long count = baseMapper.selectCount(queryWrapper1);
            if (count == 0) {
                // 父级节点下不存在其他数据，更新叶子节点字段为1
                parent.setTreeLeaf("1");
                baseMapper.updateById(parent);
            }
        }
        // 删除子节点
        childList.forEach(item -> {
            deleteTree(item.getId());
        });
    }

    public IPage<T> pageList(long current, long size, QueryListener<T> queryListener) {
        List<T> queryDataList = queryListener.queryList();
        // 构建所有的数据到最外层数据
        List<T> allDataList = generateData(queryDataList, queryListener);
        return generatePageData(allDataList, current, size);
    }

    public List<T> queryList(LambdaQueryWrapper<T> queryWrapper) {
        // 符合查询条件的数据
        List<T> queryDataList = baseMapper.selectList(queryWrapper);
        // 构建所有的数据到最外层数据
        return generateData(queryDataList, null);
    }

    public List<T> treeList(LambdaQueryWrapper<T> queryWrapper) {
        List<T> list = queryList(queryWrapper);
        return createTree(list, Long.valueOf(0));
    }

    public IPage<T> pageList(LambdaQueryWrapper<T> queryWrapper, long current, long size) {
        // 符合查询条件的数据
        List<T> queryDataList = baseMapper.selectList(queryWrapper);
        // 构建所有的数据到最外层数据
        List<T> allDataList = generateData(queryDataList, null);
        return generatePageData(allDataList, current, size);
    }

    private IPage<T> generatePageData(List<T> allDataList, long current, long size) {
        // 手动分页查询
        List<T> treeDataList = createTree(allDataList, Long.valueOf(0));
        int startIndex = (int) ((current - 1) * size);
        int endIndex = (int) (startIndex + size);
        List<T> result = null;
        if (endIndex > treeDataList.size()) {
            result = treeDataList;
        } else {
            result = treeDataList.subList(startIndex, endIndex);
        }
        IPage<T> page = new Page<>();
        page.setTotal(treeDataList.size());
        page.setSize(size);
        page.setCurrent(current);
        page.setRecords(result);
        return page;
    }

    public List<T> createTree(List<T> dataList, Long parentId) {
        List<T> list = new ArrayList<>();
        if (dataList == null || dataList.isEmpty()) {
            return list;
        }
        dataList.forEach(item -> {
            if (item.getParentId().equals(parentId)) {
                item.setChildren(createTree(dataList, item.getId()).size() > 0 ? createTree(dataList, item.getId()) : null);
                list.add(item);
            }
        });
        return list;
    }

    public List<T> generateData(List<T> original, QueryListener<T> queryListener) {
        List<T> copy = new ArrayList<>(original);
        List<Long> copyId = copy.stream().map(item -> item.getId()).collect(Collectors.toList());
        original.forEach(item -> {
            // 需要查询父级数据
            if (!Objects.equals(item.getParentId(), "0") && !copyId.contains(item.getParentId())) {
                T t = null;
                if (queryListener != null) {
                    t = queryListener.queryById(item.getParentId());
                } else {
                    t = this.baseMapper.selectById(item.getParentId());
                }
                if (t != null) {
                    copy.add(t);
                    copyId.add(t.getId());
                }
            }
        });
        if (copy.size() == original.size()) {
            return copy;
        } else {
            return generateData(copy, queryListener);
        }
    }

    /**
     * 保存或更新树形数据
     *
     * @param entity 实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateTree(T entity) {
        if (entity.getId() == null) {
            saveTree(entity);
        } else {
            T t = baseMapper.selectById(entity.getId());
            if (t == null) {
                saveTree(entity);
            } else {
                updateTree(entity);
            }
        }
    }
}
