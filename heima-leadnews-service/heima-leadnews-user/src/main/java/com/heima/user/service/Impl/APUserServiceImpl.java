package com.heima.user.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dto.LoginDto;
import com.heima.model.user.pojo.ApUser;
import com.heima.user.mapper.APUserMapper;
import com.heima.user.service.APUserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Slf4j
@Transactional
//事务
public class APUserServiceImpl extends ServiceImpl<APUserMapper, ApUser> implements APUserService {
    @Override
    public ResponseResult login(LoginDto dto) {
        if(!StringUtils.isBlank(dto.getPhone()) && !StringUtils.isBlank(dto.getPassword())){
            // 1.获取该用户数据
            ApUser apUser = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
            if (apUser==null){
                //为空返回
                return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST,"用户信息不存在");
            }
            //2.将用户密码进行比对
            String password = dto.getPassword();
            String salt = apUser.getSalt();
            String hex = DigestUtils.md5Hex((password + salt).getBytes());
            if (!hex.equals(apUser.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            //3.返回jwt和用户数据
            String token = AppJwtUtil.getToken(apUser.getId().longValue());

            HashMap<String, Object> map = new HashMap<>();
            map.put("token",token);
            apUser.setSalt("");
            apUser.setPassword("");
            map.put("user",apUser);
            return ResponseResult.okResult(map);
        }else {
            //游客登录
            HashMap<String, Object> map = new HashMap<>();
            //返回默认的token
            map.put("token",AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);

        }
    }
}
