LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := xvpn

LOCAL_SRC_FILES := PMSocket/PMSocketServer.cpp \
				   PMServer/PMerror.cpp \

LOCAL_CPP_INCLUDES += $(LOCAL_PATH)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/PMSocket
LOCAL_C_INCLUDES += $(LOCAL_PATH)/PMServer

LOCAL_LDLIBS += -llog

include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)

LOCAL_MODULE := vxposed

LOCAL_SRC_FILES := PMSocket/PMSocketClient.cpp \
                   PMClient/PMerror.cpp \

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/PMSocket
LOCAL_C_INCLUDES += $(LOCAL_PATH)/PMClient

LOCAL_LDFLAGS += -llog

include $(BUILD_SHARED_LIBRARY)




