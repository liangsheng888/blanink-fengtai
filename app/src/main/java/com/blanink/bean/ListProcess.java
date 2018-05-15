package com.blanink.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Created by liangshen on 2017/11/28 0028 16:48
 * 邮箱：liangshen6868@163.com
 */
public class ListProcess implements Serializable {

    /**
     * errorCode : 00000
     * reason : 获取数据成功！
     * result : [{"id":"be091cd5cdbc4b778494d54c870e7add","isNewRecord":false,"remarks":"沙敦府","createBy":{"id":"d6c8e9bc4c2b476ba84b962c27882f41","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2017-02-23 13:06:59","updateDate":"2017-06-03 13:56:43","parentIds":"0,","name":"工序A","sort":30,"type":"1","feedbackType":"1","company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"relPPList":[],"proList":[],"parentId":"0","positionNames":[]},{"id":"f396133570804cc89b95fda4346aee80","isNewRecord":false,"remarks":"","createBy":{"id":"d6c8e9bc4c2b476ba84b962c27882f41","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2017-02-23 13:09:17","updateDate":"2017-07-14 10:29:54","parentIds":"0,6d515db34d24477aafae95ca89511ccd,","name":"工序二d","sort":30,"type":"1","feedbackType":"1","company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"relPPList":[],"proList":[],"parentId":"6d515db34d24477aafae95ca89511ccd","positionNames":[]},{"id":"84fd0bf593f9402a8e1d96daf7b34816","isNewRecord":false,"remarks":"","createBy":{"id":"d6c8e9bc4c2b476ba84b962c27882f41","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2017-02-23 13:09:30","updateDate":"2017-03-31 11:28:22","parentIds":"0,6d515db34d24477aafae95ca89511ccd,","name":"工序二e","sort":60,"type":"1","feedbackType":"1","company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"relPPList":[],"proList":[],"parentId":"6d515db34d24477aafae95ca89511ccd","positionNames":[]},{"id":"4c6bbb3dae2d4ef0a6aa5013136d49d9","isNewRecord":false,"remarks":"","createBy":{"id":"d6c8e9bc4c2b476ba84b962c27882f41","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2017-04-07 17:43:18","updateDate":"2017-06-03 14:49:41","parentIds":"0,","name":"工序E","sort":30,"type":"1","feedbackType":"1","company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"relPPList":[],"proList":[],"parentId":"0","positionNames":[]},{"id":"dce551fbde2a46f5881fbc689e275f9e","isNewRecord":false,"remarks":"","createBy":{"id":"4856035c527b419a9cf70d856e65c93b","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2017-05-09 16:55:16","updateDate":"2017-05-09 16:55:16","parentIds":"0,","name":"发货","sort":30,"type":"3","feedbackType":"1","company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"relPPList":[],"proList":[],"parentId":"0","positionNames":[]},{"id":"9804b0d9d66546b5892d431c1b017b6a","isNewRecord":false,"remarks":"形成","createBy":{"id":"4856035c527b419a9cf70d856e65c93b","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2017-08-03 15:23:13","updateDate":"2017-08-03 15:23:58","parentIds":"0,","name":"测试工序","sort":30,"type":"1","feedbackType":"1","company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"relPPList":[],"proList":[],"parentId":"0","positionNames":[]},{"id":"28810d410a614cb9898b5878d26943ba","isNewRecord":false,"remarks":"","createBy":{"id":"4856035c527b419a9cf70d856e65c93b","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2017-11-21 21:08:14","updateDate":"2017-11-21 21:08:14","parentIds":"0,a3a9942bbd0845be8e18fc7fe2d88db7,57d1d29392fc4b188cf497ba90b6b01d,20c56b7da4e645ee92ba1e68d730914a,","name":"hh","sort":30,"type":"1","feedbackType":"1","company":{"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"relPPList":[],"proList":[],"parentId":"20c56b7da4e645ee92ba1e68d730914a","positionNames":[]}]
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

    public static class ResultBean implements Serializable {
        /**
         * id : be091cd5cdbc4b778494d54c870e7add
         * isNewRecord : false
         * remarks : 沙敦府
         * createBy : {"id":"d6c8e9bc4c2b476ba84b962c27882f41","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"}
         * createDate : 2017-02-23 13:06:59
         * updateDate : 2017-06-03 13:56:43
         * parentIds : 0,
         * name : 工序A
         * sort : 30
         * type : 1
         * feedbackType : 1
         * company : {"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"}
         * relPPList : []
         * proList : []
         * parentId : 0
         * positionNames : []
         */

        private String id;
        private boolean isNewRecord;
        private String remarks;
        private CreateByBean createBy;
        private String createDate;
        private String updateDate;
        private String parentIds;
        private String name;
        private int sort;
        private String type;
        private String feedbackType;
        private CompanyBean company;
        private String parentId;
        private List<?> relPPList;
        private List<?> proList;
        private List<?> positionNames;

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

        public String getParentIds() {
            return parentIds;
        }

        public void setParentIds(String parentIds) {
            this.parentIds = parentIds;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getFeedbackType() {
            return feedbackType;
        }

        public void setFeedbackType(String feedbackType) {
            this.feedbackType = feedbackType;
        }

        public CompanyBean getCompany() {
            return company;
        }

        public void setCompany(CompanyBean company) {
            this.company = company;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public List<?> getRelPPList() {
            return relPPList;
        }

        public void setRelPPList(List<?> relPPList) {
            this.relPPList = relPPList;
        }

        public List<?> getProList() {
            return proList;
        }

        public void setProList(List<?> proList) {
            this.proList = proList;
        }

        public List<?> getPositionNames() {
            return positionNames;
        }

        public void setPositionNames(List<?> positionNames) {
            this.positionNames = positionNames;
        }

        public static class CreateByBean implements Serializable {
            /**
             * id : d6c8e9bc4c2b476ba84b962c27882f41
             * isNewRecord : false
             * sort : 30
             * loginFlag : 1
             * mobileMenuList : []
             * roleList : []
             * admin : false
             * roleNames :
             * parentId : 0
             */

            private String id;
            private boolean isNewRecord;
            private int sort;
            private String loginFlag;
            private boolean admin;
            private String roleNames;
            private String parentId;
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

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getLoginFlag() {
                return loginFlag;
            }

            public void setLoginFlag(String loginFlag) {
                this.loginFlag = loginFlag;
            }

            public boolean isAdmin() {
                return admin;
            }

            public void setAdmin(boolean admin) {
                this.admin = admin;
            }

            public String getRoleNames() {
                return roleNames;
            }

            public void setRoleNames(String roleNames) {
                this.roleNames = roleNames;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
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
