package com.liuyanzhao.framework.web.service;

import com.liuyanzhao.common.core.domain.entity.SysRole;
import com.liuyanzhao.framework.manager.AsyncManager;
import com.liuyanzhao.framework.manager.factory.AsyncFactory;
import com.liuyanzhao.verio.domain.SysUserRole;
import com.liuyanzhao.verio.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.liuyanzhao.common.constant.CacheConstants;
import com.liuyanzhao.common.constant.Constants;
import com.liuyanzhao.common.constant.UserConstants;
import com.liuyanzhao.common.core.domain.entity.SysUser;
import com.liuyanzhao.common.core.domain.model.RegisterBody;
import com.liuyanzhao.common.core.redis.RedisCache;
import com.liuyanzhao.common.exception.user.CaptchaException;
import com.liuyanzhao.common.exception.user.CaptchaExpireException;
import com.liuyanzhao.common.utils.MessageUtils;
import com.liuyanzhao.common.utils.SecurityUtils;
import com.liuyanzhao.common.utils.StringUtils;
import com.liuyanzhao.verio.service.ISysConfigService;
import com.liuyanzhao.verio.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 注册校验方法
 *
 * @author saysky
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterBody registerBody) {
        String msg = "",
                username = registerBody.getUsername(),
                password = registerBody.getPassword(),
                email = registerBody.getEmail(),
                phonenumber = registerBody.getPhonenumber(),
                nickname = registerBody.getNickName();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(nickname);
        sysUser.setEmail(email);
        sysUser.setPhonenumber(phonenumber);

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(sysUser)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);

            // 添加角色
            insertUserRole(sysUser.getUserId(), registerBody.getRole());
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    private void insertUserRole(Long userId, String roleKey) {
        if (userId == null || StringUtils.isNull(roleKey)) {
            return;
        }
        if (!roleKey.equals("owner") && !roleKey.equals("customer")) {
            return;
        }
        // 此处可优化，节约时间，先这样
        List<SysRole> sysRoles = sysRoleService.selectRoleAll();
        Long roleId = null;
        for (SysRole sysRole : sysRoles) {
            if (roleKey.equals(sysRole.getRoleKey())) {
                roleId = sysRole.getRoleId();
            }
        }
        if (roleId == null) {
            return;
        }
        sysRoleService.insertAuthUser(userId, roleId);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }

}
