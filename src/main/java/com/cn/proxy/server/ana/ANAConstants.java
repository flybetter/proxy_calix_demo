/** 
 * Project Name:CMS 
 * File Name:ANAConstants.java 
 * Package Name:com.calix.bseries.server.ana 
 * Date:14 Sep, 2016
 * Copyright (c) 2016, Calix All Rights Reserved. 
 * 
 */
package com.cn.proxy.server.ana;

import com.cn.proxy.server.ana.common.CommonStringUtils;

import java.util.HashMap;
import java.util.Map;



/**
 * ClassName:ANAConstants <br/>
 * Function: The constants for ana project <br/>
 * Date: 14 Sep, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 * @see
 */
public final class ANAConstants {
	/**
	 * default socket listen port
	 */
	public static final int DEFAULT_SOCKET_PORT = 9002;
	//boolean flags
	public static final int INT_TRUE=1;
	public static final int INT_FALSE=0;
	public static final String STR_TRUE="1";
	public static final String STR_FALSE="0";
	
	
	/**
	 * Template pojo package name
	 */
	public static final String TEMPLATE_PACKAGE_NAME = "com.calix.bseries.server.dbmodel";
	public static final String ROOT_DIR = ANAConstants.class.getResource("/")
			.getPath();
	public static final String ANA_SETTING_FILE = "com/calix/conf/ProxySettings.xml";


	public static final String ANA_PATH="/opt/proxy/ProxySettings.xml";
	// For B6 telnet
	public final static String DEFAULT_B6_TELECT_PASSWD = "occam";
	public final static String DEFAULT_ENABLE_COMMAND = "enable";
	public final static String DEFAULT_EN_PASSWORD = "razor";
	public final static String DEFAULT_TERMTYPE = "VT220";
	public final static int DEFAULT_TELNET_PORT = 23;
	public final static int DEFAULT_TIMEOUT = 1000;// 1 sec
	public final static int LONG_TIMEOUT = 1000 * 30;// 30 sec
	public final static int CLI_TIMEOUT = 1000 * 6;// 6 sec

	public final static String ANA_SOCKET_SOURCE = "M6";
	public final static String CALIX_DEVICE_PREF = "NTWK-";

	public static final int TURNUPTOOL_DEFAULT_SOCKET_PORT = 9000;
	public static final boolean TURNUPTOOL_DEFAULT_ISSSL = false;

	private final static String UNKNOWN_STR = "UNKNOWN";
	private final static int UNKNOWN_VAL = -99;

	/**
	 * ClassName: AnaErrorCode <br/>
	 * Function: TODO ADD FUNCTION. <br/>
	 * Reason: TODO ADD REASON <br/>
	 * date: 14 Sep, 2016 <br/>
	 * 
	 * @author Tony Ben
	 * @version ANAConstants
	 * @since JDK 1.6
	 */
	public enum AnaErrorCode {
		// 100100 Device is not available
		/**
		 * Device Not Exist
		 */
		DEVICE_NOT_EXIST("100100", "Device {0} is not exist"),
		/**
		 * Device Not Connect
		 */
		DEVICE_NOT_CONNECT("100101", "Device {0} is not active"),
		// 100200 Device Password is not right
		/**
		 * Password of telnet is not correct
		 */
		DEVICE_TELNET_PASSWD_NOT_CORRECT("100200",
				"The password {0} of device {1} for telnet is not correct"),
		/**
		 * Password of Enable command is not correct
		 */
		DEVICE_ENABLE_PASSWD_NOT_CORRECT("100201",
				"The password {0} of device {1} for enable is not correct"),
		// Template
		/**
		 * 
		 */
		TEMPLATE_NAME_EMPTY("200100", "The template name is empty"),
		/**
		 * 
		 */
		TEMPLATE_NOT_DEFINE("200101",
				"the template name {0} is not define, please use GUI to define"),

		// Request
		REQUEST_PATTERN_NOT_RIGHT("300100",
				"The request pattern is not contains <command name=''> ."),
		//
		PARAMETER_VALUE_INVALID("PARAMETER_VALUE_INVALID",
				"{0} is invalid, please check request."),
		//
		PARAMETER_SHOULD_BENUMBER("PARAMETER_SHOULD_BENUMBER",
				"{0} should be a number"),
		//
		NUMBER_SHOULD_INRANGE("NUMBER_SHOULD_INRANGE",
				"The input {0} should be within the range"), 
				//UNKNOWN_RESPONSE
				UNKNOWN_RESPONSE(
				"UNKNOWN_RESPONSE", "Exception"),
		// NE_NOT_SUPPORT_TEMPLATE
		NE_NOT_SUPPORT_TEMPLATE("NE_NOT_SUPPORT_TEMPLATE",
				"device {0} didn't support the tempalte"),
		// The input value for {0} should {1} {2}
		PARAM_SHOULD_BE("PARAM_SHOULD_BE",
				"The input value for {0} should {1} {2}"),
		// ------------------ANA DEFINE BEGIN---------------//
		/**
		 * <css style="color:red">NOT LOGIN</css>
		 */
		LOGIN_NOT_SUCCESSFUL("LOGIN_NOT_SUCCESSFUL",
				"username or password is not correct or Proxy has no CMS configured.\r\n"),
		/**
		 * <css style="color:green">PVC_SERVICE_INT_MANDATORY</css><br>
		 * <css style="color:green">Pvc_service_int is mandatory parameter
		 * ,please input Pvc_service_int !</css>
		 */
		PVC_SERVICE_INT_MANDATORY("PVC_SERVICE_INT_MANDATORY",
				"Pvc_service_int is mandatory parameter ,please input Pvc_service_int !"),
		/**
		 * <css style="color:red">Template Not Define</css>
		 */
		TEMPATE_NOT_FOUND("TEMPATE_NOT_FOUND", "template not found"),
		/**
		 * <css style="color:red">Max User Sessions Exceeded</css>
		 */
		MAX_USER_SESSIONS_EXCEEDED("MAX_USER_SESSIONS_EXCEEDED",
				"Number of open session for user root exceeded"),
		/**
		 * <css style="color:green">ADD OPERATION SUCCESSFUL</css><br>
		 * <css style="color:green">The Add Residential HSI service was
		 * successfully activated</css>
		 */
		HSI_ADD_OPERATION_SUCCESSFUL("ADD OPERATION SUCCESSFUL",
				"The Add Residential HSI service was successfully activated"),
		/**
		 * <css style="color:green">DEL OPERATION SUCCESSFUL</css><br>
		 * <css style="color:green">The Residential HSI service was successfully
		 * deleted</css><br>
		 */
		HSI_DEL_OPERATION_SUCCESSFUL("DEL OPERATION SUCCESSFUL",
				"The Residential HSI service was successfully deleted"),
		/**
		 * <css style="color:green">MODIFY OPERATION SUCCESSFUL</css><br>
		 * <css style="color:green">The Residential HSI service was successfully
		 * modified</css><br>
		 */
		HSI_MODIFY_OPERATION_SUCCESSFUL("MODIFY OPERATION SUCCESSFUL",
				"The Residential HSI service was successfully modified"),
		/**
		 * <css style="color:green">ADD BONDED OPERATION SUCCESSFUL</css><br>
		 * <css style="color:green">Add Bonded DSL service successfully
		 * executed</css><br>
		 */
		ADD_BONDED_OPERATION_SUCCESSFUL("ADD BONDED OPERATION SUCCESSFUL",
				"Add Bonded DSL service successfully executed"),
		/**
		 * <css style="color:green">DEL OPERATION SUCCESSFUL</css><br>
		 * <css style="color:green">Delete Bonded DSL service successfully
		 * executed </css><br>
		 */
		DEL_BONDED_OPERATION_SUCCESSFUL("DEL OPERATION SUCCESSFUL",
				"Delete Bonded DSL service successfully executed"),
		/**
		 * <css style="color:red">INVALID_OPERATION</css><br>
		 * <css style="color:red">The input value for operation should be
		 * ADD/MOD/DEL</css>
		 */
		INVALID_OPERATION("INVALID_OPERATION",
				"The input value for operation should be ADD/MOD/DEL"),
		/**
		 * <css style="color:red">NE_NOT_EXISTING</css><br>
		 * <css style="color:red">VNE Does Not Exist</css>
		 */
		NE_NOT_EXISTING("NE_NOT_EXISTING", "VNE Does Not Exist"),
		/**
		 * <css style="color:red">CONNECTION_ERROR</css><br>
		 * <css style="color:red">Exception: Error! Connection/Protocol
		 * failure</css>
		 */
		CONNECTION_ERROR("CONNECTION_ERROR",
				"Exception: Error! Connection/Protocol failure"),
		/**
		 * <css style="color:red">INVALID_DSL_PROFILE</css><br>
		 * <css style="color:red">DSL Profile does not exist</css>
		 */
		INVALID_DSL_PROFILE("INVALID_DSL_PROFILE", "DSL Profile does not exist"),
		/**
		 * <css style="color:red">INVALID_ACCESS_PROFILE</css><br>
		 * <css style="color:red">Access Profile does not exist</css>
		 */
		INVALID_ACCESS_PROFILE("INVALID_ACCESS_PROFILE",
				"Access Profile does not exist"),
		/**
		 * <css style="color:red">INVALID_KEYINFO_FORMAT</css><br>
		 * <css style="color:red">key_info input should contain two Strings with
		 * a space</css>
		 */
		INVALID_KEYINFO_FORMAT("INVALID_KEYINFO_FORMAT",
				"key_info input should contain two Strings with a space"),
		/**
		 * <css style="color:red">KEYINFO_EVALUATION_FAILED</css><br>
		 * <css style="color:red">Modify_HSI :Attribute <key_info_old>
		 * evaluation failed</css>
		 */
		KEYINFO_EVALUATION_FAILED("KEYINFO_EVALUATION_FAILED",
				"Modify_HSI :Attribute <key_info_old> evaluation failed"),
		/**
		 * <css style="color:red">INVALID_VPI_VCI_FORMAT</css><br>
		 * <css style="color:red">vpi/vci format is invalid</css>
		 */
		INVALID_VPI_VCI_FORMAT("INVALID_VPI_VCI_FORMAT",
				"vpi/vci format is invalid"),
		/**
		 * <css style="color:red">PORT_ALREADY_IN_USE</css><br>
		 * <css style="color:red">DSL interface is already in use</css>
		 */
		PORT_ALREADY_IN_USE("PORT_ALREADY_IN_USE",
				"DSL interface is already in use"),
		/**
		 * <css style="color:red">SECOND_PORT_ALREADY_IN_USE</css><br>
		 * <css style="color:red">Second DSL interface is already in use</css>
		 */
		SECOND_PORT_ALREADY_IN_USE("SECOND_PORT_ALREADY_IN_USE",
				"Second DSL interface is already in use"),
		/**
		 * <css style="color:red">PORT_NOT_EXISTING</css><br>
		 * <css style="color:red">DSL interface does not exist</css>
		 */
		PORT_NOT_EXISTING("PORT_NOT_EXISTING", "DSL interface does not exist"),
		/**
		 * <css style="color:red">SECOND_PORT_NOT_EXISTING</css><br>
		 * <css style="color:red">Second DSL interface does not exist</css>
		 */
		SECOND_PORT_NOT_EXISTING("SECOND_PORT_NOT_EXISTING",
				"Second DSL interface does not exist"),
		/**
		 * <css style="color:red">SERVICE_NOT_EXISTING</css><br>
		 * <css style="color:red">Service does not exist</css>
		 */
		SERVICE_NOT_EXISTING("SERVICE_NOT_EXISTING", "Service does not exist"),
		/**
		 * <css style="color:red">BONDING_GROUP_ALREADY_IN_USE</css><br>
		 * <css style="color:red">Bonding Group is already in use</css>
		 */
		BONDING_GROUP_ALREADY_IN_USE("BONDING_GROUP_ALREADY_IN_USE",
				"Bonding Group is already in use"),
		/**
		 * <css style="color:red">BONDING_GROUP_NOT_EXISTING</css><br>
		 * <css style="color:red">Bonding Group does not exist</css>
		 */
		BONDING_GROUP_NOT_EXISTING("BONDING_GROUP_NOT_EXISTING",
				"Bonding Group does not exist"),
		/**
		 * <css style="color:red">SERVICE_NOT_EXISTING</css><br>
		 * <css style="color:red">No Service configured on  port</css>
		 */
		NO_SERVICE_CONFIG_ON_PORT("SERVICE_NOT_EXISTING",
				"No Service configured on port"),
		/**
		 * <css style="color:red">ROLLBACK_SUCCESS</css><br>
		 * <css style="color:red">Rollback successfully executed upon
		 * failure</css>
		 */
		ROLLBACK_SUCCESS("ROLLBACK_SUCCESS",
				"Rollback successfully executed upon failure"),
		/**
		 * <css style="color:red">FIRST_INTF_NOT_MAPPED_TO_BG</css><br>
		 * <css style="color:red">First DSL Interface does not belong to the
		 * Bonding Group</css>
		 */
		FIRST_INTF_NOT_MAPPED_TO_BG("FIRST_INTF_NOT_MAPPED_TO_BG",
				"First DSL Interface does not belong to the Bonding Group"),
		/**
		 * <css style="color:red">SECOND_INTF_NOT_MAPPED_TO_BG</css><br>
		 * <css style="color:red">Second DSL Interface does not belong to the
		 * Bonding Group</css>
		 */
		SECOND_INTF_NOT_MAPPED_TO_BG("SECOND_INTF_NOT_MAPPED_TO_BG",
				"Second DSL Interface does not belong to the Bonding Group"),
		/**
		 * <css style="color:red">ROLLBACK_FAILED</css><br>
		 * <css style="color:red">Rollback failed. Please activate
		 * manually</css>
		 */
		ROLLBACK_FAILED("ROLLBACK_FAILED",
				"Rollback failed. Please activate manually"),
		/**
		 * <css style="color:red">UNKNOWN_ERROR</css><br>
		 */
		UNKNOWN_ERROR("UNKNOWN_ERROR", "")

		;
		// ------------------ANA DEFINE END---------------//
		/**
		 * ErrorCode is not define
		 */
		private static final String ERROR = "This ErrorCode {0} is not define .";
		/**
		 * ErrorCode
		 */
		private String errorCode;
		/**
		 * ErrorCode Details Message
		 */
		private String errorMessage;

		// contains all the errorCode
		private static Map<String, String> errorCodeMap = null;
		// sync lock
		private static String lock = "";

		/**
		 * Creates a new instance of AnaErrorCode.
		 * 
		 * @param errorCode
		 * @param errorMessage
		 */
		private AnaErrorCode(String errorCode, String errorMessage) {
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		/**
		 * Get Error Messages Via ErrorCode
		 * 
		 * @param errorCode
		 * @param objects
		 * @return
		 */
		public static String getErrorMessage(String errorCode,
				Object... objects) {
			init();
			String msg = errorCodeMap.get(errorCode);
			msg = msg == null ? ERROR : msg;
			return CommonStringUtils.replaceStringCodes(msg, objects);
		}

		/**
		 * Initial
		 */
		private static void init() {
			synchronized (lock) {
				if (errorCodeMap == null) {
					errorCodeMap = new HashMap<String, String>();
					for (AnaErrorCode c : AnaErrorCode.values()) {
						errorCodeMap.put(c.getErrorCode(), c.getErrorMessage());
					}
				}
			}
		}

		public String getErrorCode() {
			return errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		@Override
		public String toString() {
			return this.errorCode;
		}
	}

	/**
	 * ClassName: Result <br/>
	 * Function: the result of operation <br/>
	 * Date: 14 Sep, 2016 <br/>
	 * 
	 * @author Tony Ben
	 * @version 1.0
	 * @since JDK 1.6
	 */
	public enum Result {
		/**
		 * Success
		 */
		SUCCESS("SUCCESS", 1),
		/**
		 * Failure
		 */
		FAILURE("FAILURE", -1),
		/**
		 * In process
		 */
		INPROCESS("INPROCESS", 0),
		/**
		 * Unknown
		 */
		UNKNOWN("UNKNOWN", -99);
		// name
		private String name;
		// value
		private int value;

		/**
		 * default constructor of enum Result.
		 * 
		 * @param name
		 * @param value
		 */
		private Result(String name, int value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * Function:get operation name by value<br/>
		 * 
		 * @author Tony Ben
		 * @param value
		 * @return
		 * @since JDK 1.6
		 */
		public static String getName(int value) {
			for (Result o : Result.values()) {
				if (o.getValue() == value) {
					return o.getName();
				}
			}
			return UNKNOWN_STR;
		}

		/**
		 * Function:get operation value via operation desc<br/>
		 * Conditions:TODO<br/>
		 * WorkFlow:TODO<br/>
		 * UserGuide:TODO<br/>
		 * Remark:TODO<br/>
		 * 
		 * @author Tony Ben
		 * @param name
		 * @return
		 * @since JDK 1.6
		 */
		public static int getValue(String name) {
			if (name == null || "".equals(name.trim())) {
				return UNKNOWN_VAL;
			}
			for (Result o : Result.values()) {
				if (name.equalsIgnoreCase(o.getName())) {
					return o.getValue();
				}
			}
			return UNKNOWN_VAL;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	/**
	 * 
	 * ClassName: Operation <br/>
	 * Function: the operation of execute command <br/>
	 * Date: 14 Sep, 2016 <br/>
	 * 
	 * @author Tony Ben
	 * @version 1.0
	 * @since JDK 1.6
	 */
	public enum Operation {
		/**
		 * Add
		 */
		ADD("ADD", 1),
		/**
		 * Modify
		 */
		MOD("MODIFY", 2),
		/**
		 * Delete
		 */
		DEL("DELETE", 3),
		/**
		 * query
		 */
		QUERY("QUERY", 4),
		/**
		 * unknown
		 */
		UNKNOWN("UNKNOWN", -99);
		// Name
		private String name;
		// Value
		private int value;

		private Operation(String name, int value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * Function:get operation name by value<br/>
		 * 
		 * @author Tony Ben
		 * @param value
		 * @return
		 * @since JDK 1.6
		 */
		public static String getName(int value) {
			for (Operation o : Operation.values()) {
				if (o.getValue() == value) {
					return o.getName();
				}
			}
			return UNKNOWN_STR;
		}

		/**
		 * Function:get operation value via operation desc<br/>
		 * Conditions:TODO<br/>
		 * WorkFlow:TODO<br/>
		 * UserGuide:TODO<br/>
		 * Remark:TODO<br/>
		 * 
		 * @author Tony Ben
		 * @param name
		 * @return
		 * @since JDK 1.6
		 */
		public static int getValue(String name) {
			if (name == null || "".equals(name.trim())) {
				return UNKNOWN_VAL;
			}
			for (Operation o : Operation.values()) {
				if (name.equalsIgnoreCase(o.getName())) {
					return o.getValue();
				}
			}
			return UNKNOWN_VAL;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return this.name;
		}

	}
}
