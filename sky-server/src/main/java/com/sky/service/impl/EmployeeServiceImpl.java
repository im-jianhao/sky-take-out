package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     * @param employeeLoginDTO
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        // 对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置账号的状态，默认正常
        employee.setStatus(StatusConstant.ENABLE);

        // 设置密码，默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 设置当前记录的创建时间和修改时间
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());

        // 设置当前记录创建人以及修改人
        // employee.setCreateUser(currentId);
        // employee.setUpdateUser(currentId);

        // 调用持久层，插入数据
        employeeMapper.insert(employee);
    }

    /**
     * 实现员工的分页查询功能。
     * @param employeePageQueryDTO 包含分页信息和查询条件的DTO（数据传输对象）。
     * @return 返回一个PageResult对象，其中包含查询到的员工信息列表和总记录数。
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 使用PageHelper进行分页初始化，根据传入的页码和每页大小
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        // 调用mapper层执行分页查询
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        // 对查询结果进行封装，获取总记录数和查询结果列表
        long total = page.getTotal();
        List<Employee> result = page.getResult();

        // 返回封装好的分页查询结果
        return new PageResult(total, result);
    }

    /**
     * 根据提供的状态和员工ID，更新员工的状态。
     * @param status 员工的新状态，1 代表启用，0 代表停用。
     * @param id     员工的唯一标识符。
     */
    public void starOrStop(Integer status, Long id) {
        // 使用Builder模式构建Employee对象，以设置新的状态和ID
        Employee employee = Employee.builder().status(status).id(id).build();
        employeeMapper.update(employee);  // 调用mapper层执行更新操作
    }


    /**
     * 根据员工ID查询员工信息。
     * @param id 员工的唯一标识符。
     * @return 返回查询到的员工信息。
     */
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }

    /**
     * 根据提供的员工信息DTO，更新员工信息。
     * @param employeeDTO 包含员工信息的DTO（数据传输对象）。
     */
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        // 对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置当前记录的修改时间
        // employee.setUpdateTime(LocalDateTime.now());

        // 设置当前记录修改人
        // employee.setUpdateUser(currentId);

        // 调用持久层，更新数据
        employeeMapper.update(employee);
    }

}
