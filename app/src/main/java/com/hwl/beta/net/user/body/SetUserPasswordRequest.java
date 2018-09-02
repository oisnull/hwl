package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/14.
 */

public class SetUserPasswordRequest {
        private String Email;
        private String Mobile;
        private String Password;
        private String PasswordOK;
        private String CheckCode;

        public String getEmail() {
                return Email;
        }

        public void setEmail(String email) {
                Email = email;
        }

        public String getMobile() {
                return Mobile;
        }

        public void setMobile(String mobile) {
                Mobile = mobile;
        }

        public String getPassword() {
                return Password;
        }

        public void setPassword(String password) {
                Password = password;
        }

        public String getPasswordOK() {
                return PasswordOK;
        }

        public void setPasswordOK(String passwordOK) {
                PasswordOK = passwordOK;
        }

        public String getCheckCode() {
                return CheckCode;
        }

        public void setCheckCode(String checkCode) {
                CheckCode = checkCode;
        }
}
