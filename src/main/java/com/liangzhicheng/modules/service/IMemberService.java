package com.liangzhicheng.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liangzhicheng.modules.entity.Member;

import java.util.List;

public interface IMemberService extends IService<Member> {

    String getToken(String appId);

}
