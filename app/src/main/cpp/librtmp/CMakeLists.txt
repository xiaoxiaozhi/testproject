cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -DNO_CRYPTO")#openssl起加密作用，普通直播不需要，如果删除引用，就会牵涉到改业务逻辑问题，所以在这里不让编译，给宏传参数
add_library(
        rtmp
        STATIC
        rtmp.c
        log.c
        amf.c
        hashswf.c
        parseurl.c
)


