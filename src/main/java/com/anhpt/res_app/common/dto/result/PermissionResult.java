package com.anhpt.res_app.common.dto.result;

import com.anhpt.res_app.common.enums.FeatureMethod;

public record PermissionResult (FeatureMethod method, String uri) {}
