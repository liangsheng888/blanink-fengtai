package com.blanink.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Created by liangshen on 2018/1/12 0012 10:39
 * 邮箱：liangshen6868@163.com
 */
public class Dashboard implements Serializable {
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
         * total : 79
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

            private String id;
            private boolean isNewRecord;
            private String remarks;
            private CreateByBean createBy;
            private String createDate;
            private String updateDate;
            private OrderBean order;
            private CompanyCategoryBean companyCategory;
            private String price;
            private String amount;
            private String productDescription;
            private String deliveryTime;
            private String orderProductType;
            private String orderProductStatus;
            private String orderProductStatusDesc;
            private String productName;
            private String htmlOrderProductSpecificationAttributes;
            private String deliveryTimeString;
            private String companyAPriority;
            private String companyAPriorityDesc;
            private String companyBPriority;
            private String companyBPriorityDesc;
            private String innerDeliveryTime;
            private String innerDeliveryTimeString;
            private String productSn;
            private String source;
            private CompanyBeanX aCompany;
            private CompanyBeanX bCompany;
            private String flowFinish;
            private String urls;
            private int editNotDownOrderNum;
            private int purchaseInTheWayNum;
            private int shouldPurchanseNum;
            private int totalRequireNum;
            private String isAConfirm;
            private String confirmUserid;
            private String aConfirmTime;
            private String aProductName;
            private String aProductSn;
            private String aconfirmTime;
            private String closure;
            private String takeMaterielStatus;
            private List<OrderProductSpecificationListBean> orderProductSpecificationList;
            private List<?> imageList;
            private List<?> flowRemarkList;
            private List<?> processFeedbackList;
            private List<?> processWorkerList;
            private List<?> sendUserList;
            private List<?> workPlanList;
            private List<RelFlowProcessBean> relFlowProcessList;
            private List<?> parentOpList;

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

            public OrderBean getOrder() {
                return order;
            }

            public void setOrder(OrderBean order) {
                this.order = order;
            }

            public CompanyCategoryBean getCompanyCategory() {
                return companyCategory;
            }

            public void setCompanyCategory(CompanyCategoryBean companyCategory) {
                this.companyCategory = companyCategory;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getProductDescription() {
                return productDescription;
            }

            public void setProductDescription(String productDescription) {
                this.productDescription = productDescription;
            }

            public String getDeliveryTime() {
                return deliveryTime;
            }

            public void setDeliveryTime(String deliveryTime) {
                this.deliveryTime = deliveryTime;
            }

            public String getOrderProductType() {
                return orderProductType;
            }

            public void setOrderProductType(String orderProductType) {
                this.orderProductType = orderProductType;
            }

            public String getOrderProductStatus() {
                return orderProductStatus;
            }

            public void setOrderProductStatus(String orderProductStatus) {
                this.orderProductStatus = orderProductStatus;
            }

            public String getOrderProductStatusDesc() {
                return orderProductStatusDesc;
            }

            public void setOrderProductStatusDesc(String orderProductStatusDesc) {
                this.orderProductStatusDesc = orderProductStatusDesc;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getHtmlOrderProductSpecificationAttributes() {
                return htmlOrderProductSpecificationAttributes;
            }

            public void setHtmlOrderProductSpecificationAttributes(String htmlOrderProductSpecificationAttributes) {
                this.htmlOrderProductSpecificationAttributes = htmlOrderProductSpecificationAttributes;
            }

            public String getDeliveryTimeString() {
                return deliveryTimeString;
            }

            public void setDeliveryTimeString(String deliveryTimeString) {
                this.deliveryTimeString = deliveryTimeString;
            }

            public String getCompanyAPriority() {
                return companyAPriority;
            }

            public void setCompanyAPriority(String companyAPriority) {
                this.companyAPriority = companyAPriority;
            }

            public String getCompanyAPriorityDesc() {
                return companyAPriorityDesc;
            }

            public void setCompanyAPriorityDesc(String companyAPriorityDesc) {
                this.companyAPriorityDesc = companyAPriorityDesc;
            }

            public String getCompanyBPriority() {
                return companyBPriority;
            }

            public void setCompanyBPriority(String companyBPriority) {
                this.companyBPriority = companyBPriority;
            }

            public String getCompanyBPriorityDesc() {
                return companyBPriorityDesc;
            }

            public void setCompanyBPriorityDesc(String companyBPriorityDesc) {
                this.companyBPriorityDesc = companyBPriorityDesc;
            }

            public String getInnerDeliveryTime() {
                return innerDeliveryTime;
            }

            public void setInnerDeliveryTime(String innerDeliveryTime) {
                this.innerDeliveryTime = innerDeliveryTime;
            }

            public String getInnerDeliveryTimeString() {
                return innerDeliveryTimeString;
            }

            public void setInnerDeliveryTimeString(String innerDeliveryTimeString) {
                this.innerDeliveryTimeString = innerDeliveryTimeString;
            }

            public String getProductSn() {
                return productSn;
            }

            public void setProductSn(String productSn) {
                this.productSn = productSn;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public CompanyBeanX getACompany() {
                return aCompany;
            }

            public void setACompany(CompanyBeanX aCompany) {
                this.aCompany = aCompany;
            }

            public CompanyBeanX getBCompany() {
                return bCompany;
            }

            public void setBCompany(CompanyBeanX bCompany) {
                this.bCompany = bCompany;
            }

            public String getFlowFinish() {
                return flowFinish;
            }

            public void setFlowFinish(String flowFinish) {
                this.flowFinish = flowFinish;
            }

            public String getUrls() {
                return urls;
            }

            public void setUrls(String urls) {
                this.urls = urls;
            }

            public int getEditNotDownOrderNum() {
                return editNotDownOrderNum;
            }

            public void setEditNotDownOrderNum(int editNotDownOrderNum) {
                this.editNotDownOrderNum = editNotDownOrderNum;
            }

            public int getPurchaseInTheWayNum() {
                return purchaseInTheWayNum;
            }

            public void setPurchaseInTheWayNum(int purchaseInTheWayNum) {
                this.purchaseInTheWayNum = purchaseInTheWayNum;
            }

            public int getShouldPurchanseNum() {
                return shouldPurchanseNum;
            }

            public void setShouldPurchanseNum(int shouldPurchanseNum) {
                this.shouldPurchanseNum = shouldPurchanseNum;
            }

            public int getTotalRequireNum() {
                return totalRequireNum;
            }

            public void setTotalRequireNum(int totalRequireNum) {
                this.totalRequireNum = totalRequireNum;
            }

            public String getIsAConfirm() {
                return isAConfirm;
            }

            public void setIsAConfirm(String isAConfirm) {
                this.isAConfirm = isAConfirm;
            }

            public String getConfirmUserid() {
                return confirmUserid;
            }

            public void setConfirmUserid(String confirmUserid) {
                this.confirmUserid = confirmUserid;
            }

            public String getAConfirmTime() {
                return aConfirmTime;
            }

            public void setAConfirmTime(String aConfirmTime) {
                this.aConfirmTime = aConfirmTime;
            }

            public String getAProductName() {
                return aProductName;
            }

            public void setAProductName(String aProductName) {
                this.aProductName = aProductName;
            }

            public String getAProductSn() {
                return aProductSn;
            }

            public void setAProductSn(String aProductSn) {
                this.aProductSn = aProductSn;
            }

            public String getAconfirmTime() {
                return aconfirmTime;
            }

            public void setAconfirmTime(String aconfirmTime) {
                this.aconfirmTime = aconfirmTime;
            }

            public String getClosure() {
                return closure;
            }

            public void setClosure(String closure) {
                this.closure = closure;
            }

            public String getTakeMaterielStatus() {
                return takeMaterielStatus;
            }

            public void setTakeMaterielStatus(String takeMaterielStatus) {
                this.takeMaterielStatus = takeMaterielStatus;
            }

            public List<OrderProductSpecificationListBean> getOrderProductSpecificationList() {
                return orderProductSpecificationList;
            }

            public void setOrderProductSpecificationList(List<OrderProductSpecificationListBean> orderProductSpecificationList) {
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

            public List<?> getSendUserList() {
                return sendUserList;
            }

            public void setSendUserList(List<?> sendUserList) {
                this.sendUserList = sendUserList;
            }

            public List<?> getWorkPlanList() {
                return workPlanList;
            }

            public void setWorkPlanList(List<?> workPlanList) {
                this.workPlanList = workPlanList;
            }

            public List<RelFlowProcessBean> getRelFlowProcessList() {
                return relFlowProcessList;
            }

            public void setRelFlowProcessList(List<RelFlowProcessBean> relFlowProcessList) {
                this.relFlowProcessList = relFlowProcessList;
            }

            public List<?> getParentOpList() {
                return parentOpList;
            }

            public void setParentOpList(List<?> parentOpList) {
                this.parentOpList = parentOpList;
            }
            public static class RelFlowProcessBean{

                /**
                 * isNewRecord : true
                 * remarks :
                 * flow : {"id":"617eeb1835d348bfbd16db72cd2859f6","isNewRecord":false,"orderProduct":{"id":"a0075f8a7a464d21aae1da27aa98daf0","isNewRecord":false,"orderProductSpecificationList":[],"htmlOrderProductSpecificationAttributes":"","imageList":[],"flowRemarkList":[],"processFeedbackList":[],"processWorkerList":[],"sendUserList":[],"workPlanList":[],"urls":"","relFlowProcessList":[],"editNotDownOrderNum":0,"purchaseInTheWayNum":0,"shouldPurchanseNum":0,"totalRequireNum":0,"parentOpList":[]},"processList":[],"orderProducts":[],"remaksList":[]}
                 * process : {"id":"51cd7f8c5c774dd084bf3abc99371116","isNewRecord":false,"name":"剪料","sort":30,"type":"1","feedbackType":"1","relPPList":[],"isPublic":"1","proList":[],"parentId":"0","positionNames":[]}
                 * target : 200
                 * latestFeedback : {"id":"4c9776a4ae7b44ae83391ff4a6bf6ade","isNewRecord":false,"remarks":"","createBy":{"id":"3b62a05ae6c240ac8b7c1aa65c61f493","isNewRecord":false,"name":"小吴","sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"createDate":"2018-01-15 13:52:24","updateDate":"2018-01-15 13:52:24","flow":{"id":"617eeb1835d348bfbd16db72cd2859f6","isNewRecord":false,"processList":[],"orderProducts":[],"remaksList":[]},"process":{"id":"51cd7f8c5c774dd084bf3abc99371116","isNewRecord":false,"name":"剪料","sort":30,"relPPList":[],"proList":[],"parentId":"0","positionNames":[]},"isFinished":"1","achieveAmount":200,"resources":[],"feedbackUser":{"id":"3b62a05ae6c240ac8b7c1aa65c61f493","isNewRecord":false,"name":"小吴","sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"},"target":0,"feedbackAttachmentStr":"","urls":""}
                 * processPriority : 1
                 * status : 3
                 * totalCompletedQuantity : 200
                 * isPublic : 0
                 * confirmQuantity : 0
                 * processFeedbackList : []
                 */

                private boolean isNewRecord;
                private String remarks;
                private FlowBean flow;
                private ProcessBean process;
                private int target;
                private LatestFeedbackBean latestFeedback;
                private String processPriority;
                private String status;
                private int totalCompletedQuantity;
                private String isPublic;
                private int confirmQuantity;
                private List<?> processFeedbackList;

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

                public FlowBean getFlow() {
                    return flow;
                }

                public void setFlow(FlowBean flow) {
                    this.flow = flow;
                }

                public ProcessBean getProcess() {
                    return process;
                }

                public void setProcess(ProcessBean process) {
                    this.process = process;
                }

                public int getTarget() {
                    return target;
                }

                public void setTarget(int target) {
                    this.target = target;
                }

                public LatestFeedbackBean getLatestFeedback() {
                    return latestFeedback;
                }

                public void setLatestFeedback(LatestFeedbackBean latestFeedback) {
                    this.latestFeedback = latestFeedback;
                }

                public String getProcessPriority() {
                    return processPriority;
                }

                public void setProcessPriority(String processPriority) {
                    this.processPriority = processPriority;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public int getTotalCompletedQuantity() {
                    return totalCompletedQuantity;
                }

                public void setTotalCompletedQuantity(int totalCompletedQuantity) {
                    this.totalCompletedQuantity = totalCompletedQuantity;
                }

                public String getIsPublic() {
                    return isPublic;
                }

                public void setIsPublic(String isPublic) {
                    this.isPublic = isPublic;
                }

                public int getConfirmQuantity() {
                    return confirmQuantity;
                }

                public void setConfirmQuantity(int confirmQuantity) {
                    this.confirmQuantity = confirmQuantity;
                }

                public List<?> getProcessFeedbackList() {
                    return processFeedbackList;
                }

                public void setProcessFeedbackList(List<?> processFeedbackList) {
                    this.processFeedbackList = processFeedbackList;
                }

                public static class FlowBean {
                    /**
                     * id : 617eeb1835d348bfbd16db72cd2859f6
                     * isNewRecord : false
                     * orderProduct : {"id":"a0075f8a7a464d21aae1da27aa98daf0","isNewRecord":false,"orderProductSpecificationList":[],"htmlOrderProductSpecificationAttributes":"","imageList":[],"flowRemarkList":[],"processFeedbackList":[],"processWorkerList":[],"sendUserList":[],"workPlanList":[],"urls":"","relFlowProcessList":[],"editNotDownOrderNum":0,"purchaseInTheWayNum":0,"shouldPurchanseNum":0,"totalRequireNum":0,"parentOpList":[]}
                     * processList : []
                     * orderProducts : []
                     * remaksList : []
                     */

                    private String id;
                    private boolean isNewRecord;
                    private OrderProductBean orderProduct;
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

                    public OrderProductBean getOrderProduct() {
                        return orderProduct;
                    }

                    public void setOrderProduct(OrderProductBean orderProduct) {
                        this.orderProduct = orderProduct;
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

                    public static class OrderProductBean {
                        /**
                         * id : a0075f8a7a464d21aae1da27aa98daf0
                         * isNewRecord : false
                         * orderProductSpecificationList : []
                         * htmlOrderProductSpecificationAttributes :
                         * imageList : []
                         * flowRemarkList : []
                         * processFeedbackList : []
                         * processWorkerList : []
                         * sendUserList : []
                         * workPlanList : []
                         * urls :
                         * relFlowProcessList : []
                         * editNotDownOrderNum : 0
                         * purchaseInTheWayNum : 0
                         * shouldPurchanseNum : 0
                         * totalRequireNum : 0
                         * parentOpList : []
                         */

                        private String id;
                        private boolean isNewRecord;
                        private String htmlOrderProductSpecificationAttributes;
                        private String urls;
                        private int editNotDownOrderNum;
                        private int purchaseInTheWayNum;
                        private int shouldPurchanseNum;
                        private int totalRequireNum;
                        private List<?> orderProductSpecificationList;
                        private List<?> imageList;
                        private List<?> flowRemarkList;
                        private List<?> processFeedbackList;
                        private List<?> processWorkerList;
                        private List<?> sendUserList;
                        private List<?> workPlanList;
                        private List<?> relFlowProcessList;
                        private List<?> parentOpList;

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

                        public int getEditNotDownOrderNum() {
                            return editNotDownOrderNum;
                        }

                        public void setEditNotDownOrderNum(int editNotDownOrderNum) {
                            this.editNotDownOrderNum = editNotDownOrderNum;
                        }

                        public int getPurchaseInTheWayNum() {
                            return purchaseInTheWayNum;
                        }

                        public void setPurchaseInTheWayNum(int purchaseInTheWayNum) {
                            this.purchaseInTheWayNum = purchaseInTheWayNum;
                        }

                        public int getShouldPurchanseNum() {
                            return shouldPurchanseNum;
                        }

                        public void setShouldPurchanseNum(int shouldPurchanseNum) {
                            this.shouldPurchanseNum = shouldPurchanseNum;
                        }

                        public int getTotalRequireNum() {
                            return totalRequireNum;
                        }

                        public void setTotalRequireNum(int totalRequireNum) {
                            this.totalRequireNum = totalRequireNum;
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

                        public List<?> getSendUserList() {
                            return sendUserList;
                        }

                        public void setSendUserList(List<?> sendUserList) {
                            this.sendUserList = sendUserList;
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

                        public List<?> getParentOpList() {
                            return parentOpList;
                        }

                        public void setParentOpList(List<?> parentOpList) {
                            this.parentOpList = parentOpList;
                        }
                    }
                }

                public static class ProcessBean {
                    /**
                     * id : 51cd7f8c5c774dd084bf3abc99371116
                     * isNewRecord : false
                     * name : 剪料
                     * sort : 30
                     * type : 1
                     * feedbackType : 1
                     * relPPList : []
                     * isPublic : 1
                     * proList : []
                     * parentId : 0
                     * positionNames : []
                     */

                    private String id;
                    private boolean isNewRecord;
                    private String name;
                    private int sort;
                    private String type;
                    private String feedbackType;
                    private String isPublic;
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

                    public String getIsPublic() {
                        return isPublic;
                    }

                    public void setIsPublic(String isPublic) {
                        this.isPublic = isPublic;
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
                }

                public static class LatestFeedbackBean {
                    /**
                     * id : 4c9776a4ae7b44ae83391ff4a6bf6ade
                     * isNewRecord : false
                     * remarks :
                     * createBy : {"id":"3b62a05ae6c240ac8b7c1aa65c61f493","isNewRecord":false,"name":"小吴","sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"}
                     * createDate : 2018-01-15 13:52:24
                     * updateDate : 2018-01-15 13:52:24
                     * flow : {"id":"617eeb1835d348bfbd16db72cd2859f6","isNewRecord":false,"processList":[],"orderProducts":[],"remaksList":[]}
                     * process : {"id":"51cd7f8c5c774dd084bf3abc99371116","isNewRecord":false,"name":"剪料","sort":30,"relPPList":[],"proList":[],"parentId":"0","positionNames":[]}
                     * isFinished : 1
                     * achieveAmount : 200
                     * resources : []
                     * feedbackUser : {"id":"3b62a05ae6c240ac8b7c1aa65c61f493","isNewRecord":false,"name":"小吴","sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"}
                     * target : 0
                     * feedbackAttachmentStr :
                     * urls :
                     */

                    private String id;
                    private boolean isNewRecord;
                    private String remarks;
                    private CreateByBean createBy;
                    private String createDate;
                    private String updateDate;
                    private FlowBeanX flow;
                    private ProcessBeanX process;
                    private String isFinished;
                    private int achieveAmount;
                    private FeedbackUserBean feedbackUser;
                    private int target;
                    private String feedbackAttachmentStr;
                    private String urls;
                    private List<?> resources;

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

                    public FlowBeanX getFlow() {
                        return flow;
                    }

                    public void setFlow(FlowBeanX flow) {
                        this.flow = flow;
                    }

                    public ProcessBeanX getProcess() {
                        return process;
                    }

                    public void setProcess(ProcessBeanX process) {
                        this.process = process;
                    }

                    public String getIsFinished() {
                        return isFinished;
                    }

                    public void setIsFinished(String isFinished) {
                        this.isFinished = isFinished;
                    }

                    public int getAchieveAmount() {
                        return achieveAmount;
                    }

                    public void setAchieveAmount(int achieveAmount) {
                        this.achieveAmount = achieveAmount;
                    }

                    public FeedbackUserBean getFeedbackUser() {
                        return feedbackUser;
                    }

                    public void setFeedbackUser(FeedbackUserBean feedbackUser) {
                        this.feedbackUser = feedbackUser;
                    }

                    public int getTarget() {
                        return target;
                    }

                    public void setTarget(int target) {
                        this.target = target;
                    }

                    public String getFeedbackAttachmentStr() {
                        return feedbackAttachmentStr;
                    }

                    public void setFeedbackAttachmentStr(String feedbackAttachmentStr) {
                        this.feedbackAttachmentStr = feedbackAttachmentStr;
                    }

                    public String getUrls() {
                        return urls;
                    }

                    public void setUrls(String urls) {
                        this.urls = urls;
                    }

                    public List<?> getResources() {
                        return resources;
                    }

                    public void setResources(List<?> resources) {
                        this.resources = resources;
                    }

                    public static class CreateByBean {
                        /**
                         * id : 3b62a05ae6c240ac8b7c1aa65c61f493
                         * isNewRecord : false
                         * name : 小吴
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
                        private String name;
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

                    public static class FlowBeanX {
                        /**
                         * id : 617eeb1835d348bfbd16db72cd2859f6
                         * isNewRecord : false
                         * processList : []
                         * orderProducts : []
                         * remaksList : []
                         */

                        private String id;
                        private boolean isNewRecord;
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
                    }

                    public static class ProcessBeanX {
                        /**
                         * id : 51cd7f8c5c774dd084bf3abc99371116
                         * isNewRecord : false
                         * name : 剪料
                         * sort : 30
                         * relPPList : []
                         * proList : []
                         * parentId : 0
                         * positionNames : []
                         */

                        private String id;
                        private boolean isNewRecord;
                        private String name;
                        private int sort;
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
                    }

                    public static class FeedbackUserBean {
                        /**
                         * id : 3b62a05ae6c240ac8b7c1aa65c61f493
                         * isNewRecord : false
                         * name : 小吴
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
                        private String name;
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
                }
            }
            public static class CreateByBean {
                /**
                 * id : 4856035c527b419a9cf70d856e65c93b
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

            public static class OrderBean {
                /**
                 * id : 30bbf99d2b924798b9c9230bc6fea8b7
                 * isNewRecord : false
                 * remarks : 看看看看
                 * createBy : {"id":"4856035c527b419a9cf70d856e65c93b","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"}
                 * createDate : 2018-01-04 12:39:35
                 * updateDate : 2018-01-04 12:39:35
                 * parentIds : 0,
                 * sort : 30
                 * superId : af6c69d519074341ac1064fe3089a8a4
                 * aCompany : {"id":"7c10ee5fadd044e68f2b29f8a01d3f9a","isNewRecord":false,"remarks":"","name":"浙江总公司","sort":30,"code":"100000002","shortName":"浙江总公司","registTime":"2017-01-05 15:12:20","type":"1","grade":"1","address":"浙江省杭州市","zipCode":"310000","master":"李四","phone":"","fax":"","email":"","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_PictureUnlock_75303.pictureunlock","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"}
                 * bCompany : {"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"remarks":"","name":"江苏总公司萨达","sort":30,"code":"100000001","scope":"","shortName":"江苏总公司","type":"1","grade":"1","address":"江苏省南京市","zipCode":"210000","master":"张三","phone":"02512345678","fax":"","email":"zhagnsan@email.com","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_mmexport1494817708632.jpg","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"}
                 * bCompanyOrderOwnerUser : {"id":"4856035c527b419a9cf70d856e65c93b","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"}
                 * aOrderNumber : 125855
                 * orderStatus : 3
                 * takeOrderTime : 2018-01-04
                 * delieverTime : 2018-01-04
                 * takeOrderTimeString : 2018-01-04
                 * delieverTimeString : 2018-01-04
                 * orderType : 2
                 * orderProductList : []
                 * orderList : []
                 * historyOrderNumber : 0
                 * takeOrderTimeBeginString :
                 * takeOrderTimeEndString :
                 * parentId : 0
                 */

                private String id;
                private boolean isNewRecord;
                private String remarks;
                private CreateByBeanX createBy;
                private String createDate;
                private String updateDate;
                private String parentIds;
                private int sort;
                private String superId;
                private ACompanyBean aCompany;
                private BCompanyBean bCompany;
                private BCompanyOrderOwnerUserBean bCompanyOrderOwnerUser;
                private String aOrderNumber;
                private String orderStatus;
                private String takeOrderTime;
                private String delieverTime;
                private String takeOrderTimeString;
                private String delieverTimeString;
                private String orderType;
                private int historyOrderNumber;
                private String takeOrderTimeBeginString;
                private String takeOrderTimeEndString;
                private String parentId;
                private List<?> orderProductList;
                private List<?> orderList;

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

                public CreateByBeanX getCreateBy() {
                    return createBy;
                }

                public void setCreateBy(CreateByBeanX createBy) {
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

                public int getSort() {
                    return sort;
                }

                public void setSort(int sort) {
                    this.sort = sort;
                }

                public String getSuperId() {
                    return superId;
                }

                public void setSuperId(String superId) {
                    this.superId = superId;
                }

                public ACompanyBean getACompany() {
                    return aCompany;
                }

                public void setACompany(ACompanyBean aCompany) {
                    this.aCompany = aCompany;
                }

                public BCompanyBean getBCompany() {
                    return bCompany;
                }

                public void setBCompany(BCompanyBean bCompany) {
                    this.bCompany = bCompany;
                }

                public BCompanyOrderOwnerUserBean getBCompanyOrderOwnerUser() {
                    return bCompanyOrderOwnerUser;
                }

                public void setBCompanyOrderOwnerUser(BCompanyOrderOwnerUserBean bCompanyOrderOwnerUser) {
                    this.bCompanyOrderOwnerUser = bCompanyOrderOwnerUser;
                }

                public String getAOrderNumber() {
                    return aOrderNumber;
                }

                public void setAOrderNumber(String aOrderNumber) {
                    this.aOrderNumber = aOrderNumber;
                }

                public String getOrderStatus() {
                    return orderStatus;
                }

                public void setOrderStatus(String orderStatus) {
                    this.orderStatus = orderStatus;
                }

                public String getTakeOrderTime() {
                    return takeOrderTime;
                }

                public void setTakeOrderTime(String takeOrderTime) {
                    this.takeOrderTime = takeOrderTime;
                }

                public String getDelieverTime() {
                    return delieverTime;
                }

                public void setDelieverTime(String delieverTime) {
                    this.delieverTime = delieverTime;
                }

                public String getTakeOrderTimeString() {
                    return takeOrderTimeString;
                }

                public void setTakeOrderTimeString(String takeOrderTimeString) {
                    this.takeOrderTimeString = takeOrderTimeString;
                }

                public String getDelieverTimeString() {
                    return delieverTimeString;
                }

                public void setDelieverTimeString(String delieverTimeString) {
                    this.delieverTimeString = delieverTimeString;
                }

                public String getOrderType() {
                    return orderType;
                }

                public void setOrderType(String orderType) {
                    this.orderType = orderType;
                }

                public int getHistoryOrderNumber() {
                    return historyOrderNumber;
                }

                public void setHistoryOrderNumber(int historyOrderNumber) {
                    this.historyOrderNumber = historyOrderNumber;
                }

                public String getTakeOrderTimeBeginString() {
                    return takeOrderTimeBeginString;
                }

                public void setTakeOrderTimeBeginString(String takeOrderTimeBeginString) {
                    this.takeOrderTimeBeginString = takeOrderTimeBeginString;
                }

                public String getTakeOrderTimeEndString() {
                    return takeOrderTimeEndString;
                }

                public void setTakeOrderTimeEndString(String takeOrderTimeEndString) {
                    this.takeOrderTimeEndString = takeOrderTimeEndString;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public List<?> getOrderProductList() {
                    return orderProductList;
                }

                public void setOrderProductList(List<?> orderProductList) {
                    this.orderProductList = orderProductList;
                }

                public List<?> getOrderList() {
                    return orderList;
                }

                public void setOrderList(List<?> orderList) {
                    this.orderList = orderList;
                }

                public static class CreateByBeanX {
                    /**
                     * id : 4856035c527b419a9cf70d856e65c93b
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

                public static class ACompanyBean {
                    /**
                     * id : 7c10ee5fadd044e68f2b29f8a01d3f9a
                     * isNewRecord : false
                     * remarks :
                     * name : 浙江总公司
                     * sort : 30
                     * code : 100000002
                     * shortName : 浙江总公司
                     * registTime : 2017-01-05 15:12:20
                     * type : 1
                     * grade : 1
                     * address : 浙江省杭州市
                     * zipCode : 310000
                     * master : 李四
                     * phone :
                     * fax :
                     * email :
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
                    private String remarks;
                    private String name;
                    private int sort;
                    private String code;
                    private String shortName;
                    private String registTime;
                    private String type;
                    private String grade;
                    private String address;
                    private String zipCode;
                    private String master;
                    private String phone;
                    private String fax;
                    private String email;
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

                    public String getRemarks() {
                        return remarks;
                    }

                    public void setRemarks(String remarks) {
                        this.remarks = remarks;
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

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getShortName() {
                        return shortName;
                    }

                    public void setShortName(String shortName) {
                        this.shortName = shortName;
                    }

                    public String getRegistTime() {
                        return registTime;
                    }

                    public void setRegistTime(String registTime) {
                        this.registTime = registTime;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getGrade() {
                        return grade;
                    }

                    public void setGrade(String grade) {
                        this.grade = grade;
                    }

                    public String getAddress() {
                        return address;
                    }

                    public void setAddress(String address) {
                        this.address = address;
                    }

                    public String getZipCode() {
                        return zipCode;
                    }

                    public void setZipCode(String zipCode) {
                        this.zipCode = zipCode;
                    }

                    public String getMaster() {
                        return master;
                    }

                    public void setMaster(String master) {
                        this.master = master;
                    }

                    public String getPhone() {
                        return phone;
                    }

                    public void setPhone(String phone) {
                        this.phone = phone;
                    }

                    public String getFax() {
                        return fax;
                    }

                    public void setFax(String fax) {
                        this.fax = fax;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public void setEmail(String email) {
                        this.email = email;
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

                public static class BCompanyBean {
                    /**
                     * id : f541afa391974409ba680e9bd9a394fd
                     * isNewRecord : false
                     * remarks :
                     * name : 江苏总公司萨达
                     * sort : 30
                     * code : 100000001
                     * scope :
                     * shortName : 江苏总公司
                     * type : 1
                     * grade : 1
                     * address : 江苏省南京市
                     * zipCode : 210000
                     * master : 张三
                     * phone : 02512345678
                     * fax :
                     * email : zhagnsan@email.com
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
                     * photo : http://blanink.oss-cn-hangzhou.aliyuncs.com/alioss_mmexport1494817708632.jpg
                     * roleList : []
                     * grossProfit : 0
                     * pType :
                     * places : 0
                     * parentId : 0
                     */

                    private String id;
                    private boolean isNewRecord;
                    private String remarks;
                    private String name;
                    private int sort;
                    private String code;
                    private String scope;
                    private String shortName;
                    private String type;
                    private String grade;
                    private String address;
                    private String zipCode;
                    private String master;
                    private String phone;
                    private String fax;
                    private String email;
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

                    public String getRemarks() {
                        return remarks;
                    }

                    public void setRemarks(String remarks) {
                        this.remarks = remarks;
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

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getScope() {
                        return scope;
                    }

                    public void setScope(String scope) {
                        this.scope = scope;
                    }

                    public String getShortName() {
                        return shortName;
                    }

                    public void setShortName(String shortName) {
                        this.shortName = shortName;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public String getGrade() {
                        return grade;
                    }

                    public void setGrade(String grade) {
                        this.grade = grade;
                    }

                    public String getAddress() {
                        return address;
                    }

                    public void setAddress(String address) {
                        this.address = address;
                    }

                    public String getZipCode() {
                        return zipCode;
                    }

                    public void setZipCode(String zipCode) {
                        this.zipCode = zipCode;
                    }

                    public String getMaster() {
                        return master;
                    }

                    public void setMaster(String master) {
                        this.master = master;
                    }

                    public String getPhone() {
                        return phone;
                    }

                    public void setPhone(String phone) {
                        this.phone = phone;
                    }

                    public String getFax() {
                        return fax;
                    }

                    public void setFax(String fax) {
                        this.fax = fax;
                    }

                    public String getEmail() {
                        return email;
                    }

                    public void setEmail(String email) {
                        this.email = email;
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

                public static class BCompanyOrderOwnerUserBean {
                    /**
                     * id : 4856035c527b419a9cf70d856e65c93b
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
            }

            public static class CompanyCategoryBean {
                /**
                 * id : 0c89ee5b537e4056b92d009508375f22
                 * isNewRecord : false
                 * remarks :
                 * createBy : {"id":"d6c8e9bc4c2b476ba84b962c27882f41","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"}
                 * createDate : 2017-04-02 21:29:27
                 * updateDate : 2017-09-04 15:29:03
                 * parentIds : 0,1449c341242c4075b3fa5e8bbeabf588,82085507266541abad03453af441bb30,
                 * name : TCL
                 * sort : 30
                 * company : {"id":"f541afa391974409ba680e9bd9a394fd","isNewRecord":false,"sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"0"}
                 * isLeaf : 1
                 * serveType : 0
                 * unit : 1
                 * relIndustryCategoryAttribute : {"id":"a7e9f04c18134441b1703117d605f912,eb646ffa0b1c4d93837d29e77de1d07b","isNewRecord":false}
                 * size : 0
                 * categoryAttributeList : []
                 * relIndustryCompanyCategoryList : []
                 * attributeNames :
                 * industryCategoryIds :
                 * industryCategoryNames :
                 * parentName :
                 * attributeIds :
                 * parentId : 82085507266541abad03453af441bb30
                 */

                private String id;
                private boolean isNewRecord;
                private String remarks;
                private CreateByBeanXX createBy;
                private String createDate;
                private String updateDate;
                private String parentIds;
                private String name;
                private int alarmDay;
                private int sort;
                private CompanyBean company;
                private String isLeaf;
                private String serveType;
                private String unit;
                private RelIndustryCategoryAttributeBean relIndustryCategoryAttribute;
                private int size;
                private String attributeNames;
                private String industryCategoryIds;
                private String industryCategoryNames;
                private String parentName;
                private String attributeIds;
                private String parentId;
                private List<?> categoryAttributeList;
                private List<?> relIndustryCompanyCategoryList;

                public int getAlarmDay() {
                    return alarmDay;
                }

                public void setAlarmDay(int alarmDay) {
                    this.alarmDay = alarmDay;
                }

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

                public CreateByBeanXX getCreateBy() {
                    return createBy;
                }

                public void setCreateBy(CreateByBeanXX createBy) {
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

                public CompanyBean getCompany() {
                    return company;
                }

                public void setCompany(CompanyBean company) {
                    this.company = company;
                }

                public String getIsLeaf() {
                    return isLeaf;
                }

                public void setIsLeaf(String isLeaf) {
                    this.isLeaf = isLeaf;
                }

                public String getServeType() {
                    return serveType;
                }

                public void setServeType(String serveType) {
                    this.serveType = serveType;
                }

                public String getUnit() {
                    return unit;
                }

                public void setUnit(String unit) {
                    this.unit = unit;
                }

                public RelIndustryCategoryAttributeBean getRelIndustryCategoryAttribute() {
                    return relIndustryCategoryAttribute;
                }

                public void setRelIndustryCategoryAttribute(RelIndustryCategoryAttributeBean relIndustryCategoryAttribute) {
                    this.relIndustryCategoryAttribute = relIndustryCategoryAttribute;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public String getAttributeNames() {
                    return attributeNames;
                }

                public void setAttributeNames(String attributeNames) {
                    this.attributeNames = attributeNames;
                }

                public String getIndustryCategoryIds() {
                    return industryCategoryIds;
                }

                public void setIndustryCategoryIds(String industryCategoryIds) {
                    this.industryCategoryIds = industryCategoryIds;
                }

                public String getIndustryCategoryNames() {
                    return industryCategoryNames;
                }

                public void setIndustryCategoryNames(String industryCategoryNames) {
                    this.industryCategoryNames = industryCategoryNames;
                }

                public String getParentName() {
                    return parentName;
                }

                public void setParentName(String parentName) {
                    this.parentName = parentName;
                }

                public String getAttributeIds() {
                    return attributeIds;
                }

                public void setAttributeIds(String attributeIds) {
                    this.attributeIds = attributeIds;
                }

                public String getParentId() {
                    return parentId;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public List<?> getCategoryAttributeList() {
                    return categoryAttributeList;
                }

                public void setCategoryAttributeList(List<?> categoryAttributeList) {
                    this.categoryAttributeList = categoryAttributeList;
                }

                public List<?> getRelIndustryCompanyCategoryList() {
                    return relIndustryCompanyCategoryList;
                }

                public void setRelIndustryCompanyCategoryList(List<?> relIndustryCompanyCategoryList) {
                    this.relIndustryCompanyCategoryList = relIndustryCompanyCategoryList;
                }

                public static class CreateByBeanXX {
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

                public static class RelIndustryCategoryAttributeBean {
                    /**
                     * id : a7e9f04c18134441b1703117d605f912,eb646ffa0b1c4d93837d29e77de1d07b
                     * isNewRecord : false
                     */

                    private String id;
                    private boolean isNewRecord;

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
                }
            }

            public static class CompanyBeanX {
                /**
                 * id : 7c10ee5fadd044e68f2b29f8a01d3f9a
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
                private String name;
                private String shortName;
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

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getShortName() {
                    return shortName;
                }

                public void setShortName(String shortName) {
                    this.shortName = shortName;
                }

                public String getpType() {
                    return pType;
                }

                public void setpType(String pType) {
                    this.pType = pType;
                }

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

            public static class BCompanyBeanX {
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

            public static class OrderProductSpecificationListBean {
                /**
                 * id : 318ad5387d1648f39d57f93cd16c4b99
                 * isNewRecord : false
                 * orderProduct : {"id":"cb4558d4696b4a428aaca6cdd29ac6a0","isNewRecord":false,"orderProductSpecificationList":[],"htmlOrderProductSpecificationAttributes":"","imageList":[],"flowRemarkList":[],"processFeedbackList":[],"processWorkerList":[],"sendUserList":[],"workPlanList":[],"urls":"","relFlowProcessList":[],"editNotDownOrderNum":0,"purchaseInTheWayNum":0,"shouldPurchanseNum":0,"totalRequireNum":0,"parentOpList":[]}
                 * attribute : {"id":"267a9460de74443cb8d292aac1b2e0ab","isNewRecord":false,"name":"容量","sort":30,"public":false,"parentId":"0"}
                 * attributeValue :
                 * hardCodeValueList : []
                 */

                private String id;
                private boolean isNewRecord;
                private OrderProductBean orderProduct;
                private AttributeBean attribute;
                private String attributeValue;
                private List<?> hardCodeValueList;

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

                public OrderProductBean getOrderProduct() {
                    return orderProduct;
                }

                public void setOrderProduct(OrderProductBean orderProduct) {
                    this.orderProduct = orderProduct;
                }

                public AttributeBean getAttribute() {
                    return attribute;
                }

                public void setAttribute(AttributeBean attribute) {
                    this.attribute = attribute;
                }

                public String getAttributeValue() {
                    return attributeValue;
                }

                public void setAttributeValue(String attributeValue) {
                    this.attributeValue = attributeValue;
                }

                public List<?> getHardCodeValueList() {
                    return hardCodeValueList;
                }

                public void setHardCodeValueList(List<?> hardCodeValueList) {
                    this.hardCodeValueList = hardCodeValueList;
                }

                public static class OrderProductBean {
                    /**
                     * id : cb4558d4696b4a428aaca6cdd29ac6a0
                     * isNewRecord : false
                     * orderProductSpecificationList : []
                     * htmlOrderProductSpecificationAttributes :
                     * imageList : []
                     * flowRemarkList : []
                     * processFeedbackList : []
                     * processWorkerList : []
                     * sendUserList : []
                     * workPlanList : []
                     * urls :
                     * relFlowProcessList : []
                     * editNotDownOrderNum : 0
                     * purchaseInTheWayNum : 0
                     * shouldPurchanseNum : 0
                     * totalRequireNum : 0
                     * parentOpList : []
                     */

                    private String id;
                    private boolean isNewRecord;
                    private String htmlOrderProductSpecificationAttributes;
                    private String urls;
                    private int editNotDownOrderNum;
                    private int purchaseInTheWayNum;
                    private int shouldPurchanseNum;
                    private int totalRequireNum;
                    private List<?> orderProductSpecificationList;
                    private List<?> imageList;
                    private List<?> flowRemarkList;
                    private List<?> processFeedbackList;
                    private List<?> processWorkerList;
                    private List<?> sendUserList;
                    private List<?> workPlanList;
                    private List<?> relFlowProcessList;
                    private List<?> parentOpList;

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

                    public int getEditNotDownOrderNum() {
                        return editNotDownOrderNum;
                    }

                    public void setEditNotDownOrderNum(int editNotDownOrderNum) {
                        this.editNotDownOrderNum = editNotDownOrderNum;
                    }

                    public int getPurchaseInTheWayNum() {
                        return purchaseInTheWayNum;
                    }

                    public void setPurchaseInTheWayNum(int purchaseInTheWayNum) {
                        this.purchaseInTheWayNum = purchaseInTheWayNum;
                    }

                    public int getShouldPurchanseNum() {
                        return shouldPurchanseNum;
                    }

                    public void setShouldPurchanseNum(int shouldPurchanseNum) {
                        this.shouldPurchanseNum = shouldPurchanseNum;
                    }

                    public int getTotalRequireNum() {
                        return totalRequireNum;
                    }

                    public void setTotalRequireNum(int totalRequireNum) {
                        this.totalRequireNum = totalRequireNum;
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

                    public List<?> getSendUserList() {
                        return sendUserList;
                    }

                    public void setSendUserList(List<?> sendUserList) {
                        this.sendUserList = sendUserList;
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

                    public List<?> getParentOpList() {
                        return parentOpList;
                    }

                    public void setParentOpList(List<?> parentOpList) {
                        this.parentOpList = parentOpList;
                    }
                }

                public static class AttributeBean {
                    /**
                     * id : 267a9460de74443cb8d292aac1b2e0ab
                     * isNewRecord : false
                     * name : 容量
                     * sort : 30
                     * public : false
                     * parentId : 0
                     */

                    private String id;
                    private boolean isNewRecord;
                    private String name;
                    private int sort;
                    @SerializedName("public")
                    private boolean publicX;
                    private String parentId;

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

                    public boolean isPublicX() {
                        return publicX;
                    }

                    public void setPublicX(boolean publicX) {
                        this.publicX = publicX;
                    }

                    public String getParentId() {
                        return parentId;
                    }

                    public void setParentId(String parentId) {
                        this.parentId = parentId;
                    }
                }
            }
        }
    }
}
