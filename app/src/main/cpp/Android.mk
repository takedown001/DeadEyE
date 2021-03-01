LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := xcode

LOCAL_SRC_FILES := Socket/SocketServer.cpp \
				   Server/error.cpp \

LOCAL_CPP_INCLUDES += $(LOCAL_PATH)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/Socket
LOCAL_C_INCLUDES += $(LOCAL_PATH)/Server

LOCAL_LDLIBS += -llog

include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)

LOCAL_MODULE := error

LOCAL_SRC_FILES := Socket/SocketClient.cpp \
                   Client/error.cpp \

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/Socket
LOCAL_C_INCLUDES += $(LOCAL_PATH)/Client

LOCAL_LDFLAGS += -llog

include $(BUILD_SHARED_LIBRARY)




