package melon.mybatis.po;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @user: melon.zhao
 * @date: 2018/7/31
 */
@Table
public class Customer {
    private static final long serialVersionUID = -3409169341159226529L;

    @Id
    private String id="";
    private long sequence;
    private String crmUserId="";

    private String phone="";

    //名称
    private String name="";

    //店铺
    private String shopCode="";

    //集团
    private Integer siteId;

    //性别/0-男/1-女
    private Integer sex;

    //来源
    private String source="";

    //一级类别来源
    private String firstSource;

    //来源不能为空
    //二级类别来源
    private String secondSource;

    //三级类别来源
    private String thirdSource;

    //dd来源
    private String ddSource="";

    //备胎手机
    private String backupPhone="";

    //身份证号码
    private String identificationNumber="";

    //QQ
    private String qq="";

    //微信
    private String wechat="";

    //省份
    private String province="";

    //城市
    private String city="";

    //地区
    private String district="";

    //生日
    private long birthday;

    //备注
    private String remark="";

    //支付宝账号
    private String alipayAccount;

    //淘宝账号
    private String taobaoAccount;

    //地址
    private String address;

    //操作人
    private String operator="";

    //归属销售
    private String belongSales="";

    //归属评估师
    private String belongAppraiser="";

    //卖家标签/1有
    private int sellerLable;

    //coc客户等级
    private String cocLevel;

    /**
     * 用于订单取消后，将客户级别修改会下单前状态
     */
    //下单前客户级别
    private String beforeOrderLevel="";

    //客户级别
    private String level="";

    private String creator="";

    private int createType;

    private String ext;

    //用户轨迹的最后跟进时间
    private long followTime;

    private Date levelChangeTime;

    private Date cocFollowTime;//coc同步客户跟进时间

    private int isArriveShop;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public String getCrmUserId() {
        return crmUserId;
    }

    public void setCrmUserId(String crmUserId) {
        this.crmUserId = crmUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDdSource() {
        return ddSource;
    }

    public void setDdSource(String ddSource) {
        this.ddSource = ddSource;
    }

    public String getBackupPhone() {
        return backupPhone;
    }

    public void setBackupPhone(String backupPhone) {
        this.backupPhone = backupPhone;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }


    public Date getLevelChangeTime() {
        return levelChangeTime;
    }

    public void setLevelChangeTime(Date levelChangeTime) {
        this.levelChangeTime = levelChangeTime;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }



    public String getBelongAppraiser() {
        return belongAppraiser;
    }

    public void setBelongAppraiser(String belongAppraiser) {
        this.belongAppraiser = belongAppraiser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }


    /**
     * 等级为无效，销售归属置为空
     * @return
     */
    public String getBelongSales() {
        return belongSales;
    }

    public void setBelongSales(String belongSales) {
        this.belongSales = belongSales;
    }

    public int getSellerLable() {
        return sellerLable;
    }

    public void setSellerLable(int sellerLable) {
        this.sellerLable = sellerLable;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getTaobaoAccount() {
        return taobaoAccount;
    }

    public void setTaobaoAccount(String taobaoAccount) {
        this.taobaoAccount = taobaoAccount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCreateType() {
        return createType;
    }

    public void setCreateType(int createType) {
        this.createType = createType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getIsArriveShop() {
        return isArriveShop;
    }

    public void setIsArriveShop(int isArriveShop) {
        this.isArriveShop = isArriveShop;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getFirstSource() {
        return firstSource;
    }

    public void setFirstSource(String firstSource) {
        this.firstSource = firstSource;
    }

    public String getSecondSource() {
        return secondSource;
    }

    public void setSecondSource(String secondSource) {
        this.secondSource = secondSource;
    }

    public String getThirdSource() {
        return thirdSource;
    }

    public void setThirdSource(String thirdSource) {
        this.thirdSource = thirdSource;
    }

    public String getBeforeOrderLevel() {
        return beforeOrderLevel;
    }

    public void setBeforeOrderLevel(String beforeOrderLevel) {
        this.beforeOrderLevel = beforeOrderLevel;
    }

    public String getCocLevel() {
        return cocLevel;
    }

    public void setCocLevel(String cocLevel) {
        this.cocLevel = cocLevel;
    }

    public Date getCocFollowTime() {
        return cocFollowTime;
    }

    public void setCocFollowTime(Date cocFollowTime) {
        this.cocFollowTime = cocFollowTime;
    }

}
