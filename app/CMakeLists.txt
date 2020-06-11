# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
include_directories(src/main/cpp/include)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")
add_subdirectory(src/main/cpp/librtmp)#引入librtmp下面的CMakeLists
add_library( # Sets the name of the library.
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp
        src/main/cpp/VideoChannel.cpp
        src/main/cpp/AudioChannel.cpp
        src/main/cpp/WangYiFFmpeg.cpp
        src/main/cpp/JavaCallHelper.cpp
        )

find_library( # Sets the name of the path variable.
        log-lib
        log)

target_link_libraries( # Specifies the target library.
        native-lib
        avfilter avformat avcodec avutil swresample swscale
        rtmp
        z
        android
        OpenSLES
        ${log-lib})