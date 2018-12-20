LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_STATIC_JAVA_LIBRARIES := eventbus

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_SRC_FILES := \
    $(call all-java-files-under) \
    src/com/driverlayer/kdos_driverServer/IECarDriver.aidl \
    src/com/driverlayer/kdos_driverServer/BlueDriver.aidl

LOCAL_STATIC_JAVA_LIBRARIES += android-support-v4 eventbus jxl

LOCAL_PACKAGE_NAME := KdCarHome
LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := eventbus:libs/eventbus.jar jxl:libs/jxl.jar
include $(BUILD_MULTI_PREBUILT)
