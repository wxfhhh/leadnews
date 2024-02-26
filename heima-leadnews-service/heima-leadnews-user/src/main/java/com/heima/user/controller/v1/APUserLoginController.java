package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dto.LoginDto;
import com.heima.user.service.APUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@Api(value = "用户注册",tags = "app端用户登录")
public class APUserLoginController {
    @Autowired
    public APUserService apUserService;
    @ApiOperation("用户注册")
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto dto){
//        apUserService
        return  apUserService.login(dto);
    }

}
