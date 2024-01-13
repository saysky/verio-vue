package com.liuyanzhao.web.controller.system;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.liuyanzhao.common.constant.Constants;
import com.liuyanzhao.common.core.domain.AjaxResult;
import com.liuyanzhao.common.core.domain.entity.SysMenu;
import com.liuyanzhao.common.core.domain.entity.SysUser;
import com.liuyanzhao.common.core.domain.model.LoginBody;
import com.liuyanzhao.common.utils.SecurityUtils;
import com.liuyanzhao.framework.web.service.SysLoginService;
import com.liuyanzhao.framework.web.service.SysPermissionService;
import com.liuyanzhao.verio.service.ISysMenuService;

/**
 * 登录验证
 *
 * @author saysky
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }


    /**
     * 获取用户简单信息
     *
     * @return 用户信息
     */
    @GetMapping("getSimpleInfo")
    public AjaxResult getSimpleInfo() {
        AjaxResult ajax = AjaxResult.success();
        if (SecurityUtils.getAuthentication() instanceof AnonymousAuthenticationToken) {
            // 未登录
            return ajax;
        }
        SysUser user = SecurityUtils.getLoginUser().getUser();
        ajax.put("user", user);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
