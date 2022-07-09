package com.papatalk.server.common.format;

import com.papatalk.server.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@AllArgsConstructor
@Data
public class ApiResponse {
    String resultCode;  // required
    String resultMsg;   // required
    HashMap resultData;

    public ApiResponse() {
        this.resultCode = Constants.RESULT_CODE_OK;
        this.resultMsg = Constants.RESULT_MSG_OK;
    }
}
