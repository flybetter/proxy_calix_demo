package com.cn.proxy.server.ana.process;

import com.cn.proxy.server.ana.ANAConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ANAProcessResult {
	private static final Logger log = Logger.getLogger(ANAProcessResult.class);
	public static final boolean SUCCESS = true;
	public static final boolean FAILURE = false;
	private static final String LINE = "_____________________________________________________________";
	private static final String PROMPT = "Telnet Prompt:";
	private static final String COMMAND_STR = "Telnet Command: ";
	private static final String COMMAND_REPLY_STR = "Telnet Reply: ";
	private static final String USER_REMARK = "User Remark: ";
	private static final String PATTERN_STR = "\\S+";
	private static final Pattern PATTERN = Pattern.compile(PATTERN_STR);
	private boolean result;
	private StringBuffer logs = new StringBuffer("");

	//For Log
	private String username;
	private String ipaddress;
	private String deviceName;
	private String operate;
	private String service;
	private String serviceType;
	private String serviceName;
	private String lastResponse;
	//for socket response 
	private String returnMessage="";
	private String authMessage="";

	/**
	 * Creates a new instance of ANAProcessResult.
	 */
	public ANAProcessResult() {

	}

	/**
	 * Creates a new instance of ANAProcessResult.
	 * 
	 * @param username
	 * @param ipaddress
	 */
	public ANAProcessResult(String username, String ipaddress,String authMsg) {
		this.username = username;
		this.ipaddress = ipaddress;
		this.authMessage=authMsg;
	}
	
	public String getAuthMsg(){
		return this.authMessage;
	}

	/**
	 * @return the result
	 */
	public boolean getResult() {
		return result;
	}

	public String getResultStr() {
//		return result ? "SUCCESS" : "FAILURE";
		if(StringUtils.isBlank(returnMessage)){
			returnMessage= ANAConstants.AnaErrorCode.UNKNOWN_ERROR.getErrorMessage();
		}
		return returnMessage;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return the logs
	 */
	public String getLogs() {
		return logs.toString();
	}

	/**
	 * @param logs
	 *            the logs to set
	 */
	public void addLogs(String logs) {
		log.debug(logs);
		if (logs != null) {
			this.logs.append(logs.trim() + "\r\n");
		}
	}

	/**
	 * 
	 * @param command
	 * @param result
	 */
	public void addCommandResult(String command, String result) {
		addLogs(COMMAND_STR + command);
		lastResponse=handleResponse(result);
		addLogs(COMMAND_REPLY_STR + lastResponse);
		
	}
	
	public void addUserRemark(String remark){
		String str=handleResponse(remark);
		addLogs(USER_REMARK + str);
		this.returnMessage=str.replace("SUCCESS -- ", "").trim();
	}

	
	/**
	 * Add Prompt
	 * 
	 * @param prompt
	 */
	public void addPrompt(String prompt) {
		addLogs(PROMPT + handleResponse(prompt));
	}

	/**
	 * Handle Response to remove space and carriage return
	 * 
	 * @param str
	 * @return
	 */
	private String handleResponse(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		Matcher matcher = PATTERN.matcher(str);
		if (matcher.find()) {
			String tmp = matcher.group(0).trim();
			str = str.substring(str.indexOf(tmp));
		}
		matcher = null;
		return str;
	}

	/**
	 * 
	 * @param errorCode
	 */
	public void setErrorInfo(String errorCode) {
		setErrorInfo(errorCode, null, null);
	}
	
	public void setErrorInfo(ANAConstants.AnaErrorCode errorCode){
		setErrorInfo(errorCode.toString());
	}

	/**
	 * Set Error Info
	 * 
	 * @param errorCode
	 * @param errorMessage
	 */
	public void setErrorInfo(String errorCode, String errorMessage) {
		setErrorInfo(errorCode, errorMessage, null);
	}

	/**
	 * Add Logs when Begin
	 * 
	 * @param service
	 *            : serviceId
	 * @param serviceName
	 *            : serviceName
	 * @param serviceType
	 *            :service type
	 * @param operation
	 *            : operation
	 * @param deviceName
	 *            : device name
	 */
	public void addLogHead(String service, String serviceName,
			String serviceType, String operation, String deviceName) {
		addLogs("SERVICE = " + service);
		addLogs(LINE);
		addLogs("");
		addLogs(serviceName);
		addLogs(LINE);
		addLogs("");
		addLogs("SERVICE_TYPE=" + serviceType + "   OPERATION=" + operation);
		addLogs(LINE);
		addLogs("");
		addLogs("Commands sent to device " + deviceName + ":");
		this.deviceName=deviceName;
		this.operate=operation;
		this.service=service;
		this.serviceName=serviceName;
		this.serviceType=serviceType;
	}
	
	public void addLogHead(String str){
		addLogs("");
		addLogs(str);
		addLogs(LINE);
	}
	
	public void addDeviceName(String deviceName){
		addLogs("Commands sent to device " + deviceName + ":");
	}

	/**
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @param e
	 */
	public void setErrorInfo(String errorCode, String errorMessage, Exception e) {
		this.result = FAILURE;
		String str=ANAConstants.AnaErrorCode.getErrorMessage(errorCode, errorMessage, e);
		this.returnMessage=str;
		addUserRemark(str);
	}
	
	public void setErrorInfo(String errorCode, Object ... errorMessage){
		this.result = FAILURE;
		String str=ANAConstants.AnaErrorCode.getErrorMessage(errorCode, errorMessage);
		this.returnMessage=str;
		addUserRemark(str);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "ANAProcessResult [result=" + result + ", logs=" + logs
				+ ", username=" + username + ", ipaddress=" + ipaddress
				+ ", deviceName=" + deviceName + ", operate=" + operate
				+ ", service=" + service + ", serviceType=" + serviceType
				+ ", serviceName=" + serviceName + ", lastResponse="
				+ lastResponse + ", returnMessage=" + returnMessage
				+ ", authMessage=" + authMessage + "]";
	}

	
	
}
