package com.blanink.bean;

import java.util.List;

/**
 * 作者：Created by liangshen on 2018/1/30 0030 11:27
 * 邮箱：liangshen6868@163.com
 */
public class User {

    /**
     * id : 0afa051b4a50459cbe7058bdbba64ed9
     * isNewRecord : false
     * createBy : {"id":"free","isNewRecord":false,"sort":30,"loginFlag":"1","mobileMenuList":[],"roleList":[],"admin":false,"roleNames":"","parentId":"0"}
     * createDate : 2018-01-26 17:10:58
     * updateDate : 2018-01-26 17:10:58
     * name : 15236000860
     * sort : 30
     * company : {"id":"41517c0be52e41ddab0f7423b0605447","isNewRecord":false,"parentIds":"0,1,","name":"单单打打","sort":30,"type":"1","address":"一直在","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"1"}
     * office : {"id":"41517c0be52e41ddab0f7423b0605447","isNewRecord":false,"parentIds":"0,1,","name":"单单打打","sort":30,"type":"1","customerServiceList":[],"reviewOthers":0,"reviewSelf":0,"otherCosting":0,"otherPayment":0,"otherQuality":0,"otherService":0,"otherTime":0,"selfCosting":0,"selfPayment":0,"serviceCount":0,"photo":"","roleList":[],"grossProfit":0,"pType":"","places":0,"parentId":"1"}
     * loginName : 18994313141
     * phone : 15236000860
     * loginIp : 58.208.15.92
     * loginDate : 2018-01-26 17:46:07
     * loginFlag : 1
     * oldLoginIp : 58.208.15.92
     * oldLoginDate : 2018-01-26 17:46:07
     * failureDate : 1674724255000
     * mobileMenuList : []
     * roleList : [{"id":"4b5342f1b781472a82c6bf8ff1c6b6bd","isNewRecord":false,"remarks":"","name":"免费使用岗","roleType":"user","dataScope":"8","sysData":"1","useable":"1","isPublic":"0","userList":[],"menuList":[],"officeList":[],"permissions":[],"officeIds":"","menuIdList":[],"menuIds":"","officeIdList":[]}]
     * admin : false
     * roleNames : 免费使用岗
     * parentId : 0
     */

    private String id;
    private boolean isNewRecord;
    private CreateByBean createBy;
    private String createDate;
    private String updateDate;
    private String name;
    private int sort;
    private CompanyBean company;
    private OfficeBean office;
    private String loginName;
    private String phone;
    private String loginIp;
    private String loginDate;
    private String loginFlag;
    private String oldLoginIp;
    private String oldLoginDate;
    private long failureDate;
    private boolean admin;
    private String roleNames;
    private String parentId;
    private List<?> mobileMenuList;
    private List<RoleListBean> roleList;

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

    public OfficeBean getOffice() {
        return office;
    }

    public void setOffice(OfficeBean office) {
        this.office = office;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getOldLoginIp() {
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    public String getOldLoginDate() {
        return oldLoginDate;
    }

    public void setOldLoginDate(String oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

    public long getFailureDate() {
        return failureDate;
    }

    public void setFailureDate(long failureDate) {
        this.failureDate = failureDate;
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

    public List<RoleListBean> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleListBean> roleList) {
        this.roleList = roleList;
    }

    public static class CreateByBean {
        /**
         * id : free
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
         * id : 41517c0be52e41ddab0f7423b0605447
         * isNewRecord : false
         * parentIds : 0,1,
         * name : 单单打打
         * sort : 30
         * type : 1
         * address : 一直在
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
         * parentId : 1
         */

        private String id;
        private boolean isNewRecord;
        private String parentIds;
        private String name;
        private int sort;
        private String type;
        private String address;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

    public static class OfficeBean {
        /**
         * id : 41517c0be52e41ddab0f7423b0605447
         * isNewRecord : false
         * parentIds : 0,1,
         * name : 单单打打
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
         * parentId : 1
         */

        private String id;
        private boolean isNewRecord;
        private String parentIds;
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

    public static class RoleListBean {
        /**
         * id : 4b5342f1b781472a82c6bf8ff1c6b6bd
         * isNewRecord : false
         * remarks :
         * name : 免费使用岗
         * roleType : user
         * dataScope : 8
         * sysData : 1
         * useable : 1
         * isPublic : 0
         * userList : []
         * menuList : []
         * officeList : []
         * permissions : []
         * officeIds :
         * menuIdList : []
         * menuIds :
         * officeIdList : []
         */

        private String id;
        private boolean isNewRecord;
        private String remarks;
        private String name;
        private String roleType;
        private String dataScope;
        private String sysData;
        private String useable;
        private String isPublic;
        private String officeIds;
        private String menuIds;
        private List<?> userList;
        private List<?> menuList;
        private List<?> officeList;
        private List<?> permissions;
        private List<?> menuIdList;
        private List<?> officeIdList;

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

        public String getRoleType() {
            return roleType;
        }

        public void setRoleType(String roleType) {
            this.roleType = roleType;
        }

        public String getDataScope() {
            return dataScope;
        }

        public void setDataScope(String dataScope) {
            this.dataScope = dataScope;
        }

        public String getSysData() {
            return sysData;
        }

        public void setSysData(String sysData) {
            this.sysData = sysData;
        }

        public String getUseable() {
            return useable;
        }

        public void setUseable(String useable) {
            this.useable = useable;
        }

        public String getIsPublic() {
            return isPublic;
        }

        public void setIsPublic(String isPublic) {
            this.isPublic = isPublic;
        }

        public String getOfficeIds() {
            return officeIds;
        }

        public void setOfficeIds(String officeIds) {
            this.officeIds = officeIds;
        }

        public String getMenuIds() {
            return menuIds;
        }

        public void setMenuIds(String menuIds) {
            this.menuIds = menuIds;
        }

        public List<?> getUserList() {
            return userList;
        }

        public void setUserList(List<?> userList) {
            this.userList = userList;
        }

        public List<?> getMenuList() {
            return menuList;
        }

        public void setMenuList(List<?> menuList) {
            this.menuList = menuList;
        }

        public List<?> getOfficeList() {
            return officeList;
        }

        public void setOfficeList(List<?> officeList) {
            this.officeList = officeList;
        }

        public List<?> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<?> permissions) {
            this.permissions = permissions;
        }

        public List<?> getMenuIdList() {
            return menuIdList;
        }

        public void setMenuIdList(List<?> menuIdList) {
            this.menuIdList = menuIdList;
        }

        public List<?> getOfficeIdList() {
            return officeIdList;
        }

        public void setOfficeIdList(List<?> officeIdList) {
            this.officeIdList = officeIdList;
        }
    }
}
