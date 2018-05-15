package com.blanink.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1 0001.
 */
public class OrderProductFlow {

    /**
     * errorCode : 00000
     * reason : 保存数据成功！
     * result : [{"id":"73eef14c850541d48d8ec9af46fb1934","isNewRecord":false,"remarks":"ww","createBy":{"id":"4856035c527b419a9cf70d856e65c93b","isNewRecord":false,"loginFlag":"1","mobileMenuList":[],"roleList":[],"roleNames":"","admin":false},"createDate":"2017-08-08 18:22:29","updateDate":"2017-09-01 10:24:27","orderProduct":{"id":"0c282302f94f4a679dafbd5c32049738","isNewRecord":false,"orderProductSpecificationList":[],"htmlOrderProductSpecificationAttributes":"","imageList":[],"flowRemarkList":[],"processFeedbackList":[],"processWorkerList":[],"workPlanList":[],"urls":"","relFlowProcessList":[]},"company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"status":"2","isCommon":"0","name":"111","flowData":"{\"transitions\":{\"be091cd5cdbc4b778494d54c870e7add\":\"f396133570804cc89b95fda4346aee80\",\"f396133570804cc89b95fda4346aee80\":\"4c6bbb3dae2d4ef0a6aa5013136d49d9\",\"4c6bbb3dae2d4ef0a6aa5013136d49d9\":\"57d1d29392fc4b188cf497ba90b6b01d\",\"57d1d29392fc4b188cf497ba90b6b01d\":\"84fd0bf593f9402a8e1d96daf7b34816\",\"84fd0bf593f9402a8e1d96daf7b34816\":\"dce551fbde2a46f5881fbc689e275f9e\",\"dce551fbde2a46f5881fbc689e275f9e\":\"9804b0d9d66546b5892d431c1b017b6a\"},\"names\":{\"be091cd5cdbc4b778494d54c870e7add\":\"工序A(30)\",\"f396133570804cc89b95fda4346aee80\":\"工序二d(30)\",\"84fd0bf593f9402a8e1d96daf7b34816\":\"工序二e(30)\",\"57d1d29392fc4b188cf497ba90b6b01d\":\"工序c1(30)\",\"4c6bbb3dae2d4ef0a6aa5013136d49d9\":\"工序E(30)\",\"dce551fbde2a46f5881fbc689e275f9e\":\"发货(30)\",\"9804b0d9d66546b5892d431c1b017b6a\":\"测试工序(30)\"},\"positions\":{\"be091cd5cdbc4b778494d54c870e7add\":{\"top\":92,\"left\":30},\"f396133570804cc89b95fda4346aee80\":{\"top\":102,\"left\":348},\"84fd0bf593f9402a8e1d96daf7b34816\":{\"top\":192,\"left\":286},\"57d1d29392fc4b188cf497ba90b6b01d\":{\"top\":188,\"left\":509},\"4c6bbb3dae2d4ef0a6aa5013136d49d9\":{\"top\":116,\"left\":582},\"dce551fbde2a46f5881fbc689e275f9e\":{\"top\":180,\"left\":59},\"9804b0d9d66546b5892d431c1b017b6a\":{\"top\":269,\"left\":44}},\"container\":\"workflow-2\"}","internalDeliveryDate":"2017-08-09","processList":[],"orderProducts":[],"remaksList":[]}]
     */

    private String errorCode;
    private String reason;
    private List<ResultBean> result;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 73eef14c850541d48d8ec9af46fb1934
         * isNewRecord : false
         * remarks : ww
         * createBy : {"id":"4856035c527b419a9cf70d856e65c93b","isNewRecord":false,"loginFlag":"1","mobileMenuList":[],"roleList":[],"roleNames":"","admin":false}
         * createDate : 2017-08-08 18:22:29
         * updateDate : 2017-09-01 10:24:27
         * orderProduct : {"id":"0c282302f94f4a679dafbd5c32049738","isNewRecord":false,"orderProductSpecificationList":[],"htmlOrderProductSpecificationAttributes":"","imageList":[],"flowRemarkList":[],"processFeedbackList":[],"processWorkerList":[],"workPlanList":[],"urls":"","relFlowProcessList":[]}
         * company : {"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"}
         * status : 2
         * isCommon : 0
         * name : 111
         * flowData : {"transitions":{"be091cd5cdbc4b778494d54c870e7add":"f396133570804cc89b95fda4346aee80","f396133570804cc89b95fda4346aee80":"4c6bbb3dae2d4ef0a6aa5013136d49d9","4c6bbb3dae2d4ef0a6aa5013136d49d9":"57d1d29392fc4b188cf497ba90b6b01d","57d1d29392fc4b188cf497ba90b6b01d":"84fd0bf593f9402a8e1d96daf7b34816","84fd0bf593f9402a8e1d96daf7b34816":"dce551fbde2a46f5881fbc689e275f9e","dce551fbde2a46f5881fbc689e275f9e":"9804b0d9d66546b5892d431c1b017b6a"},"names":{"be091cd5cdbc4b778494d54c870e7add":"工序A(30)","f396133570804cc89b95fda4346aee80":"工序二d(30)","84fd0bf593f9402a8e1d96daf7b34816":"工序二e(30)","57d1d29392fc4b188cf497ba90b6b01d":"工序c1(30)","4c6bbb3dae2d4ef0a6aa5013136d49d9":"工序E(30)","dce551fbde2a46f5881fbc689e275f9e":"发货(30)","9804b0d9d66546b5892d431c1b017b6a":"测试工序(30)"},"positions":{"be091cd5cdbc4b778494d54c870e7add":{"top":92,"left":30},"f396133570804cc89b95fda4346aee80":{"top":102,"left":348},"84fd0bf593f9402a8e1d96daf7b34816":{"top":192,"left":286},"57d1d29392fc4b188cf497ba90b6b01d":{"top":188,"left":509},"4c6bbb3dae2d4ef0a6aa5013136d49d9":{"top":116,"left":582},"dce551fbde2a46f5881fbc689e275f9e":{"top":180,"left":59},"9804b0d9d66546b5892d431c1b017b6a":{"top":269,"left":44}},"container":"workflow-2"}
         * internalDeliveryDate : 2017-08-09
         * processList : []
         * orderProducts : []
         * remaksList : []
         */

        private String id;
        private boolean isNewRecord;
        private String remarks;
        private CreateByBean createBy;
        private String createDate;
        private String updateDate;
        private OrderProductBean orderProduct;
        private CompanyBean company;
        private String status;
        private String isCommon;
        private String name;
        private String flowData;
        private String internalDeliveryDate;
        private List<?> processList;
        private List<?> orderProducts;
        private List<?> remaksList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(boolean isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public CreateByBean getCreateBy() {
            return createBy;
        }

        public void setCreateBy(CreateByBean createBy) {
            this.createBy = createBy;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        public OrderProductBean getOrderProduct() {
            return orderProduct;
        }

        public void setOrderProduct(OrderProductBean orderProduct) {
            this.orderProduct = orderProduct;
        }

        public CompanyBean getCompany() {
            return company;
        }

        public void setCompany(CompanyBean company) {
            this.company = company;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsCommon() {
            return isCommon;
        }

        public void setIsCommon(String isCommon) {
            this.isCommon = isCommon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFlowData() {
            return flowData;
        }

        public void setFlowData(String flowData) {
            this.flowData = flowData;
        }

        public String getInternalDeliveryDate() {
            return internalDeliveryDate;
        }

        public void setInternalDeliveryDate(String internalDeliveryDate) {
            this.internalDeliveryDate = internalDeliveryDate;
        }

        public List<?> getProcessList() {
            return processList;
        }

        public void setProcessList(List<?> processList) {
            this.processList = processList;
        }

        public List<?> getOrderProducts() {
            return orderProducts;
        }

        public void setOrderProducts(List<?> orderProducts) {
            this.orderProducts = orderProducts;
        }

        public List<?> getRemaksList() {
            return remaksList;
        }

        public void setRemaksList(List<?> remaksList) {
            this.remaksList = remaksList;
        }

        public static class CreateByBean {
            /**
             * id : 4856035c527b419a9cf70d856e65c93b
             * isNewRecord : false
             * loginFlag : 1
             * mobileMenuList : []
             * roleList : []
             * roleNames :
             * admin : false
             */

            private String id;
            private boolean isNewRecord;
            private String loginFlag;
            private String roleNames;
            private boolean admin;
            private List<?> mobileMenuList;
            private List<?> roleList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getLoginFlag() {
                return loginFlag;
            }

            public void setLoginFlag(String loginFlag) {
                this.loginFlag = loginFlag;
            }

            public String getRoleNames() {
                return roleNames;
            }

            public void setRoleNames(String roleNames) {
                this.roleNames = roleNames;
            }

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }

            public List<?> getMobileMenuList() {
                return mobileMenuList;
            }

            public void setMobileMenuList(List<?> mobileMenuList) {
                this.mobileMenuList = mobileMenuList;
            }

            public List<?> getRoleList() {
                return roleList;
            }

            public void setRoleList(List<?> roleList) {
                this.roleList = roleList;
            }
        }

        public static class OrderProductBean {
            /**
             * id : 0c282302f94f4a679dafbd5c32049738
             * isNewRecord : false
             * orderProductSpecificationList : []
             * htmlOrderProductSpecificationAttributes :
             * imageList : []
             * flowRemarkList : []
             * processFeedbackList : []
             * processWorkerList : []
             * workPlanList : []
             * urls :
             * relFlowProcessList : []
             */

            private String id;
            private boolean isNewRecord;
            private String htmlOrderProductSpecificationAttributes;
            private String urls;
            private List<?> orderProductSpecificationList;
            private List<?> imageList;
            private List<?> flowRemarkList;
            private List<?> processFeedbackList;
            private List<?> processWorkerList;
            private List<?> workPlanList;
            private List<?> relFlowProcessList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getHtmlOrderProductSpecificationAttributes() {
                return htmlOrderProductSpecificationAttributes;
            }

            public void setHtmlOrderProductSpecificationAttributes(String htmlOrderProductSpecificationAttributes) {
                this.htmlOrderProductSpecificationAttributes = htmlOrderProductSpecificationAttributes;
            }

            public String getUrls() {
                return urls;
            }

            public void setUrls(String urls) {
                this.urls = urls;
            }

            public List<?> getOrderProductSpecificationList() {
                return orderProductSpecificationList;
            }

            public void setOrderProductSpecificationList(List<?> orderProductSpecificationList) {
                this.orderProductSpecificationList = orderProductSpecificationList;
            }

            public List<?> getImageList() {
                return imageList;
            }

            public void setImageList(List<?> imageList) {
                this.imageList = imageList;
            }

            public List<?> getFlowRemarkList() {
                return flowRemarkList;
            }

            public void setFlowRemarkList(List<?> flowRemarkList) {
                this.flowRemarkList = flowRemarkList;
            }

            public List<?> getProcessFeedbackList() {
                return processFeedbackList;
            }

            public void setProcessFeedbackList(List<?> processFeedbackList) {
                this.processFeedbackList = processFeedbackList;
            }

            public List<?> getProcessWorkerList() {
                return processWorkerList;
            }

            public void setProcessWorkerList(List<?> processWorkerList) {
                this.processWorkerList = processWorkerList;
            }

            public List<?> getWorkPlanList() {
                return workPlanList;
            }

            public void setWorkPlanList(List<?> workPlanList) {
                this.workPlanList = workPlanList;
            }

            public List<?> getRelFlowProcessList() {
                return relFlowProcessList;
            }

            public void setRelFlowProcessList(List<?> relFlowProcessList) {
                this.relFlowProcessList = relFlowProcessList;
            }
        }

        public static class CompanyBean {
            /**
             * id : f541afa391974409ba680e9bd9a394fd
             * isNewRecord : false
             * sort : 30
             * type : 1
             * customerServiceList : []
             * reviewOthers : 0
             * reviewSelf : 0
             * otherCosting : 0
             * otherPayment : 0
             * otherQuality : 0
             * otherService : 0
             * otherTime : 0
             * selfCosting : 0
             * selfPayment : 0
             * serviceCount : 0
             * photo :
             * roleList : []
             * grossProfit : 0
             * pType :
             * places : 0
             * parentId : 0
             */

            private String id;
            private boolean isNewRecord;
            private int sort;
            private String type;
            private int reviewOthers;
            private int reviewSelf;
            private int otherCosting;
            private int otherPayment;
            private int otherQuality;
            private int otherService;
            private int otherTime;
            private int selfCosting;
            private int selfPayment;
            private int serviceCount;
            private String photo;
            private int grossProfit;
            private String pType;
            private int places;
            private String parentId;
            private List<?> customerServiceList;
            private List<?> roleList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getReviewOthers() {
                return reviewOthers;
            }

            public void setReviewOthers(int reviewOthers) {
                this.reviewOthers = reviewOthers;
            }

            public int getReviewSelf() {
                return reviewSelf;
            }

            public void setReviewSelf(int reviewSelf) {
                this.reviewSelf = reviewSelf;
            }

            public int getOtherCosting() {
                return otherCosting;
            }

            public void setOtherCosting(int otherCosting) {
                this.otherCosting = otherCosting;
            }

            public int getOtherPayment() {
                return otherPayment;
            }

            public void setOtherPayment(int otherPayment) {
                this.otherPayment = otherPayment;
            }

            public int getOtherQuality() {
                return otherQuality;
            }

            public void setOtherQuality(int otherQuality) {
                this.otherQuality = otherQuality;
            }

            public int getOtherService() {
                return otherService;
            }

            public void setOtherService(int otherService) {
                this.otherService = otherService;
            }

            public int getOtherTime() {
                return otherTime;
            }

            public void setOtherTime(int otherTime) {
                this.otherTime = otherTime;
            }

            public int getSelfCosting() {
                return selfCosting;
            }

            public void setSelfCosting(int selfCosting) {
                this.selfCosting = selfCosting;
            }

            public int getSelfPayment() {
                return selfPayment;
            }

            public void setSelfPayment(int selfPayment) {
                this.selfPayment = selfPayment;
            }

            public int getServiceCount() {
                return serviceCount;
            }

            public void setServiceCount(int serviceCount) {
                this.serviceCount = serviceCount;
            }

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public int getGrossProfit() {
                return grossProfit;
            }

            public void setGrossProfit(int grossProfit) {
                this.grossProfit = grossProfit;
            }

            public String getPType() {
                return pType;
            }

            public void setPType(String pType) {
                this.pType = pType;
            }

            public int getPlaces() {
                return places;
            }

            public void setPlaces(int places) {
                this.places = places;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public List<?> getCustomerServiceList() {
                return customerServiceList;
            }

            public void setCustomerServiceList(List<?> customerServiceList) {
                this.customerServiceList = customerServiceList;
            }

            public List<?> getRoleList() {
                return roleList;
            }

            public void setRoleList(List<?> roleList) {
                this.roleList = roleList;
            }
        }
    }
}
