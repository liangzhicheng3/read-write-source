package com.liangzhicheng;

import com.liangzhicheng.modules.entity.Member;
import com.liangzhicheng.modules.service.IMemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RunTest {

    @Resource
    private IMemberService memberService;

    @Test
    public void testWrite() {
        Member member =
                new Member("1", "zhangsan");
        memberService.save(member);
    }

    @Test
    public void testRead() {
        for (int i = 0; i < 4; i++) {
            memberService.list();
        }
    }

    @Test
    public void testReadFromMaster() {
        memberService.getToken("1234");
    }

}
