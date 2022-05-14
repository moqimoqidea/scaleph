package cn.sliew.scalegh.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scalegh.dao.entity.master.security.Privilege;
import cn.sliew.scalegh.dao.mapper.master.security.PrivilegeMapper;
import cn.sliew.scalegh.service.admin.PrivilegeService;
import cn.sliew.scalegh.service.convert.admin.PrivilegeConvert;
import cn.sliew.scalegh.service.dto.admin.PrivilegeDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Override
    public List<PrivilegeDTO> listAll(String resourceType) {
        List<Privilege> list = this.privilegeMapper.selectList(new LambdaQueryWrapper<Privilege>()
                .eq(StrUtil.isNotEmpty(resourceType), Privilege::getResourceType, resourceType));
        return PrivilegeConvert.INSTANCE.toDto(list);
    }

}
