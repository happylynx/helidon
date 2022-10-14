/*
 * Copyright (c) 2022 Oracle and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.helidon.nima.webserver.http;

import java.util.function.Predicate;

import io.helidon.common.http.Http;
import io.helidon.common.http.HttpPrologue;
import io.helidon.common.http.PathMatcher;
import io.helidon.common.http.PathMatchers;

class HttpRouteImpl extends HttpRouteBase implements HttpRoute {
    private final Handler handler;
    private final Predicate<Http.Method> methodPredicate;
    private final PathMatcher pathMatcher;

    HttpRouteImpl(HttpRoute.Builder builder) {
        this.handler = builder.handler();
        this.methodPredicate = builder.methodPredicate();
        this.pathMatcher = builder.pathPredicate();
    }

    @Override
    public PathMatchers.MatchResult accepts(HttpPrologue prologue) {
        if (!methodPredicate.test(prologue.method())) {
            return PathMatchers.MatchResult.notAccepted();
        }

        return pathMatcher.match(prologue.uriPath());
    }

    @Override
    public Handler handler() {
        return handler;
    }

    @Override
    public void beforeStart() {
        handler.beforeStart();
    }

    @Override
    public void afterStop() {
        handler.afterStop();
    }

    @Override
    public String toString() {
        return methodPredicate + " (" + pathMatcher + "): " + handler;
    }
}
