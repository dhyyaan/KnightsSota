package com.think360.sotaknights.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.think360.sotaknights.model.TaskItem;

import java.util.List;

/**
 * Created by think360 on 09/10/17.
 */

public class Responce {

public class CompanyInfoResponce{
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("description")
    @Expose
    private String description;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    }
    public class ProfileResponce{
        public Boolean getStatus() {
            return status;
        }
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private Data data;
        public Data getData() {
            return data;
        }
        public void setData(Data data) {
            this.data = data;
        }


     public  class Data{
            public String getUser_id() {
                return user_id;
            }
            public String getShort_name() {
               return short_name;
           }
            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }
            public String getEmail() {
                return email;
            }
            public String getMobile() {
                return mobile;
            }
            public String getAddress() {
                return address;
            }
            public String getCity() {
                return city;
            }
            public String getState() {
                return state;
            }
            public String getBank_acc_no() {
                return bank_acc_no;
            }
            public String getBranch_name() {
                return branch_name;
            }
            public String getAdhaar() {
                return adhaar;
            }
            public String getPsara() {
                return psara;
            }
            public String getDrving() {
                return drving;
            }
            public String getGun() {
                return gun;
            }
            public String getAadhar_1() {
                return aadhar_1;
            }
            public String getPasara_1() {
                return pasara_1;
            }
            public String getDriving_2() {
                return driving_2;
            }
            public String getGun_1() {return gun_1;}
            public String getUser_name() {
             return user_name;
         }
            public String getSota_profile_pic() {return profile_pic;}
            public String getSota_business_trade() {return sota_business_trade;}
            public String getIfsc() {return ifsc;}

            @SerializedName("user_id")
            @Expose
            private String user_id;
            @SerializedName("short_name")
            @Expose
            private String short_name;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("state")
            @Expose
            private String state;
            @SerializedName("bank_acc_no")
            @Expose
            private String bank_acc_no;
            @SerializedName("branch_name")
            @Expose
            private String branch_name;
            @SerializedName("user_name")
            @Expose
            private String user_name;
            @SerializedName("adhaar")
            @Expose
            private String adhaar;
            @SerializedName("psara")
            @Expose
            private String psara;
            @SerializedName("drving")
            @Expose
            private String drving;
            @SerializedName("gun")
            @Expose
            private String gun;
            @SerializedName("aadhar_1")
            @Expose
            private String aadhar_1;
            @SerializedName("pasara_1")
            @Expose
            private String pasara_1;
            @SerializedName("driving_2")
            @Expose
            private String driving_2;
            @SerializedName("gun_1")
            @Expose
            private String gun_1;
            @SerializedName("profile_pic")
            @Expose
            private String profile_pic;
            @SerializedName("sota_business_trade")
            @Expose
            private String sota_business_trade;
            @SerializedName("ifsc")
            @Expose
            private String ifsc;
     }
    }
    public class SotaTaskResponce{

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private List<TaskItem> data = null;
        public List<TaskItem> getTaskList() {
            return data;
        }
        public Boolean getStatus() {
    return status;
}

    }
    public class LoginResponce{
        public Boolean getStatus() {
            return status;
        }
        public String getMessage() {
            return message;
        }
        public String getUserId() {
            return user_id;
        }

        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("user_id")
        @Expose
        private String user_id;

        public String getDevice_id() {
            return device_id;
        }

        @SerializedName("device_id")
        @Expose
        private String device_id;
    }
    public class StatusResponce{
        public Boolean getStatus() {
            return status;
        }
        public String getMessage() {
            return message;
        }

        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("message")
        @Expose
        private String message;
    }
    public class StateResponce{
        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public List<Data> getData() {
            return data;
        }

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private List<Data> data;

        public  class Data {
            @SerializedName("state_id")
            @Expose
            private String state_id;

            public String getState_id() {
                return state_id;
            }

            public String getState_name() {
                return state_name;
            }

            @SerializedName("state_name")
            @Expose
            private String state_name;
        }
        }


  public  class CityResponce{
        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private List<Data> data;

      public int getStatus() {
          return status;
      }

      public String getMessage() {
          return message;
      }

      public List<Data> getData() {
          return data;
      }





        public  class Data {

            public String getCity_id() {
                return city_id;
            }

            public String getCity_name() {
                return city_name;
            }

            @SerializedName("city_id")
            @Expose
            private String city_id;

            @SerializedName("city_name")
            @Expose
            private String city_name;

        }
    }
}
