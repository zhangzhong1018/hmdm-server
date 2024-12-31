/*
 *
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hmdm.persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hmdm.persistence.domain.Location;
import com.hmdm.persistence.mapper.LocationMapper;
import com.hmdm.security.SecurityContext;

import java.util.List;

/**
 * @author isv
 */
@Singleton
public class LocationDAO{

    private final UnsecureDAO unsecureDAO;
    private final UserDAO userDAO;
    private final LocationMapper mapper;

    @Inject
    public LocationDAO(UnsecureDAO unsecureDAO, UserDAO userDAO, LocationMapper mapper) {
        this.unsecureDAO = unsecureDAO;
        this.userDAO = userDAO;
        this.mapper = mapper;
    }

    public boolean hasAccess() {
        return SecurityContext.get().getCurrentUser().map(u -> {
            if (unsecureDAO.isSingleCustomer()) {
                if (!u.getUserRole().isSuperAdmin() && !userDAO.isOrgAdmin(u)) {
                    return false;
                }
            } else if (!u.getUserRole().isSuperAdmin()) {
                return false;
            }
            return true;
        }).orElse(false);
    }

    void checkAccess() {
        if (!hasAccess()) {
            throw new IllegalArgumentException("Operation not allowed");
        }
    }

    public List<Location> getAllLocations() {
        checkAccess();
        return mapper.findAll();
    }

    public List<Location> getLocationByCode(String code) {
        checkAccess();
        return mapper.findByCode(code);
    }

    public void insertLocation(Location location) {
        checkAccess();
        mapper.insert(location);
    }

    public void updateLocation(Location location) {
        checkAccess();
        mapper.update(location);
    }

    public void removeLocationById(Integer id) {
        checkAccess();
        mapper.delete(id);
    }

    public Location getLocationById(Integer id) {
        checkAccess();
        return mapper.findById(id);
    }

}
