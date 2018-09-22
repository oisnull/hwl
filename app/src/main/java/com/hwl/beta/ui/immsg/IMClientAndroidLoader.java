//package com.hwl.beta.ui.immsg;
//
//import com.hwl.im.client.IMClientListener;
//
//import java.util.logging.Logger;
//
//public class IMClientAndroidLoader {
//
//    /**
//     * Process : 1,(app start|app disconnect network and reconnect) and connect im
//     * server 2,connect im server success and send user validate message
//     *
//     *
//     * Function list :
//     *
//     *
//     */
//
//    static Logger log = Logger.getLogger(IMClientAndroidLoader.class.getName());
//    static Long userId = 10000L;
//    static String userToken = "123456";
//
//    public static void autoConnect() {
////        log.debug("Client listen : im loader auto connect {} start ...", IMClientEntry.getServerAddress());
//        IMClientEntry.registerClientListener(clientListener);
//        IMClientEntry.connectServer();
//    }
//
//    static IMClientEntry.DefaultSendOperateListener operateListener = new IMClientEntry.DefaultSendOperateListener() {
//
//        @Override
//        public void success() {
//            // nothing do it
////            log.debug("Client listen :  send message success.");
//        }
//
//        public void unconnect() {
////            log.debug("Client listen : client unconnect to server ");
//        }
//
//        public void notSendToServer() {
////            log.debug("Client listen : message not send to server ");
//        }
//
//        public void failed(String message) {
//            // call ui for android
////            log.debug("Client listen : client send operate failed : {}", message);
//        }
//
//        public void sessionidInvaild() {
////            log.debug("Client listen : sessionid invalid");
//        }
//    };
//
//    static IMClientListener clientListener = new IMClientListener() {
//        @Override
//        public void onBuildConnectionSuccess(String clientAddress, String serverAddress) {
////            log.debug("Client listen : client {} connected to server {} successfully.", clientAddress, serverAddress);
//            IMClientEntry.stopCheckConnect();
////            log.debug("Client listen : send user validate message userid({}) usertoken({}) start.", userId, userToken);
//            IMClientEntry.sendUserValidateMessage(userId, userToken, operateListener);
//        }
//
//        @Override
//        public void onBuildConnectionError(String clientAddress, String serverAddress, String errorInfo) {
////            log.error("Client listen : client {} connected to server {} failure. info :", clientAddress, serverAddress,
////                    errorInfo);
//            IMClientEntry.startCheckConnect();
//        }
//
//        @Override
//        public void onClosed(String clientAddress) {
////            log.debug("Client listen : client {} closed", clientAddress);
//            IMClientEntry.startCheckConnect();
//        }
//
//        @Override
//        public void onDisconnected(String clientAddress) {
////            log.debug("Client listen : client {} disconnect", clientAddress);
//            IMClientEntry.startCheckConnect();
//        }
//
//        @Override
//        public void onError(String clientAddress, String errorInfo) {
////            log.error("Client listen : an error occurred on the client {}. info : {}", clientAddress, errorInfo);
//            IMClientEntry.startCheckConnect();
//        }
//    };
//
//}