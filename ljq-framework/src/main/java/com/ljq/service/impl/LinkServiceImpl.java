package com.ljq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljq.constant.SystemConstant;
import com.ljq.domain.ResponseResult;
import com.ljq.domain.entity.Link;
import com.ljq.domain.vo.LinkVo;
import com.ljq.mapper.LinkMapper;
import com.ljq.service.LinkService;
import com.ljq.utils.BeanCopyUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-01-18 04:52:57
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstant.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        List<LinkVo> linkVos = BeanCopyUtil.copyList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }
}

