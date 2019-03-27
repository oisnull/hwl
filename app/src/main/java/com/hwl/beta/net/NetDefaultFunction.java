package com.hwl.beta.net;

/**
 * Created by Administrator on 2019/3/26.
 */
public class NetDefaultFunction<T> implements Function<ResponseBase<T>, T> {

	@Override
    public T apply(ResponseBase<T> response) throws Exception {
		if(response==null)
			throw new Exception("Response stream is empty");
			
		if(response.getResponseHead()==null)
			throw new Exception("Response head is empty");
	
		if(response.getResponseHead().getResultCode().equals(NetConstant.RESPONSE_SUCCESS))
			return response.getResponseBody();
		else if (head.getResultCode().equals(NetConstant.RESPONSE_RELOGIN))
			throw new NetException("Login timeout");
		else
			throw new Exception(getFailedMessage(response));
    }

	private String getFailedMessage(ResponseBase<T> response){
		if (StringUtils.isBlank(response.getResponseHead().getResultMessage())){
			return "Request data failed";
		}

		return response.getResponseHead().getResultMessage();
	}
}
