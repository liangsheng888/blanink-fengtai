package com.blanink.http;


import com.blanink.bean.AfterSaleHistory;
import com.blanink.bean.AfterSales;
import com.blanink.bean.AllReview;
import com.blanink.bean.BaseResponse;
import com.blanink.bean.BidDetailInfo;
import com.blanink.bean.BidTender;
import com.blanink.bean.BigSeekContent;
import com.blanink.bean.ComeOder;
import com.blanink.bean.Company;
import com.blanink.bean.CompanyCateGory;
import com.blanink.bean.CompanyEmployee;
import com.blanink.bean.CompanyProduct;
import com.blanink.bean.CompanyProductCategory;
import com.blanink.bean.Dashboard;
import com.blanink.bean.DraftContent;
import com.blanink.bean.FeedBackingTask;
import com.blanink.bean.FeedPrcessDetail;
import com.blanink.bean.FlowDetail;
import com.blanink.bean.FlowRemark;
import com.blanink.bean.FlowSort;
import com.blanink.bean.GoFormDownOrder;
import com.blanink.bean.GoOrderDown;
import com.blanink.bean.ListProcess;
import com.blanink.bean.LoginResult;
import com.blanink.bean.ManyCustomer;
import com.blanink.bean.MenuControl;
import com.blanink.bean.Notify;
import com.blanink.bean.NotifyOffice;
import com.blanink.bean.Office;
import com.blanink.bean.OfficeEmp;
import com.blanink.bean.OneOrderProduct;
import com.blanink.bean.OrderDetail;
import com.blanink.bean.OrderProduct;
import com.blanink.bean.OrderProductAttributes;
import com.blanink.bean.OrderProductRemark;
import com.blanink.bean.OrderProductStatus;
import com.blanink.bean.PartnerInfo;
import com.blanink.bean.ProcessFeedback;
import com.blanink.bean.ProcessTarget;
import com.blanink.bean.ProductAttributes;
import com.blanink.bean.PurchaseProduct;
import com.blanink.bean.RelFlow;
import com.blanink.bean.RelIndustryCategoryAttribute;
import com.blanink.bean.Response;
import com.blanink.bean.ResponseAllocation;
import com.blanink.bean.ResponseDelete;
import com.blanink.bean.ResponseOrder;
import com.blanink.bean.SingleCustomer;
import com.blanink.bean.TalkHistoryLog;
import com.blanink.bean.TenderAndBid;
import com.blanink.bean.TypeCateGory;
import com.blanink.bean.UnReadNotify;
import com.blanink.bean.UsefulFlow;
import com.blanink.bean.User;
import com.blanink.bean.VersionInfo;
import com.blanink.bean.WorkPlanToAllocation;
import com.blanink.bean.WorkPlaned;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


/**
 * 作者：Created by liangshen on 2017/10/17 0017 10:30
 * 邮箱：liangshen6868@163.com
 */

public interface ApiService {

    //登录
    @GET("login")
    Observable<LoginResult> loginResult(@QueryMap Map<String,Object> params);
    //
    @GET("getUserProfie")
    Observable<LoginResult> getUserProfie(@QueryMap Map<String,Object> params);

    //
    @GET("userUpdate")
    Observable<LoginResult> userUpdate(@QueryMap Map<String,Object> params);
    //获取版本
    @POST("Version/getVersionInfo")
    Observable<VersionInfo> getVersionInfo(@QueryMap Map<String,Object> params);
    //首页菜单
    @Headers("Cache-Control: public, max-age=5")
    @GET("getMenu")
    Observable<MenuControl> getMenu(@QueryMap Map<String,Object> params);
    //获取售后列表
    @GET("afterSales/myAfterSalesList")
    Observable<AfterSales> getAfterSales(@QueryMap Map<String,Object> params);
    //获取未读通知个数
    @GET("notify/newSize")
    Observable<UnReadNotify> getUnReadNotify(@Query("currentUser.id") String userId);

    //客户申请通知
    @POST("jpush/applyCustomer")
    Observable<ResponseDelete> applyCustomer(@QueryMap Map<String,Object> params);

    //客户解除通知
    @POST("jpush/delCustomer")
    Observable<ResponseDelete> delCustomer(@QueryMap Map<String,Object> params);

    //供应商解除通知
    @POST("jpush/delSupplier")
    Observable<ResponseDelete> delSupplier(@QueryMap Map<String,Object> params);

    //供应商合作通知
    @POST("jpush/applySupplier")
    Observable<ResponseDelete> applySupplier(@QueryMap Map<String,Object> params);
    // 申请回复通知
    @POST("jpush/applyResponse")
    Observable<ResponseDelete> applyResponse(@QueryMap Map<String,Object> params);

    // 产品打回甲方通知 flag 1 打回 2 拒绝
    @POST("jpush/repluseA")
    Observable<ResponseDelete> repluseA(@QueryMap Map<String,Object> params);

    // 产品发货通知
    @POST("jpush/deliver")
    Observable<ResponseDelete> deliver(@QueryMap Map<String,Object> params);

    // 产品收货通知
    @POST("jpush/recevieGoods")
    Observable<ResponseDelete> receiveGoods(@QueryMap Map<String,Object> params);
    //流程发布通知
    @POST("jpush/flowPublish")
    Observable<ResponseDelete> flowPublish(@QueryMap Map<String, Object> paramsMap);
    //投标通知
    @POST("jpush/bidNotify")
    Observable<ResponseDelete> bidNotify(@QueryMap Map<String, Object> paramsMap);
    //投标确认合作
    @POST("jpush/bidAccord")
    Observable<ResponseDelete> bidAccord(@QueryMap Map<String, Object> paramsMap);

    //任务分配
    @POST("jpush/taskAllocation")
    Observable<ResponseDelete> taskAllocation(@QueryMap Map<String, Object> paramsMap);
    //查询工序个数
    @POST("processFeedback/listProcess")
    Observable<ListProcess> listProcess(@QueryMap Map<String, Object> paramsMap);

    //查询工序个数
    @POST("workPlan/listProcess")
    Observable<ProcessFeedback> workPlanListProcess(@QueryMap Map<String, Object> paramsMap);

    //二维码扫描后，进行反馈的页面调用方法，查看页面（剩余工作、产品详情、产品备注补充信息）
    @POST("processFeedback/listFeedbackingTaskQRCode")
    Observable<FeedBackingTask> listFeedbackingTaskQRCode(@QueryMap Map<String, Object> paramsMap);
    //权限控制
    @POST("processFeedback/accessControl")
    Observable<Response> accessControl(@QueryMap Map<String, Object> paramsMap);
    //发货
    @POST("processFeedback/delivery")
    Observable<Response> delivery(@QueryMap Map<String, Object> paramsMap);

    //viewOneHistoryTask
    @GET("processFeedback/viewOneHistoryTask")
    Observable<FeedPrcessDetail> viewOneHistoryTask(@QueryMap Map<String, Object> paramsMap);

    /***
     *
     * 售后处理模块
     */
    //保存售后
    @POST("afterSales/saveAfterSales")
    Observable<ResponseDelete> saveAfterSales(@QueryMap Map<String, Object> paramsMap);

    //售后处理记录
    @GET("afterSales/getAfterSalesHandleList")
    Observable<AfterSaleHistory> getAfterSalesHandleList(@QueryMap Map<String, Object> paramsMap);
    //售后列表
    @GET("afterSales/myAfterSalesList")
    Observable<AfterSaleHistory> myAfterSalesList(@QueryMap Map<String, Object> paramsMap);
    //售后处理
    @POST("afterSales/afterSalesHandle")
    Observable<ResponseDelete> afterSalesHandle(@QueryMap Map<String, Object> paramsMap);

    /***
     *
     * 招投标模块
     */
    //投标列表
    @GET("inviteBid/inviteBidSort")
    Observable<TenderAndBid> inviteBidSort(@QueryMap Map<String, Object> paramsMap);
    //投标申请
    @GET("inviteBid/saveBid")
    Observable<Response> saveBid(@QueryMap Map<String, Object> paramsMap);
    //投标详情
    @GET("inviteBid/bidDetail")
    Observable<BidDetailInfo> bidDetail(@QueryMap Map<String, Object> paramsMap);
    //聊天记录
    @GET("inviteBid/bidReturn")
    Observable<TalkHistoryLog> bidReturn(@QueryMap Map<String, Object> paramsMap);
    //发送
    @POST("inviteBid/bidAddReturn")
    Observable<Response> bidAddReturn(@QueryMap Map<String, Object> paramsMap);
    @GET("inviteBid/bidlist")
    Observable<TenderAndBid> bidlist(@QueryMap Map<String, Object> paramsMap);


    @GET("inviteBid/findInviteBid")
    Observable<TenderAndBid> findInviteBid(@QueryMap Map<String, Object> paramsMap);
    //投标确认合作
    @POST("partner/savePartner")
    Observable<Response> savePartner(@QueryMap Map<String, Object> paramsMap);

    @GET("inviteBid/myInviteBidSort")
    Observable<TenderAndBid> myInviteBidSort(@QueryMap Map<String, Object> paramsMap);
    //招标修改
    @POST("inviteBid/update")
    Observable<Response> update(@QueryMap Map<String, Object> paramsMap);

    //招标发布
    @POST("inviteBid/save")
    Observable<Response> save(@QueryMap Map<String, Object> paramsMap);

    /***
     * 排流程模块
     */
    //流程留言保存
    @POST("flow/saveFlowRemark")
    Observable<Response> saveFlowRemark(@QueryMap Map<String, Object> paramsMap);
    //流程留言记录
    @POST("flow/getFlowRemark")
    Observable<FlowRemark> getFlowRemark(@QueryMap Map<String, Object> paramsMap);
    //常用流程
    @GET("flow/commonList")
    Observable<UsefulFlow> commonList(@QueryMap Map<String, Object> paramsMap);
    //
    @GET("flow/getCommon")
    Observable<FlowDetail> getCommon(@QueryMap Map<String, Object> paramsMap);
    //获取流程
    @GET("flow/getFlow")
    Observable<FlowDetail> getFlow(@QueryMap Map<String, Object> paramsMap);
   //流程保存
   @POST("flow/流程保存saveFlow")
   Observable<Response> saveFlow(@QueryMap Map<String, Object> paramsMap);
    //已拍流程产品
    @GET("flow/sortCompletedList")
    Observable<FlowSort> sortCompletedList(@QueryMap Map<String, Object> paramsMap);
    //工序
    @GET("process/getRelFlowProcess")
    Observable<RelFlow> getRelFlowProcess(@QueryMap Map<String, Object> paramsMap);
    //工序详情
    @GET("process/getProcessTarget")
    Observable<ProcessTarget> getProcessTarget(@QueryMap Map<String, Object> paramsMap);

    //发布
    @GET("flow/publish")
    Observable<Response> publish(@QueryMap Map<String, Object> paramsMap);

    //生产仪表盘
    @GET("dashboard/listOrderProductJson")
    Observable<Dashboard> listOrderProductJson(@QueryMap Map<String, Object> paramsMap);

    //采购仪表盘
    @GET("dashboard/listSupplierOrderProductStatusJson")
    Observable<Dashboard> listSupplierOrderProductStatusJson(@QueryMap Map<String, Object> paramsMap);
    /***
     * 订单模块
     */
    //来单（）  流程订单（flag=1）
    @GET("order/listJson_coming")
    Observable<ComeOder> listJson_coming(@QueryMap Map<String, Object> paramsMap);
    //;组合下单
    @GET("order/makeOrderGoForm")
    Observable<GoFormDownOrder> makeOrderGoForm(@QueryMap Map<String, Object> paramsMap);
    //产品 orderDetail
    @GET("order/orderDetail")
    Observable<OrderDetail> orderDetail(@QueryMap Map<String, Object> paramsMap);
    //订单产品状态
    @GET("order/getOneOrderProductStatus")
    Observable<OrderProductStatus> getOneOrderProductStatus(@QueryMap Map<String, Object> paramsMap);
    //产品详情
    @GET("order/getOneOrderProduct")
    Observable<OneOrderProduct> getOneOrderProduct(@QueryMap Map<String, Object> paramsMap);
    //产品修改
    @POST("order/save_order_prod")
    Observable<Response> save_order_prod(@QueryMap Map<String, Object> paramsMap);
    //添加产品
    @POST("order/saveOrder")
    Observable<ResponseOrder> saveOrder(@QueryMap Map<String, Object> paramsMap);
    //继续添加产品
    @POST("order/saveOrderProduct")
    Observable<ResponseOrder> saveOrderProduct(@QueryMap Map<String, Object> paramsMap);
    //删除产品
    @POST("order/deleteOrderProduct")
    Observable<ResponseDelete> deleteOrderProduct(@QueryMap Map<String, Object> paramsMap);
    @GET("order/orderProductAttribute")
    Observable<OrderProductAttributes> orderProductAttribute(@QueryMap Map<String, Object> paramsMap);
    //客户
    @GET("order/officeList")
    Observable<PartnerInfo> officeList(@QueryMap Map<String, Object> paramsMap);
    //供应商
    @GET("order/partnerList")
    Observable<PartnerInfo> partnerList(@QueryMap Map<String, Object> paramsMap);
    //产品类
    @GET("order/categoryList")
    Observable<CompanyProductCategory> categoryList(@QueryMap Map<String, Object> paramsMap);
    //产品属性
    @GET("order/attributeList")
    Observable<RelIndustryCategoryAttribute> attributeList(@QueryMap Map<String, Object> paramsMap);
    //公司用户列表userList
    @GET("order/userList")
    Observable<OfficeEmp> userList(@QueryMap Map<String, Object> paramsMap);
     //来单修改
    @POST("order/orderUpdate")
    Observable<Response> orderUpdate(@QueryMap Map<String, Object> paramsMap);
    //产品沟通记录
    @GET("order/viewComments")
    Observable<OrderProductRemark> viewComments(@QueryMap Map<String, Object> paramsMap);
    //产品沟通发送
    @POST("order/sendComments")
    Observable<Response> sendComments(@QueryMap Map<String, Object> paramsMap);
    //
    @POST("order/save_order_head_go_list")
    Observable<Response> save_order_head_go_list(@QueryMap Map<String, Object> paramsMap);
    //修改供应商
    @POST("order/changeProvider")
    Observable<Response> changeProvider(@QueryMap Map<String, Object> paramsMap);
    //
    @POST("order/save_go")
    Observable<ResponseOrder> save_go(@QueryMap Map<String, Object> paramsMap);

    //
    @POST("order/reTransfer")
    Observable<Response> reTransfer(@QueryMap Map<String, Object> paramsMap);
    //新增采购
    @POST("order/save_go_self")
    Observable<ResponseOrder> save_go_self(@QueryMap Map<String, Object> paramsMap);
    //去单采购记录
    @GET("order/go_preAddOrder_lists")
    Observable<PurchaseProduct> go_preAddOrder_lists(@QueryMap Map<String, Object> paramsMap);
   //去单
   @GET("order/listJson_going")
   Observable<GoOrderDown> listJson_going(@QueryMap Map<String, Object> paramsMap);
    //组合下单
    @POST("order/makeOrderGoCombination")
    Observable<Response> makeOrderGoCombination(@QueryMap Map<String, Object> paramsMap);
    //删除去单产品
    @POST("order/delete_order_product_go")
    Observable<ResponseDelete> delete_order_product_go(@QueryMap Map<String, Object> paramsMap);
    //保存去单
    @POST("order/save_order_prod_go")
    Observable<ResponseOrder> save_order_prod_go(@QueryMap Map<String, Object> paramsMap);

    /***
     * 任务反馈模块
     */
    //任务详情
    @GET("processFeedback/listFeedbackingTask")
    Observable<FeedBackingTask> listFeedbackingTask(@QueryMap Map<String, Object> paramsMap);
    //历史反馈
    @GET("processFeedback/listWorkedTask")
    Observable<OrderProduct> listWorkedTask(@QueryMap Map<String, Object> paramsMap);
    //我的任务
    @GET("processFeedback/listPlanedTask")
    Observable<OrderProduct> listPlanedTask(@QueryMap Map<String, Object> paramsMap);
    //待分配的任务
    @GET("processFeedback/listTask")
    Observable<OrderProduct> listTask(@QueryMap Map<String, Object> paramsMap);
    //任务反馈保存save
    @POST("processFeedback/save")
    Observable<Response> processFeedbackSave(@QueryMap Map<String, Object> paramsMap);


    //类型
    @GET("Dict/listValueByType")
    Observable<TypeCateGory> listValueByType(@QueryMap Map<String, Object> paramsMap);


    /***
     * 任务分工
     */
    //任务分配 "
    @GET("workPlan/searchWorkPlan")
    Observable<WorkPlaned> searchWorkPlan(@QueryMap Map<String, Object> paramsMap);
    //历史分配 listCanPlanTask
    @GET("workPlan/listCanPlanTask")
    Observable<OrderProduct> listCanPlanTask(@QueryMap Map<String, Object> paramsMap);
    //
    @GET("workPlan/listPlanTaskPage")
    Observable<WorkPlanToAllocation> listPlanTaskPage(@QueryMap Map<String, Object> paramsMap);
    //
    @GET("workPlan/saveWorkPlan")
    Observable<ResponseAllocation> saveWorkPlan(@QueryMap Map<String, Object> paramsMap);
    /***
     * 公司管理模块
     */
    //公司详情

    @GET("customer/info")
    Observable<SingleCustomer> customerInfo(@QueryMap Map<String, Object> paramsMap);
    //客户列表
    @GET("customer/partnerList")
    Observable<ManyCustomer> customerList(@QueryMap Map<String, Object> paramsMap);
    //公司信誉
    @GET("customer/companyAreview")
    Observable<SingleCustomer> companyAreview(@QueryMap Map<String, Object> paramsMap);
    //公司信誉
    @GET("customer/companyBreview")
    Observable<SingleCustomer> companyBreview(@QueryMap Map<String, Object> paramsMap);
    //公司产品
    @GET("customer/productList")
    Observable<CompanyProduct> productList(@QueryMap Map<String, Object> paramsMap);
    @GET("review/allReview")
    Observable<AllReview> allReview(@QueryMap Map<String, Object> paramsMap);

    //公司产品属性
    @GET("customer/getAttributes")
    Observable<ProductAttributes> getCompanyProductAttributes(@QueryMap Map<String, Object> paramsMap);
    //是否存在订单关联
    @GET("partner/CanRemove")
    Observable<Response> canRemove(@QueryMap Map<String, Object> paramsMap);
    //
    @POST("notify/updateStatus")
    Observable<Response> updateStatus(@QueryMap Map<String, Object> paramsMap);

    //
    @GET("notify/getOffice")
    Observable<NotifyOffice> getOffice(@QueryMap Map<String, Object> paramsMap);
     //客户列表
     @GET("customer/list")
     Observable<ManyCustomer> list(@QueryMap Map<String, Object> paramsMap);
    //供应商列表
    @GET("customer/list")
    Observable<ManyCustomer> supplierList(@QueryMap Map<String, Object> paramsMap);
    //同意上家申请                     0
	//同意解除上家关系                 1
    @POST("customer/customerAgree")
    Observable<Response> customerAgree(@QueryMap Map<String, Object> paramsMap);
     //拒绝上家申请0
    //拒绝解除上家1
    @POST("partner/customerRemoveNo")
    Observable<Response> customerRemoveNo(@QueryMap Map<String, Object> paramsMap);
    //上家申请0
    //上家申请解除1
    @POST("partner/customerApply")
    Observable<Response> customerApply(@QueryMap Map<String, Object> paramsMap);
    //客户创建
    @POST("customer/apply")
    Observable<Response> customerSave(@QueryMap Map<String, Object> paramsMap);
    //客户匹配
    @GET("customer/findNameA")
    Observable<Office> findNameA(@QueryMap Map<String, Object> paramsMap);
    //供应商匹配
    @GET("customer/findNameB")
    Observable<Office> findNameB(@QueryMap Map<String, Object> paramsMap);

    //
    @POST("partner/nextHomeReject")
    Observable<Response> nextHomeReject(@QueryMap Map<String, Object> paramsMap);

       //
    @POST("customer/nextHomeAgree")
    Observable<Response> nextHomeAgree(@QueryMap Map<String, Object> paramsMap);
    //
    @POST("customer/nextHomeApply")
    Observable<Response> nextHomeApply(@QueryMap Map<String, Object> paramsMap);

    //


    //供应商列表
    @GET("partner/list")
    Observable<ManyCustomer> partners(@QueryMap Map<String, Object> paramsMap);
    //供应商信息
    @GET("partner/info")
    Observable<SingleCustomer> partnerInfo(@QueryMap Map<String, Object> paramsMap);
    //大搜
    @GET("search/bigSearch")
    Observable<BigSeekContent> bigHomeSearch(@QueryMap Map<String, Object> paramsMap);

    @GET("search/bigSearch")
    Observable<CompanyCateGory> productBigSearch(@QueryMap Map<String, Object> paramsMap);

    @GET("search/bigSearch")
    Observable<Company> companyMore(@QueryMap Map<String, Object> paramsMap);

    @GET("search/bigSearch")
    Observable<BidTender> bigSearch(@QueryMap Map<String, Object> paramsMap);

    /***
     * 通知模块
     */

    //
    @GET("notify/getRelateCompanyPersons")
    Observable<CompanyEmployee> getRelateCompanyPersons(@QueryMap Map<String, Object> paramsMap);

    //notify/draftSend

    @GET("notify/draftSend")
    Observable<Response> draftSend(@QueryMap Map<String, Object> paramsMap);

    @GET("notify/notifySend")
    Observable<Response> notifySend(@QueryMap Map<String, Object> paramsMap);

    //notifyEdit
    @GET("notify/notifyEdit")
    Observable<DraftContent> notifyEdit(@QueryMap Map<String, Object> paramsMap);

    //收件箱
    @Headers("Cache-Control: public, max-age=5")
    @GET("notify/filter")
    Observable<Notify> filter(@QueryMap Map<String, Object> paramsMap);
    //发件箱
    @Headers("Cache-Control: public, max-age=5")
    @GET("notify/myList")
    Observable<Notify> myList(@QueryMap Map<String, Object> paramsMap);
    //用户信息
    @Headers("Cache-Control: public, max-age=3600")
    @GET("user")
    Observable<LoginResult> user(@QueryMap Map<String, Object> paramsMap);

    //更新密码
    @GET("passwordUpdata")
    Observable<Response> passwordUpdata(@QueryMap Map<String, Object> paramsMap);

    //文件下載
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
    //文件上传
     @Multipart
     @POST("upload")
     Observable<ResponseBody> uploadFiles(
             @Path("url") String url,
             @Part("filename") String description,
             @PartMap() Map<String, RequestBody> maps);
    //注册短信验证
    @GET("customerContact/phoneQuery")
    Observable<BaseResponse<Integer>> phoneQuery(@QueryMap Map<String, Object> paramsMap);
    //手机短信验证 忘记密码
    @GET("customerContact/phoneCheck")
    Observable<BaseResponse<Integer>> phoneCheck(@QueryMap Map<String, Object> paramsMap);

    //手机账号验证
    @GET("customerContact/accountCheck")
    Observable<BaseResponse<User>> accountCheck(@QueryMap Map<String, Object> paramsMap);
    //手机绑定验证
    @GET("customerContact/mobileCheck")
    Observable<BaseResponse<User>> mobileCheck(@QueryMap Map<String, Object> paramsMap);
    //注册账号
    @POST("customerContact/saveOffice")
    Observable<BaseResponse<String>> officeSave(@QueryMap Map<String, Object> paramsMap);
}
