package com.xpp.dao;

import com.xpp.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 标签Dao层接口
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * 根据标签名查询标签数据
     *
     * @param name 标签名
     * @return 标签数据
     */
    Tag findByname(String name);

    /**
     * 查询标签分页数据
     */
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
