package com.blanink.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */
public class AfterSaleHistory {


    /**
     * errorCode : 00000
     * reason : 删除数据成功！
     * result : {"total":4,"rows":[{"id":"342c972ed59e488b81bc557a41fac844","isNewRecord":false,"remarks":"DDD","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:31:34","updateDate":"2017-07-07 11:31:34","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"2"},{"id":"0c089eea937745288c0cec042141a2eb","isNewRecord":false,"remarks":"灌灌灌灌","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:24:04","updateDate":"2017-07-07 11:24:04","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"1"},{"id":"e0866413c965471fbde3886b446b2099","isNewRecord":false,"remarks":"灌灌灌灌","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:22:44","updateDate":"2017-07-07 11:22:44","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"1"},{"id":"2d3386cb7b14429fbcc57284872a16a1","isNewRecord":false,"remarks":"灌灌灌灌灌","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:21:09","updateDate":"2017-07-07 11:21:09","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"2"}]}
     */

    private String errorCode;
    private String reason;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * total : 4
         * rows : [{"id":"342c972ed59e488b81bc557a41fac844","isNewRecord":false,"remarks":"DDD","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:31:34","updateDate":"2017-07-07 11:31:34","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"2"},{"id":"0c089eea937745288c0cec042141a2eb","isNewRecord":false,"remarks":"灌灌灌灌","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:24:04","updateDate":"2017-07-07 11:24:04","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"1"},{"id":"e0866413c965471fbde3886b446b2099","isNewRecord":false,"remarks":"灌灌灌灌","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:22:44","updateDate":"2017-07-07 11:22:44","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"1"},{"id":"2d3386cb7b14429fbcc57284872a16a1","isNewRecord":false,"remarks":"灌灌灌灌灌","createBy":{"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""},"createDate":"2017-07-07 11:21:09","updateDate":"2017-07-07 11:21:09","afterSales":{"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""},"company":{"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"},"isCalled":"1","solution":"2"}]
         */

        private int total;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * id : 342c972ed59e488b81bc557a41fac844
             * isNewRecord : false
             * remarks : DDD
             * createBy : {"id":"447e4526d22045f59ed58250046b3842","isNewRecord":false,"name":"李四","loginFlag":"1","roleList":[],"admin":false,"roleNames":""}
             * createDate : 2017-07-07 11:31:34
             * updateDate : 2017-07-07 11:31:34
             * afterSales : {"id":"0d60f70cadd14c46830a4702ce29af5e","isNewRecord":false,"imageList":[],"images":""}
             * company : {"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"name":"浙江总公司","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"}
             * isCalled : 1
             * solution : 2
             */

            private String id;
            private boolean isNewRecord;
            private String remarks;
            private CreateByBean createBy;
            private String createDate;
            private String updateDate;
            private AfterSalesBean afterSales;
            private CompanyBean company;
            private String isCalled;
            private String solution;

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

            public AfterSalesBean getAfterSales() {
                return afterSales;
            }

            public void setAfterSales(AfterSalesBean afterSales) {
                this.afterSales = afterSales;
            }

            public CompanyBean getCompany() {
                return company;
            }

            public void setCompany(CompanyBean company) {
                this.company = company;
            }

            public String getIsCalled() {
                return isCalled;
            }

            public void setIsCalled(String isCalled) {
                this.isCalled = isCalled;
            }

            public String getSolution() {
                return solution;
            }

            public void setSolution(String solution) {
                this.solution = solution;
            }

            public static class CreateByBean {
                /**
                 * id : 447e4526d22045f59ed58250046b3842
                 * isNewRecord : false
                 * name : 李四
                 * loginFlag : 1
                 * roleList : []
                 * admin : false
                 * roleNames :
                 */

                private String id;
                private boolean isNewRecord;
                private String name;
                private String loginFlag;
                private boolean admin;
                private String roleNames;
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public List<?> getRoleList() {
                    return roleList;
                }

                public void setRoleList(List<?> roleList) {
                    this.roleList = roleList;
                }
            }

            public static class AfterSalesBean {
                /**
                 * id : 0d60f70cadd14c46830a4702ce29af5e
                 * isNewRecord : false
                 * imageList : []
                 * images :
                 */

                private String id;
                private boolean isNewRecord;
                private String images;
                private List<?> imageList;

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

                public String getImages() {
                    return images;
                }

                public void setImages(String images) {
                    this.images = images;
                }

                public List<?> getImageList() {
                    return imageList;
                }

                public void setImageList(List<?> imageList) {
                    this.imageList = imageList;
                }
            }

            public static class CompanyBean {
                /**
                 * id : 7c10ee5fadd044e68f2b29f8a01d3f9a
                 * isNewRecord : false
                 * name : 浙江总公司
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
                 * photo : http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock
                 * roleList : []
                 * grossProfit : 0
                 * pType :
                 * places : 0
                 * parentId : 0
                 */

                private String id;
                private boolean isNewRecord;
                private String name;
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
}
