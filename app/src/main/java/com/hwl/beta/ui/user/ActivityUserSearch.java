//package com.hwl.beta.ui.user;
//
//import android.app.Activity;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityUserSearchBinding;
//import com.hwl.beta.mq.send.UserMessageSend;
//import com.hwl.beta.net.user.UserSearchInfo;
//import com.hwl.beta.net.user.UserService;
//import com.hwl.beta.net.user.body.SearchUserResponse;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.KeyBoardAction;
//import com.hwl.beta.ui.common.rxext.MQDefaultObserver;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.dialog.AddFriendDialogFragment;
//import com.hwl.beta.ui.dialog.LoadingDialog;
//import com.hwl.beta.ui.user.action.IUserSearchItemListener;
//import com.hwl.beta.ui.user.action.IUserSearchListener;
//import com.hwl.beta.ui.user.adp.UserSearchAdapter;
//import com.hwl.beta.ui.user.bean.UserSearchBean;
//import com.hwl.beta.utils.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Administrator on 2018/1/27.
// */
//
//public class ActivityUserSearch extends BaseActivity {
//    Activity activity;
//    ActivityUserSearchBinding binding;
//    UserSearchBean userSearchBean;
//    UserSearchListener userSearchListener;
//    UserSearchAdapter userAdapter;
//    List<UserSearchInfo> users;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        activity = this;
//        userSearchBean = new UserSearchBean();
//        userSearchListener = new UserSearchListener();
//        users = new ArrayList<>();
//        userAdapter = new UserSearchAdapter(this, users, new UserSearchItemListener());
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_search);
//        binding.setSearchBean(userSearchBean);
//        binding.setAction(userSearchListener);
//        binding.setSearchAdapter(userAdapter);
//
//        userSearchListener.initView();
//        userSearchListener.loadUsers();
//    }
//
//    public class UserSearchListener implements IUserSearchListener {
//
//        private boolean isRuning = false;
//
//        @Override
//        public void initView() {
//            binding.pbLoading.setVisibility(View.GONE);
//            binding.tvShow.setVisibility(View.GONE);
//        }
//
//        @Override
//        public void loadUsers() {
//
//        }
//
//        @Override
//        public void onBackClick() {
//            onBackPressed();
//        }
//
//        @Override
//        public void onSearchClick() {
//            if (isRuning) {
//                return;
//            }
//            isRuning = true;
//
//            if (StringUtils.isBlank(userSearchBean.getUserSymbol())) {
//                isRuning = false;
//                return;
//            }
//            KeyBoardAction.hideSoftInput(activity);
//            binding.pbLoading.setVisibility(View.VISIBLE);
//            UserService.searchUser(userSearchBean.getUserSymbol())
//                    .subscribe(new NetDefaultObserver<SearchUserResponse>() {
//                        @Override
//                        protected void onSuccess(SearchUserResponse response) {
//                            isRuning = false;
//                            binding.pbLoading.setVisibility(View.GONE);
//                            if (response != null && response.getUserInfos() != null && response.getUserInfos().size() > 0) {
//                                users.clear();
//                                users.addAll(response.getUserInfos());
//                                userAdapter.notifyDataSetChanged();
//                                binding.tvShow.setVisibility(View.GONE);
//
//                            } else {
//                                binding.tvShow.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            isRuning = false;
//                            binding.tvShow.setVisibility(View.GONE);
//                            binding.pbLoading.setVisibility(View.GONE);
//                        }
//                    });
//        }
//    }
//
//    public class UserSearchItemListener implements IUserSearchItemListener {
//        private AddFriendDialogFragment addFriendDialogFragment;
//
//        @Override
//        public void onHeadImageClick(UserSearchInfo user) {
//
//        }
//
//        //用户添加好友需要发送一条请求验证的消息
//        //好友验证通过需要请求net api，将添加成功的好友数据存储到数据库中
//        //只有当好友验证通过了，再发送一条验证通过的消息到自己
//        @Override
//        public void onAddClick(final View view, final UserSearchInfo user) {
//            if (addFriendDialogFragment == null) {
//                addFriendDialogFragment = new AddFriendDialogFragment();
//            }
//            final String remark = "我是 " + UserSP.getUserShowName();
//            addFriendDialogFragment.setRemark(remark);
//            addFriendDialogFragment.setTitle(user.getShowName());
//            addFriendDialogFragment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//                    LoadingDialog.show(activity, "请求发送中...");
//                    KeyBoardAction.hideSoftInput(activity);
//
//                    UserMessageSend.sendFriendRequestMessage(user.getId(), remark).subscribe(new MQDefaultObserver() {
//                        @Override
//                        protected void onSuccess() {
//                            view.setVisibility(View.GONE);
//                            Toast.makeText(activity, "好友请求发送成功", Toast.LENGTH_SHORT).show();
//                            addFriendDialogFragment.dismiss();
//                            LoadingDialog.hide();
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            LoadingDialog.hide();
//                        }
//                    });
//                }
//            });
//            addFriendDialogFragment.show(getFragmentManager(), "AddFriendDialogFragment");
//        }
//    }
//
//    //    EditText etUserKey;
////    Button btnSearch, btnAddFriend;
////    ListView lvUsers;
////    TextView tvShow;
////    ProgressDialog progressDialog;
////    AddFriendDialogFragment addFriendDialogFragment;
////    UserSearchAdapter userAdapter;
////
////    @Override
////    public String getName() {
////        return null;
////    }
////
////    @Override
////    public int getLayoutId() {
////        return R.layout.activity_user_search;
////    }
////
////    private Handler handler = new Handler() {
////        @Override
////        public void handleMessage(Message msg) {
////            super.handleMessage(msg);
////            switch (msg.what) {
////                case 1:
////                    if (addFriendDialogFragment != null) {
////                        addFriendDialogFragment.dismiss();
////                    }
////                    if (btnAddFriend != null) {
////                        btnAddFriend.setVisibility(View.GONE);
////                    }
////                    if (progressDialog != null) {
////                        progressDialog.dismiss();
////                    }
////                    Toast.makeText(getApplicationContext(), "好友请求发送成功", Toast.LENGTH_SHORT).show();
////                    break;
////                case 0:
////                    if (progressDialog != null) {
////                        progressDialog.dismiss();
////                    }
////                    Toast.makeText(getApplicationContext(), msg.getData().getString("result"), Toast.LENGTH_SHORT).show();
////                    break;
////            }
////        }
////    };
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////    }
////
////    @Override
////    public void initView() {
////        ImageView ivBack = findViewById(R.id.iv_back);
////        ivBack.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                onBackPressed();
////            }
////        });
////        etUserKey = findViewById(R.id.et_user_key);
////        btnSearch = findViewById(R.id.btn_search);
////        lvUsers = findViewById(R.id.lv_users);
////        tvShow = findViewById(R.id.tv_show);
////
////        btnSearch.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                progressDialog = new ProgressDialog(context);
////                progressDialog.setMessage("搜索中,请稍后...");
////                progressDialog.show();
////                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
////                imm.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
////
////                searchUser();
////            }
////        });
////    }
////
////    private void searchUser() {
////        String key = etUserKey.getText() + "";
////        UserService.searchUser(key, new RetrofitCallBack() {
////            @Override
////            public void onResponse(ResponseBase responseBase) {
////                SearchUserResponse res = (SearchUserResponse) responseBase;
////                if (res != null && res.getUserInfos() != null && res.getUserInfos().size() > 0) {
////                    tvShow.setVisibility(View.GONE);
////                    userAdapter = new UserSearchAdapter(context, res.getUserInfos());
////                    userAdapter.setFriendEvent(new UserSearchAdapter.IFriendEvent() {
////                        @Override
////                        public void onAddClickListener(Button btn, int friendId, String friendName) {
////                            btnAddFriend = btn;
////                            showAddFriendDialog(friendId, friendName);
////                        }
////                    });
////                    lvUsers.setAdapter(userAdapter);
////                } else {
////                    tvShow.setVisibility(View.VISIBLE);
////                    lvUsers.setAdapter(null);
////                }
////                if (progressDialog != null) {
////                    progressDialog.dismiss();
////                }
////            }
////
////            @Override
////            public void onFailure(String resultCode, String resultMsg) {
////                if (progressDialog != null) {
////                    progressDialog.dismiss();
////                }
////                Toast.makeText(getApplicationContext(), resultMsg, Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onRelogin() {
////                relogin();
////            }
////        });
////    }
////
////    private void showAddFriendDialog(final int friendId, final String friendName) {
////        if (addFriendDialogFragment == null) {
////            addFriendDialogFragment = new AddFriendDialogFragment();
////        }
////        final String remark = "我是 " + UserSP.getUserName();
////        addFriendDialogFragment.setRemark(remark);
////        addFriendDialogFragment.setTitle(friendName);
////        addFriendDialogFragment.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                progressDialog = new ProgressDialog(context);
////                progressDialog.setMessage("请求发送中...");
////                progressDialog.show();
////                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
////                imm.hideSoftInputFromWindow(btnSearch.getWindowToken(), 0);
////
////                addFriend(friendId, remark);
////            }
////        });
////        addFriendDialogFragment.show(getFragmentManager(), "AddFriendDialogFragment");
////    }
////
////    //用户添加好友需要发送一条请求验证的消息
////    //好友验证通过需要请求net api，将添加成功的好友数据存储到数据库中
////    //只有当好友验证通过了，再发送一条验证通过的消息到自己
////    private void addFriend(final int friendId, final String remark) {
////        MQUserMessage.sendFriendRequestMessage(friendId, remark, new IMessageCallBack() {
////            @Override
////            public void onSuccess() {
////                Message msg = Message.obtain();
////                msg.what = 1;
////                handler.sendMessage(msg);
////            }
////
////            @Override
////            public void onFaild(String resultMsg) {
////                Message msg = Message.obtain();
////                Bundle bundle = new Bundle();
////                bundle.putString("result", resultMsg);
////                msg.setData(bundle);
////                msg.what = 0;
////                handler.sendMessage(msg);
////            }
////        });
////    }
//}
