/*
 * Copyright 2013-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.rules;

import com.google.common.base.Function;

import javax.annotation.Nullable;

/**
 * This is a union type that represents either a success or a failure. This exists so that
 * {@code com.facebook.buck.rules.CachingBuildEngine#buildOnceDepsAreBuilt()}
 * can return a strongly typed value.
 */
public class BuildResult {

  private final BuildRule rule;
  private final BuildRuleStatus status;
  private final CacheResult cacheResult;

  @Nullable private final BuildRuleSuccessType success;
  @Nullable private final Throwable failure;

  public static final Function<BuildResult, BuildRuleSuccessType> RULE_TO_SUCCESS =
      new Function<BuildResult, BuildRuleSuccessType>() {
        @Nullable
        @Override
        public BuildRuleSuccessType apply(BuildResult input) {
          return input.getSuccess();
        }
      };

  public BuildResult(BuildRule rule, BuildRuleSuccessType success, CacheResult cacheResult) {
    this.rule = rule;
    this.status = BuildRuleStatus.SUCCESS;
    this.cacheResult = cacheResult;
    this.success = success;
    this.failure = null;
  }

  /**
   * Note: This should only be used inside {@link CachingBuildEngine} and unit tests.
   */
  public BuildResult(BuildRule rule, Throwable failure) {
    this.rule = rule;
    this.status = BuildRuleStatus.FAIL;
    this.cacheResult = CacheResult.miss();
    this.success = null;
    this.failure = failure;
  }

  public BuildRule getRule() {
    return rule;
  }

  BuildRuleStatus getStatus() {
    return status;
  }

  CacheResult getCacheResult() {
    return cacheResult;
  }

  @Nullable
  public BuildRuleSuccessType getSuccess() {
    return success;
  }

  @Nullable
  Throwable getFailure() {
    return failure;
  }
}
