package com.test.demo.config;
import com.test.demo.model.Group;
import com.test.demo.model.Role;
import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ShiroRealm  extends AuthorizingRealm{
    @Autowired
    UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("principal123:"+principalCollection.getPrimaryPrincipal().toString());
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles=new HashSet<>();
        Set<String> groups=new HashSet<>();
        user.getMembers().forEach(member -> {
            roles.add(member.getRole().getRoleName());
            groups.add("group:"+member.getGroup().getGroupName());
        });
        info.setRoles(roles);
        info.addStringPermissions(groups);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("authenticate");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user=userRepository.findByUsername(token.getUsername());
        if(user==null)
            throw new UnknownAccountException();
        if(user.getState()==0)
            throw new LockedAccountException();
        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", user);
        return authenticationInfo;
    }
}
