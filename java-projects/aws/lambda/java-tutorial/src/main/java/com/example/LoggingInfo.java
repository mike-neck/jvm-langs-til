/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example;

import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class LoggingInfo {

    private final String awsRequestId;
    private final String identityId;
    private final String identityPoolId;
    private final String functionName;
    private final String functionVersion;
    private final String invokedFunctionArn;
    private final String logGroupName;
    private final String logStreamName;
    private final String zoneId;
    private final Long seed;

    static LoggingInfo of(Context context, Input input) {
        final CognitoIdentity identity = context.getIdentity();
        return new LoggingInfo(context.getAwsRequestId(), identity.getIdentityId(), identity.getIdentityPoolId(),
                context.getFunctionName(), context.getFunctionVersion(), context.getInvokedFunctionArn(),
                context.getLogGroupName(), context.getLogStreamName(), input.getZoneId(), input.getSeed());
    }
}
