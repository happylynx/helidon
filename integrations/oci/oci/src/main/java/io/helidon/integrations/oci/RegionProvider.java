/*
 * Copyright (c) 2024 Oracle and/or its affiliates.
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

package io.helidon.integrations.oci;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import io.helidon.integrations.oci.spi.OciRegion;
import io.helidon.service.registry.Service;

import com.oracle.bmc.Region;

@Service.Provider
@Service.ExternalContracts(Region.class)
class RegionProvider implements Supplier<Region> {
    private final List<OciRegion> regionProviders;

    RegionProvider(List<OciRegion> regionProviders) {
        this.regionProviders = regionProviders;
    }

    @Override
    public Region get() {
        for (OciRegion regionProvider : regionProviders) {
            Optional<Region> region = regionProvider.region();
            if (region.isPresent()) {
                return region.get();
            }
        }
        return null;
    }
}
