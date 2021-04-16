package com.smk.admin.config;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.smk.admin.filter.KickoutSessionFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @Description: shiro配置
 * @author: liuhui
 * @version: 1.0
 * @since: JDK 1.8
 * @Date: 2020/4/10 11:43
 * Copyright (c) , .
 */
@Configuration
@Slf4j
public class ShiroConfig {

    /**
     * 不需要权限验证的资源表达式
     */
    private List<String> NO_NEED_PERMISSION_URL = Arrays.asList("/css/*", "/js/**", "/images/**", "/layui/**",
            "/treegrid/**", "/fragments/**", "/layout", "/home", "/sysUser/login");

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件过滤器
     * 1：配置shiro安全管理器接口securityManage;
     * 2：shiro 连接约束配置filterChainDefinitions;
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        log.info("Shiro拦截器工厂类注入开始");

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

        // 配置shiro安全管理器 SecurityManager
        bean.setSecurityManager(securityManager);

        //添加kickout认证
        HashMap<String, Filter> hashMap = new HashMap<>();
        hashMap.put("kickout", kickoutSessionFilter());
        bean.setFilters(hashMap);

        // 指定要求登录时的链接
        bean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        bean.setSuccessUrl("/home");
        // 未授权时跳转的界面;
        bean.setUnauthorizedUrl("/error");

        // filterChainDefinitions拦截器map必须用：LinkedHashMap，因为它必须保证有序
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 放行登录页面
        filterMap.put("/login", "anon");
        // 配置退出过滤器,具体的退出代码Shiro已经实现
        filterMap.put("/logout", "logout");

        //配置记住我或认证通过可以访问的地址
        filterMap.put("/sysUser/getUserList", "user");
        filterMap.put("/", "user");

        // 配置不会被拦截的链接 从上向下顺序判断
        for (String url : NO_NEED_PERMISSION_URL) {
            filterMap.put(url, "anon");
        }

        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】
        filterMap.put("/**", "kickout,authc");

        // 添加 shiro 过滤器
        bean.setFilterChainDefinitionMap(filterMap);

        log.info("Shiro拦截器工厂类注入成功");

        return bean;
    }

    /**
     * shiro安全管理器设置realm认证和ehcache缓存管理
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

        // 关联realm
        manager.setRealm(shiroRealm());
        //注入ehcache缓存管理器;
        manager.setCacheManager(cacheManager());
        //注入session管理器;
        manager.setSessionManager(sessionManager());
        //注入Cookie记住我管理器
        manager.setRememberMeManager(rememberMeManager());

        return manager;
    }

    /**
     * 创建身份认证 Realm
     * 这种方式跟类上加@Component注解一样，可以使ShiroRealm里@Autowired的属性注入成功，如果： manager.setRealm(new ShiroRealm())
     * 则不会注入成功
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm realm = new ShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        realm.setAuthenticationCacheName("authenticationCache");//默认是getClass().getName() + ".authenticationCache"
        realm.setAuthorizationCacheName("authorizationCache");
        return realm;
    }


    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);// 散列的次数，比如散列两次，相当于 // md5(md5(""));
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    /**
     * RememberMe 的功能
     */
    // 创建 Cookie
    @Bean
    public SimpleCookie remeberMeCookie() {
        log.info("记住我，设置cookie过期时间！");
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // //记住我cookie单位秒  [10天] ，2592000[30天]
        cookie.setMaxAge(864000);
        // 设置只读模型
        //cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * 配置cookie记住我管理器
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        //rememberme cookie加密的密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("u51QxotqpB1whLs0BT/8Zg=="));
        cookieRememberMeManager.setCookie(remeberMeCookie());
        return cookieRememberMeManager;
    }


    /**
     * 用户登录拦截配置
     *
     * @return
     */
    public KickoutSessionFilter kickoutSessionFilter() {
        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutSessionFilter.setCacheManager(cacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionFilter;
    }


    /**
     * ehcache缓存管理器
     *
     * @return
     */
//    @Bean
//    public EhCacheManager cacheManager() {
//        log.debug("shiro整合ehcache缓存：ShiroConfiguration.getEhCacheManager()");
//        EhCacheManager ehcache = new EhCacheManager();
//        CacheManager cacheManager = CacheManager.getCacheManager("shiro");
//        if (cacheManager == null) {
//            try {
//                cacheManager = CacheManager.create(ResourceUtils.getInputStreamForPath("classpath:ehcache.xml"));
//            } catch (CacheException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//        ehcache.setCacheManager(cacheManager);
//        return ehcache;
//    }
    @Bean
    public RedisShiroCacheManager cacheManager() {
        return new RedisShiroCacheManager();
    }

    /**
     * sessionManager添加session缓存操作DAO
     *
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(cacheManager());
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setSessionIdCookie(sessionIdCookie());
        //过期时间1小时，默认30min
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * /**
     * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     */
//    @Bean
//    public EnterpriseCacheSessionDAO sessionDAO() {
//        EnterpriseCacheSessionDAO enterCacheSessionDAO = new EnterpriseCacheSessionDAO();
//        enterCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
//        return enterCacheSessionDAO;
//    }
    @Bean
    public RedisSessionDao sessionDAO() {
        RedisSessionDao redisSessionDao = new RedisSessionDao();
        return redisSessionDao;
    }


    /**
     * 自定义cookie中session名称等配置
     *
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        //如果在Cookie中设置了"HttpOnly"属性，那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息，这样能有效的防止XSS攻击。
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("SHIROSESSIONID");
        return simpleCookie;
    }


    /**
     * 在html中使用 Shiro 标签
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证 配置以下两个bean
     * (DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor
     * (保证实现了Shiro内部lifecycle函数的bean执行) has run
     * <p>
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
