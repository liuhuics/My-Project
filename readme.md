本版本为理财相关：
    schedule 为定时任务模块；
    business 为交易前端模块相关；
    ebank 为business模块请求银行相关；
    api 为business和ebank之前rpc通信用接口层；
    admin  为理财管理台。
    common 则是以上两者共同依赖的模块。
    plugin 为mybatis逆向工程专用自定义插件包，运行mybatis插件前，需要先install 该包。
