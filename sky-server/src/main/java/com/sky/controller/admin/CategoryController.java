package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("update参数：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("page参数：{}", categoryPageQueryDTO);
        PageResult pageQuery = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageQuery);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("分类启用禁用")
    public Result starOrStop(@PathVariable Integer status, Long id) {
        log.info("分类启用禁用参数：{},{}", status, id);
        categoryService.starOrStop(status, id);
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类参数：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("根据ID删除分类")
    public Result deleteById(Long id) {
        log.info("删除分类参数：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("分类列表查询")
    public Result<List<Category>> list(Integer type) {
        return Result.success(categoryService.list(type));
    }
}
