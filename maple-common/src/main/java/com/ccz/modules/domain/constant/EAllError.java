package com.ccz.modules.domain.constant;

public enum EAllError {
	ok,
	NoServiceCode,
	NotAvailableServiceCode,

	//for EAddrError
	invalid_command,
	failed_search,
	invalid_search,
	empty_search,
	no_search_result,
	
	invalid_from_addressid,
	invalid_to_addressid,
	
	empty_goods_name,
	empty_goods_size,
	empty_goods_weight,
	empty_goods_type,
	empty_goods_price,
	empty_order_begintime,
	empty_order_endtime,
	empty_gpslist,
	failed_to_saveorder,
	
	invalid_offset_count,
	empty_order_list,
	no_order_data,
	
	not_exist_order,
	late_delivery_request,
	already_assigned_order,
	failed_assign_deliver,
	not_authorized_user,
	not_assigned_order,
	not_start_order,
	not_allowed_order,
	not_started_order,
	not_receipt_order,
	not_delivering_order,
	not_arrived_order,
	not_delivered_order,
	already_starting_order,
	failed_cancel_delivery_ready,
	already_occupied_order,
	failed_apply_order,
	failed_to_saveassign,
	failed_to_savestartmoving,
	failed_to_savebeforegotcha,
	failed_to_savegotcha,
	failed_to_savedelivering,
	failed_to_savebeforedelivered,
	failed_to_savedelivered,
	failed_to_saveconfirm,
	failed_to_cancelbysender,
	failed_to_cancelbydeliver,
	failed_to_updateordercancel,
	impossible_cancel_delivery,
	invalid_start_passcode,
	invalid_end_passcode,
	no_permission,
	not_exist_deliver,
	
	//EAdminError
	invalid_email_format,

	short_password_length_than_8,
	register_failed,

	/*for the add app*/
	AlreadyExistScode,
	NotExistScode,
	ScodeAllowedOnlyAlphabet,
	failed_to_create_app_database,
	invalid_db_parameter,
	failed_to_add_app,

	eWrongAccountInfo,
	
	eFailedToUpdateApp,
	
	eNoListData,
	eNotExistUser,
	
	/*for common*/
	mismatch_token_or_expired_token,
	
	//EAuthError
	wrong_appid,
	unknown_datatype,
	
	failed_register,
	ExistUserId,
	NotExistUserId,
	ExistPhoneNo,
	ExistEmail,
	appTokenNotExist,
	appTokenNotValidated,
	
	userIdMoreThan6Characters,
	userid_alphabet_and_digit,
	passwordMoreThan8Characters,
	
	invalid_phoneno_format,
	smscode_size_4,
	mismatch_smscode,
	
	invalid_app_token,
	invalid_user_token,
	ExpiredOrDifferentLoginToken,
	ExpiredLoginToken,
	ExpiredSigninToken,
	InvalidUUID,
	unauthorized_token,
	InvalidAdminToken,
	InvalidLoginToken,
	InvalidSigninToken,
	MightBeLeftUser,

	mismatch_token,
	NotExistUserAuth,
	not_exist_userinfo,
	not_exist_building,

	ExpiredAdminToken,
	invalid_or_expired_token,
	UnauthorizedUserId,
	UnauthorizedAnonymousUserId,
	InvalidUser,
	failed_email_verify,
	failed_phone_Verify,
	FailToChangePW,
	failed_update_token,
	mismatch_pw,
	WrongPassword,
	
	eNotExistIds,
	FailToCreateAnonymousAccount,
	//EBoardError
	NoSession,
	FailAddBoard,
	FailDeleteBoard,
	FailUpdate,
	FailAddReply,
	FailDeleteReply,
	FailAddVoteUser,
	FailDelVoteUser,
	NotExistLikedUser,
	NotExistDislikeUser,
	NotExistVoteUser,
	NotExistVoteInfo,
	AlreadyLiked,
	AlreadyDisliked,
	AlreadyVoteUser,
	AlreadyExpired,
	NoData,
	NoListData,
	InvalidParameter,
	InvalidCategoryId,
	PermissionDeny,
	WrongAptCode,
	
	//EChannelError, EFriendError
	eNoSession,
	eNoChannel,
	eNoData,
	eInvalidParameter,
	eFailToUpdate,

	//EDeliveryError same with EAddrError
	//..
	//EFileError
	complete,
	exception,
	commit_error,
	invalid_file_session,
	invalid_file_size,
	invalid_file_name,
	invalid_fileid,
	too_large_file,
	too_small_file,
	fail_to_uploadfile,
	fail_to_loadfile,
	fail_to_createfile,
	not_exist_fileinfo,
	
	//ELocationError
	invalid_orderid,
	mismatch_orderid_deliverid,

	//EMessageError
	eNoMessage,
	eAlreadyReadMessage,
	eFailToSaveMessage,
	eFailToDeleteMessage,
	eFailToUpdateChannel,

	eNoServiceCommand,

	unauthorized_user,
	UnauthorizedOrExpiredUser,
	//New
	FailToMakePooling,

	unknown_error
	

	
}
