package tp.skt.onem2m.binder.mqtt_v1_1;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * definitions
 * <p>
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class Definitions {
    /**
     * @param resource
     * @return
     */
    public static String getResourceName(@ResourceType int resource) {
        final String[] RESOURCE = {"",
                "accessControlPolicy", "AE", "container", "contentInstance", "CSEBase", "delivery", "eventConfig", "execInstance", "group", "locationPolicy", "m2mServiceSubscriptionProfile", "mgmtCmd", "mgmtObj", "node", "pollingChannel", "remoteCSE", "request", "schedule", "serviceSubscribedNode", "statsCollect", "statsConfig", "subscription", "accessControlPolicyAnnc", "AEAnnc", "containerAnnc", "contentInstanceAnnc", "groupAnnc", "locationPolicyAnnc", "mgmtObjAnnc", "nodeAnnc", "remoteCSEAnnc", "scheduleAnnc"};
        return RESOURCE[resource];
    }

    public static String getOperationString(@Operation int op) {
        final String[] OPERATION = {"", "Create", "Retrieve", "Update", "Delete", "Notify"};
        return OPERATION[op];
    }

    /**
     * resourceType (ty)
     */
    @IntDef({
            ResourceType.accessControlPolicy/* 1 */, ResourceType.AE/* 2 */, ResourceType.container/* 3 */, ResourceType.contentInstance/* 4 */, ResourceType.CSEBase/* 5 */, ResourceType.delivery/* 6 */, ResourceType.eventConfig/* 7 */, ResourceType.execInstance/* 8 */, ResourceType.group/* 9 */, ResourceType.locationPolicy/* 10 */,
            ResourceType.m2mServiceSubscriptionProfile/* 11 */, ResourceType.mgmtCmd/* 12 */, ResourceType.mgmtObj/* 13 */, ResourceType.node/* 14 */, ResourceType.pollingChannel/* 15 */, ResourceType.remoteCSE/* 16 */, ResourceType.request/* 17 */, ResourceType.schedule/* 18 */, ResourceType.serviceSubscribedNode/* 19 */, ResourceType.statsCollect/* 20 */, ResourceType.statsConfig/* 21 */,
            ResourceType.subscription/* 22 */, ResourceType.accessControlPolicyAnnc/* 10001 */, ResourceType.AEAnnc/* 10002 */, ResourceType.containerAnnc/* 10003 */, ResourceType.contentInstanceAnnc/* 10004 */, ResourceType.groupAnnc/* 10009 */, ResourceType.locationPolicyAnnc/* 10010 */, ResourceType.mgmtObjAnnc/* 10013 */, ResourceType.nodeAnnc/* 10014 */, ResourceType.remoteCSEAnnc/* 10016 */, ResourceType.scheduleAnnc/* 10018 */,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResourceType {
        int accessControlPolicy = 1;
        int AE = 2;
        int container = 3;
        int contentInstance = 4;
        int CSEBase = 5;
        int delivery = 6;
        int eventConfig = 7;
        int execInstance = 8;
        int group = 9;
        int locationPolicy = 10;
        int m2mServiceSubscriptionProfile = 11;
        int mgmtCmd = 12;
        int mgmtObj = 13;
        int node = 14;
        int pollingChannel = 15;
        int remoteCSE = 16;
        int request = 17;
        int schedule = 18;
        int serviceSubscribedNode = 19;
        int statsCollect = 20;
        int statsConfig = 21;
        int subscription = 22;
        int accessControlPolicyAnnc = 10001;
        int AEAnnc = 10002;
        int containerAnnc = 10003;
        int contentInstanceAnnc = 10004;
        int groupAnnc = 10009;
        int locationPolicyAnnc = 10010;
        int mgmtObjAnnc = 10013;
        int nodeAnnc = 10014;
        int remoteCSEAnnc = 10016;
        int scheduleAnnc = 10018;
    }

    /**
     * Operation (op)
     */
    @IntDef({
            Operation.Create/* 1 */, Operation.Retrieve/* 2 */, Operation.Update/* 3 */, Operation.Delete/* 4 */, Operation.Notify/* 5 */,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Operation {
        int Create = 1;
        int Retrieve = 2;
        int Update = 3;
        int Delete = 4;
        int Notify = 5;
    }


    /**
     * Response Status Definitions (rsc)
     */
    @IntDef({
            ResponseStatusCode.ACCEPTED/* 1000 */, ResponseStatusCode.OK/* 2000 */, ResponseStatusCode.CREATED/* 2001 */, ResponseStatusCode.DELETED/* 2002 */, ResponseStatusCode.CHANGED/* 2004 */, ResponseStatusCode.BAD_REQUEST/* 4000 */,
            ResponseStatusCode.NOT_FOUND/* 4004 */, ResponseStatusCode.OPERATION_NOT_ALLOWED/* 4005 */, ResponseStatusCode.REQUEST_TIMEOUT/* 4008 */, ResponseStatusCode.SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE/* 4101 */,
            ResponseStatusCode.CONTENTS_UNACCEPTABLE/* 4102 */, ResponseStatusCode.ACCESS_DENIED/* 4103 */, ResponseStatusCode.GROUP_REQUEST_IDENTIFIER_EXISTS/* 4104 */, ResponseStatusCode.CONFLICT/* 4105 */,
            ResponseStatusCode.INTERNAL_SERVER_ERROR/* 5000 */, ResponseStatusCode.NOT_IMPLEMENTED/* 5001 */, ResponseStatusCode.TARGET_NOT_REACHABLE/* 5103 */, ResponseStatusCode.NO_PRIVILEGE/* 5105 */,
            ResponseStatusCode.ALREADY_EXISTS/* 5106 */, ResponseStatusCode.TARGET_NOT_SUBSCRIBABLE/* 5203 */, ResponseStatusCode.SUBSCRIPTION_VERIFICATION_INITIATION_FAILED/* 5204 */, ResponseStatusCode.SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE/* 5205 */,
            ResponseStatusCode.NON_BLOCKING_REQUEST_NOT_SUPPORTED/* 5206 */, ResponseStatusCode.EXTENAL_OBJECT_NOT_REACHABLE/* 6003 */, ResponseStatusCode.EXTENAL_OBJECT_NOT_FOUND/* 6005 */, ResponseStatusCode.MAX_NUMBERF_OF_MEMBER_EXCEEDED/* 6010 */,
            ResponseStatusCode.MEMBER_TYPE_INCONSISTENT/* 6011 */, ResponseStatusCode.MGMT_SESSION_CANNOT_BE_ESTABLISHED/* 6020 */, ResponseStatusCode.MGMT_SESSION_ESTABLISHMENT_TIMEOUT/* 6021 */, ResponseStatusCode.INVALID_CMDTYPE/* 6022 */,
            ResponseStatusCode.INSUFFICIENT_ARGUMENTS/* 6023 */, ResponseStatusCode.MGMT_CONVERSION_ERROR/* 6024 */, ResponseStatusCode.MGMT_CANCELATION_FAILURE/* 6025 */, ResponseStatusCode.ALREADY_COMPLETE/* 6028 */, ResponseStatusCode.COMMAND_NOT_CANCELLABLE/* 6029 */,
            ResponseStatusCode.INTERNAL_SDK_ERROR/* 9999 */,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ResponseStatusCode {
        int ACCEPTED = 1000;
        int OK = 2000;
        int CREATED = 2001;
        int DELETED = 2002;
        int CHANGED = 2004;
        int BAD_REQUEST = 4000;
        int NOT_FOUND = 4004;
        int OPERATION_NOT_ALLOWED = 4005;
        int REQUEST_TIMEOUT = 4008;
        int SUBSCRIPTION_CREATOR_HAS_NO_PRIVILEGE = 4101;
        int CONTENTS_UNACCEPTABLE = 4102;
        int ACCESS_DENIED = 4103;
        int GROUP_REQUEST_IDENTIFIER_EXISTS = 4104;
        int CONFLICT = 4105;
        int INTERNAL_SERVER_ERROR = 5000;
        int NOT_IMPLEMENTED = 5001;
        int TARGET_NOT_REACHABLE = 5103;
        int NO_PRIVILEGE = 5105;
        int ALREADY_EXISTS = 5106;
        int TARGET_NOT_SUBSCRIBABLE = 5203;
        int SUBSCRIPTION_VERIFICATION_INITIATION_FAILED = 5204;
        int SUBSCRIPTION_HOST_HAS_NO_PRIVILEGE = 5205;
        int NON_BLOCKING_REQUEST_NOT_SUPPORTED = 5206;
        int EXTENAL_OBJECT_NOT_REACHABLE = 6003;
        int EXTENAL_OBJECT_NOT_FOUND = 6005;
        int MAX_NUMBERF_OF_MEMBER_EXCEEDED = 6010;
        int MEMBER_TYPE_INCONSISTENT = 6011;
        int MGMT_SESSION_CANNOT_BE_ESTABLISHED = 6020;
        int MGMT_SESSION_ESTABLISHMENT_TIMEOUT = 6021;
        int INVALID_CMDTYPE = 6022;
        int INSUFFICIENT_ARGUMENTS = 6023;
        int MGMT_CONVERSION_ERROR = 6024;
        int MGMT_CANCELATION_FAILURE = 6025;
        int ALREADY_COMPLETE = 6028;
        int COMMAND_NOT_CANCELLABLE = 6029;
        int INTERNAL_SDK_ERROR = 9999;
    }
}
