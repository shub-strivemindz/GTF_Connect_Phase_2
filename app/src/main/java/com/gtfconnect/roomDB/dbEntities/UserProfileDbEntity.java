package com.gtfconnect.roomDB.dbEntities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gtfconnect.models.ProfileResponseModel;

import java.util.List;

@Entity(tableName = "user_profile_info_data")
public class UserProfileDbEntity {


    @PrimaryKey(autoGenerate = true)
    int keyID;

    @SerializedName("profile_info")
    @Expose
    private ProfileInfo profileInfo;
    @SerializedName("user_role_info")
    @Expose
    private UserRoleInfo userRoleInfo;
    @SerializedName("user_permission")
    @Expose
    private List<UserPermission> userPermission;
    @SerializedName("user_setting")
    @Expose
    private List<UserSetting> userSetting;

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public void setProfileInfo(ProfileInfo profileInfo) {
        this.profileInfo = profileInfo;
    }

    public UserRoleInfo getUserRoleInfo() {
        return userRoleInfo;
    }

    public void setUserRoleInfo(UserRoleInfo userRoleInfo) {
        this.userRoleInfo = userRoleInfo;
    }

    public List<UserPermission> getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(List<UserPermission> userPermission) {
        this.userPermission = userPermission;
    }

    public List<UserSetting> getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(List<UserSetting> userSetting) {
        this.userSetting = userSetting;
    }


    public class CityList {

        @SerializedName("CityID")
        @Expose
        private Integer cityID;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("StateID")
        @Expose
        private Integer stateID;

        public Integer getCityID() {
            return cityID;
        }

        public void setCityID(Integer cityID) {
            this.cityID = cityID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getStateID() {
            return stateID;
        }

        public void setStateID(Integer stateID) {
            this.stateID = stateID;
        }

    }
    public class CountryList {

        @SerializedName("CountryID")
        @Expose
        private Integer countryID;
        @SerializedName("sortname")
        @Expose
        private String sortname;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("nicename")
        @Expose
        private String nicename;
        @SerializedName("phonecode")
        @Expose
        private Integer phonecode;

        public Integer getCountryID() {
            return countryID;
        }

        public void setCountryID(Integer countryID) {
            this.countryID = countryID;
        }

        public String getSortname() {
            return sortname;
        }

        public void setSortname(String sortname) {
            this.sortname = sortname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNicename() {
            return nicename;
        }

        public void setNicename(String nicename) {
            this.nicename = nicename;
        }

        public Integer getPhonecode() {
            return phonecode;
        }

        public void setPhonecode(Integer phonecode) {
            this.phonecode = phonecode;
        }

    }

    public class Permission {

        @SerializedName("PermissionID")
        @Expose
        private Integer permissionID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Group")
        @Expose
        private String group;

        public Integer getPermissionID() {
            return permissionID;
        }

        public void setPermissionID(Integer permissionID) {
            this.permissionID = permissionID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

    }
    public class Primary {

        @SerializedName("UserRoleID")
        @Expose
        private Integer userRoleID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("RoleID")
        @Expose
        private Integer roleID;
        @SerializedName("IsPrimary")
        @Expose
        private Integer isPrimary;
        @SerializedName("role")
        @Expose
        private Role role;

        public Integer getUserRoleID() {
            return userRoleID;
        }

        public void setUserRoleID(Integer userRoleID) {
            this.userRoleID = userRoleID;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public Integer getRoleID() {
            return roleID;
        }

        public void setRoleID(Integer roleID) {
            this.roleID = roleID;
        }

        public Integer getIsPrimary() {
            return isPrimary;
        }

        public void setIsPrimary(Integer isPrimary) {
            this.isPrimary = isPrimary;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

    }
    public class ProfileInfo {

        @SerializedName("GTFUserID")
        @Expose
        private Integer gTFUserID;
        @SerializedName("UniqueID")
        @Expose
        private Object uniqueID;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("PhoneCode")
        @Expose
        private String phoneCode;
        @SerializedName("Phone")
        @Expose
        private String phone;
        @SerializedName("Firstname")
        @Expose
        private String firstname;
        @SerializedName("Lastname")
        @Expose
        private String lastname;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Gender")
        @Expose
        private String gender;
        @SerializedName("DOB")
        @Expose
        private String dob;
        @SerializedName("AdditionalInfo")
        @Expose
        private String additionalInfo;
        @SerializedName("GST")
        @Expose
        private String gst;
        @SerializedName("CompanyName")
        @Expose
        private String companyName;
        @SerializedName("State")
        @Expose
        private String state;
        @SerializedName("Country")
        @Expose
        private String country;
        @SerializedName("Pincode")
        @Expose
        private Integer pincode;
        @SerializedName("ReferralCode")
        @Expose
        private String referralCode;
        @SerializedName("City")
        @Expose
        private String city;
        @SerializedName("ProfileImage")
        @Expose
        private String profileImage;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("IsBlocked")
        @Expose
        private Integer isBlocked;
        @SerializedName("UserType")
        @Expose
        private String userType;
        @SerializedName("find_us")
        @Expose
        private String findUs;
        @SerializedName("find_us_other_text")
        @Expose
        private String findUsOtherText;
        @SerializedName("IsLogging")
        @Expose
        private Integer isLogging;
        @SerializedName("CreatedAt")
        @Expose
        private String createdAt;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("ProfileThumbnail")
        @Expose
        private String profileThumbnail;
        @SerializedName("country")
        @Expose
        private CountryList countryList;
        @SerializedName("state")
        @Expose
        private StateList stateList;
        @SerializedName("city")
        @Expose
        private CityList cityList;

        public Integer getGTFUserID() {
            return gTFUserID;
        }

        public void setGTFUserID(Integer gTFUserID) {
            this.gTFUserID = gTFUserID;
        }

        public Object getUniqueID() {
            return uniqueID;
        }

        public void setUniqueID(Object uniqueID) {
            this.uniqueID = uniqueID;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(String phoneCode) {
            this.phoneCode = phoneCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getAdditionalInfo() {
            return additionalInfo;
        }

        public void setAdditionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
        }

        public String getGst() {
            return gst;
        }

        public void setGst(String gst) {
            this.gst = gst;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Integer getPincode() {
            return pincode;
        }

        public void setPincode(Integer pincode) {
            this.pincode = pincode;
        }

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getIsBlocked() {
            return isBlocked;
        }

        public void setIsBlocked(Integer isBlocked) {
            this.isBlocked = isBlocked;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getFindUs() {
            return findUs;
        }

        public void setFindUs(String findUs) {
            this.findUs = findUs;
        }

        public String getFindUsOtherText() {
            return findUsOtherText;
        }

        public void setFindUsOtherText(String findUsOtherText) {
            this.findUsOtherText = findUsOtherText;
        }

        public Integer getIsLogging() {
            return isLogging;
        }

        public void setIsLogging(Integer isLogging) {
            this.isLogging = isLogging;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public String getProfileThumbnail() {
            return profileThumbnail;
        }

        public void setProfileThumbnail(String profileThumbnail) {
            this.profileThumbnail = profileThumbnail;
        }

        public CountryList getCountryList() {
            return countryList;
        }

        public void setCountryList(CountryList countryList) {
            this.countryList = countryList;
        }

        public StateList getStateList() {
            return stateList;
        }

        public void setStateList(StateList stateList) {
            this.stateList = stateList;
        }

        public CityList getCityList() {
            return cityList;
        }

        public void setCityList(CityList cityList) {
            this.cityList = cityList;
        }

    }
    public class Role {

        @SerializedName("RoleID")
        @Expose
        private Integer roleID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Slug")
        @Expose
        private String slug;
        @SerializedName("role_permissions")
        @Expose
        private List<RolePermission> rolePermissions;

        public Integer getRoleID() {
            return roleID;
        }

        public void setRoleID(Integer roleID) {
            this.roleID = roleID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public List<RolePermission> getRolePermissions() {
            return rolePermissions;
        }

        public void setRolePermissions(List<RolePermission> rolePermissions) {
            this.rolePermissions = rolePermissions;
        }

    }
    public class RolePermission {

        @SerializedName("RolePermissionID")
        @Expose
        private Integer rolePermissionID;
        @SerializedName("RoleID")
        @Expose
        private Integer roleID;
        @SerializedName("PermissionID")
        @Expose
        private Integer permissionID;
        @SerializedName("PermissionValue")
        @Expose
        private Integer permissionValue;
        @SerializedName("permission")
        @Expose
        private Permission permission;

        public Integer getRolePermissionID() {
            return rolePermissionID;
        }

        public void setRolePermissionID(Integer rolePermissionID) {
            this.rolePermissionID = rolePermissionID;
        }

        public Integer getRoleID() {
            return roleID;
        }

        public void setRoleID(Integer roleID) {
            this.roleID = roleID;
        }

        public Integer getPermissionID() {
            return permissionID;
        }

        public void setPermissionID(Integer permissionID) {
            this.permissionID = permissionID;
        }

        public Integer getPermissionValue() {
            return permissionValue;
        }

        public void setPermissionValue(Integer permissionValue) {
            this.permissionValue = permissionValue;
        }

        public Permission getPermission() {
            return permission;
        }

        public void setPermission(Permission permission) {
            this.permission = permission;
        }

    }
    public class StateList {

        @SerializedName("StateID")
        @Expose
        private Integer stateID;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("CountryID")
        @Expose
        private Integer countryID;
        @SerializedName("GstCode")
        @Expose
        private String gstCode;
        @SerializedName("StateCode")
        @Expose
        private String stateCode;

        public Integer getStateID() {
            return stateID;
        }

        public void setStateID(Integer stateID) {
            this.stateID = stateID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCountryID() {
            return countryID;
        }

        public void setCountryID(Integer countryID) {
            this.countryID = countryID;
        }

        public String getGstCode() {
            return gstCode;
        }

        public void setGstCode(String gstCode) {
            this.gstCode = gstCode;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

    }
    public class UserRoleInfo {

        @SerializedName("primary")
        @Expose
        private Primary primary;
        @SerializedName("additional")
        @Expose
        private List<Object> additional;

        public Primary getPrimary() {
            return primary;
        }

        public void setPrimary(Primary primary) {
            this.primary = primary;
        }

        public List<Object> getAdditional() {
            return additional;
        }

        public void setAdditional(List<Object> additional) {
            this.additional = additional;
        }

    }
    public class UserSetting {

        @SerializedName("SettingID")
        @Expose
        private Integer settingID;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("NameText")
        @Expose
        private String nameText;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("TypeText")
        @Expose
        private String typeText;
        @SerializedName("Category")
        @Expose
        private String category;
        @SerializedName("CategoryText")
        @Expose
        private String categoryText;
        @SerializedName("SettingValue")
        @Expose
        private String settingValue;

        public Integer getSettingID() {
            return settingID;
        }

        public void setSettingID(Integer settingID) {
            this.settingID = settingID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameText() {
            return nameText;
        }

        public void setNameText(String nameText) {
            this.nameText = nameText;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeText() {
            return typeText;
        }

        public void setTypeText(String typeText) {
            this.typeText = typeText;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategoryText() {
            return categoryText;
        }

        public void setCategoryText(String categoryText) {
            this.categoryText = categoryText;
        }

        public String getSettingValue() {
            return settingValue;
        }

        public void setSettingValue(String settingValue) {
            this.settingValue = settingValue;
        }

    }

    public class UserPermission {

        @SerializedName("UserPermissionID")
        @Expose
        private Integer userPermissionID;
        @SerializedName("UserID")
        @Expose
        private Integer userID;
        @SerializedName("PermissionID")
        @Expose
        private Integer permissionID;
        @SerializedName("CustomValue")
        @Expose
        private String customValue;
        @SerializedName("permission")
        @Expose
        private Permission permission;

        public Integer getUserPermissionID() {
            return userPermissionID;
        }

        public void setUserPermissionID(Integer userPermissionID) {
            this.userPermissionID = userPermissionID;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }

        public Integer getPermissionID() {
            return permissionID;
        }

        public void setPermissionID(Integer permissionID) {
            this.permissionID = permissionID;
        }

        public String getCustomValue() {
            return customValue;
        }

        public void setCustomValue(String customValue) {
            this.customValue = customValue;
        }

        public Permission getPermission() {
            return permission;
        }

        public void setPermission(Permission permission) {
            this.permission = permission;
        }

    }
}



