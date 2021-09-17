package com.liangzhicheng.modules.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liangzhicheng.config.annotation.Master;
import com.liangzhicheng.modules.dao.IMemberDao;
import com.liangzhicheng.modules.entity.Member;
import com.liangzhicheng.modules.service.IMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberServiceImpl extends ServiceImpl<IMemberDao, Member> implements IMemberService {

    @Master
    @Override
    public String getToken(String appId) {
        //有些读操作必须读主数据库
        //比如获取微信access_token，因为高峰时期主从同步可能延迟
        //这种情况下就必须强制从主数据读
        return null;
    }

}
