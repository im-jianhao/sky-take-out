package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据 dishId 查询setmealId
     * @param dishId
     * @return
     */
    @Select("select setmeal_id from setmeal_dish where dish_id = #{dishId}")
    Long getSetmealIdByDishId(Long dishId);
}
